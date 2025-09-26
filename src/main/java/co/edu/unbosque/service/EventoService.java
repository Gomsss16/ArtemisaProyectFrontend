package co.edu.unbosque.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Servicio que maneja las operaciones HTTP relacionadas con eventos.
 * <p>
 * Proporciona métodos para realizar solicitudes {@code GET}, {@code POST} y
 * {@code DELETE} hacia un backend o API REST utilizando
 * {@link java.net.HttpURLConnection}.
 * </p>
 *
 * <p>
 * Los métodos devuelven como respuesta una cadena con el formato:
 * </p>
 * <ul>
 * <li>Código de estado HTTP.</li>
 * <li>Cuerpo de la respuesta (JSON o mensaje de error).</li>
 * </ul>
 *
 * Ejemplo de respuesta: <br>
 * 
 * <pre>
 * 200
 * {"id":1,"nombre":"Evento de prueba"}
 * </pre>
 *
 * @author
 * @version 1.0
 */
public class EventoService {

	/**
	 * Realiza una petición HTTP GET a la URL especificada.
	 *
	 * @param urlString la URL del recurso al que se desea acceder
	 * @return una cadena que incluye el código de estado HTTP y el cuerpo de la
	 *         respuesta; en caso de error, devuelve un mensaje descriptivo con la
	 *         excepción.
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
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			} else {
				reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
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
	 * Realiza una petición HTTP POST a la URL especificada enviando datos en
	 * formato JSON.
	 *
	 * @param urlString la URL del recurso al que se desea enviar información
	 * @param jsonData  el contenido en formato JSON que será enviado en el cuerpo
	 *                  de la petición
	 * @return una cadena que incluye el código de estado HTTP y el cuerpo de la
	 *         respuesta; en caso de error, devuelve un mensaje descriptivo con la
	 *         excepción.
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
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			} else {
				reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
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
	 * Realiza una petición HTTP DELETE a la URL especificada.
	 *
	 * @param urlString la URL del recurso que se desea eliminar
	 * @return una cadena que incluye el código de estado HTTP y el cuerpo de la
	 *         respuesta; en caso de error, devuelve un mensaje descriptivo con la
	 *         excepción.
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
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			} else {
				reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
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
