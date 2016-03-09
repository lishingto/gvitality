package gvitality.process;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import gvitality.mongo.MongoAccess;

public class ArffHandler extends BaseFileHandler {

	private MongoCollection<Document> db;
	private MongoCollection<Document> attrDb;
	private LinkedHashMap<String,String> attrList = new LinkedHashMap<String,String>();	

	public ArffHandler(String loadDb) {
		db = MongoAccess.db.getCollection(loadDb);
		
		//Load Attribute Map
		attrDb = MongoAccess.db.getCollection(loadDb);		
		MongoCursor<Document> acur = attrDb.find().iterator();
		while(acur.hasNext()){
			Document adoc = acur.next();
			attrList.put(adoc.getString("label"), adoc.getString("type"));
		}
	}

	public void writeArff() {
		LinkedList<String> contents = new LinkedList<String>();
		MongoCursor<Document> mcur = db.find().iterator();

		int cnt = 0;

		
		//Write header
		contents.add("@RELATION gitvital");
		
		//Add attributes
		
		for(Entry<String, String> entry : attrList.entrySet()){
			contents.add("@ATTRIBUTE " + entry.getKey() + " " + entry.getValue());
		}

		while (mcur.hasNext()) {
			Document doc = mcur.next();

			Set<Entry<String, Object>> eSet = doc.entrySet();
			for (Entry<String, Object> entry : eSet) {
				// Init labels
				
				Object obj = entry.getValue();
				
				//print data row
			}

			cnt++;
		}
	}
	
	public static void main(String[] args) {
		MongoAccess.init();
		ArffHandler arff = new ArffHandler("dataSet");
	}
}
