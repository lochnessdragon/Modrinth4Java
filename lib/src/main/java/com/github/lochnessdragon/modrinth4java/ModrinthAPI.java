package com.github.lochnessdragon.modrinth4java;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;
import org.json.JSONArray;

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

    protected static String convertISToStr(InputStream stream) {
        try {
        	BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        	StringBuilder strBuilder = new StringBuilder();

        	String inputStr;
    		while ((inputStr = streamReader.readLine()) != null) {
        	    strBuilder.append(inputStr);
	    	}

        	return strBuilder.toString();
        } catch (Exception e) {
            System.err.println("Failed to convert input stream to str.");
            e.printStackTrace();
            throw new RuntimeException("Failed to convert input stream to str.");
        }
    }
    
	protected static JSONObject convertISToObject(InputStream stream) {
		try {
			String jsonStr = convertISToStr(stream);
			JSONObject json = new JSONObject(jsonStr);
			return json;
		} catch (Exception e) {
			System.err.println("Failed to convert stream to object.");
			e.printStackTrace();
			throw new RuntimeException("Failed to convert stream to object.");
		}
	}

    protected static JSONArray convertISToArray(InputStream stream) {
        try {
            String jsonStr = convertISToStr(stream);
            JSONArray json = new JSONArray(jsonStr);
            return json;
        } catch (Exception e) {
            System.err.println("Failed to convert stream to array.");
            e.printStackTrace();
            throw new RuntimeException("Failed to convert stream to array.");
        }
    }
	
    public static Project getProject(String id) {
        try {
            URL endpointUrl = new URL(BASE_URL + "project/" + id);
            HttpsURLConnection connection = (HttpsURLConnection) endpointUrl.openConnection();

			// handle response
			InputStream content = (InputStream) connection.getContent();
			
			JSONObject parsedJson = convertISToObject(content);
			
			Project project = Project.fromJson(parsedJson);
        	return project;
		} catch (Exception e) {
            System.err.println("Failed to grab project details for: " + id);
            e.printStackTrace();
			throw new RuntimeException("Failed to grab project details for project: " + id);
        }
    }

    public static Map<String, Project> getMultipleProjects(String[] ids) {
        try {
            String idsParam = "[";
            for (int i = 0; i < ids.length; i++) {
                idsParam += "\"" + ids[i] + "\"";
                if (i < ids.length - 1) {
                    idsParam += ",";
                }
            }
            idsParam += "]";

            URL endpointUrl = new URL(BASE_URL + "projects?ids=" + idsParam);
            HttpsURLConnection connection = (HttpsURLConnection) endpointUrl.openConnection();
            InputStream content = (InputStream) connection.getContent();

            JSONArray parsedArray = convertISToArray(content);
            
            Map<String, Project> projects = new HashMap<String, Project>();
            
            for (Object projectObj : parsedArray) {
                Project project = Project.fromJson((JSONObject) projectObj);
                projects.put(project.id, project);
            }
            
            return projects;
        } catch (Exception e) {
            System.err.println("Failed to grab multiple project details for: " + ids);
            e.printStackTrace();
            throw new RuntimeException("Failed to grab multiple project details for: " + ids);
        }
    }

    public static enum SortMethod {
        RELEVANCE,
        DOWNLOADS,
        FOLLOWS,
        NEWEST,
        UPDATED
    }

    public static ProjectSearchResult[] searchProjects(String query) {
        return searchProjects(query, SortMethod.RELEVANCE);
    }

    public static ProjectSearchResult[] searchProjects(String query, SortMethod sortMethod) {
        return searchProjects(query, sortMethod, "", 0, 20);
    }

    public static ProjectSearchResult[] searchProjects(String query, SortMethod sortMethod, String facets, int offset, int limit) {
        try {
            String urlStr = BASE_URL + "search?";
            if (query != "") {
                urlStr += "query=" + URLEncoder.encode(query, "UTF-8") + "&";
            }
            if(facets != "") {
                urlStr += "facets=" + facets + "&";
            }

            urlStr += "index=" + sortMethod.toString().toLowerCase() + "&offset=" + offset + "&limit=" + limit;
            
            URL endpointUrl = new URL(urlStr);
            HttpsURLConnection connection = (HttpsURLConnection) endpointUrl.openConnection();
            InputStream content = (InputStream) connection.getContent();

            JSONObject parsedJson = convertISToObject(content);

            int totalHits = parsedJson.getInt("total_hits");
            ProjectSearchResult[] projects = new ProjectSearchResult[totalHits];
            
            JSONArray projectsJson = parsedJson.getJSONArray("hits");
            for (int i = 0; i < totalHits; i++) {
                projects[i] = ProjectSearchResult.fromJson(projectsJson.getJSONObject(i));
            }

            return projects;
        } catch (Exception e) {
            System.err.println("Failed to search modrinth: query=" + query);
            e.printStackTrace();
            throw new RuntimeException("Failed to search modrinth: query=" + query);
        }
    }

	public static ProjectVersion getVersion(String id) {
		try {
			URL endpointUrl = new URL(BASE_URL + "version/" + id);
			HttpsURLConnection connection = (HttpsURLConnection) endpointUrl.openConnection();

			// handle response
			JSONObject parsedJson = convertISToObject((InputStream) connection.getContent());

			ProjectVersion version = ProjectVersion.fromJson(parsedJson);
			return version;
		} catch (Exception e) {
			System.err.println("Failed to grab the version details for: " + id);
			e.printStackTrace();
			throw new RuntimeException("Failed to grab the details for: " + id);
		}
	}

	public static UserInfo getUser(String id) {
		
	}
}