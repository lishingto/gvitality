package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBList;

import gitvital.api.ExtraInfoCrawler;
import gitvital.mongo.MongoAccess;

public class ExtraInfoTests {

	private String repoName = "facebook/draft-js";

	private ExtraInfoCrawler service = null;

	@Before
	public void setUp() {

		try {
			MongoAccess.init();
			service = new ExtraInfoCrawler();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testWeb() {
		try {
			Document doc = service.webProcess("pubnub/java");
			System.out.println("Doc: " + doc.toJson());
			assertTrue(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Exception");
		}
	}

	@Test
	public void testReadme() {
		Document doc = new Document();
		service.requestReadme(repoName, doc);

		long size = 0;
		try {
			size = doc.getLong("readmeSize");
		} catch (ClassCastException e) {
			fail("Unable to get Size of Readme");
		}

		assertNotNull(size);
		System.out.println("Size of readme is " + size);
	}

	@Test
	public void testContributors() {
		Document doc = new Document();
		try {
			service.requestContributors(repoName, doc);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		BasicDBList dbList = doc.get("contributors", BasicDBList.class);

		Iterator<Object> itr = dbList.iterator();
		while (itr.hasNext()) {
			Object obj = itr.next();
			if (obj instanceof Document) {
				Document contDoc = (Document) obj;
				String login = contDoc.getString("login");
				int contributions = contDoc.getInteger("contributions");

				assertNotNull(login);
				assertNotNull(contributions);

				System.out.println(login + ":" + contributions);
			}
		}

	}

}
