package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonException;
import javax.json.JsonObject;

import enums.GiantBombResource;

public class GiantBombUtils {
    
	private static final String BASE_URI = "https://www.giantbomb.com/api/";
	private static final String FORMAT_JSON = "json";
	
	// Need to specify a user agent (it can be any value) so that GiantBomb can respond
	private static final String USER_AGENT = "GiantBombUserAgent";
	private static final String ACCEPT = "text/" + FORMAT_JSON;
	
	// SHOULD NORMALLY BE READ FROM A FILE
	private static final String API_KEY = "784568466662eacd7cf5ba81d73976e4aa9291e3";
	
	/***************************
	 * Platforms               *
	 ***************************/
	public static String getPlatformsListAsJsonString() {
	    return ResultRequester.resultRequest(
	            // Resource
                createRequestResourceUrl(GiantBombResource.PLATFORMS) +
                // Params
                URLEncoderSpaces.encode("&field_list=name"),
                USER_AGENT,
                ACCEPT
        );
	}
	
	public static List<String> getPlatformsList() {
		String response = getPlatformsListAsJsonString();
	    
	    JsonObject JOPlatforms = JsonUtils.JsonObjectFromString(response);
	    JsonArray JAPlatforms = JOPlatforms.getJsonArray("results");
	    
	    List<String> platforms = new ArrayList<>();
	    for (int i = 0; i < JAPlatforms.size(); i++) {
	    	platforms.add(JAPlatforms.getJsonObject(i).getString("name"));
	    }
	    
	    return platforms;
	}
	
	public static String getPlatformInfosAsJsonString(String platform) {
	    return ResultRequester.resultRequest(
	            // Resource
                createRequestResourceUrl(GiantBombResource.PLATFORMS) +
                // Params
                URLEncoderSpaces.encode("&filter=name:" + platform),
                USER_AGENT,
                ACCEPT
        );
	}
	
	public static Map<String, String> getPlatformInfos(String platform) {
	    String response = getPlatformInfosAsJsonString(platform);
        
        // Get all informations
        JsonObject JOReponse = JsonUtils.JsonObjectFromString(response);
        JsonArray JAPlatformInfos = JOReponse.getJsonArray("results");
        if (JAPlatformInfos.isEmpty()) {
            return null;
        }
        
        JsonObject JOPlatformInfos = JAPlatformInfos.getJsonObject(0);
        
        Map<String, String> platformInfos = new HashMap<>();
        for (String key : JOPlatformInfos.keySet()) {
            platformInfos.put(key, JOPlatformInfos.get(key).toString());
        }
        
        // Get only one main image
        if (platformInfos.get("image") != "null") {
            JsonObject JOImages = JsonUtils.JsonObjectFromString(platformInfos.get("image"));
            platformInfos.put("image", JOImages.get("small_url").toString());
        }
        
        // Get Compagnie
        if (platformInfos.get("company") != "null") {
            JsonObject JOImages = JsonUtils.JsonObjectFromString(platformInfos.get("company"));
            platformInfos.put("company", JOImages.get("name").toString());
        }
        
        return platformInfos;
    }
	
   
    /****************************
     * Genres                   *
     ****************************/
   public static String getGamesTypesAsJsonString() {
       return ResultRequester.resultRequest(
               // Resource
               createRequestResourceUrl(GiantBombResource.GENRES) +
               // Params
               URLEncoderSpaces.encode("&field_list=name"),
               USER_AGENT,
               ACCEPT
       );
   }
   
   public static List<String> getGamesTypes() {
	    String response = getGamesTypesAsJsonString();
	    	    
	    JsonObject JOGamesTypes = JsonUtils.JsonObjectFromString(response);
	    JsonArray JAGamesTypes = JOGamesTypes.getJsonArray("results");
	    
	    List<String> gamesTypes = new ArrayList<>();
	    for (int i = 0; i < JAGamesTypes.size(); i++) {
	    	gamesTypes.add(JAGamesTypes.getJsonObject(i).getString("name"));
	    }
	    
	    return gamesTypes;
	}
	
	
    /****************************
     * Games                    *
     ****************************/
	public static String getMostRecentGamesAsJsonString() {
	    return ResultRequester.resultRequest(
	            // Resource
	            createRequestResourceUrl(GiantBombResource.GAMES) +
	            // Params
	            "&sort=original_release_date:desc&field_list=name",
                USER_AGENT,
                ACCEPT
        );
	}
	
	public static List<String> getMostRecentGames() {
	    String response = getMostRecentGamesAsJsonString();
			    
	    JsonObject JOGames = JsonUtils.JsonObjectFromString(response);
	    JsonArray JAGames= JOGames.getJsonArray("results");
	    
	    List<String> games = new ArrayList<>();
	    for(int i = 0; i < JAGames.size(); i++){
	    	games.add(JAGames.getJsonObject(i).getString("name"));
	    }
	    
	    return games;
	}
	
	
	public static String getGamesAsJsonString(String gameName) {
	    return ResultRequester.resultRequest(
	            // Resource
                createRequestResourceUrl(GiantBombResource.GAMES) +
                // Params
                URLEncoderSpaces.encode("&filter=name:" + gameName + "&sort=date_added:desc&field_list=name,original_release_date,image"),
                USER_AGENT,
                ACCEPT
        );      
	}
	
	public static List<Map<String, String>> getGames(String gameName) {
	    String response = getGamesAsJsonString(gameName);
        
        JsonObject JOGames = JsonUtils.JsonObjectFromString(response);
        JsonArray JAGames= JOGames.getJsonArray("results");
        
        List<Map<String, String>> games = new ArrayList<>();
        for (int i = 0; i < JAGames.size(); i++) {
            games.add(new HashMap<String,String>());
            games.get(i).put("name", JAGames.getJsonObject(i).getString("name"));
            games.get(i).put("original_release_date", JAGames.getJsonObject(i).get("original_release_date").toString());
            if (JAGames.getJsonObject(i).get("image").toString() != "null") {
                games.get(i).put("image", JAGames.getJsonObject(i).getJsonObject("image").get("small_url").toString());
            }
        }
        
        return games;
    }
	
	public static String getGameInfosAsJsonString(String game) {
	    return ResultRequester.resultRequest(
	            // Resource
                createRequestResourceUrl(GiantBombResource.SEARCH) +
                // Params
                URLEncoderSpaces.encode("&query=" + game + "&resources=game"),
                USER_AGENT,
                ACCEPT
        );
	}
	
	public static Map<String, String> getGameInfos(String game) {
        String response = getGameInfosAsJsonString(game);
			    
	    // Get all information
	    JsonObject JOReponse = JsonUtils.JsonObjectFromString(response);
	    JsonArray JAGameInfos = JOReponse.getJsonArray("results");
        if (JAGameInfos.isEmpty()) {
            return null;
        }
	    
	    JsonObject JOGameInfos = JAGameInfos.getJsonObject(0);
	    
	    Map<String, String> gameInfos = new HashMap<>();
	    for (String key : JOGameInfos.keySet()) {
    		gameInfos.put(key, JOGameInfos.get(key).toString());
	    }
	    
	    // Get only one main image
	    if (gameInfos.get("image") != "null"){
		    JsonObject JOImages = JsonUtils.JsonObjectFromString(gameInfos.get("image"));
		    gameInfos.put("image", JOImages.get("small_url").toString());
	    }
	    
	    // Get list of platforms
	    try {
	        JsonArray JAPlatforms = JsonUtils.JsonArrayFromString(gameInfos.get("platforms"));
    	    
    	    StringBuilder platforms = new StringBuilder();
    	    for (int i = 0; i < JAPlatforms.size(); i++) {
    	    	platforms.append(JAPlatforms.getJsonObject(i).getString("name"));
    	    	platforms.append(", ");
    	    }
    	    gameInfos.put("platforms", platforms.substring(0, platforms.length() - 2));
	    }
	    catch (JsonException e) {
	    }
	    
	    return gameInfos;
	}
	
	private static String createRequestResourceUrl(GiantBombResource Resource) {
	    return BASE_URI +
	           Resource + "/?" +
	           "format=" + FORMAT_JSON + "&api_key=" + API_KEY;
	}

}
