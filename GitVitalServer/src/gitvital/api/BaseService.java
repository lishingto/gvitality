package gitvital.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.Gson;

import gitvital.api.parse.Contributor;
import gitvital.api.parse.RateLimit;
import gitvital.api.parse.Repository;
import gitvital.api.parse.Status;

public abstract class BaseService {
	protected final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	protected final JsonFactory JSON_FACTORY = new JacksonFactory();

	protected HttpRequestFactory requestFactory;

	public static class Repositories extends LinkedList<Repository> {

	}

	public static class StatusList extends ArrayList<Status> {

	}	
	
	public static class ContributorList extends LinkedList<Contributor>{
		
	}
	
	public void start(){
		boolean isRun = true;
		while (isRun) {
			try {
				RateLimit l = getRateLmit();

				if (l.resources.core.remaining > 0) {
					isRun = run();
				} else {
					try {
						long delay = (l.resources.core.reset * 1000) - (new Date()).getTime();
						Thread.sleep(delay);
					} catch (InterruptedException ex) {
						Thread.currentThread().interrupt();
					}
				}
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
	}
	
	public RateLimit getRateLmit() throws Exception {
		GithubUrl url = new GithubUrl("https://api.github.com/rate_limit");
		HttpRequest request = requestFactory.buildGetRequest(url);
		String response = request.execute().parseAsString();

		Gson gson = new Gson();

		RateLimit rlimit = gson.fromJson(response, RateLimit.class);
		
		Date reset = new Date(rlimit.resources.core.reset*1000);
		
		System.out.println(
				"Limit: " + rlimit.resources.core.limit + " | Remanining: " + rlimit.resources.core.remaining + " | Reset: " + reset.toString());
		return rlimit;
	}
	
	public abstract boolean run() throws Exception;
	
	public BaseService() throws Exception{
		requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
			@Override
			public void initialize(HttpRequest request) {
				request.setParser(new JsonObjectParser(JSON_FACTORY));
			}
		});
	}
}
