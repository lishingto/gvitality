package gvitality.api;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;

import com.google.api.client.http.HttpRequest;
import com.mongodb.BasicDBList;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import gvitality.api.parse.Status;
import gvitality.mongo.MongoAccess;

public class ExtraInfoCrawler extends BaseService{	

	
	private MongoCollection<Document> rawDb;
	private MongoCursor<Document> mcur;
	
	public ExtraInfoCrawler() throws Exception{
		super();
		
		rawDb = MongoAccess.db.getCollection("rawRepos");
		mcur = rawDb.find().iterator();
	}
	
	public void process(Document doc) throws IOException{

		//Do stuff
		String repoName = doc.getString("full_name");
		System.out.println("Servicing: " + repoName);
		
		// Activity Data
		GithubUrl statsUrl = new GithubUrl("https://api.github.com/repos/" + repoName + "/stats/commit_activity");
		HttpRequest statsReq = requestFactory.buildGetRequest(statsUrl);
		StatusList stats = statsReq.execute().parseAs(StatusList.class);
		if (stats != null) {
			BasicDBList activityList = new BasicDBList();
			for (Status s : stats) {
				Document activity = new Document();

				//activity.put("days", s.days);
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
			doc.append("activity",activityList);

		}else{
			System.out.println(repoName + " has no Activity");
		}
		

		rawDb.findOneAndUpdate(new Document("_id", doc.get("_id")), new Document( "$set", doc));
	}
	
	public void run(){
		//TODO: 1. Populate Data Structure from DB
		//TODO: 2. Read data and crawl API
		//TODO: 3. Append new information into API
		//TODO: 4. Tea Timer for running this code
		
		long cnt = 0;
		while(mcur.hasNext()){
			try {
				process(mcur.next());
			} catch (IOException e) {
				e.printStackTrace();
			}		
			cnt++;
		}
	}
	
	public static void main(String[] args){
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
