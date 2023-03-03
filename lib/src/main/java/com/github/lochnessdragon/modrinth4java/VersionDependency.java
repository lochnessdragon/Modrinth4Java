package com.github.lochnessdragon.modrinth4java;

import java.util.Optional;
import java.util.ArrayList;

import org.json.JSONObject;

public class VersionDependency {
	public enum Enforcement {
		REQUIRED,
		OPTIONAL,
		INCOMPATIBLE,
		EMBEDDED
	}
	
	public Optional<String> versionId;
	public Optional<String> projectId;
	public Optional<String> filename;
	public Enforcement enforcementPolicy;

    public VersionDependency(Enforcement policy) {
        VersionDepedency(policy, Optional.empty(), Optional.empty(), Optional.empty());
    }

    public VersionDependency(Enforcement policy, String versionId, String projectId, String filename) {
        this.enforcementPolicy = policy;
        this.versionId = Optional.of(versionId);
        this.projectId = Optional.of(projectId);
        this.filename = Optional.of(filename);
    }
    
    public VersionDependency(Enforcement policy, Optional<String> versionId, Optional<String> projectId, Optional<String> filename) {
        this.enforcementPolicy = policy;
        this.versionId = versionId;
        this.projectId = projectId;
        this.filename = filename;
    }

    public static VersionDependency fromJson(JSONObject json) {
        Enforcement dependencyType = Enforcement.valueOf(json.getString("depedency_type").toUpperCase());
        VersionDependency dependency = new VersionDependency(dependencyType);

        if(json.has("version_id") && !json.isNull("version_id")) {
            dependency.versionId = Optional.of(json.getString("version_id"));
        }
        
        if(json.has("project_id") && !json.isNull("project_id")) {
            dependency.projectId = Optional.of(json.getString("project_id"));
        }

        if(json.has("file_name") && !json.isNull("file_name")) {
            dependency.filename = Optional.of(json.getString("file_name"));
        }
        
        return dependency;
    }
}
