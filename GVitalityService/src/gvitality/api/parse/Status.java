package gvitality.api.parse;

import com.google.api.client.util.Key;

public class Status {
	@Key
	public int[] days;

	@Key
	public int total;

	@Key
	public int week;
}