package co.edu.unbosque.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LibroService {

    private static String readResponse(HttpURLConnection connection) throws Exception {
        int responseCode = connection.getResponseCode();
        BufferedReader reader;
        if (responseCode >= 200 && responseCode < 300) {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        } else {
            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
        }

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString(); // 🔹 solo JSON limpio
    }

    public static String doGet(String urlString) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            return readResponse(connection);

        } catch (Exception e) {
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    public static String doPost(String urlString, String jsonData) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonData.getBytes(StandardCharsets.UTF_8));
            }

            return readResponse(connection);

        } catch (Exception e) {
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    public static String doDelete(String urlString) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            return readResponse(connection);

        } catch (Exception e) {
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }
}
