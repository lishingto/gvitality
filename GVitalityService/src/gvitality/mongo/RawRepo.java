package gvitality.mongo;

import java.util.LinkedList;
import java.util.List;

import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.Document;

import com.google.api.client.util.Key;
import com.mongodb.client.MongoCollection;

import gvitality.api.parse.FullRepo;
import gvitality.api.parse.Owner;
import gvitality.api.parse.Repository;
import gvitality.api.parse.Status;

public class RawRepo {
	private Document doc;

	public RawRepo(FullRepo r) {
		doc = new Document();
		doc.append("id", r.id);
		doc.append("ownerLogin", r.owner.login);
		doc.append("ownerId", r.owner.id);
		doc.append("name", r.name);
		doc.append("full_name", r.full_name);
		doc.append("description", r.description);
		doc.append("fork", r.fork);
		doc.append("url", r.url);
		doc.append("created_at", r.created_at);
		doc.append("updated_at", r.updated_at);
		doc.append("pushed_at", r.pushed_at);
		doc.append("size", r.size);
		doc.append("stargazers_count", r.stargazers_count);
		doc.append("watchers_count", r.watchers_count);
		doc.append("language", r.language);
		doc.append("has_issues", r.has_issues);
		doc.append("has_downloads", r.has_downloads);
		doc.append("has_wiki", r.has_wiki);
		doc.append("has_pages", r.has_pages);
		doc.append("forks_count", r.forks_count);
		doc.append("open_issues_count", r.open_issues_count);
		doc.append("forks", r.forks);
		doc.append("open_issues", r.open_issues);
		doc.append("watchers", r.watchers);
		doc.append("default_branch", r.default_branch);
		doc.append("score", r.score);		
	}

	public void save() {
		MongoCollection<Document> raw = MongoAccess.db.getCollection("rawRepos");
		Document findId = new Document();
		findId.append("id", doc.get("id"));
		if (raw.findOneAndUpdate(findId, new Document("$set", doc)) == null) {
			raw.insertOne(doc);
		}
	}

}
