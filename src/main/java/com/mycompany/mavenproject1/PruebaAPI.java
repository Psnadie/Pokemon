package com.mycompany.mavenproject1;
import java.net.http.*;
import java.net.URI;
import java.io.IOException;
import com.google.gson.*;
import java.util.Scanner;
import java.net.HttpURLConnection;
import java.net.URL;
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
            System.out.println(jsonResponse);
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
