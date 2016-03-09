package gvitality.api;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponseException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBList;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import gvitality.api.parse.Contributor;
import gvitality.api.parse.Status;
import gvitality.mongo.MongoAccess;

public class ExtraInfoCrawler extends BaseService {

	private MongoCollection<Document> rawDb;
	private MongoCursor<Document> mcur;
	private long startTime;
	private long renewDelay = 600000;

	public ExtraInfoCrawler() throws Exception {
		super();

		rawDb = MongoAccess.db.getCollection("rawRepos");
		renew();
	}

	public void renew() {
		startTime = System.currentTimeMillis();
		mcur = rawDb.find(new Document("readmeSize", new Document("$exists", false))).iterator();
	}

	public void requestContributors(String repoName, Document doc) throws IOException {
		GithubUrl contUrl = new GithubUrl("https://api.github.com/repos/" + repoName + "/contributors");
		HttpRequest contReq = requestFactory.buildGetRequest(contUrl);

		ContributorList contList = contReq.execute().parseAs(ContributorList.class);
		if (contList != null) {

			BasicDBList contributorDBlist = new BasicDBList();
			for (Contributor c : contList) {
				Document curCont = new Document();
				curCont.append("login", c.login).append("id", c.id).append("type", c.type)
						.append("site_admin", c.site_admin).append("contributions", c.contributions);

				contributorDBlist.add(curCont);
			}
			doc.append("contributors", contributorDBlist);
		}
	}

	public void requestReadme(String repoName, Document doc) throws IOException {

		Gson gson = new Gson();
		GithubUrl readmeUrl = new GithubUrl("https://api.github.com/repos/" + repoName + "/contents/README.md");
		HttpRequest readmeReq = requestFactory.buildGetRequest(readmeUrl);

		Type type = new TypeToken<Map<String, Object>>() {
		}.getType();

		long size = 0;
		try {
			Map<String, Object> readmeResponse = gson.fromJson(readmeReq.execute().parseAsString(), type);
			size = Math.round((double) readmeResponse.get("size"));
		} catch (HttpResponseException e) {
			e.printStackTrace();
		}

		doc.append("readmeSize", size);
	}

	public void requestActivity(String repoName, Document doc) throws IOException {
		GithubUrl statsUrl = new GithubUrl("https://api.github.com/repos/" + repoName + "/stats/commit_activity");
		HttpRequest statsReq = requestFactory.buildGetRequest(statsUrl);
		StatusList stats = statsReq.execute().parseAs(StatusList.class);
		if (stats != null) {
			BasicDBList activityList = new BasicDBList();

			for (Status s : stats) {
				Document activity = new Document();

				// activity.put("days", s.days);
				activity.put("total", s.total);
				activity.put("week", s.week);
				System.out.println("Activity Week: " + s.week + " | total: " + s.total);
				if (s.total > 0) {
					System.out.print("# Commits / Day: ");
					String[] days = { "SU", "MO", "TU", "WD", "TH", "FR", "ST" };
					int day = 0;
					for (int d : s.days) {
						System.out.print(", " + days[day] + ":" + d);
						day++;
					}
				}
				activityList.add(activity);
			}
			doc.append("activity", activityList);

		} else {
			System.out.println(repoName + " has no Activity");
		}
	}

	public void process(Document doc) throws IOException {

		// Do stuff
		String repoName = doc.getString("full_name");
		System.out.println("Servicing: " + repoName);

		// Activity Data
		requestActivity(repoName, doc);

		// Get Contributors
		requestContributors(repoName, doc);

		// README Size
		requestReadme(repoName, doc);

		rawDb.findOneAndUpdate(new Document("_id", doc.get("_id")), new Document("$set", doc));
	}

	public boolean run() throws IOException {	
		
		long estimatedTime = System.currentTimeMillis() - startTime;
		if(estimatedTime >= renewDelay ){
			renew();
		}
		
		if (!mcur.hasNext())
			return false;

		Document doc = mcur.next();
		
		// Process only if key field is missing
		if (doc != null) {
			process(doc);
		}
		return true;
	}

	public static void main(String[] args) {
		MongoAccess.init();
		ExtraInfoCrawler service;
		try {
			service = new ExtraInfoCrawler();
			service.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
