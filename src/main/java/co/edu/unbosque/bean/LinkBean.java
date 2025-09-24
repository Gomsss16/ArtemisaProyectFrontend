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

@RequestScoped
@Named("linkBean")
public class LinkBean {

	private List<LinkDTO> links = new ArrayList<>();
	private String titulo = "";
	private String descripcion = "";
	private String enlace = "";
	private UploadedFile imagenFile;
	private Gson gson = new Gson();

	public LinkBean() {
		cargarLink();
	}

	public void cargarLink() {
		try {
			System.out.println("=== CARGANDO LINKS ===");
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

						System.out.println("Links válidos cargados: " + links.size());

					} catch (Exception e) {
						System.err.println("Error parseando JSON: " + e.getMessage());
						links = new ArrayList<>();
					}
				} else {
					links = new ArrayList<>();
				}
			} else {
				links = new ArrayList<>();
			}

		} catch (Exception e) {
			System.err.println("Error cargando links: " + e.getMessage());
			links = new ArrayList<>();
		}
	}

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
	            linkParaJson.put("imagenBase64", base64); // Mandamos la imagen como texto
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

	public void eliminarPorId(Long id) {
		try {
			System.out.println("=== ELIMINANDO LINK POR ID: " + id + " ===");

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
		descripcion = "";
		enlace = "";
		imagenFile = null;
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

	public List<LinkDTO> getLinks() {
		return links;
	}

	public void setLinks(List<LinkDTO> links) {
		this.links = links;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEnlace() {
		return enlace;
	}

	public void setEnlace(String enlace) {
		this.enlace = enlace;
	}

	public UploadedFile getImagenFile() {
		return imagenFile;
	}

	public void setImagenFile(UploadedFile imagenFile) {
		this.imagenFile = imagenFile;
	}
}
