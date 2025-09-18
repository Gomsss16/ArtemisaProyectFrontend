package co.edu.unbosque.bean;

import co.edu.unbosque.service.AdministradorService;
import co.edu.unbosque.service.EstudianteService;
import co.edu.unbosque.service.ProfesorService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;

@RequestScoped
@Named("LoginBean")
public class LoginBean {

    private String usuario = "";
    private String contrasenia = "";
    private String confirmarContrasena = "";
    private String nivelDePermiso = "";

    public String login() {
        try {
            if (usuario == null || usuario.trim().isEmpty()) {
                showStickyLogin("Error", "El usuario no puede estar vacío");
                return null;
            }
            
            if (contrasenia == null || contrasenia.trim().isEmpty()) {
                showStickyLogin("Error", "La contraseña no puede estar vacía");
                return null;
            }
            
            if (nivelDePermiso == null || nivelDePermiso.trim().isEmpty()) {
                showStickyLogin("Error", "Debe seleccionar un rol");
                return null;
            }

            String json = "{"
                    + "\"usuario\":\"" + usuario + "\","
                    + "\"contrasenia\":\"" + contrasenia + "\""
                    + "}";
            
            String respuesta = "";
            boolean loginExitoso = false;
            
            switch (nivelDePermiso) {
                case "Administrador":
                    respuesta = AdministradorService.doPost("http://localhost:8081/admin/loginadmin", json);
                    break;
                case "Profesor":
                    respuesta = ProfesorService.doPost("http://localhost:8081/profesor/loginprofesor", json);
                    break;
                case "Estudiante":
                    respuesta = EstudianteService.doPost("http://localhost:8081/estudiante/loginestudiantejson", json);
                    break;
                default:
                    showStickyLogin("Error", "Rol no válido");
                    return null;
            }

            if (respuesta != null && 
                (respuesta.contains("Login exitoso") || respuesta.contains("exitoso") || 
                 respuesta.contains("200") || (!respuesta.contains("Error") && !respuesta.contains("no encontrado")))) {
                loginExitoso = true;
            }

            if (loginExitoso) {
                HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                        .getExternalContext().getSession(true);
                session.setAttribute("username", usuario);
                session.setAttribute("role", nivelDePermiso);
                session.setAttribute("loggedIn", true);
                
                showStickyLogin("200", "Bienvenido al Sistema Artemisa - " + nivelDePermiso + ": " + usuario);
                
                return "temario?faces-redirect=true";
                
            } else {
                if (respuesta != null && respuesta.contains("no encontrado")) {
                    showStickyLogin("404", "El usuario '" + usuario + "' no existe como " + nivelDePermiso);
                } else if (respuesta != null && respuesta.contains("Contraseña incorrecta")) {
                    showStickyLogin("401", "Contraseña incorrecta para " + nivelDePermiso);
                } else {
                    showStickyLogin("401", "Credenciales incorrectas para " + nivelDePermiso);
                }
                return null;
            }

        } catch (Exception e) {
            showStickyLogin("Error", "Error en el sistema: " + e.getMessage());
            return null;
        }
    }

    public void registrar() {
        try {
            if (usuario == null || usuario.trim().isEmpty() ||
                contrasenia == null || contrasenia.trim().isEmpty() ||
                confirmarContrasena == null || confirmarContrasena.trim().isEmpty() ||
                nivelDePermiso == null || nivelDePermiso.trim().isEmpty()) {
                showStickyLogin("Error", "Todos los campos son obligatorios");
                return;
            }

            if (!contrasenia.equals(confirmarContrasena)) {
                showStickyLogin("Error", "Las contraseñas no coinciden");
                return;
            }

            if (contrasenia.length() < 6) {
                showStickyLogin("Error", "La contraseña debe tener al menos 6 caracteres");
                return;
            }

            String json = "{"
                    + "\"usuario\":\"" + usuario + "\","
                    + "\"contrasenia\":\"" + contrasenia + "\","
                    + "\"nivelDePermiso\":\"" + nivelDePermiso + "\""
                    + "}";

            String respuesta = "";
            switch (nivelDePermiso) {
                case "Administrador":
                    respuesta = AdministradorService.doPost("http://localhost:8081/admin/createadminjson", json);
                    break;
                case "Profesor":
                    respuesta = ProfesorService.doPost("http://localhost:8081/profesor/createprofesorjson", json);
                    break;
                case "Estudiante":
                    respuesta = EstudianteService.doPost("http://localhost:8081/estudiante/createestudiantejson", json);
                    break;
                default:
                    showStickyLogin("Error", "Debe seleccionar un rol válido");
                    return;
            }

            if (respuesta != null && 
                (respuesta.contains("exitosamente") || respuesta.contains("201") || respuesta.contains("creado"))) {
                showStickyLogin("201", nivelDePermiso + " '" + usuario + "' registrado exitosamente en Artemisa");
            } else if (respuesta != null && respuesta.contains("existe")) {
                showStickyLogin("409", "El usuario '" + usuario + "' ya existe como " + nivelDePermiso);
            } else {
                showStickyLogin("406", "Error en el registro: " + respuesta);
            }

        } catch (Exception e) {
            showStickyLogin("Error", "Error en el registro: " + e.getMessage());
        }
    }

    public String logout() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "index?faces-redirect=true";
    }

    public void checkAccess() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
        
        if (session == null || session.getAttribute("loggedIn") == null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
            } catch (Exception e) {
                System.err.println("Error redirigiendo: " + e.getMessage());
            }
        }
    }

    public void showStickyLogin(String code, String content) {
        FacesContext context = FacesContext.getCurrentInstance();
        
        if ("201".equals(code) || "200".equals(code)) {
            context.addMessage("sticky-key",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Operación Exitosa", content));
            context.addMessage("growl",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sistema Artemisa", content));
                    
        } else if ("409".equals(code)) {
            context.addMessage("sticky-key",
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuario Existente", content));
            context.addMessage("growl",
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Registro Fallido", content));
                    
        } else if ("401".equals(code)) {
            context.addMessage("sticky-key",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Acceso Denegado", content));
            context.addMessage("growl",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Fallido", content));
                    
        } else if ("404".equals(code)) {
            context.addMessage("sticky-key",
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuario No Encontrado", content));
            context.addMessage("growl",
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Fallido", content));
                    
        } else if ("406".equals(code)) {
            context.addMessage("sticky-key",
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Error de Validación", content));
            context.addMessage("growl",
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Datos Inválidos", content));
                    
        } else {
            context.addMessage("sticky-key", 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error del Sistema", content));
            context.addMessage("growl",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Crítico", content));
        }
    }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    
    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }
    
    public String getConfirmarContrasena() { return confirmarContrasena; }
    public void setConfirmarContrasena(String confirmarContrasena) { this.confirmarContrasena = confirmarContrasena; }
    
    public String getNivelDePermiso() { return nivelDePermiso; }
    public void setNivelDePermiso(String nivelDePermiso) { this.nivelDePermiso = nivelDePermiso; }

    public LoginBean() { }
}
