package gvitality.mongo;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoAccess {

	public static MongoDatabase db;

	public static void createCollections() {
		String[] colList = { "rawRepos", "dataSet" };

		for (String s : colList) {
			if (db.getCollection(s) == null) {
				db.createCollection(s);
			}
		}
	}
	
	public static void upsert(MongoCollection<Document> colle, String matchCol, Document upsertDoc){
		Document findId = new Document();
		findId.append(matchCol, upsertDoc.get(matchCol));
		if (colle.findOneAndUpdate(findId, new Document("$set", upsertDoc)) == null) {
			colle.insertOne(upsertDoc);
		}
	}

	public static void init(){
			try{
				
		         // To connect to mongodb server
		         MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
					
		         // Now connect to your databases
		         db = mongoClient.getDatabase("gvital");
		         System.out.println("Connect to database successfully");	 
					
		         createCollections();
		      }catch(Exception e){
		         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      }	
		
		}
}
