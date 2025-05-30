package com.mycompany.mavenproject1;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.List;

import com.google.gson.Gson;
public class PruebaAPI {
    public static void main(String[] args) {
        boolean flag = true;
        while (flag) {
            System.out.println("\nIngresa el nombre del pokemon (exit para salir): ");
            Scanner sc = new Scanner(System.in);
            String pokemonName = sc.nextLine().toLowerCase().trim();
            
            if (pokemonName.equalsIgnoreCase("exit")) {
                System.out.println("Saliendo...");
                flag = false;
                break;
            } else if (pokemonName.isEmpty()) {
                System.out.println("Por favor, ingresa un nombre de pokemon válido.");
                continue;
            }
            
            String url = "https://pokeapi.co/api/v2/pokemon/" + pokemonName;
            try {
                HttpURLConnection apiConnection = fetchApiResponse(url);
            
                if (apiConnection.getResponseCode() != 200) {
                    System.out.println("Error: Pokemon no encontrado o problema de conexión.");
                    continue;
                }
                
                String jsonResponse = readApiResponse(apiConnection);
                Gson gson = new Gson();
                Pokemon pokemon = gson.fromJson(jsonResponse, Pokemon.class);
                
                // Display basic information
                System.out.println("\n=== Información Básica ===");
                System.out.println("Nombre: " + pokemon.getName());
                System.out.println("ID: " + pokemon.getId());   
                System.out.println("Altura: " + pokemon.getHeight() / 10.0 + " m");
                System.out.println("Peso: " + pokemon.getWeight() / 10.0 + " kg");
                System.out.println("Experiencia base: " + pokemon.getBaseExperience());
                
                // Display types
                System.out.println("\n=== Tipos ===");
                System.out.println(pokemon.getTypesAsString());
                
                // Display abilities
                System.out.println("\n=== Habilidades ===");
                System.out.println(pokemon.getAbilitiesAsString());
                
                // Display stats with improved formatting
                System.out.println("\n=== Estadísticas ===");
                PokemonStat[] stats = pokemon.getStats();
                if (stats != null && stats.length > 0) {
                    for (PokemonStat stat : stats) {
                        String statName = stat.getStat().getName();
                        System.out.printf("%-20s: %d%n", statName, stat.getBaseStat());
                    }
                } else {
                    System.out.println("No hay estadísticas disponibles");
                }
                
                // Display moves
                System.out.println("\n=== Movimientos ===");
                System.out.println(pokemon.getMovesAsString());
                
                // Display sprite URLs
                System.out.println("\n=== Sprites ===");
                PokemonSprites sprites = pokemon.getSprites();
                if (sprites != null) {
                    if (sprites.getFrontDefault() != null) 
                        System.out.println("Sprite frontal: " + sprites.getFrontDefault());
                    if (sprites.getBackDefault() != null) 
                        System.out.println("Sprite trasero: " + sprites.getBackDefault());
                    if (sprites.getFrontShiny() != null) 
                        System.out.println("Sprite shiny frontal: " + sprites.getFrontShiny());
                    if (sprites.getBackShiny() != null) 
                        System.out.println("Sprite shiny trasero: " + sprites.getBackShiny());
                }

                // Display species information
                PokemonSpecies speciesRef = pokemon.getSpecies();
                if (speciesRef != null) {
                    // Fetch detailed species information
                    try {
                        HttpURLConnection speciesConnection = fetchApiResponse(speciesRef.getUrl());
                        if (speciesConnection.getResponseCode() == 200) {
                            String speciesJson = readApiResponse(speciesConnection);
                            Species speciesDetails = new Gson().fromJson(speciesJson, Species.class);
                            
                            System.out.println("\n========== Species Information ==========");
                            // Basic characteristics
                            System.out.println("Category: " + findEnglishGenus(speciesDetails.getGenera()));
                            System.out.println("Capture Rate: " + speciesDetails.getCaptureRate());
                            System.out.println("Base Happiness: " + speciesDetails.getBaseHappiness());
                            
                            // Special flags
                            if (speciesDetails.isBaby()) System.out.println("This is a baby Pokémon!");
                            if (speciesDetails.isLegendary()) System.out.println("This is a legendary Pokémon!");
                            if (speciesDetails.isMythical()) System.out.println("This is a mythical Pokémon!");
                            
                            // Physical characteristics
                            if (speciesDetails.getColor() != null) {
                                System.out.println("Color: " + speciesDetails.getColor().getName());
                            }
                            if (speciesDetails.getShape() != null) {
                                System.out.println("Shape: " + speciesDetails.getShape().getName());
                            }
                            
                            // Breeding information
                            System.out.println("\n=== Breeding Information ===");
                            System.out.println("Gender Rate: " + getGenderRateDescription(speciesDetails.getGenderRate()));
                            System.out.println("Hatch Counter: " + speciesDetails.getHatchCounter() + " cycles");
                            if (speciesDetails.getEggGroups() != null && !speciesDetails.getEggGroups().isEmpty()) {
                                System.out.println("Egg Groups: " + getEggGroupsString(speciesDetails.getEggGroups()));
                            }
                            
                            // Evolution information
                            System.out.println("\n=== Evolution Information ===");
                            if (speciesDetails.getEvolvesFromSpecies() != null) {
                                System.out.println("Evolves from: " + 
                                    speciesDetails.getEvolvesFromSpecies().getName());
                            } else {
                                System.out.println("This is a basic Pokémon (does not evolve from anything)");
                            }
                            
                            // Description
                            System.out.println("\n=== Pokédex Entry ===");
                            String description = findEnglishFlavorText(speciesDetails.getFlavorTextEntries());
                            if (description != null) {
                                System.out.println(description);
                            }
                            
                            // Variants/Forms
                            if (speciesDetails.getVarieties() != null && !speciesDetails.getVarieties().isEmpty()) {
                                System.out.println("\n=== Forms & Variants ===");
                                for (Species.PokemonVariety variety : speciesDetails.getVarieties()) {
                                    String defaultMark = variety.isDefault() ? " (Default Form)" : "";
                                    System.out.println("\n➤ " + variety.getPokemon().getName().toUpperCase() + defaultMark);
                                    System.out.println("  " + "=".repeat(40));
                                    
                                    try {
                                        // Fetch variant Pokemon data
                                        HttpURLConnection variantConnection = fetchApiResponse(variety.getPokemon().getUrl());
                                        if (variantConnection.getResponseCode() == 200) {
                                            String variantJson = readApiResponse(variantConnection);
                                            Pokemon variantPokemon = new Gson().fromJson(variantJson, Pokemon.class);
                                            
                                            // Display variant stats
                                            System.out.println("  Base Stats:");
                                            for (PokemonStat stat : variantPokemon.getStats()) {
                                                String statName = formatStatName(stat.getStat().getName());
                                                System.out.printf("  %-15s: %d%n", statName, stat.getBaseStat());
                                            }
                                            
                                            // Display variant types
                                            System.out.println("\n  Types: " + variantPokemon.getTypesAsString());
                                            
                                            // Display variant abilities
                                            System.out.println("\n  Abilities:");
                                            for (PokemonAbility ability : variantPokemon.getAbilities()) {
                                                String hiddenMark = ability.isHidden() ? " (Hidden)" : "";
                                                System.out.println("  - " + ability.getAbility().getName() + hiddenMark);
                                            }
                                            
                                            // Display height and weight
                                            System.out.printf("\n  Height: %.1f m%n", variantPokemon.getHeight() / 10.0);
                                            System.out.printf("  Weight: %.1f kg%n", variantPokemon.getWeight() / 10.0);
                                        }
                                    } catch (IOException e) {
                                        System.out.println("  Error fetching variant details: " + e.getMessage());
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("Error fetching species details: " + e.getMessage());
                    }
                }
                
            } catch(IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private static String formatStatName(String statName) {
        switch (statName) {
            case "hp": return "HP";
            case "attack": return "Attack";
            case "defense": return "Defense";
            case "special-attack": return "Sp. Attack";
            case "special-defense": return "Sp. Defense";
            case "speed": return "Speed";
            default: return statName;
        }
    }
    
    // Helper methods for formatting species information
    private static String findEnglishGenus(List<Species.Genus> genera) {
        if (genera != null) {
            for (Species.Genus genus : genera) {
                if (genus.getLanguage() != null && 
                    genus.getLanguage().getName().equals("en")) {
                    return genus.getGenus();
                }
            }
        }
        return "Unknown";
    }
    
    private static String findEnglishFlavorText(List<Species.FlavorTextEntry> entries) {
        if (entries != null) {
            for (Species.FlavorTextEntry entry : entries) {
                if (entry.getLanguage() != null && 
                    entry.getLanguage().getName().equals("en")) {
                    return entry.getFlavorText()
                        .replace("\n", " ")
                        .replace("\f", " ");
                }
            }
        }
        return null;
    }
    
    private static String getGenderRateDescription(int genderRate) {
        if (genderRate == -1) return "Genderless";
        double femalePercentage = (genderRate / 8.0) * 100;
        double malePercentage = 100 - femalePercentage;
        return String.format("%.1f%% Male, %.1f%% Female", malePercentage, femalePercentage);
    }
    
    private static String getEggGroupsString(List<Species.NamedAPIResource> eggGroups) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < eggGroups.size(); i++) {
            sb.append(capitalizeFirstLetter(eggGroups.get(i).getName()));
            if (i < eggGroups.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
    
    private static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public static HttpURLConnection fetchApiResponse(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        return conn;
    }
    
    public static String readApiResponse(HttpURLConnection apiConnection) throws IOException {
        StringBuilder resultJson = new StringBuilder();
        Scanner sc = new Scanner(apiConnection.getInputStream());
        
        while(sc.hasNext()) {
            resultJson.append(sc.nextLine());
        }
        
        sc.close();
        return resultJson.toString();
    }
}
