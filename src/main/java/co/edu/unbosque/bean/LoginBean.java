package co.edu.unbosque.bean;


import co.edu.unbosque.service.AdministradorService;
import co.edu.unbosque.service.EstudianteService;
import co.edu.unbosque.service.ProfesorService;
import co.edu.unbosque.util.AESUtil;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@RequestScoped
@Named("LoginBean")
public class LoginBean {

	private String usuario = "";
	private String contrasenia = "";
	private String confirmarContrasena = "";
	private String nivelDePermiso = "";

	public void registrar() {
	    try {
	        // Validar contraseñas
	        if (!contrasenia.equals(confirmarContrasena)) {
	            showStickyLogin("Error", "Las contraseñas no coinciden");
	            return;
	        }

	        // Encriptar datos
//	        String userEnc = AESUtil.encrypt(usuario);
//	        String passEnc = AESUtil.encrypt(contrasena);

	        // Construir JSON (lo reutilizas)
	        String json = "{"
	                + "\"usuario\":\"" + usuario + "\","
	                + "\"contrasenia\":\"" + contrasenia + "\","
	                + "\"nivelDePermiso\":\"" + nivelDePermiso + "\""
	                + "}";

	        // Llamar al service correspondiente según el rol
	        String respuesta = "";
	        switch (nivelDePermiso) {
	            case "Administrador":
	                respuesta = AdministradorService.doPost("http://localhost:8081/admin/createadminjson", json);
	                break;
	            case "Profesor":
	                respuesta = ProfesorService.doPost("http://localhost:8081/profesor/createprofesorjson",json);
	                break;
	            case "Estudiante":
	                respuesta = EstudianteService.doPost("http://localhost:8081/estudiante/createestudiantejson",json);
	                break;
	            default:
	                showStickyLogin("Error", "Debe seleccionar un rol válido");
	                return;
	        }

	        // Mostrar mensaje
	        showStickyLogin("Registro exitoso", respuesta);

	        // Limpiar campos
	        usuario = "";
	        contrasenia = "";
	        confirmarContrasena = "";
	        nivelDePermiso = "";

	    } catch (Exception e) {
	        showStickyLogin("Error", "No se pudo registrar: " + e.getMessage());
	    }
	    
	}

	    
		public void showStickyLogin(String code, String content) {
			if (code.equals("201")) {
				FacesContext.getCurrentInstance().addMessage("sticky-key",
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Hecho", content));
			} else if (code.equals("406")) {
				FacesContext.getCurrentInstance().addMessage("sticky-key",
						new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", content));
			} else {
				System.out.println("Error en crear cuenta");
				System.out.println("Status code: " + code);
				System.out.println("reason: " + content);
				FacesContext.getCurrentInstance().addMessage("sticky-key", new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error Critico", "Error al crear," + "comuniquese con el administrador"));
			}
		}
		
		public LoginBean() {
			// TODO Auto-generated constructor stub
		}


		public String getUsuario() {
			return usuario;
		}


		public void setUsuario(String usuario) {
			this.usuario = usuario;
		}


		public String getContrasena() {
			return contrasenia;
		}


		public void setContrasena(String contrasena) {
			this.contrasenia = contrasena;
		}


		public String getConfirmarContrasena() {
			return confirmarContrasena;
		}


		public void setConfirmarContrasena(String confirmarContrasena) {
			this.confirmarContrasena = confirmarContrasena;
		}


		public String getContrasenia() {
			return contrasenia;
		}


		public void setContrasenia(String contrasenia) {
			this.contrasenia = contrasenia;
		}


		public String getNivelDePermiso() {
			return nivelDePermiso;
		}


		public void setNivelDePermiso(String nivelDePermiso) {
			this.nivelDePermiso = nivelDePermiso;
		}


	
		
		

}
