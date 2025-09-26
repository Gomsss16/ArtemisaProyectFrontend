package co.edu.unbosque.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Servicio encargado de realizar peticiones HTTP (GET y POST) hacia el backend
 * relacionadas con las operaciones de los estudiantes.
 * <p>
 * Utiliza la API {@link java.net.http.HttpClient} introducida en Java 11 para
 * enviar y recibir datos en formato JSON.
 * </p>
 * 
 * @author
 * @version 1.0
 */
public class EstudianteService {

	/**
	 * Cliente HTTP reutilizable para realizar las peticiones. Configurado con
	 * soporte HTTP/2 y un tiempo de conexión máximo de 10 segundos.
	 */
	private static final HttpClient CLIENTE = HttpClient.newBuilder().version(Version.HTTP_2)
			.connectTimeout(Duration.ofSeconds(10)).build();

	/**
	 * Realiza una petición HTTP GET al backend.
	 * 
	 * @param url la URL del recurso al que se desea acceder
	 * @return una cadena con el código de estado y el cuerpo de la respuesta. En
	 *         caso de error de conexión o interrupción, devuelve un mensaje
	 *         descriptivo.
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
	 * Realiza una petición HTTP POST al backend con datos en formato JSON.
	 * 
	 * @param url  la URL del recurso al que se desea enviar la información
	 * @param json el contenido en formato JSON que será enviado en el cuerpo de la
	 *             petición
	 * @return una cadena con el resultado de la operación:
	 *         <ul>
	 *         <li>Respuesta del backend si el estado está entre 200 y 299.</li>
	 *         <li>"Usuario no encontrado" si el estado es 404.</li>
	 *         <li>"Credenciales incorrectas" si el estado es 401.</li>
	 *         <li>"Usuario ya existe" si el estado es 409.</li>
	 *         <li>"Error: datos no aceptables" si el estado es 406.</li>
	 *         <li>Un mensaje de error genérico para otros códigos de estado.</li>
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
