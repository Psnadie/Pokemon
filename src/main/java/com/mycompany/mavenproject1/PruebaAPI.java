package com.mycompany.mavenproject1;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

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
                
            } catch(IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
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
