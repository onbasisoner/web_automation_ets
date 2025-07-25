package utils;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ElementReader {

    private static final Map<String, JSONObject> cache = new HashMap<>();

    public static JSONObject getElements(String pageJsonFileName) {
        if (cache.containsKey(pageJsonFileName)) {
            return cache.get(pageJsonFileName);
        }
        try (InputStream is = ElementReader.class.getClassLoader().getResourceAsStream(pageJsonFileName)) {
            if (is == null) {
                throw new RuntimeException(pageJsonFileName + " not found in resources");
            }
            JSONTokener tokener = new JSONTokener(is);
            JSONObject json = new JSONObject(tokener);
            cache.put(pageJsonFileName, json);
            return json;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load " + pageJsonFileName, e);
        }
    }

    public static String getElementValue(String pageJsonFileName, String key) {
        JSONObject json = getElements(pageJsonFileName);
        if (!json.has(key)) {
            throw new RuntimeException("Key " + key + " not found in " + pageJsonFileName);
        }
        JSONObject element = json.getJSONObject(key);
        return element.getString("value");
    }

    public static String getElementType(String pageJsonFileName, String key) {
        JSONObject json = getElements(pageJsonFileName);
        if (!json.has(key)) {
            throw new RuntimeException("Key " + key + " not found in " + pageJsonFileName);
        }
        JSONObject element = json.getJSONObject(key);
        return element.getString("type");
    }
}
