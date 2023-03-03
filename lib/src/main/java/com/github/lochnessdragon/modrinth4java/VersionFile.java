package com.github.lochnessdragon.modrinth4java;

import java.util.List;
import java.util.Optional;

public class VersionFile {
	public class Hash {
		public enum Algorithm {
			SHA512,
			SHA1
		}
		
		Algorithm hashAlgo;
		String hash;
	}
	
	public List<Hash> hashes;
	public String url;
	public String filename;
	public boolean primary;
	public int filesize;
	//Enum: "required-resource-pack" "optional-resource-pack"
	// The type of the additional file, used mainly for adding resource packs to datapacks
	public Optional<String> filetype;
}
