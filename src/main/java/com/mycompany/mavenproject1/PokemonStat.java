package com.mycompany.mavenproject1;

import com.google.gson.annotations.SerializedName;

public class PokemonStat {
    @SerializedName("base_stat")
    private int baseStat;
    private int effort;
    private Stat stat;

    // Default constructor for Gson
    public PokemonStat() {
    }

    public PokemonStat(int baseStat, int effort, Stat stat) {
        this.baseStat = baseStat;
        this.effort = effort;
        this.stat = stat;
    }

    public int getBaseStat() {
        return baseStat;
    }

    public int getEffort() {
        return effort;
    }

    public Stat getStat() {
        return stat;
    }
} 