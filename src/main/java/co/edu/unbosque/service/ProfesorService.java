package co.edu.unbosque.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Servicio para realizar peticiones HTTP (GET y POST) hacia recursos externos
 * relacionados con profesores.
 *
 * <p>
 * Utiliza la API {@link HttpClient} de Java 11+ para manejar las conexiones
 * HTTP, configurada con soporte para HTTP/2 y tiempos de espera.
 * </p>
 *
 * <p>
 * Todos los métodos son estáticos, por lo cual no es necesario instanciar la
 * clase para usarlos.
 * </p>
 *
 * Ejemplo de uso:
 * 
 * <pre>{@code
 * String respuesta = ProfesorService.doGet("http://localhost:8080/api/profesores");
 * System.out.println(respuesta);
 * }</pre>
 *
 * @author
 * @version 1.0
 */
public class ProfesorService {

	/** Cliente HTTP reutilizable para todas las solicitudes */
	private static final HttpClient CLIENTE = HttpClient.newBuilder().version(Version.HTTP_2)
			.connectTimeout(Duration.ofSeconds(10)).build();

	/**
	 * Realiza una petición HTTP GET hacia la URL indicada.
	 *
	 * @param url la URL del recurso al que se realizará la petición
	 * @return la respuesta del servidor en formato {@code String}, incluyendo el
	 *         código de estado; o un mensaje de error si ocurre una excepción
	 */
	public static String doGet(String url) {
		HttpRequest solicitud = HttpRequest.newBuilder().GET().uri(URI.create(url))
				.header("Content-Type", "application/json").timeout(Duration.ofSeconds(30)).build();

		HttpResponse<String> respuesta = null;
		try {
			respuesta = CLIENTE.send(solicitud, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			System.out.println("Error al solicitar: " + e.getMessage());
			return "Error de conexión: " + e.getMessage();
		} catch (InterruptedException e) {
			System.out.println("Error de interrupción: " + e.getMessage());
			return "Error de interrupción: " + e.getMessage();
		}

		String statusCode = String.valueOf(respuesta.statusCode());
		String body = respuesta.body();

		return statusCode + "\n" + body;
	}

	/**
	 * Realiza una petición HTTP POST hacia la URL indicada enviando datos en
	 * formato JSON.
	 *
	 * @param url  la URL del recurso destino
	 * @param json los datos en formato JSON que se enviarán en el cuerpo de la
	 *             petición
	 * @return la respuesta del servidor en formato {@code String}.
	 *         <ul>
	 *         <li>Si el código es 2xx, devuelve el cuerpo de la respuesta.</li>
	 *         <li>Si es 404, devuelve "Usuario no encontrado".</li>
	 *         <li>Si es 401, devuelve "Credenciales incorrectas".</li>
	 *         <li>Si es 409, devuelve "Usuario ya existe".</li>
	 *         <li>Si es 406, devuelve "Error: datos no aceptables".</li>
	 *         <li>Si es otro código, devuelve "Error [código]: [respuesta]".</li>
	 *         </ul>
	 */
	public static String doPost(String url, String json) {
		HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(json))
				.uri(URI.create(url)).header("User-Agent", "Java 11 HttpClient Bot")
				.header("Content-Type", "application/json").header("Accept", "application/json")
				.timeout(Duration.ofSeconds(30)).build();

		HttpResponse<String> response = null;
		try {
			response = CLIENTE.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (InterruptedException e) {
			System.err.println("Error de interrupción: " + e.getMessage());
			return "Error de interrupción: " + e.getMessage();
		} catch (IOException e) {
			System.err.println("Error de conexión: " + e.getMessage());
			return "Error de conexión: " + e.getMessage();
		}

		int statusCode = response.statusCode();
		String body = response.body();

		if (statusCode >= 200 && statusCode < 300) {
			return body;
		} else if (statusCode == 404) {
			return "Usuario no encontrado";
		} else if (statusCode == 401) {
			return "Credenciales incorrectas";
		} else if (statusCode == 409) {
			return "Usuario ya existe";
		} else if (statusCode == 406) {
			return "Error: datos no aceptables";
		} else {
			return "Error " + statusCode + ": " + body;
		}
	}
}
