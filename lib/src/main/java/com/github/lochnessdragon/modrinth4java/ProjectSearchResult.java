package com.github.lochnessdragon.modrinth4java;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProjectSearchResult {
	public final String id;
	public final String slug;
	public String title;
	public String shortDesc;
	public String authorName;
	public List<String> categories;

	public RequiredStatus requiredClientSide;
    public RequiredStatus requiredServerSide;
    public ProjectType type;

	public final int downloadCount;
	public final int followerCount;
	public Optional<String> iconUrl;
	public int iconColor;

	public List<String> gameVersions;
	public String latestGameVersion;
	public OffsetDateTime publishedAt;
    public OffsetDateTime updatedAt;
	public String licenseId;

	public List<String> galleryImgIds;
	public String featuredImageId;

	public ProjectSearchResult(String id, String slug, int downloadCount, int followerCount) {
		this.id = id;
		this.slug = slug;
		this.downloadCount = downloadCount;
		this.followerCount = followerCount;

		this.gameVersions = new ArrayList<String>();
		this.galleryImgIds = new ArrayList<String>();
		this.categories = new ArrayList<String>();
	}
	
	public Project getFullProject() {
		return ModrinthAPI.getProject(this.id);
	}
	
	public static ProjectSearchResult fromJson(JSONObject json) {
		String id = json.getString("project_id");
		String slug = json.getString("slug");
		int downloadCount = json.getInt("downloads");
		int followerCount = json.getInt("follows");
		ProjectSearchResult result = new ProjectSearchResult(id, slug, downloadCount, followerCount);
		
		result.title = json.getString("title");
		result.shortDesc = json.getString("description");

		if(json.has("categories") && !json.isNull("categories")) {
			JSONArray categoriesJson = json.getJSONArray("categories");
			for(Object category : categoriesJson) {
				result.categories.add(category);
			}
		}

		result.requiredClientSide = RequiredStatus.valueOf(json.getString("client_side"));
		result.requiredServerSide = RequiredStatus.valueOf(json.getString("server_side"));
		result.type = ProjectType.valueOf(json.getString("project_type"));

		if(json.has("icon_url") && !json.isNull("icon_url")) {
			result.iconUrl = Optional.of(json.getString("icon_url"));
		}
		
		return result;
	}

	
}