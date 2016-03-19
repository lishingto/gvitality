package gitvital.rest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.util.JSON;

import gitvital.mongo.MongoAccess;

@Path("stats")
public class StatsService {
	private MongoCollection<Document> dataset = MongoAccess.db.getCollection("dataSet");;

	private int perPage = 20;

	@GET
	@Path("/dataset/count")
	@Produces(MediaType.APPLICATION_JSON)
	public String datasetCount() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		long size = dataset.count();
		jsonObject.put("count", size);
		return jsonObject.toString();
	}

	@GET
	@Path("/dataset/stats")
	@Produces(MediaType.APPLICATION_JSON)
	public String datasetStats() throws JSONException {
		JSONObject jsonObject = new JSONObject();

		List<Document> avgCont = new LinkedList<Document>();
		avgCont.add(new Document("$group",
				new Document("_id", 0).append("avgContributors", new Document("$avg", "$numOfContributors"))
				.append("avgSizeKb", new Document("$avg", "$SizeKb"))
				.append("avgStargazers", new Document("$avg", "$stargazers"))
				.append("avgForks", new Document("$avg", "$forks_count"))
				.append("avgReadmeSize", new Document("$avg", "$readmeSize"))
				.append("avgDescLen", new Document("$avg", "$DescLen"))
				.append("avgOpenIssues", new Document("$avg", "$open_issues_count"))
		));

		jsonObject.put("averages", dataset.aggregate(avgCont).first());

		return jsonObject.toString();
	}

	@GET
	@Path("/dataset/{perPage:[0-9]*}/{pageNo:[0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public String dataset(@PathParam("perPage") int perPage, @PathParam("pageNo") int pageNo) throws JSONException {
		JSONArray jsonArray = new JSONArray();
		ArrayList<Document> pageList = new ArrayList<Document>();
		FindIterable<Document> mcur = dataset.find().skip(pageNo * perPage).limit(perPage);
		for (Document d : mcur) {
			pageList.add(d);
		}
		jsonArray = new JSONArray(JSON.serialize(pageList));
		return jsonArray.toString();
	}
}
