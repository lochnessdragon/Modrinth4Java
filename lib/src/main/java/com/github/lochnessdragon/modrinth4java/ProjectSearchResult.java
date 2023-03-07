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

	public ProjectSearchResult() {
		
	}
	
	public Project getFullProject() {
		
	}
	
	public static ProjectSearchResult fromJson(JSONObject json) {
		
	}

	
}