package co.edu.unbosque.bean;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;

import org.primefaces.model.file.UploadedFile;

@SessionScoped
@Named("perfilBean")
public class PerfilBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private UploadedFile profileImage;
	private String profileImagePath = "assets/LogoArtemisa.png";

	public String getUsername() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		return session != null && session.getAttribute("username") != null ? (String) session.getAttribute("username")
				: "Usuario Demo";
	}

	public String getRole() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		return session != null && session.getAttribute("role") != null ? (String) session.getAttribute("role")
				: "Estudiante";
	}

	public String getRoleIcon() {
		switch (getRole()) {
		case "Administrador":
			return "pi pi-crown";
		case "Profesor":
			return "pi pi-graduation-cap";
		case "Estudiante":
			return "pi pi-user";
		default:
			return "pi pi-user";
		}
	}

	public String getRoleColor() {
		switch (getRole()) {
		case "Administrador":
			return "#e74c3c";
		case "Profesor":
			return "#3498db";
		case "Estudiante":
			return "#2ecc71";
		default:
			return "#95a5a6";
		}
	}

	public void uploadProfileImage() {
	    System.out.println("=== UPLOAD INICIADO ===");
	    
	    if (profileImage != null && profileImage.getSize() > 0) {
	        try {
	            String fileName = profileImage.getFileName();
	            System.out.println("Archivo: " + fileName + " (" + profileImage.getSize() + " bytes)");
	            
	            if (fileName != null) {
	                String extension = fileName.substring(fileName.lastIndexOf('.'));
	                String newFileName = "perfil_" + getUsername() + extension;
	                
	                // Crear carpeta uploads si no existe
	                String contextPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
	                String uploadPath = contextPath + "resources" + File.separator + "uploads" + File.separator;
	                File uploadDir = new File(uploadPath);
	                if (!uploadDir.exists()) {
	                    uploadDir.mkdirs();
	                }
	                
	                // Guardar archivo
	                File file = new File(uploadPath + newFileName);
	                try (InputStream input = profileImage.getInputStream();
	                     FileOutputStream output = new FileOutputStream(file)) {
	                    
	                    byte[] buffer = new byte[1024];
	                    int length;
	                    while ((length = input.read(buffer)) > 0) {
	                        output.write(buffer, 0, length);
	                    }
	                }
	                
	                this.profileImagePath = "resources/uploads/" + newFileName;
	                System.out.println("ÉXITO: Imagen guardada en " + this.profileImagePath);
	                
	                FacesContext.getCurrentInstance().addMessage(null,
	                    new FacesMessage(FacesMessage.SEVERITY_INFO, "¡Éxito!", "Imagen actualizada"));
	                
	            } else {
	                System.out.println("ERROR: Nombre de archivo inválido");
	            }
	            
	        } catch (Exception e) {
	            System.out.println("ERROR: " + e.getMessage());
	            e.printStackTrace();
	            FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo subir la imagen"));
	        }
	    } else {
	        System.out.println("ERROR: No hay archivo o está vacío");
	        FacesContext.getCurrentInstance().addMessage(null,
	            new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Selecciona una imagen"));
	    }
	    
	    System.out.println("=== UPLOAD FINALIZADO ===");
	}
	private String getFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		for (String token : contentDisp.split(";")) {
			if (token.trim().startsWith("filename")) {
				return token.substring(token.indexOf("=") + 2, token.length() - 1);
			}
		}
		return "imagen.jpg";
	}

	// Getters y Setters
	public UploadedFile getProfileImage() { return profileImage; }
	public void setProfileImage(UploadedFile profileImage) { 
	    System.out.println("setProfileImage: " + (profileImage != null ? "archivo recibido" : "null"));
	    this.profileImage = profileImage; 
	}

	public String getProfileImagePath() {
		return profileImagePath;
	}

	public void setProfileImagePath(String profileImagePath) {
		this.profileImagePath = profileImagePath;
	}
}
