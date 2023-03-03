package com.github.lochnessdragon.modrinth4java;

import java.util.Optional;

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
}
