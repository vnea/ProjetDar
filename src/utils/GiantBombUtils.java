package utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import utils.JsonUtils;

public class GiantBombUtils {
	
	public static List<String> getPlatformsList() {
		String response = null;
		List<String> platforms = new ArrayList<String>();
		
	    response = resultRequest("http://www.giantbomb.com/api/platforms/?format=json&api_key=784568466662eacd7cf5ba81d73976e4aa9291e3&field_list=name");  
	    
	    JsonObject JOPlatforms = JsonUtils.JsonObjectFromString(response);
	    JsonArray JAPlatforms = JOPlatforms.getJsonArray("results");
	    for(int i = 0; i < JAPlatforms.size(); i++){
	    	platforms.add(JAPlatforms.getJsonObject(i).getString("name"));
	    }
	    
	    return platforms;
	}
	
	public static List<String> getGamesTypes() {
		String response = null;
		List<String> gamesTypes = new ArrayList<String>();
		
	    response = resultRequest("http://www.giantbomb.com/api/genres/?format=json&api_key=784568466662eacd7cf5ba81d73976e4aa9291e3&field_list=name");  
	    
	    JsonObject JOGamesTypes = JsonUtils.JsonObjectFromString(response);
	    JsonArray JAGamesTypes = JOGamesTypes.getJsonArray("results");
	    for(int i = 0; i < JAGamesTypes.size(); i++){
	    	gamesTypes.add(JAGamesTypes.getJsonObject(i).getString("name"));
	    }
	    
	    return gamesTypes;
	}
	
	public static List<String> getMostRecentGames() {
		String response = null;
		List<String> games = new ArrayList<String>();
		
	    response = resultRequest("http://www.giantbomb.com/api/games/?format=json&sort=original_release_date:desc&api_key=784568466662eacd7cf5ba81d73976e4aa9291e3&field_list=name");  
	    
	    JsonObject JOGames = JsonUtils.JsonObjectFromString(response);
	    JsonArray JAGames= JOGames.getJsonArray("results");
	    for(int i = 0; i < JAGames.size(); i++){
	    	games.add(JAGames.getJsonObject(i).getString("name"));
	    }
	    
	    return games;
	}
	
	public static Map<String,String> getGameInfos(String game) {
		String response = null;
		Map<String, String> gameInfos = new HashMap<String, String>();
		
	    response = resultRequest("http://www.giantbomb.com/api/search/?api_key=784568466662eacd7cf5ba81d73976e4aa9291e3&format=json&query="+game+"&resources=game");  
	    
	    // Get all informations
	    JsonObject JOReponse = JsonUtils.JsonObjectFromString(response);
	    JsonArray JAGameInfos = JOReponse.getJsonArray("results");
	    JsonObject JOGameInfos = JAGameInfos.getJsonObject(0);
	    for(String key : JOGameInfos.keySet()){
    		gameInfos.put(key,JOGameInfos.get(key).toString());
	    }
	    
	    // Get only one main image
	    JsonObject JOImages = JsonUtils.JsonObjectFromString(gameInfos.get("image"));
	    gameInfos.put("image", JOImages.get("small_url").toString());
	    
	    // Get list of platforms
	    JsonArray JAPlatforms = JsonUtils.JsonArrayFromString(gameInfos.get("platforms"));
	    String platforms = "";
	    for(int i = 0; i < JAPlatforms.size(); i++){
	    	platforms += JAPlatforms.getJsonObject(i).getString("name")+", ";
	    	
	    }
	    gameInfos.put("platforms", platforms.substring(0, platforms.length() - 2));
	    
	    return gameInfos;
	}
	
	
	private static String resultRequest(String requestURL) {	
		String responseBody = null; 
		
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
	        HttpGet httpget = new HttpGet(requestURL);
	        // NEED TO SET A CUSTOM USER AGENT, IT CAN BE A RANDOM VALUE
	        httpget.setHeader(HttpHeaders.USER_AGENT, "GiantBombUserAgent");
	        httpget.setHeader(HttpHeaders.ACCEPT, "application/json");
	
	        // Custom Response Handler
	        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
	            public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
	                int status = response.getStatusLine().getStatusCode();
	                if (status >= 200 && status < 300) {
	                    HttpEntity entity = response.getEntity();
	                    return entity != null ? EntityUtils.toString(entity) : null;
	                }
	                else {
	                    throw new ClientProtocolException("Unexpected response status: " + status);
	                }
	            }
	        };
	        
	        responseBody = httpclient.execute(httpget, responseHandler);
	        
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        return responseBody;
	}
	
}
