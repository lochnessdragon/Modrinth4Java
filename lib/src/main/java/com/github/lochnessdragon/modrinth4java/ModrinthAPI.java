package com.github.lochnessdragon.modrinth4java;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

/**
* Represents all the methods that can be performed on the 
* modrinth endpoint without authentication.
*/
public class ModrinthAPI {
    protected static String BASE_URL = "https://api.modrinth.com/v2/";

	public static String getBaseUrl() {
		return BASE_URL;
	}

	public static void useStagingUrl() {
		BASE_URL = "https://staging-api.modrinth.com/v2/";
	}

	public static void useProdUrl() {
		BASE_URL = "https://api.modrinth.com/v2/";
	}

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
			
			Project project = Project.fromJson(parsedJson);
        	return project;
		} catch (Exception e) {
            System.err.println("Failed to grab project details for: " + id);
            e.printStackTrace();
			throw new RuntimeException("Failed to grab project details for project: " + id);
        }
    }

	public static ProjectVersion getVersion(String id) {
		try {
			URL endpointUrl = new URL(BASE_URL + "version/" + id);
			HttpsURLConnection connection = (HttpsURLConnection) endpointUrl.openConnection();

			// handle response
			JSONObject parsedJson = convertInputStreamToJson((InputStream) connection.getContent());

			ProjectVersion version = ProjectVersion.fromJson(parsedJson);
			return version;
		} catch (Exception e) {
			System.err.println("Failed to grab the version details for: " + id);
			e.printStackTrace();
			throw new RuntimeException("Failed to grab the details for: " + id);
		}
	}
}