package com.github.lochnessdragon.modrinth4java;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONObject;
import java.util.Optional;

/**
* Represents all the methods that can be performed on the 
* modrinth endpoint without authentication.
*/
public class ModrinthAPI {
    public static String BASE_URL = "https://api.modrinth.com/v2/";

	protected static JSONObject convertInputStreamToJson(InputStream stream) {
		try {
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			StringBuilder strBuilder = new StringBuilder();

			String inputStr;
    		while ((inputStr = streamReader.readLine()) != null) {
        		strBuilder.append(inputStr);
			}
			JSONObject json = new JSONObject(strBuilder.toString());
			return json;
		} catch (Exception e) {
			System.err.println("Failed to parse json.");
			e.printStackTrace();
			throw new RuntimeException("Failed to parse json.");
		}
	}
	
    public static Project getProject(String id) {
        try {
            URL endpointUrl = new URL(BASE_URL + "project/" + id);
            HttpsURLConnection connection = (HttpsURLConnection) endpointUrl.openConnection();

			// handle response
			InputStream content = (InputStream) connection.getContent();
			
			JSONObject parsedJson = convertInputStreamToJson(content);
			System.out.println(parsedJson.toString());

			Project project = Project.fromJson(parsedJson);
        	return project;
		} catch (Exception e) {
            System.err.println("Failed to grab project details for: " + id);
            e.printStackTrace();
			throw new RuntimeException("Failed to grab project details for project: " + id);
        }
    }
}