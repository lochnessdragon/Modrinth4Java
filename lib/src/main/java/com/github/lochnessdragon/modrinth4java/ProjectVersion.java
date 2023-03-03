package com.github.lochnessdragon.modrinth4java;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public class ProjectVersion {
	public enum Type {
		RELEASE,
		BETA,
		ALPHA
	}
	
	public enum Status {
		LISTED,
		ARCHIVED,
		DRAFT,
		UNLISTED,
		SCHEDULED,
		UNKNOWN,
	}
	
	public String name;
	public String versionNumber;
	public Optional<String> changelog;
	public List<VersionDependency> depedencies;
	public List<String> gameVersions;
	public Type type;
	public List<String> modLoaders;
	public boolean featured;
	public Status status;
	public Optional<Status> requestedStatus;
	public String id;
	public String projectId;
	public String authorId;
	public OffsetDateTime publishedAt;
	public int downloads;
	public List<VersionFile> files;
}
