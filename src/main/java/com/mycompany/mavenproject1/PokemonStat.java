package com.mycompany.mavenproject1;

public class PokemonStat {
    private int baseStat;
    private int effort;
    private Stat stat;

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