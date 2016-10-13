package utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class JsonUtils {
    public static JsonObject loadJsonObject(String filepath) {
        InputStream inputStream = JsonUtils.class.getClassLoader().getResourceAsStream(filepath);
        JsonReader jsonReader = Json.createReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        return jsonReader.readObject();
    }
}
