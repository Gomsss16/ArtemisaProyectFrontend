package co.edu.unbosque.bean;

import co.edu.unbosque.dto.LinkDTO;
import co.edu.unbosque.service.LinkService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import org.primefaces.model.file.UploadedFile;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Bean encargado de gestionar los links (enlaces con título, descripción e
 * imagen) dentro de la aplicación web. Se conecta con el servicio REST de
 * {@link LinkService}.
 */
@RequestScoped
@Named("linkBean")
public class LinkBean {

	/** Lista de enlaces disponibles en el sistema. */
	private List<LinkDTO> links = new ArrayList<>();

	/** Título del link. */
	private String titulo = "";

	/** Descripción del link. */
	private String descripcion = "";

	/** Enlace/URL del link. */
	private String enlace = "";

	/** Archivo de imagen cargado (si existe). */
	private UploadedFile imagenFile;

	/** Instancia de Gson para serializar y deserializar JSON. */
	private Gson gson = new Gson();

	/**
	 * Constructor que inicializa el bean cargando los links disponibles.
	 */
	public LinkBean() {
		cargarLink();
	}

	/**
	 * Carga todos los links desde el servicio REST remoto. Filtra aquellos que
	 * tengan título válido.
	 */
	public void cargarLink() {
		try {
			String respuesta = LinkService.doGet("http://localhost:8081/link/getall");

			if (respuesta != null && !respuesta.contains("Error")) {
				String[] partes = respuesta.split("\n", 2);

				if (partes.length > 1 && !partes[1].equals("[]")) {
					try {
						Type listType = new TypeToken<List<LinkDTO>>() {
						}.getType();
						List<LinkDTO> lista = gson.fromJson(partes[1], listType);

						if (lista != null) {
							links = lista.stream().filter(l -> l.getTitulo() != null && !l.getTitulo().trim().isEmpty())
									.collect(Collectors.toList());
						} else {
							links = new ArrayList<>();
						}

					} catch (Exception e) {
						links = new ArrayList<>();
					}
				} else {
					links = new ArrayList<>();
				}
			} else {
				links = new ArrayList<>();
			}
		} catch (Exception e) {
			links = new ArrayList<>();
		}
	}

	/**
	 * Agrega un nuevo link al sistema mediante una petición POST al servicio.
	 * Valida los campos obligatorios antes de enviar.
	 */
	public void addLink() {
		try {
			if (titulo == null || titulo.trim().isEmpty()) {
				showMessage("Error", "El título es obligatorio");
				return;
			}

			if (enlace == null || enlace.trim().isEmpty()) {
				showMessage("Error", "El enlace es obligatorio");
				return;
			}

			Map<String, Object> linkParaJson = new HashMap<>();
			linkParaJson.put("titulo", titulo.trim());
			linkParaJson.put("descripcion", descripcion != null ? descripcion.trim() : "");
			linkParaJson.put("enlace", enlace.trim());

			if (imagenFile != null) {
				String base64 = Base64.getEncoder().encodeToString(imagenFile.getContent());
				linkParaJson.put("imagenBase64", base64);
			}

			String json = gson.toJson(linkParaJson);
			String respuesta = LinkService.doPost("http://localhost:8081/link/createlinkjson", json);

			if (respuesta != null && respuesta.startsWith("201")) {
				showMessage("201", "Link '" + titulo + "' creado exitosamente");
				limpiarCampos();
				cargarLink();

			} else if (respuesta != null && respuesta.contains("ya existe")) {
				showMessage("409", "El link '" + titulo + "' ya existe");

			} else {
				showMessage("Error", "Error del servidor: " + respuesta);
			}

		} catch (Exception e) {
			showMessage("Error", "Error interno: " + e.getMessage());
		}
	}

	/**
	 * Elimina un link por su ID llamando al servicio REST.
	 *
	 * @param id identificador del link a eliminar.
	 */
	public void eliminarPorId(Long id) {
		try {
			if (id == null) {
				showMessage("Error", "Error: ID es null");
				return;
			}

			LinkDTO linkAEliminar = null;
			for (LinkDTO l : links) {
				if (l.getId() != null && l.getId().equals(id)) {
					linkAEliminar = l;
					break;
				}
			}

			if (linkAEliminar == null) {
				showMessage("Error", "No se encontró el link con ID: " + id);
				return;
			}

			String titleEncoded = java.net.URLEncoder.encode(linkAEliminar.getTitulo(), "UTF-8");
			String url = "http://localhost:8081/link/deletebyTitle?title=" + titleEncoded;

			String respuesta = LinkService.doDelete(url);

			if (respuesta != null && (respuesta.startsWith("200") || respuesta.startsWith("202"))) {
				showMessage("200", "Link '" + linkAEliminar.getTitulo() + "' eliminado");
				cargarLink();
			} else {
				showMessage("Error", "Error eliminando: " + respuesta);
			}

		} catch (Exception e) {
			showMessage("Error", "Error: " + e.getMessage());
		}
	}

	/**
	 * Verifica si el usuario actual es un estudiante, según el rol guardado en
	 * sesión.
	 *
	 * @return true si el rol es "Estudiante" o si ocurre error.
	 */
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
	public boolean puedeEditar() {
	    try {
	        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
	                .getExternalContext().getSession(false);
	        
	        if (session != null) {
	            String role = (String) session.getAttribute("role");
	            return "Administrador".equals(role);
	        }
	        return false;
	    } catch (Exception e) {
	        return false;
	    }
	}


	public boolean esProfesor() {
	    try {
	        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
	                .getExternalContext().getSession(false);
	        
	        if (session != null) {
	            String role = (String) session.getAttribute("role");
	            return "Profesor".equals(role);
	        }
	        return false;
	    } catch (Exception e) {
	        return false;
	    }
	}


	/**
	 * Limpia los campos del formulario de creación de link.
	 */
	private void limpiarCampos() {
		titulo = "";
		descripcion = "";
		enlace = "";
		imagenFile = null;
	}

	/**
	 * Muestra un mensaje en pantalla mediante PrimeFaces growl.
	 *
	 * @param code    código de estado (200, 201, 409, etc.)
	 * @param content contenido del mensaje.
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


	/**
	 * @return lista de links cargados.
	 */
	public List<LinkDTO> getLinks() {
		return links;
	}

	/**
	 * @param links lista de links a establecer.
	 */
	public void setLinks(List<LinkDTO> links) {
		this.links = links;
	}

	/**
	 * @return título actual.
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * @param titulo título a establecer.
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	/**
	 * @return descripción actual.
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion descripción a establecer.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return enlace actual.
	 */
	public String getEnlace() {
		return enlace;
	}

	/**
	 * @param enlace enlace a establecer.
	 */
	public void setEnlace(String enlace) {
		this.enlace = enlace;
	}

	/**
	 * @return archivo de imagen cargado.
	 */
	public UploadedFile getImagenFile() {
		return imagenFile;
	}

	/**
	 * @param imagenFile archivo de imagen a establecer.
	 */
	public void setImagenFile(UploadedFile imagenFile) {
		this.imagenFile = imagenFile;
	}
}
