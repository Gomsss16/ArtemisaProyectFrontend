package co.edu.unbosque.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Servicio utilitario para realizar peticiones HTTP (GET, POST, DELETE) hacia
 * servicios externos que exponen APIs REST.
 * 
 * <p>
 * Este servicio está diseñado para trabajar con datos en formato JSON,
 * estableciendo los encabezados correspondientes en cada petición.
 * </p>
 * 
 * <p>
 * Los métodos de esta clase son estáticos, por lo cual no es necesario
 * instanciar {@code LinkService} para utilizarlos.
 * </p>
 * 
 * Ejemplo de uso:
 * 
 * <pre>{@code
 * String respuesta = LinkService.doGet("http://localhost:8080/api/links");
 * System.out.println(respuesta);
 * }</pre>
 * 
 * @author
 * @version 1.0
 */
public class LinkService {

	/**
	 * Realiza una petición HTTP GET hacia la URL indicada.
	 *
	 * @param urlString la URL del recurso destino
	 * @return la respuesta del servidor en formato {@code String}, incluyendo el
	 *         código de estado o un mensaje de error si ocurre una excepción
	 */
	public static String doGet(String urlString) {
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);

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
			connection.disconnect();

			return responseCode + "\n" + response.toString();

		} catch (Exception e) {
			return "Error: " + e.getMessage();
		}
	}

	/**
	 * Realiza una petición HTTP POST hacia la URL indicada enviando datos en
	 * formato JSON.
	 *
	 * @param urlString la URL del recurso destino
	 * @param jsonData  los datos en formato JSON a enviar en el cuerpo de la
	 *                  petición
	 * @return la respuesta del servidor en formato {@code String}, incluyendo el
	 *         código de estado o un mensaje de error si ocurre una excepción
	 */
	public static String doPost(String urlString, String jsonData) {
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
			connection.setDoOutput(true);
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);

			byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);

			try (OutputStream os = connection.getOutputStream()) {
				os.write(input, 0, input.length);
			}

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
			connection.disconnect();

			return responseCode + "\n" + response.toString();

		} catch (Exception e) {
			return "Error: " + e.getMessage();
		}
	}

	/**
	 * Realiza una petición HTTP DELETE hacia la URL indicada.
	 *
	 * @param urlString la URL del recurso destino
	 * @return la respuesta del servidor en formato {@code String}, incluyendo el
	 *         código de estado o un mensaje de error si ocurre una excepción
	 */
	public static String doDelete(String urlString) {
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("DELETE");
			connection.setRequestProperty("Accept", "application/json");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);

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
			connection.disconnect();

			return responseCode + "\n" + response.toString();

		} catch (Exception e) {
			return "Error: " + e.getMessage();
		}
	}

}
