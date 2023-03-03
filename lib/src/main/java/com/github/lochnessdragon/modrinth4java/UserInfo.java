package com.github.lochnessdragon.modrinth4java;

import java.time.OffsetDateTime;
import java.util.Optional;

public class UserInfo {
	public enum Role {
		ADMIN,
		MODERATOR,
		DEV
	}
	
	public String id;
	public Optional<Integer> githubId;
	public String avatarUrl;
	
	public String username;
	public Optional<String> name;
	public Optional<String> email;
	public String bio;
	
	public OffsetDateTime createdAt;
	public Role role;
	
	// part of the api, but unused
	// public Bitfield badges;
	
	// unimplemented (requires authorized access)
	// public PayoutData payoutData;
}
