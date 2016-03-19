package gitvital.process;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import gitvital.api.CustUtil;
import gitvital.mongo.MongoAccess;

public class Feature {

	private MongoCollection<Document> rawDb;
	private MongoCollection<Document> dataSet;
	private MongoCursor<Document> mcur;

	private String[] commonLanguages = { "JavaScript", "Java", "Python", "Ruby", "PHP", "C", "C++", "CSS", "HTML",
			"Objective-C", "C#" };
	private List<String> commonLanguageList = null;

	private int dayThreshold = 182;

	public Feature() {
		commonLanguageList = Arrays.asList(commonLanguages);
		rawDb = MongoAccess.db.getCollection("rawRepos");
		dataSet = MongoAccess.db.getCollection("dataSet");
		mcur = rawDb.find().iterator();
	}
	
	public Document processInstance(Document read){
		Document row = new Document();


		// Copy Identifiers
		row.put("gitId", read.get("id"));
		String repoName = read.getString("full_name");


		row.put("gitName", repoName);

		// Direct Copy
		row.put("SizeKb", read.get("size"));
		row.put("readmeSize", read.get("readmeSize"));
		row.put("isFork", read.get("fork"));
		row.put("stargazers", read.get("stargazers_count"));
		row.put("watchers", read.get("watchers_count"));
		
		String language = read.getString("language");
		if(language == null || language.length() < 1){
			language = "Not Specified";
		}else if(!commonLanguageList.contains(language)){
			language = "Other";
		};
		
		row.put("language", language);
		row.put("has_issues", read.get("has_issues"));
		row.put("has_downloads", read.get("has_downloads"));
		row.put("has_wiki", read.get("has_wiki"));
		row.put("has_pages", read.get("has_pages"));
		row.put("forks_count", read.get("forks_count"));
		row.put("open_issues_count", read.get("open_issues_count"));
		row.put("watchers", read.get("watchers"));
		row.put("isDefaultMaster", read.getString("default_branch").equals("master"));
		row.put("score", read.get("score"));

		// Create Features
		String description = read.getString("description");
		row.put("DescLen", description.length());

		ArrayList<Document> contributors = null;
		try {
			contributors = (ArrayList<Document>) read.get("contributors");
		} catch (ClassCastException e) {
			e.printStackTrace();
		}

		if (contributors != null) {
			long numOfContributors = contributors.size();

			long totalContributions = 0;
			for (Object c : contributors) {
				Document doc = (Document) c;
				totalContributions += doc.getInteger("contributions");
			}
			double avgContributions = 0;
			try {
				avgContributions = totalContributions / numOfContributors;
			} catch (ArithmeticException e) {
				avgContributions = -1;
			}

			row.put("numOfContributors", numOfContributors);
			row.put("totalContributions", totalContributions);
			row.put("avgContributions", avgContributions);
		}

		// 1. Days since creations
		addDays(read, row, "created_at", "daysCreated");

		// 2. Days since updated
		long updated = addDays(read, row, "updated_at", "daysUpdated");

		// 3. Days since last push
		addDays(read, row, "pushed_at", "daysPushed");

		row.put("isActive", updated > -1 && updated < dayThreshold);
		return row;
	}

	@SuppressWarnings("unchecked")
	public void createFromDB() {

		long cnt = 0;

		while (mcur.hasNext()) {

			System.out.println("Start #" + cnt);
			Document row = processInstance(mcur.next());

			cnt++;

			System.out.println("----Adding----");
			System.out.println(row.toJson());

			// Upsert
			MongoAccess.upsert(dataSet, "gitId", row);
			System.out.println("---Completed---");
		}
	}

	public long addDays(Document read, Document write, String readCol, String writeCol) {
		String dateStr = read.getString(readCol);
		if (dateStr != null && dateStr.length() > 0) {
			long days = CustUtil.daysFromStr(dateStr);
			write.put(writeCol, days);
			return days;
		}
		return -1;
	}

	public static void main(String[] args) {
		MongoAccess.init();

		Feature creator = new Feature();
		creator.createFromDB();

		MongoAccess.close();
	}
}
