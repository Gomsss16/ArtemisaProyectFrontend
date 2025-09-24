package co.edu.unbosque.bean;

import co.edu.unbosque.dto.ProblemaDTO;
import co.edu.unbosque.service.ProblemaService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Named("problemaBean")
public class ProblemaBean {

	private List<ProblemaDTO> problemas = new ArrayList<>();
	private List<ProblemaDTO> filteredProblemas = new ArrayList<>();
	private String titulo = "";
	private int dificultad = 1;
	private String tema = "";
	private String juez = "";
	private String link = "";
	private List<String> jueces = Arrays.asList("Codeforces", "AtCoder", "LeetCode", "HackerRank", "SPOJ", "UVa",
			"CodeChef", "Otros");
	private Gson gson = new Gson();

	public ProblemaBean() {
		cargarProblema();
	}

	public void cargarProblema() {
		try {
			System.out.println("=== CARGANDO PROBLEMAS ===");
			String respuesta = ProblemaService.doGet("http://localhost:8081/problema/getall");

			if (respuesta != null && !respuesta.contains("Error")) {
				String[] partes = respuesta.split("\n", 2);

				if (partes.length > 1 && !partes[1].equals("[]")) {
					try {
						Type listType = new TypeToken<List<ProblemaDTO>>() {
						}.getType();
						List<ProblemaDTO> lista = gson.fromJson(partes[1], listType);

						if (lista != null) {
							problemas = lista.stream()
									.filter(p -> p.getTitulo() != null && !p.getTitulo().trim().isEmpty())
									.collect(Collectors.toList());
						} else {
							problemas = new ArrayList<>();
						}

						filteredProblemas = new ArrayList<>(problemas);
						System.out.println("Problemas válidos cargados: " + problemas.size());

					} catch (Exception e) {
						System.err.println("Error parseando JSON: " + e.getMessage());
						problemas = new ArrayList<>();
						filteredProblemas = new ArrayList<>();
					}
				} else {
					problemas = new ArrayList<>();
					filteredProblemas = new ArrayList<>();
				}
			} else {
				problemas = new ArrayList<>();
				filteredProblemas = new ArrayList<>();
			}

		} catch (Exception e) {
			System.err.println("Error cargando problemas: " + e.getMessage());
			problemas = new ArrayList<>();
			filteredProblemas = new ArrayList<>();
		}
	}

	public void crearProblema() {
		try {
			System.out.println("=== CREANDO PROBLEMA ===");

			if (titulo == null || titulo.trim().isEmpty()) {
				showMessage("Error", "El título es obligatorio");
				return;
			}

			if (tema == null || tema.trim().isEmpty()) {
				showMessage("Error", "El tema es obligatorio");
				return;
			}

			if (juez == null || juez.trim().isEmpty()) {
				showMessage("Error", "El juez es obligatorio");
				return;
			}

			ProblemaDTO nuevoProblema = new ProblemaDTO(null, titulo.trim(), dificultad, tema.trim(), juez.trim(),
					link != null ? link.trim() : "");

			String json = gson.toJson(nuevoProblema);
			String respuesta = ProblemaService.doPost("http://localhost:8081/problema/createproblemajson", json);

			if (respuesta != null && (respuesta.contains("exitosamente") || respuesta.contains("creado")
					|| respuesta.contains("message") || respuesta.contains("Problema creado"))) {

				showMessage("201", "Problema '" + titulo + "' creado exitosamente");
				limpiarCampos();
				cargarProblema();

			} else if (respuesta != null && respuesta.contains("ya existe")) {
				showMessage("409", "El problema '" + titulo + "' ya existe");

			} else {
				showMessage("Error", "Error del servidor: " + respuesta);
			}

		} catch (Exception e) {
			showMessage("Error", "Error interno: " + e.getMessage());
		}
	}

	public void eliminarPorId(Long id) {
		try {
			System.out.println("=== ELIMINANDO PROBLEMA POR ID: " + id + " ===");

			if (id == null) {
				showMessage("Error", "Error: ID es null");
				return;
			}

			ProblemaDTO problemaAEliminar = null;
			for (ProblemaDTO p : problemas) {
				if (p.getId() != null && p.getId().equals(id)) {
					problemaAEliminar = p;
					break;
				}
			}

			if (problemaAEliminar == null) {
				showMessage("Error", "No se encontró el problema con ID: " + id);
				return;
			}

			String tituloEncoded = java.net.URLEncoder.encode(problemaAEliminar.getTitulo(), "UTF-8");
			String url = "http://localhost:8081/problema/deletebyTitulo?titulo=" + tituloEncoded;

			String respuesta = ProblemaService.doDelete(url);

			if (respuesta != null && respuesta.contains("successfully")) {
				showMessage("200", "Problema '" + problemaAEliminar.getTitulo() + "' eliminado");
				cargarProblema();
			} else {
				showMessage("Error", "Error eliminando: " + respuesta);
			}

		} catch (Exception e) {
			showMessage("Error", "Error: " + e.getMessage());
		}
	}

	public boolean esEstudiante() {
		try {
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(false);

			if (session != null) {
				String role = (String) session.getAttribute("role");
				return "Estudiante".equals(role);
			}

			return true;
		} catch (Exception e) {
			return true;
		}
	}

	private void limpiarCampos() {
		titulo = "";
		dificultad = 1;
		tema = "";
		juez = "";
		link = "";
	}

	public void showMessage(String code, String content) {
		FacesContext context = FacesContext.getCurrentInstance();

		if ("201".equals(code) || "200".equals(code)) {
			context.addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Sistema Artemisa", content));
		} else if ("409".equals(code)) {
			context.addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", content));
		} else if ("404".equals(code)) {
			context.addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_WARN, "No Encontrado", content));
		} else {
			context.addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", content));
		}
	}

	// Getters y Setters
	public List<ProblemaDTO> getProblemas() {
		return problemas;
	}

	public void setProblemas(List<ProblemaDTO> problemas) {
		this.problemas = problemas;
	}

	public List<ProblemaDTO> getFilteredProblemas() {
		return filteredProblemas;
	}

	public void setFilteredProblemas(List<ProblemaDTO> filteredProblemas) {
		this.filteredProblemas = filteredProblemas;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public int getDificultad() {
	    return dificultad;
	}

	public void setDificultad(int dificultad) {
	    this.dificultad = dificultad;
	}


	public String getTema() {
		return tema;
	}

	public void setTema(String tema) {
		this.tema = tema;
	}

	public String getJuez() {
		return juez;
	}

	public void setJuez(String juez) {
		this.juez = juez;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public List<String> getJueces() {
		return jueces;
	}

	public void setJueces(List<String> jueces) {
		this.jueces = jueces;
	}
}
