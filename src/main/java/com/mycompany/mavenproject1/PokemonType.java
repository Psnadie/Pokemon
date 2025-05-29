package com.mycompany.mavenproject1;

public class PokemonType {
    private int slot;
    private Type type;

    public PokemonType(int slot, Type type) {
        this.slot = slot;
        this.type = type;
    }

    public int getSlot() {
        return slot;
    }

    public Type getType() {
        return type;
    }
} 