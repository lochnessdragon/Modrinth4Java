package com.github.lochnessdragon.modrinth4java;

import java.time.OffsetDateTime;

public class ProjectImage {
    public String url;
    public boolean featured;
    public String title;
    public String description;
    public OffsetDateTime createdAt;
    
    // The order of the gallery image. Gallery images are sorted by this field and then alphabetically by title.
    public int ordering;
	
    public ProjectImage(String url, boolean featured, String title, String description,
			OffsetDateTime createdAt, int ordering) {
		this.url = url;
		this.featured = featured;
		this.title = title;
		this.description = description;
		this.createdAt = createdAt;
		this.ordering = ordering;
	}
}