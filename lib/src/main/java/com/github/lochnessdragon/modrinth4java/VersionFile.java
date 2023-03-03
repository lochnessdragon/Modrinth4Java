package com.github.lochnessdragon.modrinth4java;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.json.JSONObject;

public class VersionFile {
	public class Hash {
		public enum Algorithm {
			SHA512,
			SHA1
		}
		
		public Algorithm algo;
		public String hash;

        public Hash(Algorithm algo, String hash) {
            this.algo = algo;
            this.hash = hash;
        }
	}
	
	public List<Hash> hashes;
	public String url;
	public String filename;
	public boolean primary;
	public int filesize;
	//Enum: "required-resource-pack" "optional-resource-pack"
	// The type of the additional file, used mainly for adding resource packs to datapacks
	public Optional<String> filetype;

    public VersionFile(String url, String filename) {
        this.url = url;
        this.filename = filename;
        this.hashes = new ArrayList<Hash>();
        this.primary = false;
        this.filesize = 0;
        this.filetype = Optional.empty();
    }
    
    public static VersionFile fromJson(JSONObject json) {
        String url = json.getString("url");
        String filename = json.getString("filename");
        VersionFile file = new VersionFile(url, filename);

        file.primary = json.getBoolean("primary");
        file.filesize = json.getInt("size");

        if (json.has("file_type") && !json.isNull("file_type")) {
            file.filetype = Optional.of(json.getString("file_type"));
        }

        JSONObject hashesObj = json.getJSONObject("hashes");
        Iterator<String> hashesKeys = hashesObj.keys();
        while (hashesKeys.hasNext()) {
            String key = hashesKeys.next();
            if (key.equals("sha512")) {
                file.hashes.add(new Hash(Hash.Algorithm.SHA512, hashesObj.getString(key)));
            } else if (key.equals("sha1")) {
                file.hashes.add(new Hash(Hash.Algorithm.SHA1, hashesObj.getString(key)));
            }
        }
        
        return file;
    }
}
