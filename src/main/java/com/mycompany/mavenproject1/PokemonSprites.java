package com.mycompany.mavenproject1;

public class PokemonSprites {
    private String frontDefault;
    private String frontShiny;
    private String frontFemale;
    private String frontShinyFemale;
    private String backDefault;
    private String backShiny;
    private String backFemale;
    private String backShinyFemale;

    public PokemonSprites(String frontDefault, String frontShiny, String frontFemale, String frontShinyFemale,
                         String backDefault, String backShiny, String backFemale, String backShinyFemale) {
        this.frontDefault = frontDefault;
        this.frontShiny = frontShiny;
        this.frontFemale = frontFemale;
        this.frontShinyFemale = frontShinyFemale;
        this.backDefault = backDefault;
        this.backShiny = backShiny;
        this.backFemale = backFemale;
        this.backShinyFemale = backShinyFemale;
    }

    public String getFrontDefault() {
        return frontDefault;
    }

    public String getFrontShiny() {
        return frontShiny;
    }

    public String getFrontFemale() {
        return frontFemale;
    }

    public String getFrontShinyFemale() {
        return frontShinyFemale;
    }

    public String getBackDefault() {
        return backDefault;
    }

    public String getBackShiny() {
        return backShiny;
    }

    public String getBackFemale() {
        return backFemale;
    }

    public String getBackShinyFemale() {
        return backShinyFemale;
    }
} 