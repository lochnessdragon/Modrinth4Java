package com.github.lochnessdragon.modrinth4java;

import java.util.Date;
import java.util.Optional;

public class ProjectImage {
    public String url;
    public boolean featured;
    public Optional<String> title;
    public Optional<String> description;
    public Date createdAt;
    public int ordering;
}