package utils;

import javax.json.JsonException;
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
        
        try {
            JsonObject joResponse = JsonUtils.JsonObjectFromString(response);
            String status = joResponse.getString("status");
            
            // Error (this case might never happen as we check address in create game session form)
            if (status == null || !"OK".equals(status)) {
                return null;
            }
            
            // Get location
            JsonObject joLocation = joResponse.getJsonArray("results").getJsonObject(0).getJsonObject("geometry").getJsonObject("location");
            return new Location(
                    joLocation.getJsonNumber("lat").doubleValue(),
                    joLocation.getJsonNumber("lng").doubleValue());
        }
        // Parsing error (this case might never happen as we check address in create game session form)
        catch (JsonException e) {
            return null;
        }
    }
    
    
    private static String createRequestAdressUrl(String address) {
        return BASE_URI +
               FORMAT_JSON + "?" +
               "api_key=" + API_KEY +
               URLEncoderSpaces.encode("&address=" + address);
    }
}
