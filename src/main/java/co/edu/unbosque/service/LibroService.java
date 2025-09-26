package co.edu.unbosque.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Servicio que maneja las operaciones HTTP relacionadas con libros.
 * <p>
 * Esta clase proporciona métodos estáticos para realizar peticiones
 * {@code GET}, {@code POST} y {@code DELETE} a un backend o API REST,
 * utilizando {@link java.net.HttpURLConnection}.
 * </p>
 *
 * <p>
 * Los métodos retornan una cadena que incluye:
 * <ul>
 * <li>El código de estado HTTP.</li>
 * <li>El cuerpo de la respuesta en formato JSON o el mensaje de error.</li>
 * </ul>
 * </p>
 *
 * Ejemplo de respuesta:
 * 
 * <pre>
 * 200
 * {"id":1,"titulo":"Introducción a Java"}
 * </pre>
 *
 * @author
 * @version 1.0
 */
public class LibroService {

	/**
	 * Realiza una petición HTTP GET a la URL especificada para obtener información
	 * de libros.
	 *
	 * @param urlString la URL del recurso a consultar
	 * @return una cadena con el código de estado y la respuesta del servidor; en
	 *         caso de error, devuelve un mensaje descriptivo
	 */
	public static String doGet(String urlString) {
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");

			// Tiempos de espera extendidos para manejar archivos grandes
			connection.setConnectTimeout(15000);
			connection.setReadTimeout(60000);

			int responseCode = connection.getResponseCode();

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

			return response.toString();

		} catch (java.net.SocketTimeoutException e) {
			return "Error: Timeout - archivos demasiado grandes";
		} catch (Exception e) {
			e.printStackTrace();
			return "Error: " + e.getMessage();
		}
	}

	/**
	 * Realiza una petición HTTP POST a la URL especificada enviando datos en
	 * formato JSON.
	 *
	 * @param urlString la URL del recurso al que se desea enviar la información
	 * @param jsonData  el contenido en formato JSON que será enviado en el cuerpo
	 *                  de la petición
	 * @return una cadena con el código de estado y la respuesta del servidor; en
	 *         caso de error, devuelve un mensaje descriptivo
	 */
	public static String doPost(String urlString, String jsonData) {
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
			connection.setDoOutput(true);

			// Tiempo extra para manejar archivos grandes
			connection.setConnectTimeout(15000);
			connection.setReadTimeout(120000);

			try (OutputStream os = connection.getOutputStream()) {
				byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
				os.write(input, 0, input.length);
				os.flush();
			}

			int responseCode = connection.getResponseCode();

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

			return response.toString();

		} catch (java.net.SocketTimeoutException e) {
			return "Error: Timeout - archivo demasiado grande";
		} catch (Exception e) {
			e.printStackTrace();
			return "Error: " + e.getMessage();
		}
	}

	/**
	 * Realiza una petición HTTP DELETE a la URL especificada para eliminar un
	 * recurso de libro.
	 *
	 * @param urlString la URL del recurso que se desea eliminar
	 * @return una cadena con el código de estado y la respuesta del servidor; en
	 *         caso de error, devuelve un mensaje descriptivo
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
