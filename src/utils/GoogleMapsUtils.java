package utils;

import javax.json.JsonArray;
import javax.json.JsonObject;

import models.Location;

public class GoogleMapsUtils {

    private static final String BASE_URI = "https://maps.googleapis.com/maps/api/geocode/";
    private static final String FORMAT_JSON = "json";
    private static final String ACCEPT = "text/" + FORMAT_JSON;

    
    // SHOULD NORMALLY BE READ FROM A FILE
    private static final String API_KEY = "AIzaSyC5U4UKF009_yNlDjGO9X3JEoE9QsADw9g";
    
    public static Location geocode(String address) {
        String response = ResultRequester.resultRequest(createRequestAdressUrl(address), null, ACCEPT);
        
        JsonObject joReponse = JsonUtils.JsonObjectFromString(response);
        JsonArray jaPlatformInfos = joReponse.getJsonArray("status");


        
        return null;
    }
    
    
    private static String createRequestAdressUrl(String address) {
        return BASE_URI +
               FORMAT_JSON + "?" +
               "api_key=" + API_KEY +
               URLEncoderSpaces.encode("&address=" + address);
    }
}
