package com.github.lochnessdragon.modrinth4java;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

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

    public String id;
	public String name;
    public Type type;
	public String versionNumber;
	public Optional<String> changelog;
	public List<VersionDependency> dependencies;
	public List<String> gameVersions;
	public List<String> modLoaders;
	public boolean featured;
	public Status status;
	public Optional<Status> requestedStatus;
	public String projectId;
	public String authorId;
	public OffsetDateTime publishedAt;
	public int downloads;
	public List<VersionFile> files;

	public ProjectVersion(String id, String name, Type type, String versionNumber, Status status, String projectId, String authorId) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.versionNumber = versionNumber;
		this.status = status;
		this.projectId = projectId;
		this.authorId = authorId;
		this.downloads = 0;
		this.changelog = Optional.empty();
		this.dependencies = new ArrayList<VersionDependency>();
		this.gameVersions = new ArrayList<String>();
		this.modLoaders = new ArrayList<String>();
		this.featured = false;
		this.requestedStatus = Optional.empty();
		this.publishedAt = OffsetDateTime.now();
		this.files = new ArrayList<VersionFile>();
	}
	
    @Override
    public String toString() {
        String repr = featured ? "**FEATURED** " : "";
        repr += "Version: " + this.name + " " + this.versionNumber + " [" + this.type.toString().toLowerCase() + "]\n";
        repr += " - UUID: " + this.id + "\n";
        
        repr += " - Status: " + this.status.toString().toLowerCase();
        if (requestedStatus.isPresent()) {
            repr += " Requested Status: " + this.requestedStatus.get().toString().toLowerCase();    
        }
        repr += "\n";

        repr += " - Project: " + this.projectId + " Author: " + this.authorId;
        if(gameVersions.size() > 0) {
            repr += " - MC Versions: [";
            for (int i = 0; i < gameVersions.size(); i++) {
                repr += gameVersions.get(i);

                if (i < gameVersions.size() - 1) {
                    repr += ", ";
                }
            }
            repr += "]\n";
        }
        if(modLoaders.size() > 0) {
            repr += " - Mod Loaders: [";
            for (int i = 0; i < modLoaders.size(); i++) {
                repr += modLoaders.get(i);

                if (i < gameVersions.size() - 1) {
                    repr += ", ";
                }
            }
            repr += "]\n";
        }
        repr += " - Published at: " + this.publishedAt + "\n";
        repr += " - Download Count: " + this.downloads + "\n";

        if (this.changelog.isPresent()) {
            repr += "Changelog\n" + this.changelog.get();
        }

        for (VersionFile file : files) {
            repr += "File: " + file.filename + " (" + file.filesize + " bytes) [" + file.url + "]";
            if (file.primary) {
                repr += " <--- PRIMARY";
            }
            repr += "\n";

            for (VersionFile.Hash hash : file.hashes) {
                repr += " - Hash: " + hash.algo.toString() + ": " + hash.hash + "\n";
            }
        }

        if (dependencies.size() > 0) {
            repr += "Dependencies:\n";
            for (VersionDependency dependency : dependencies) {
                repr += " - " + dependency.enforcementPolicy.toString() + ": " + dependency.filename.orElse("") + " [" + dependency.versionId.orElse("") + "] (" + dependency.projectId.orElse("") + ")\n";
            }
        }

        return repr;
    }

    public static ProjectVersion fromJson(JSONObject json) {
        String id = json.getString("id");
		String projectId = json.getString("project_id");
		String authorId = json.getString("author_id");
		Type versionType = Type.valueOf(json.getString("version_type").toUpperCase());
		Status versionStatus = Status.valueOf(json.getString("status").toUpperCase());
		String name = json.getString("name");
		String versionNumber = json.getString("version_number");
		ProjectVersion version = new ProjectVersion(id, name, versionType, versionNumber, versionStatus, projectId, authorId);

		// read changelog
		if (json.has("changelog") && !json.isNull("changelog")) {
			version.changelog = Optional.of(json.getString("changelog"));
		}

		// read dependencies
		JSONArray dependencies = json.getJSONArray("dependencies");
		for (Object dependencyObj : dependencies) {
			version.dependencies.add(VersionDependency.fromJson((JSONObject) dependencyObj));
		}

        // read game versions
        for (Object gameVersion : json.getJSONArray("game_versions")) {
            version.gameVersions.add((String) gameVersion);
        }

        // read loader versions
        for (Object modLoader : json.getJSONArray("loaders")) {
            version.modLoaders.add((String) modLoader);
        }

        // is the version featured
        version.featured = json.getBoolean("featured");

        // the new requested status (only used sometimes)
        if(json.has("requested_status") && !json.isNull("requested_status")) {
            version.requestedStatus = Optional.of(Status.valueOf(json.getString("requested_status").toUpperCase()));
        }

        // the date it was published
        version.publishedAt = OffsetDateTime.parse(json.getString("date_published"));

        // downloads for the project
        version.downloads = json.getInt("downloads");

        // files
        JSONArray filesArray = json.getJSONArray("files");
        for (Object file : filesArray) {
            version.files.add(VersionFile.fromJson((JSONObject) file));
        }
		
		return version;
    }
}
