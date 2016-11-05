package utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class JsonUtils {
    public static JsonObject loadJsonObject(String filepath) {
        InputStream inputStream = JsonUtils.class.getClassLoader().getResourceAsStream(filepath);
        JsonReader jsonReader = Json.createReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        return jsonReader.readObject();
    }
    
    public static JsonObject JsonObjectFromString(String str){
		JsonReader jsonReader = Json.createReader(new StringReader(str));
		JsonObject object = jsonReader.readObject();
		jsonReader.close();
		
		return object;
    }
    
    public static JsonArray JsonArrayFromString(String str){
		JsonReader jsonReader = Json.createReader(new StringReader(str));
		JsonArray array = jsonReader.readArray();
		jsonReader.close();
		
		return array;
    }
}
