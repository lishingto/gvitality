package gitvital.weka;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bson.Document;

import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import gitvital.api.CustUtil;
import gitvital.mongo.MongoAccess;
import gitvital.process.BaseFileHandler;
import weka.core.Attribute;
import weka.core.FastVector;

public class ArffHandler extends BaseFileHandler {

	private MongoCollection<Document> db;
	private MongoCollection<Document> attrDb;
	private LinkedHashMap<String, String> attrList = new LinkedHashMap<String, String>();

	public ArffHandler(String loadDb) {
		db = MongoAccess.db.getCollection(loadDb);

		// Load Attribute Map
		attrDb = MongoAccess.db.getCollection("ArffLabels");
		MongoCursor<Document> acur = attrDb.find().iterator();
		while (acur.hasNext()) {
			Document adoc = acur.next();

			String label = adoc.getString("label");
			String type = adoc.getString("type");

			// Generates Nominal Values from all distinct values
			if (type.equals("NOMINAL")) {
				type = "{";
				int cnt = 0;
				// Using MongoDB's super handy feature
				DistinctIterable<String> ditr = db.distinct(label, String.class);
				for (String val : ditr) {
					if (cnt > 0) {
						type += ",";
					}

					if (val.contains(" ") || val.contains("\'") || val.length() < 1) {
						type += "\"" + val + "\"";
					} else {
						type += val;
					}
					cnt++;
				}
				type += "}";
			}

			attrList.put(label, type);
		}
	}

	public LinkedList<String> generateArff() {
		LinkedList<String> contents = new LinkedList<String>();
		MongoCursor<Document> mcur = db.find().iterator();

		int cnt = 0;

		// Write header
		System.out.println("Arff File Write Started");
		contents.add("@RELATION gitvital\r\n");

		// Add attributes

		for (Entry<String, String> entry : attrList.entrySet()) {
			contents.add("@ATTRIBUTE " + entry.getKey() + " " + entry.getValue());
		}

		System.out.println("Header and Attributes Done");

		// Write Data
		contents.add("\r\n@DATA");
		while (mcur.hasNext()) {
			Document doc = mcur.next();

			String row = "";
			for (Entry<String, String> entry : attrList.entrySet()) {

				String label = entry.getKey();
				String type = entry.getValue();

				// Exclude Mongo ID
				if (entry.getKey().equals("_id")) {
					continue;
				}

				Object obj = doc.get(label);
				// print data row
				String item = "";

				// Add comma
				if (row.length() > 0) {
					item = ",";
				}

				// Actual Data
				if (obj == null) {
					// handle Nulls
					if (type.equals("NUMERIC")) {
						item += "-1";
					} else {
						item += "NULL";
					}
				} else {

					// if String type add quotes
					String val = obj.toString();
					if (val.length() < 1 || type.equals("string") || val.contains(" ") || val.contains("\'")) {
						item += "\"" + val + "\"";
					} else {
						item += val;
					}
				}

				row += item;
			}
			contents.add(row);
			System.out.println("Row " + cnt + " done.");
			cnt++;
		}

		return contents;
	}

	public ArrayList<Attribute> getAttributes() {
		ArrayList<Attribute> list = new ArrayList<Attribute>();
		for (Entry<String, String> entry : attrList.entrySet()) {
			Attribute attr = null;
			if (entry.getValue().contains("{")) {
				// Nominal
				String clean = entry.getValue().replace("{", "").replace("}","");
				FastVector nomVal = CustUtil.strArrayToFv(clean.split(","));
				attr = new Attribute(entry.getKey(), nomVal);
			} else {
				//non Nominal
				attr = new Attribute(entry.getKey());
			}
			list.add(attr);
		}
		return list;
	}

	public boolean writeArff(String outputFileName) {
		LinkedList<String> contents = generateArff();

		System.out.println("Outputing to Disk.");
		// writeout
		return writeFile(outputFileName, contents);
	}

	public static void main(String[] args) {
		MongoAccess.init();
		ArffHandler arff = new ArffHandler("dataSet");
		arff.writeArff("defaultOutput.arff");
		MongoAccess.close();
	}
}
