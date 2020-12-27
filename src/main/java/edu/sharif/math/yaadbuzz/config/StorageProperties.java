package edu.sharif.math.yaadbuzz.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "/data/files";

    public String getLocation() {
	return location;
    }

    public void setLocation(String location) {
	this.location = location;
    }

}