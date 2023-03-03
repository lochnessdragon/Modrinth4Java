package com.github.lochnessdragon.modrinth4java;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

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

    public static ProjectVersion.fromJson(JSONObject json) {
        return new ProjectVersion();
    }
}
