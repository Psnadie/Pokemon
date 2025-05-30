package com.mycompany.mavenproject1;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Species {
    private int id;
    private String name;
    private int order;
    @SerializedName("gender_rate")
    private int genderRate;
    @SerializedName("capture_rate")
    private int captureRate;
    @SerializedName("base_happiness")
    private int baseHappiness;
    @SerializedName("is_baby")
    private boolean isBaby;
    @SerializedName("is_legendary")
    private boolean isLegendary;
    @SerializedName("is_mythical")
    private boolean isMythical;
    @SerializedName("hatch_counter")
    private int hatchCounter;
    @SerializedName("has_gender_differences")
    private boolean hasGenderDifferences;
    @SerializedName("forms_switchable")
    private boolean formsSwitchable;
    @SerializedName("growth_rate")
    private NamedAPIResource growthRate;
    @SerializedName("pokedex_numbers")
    private List<PokedexNumber> pokedexNumbers;
    @SerializedName("egg_groups")
    private List<NamedAPIResource> eggGroups;
    private NamedAPIResource color;
    private NamedAPIResource shape;
    @SerializedName("evolves_from_species")
    private NamedAPIResource evolvesFromSpecies;
    @SerializedName("evolution_chain")
    private EvolutionChain evolutionChain;
    private NamedAPIResource habitat;
    private NamedAPIResource generation;
    private List<Name> names;
    @SerializedName("flavor_text_entries")
    private List<FlavorTextEntry> flavorTextEntries;
    @SerializedName("form_descriptions")
    private List<FormDescription> formDescriptions;
    private List<Genus> genera;
    private List<PokemonVariety> varieties;

    // Nested classes
    public static class NamedAPIResource {
        private String name;
        private String url;

        public String getName() { return name; }
        public String getUrl() { return url; }
    }

    public static class PokedexNumber {
        @SerializedName("entry_number")
        private int entryNumber;
        private NamedAPIResource pokedex;

        public int getEntryNumber() { return entryNumber; }
        public NamedAPIResource getPokedex() { return pokedex; }
    }

    public static class EvolutionChain {
        private String url;

        public String getUrl() { return url; }
    }

    public static class Name {
        private String name;
        private NamedAPIResource language;

        public String getName() { return name; }
        public NamedAPIResource getLanguage() { return language; }
    }

    public static class FlavorTextEntry {
        @SerializedName("flavor_text")
        private String flavorText;
        private NamedAPIResource language;
        private NamedAPIResource version;

        public String getFlavorText() { return flavorText; }
        public NamedAPIResource getLanguage() { return language; }
        public NamedAPIResource getVersion() { return version; }
    }

    public static class FormDescription {
        private String description;
        private NamedAPIResource language;

        public String getDescription() { return description; }
        public NamedAPIResource getLanguage() { return language; }
    }

    public static class Genus {
        private String genus;
        private NamedAPIResource language;

        public String getGenus() { return genus; }
        public NamedAPIResource getLanguage() { return language; }
    }

    public static class PokemonVariety {
        @SerializedName("is_default")
        private boolean isDefault;
        private NamedAPIResource pokemon;

        public boolean isDefault() { return isDefault; }
        public NamedAPIResource getPokemon() { return pokemon; }
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getOrder() { return order; }
    public int getGenderRate() { return genderRate; }
    public int getCaptureRate() { return captureRate; }
    public int getBaseHappiness() { return baseHappiness; }
    public boolean isBaby() { return isBaby; }
    public boolean isLegendary() { return isLegendary; }
    public boolean isMythical() { return isMythical; }
    public int getHatchCounter() { return hatchCounter; }
    public boolean hasGenderDifferences() { return hasGenderDifferences; }
    public boolean isFormsSwitchable() { return formsSwitchable; }
    public NamedAPIResource getGrowthRate() { return growthRate; }
    public List<PokedexNumber> getPokedexNumbers() { return pokedexNumbers; }
    public List<NamedAPIResource> getEggGroups() { return eggGroups; }
    public NamedAPIResource getColor() { return color; }
    public NamedAPIResource getShape() { return shape; }
    public NamedAPIResource getEvolvesFromSpecies() { return evolvesFromSpecies; }
    public EvolutionChain getEvolutionChain() { return evolutionChain; }
    public NamedAPIResource getHabitat() { return habitat; }
    public NamedAPIResource getGeneration() { return generation; }
    public List<Name> getNames() { return names; }
    public List<FlavorTextEntry> getFlavorTextEntries() { return flavorTextEntries; }
    public List<FormDescription> getFormDescriptions() { return formDescriptions; }
    public List<Genus> getGenera() { return genera; }
    public List<PokemonVariety> getVarieties() { return varieties; }
} 