package gitvital.api;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ArrayMap;
import com.google.api.client.util.Key;
import com.google.gson.Gson;

import gitvital.api.parse.FullRepo;
import gitvital.api.parse.RateLimit;
import gitvital.api.parse.Repository;
import gitvital.api.parse.Status;
import gitvital.mongo.MongoAccess;
import gitvital.mongo.RawRepo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class RepositoryReadService extends BaseService {

	public RepositoryReadService() throws Exception {
		super();
	}

	public void process(long sinceId) throws Exception {

		GithubUrl url = new GithubUrl("https://api.github.com/repositories");

		// TODO record last ID and iterate from there
		url.set("since", sinceId);
		HttpRequest request = requestFactory.buildGetRequest(url);

		Repositories repoList = request.execute().parseAs(Repositories.class);

		if (repoList.isEmpty()) {
			System.out.println("No repositories found");
		} else {

			for (Repository r : repoList) {
				

				RawRepo raw = new RawRepo(readRepo(r));
				raw.save();
			}
		}

		System.out.println("Access : " + GithubUrl.accessCount);
	}
	
	public FullRepo readRepo(Repository r) throws IOException{
		System.out.println("-----------------------------------------------");
		System.out.println("ID: " + r.id);
		System.out.println("Name: " + r.name);
		System.out.println("Owner: " + r.owner.login);
		System.out.println("Full Name: " + r.full_name);
		System.out.println("Description: " + r.description);
		System.out.println("Fork: " + r.fork);
		System.out.println("URL: " + r.url);
		System.out.println("HTML: " + r.html_url);

		// Full Info Data
		GithubUrl fullInfoUrl = new GithubUrl(r.url);
		HttpRequest fullInfoReq = requestFactory.buildGetRequest(fullInfoUrl);
		FullRepo fullRepo = fullInfoReq.execute().parseAs(FullRepo.class);
		return fullRepo;
	}

	@Override
	public boolean run() throws Exception{
		process(Math.round(Math.random() * 52700000));
		return true;
	}

	public static void main(String[] args) {

		MongoAccess.init();

		try {
			RepositoryReadService service = new RepositoryReadService();
			service.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MongoAccess.close();
		System.exit(1);
	}

}
