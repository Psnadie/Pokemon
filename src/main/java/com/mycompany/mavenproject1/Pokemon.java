package com.mycompany.mavenproject1;
public class Pokemon {
    private String name;
    private int id;
    private String type;
    private int baseExperience;
    private int height;
    private int weight;
    private PokemonMove[] moves;

    public Pokemon(String name, int id, String type, int height, int weight, String url) {
        this.name = name;
        this.id = id;
        this.type = type;
        this.height = height;
        this.weight = weight;
    }

    public int getBaseExperience() {
        return baseExperience;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public String getMoves() {
        StringBuilder movesString = new StringBuilder();
        for (int i = 0; i < moves.length; i++) {
            movesString.append(moves[i].getMove().getName());
            if (i < moves.length - 1) {
                movesString.append(", ");
            }
        }
        return movesString.toString();
    }
}
