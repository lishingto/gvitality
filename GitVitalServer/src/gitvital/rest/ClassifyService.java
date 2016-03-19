package gitvital.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import gitvital.api.CustUtil;
import gitvital.mongo.MongoAccess;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

@Path("classify")
public class ClassifyService {

	private static String[] attributes = { "SizeKb", "readmeSize", "stargazers", "language", "forks_count",
			"open_issues_count", "DescLen", "numOfContributors", "isActive" };

	public static List<Attribute> filterAttributes() {
		ArrayList<Attribute> allList = ServerService.weka.getAttributes();
		ArrayList<Attribute> filList = new ArrayList<Attribute>();
		for (Attribute a : allList) {
			for (String s : attributes) {
				if (a.name().equals(s)) {
					filList.add(a);
					break;
				}
			}
		}
		return filList;
	}

	public List<Document> classify(List<Document> docList) {
		ArrayList<Document> outList = new ArrayList<Document>();
		Instances inData = new Instances("single", CustUtil.listToFv(ServerService.classifyAttributes), 0);
		MongoCollection<Document> cache = MongoAccess.db.getCollection("cache");
		for (Document doc : docList) {
			Document data = ServerService.featureCreator.processInstance(doc);
			outList.add(data);

			// Create this instance
			Instance point = new Instance(attributes.length);
			for (Attribute a : ServerService.classifyAttributes) {
				if (!a.name().equals("isActive")) {
					Object obj = data.get(a.name());
					if (a.isNominal()) {
						point.setValue(a, obj.toString());
					} else if (a.isNumeric()) {
						if (obj instanceof Double)
							point.setValue(a, (double) obj);
						else if (obj instanceof Long)
							point.setValue(a, (long) obj);
						else
							point.setValue(a, (int) obj);
					}
				} else {
					point.setMissing(a);
				}
			}
			inData.add(point);
			//data.put("isActive", outData.firstInstance().toString(outData.classAttribute()));
		}

		Instances outData = ServerService.weka.loadModel("/WEB-INF/models/PART-Opt.model", inData);
		
		for(int i = 0; i < outData.numInstances(); i++){
			Instance in = outData.instance(i);
			Document out = outList.get(i);
			out.put("isActive", in.toString(outData.classAttribute()));
			
			//Store in cache
			MongoAccess.upsert(cache, "gitId", out);
		}
		return outList;
	}
	
	@Path("/cache/{gitId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getCache(@PathParam("giId") String gitId){
		JSONObject json = new JSONObject();
		MongoCollection<Document> cache = MongoAccess.db.getCollection("cache");
		MongoCursor<Document> cur = cache.find(new Document("gitId", gitId)).iterator();
		
		if(cur.hasNext()){
			Document doc = cur.next();
			json.append("classify", doc);
		}
		
		return json.toString();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postClassify(String data){
		JSONObject json = new JSONObject();
		
		JSONArray array = new JSONArray(data);

		ArrayList<Document> inList = new ArrayList<Document>();
		
		for(int i = 0; i < array.length(); i++){
			Document doc = null;
			String repoName = array.getString(i);
			try {
				doc = ServerService.crawler.webProcess(repoName);
				inList.add(doc);
			} catch (IOException e) {
				System.out.println("Unable to find " + repoName);
				return "{\"Error\":\"Unable to find " + repoName + "\"}";
			}
		}

		List<Document> output = classify(inList);
		
		for(Document o : output){
			for(Document n : inList){
				if(o.getInteger("gitId").equals(n.getInteger("id"))){
					o.append("repo", n);
					break;
				}
			}
		}
		
		json.put("classified", output);
		return json.toString();
	}

	@Path("/{owner}/{repo}")
	@GET
	@Produces("application/json")
	public String classify(@PathParam("owner") String owner, @PathParam("repo") String repo) throws JSONException {
		String repoName = owner + "/" + repo;
		Document doc = null;
		JSONObject json = null;
		try {
			doc = ServerService.crawler.webProcess(repoName);
		} catch (IOException e) {
			System.out.println("Unable to find " + repoName);
			return "{\"Error\":\"Unable to find " + repoName + "\"}";
		}
		
		ArrayList<Document> inList = new ArrayList<Document>();
		inList.add(doc);
		List<Document> output = classify(inList);
		
		json = new JSONObject();
		json.append("repo", doc);
		json.append("classify", output.get(0));

		System.out.println("Classified " + owner + "/" + repoName + " as " + output.get(0).get("isActive").toString());
		return json.toString();
	}
}
