package gitvital.api.parse;

import com.google.api.client.util.Key;

//TODO: Add Contributors to database

public class Contributor {
	
	@Key
	public String login;
	
	@Key
	public long id;
	
	@Key
	public String avatar_url;
	
	@Key
	public String gravatar_id;
	
	@Key
	public String url;
	
	@Key
	public String html_url;
	
	@Key
	public String followers_url;
	
	@Key
	public String following_url;
	
	@Key
	public String gists_url;
	
	@Key
	public String starred_url;
	
	@Key
	public String subscriptions_url;
	
	@Key
	public String organizations_url;
	
	@Key
	public String repos_url;
	
	@Key
	public String events_url;
	
	@Key
	public String received_events_url;
	
	@Key
	public String type;
	
	@Key
	public boolean site_admin;
	
	@Key
	public int contributions;

}
