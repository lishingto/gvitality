package gitvital.weka;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Random;

import gitvital.mongo.MongoAccess;
import gitvital.rest.ServerService;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;

public class WekaController {

	private ArffHandler ah = null;
	private String tempFile = "tempfile.arff";
	private DataSource source = null;
	private Instances data = null;

	public WekaController(boolean writeNew) {
		ah = new ArffHandler("dataSet");

		if (writeNew)
			ah.writeArff(tempFile);
	}
	
	public ArrayList<Attribute> getAttributes(){
		return ah.getAttributes();
	}

	public Instances loadModel(String modelPath, Instances tData) {
		Classifier cls = null;

		try {
			if (ServerService.con != null) {
				modelPath = ServerService.con.getServletContext().getRealPath(modelPath);
			}
			cls = (Classifier) weka.core.SerializationHelper.read(modelPath);

			tData.setClassIndex(tData.numAttributes() - 1);

			for (int i = 0; i < tData.numInstances(); i++) {
				double classVal = cls.classifyInstance(tData.instance(i));
				tData.instance(i).setClassValue(classVal);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
		return tData;
	}

	public void loadFromDB() {
		try {

			// Prepare Data Source
			source = new DataSource(tempFile);
			data = source.getDataSet();
			data.deleteStringAttributes();
			data.deleteAttributeAt(data.attribute("daysUpdated").index());
			if (data.classIndex() == -1)
				data.setClassIndex(data.numAttributes() - 1);

			// train classifier
			Classifier cls = new J48();
			cls.buildClassifier(data);
			// evaluate classifier and print some statistics
			Evaluation eval = new Evaluation(data);
			eval.crossValidateModel(cls, data, 10, new Random(1));

			System.out.println(eval.toSummaryString("\nResults\n======\n", false));
			System.out.println(eval.toClassDetailsString());
			System.out.println(eval.toMatrixString());
			System.out.println(cls.toString());

			SerializationHelper.write("/models/latest.model", cls);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// TEST
	public static void main(String[] args) {
		MongoAccess.init();
		WekaController wc = new WekaController(false);

		wc.loadFromDB();

		MongoAccess.close();
	}
}
