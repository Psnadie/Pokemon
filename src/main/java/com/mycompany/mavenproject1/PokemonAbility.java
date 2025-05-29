package com.mycompany.mavenproject1;

public class PokemonAbility {
    private Ability ability;
    private boolean isHidden;
    private int slot;

    public PokemonAbility(Ability ability, boolean isHidden, int slot) {
        this.ability = ability;
        this.isHidden = isHidden;
        this.slot = slot;
    }

    public Ability getAbility() {
        return ability;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public int getSlot() {
        return slot;
    }
} 