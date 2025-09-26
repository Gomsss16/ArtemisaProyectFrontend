package co.edu.unbosque.bean;

import co.edu.unbosque.service.AdministradorService;
import co.edu.unbosque.service.EstudianteService;
import co.edu.unbosque.service.ProfesorService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import org.primefaces.model.file.UploadedFile;

import java.io.Serializable;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

/**
 * Bean encargado de gestionar la información del perfil del usuario
 * autenticado.
 * 
 * <p>
 * Permite:
 * </p>
 * <ul>
 * <li>Cargar los datos del usuario y su rol desde la sesión.</li>
 * <li>Obtener o actualizar la imagen de perfil desde el backend según el
 * rol.</li>
 * <li>Mostrar una imagen por defecto si el usuario no tiene imagen.</li>
 * <li>Asignar colores e íconos representativos de cada rol.</li>
 * </ul>
 * 
 * Es un bean de sesión, por lo que la información persiste mientras la sesión
 * esté activa.
 */
@SessionScoped
@Named("perfilBean")
public class PerfilBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Archivo cargado por el usuario como imagen de perfil. */
	private UploadedFile profileImage;

	/** Nombre de usuario autenticado en la sesión. */
	private String username;

	/** Rol del usuario (Administrador, Profesor o Estudiante). */
	private String role;

	/** Imagen de perfil en base64, lista para ser mostrada en la interfaz. */
	private String profileImagePath = "";

	/** Instancia de Gson para procesar conversiones JSON. */
	private Gson gson = new Gson();

	/**
	 * Constructor por defecto. Inicializa el bean cargando la información del
	 * usuario desde la sesión.
	 */
	public PerfilBean() {
		loadUserData();
	}

	/**
	 * Carga los datos del usuario (nombre y rol) desde la sesión JSF. Si existe una
	 * imagen en el backend, la asigna; de lo contrario carga una por defecto.
	 */
	private void loadUserData() {
		try {
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(false);

			if (session != null) {
				username = (String) session.getAttribute("username");
				role = (String) session.getAttribute("role");

				if (username != null && role != null) {
					String imagenBase64 = obtenerImagenPorRol(username, role);
					if (imagenBase64 != null && !imagenBase64.isEmpty()) {
						profileImagePath = "data:image/*;base64," + imagenBase64;
					} else {
						profileImagePath = getDefaultImage();
					}
				} else {
					profileImagePath = getDefaultImage();
				}
			}
		} catch (Exception e) {
			profileImagePath = getDefaultImage();
		}
	}

	/**
	 * Obtiene la imagen de perfil desde el backend de acuerdo al rol del usuario.
	 * 
	 * @param usuario nombre del usuario.
	 * @param rol     rol del usuario (Estudiante, Profesor o Administrador).
	 * @return cadena en base64 de la imagen si existe, en caso contrario
	 *         {@code null}.
	 */
	private String obtenerImagenPorRol(String usuario, String rol) {
		try {
			String url = "";
			String response = "";

			switch (rol) {
			case "Estudiante":
				url = "http://localhost:8081/estudiante/obtenerImagen?usuario="
						+ java.net.URLEncoder.encode(usuario, "UTF-8");
				response = EstudianteService.doGet(url);
				break;
			case "Profesor":
				url = "http://localhost:8081/profesor/obtenerImagen?usuario="
						+ java.net.URLEncoder.encode(usuario, "UTF-8");
				response = ProfesorService.doGet(url);
				break;
			case "Administrador":
				url = "http://localhost:8081/admin/obtenerImagen?usuario="
						+ java.net.URLEncoder.encode(usuario, "UTF-8");
				response = AdministradorService.doGet(url);
				break;
			default:
				return null;
			}

			if (response != null && response.contains("200")) {
				String[] partes = response.split("\n", 2);
				if (partes.length > 1) {
					Map<String, Object> jsonResponse = gson.fromJson(partes[1], Map.class);
					return (String) jsonResponse.get("imagenBase64");
				}
			}
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Sube y actualiza la imagen de perfil del usuario en el backend.
	 * 
	 * <p>
	 * Convierte la imagen a base64 y la envía según el rol del usuario. Si la
	 * operación es exitosa, actualiza la vista y muestra un mensaje de éxito. Si
	 * ocurre un error, muestra un mensaje de advertencia o error.
	 * </p>
	 */
	public void uploadProfileImage() {
		if (profileImage != null && username != null && role != null) {
			try {
				String base64 = Base64.getEncoder().encodeToString(profileImage.getContent());

				Map<String, Object> data = new HashMap<>();
				data.put("usuario", username);
				data.put("imagenBase64", base64);

				String json = gson.toJson(data);
				String response = "";

				switch (role) {
				case "Estudiante":
					response = EstudianteService.doPost("http://localhost:8081/estudiante/actualizarImagen", json);
					break;
				case "Profesor":
					response = ProfesorService.doPost("http://localhost:8081/profesor/actualizarImagen", json);
					break;
				case "Administrador":
					response = AdministradorService.doPost("http://localhost:8081/admin/actualizarImagen", json);
					break;
				default:
					throw new Exception("Rol no válido: " + role);
				}

				if (response != null && (response.contains("200") || response.contains("201"))) {
					profileImagePath = "data:image/*;base64," + base64;

					FacesContext.getCurrentInstance().addMessage("perfilForm", new FacesMessage(
							FacesMessage.SEVERITY_INFO, "Éxito", "Imagen de perfil actualizada correctamente"));
				} else {
					FacesContext.getCurrentInstance().addMessage("perfilForm", new FacesMessage(
							FacesMessage.SEVERITY_ERROR, "Error", "Error actualizando imagen: " + response));
				}

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage("perfilForm", new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Error procesando imagen: " + e.getMessage()));
				e.printStackTrace();
			}
		} else {
			FacesContext.getCurrentInstance().addMessage("perfilForm",
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Por favor selecciona una imagen"));
		}
	}

	/**
	 * Devuelve una imagen SVG por defecto en formato base64.
	 * 
	 * @return cadena en base64 con la imagen SVG predeterminada.
	 */
	private String getDefaultImage() {
		return "data:image/svg+xml;base64," + Base64.getEncoder().encodeToString(
				("<svg xmlns='http://www.w3.org/2000/svg' width='120' height='120' viewBox='0 0 120 120'>"
						+ "<circle cx='60' cy='60' r='60' fill='#e0e0e0'/>"
						+ "<circle cx='60' cy='45' r='15' fill='#999'/>"
						+ "<ellipse cx='60' cy='85' rx='25' ry='20' fill='#999'/>" + "</svg>").getBytes());
	}

	/**
	 * Obtiene un color asociado al rol del usuario.
	 * 
	 * @return color en formato hexadecimal según el rol.
	 */
	public String getRoleColor() {
		if (role == null)
			return "#6c757d";

		switch (role.toLowerCase()) {
		case "administrador":
			return "#dc3545";
		case "profesor":
			return "#007bff";
		case "estudiante":
			return "#28a745";
		default:
			return "#6c757d";
		}
	}

	/**
	 * Obtiene un ícono representativo del rol del usuario.
	 * 
	 * @return nombre del ícono de PrimeFaces correspondiente al rol.
	 */
	public String getRoleIcon() {
		if (role == null)
			return "pi pi-user";

		switch (role.toLowerCase()) {
		case "administrador":
			return "pi pi-crown";
		case "profesor":
			return "pi pi-user-edit";
		case "estudiante":
			return "pi pi-graduation-cap";
		default:
			return "pi pi-user";
		}
	}

	/** @return la imagen de perfil cargada. */
	public UploadedFile getProfileImage() {
		return profileImage;
	}

	/** @param profileImage la imagen de perfil a asignar. */
	public void setProfileImage(UploadedFile profileImage) {
		this.profileImage = profileImage;
	}

	/** @return el nombre de usuario autenticado. */
	public String getUsername() {
		return username;
	}

	/** @param username el nombre de usuario a asignar. */
	public void setUsername(String username) {
		this.username = username;
	}

	/** @return el rol del usuario. */
	public String getRole() {
		return role;
	}

	/** @param role el rol del usuario a asignar. */
	public void setRole(String role) {
		this.role = role;
	}

	/** @return la cadena base64 de la imagen de perfil. */
	public String getProfileImagePath() {
		return profileImagePath;
	}

	/** @param profileImagePath la cadena base64 de la imagen a asignar. */
	public void setProfileImagePath(String profileImagePath) {
		this.profileImagePath = profileImagePath;
	}
}
