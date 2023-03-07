package com.github.lochnessdragon.modrinth4java;

import java.util.Optional;
import java.util.BitSet;

public class TeamMemberInfo {
	public final String teamId;
	public UserInfo user;

	public String teamRole;
	public BitSet permissions;

	public Optional<Boolean> acceptedMembership;

	public int payoutsSplit;
	public int order;

	public TeamMemberInfo(String teamId) {
		this.teamId = teamId;
	}
}