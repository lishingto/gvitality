package gitvital.api.parse;

import com.google.api.client.util.Key;

import gitvital.api.parse.Owner;

/** Represents a Repository. */
public class Repository {

	@Key
	public int id;

	@Key
	public String name;

	@Key
	public String full_name;

	@Key
	public Owner owner;

	@Key("private")
	public boolean isPrivate;

	@Key
	public String description;

	@Key
	public boolean fork;

	@Key
	public String url;
	@Key
	public String html_url;
	@Key
	public String forks_url;
	@Key
	public String keys_url;
	@Key
	public String collaborators_url;
	@Key
	public String teams_url;
	@Key
	public String hooks_url;
	@Key
	public String issue_events_url;
	@Key
	public String events_url;
	@Key
	public String assignees_url;
	@Key
	public String branches_url;
	@Key
	public String tags_url;
	@Key
	public String blobs_url;
	@Key
	public String git_tags_url;
	@Key
	public String git_refs_url;
	@Key
	public String trees_url;
	@Key
	public String statuses_url;
	@Key
	public String languages_url;
	@Key
	public String stargazers_url;
	@Key
	public String contributors_url;
	@Key
	public String subscribers_url;
	@Key
	public String subscription_url;
	@Key
	public String commits_url;
	@Key
	public String git_commits_url;
	@Key
	public String comments_url;
	@Key
	public String issue_comment_url;
	@Key
	public String contents_url;
	@Key
	public String compare_url;
	@Key
	public String merges_url;
	@Key
	public String archive_url;
	@Key
	public String downloads_url;
	@Key
	public String issues_url;
	@Key
	public String pulls_url;
	@Key
	public String milestones_url;
	@Key
	public String notifications_url;
	@Key
	public String labels_url;
	@Key
	public String releases_url;
	@Key
	public String deployments_url;

}