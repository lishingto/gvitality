package gvitality.mahout;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.classifier.naivebayes.training.TrainNaiveBayesJob;
import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import gvitality.mongo.MongoAccess;

public class ModelService {
	

	private MongoCollection<Document> dataSet;

	private MongoCursor<Document> mcur;
	
	public ModelService(){
		dataSet = MongoAccess.db.getCollection("dataSet");
	}
	
	public void getData(){
		mcur = dataSet.find().iterator();
		
		while(mcur.hasNext()){
			
		}
	}
	
	public void run(){	
		getData();
		
		
	}
}
