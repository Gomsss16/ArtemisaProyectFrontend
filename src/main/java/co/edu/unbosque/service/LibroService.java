package co.edu.unbosque.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LibroService {

    public static String doGet(String urlString) {
        try {
            System.out.println("=== LIBRO SERVICE GET (FRONTEND) ===");
            System.out.println("URL: " + urlString);
            
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            
            connection.setConnectTimeout(15000); 
            connection.setReadTimeout(60000);     
            
            int responseCode = connection.getResponseCode();
            System.out.println("GET Response Code: " + responseCode);

            BufferedReader reader;
            if (responseCode >= 200 && responseCode < 300) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
            }

            StringBuilder response = new StringBuilder();
            response.append(responseCode).append("\n");
            
            String line;
            int totalChars = 0;
            while ((line = reader.readLine()) != null) {
                response.append(line);
                totalChars += line.length();
                
                // Log progreso cada MB aproximadamente
                if (totalChars % 1000000 == 0) {
                    System.out.println("Procesados ~" + (totalChars / 1000000) + "MB de respuesta...");
                }
            }
            reader.close();
            connection.disconnect();

            System.out.println("Respuesta total recibida: " + response.length() + " caracteres");
            return response.toString();

        } catch (java.net.SocketTimeoutException e) {
            System.err.println("TIMEOUT leyendo respuesta (archivos muy grandes): " + e.getMessage());
            return "Error: Timeout - archivos demasiado grandes";
        } catch (Exception e) {
            System.err.println("Error en GET: " + e.getMessage());
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    public static String doPost(String urlString, String jsonData) {
        try {
            System.out.println("=== LIBRO SERVICE POST (FRONTEND) ===");
            System.out.println("URL: " + urlString);
            System.out.println("JSON data size: " + jsonData.length() + " caracteres");
            
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(120000);  

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
                System.out.println("Enviando " + input.length + " bytes...");
                os.write(input, 0, input.length);
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            System.out.println("POST Response Code: " + responseCode);

            BufferedReader reader;
            if (responseCode >= 200 && responseCode < 300) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
            }

            StringBuilder response = new StringBuilder();
            response.append(responseCode).append("\n");
            
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();

            System.out.println("POST Response recibida: " + response.length() + " caracteres");
            return response.toString();

        } catch (java.net.SocketTimeoutException e) {
            System.err.println("TIMEOUT enviando datos grandes: " + e.getMessage());
            return "Error: Timeout - archivo demasiado grande";
        } catch (Exception e) {
            System.err.println("Error en POST: " + e.getMessage());
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    public static String doDelete(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            response.append(responseCode).append("\n");
            
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return response.toString();

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
