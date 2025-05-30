package com.mycompany.mavenproject1;

public class PokemonSpecies {
    private String name;
    private String url;

    // Default constructor for Gson
    public PokemonSpecies() {
    }

    public PokemonSpecies(String name, String url) {
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