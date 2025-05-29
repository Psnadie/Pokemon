package com.mycompany.mavenproject1;

public class Pokemon {
    private String name;
    private int id;
    private int baseExperience;
    private int height;
    private int weight;
    private PokemonType[] types;
    private PokemonAbility[] abilities;
    private PokemonMove[] moves;
    private PokemonStat[] stats;
    private PokemonSprites sprites;

    // Constructor without parameters for Gson
    public Pokemon() {
    }

    // Full constructor
    public Pokemon(String name, int id, int baseExperience, int height, int weight,
                  PokemonType[] types, PokemonAbility[] abilities, PokemonMove[] moves,
                  PokemonStat[] stats, PokemonSprites sprites) {
        this.name = name;
        this.id = id;
        this.baseExperience = baseExperience;
        this.height = height;
        this.weight = weight;
        this.types = types;
        this.abilities = abilities;
        this.moves = moves;
        this.stats = stats;
        this.sprites = sprites;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getBaseExperience() {
        return baseExperience;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public PokemonType[] getTypes() {
        return types;
    }

    public String getTypesAsString() {
        if (types == null || types.length == 0) return "No types";
        StringBuilder typeString = new StringBuilder();
        for (int i = 0; i < types.length; i++) {
            typeString.append(types[i].getType().getName());
            if (i < types.length - 1) {
                typeString.append(", ");
            }
        }
        return typeString.toString();
    }

    public PokemonAbility[] getAbilities() {
        return abilities;
    }

    public String getAbilitiesAsString() {
        if (abilities == null || abilities.length == 0) return "No abilities";
        StringBuilder abilityString = new StringBuilder();
        for (int i = 0; i < abilities.length; i++) {
            abilityString.append(abilities[i].getAbility().getName());
            if (abilities[i].isHidden()) {
                abilityString.append(" (Hidden)");
            }
            if (i < abilities.length - 1) {
                abilityString.append(", ");
            }
        }
        return abilityString.toString();
    }

    public PokemonMove[] getMoves() {
        return moves;
    }

    public String getMovesAsString() {
        if (moves == null || moves.length == 0) return "No moves";
        StringBuilder movesString = new StringBuilder();
        for (int i = 0; i < moves.length; i++) {
            movesString.append(moves[i].getMove().getName());
            if (i < moves.length - 1) {
                movesString.append(", ");
            }
        }
        return movesString.toString();
    }

    public PokemonStat[] getStats() {
        return stats;
    }

    public String getStatsAsString() {
        if (stats == null || stats.length == 0) return "No stats";
        StringBuilder statsString = new StringBuilder();
        for (PokemonStat stat : stats) {
            statsString.append(stat.getStat().getName())
                      .append(": ")
                      .append(stat.getBaseStat())
                      .append("\n");
        }
        return statsString.toString();
    }

    public PokemonSprites getSprites() {
        return sprites;
    }
}
