package com.github.lochnessdragon.modrinth4java;

import java.util.Date;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class Project {
    public final String id;
    public final String slug;
    public ProjectType type;
    public ProjectStatus status;
    public String teamId;
    public Optional<Integer> brandColor;
    public String title;
    public String description;
    public String body;
    public List<String> categories;
    public ProjectLicense license;
    
    public RequiredStatus requiredClientSide;
    public RequiredStatus requiredServerSide;
    
    public Optional<String> iconUrl;
    public Optional<String> issuesUrl;
    public Optional<String> discordUrl;
    public Optional<String> wikiUrl;
    public Optional<String> sourceUrl;
    public List<ProjectDonationUrl> donationUrls;

    public final int downloadCount;
    public final int followerCount;

    public final Optional<ModeratorMessage> modMsg;

    public Date publishedAt;
    public Date updatedAt;
    public Optional<Date> approvedAt;

    public String[] versions;
    public String[] gameVersions;
    public String[] gameLoaders;

    public ProjectImage[] images;

    public Project(String id, String slug, int downloadCount, int followerCount, Optional<ModeratorMessage> modMsg) {
        this.id = id;
        this.slug = slug;
        this.downloadCount = downloadCount;
        this.followerCount = followerCount;
        this.modMsg = modMsg;

        this.categories = new ArrayList<String>();
    }

	@Override
	public String toString() {
		String repr = "Project: " + this.title + "\n";
		repr += " - id=" + this.id + " slug=" + this.slug + "\n";
		repr += " - Download Count: " + this.downloadCount + " Followers: " + this.followerCount + "\n";
		
		return repr;
	}

	public static void createNew() {}

	public static Project fromJson(JSONObject json) {
		String id = json.getString("id");
		String slug = json.getString("slug");
		int downloads = json.getInt("downloads");
		int followers = json.getInt("followers");
        Optional<ModeratorMessage> modMessage;
        if (json.has("moderator_message")) {
            JSONObject modMessageJson = json.getJSONObject("moderator_message")
            String message = modMessageJson.getString("message");
            String body = "";
            if (modMessageJson.has("body")) {
                body = modMessageJson.getString("body");
            }
            
            modMessage = Optional.of(new ModeratorMessage(message, body));
        } else {
            modMessage = Optional.empty();
        }
        
		Project project = new Project(id, slug, downloads, followers, modMessage);

        // project human-friendly info
        project.title = json.getString("title");
        project.description = json.getString("description");
        project.body = json.getString("body");
        project.type = ProjectType.valueOf(json.getString("project_type").toUpperCase());
    
        if (json.has("color")) {
            project.brandColor = Optional.of(json.getInt("color"));
        }

        project.teamId = json.getString("team");
        // dates
        project.publishedAt = json.getString("published");
        
        // add categories
        JSONArray categories = json.getJSONArray("categories");
        for (Object category : categories) {
            project.categories.add((String) category);
        }

        JSONArray additional_categories = json.getJSONArray("additional_categories");
        for (Object category : categories) {
            project.categories.add((String) category);
        }

        // sided-logic
        project.requiredClientSide = RequiredStatus.valueOf(json.getString("client_side").toUpperCase());
        project.requiredServerSide = RequiredStatus.valueOf(json.getString("server_side").toUpperCase());

        // project urls
        if (json.has("issues_url")) {
            project.issuesUrl = Optional.of(json.getString("issues_url"));
        }

        if (json.has("source_url")) {
            project.sourceUrl = Optional.of(json.getString("source_url"));
        }

        if (json.has("wiki_url")) {
            project.wikiUrl = Optional.of(json.getString("wiki_url"));
        }

        if(json.has("discord_url")) {
            project.discordUrl = Optional.of(json.getString("discord_url"));
        }

        if(json.has("icon_url")) {
            project.iconUrl = Optional.of(json.getString("icon_url"));
        }

        if(json.has("donation_urls")) {
            JSONArray donationUrls = json.getJSONArray("donation_urls");
            for (Object url : donationUrls) {
                JSONObject donationObj = (JSONObject) url;
                String donationId = donationObj.getString("id");
                String donationPlatform = donationObj.getString("platform");
                String donationUrl = donationObj.getString("url");
                project.donationUrls.add(new ProjectDonationUrl(donationId, donationPlatform, donationUrl));
            }
        }
        
		return project;
	}
}