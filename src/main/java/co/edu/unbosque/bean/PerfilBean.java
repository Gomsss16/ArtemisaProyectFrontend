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

@SessionScoped
@Named("perfilBean")
public class PerfilBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private UploadedFile profileImage;
	private String username;
	private String role;
	private String profileImagePath = "";
	private Gson gson = new Gson();

	public PerfilBean() {
		loadUserData();
	}

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
			System.err.println("Error cargando datos del usuario: " + e.getMessage());
			profileImagePath = getDefaultImage();
		}
	}

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
			System.err.println("Error obteniendo imagen: " + e.getMessage());
		}
		return null;
	}

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
	                
	                FacesContext.getCurrentInstance().addMessage("perfilForm",
	                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", 
	                        "Imagen de perfil actualizada correctamente"));

	                System.out.println("=== IMAGEN ACTUALIZADA ===");
	                System.out.println("Nueva imagen: " + profileImagePath.substring(0, 50) + "...");
	                
	            } else {
	                FacesContext.getCurrentInstance().addMessage("perfilForm",
	                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", 
	                        "Error actualizando imagen: " + response));
	            }

	        } catch (Exception e) {
	            FacesContext.getCurrentInstance().addMessage("perfilForm",
	                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", 
	                    "Error procesando imagen: " + e.getMessage()));
	            e.printStackTrace();
	        }
	    } else {
	        FacesContext.getCurrentInstance().addMessage("perfilForm",
	                new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", 
	                "Por favor selecciona una imagen"));
	    }
	}


	private String getDefaultImage() {
		return "data:image/svg+xml;base64," + Base64.getEncoder().encodeToString(
				("<svg xmlns='http://www.w3.org/2000/svg' width='120' height='120' viewBox='0 0 120 120'>"
						+ "<circle cx='60' cy='60' r='60' fill='#e0e0e0'/>"
						+ "<circle cx='60' cy='45' r='15' fill='#999'/>"
						+ "<ellipse cx='60' cy='85' rx='25' ry='20' fill='#999'/>" + "</svg>").getBytes());
	}

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

	public UploadedFile getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(UploadedFile profileImage) {
		this.profileImage = profileImage;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getProfileImagePath() {
		return profileImagePath;
	}

	public void setProfileImagePath(String profileImagePath) {
		this.profileImagePath = profileImagePath;
	}
}
