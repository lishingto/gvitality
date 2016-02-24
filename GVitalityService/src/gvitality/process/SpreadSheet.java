package gvitality.process;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import gvitality.mongo.MongoAccess;

public class SpreadSheet {
	
	private XSSFWorkbook workbook;
	private XSSFSheet spreadsheet;
	private Map<String, Object[]> data;
	
	public SpreadSheet(){
		workbook = new XSSFWorkbook();
		workbook.createSheet("repositories");
		data = new HashMap<String, Object[]>();
	}
	
	public void populateFromDB(){
		MongoCollection<Document> raw = MongoAccess.db.getCollection("rawRepos");
		MongoCursor<Document> mcur = raw.find().iterator();
		long cnt = 0;
		while(mcur.hasNext()){
			Document doc = mcur.next();
			Object[] obj = {};
			
			//TODO: Populate
			
			cnt++;
			data.put("" + cnt, obj);
		}
	}
	
	public void generate(){
		//TODO
	}
	
	public static void main(String[] args){
		//TODO: Write Out
	}
}
