package com.github.lochnessdragon.modrinth4java;

import java.util.HashSet;
import java.util.Set;
import java.util.Collection;

// Facet.builder().AND(FacetBuilder.OR(), FACETB).build();
public class FacetBuilder {
    protected Set<String> categories;
    protected Set<String> versions;
    protected Set<String> loaders;
    protected Set<String> licenses;
        
    protected FacetBuilder() {
        categories = new HashSet<String>();
        versions = new HashSet<String>();
        loaders = new HashSet<String>();
        licenses = new HashSet<String>();
    }

    public static FacetBuilder facet() {
        return new FacetBuilder();
    }

    public FacetBuilder withCategories(Collection<String> categories) {
        this.categories.addAll(categories);
        return this;
    }

    public FacetBuilder withVersions(Collection<String> versions) {
        this.versions.addAll(versions);
        return this;
    }

    public FacetBuilder withLoaders(Collection<String> loaders) {
        this.loaders.addAll(loaders);
        return this;
    }

    public FacetBuilder withLicenses(Collection<String> licenses) {
        this.licenses.addAll(licenses);
        return this;
    }

    private String addSet(String jsonSoFar, String key, Set<String> values) {
        if (values.size() > 0) {
            jsonSoFar += "[";
            for (int i = 0; i < values.size(); i++) {
                String kvPair = key + ":" + value;
                jsonSoFar += "\"" + kvPair + "\"";
                if (i < values.size() - 1) {
                    jsonSoFar += ",";
                }
            }
            jsonSoFar += "]";
        }
        return jsonSoFar;
    }

    public String build() {
        String facet = "[";

        facet = addSet(facet, "categories", categories);
        facet = addSet(facet, "versions", versions);
        facet = addSet(facet, "loaders", loaders);
        facet = addSet(facet, "licenses", licenses);

        facet += "]";
        return facet;
    }
}