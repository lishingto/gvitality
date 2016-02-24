package gvitality.api.parse;

import com.google.api.client.util.Key;

public class RateLimit {
	
	public class Resources {
		@Key
		public RateResource core;
		
		@Key
		public RateResource search;
		
		@Key
		public RateResource rate;
	}
	
	public class RateResource {
		@Key
		public long limit;

		@Key
		public long remaining;

		@Key
		public long reset;
	}
	
	@Key
	public Resources resources;
}
