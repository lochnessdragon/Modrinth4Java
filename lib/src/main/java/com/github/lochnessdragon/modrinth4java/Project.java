package com.github.lochnessdragon.modrinth4java;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Represents a modrinth project.
 * */
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

    public OffsetDateTime publishedAt;
    public OffsetDateTime updatedAt;
    public Optional<OffsetDateTime> approvedAt;

    public List<String> versionIds;
    public List<String> gameVersions;
    public List<String> modLoaders;

    public List<ProjectImage> images;

    public Project(String id, String slug, int downloadCount, int followerCount, Optional<ModeratorMessage> modMsg) {
        this.id = id;
        this.slug = slug;
        this.downloadCount = downloadCount;
        this.followerCount = followerCount;
        this.modMsg = modMsg;

        this.categories = new ArrayList<String>();
        this.donationUrls = new ArrayList<ProjectDonationUrl>();
        this.versionIds = new ArrayList<String>();
        this.gameVersions = new ArrayList<String>();
        this.modLoaders = new ArrayList<String>();
        this.images = new ArrayList<ProjectImage>();
    }

	@Override
	public String toString() {
		String repr = "Project: " + this.title + "\n";
		repr += this.description + "\n";
		repr += " - Id: " + this.id + " Slug: " + this.slug + "\n";
		repr += " - Created by: " + this.teamId + "\n";
		
		repr += " - Published at: " + this.publishedAt + " Created at: " + this.updatedAt;
		if(this.approvedAt.isPresent()) {
			repr += " Approved at: " + this.approvedAt.get();
		}
		repr += "\n";
		
		repr += " - Downloads: " + this.downloadCount + " Followers: " + this.followerCount + "\n";
		repr += " - Type: " + this.type.toString().toLowerCase() + " Status: " + this.status.toString().toLowerCase() + "\n";
		repr += " - Client Side: " + this.requiredClientSide.toString().toLowerCase() + " Server Side: " + this.requiredServerSide.toString().toLowerCase() + "\n";
		
		if(brandColor.isPresent()) {
			repr += " - Color: " + this.brandColor.get() + "\n";
		}
		
		if(this.categories.size() > 0) {
			repr += " - Categories:" + "\n";
			for (String category : categories) {
				repr += "   - " + category + "\n";
			}
		}
		
		repr += " - License: " + this.license.name + " [" + this.license.id + "] (" + this.license.url + ")" + "\n";
		repr += "Extended Desc: \n" + this.body + "\n";
		
		if (this.iconUrl.isPresent()) {
			repr += " - Icon URL: " + this.iconUrl.get() + "\n";
		}
		
		if (this.issuesUrl.isPresent()) {
			repr += " - Issues URL: " + this.issuesUrl.get() + "\n";
		}
		
		if (this.wikiUrl.isPresent()) {
			repr += " - Wiki URL: " + this.wikiUrl.get() + "\n";
		}
		
		if (this.discordUrl.isPresent()) {
			repr += " - Discord URL: " + this.discordUrl.get() + "\n";
		}
		
		if (this.sourceUrl.isPresent()) {
			repr += " - Source URL: " + this.sourceUrl.get() + "\n";
		}
		
		if (this.donationUrls.size() > 0) {
			repr += " - Donation URLs:\n";
			for (ProjectDonationUrl url : this.donationUrls) {
				repr += "   - Donate At: " + url.url + " (" + url.platform + ") [" + url.id + "]\n";
			}
		}
		
		if (this.modMsg.isPresent()) {
			ModeratorMessage msg = this.modMsg.get();
			repr += "**MODERATOR MESSAGE: " + msg.message() + " **\n";
			repr += msg.body() + "\n\n";
		}
		
		if (this.versionIds.size() > 0) {
			repr += " - Versions:\n";
			for (String version : this.versionIds) {
				repr += "   - " + version + "\n";
			}
		}
		
		if (this.gameVersions.size() > 0) {
			repr += " - Minecraft Versions:\n";
			for (String version : this.gameVersions) {
				repr += "   - " + version + "\n";
			}
		}
		
		if (this.modLoaders.size() > 0) {
			repr += " - Mod Loader:\n";
			for (String loader : this.modLoaders) {
				repr += "   - " + loader + "\n";
			}
		}
		
		if (this.images.size() > 0) {
			repr += " - Images:\n";
			for (ProjectImage image : this.images) {
				repr += "   - " + (image.featured ? "*" : "") + "[" + image.ordering + "] " + image.title + " : " + image.description + " Created at: " + image.createdAt + " (" + image.url + ")\n";
			}
		}
		
		return repr;
	}

	public static void createNew() {}

	public static Project fromJson(JSONObject json) {
		String id = json.getString("id");
		String slug = json.getString("slug");
		int downloads = json.getInt("downloads");
		int followers = json.getInt("followers");

        Optional<ModeratorMessage> modMessage;
        if (json.has("moderator_message") && !json.isNull("moderator_message")) {
            JSONObject modMessageJson = json.getJSONObject("moderator_message");
            String message = modMessageJson.getString("message");
            String body = "";
            if (modMessageJson.has("body") && !modMessageJson.isNull("body")) {
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
        project.status = ProjectStatus.valueOf(json.getString("status").toUpperCase());
    
        if (json.has("color") && !json.isNull("color")) {
            project.brandColor = Optional.of(json.getInt("color"));
        }

        project.teamId = json.getString("team");
        
        // dates
        project.publishedAt = OffsetDateTime.parse(json.getString("published"));
        project.updatedAt = OffsetDateTime.parse(json.getString("updated"));
        if (json.has("approved") && !json.isNull("approved")) {
        	project.approvedAt = Optional.of(OffsetDateTime.parse(json.getString("approved")));
        }
        
        // add categories
        JSONArray categories = json.getJSONArray("categories");
        for (Object category : categories) {
            project.categories.add((String) category);
        }

        JSONArray additional_categories = json.getJSONArray("additional_categories");
        for (Object category : additional_categories) {
            project.categories.add((String) category);
        }

        // sided-logic
        project.requiredClientSide = RequiredStatus.valueOf(json.getString("client_side").toUpperCase());
        project.requiredServerSide = RequiredStatus.valueOf(json.getString("server_side").toUpperCase());

        // project urls
        if (json.has("issues_url") && !json.isNull("issues_url")) {
            project.issuesUrl = Optional.of(json.getString("issues_url"));
        }

        if (json.has("source_url") && !json.isNull("source_url")) {
            project.sourceUrl = Optional.of(json.getString("source_url"));
        }

        if (json.has("wiki_url") && !json.isNull("wiki_url")) {
            project.wikiUrl = Optional.of(json.getString("wiki_url"));
        }

        if(json.has("discord_url") && !json.isNull("discord_url")) {
            project.discordUrl = Optional.of(json.getString("discord_url"));
        }

        if(json.has("icon_url") && !json.isNull("icon_url")) {
            project.iconUrl = Optional.of(json.getString("icon_url"));
        }

        if(json.has("donation_urls") && !json.isNull("donation_urls")) {
            JSONArray donationUrls = json.getJSONArray("donation_urls");
            for (Object url : donationUrls) {
                JSONObject donationObj = (JSONObject) url;
                String donationId = donationObj.getString("id");
                String donationPlatform = donationObj.getString("platform");
                String donationUrl = donationObj.getString("url");
                project.donationUrls.add(new ProjectDonationUrl(donationId, donationPlatform, donationUrl));
            }
        }
        
        // project license
        JSONObject licenseObj = json.getJSONObject("license");
        String licenseId = licenseObj.getString("id");
        String licenseName = licenseObj.getString("name");
        String licenseUrl = "";
        if (licenseObj.has("url") && !licenseObj.isNull("url")) {
        	licenseUrl = licenseObj.getString("url");
        }
        project.license = new ProjectLicense(licenseId, licenseName, licenseUrl);
        
        // project versions (downloadable files)
        JSONArray versions = json.getJSONArray("versions");
        for (Object version : versions) {
        	project.versionIds.add((String) version);
        }
        
        // project game versions
        JSONArray gameVersions = json.getJSONArray("game_versions");
        for (Object version : gameVersions) {
        	project.gameVersions.add((String) version);
        }
        
        // project supported loaders
        JSONArray supportedLoaders = json.getJSONArray("loaders");
        for (Object loader : supportedLoaders) {
        	project.modLoaders.add((String) loader);
        }
        
        // project gallery
        if (json.has("gallery") && !json.isNull("gallery")) {
        	JSONArray galleryImgs = json.getJSONArray("gallery");
        	
        	for (int i = 0; i < galleryImgs.length(); i++) {
        		JSONObject image = galleryImgs.getJSONObject(i);
        		String imageUrl = image.getString("url");
        		boolean featured = image.getBoolean("featured");
        		String imageTitle = "";
        		if (image.has("title") && !image.isNull("title")) {
        			imageTitle = image.getString("title");
        		}
        		
        		String imageDesc = "";
        		if(image.has("description") && !image.isNull("description")) {
        			imageDesc = image.getString("description");
        		}
        		
        		OffsetDateTime imageCreationDate = OffsetDateTime.parse(image.getString("created"));
        		
        		int imageOrdering = image.getInt("ordering");
        		
        		project.images.add(new ProjectImage(imageUrl, featured, imageTitle, imageDesc, imageCreationDate, imageOrdering));
        	}
        }
        
		return project;
	}
}