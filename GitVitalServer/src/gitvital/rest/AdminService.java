package gitvital.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

@Path("admin")
public class AdminService {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String testMessage() throws JSONException{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("test", "Test Message Okay"); 
		return jsonObject.toString();
	}
		
	@GET
	@Path("/qualify/{key}")
	@Produces("application/json")
	public JSONObject doSomething(@PathParam("key") String passkey) throws JSONException{
		JSONObject jsonObject = new JSONObject();
		return jsonObject;
	}
	
}
