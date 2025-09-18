package co.edu.unbosque.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ProfesorService {

	private static final HttpClient CLIENTE = HttpClient.newBuilder().version(Version.HTTP_2)
			.connectTimeout(Duration.ofSeconds(10)).build();

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
