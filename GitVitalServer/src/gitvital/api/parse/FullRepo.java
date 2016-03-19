package gitvital.api.parse;

import java.util.Date;

import com.google.api.client.util.Key;

public class FullRepo extends Repository{
	
	//TODO correct type
	@Key
	public String created_at;
	
	@Key
	public String updated_at;
	
	@Key
	public String pushed_at;
	
	@Key
	public String git_url;
	
	@Key
	public String ssh_url;
	
	@Key
	public String clone_url;
	
	@Key
	public String svn_url;
	
	@Key
	public String homepage;
	
	@Key
	public int size;
	
	@Key
	public int stargazers_count;
	
	@Key
	public int watchers_count;
	
	@Key
	public String language;
	
	@Key
	public boolean has_issues;
	
	@Key
	public boolean has_downloads;
	
	@Key
	public boolean has_wiki;
	
	@Key
	public boolean has_pages;
	
	@Key
	public int forks_count;
	
	@Key
	public String mirror_url;
	
	@Key
	public int open_issues_count;
	
	@Key
	public int forks;
	
	@Key
	public int open_issues;
	
	@Key
	public int watchers;
	
	@Key
	public String default_branch;
	
	@Key
	public double score;

}
