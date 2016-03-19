package gitvital.rest;

import java.io.File;
import java.util.List;

import javax.servlet.ServletException;

import org.glassfish.jersey.servlet.ServletContainer;

import gitvital.api.ExtraInfoCrawler;
import gitvital.api.RepositoryReadService;
import gitvital.mongo.MongoAccess;
import gitvital.process.Feature;
import gitvital.weka.WekaController;
import weka.core.Attribute;

public class ServerService extends ServletContainer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7534215387331503453L;
	/**
	 * 
	 */
	public static RepositoryReadService repoReader;
	public static ExtraInfoCrawler crawler;
	public static Feature featureCreator;
	public static WekaController weka;
	public static ServerService con;
	public static List<Attribute> classifyAttributes;
	
	@Override
	public void destroy() {
		MongoAccess.close();

		System.out.println("Server Stopped");

		super.destroy();
	}



	@Override
	public void init() throws ServletException {
		System.out.println("Server Start");
		MongoAccess.init();
		
		try {
			con = this;
			repoReader = new RepositoryReadService();
			crawler = new ExtraInfoCrawler();
			featureCreator = new Feature();
			weka = new WekaController(false);
			classifyAttributes = ClassifyService.filterAttributes();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.init();
	}

}
