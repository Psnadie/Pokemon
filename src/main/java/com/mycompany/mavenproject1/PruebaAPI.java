package com.mycompany.mavenproject1;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.Gson;
public class PruebaAPI {
    public static void main(String[] args) {
        System.out.println("Ingresa el nombre del pokemon");
        Scanner sc = new Scanner(System.in);
        String pokemonName = sc.nextLine();
        String url = "https://pokeapi.co/api/v2/pokemon/" + pokemonName;
        try{
            HttpURLConnection apiConnection = fetchApiResponse(url);
        
            if (apiConnection.getResponseCode() != 200) {
                System.out.println("Error: No se ha podido establecer la conexion a la API");
            }
            String jsonResponse = readApiResponse(apiConnection);
            Gson gson = new Gson();
            Pokemon pokemon = gson.fromJson(jsonResponse, Pokemon.class);
            System.out.println("Nombre: " + pokemon.getName());
            System.out.println("ID: " + pokemon.getId());   
            System.out.println("Tipo: " + pokemon.getType());
            System.out.println("Altura: " + pokemon.getHeight());
            System.out.println("Peso: " + pokemon.getWeight());
            System.out.println("Experiencia base: " + pokemon.getBaseExperience());
            System.out.println("Movimientos: " + pokemon.getMoves());
            
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    private static HttpURLConnection fetchApiResponse(String urlString){
        try{
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            conn.setRequestMethod("GET");
            
            return conn;
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
    
    private static String readApiResponse(HttpURLConnection apiConnection){
        try{
            StringBuilder resultJson = new StringBuilder();
            
            Scanner sc = new Scanner(apiConnection.getInputStream());
            
            while(sc.hasNext()){
                resultJson.append(sc.nextLine());
            }
            
            sc.close();
            
            return resultJson.toString();
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
