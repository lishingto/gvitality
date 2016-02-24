package gvitality.api;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.Key;

/** URL for Github API. */
public class GithubUrl extends GenericUrl {

	public static int accessCount = 0;

	public GithubUrl(String encodedUrl) {
		super(encodedUrl);
		accessCount++;
	}

	@Key
	public String access_token = "18066ead9927178482126935c498956564fd1488";
}