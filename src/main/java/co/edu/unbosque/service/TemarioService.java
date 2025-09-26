package co.edu.unbosque.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Servicio encargado de realizar peticiones HTTP (GET, POST y DELETE) hacia
 * recursos relacionados con {@code Temario}.
 *
 * <p>
 * Utiliza {@link HttpClient} de Java 11+ para establecer la conexión con los
 * endpoints externos, soportando HTTP/2 y manejo de tiempos de espera.
 * </p>
 *
 * <p>
 * Todos los métodos son estáticos, por lo que se pueden usar directamente sin
 * instanciar la clase.
 * </p>
 *
 * Ejemplo de uso:
 * 
 * <pre>{@code
 * String resultado = TemarioService.doGet("http://localhost:8080/api/temarios");
 * System.out.println(resultado);
 * }</pre>
 *
 * @author
 * @version 1.0
 */
public class TemarioService {

	/** Cliente HTTP reutilizable para todas las solicitudes */
	private static final HttpClient CLIENTE = HttpClient.newBuilder().version(Version.HTTP_2)
			.connectTimeout(Duration.ofSeconds(10)).build();

	/**
	 * Realiza una petición HTTP GET hacia la URL indicada.
	 *
	 * @param url la URL del recurso a consultar
	 * @return la respuesta del servidor en formato {@code String}, incluyendo
	 *         código de estado y cuerpo; o un mensaje de error en caso de fallo de
	 *         conexión o interrupción
	 */
	public static String doGet(String url) {
		HttpRequest solicitud = HttpRequest.newBuilder().GET().uri(URI.create(url))
				.header("Content-Type", "application/json").timeout(Duration.ofSeconds(30)).build();

		HttpResponse<String> respuesta = null;
		try {
			respuesta = CLIENTE.send(solicitud, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			return "Error de conexión: " + e.getMessage();
		} catch (InterruptedException e) {
			return "Error de interrupción: " + e.getMessage();
		}

		String statusCode = String.valueOf(respuesta.statusCode());
		String body = respuesta.body();

		return statusCode + "\n" + body;
	}

	/**
	 * Realiza una petición HTTP POST enviando datos en formato JSON.
	 *
	 * @param url  la URL del recurso destino
	 * @param json datos en formato JSON que se enviarán en el cuerpo de la petición
	 * @return la respuesta del servidor en formato {@code String}.
	 *         <ul>
	 *         <li>Si el código es 2xx, devuelve el cuerpo de la respuesta.</li>
	 *         <li>Si es 404, devuelve "Temario no encontrado".</li>
	 *         <li>Si es 401, devuelve "No autorizado".</li>
	 *         <li>Si es 409, devuelve "Temario ya existe".</li>
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
			return "Error de interrupción: " + e.getMessage();
		} catch (IOException e) {
			return "Error de conexión: " + e.getMessage();
		}

		int statusCode = response.statusCode();
		String body = response.body();

		if (statusCode >= 200 && statusCode < 300) {
			return body;
		} else if (statusCode == 404) {
			return "Temario no encontrado";
		} else if (statusCode == 401) {
			return "No autorizado";
		} else if (statusCode == 409) {
			return "Temario ya existe";
		} else if (statusCode == 406) {
			return "Error: datos no aceptables";
		} else {
			return "Error " + statusCode + ": " + body;
		}
	}

	/**
	 * Realiza una petición HTTP DELETE hacia la URL indicada.
	 *
	 * @param url la URL del recurso a eliminar
	 * @return la respuesta del servidor en formato {@code String}.
	 *         <ul>
	 *         <li>Si el código es 2xx, devuelve el cuerpo de la respuesta.</li>
	 *         <li>Si es 404, devuelve "Temario no encontrado".</li>
	 *         <li>Si es otro código, devuelve "Error [código]: [respuesta]".</li>
	 *         </ul>
	 */
	public static String doDelete(String url) {
		HttpRequest request = HttpRequest.newBuilder().DELETE().uri(URI.create(url))
				.header("User-Agent", "Java 11 HttpClient Bot").header("Content-Type", "application/json")
				.header("Accept", "application/json").timeout(Duration.ofSeconds(30)).build();

		HttpResponse<String> response = null;
		try {
			response = CLIENTE.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (InterruptedException e) {
			return "Error de interrupción: " + e.getMessage();
		} catch (IOException e) {
			return "Error de conexión: " + e.getMessage();
		}

		int statusCode = response.statusCode();
		String body = response.body();

		if (statusCode >= 200 && statusCode < 300) {
			return body;
		} else if (statusCode == 404) {
			return "Temario no encontrado";
		} else {
			return "Error " + statusCode + ": " + body;
		}
	}
}
