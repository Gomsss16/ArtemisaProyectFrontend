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

/**
 * Bean encargado de gestionar los problemas de programación dentro del sistema
 * Artemisa. Permite crear, cargar, filtrar y eliminar problemas según el rol
 * del usuario.
 * 
 * Este bean funciona en el contexto de una petición HTTP
 * ({@link RequestScoped}). Está identificado como "problemaBean" en las páginas
 * JSF.
 * 
 * @author
 */
@RequestScoped
@Named("problemaBean")
public class ProblemaBean {

	/** Lista de todos los problemas cargados desde el servicio. */
	private List<ProblemaDTO> problemas = new ArrayList<>();

	/** Lista de problemas filtrados según criterios aplicados. */
	private List<ProblemaDTO> filteredProblemas = new ArrayList<>();

	/** Título del problema actual. */
	private String titulo = "";

	/** Nivel de dificultad del problema (1 a 5). */
	private int dificultad = 1;

	/** Tema asociado al problema. */
	private String tema = "";

	/** Nombre del juez (plataforma) del problema. */
	private String juez = "";

	/** Enlace al problema en el juez correspondiente. */
	private String link = "";

	/** Lista predefinida de jueces soportados. */
	private List<String> jueces = Arrays.asList("Codeforces", "AtCoder", "LeetCode", "HackerRank", "SPOJ", "UVa",
			"CodeChef", "Otros");

	/** Objeto Gson para manejo de conversiones JSON. */
	private Gson gson = new Gson();

	/**
	 * Constructor por defecto. Carga los problemas al inicializar el bean.
	 */
	public ProblemaBean() {
		cargarProblema();
	}

	/**
	 * Carga los problemas desde el servicio externo. Maneja la respuesta en formato
	 * JSON y la convierte en una lista de {@link ProblemaDTO}.
	 */
	public void cargarProblema() {
		try {
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

					} catch (Exception e) {
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
			problemas = new ArrayList<>();
			filteredProblemas = new ArrayList<>();
		}
	}

	/**
	 * Crea un nuevo problema y lo envía al servicio para su persistencia. Realiza
	 * validaciones sobre los campos antes de enviarlo.
	 */
	public void crearProblema() {
		try {
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

	/**
	 * Elimina un problema dado su ID.
	 * 
	 * @param id Identificador único del problema.
	 */
	public void eliminarPorId(Long id) {
		try {
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

	/**
	 * Verifica si el usuario actual es estudiante.
	 * 
	 * @return {@code true} si el rol del usuario es "Estudiante", {@code false} en
	 *         otro caso.
	 */
	public boolean puedeEditar() {
	    try {
	        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
	                .getExternalContext().getSession(false);
	        
	        if (session != null) {
	            String role = (String) session.getAttribute("role");
	            // Profesor y Administrador pueden editar problemas
	            return "Profesor".equals(role) || "Administrador".equals(role);
	        }
	        return false;
	    } catch (Exception e) {
	        return false;
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

	/** Limpia los campos del formulario. */
	private void limpiarCampos() {
		titulo = "";
		dificultad = 1;
		tema = "";
		juez = "";
		link = "";
	}

	/**
	 * Muestra un mensaje en la interfaz de usuario.
	 * 
	 * @param code    Código del mensaje (ej. 200, 201, 409, Error).
	 * @param content Contenido del mensaje a mostrar.
	 */
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
