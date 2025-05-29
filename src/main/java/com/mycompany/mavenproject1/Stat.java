package com.mycompany.mavenproject1;

public class Stat {
    private String name;
    private String url;

    // Default constructor for Gson
    public Stat() {
    }

    public Stat(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
} 