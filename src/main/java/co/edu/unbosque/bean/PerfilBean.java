package co.edu.unbosque.bean;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import org.primefaces.model.file.UploadedFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

@SessionScoped
@Named("perfilBean")
public class PerfilBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private UploadedFile profileImage;
	private String username;
	private String role;
	private String profileImagePath = "assets/default-avatar.png"; // Imagen por defecto

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

				// Verificar si existe imagen del usuario
				if (username != null) {
					String userImagePath = "assets/profile/" + username + ".jpg";
					File imageFile = new File(
							FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + userImagePath);

					if (imageFile.exists()) {
						profileImagePath = userImagePath;
					}
				}
			}
		} catch (Exception e) {
			System.err.println("Error cargando datos del usuario: " + e.getMessage());
		}
	}

	public void uploadProfileImage() {
		if (profileImage != null && username != null) {
			try {
				// Crear directorio si no existe
				String uploadPath = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/assets/profile/");
				File uploadDir = new File(uploadPath);
				if (!uploadDir.exists()) {
					uploadDir.mkdirs();
				}

				// Guardar imagen con el nombre del usuario
				String fileName = username + ".jpg";
				File file = new File(uploadPath + fileName);

				try (FileOutputStream fos = new FileOutputStream(file)) {
					fos.write(profileImage.getContent());
				}

				// Actualizar ruta de la imagen
				profileImagePath = "assets/profile/" + fileName;

				FacesContext.getCurrentInstance().addMessage("perfilForm", new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Éxito", "Imagen de perfil actualizada correctamente"));

			} catch (IOException e) {
				FacesContext.getCurrentInstance().addMessage("perfilForm", new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Error al subir la imagen: " + e.getMessage()));
				e.printStackTrace();
			}
		} else {
			FacesContext.getCurrentInstance().addMessage("perfilForm",
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Por favor selecciona una imagen"));
		}
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

	// Getters y Setters
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
