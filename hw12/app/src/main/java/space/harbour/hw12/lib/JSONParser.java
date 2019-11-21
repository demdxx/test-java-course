package space.harbour.hw12.lib;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class JSONParser {
    static JSONArray jObj = null;

    // constructor
    public JSONParser() {}

    public JSONArray getJSONFromUrl(String url) {
        final StringBuilder content = new StringBuilder();
        try (InputStream is = new URL(url).openConnection().getInputStream();
             InputStreamReader in = new InputStreamReader(is, "UTF-8");
             BufferedReader br = new BufferedReader(in);) {
            String inputLine;
            while ((inputLine = br.readLine()) != null)
                content.append(inputLine + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        String json = content.toString();

        // try parse the string to a JSON object
        try {
            JSONObject obj = new JSONObject(json);
            jObj = obj.getJSONArray("response");
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return jObj;
    }
}

