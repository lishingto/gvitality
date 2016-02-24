package gvitality.api.parse;

import com.google.api.client.util.Key;

public class Owner {

	@Key
	public String login;

	@Key
	public int id;

	@Key
	public String followers_url;

	@Key
	public String followering_url;

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

}
