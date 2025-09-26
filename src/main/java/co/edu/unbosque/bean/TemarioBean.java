package co.edu.unbosque.bean;

import co.edu.unbosque.dto.TemarioDTO;
import co.edu.unbosque.service.TemarioService;
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
 * Bean encargado de gestionar la interacción entre la vista JSF y la lógica de
 * negocio relacionada con los {@link TemarioDTO}.
 * <p>
 * Permite crear, listar, eliminar y filtrar temarios, así como validar la
 * sesión del usuario y mostrar mensajes al sistema.
 * </p>
 * 
 * @author
 * @version 1.0
 */
@RequestScoped
@Named("temarioBean")
public class TemarioBean {

	/** Lista completa de temarios cargados desde el servicio REST. */
	private List<TemarioDTO> temarios = new ArrayList<>();

	/** Lista de temarios filtrados para búsquedas en la vista. */
	private List<TemarioDTO> filteredTemarios = new ArrayList<>();

	/** Tema o nombre del algoritmo ingresado por el usuario. */
	private String temaAlgoritmo = "";

	/** Tipo de algoritmo o categoría seleccionada. */
	private String tipo = "";

	/** Contenido o descripción del temario. */
	private String contenido = "";

	/** Código fuente de ejemplo asociado al temario. */
	private String codigo = "";

	/** Lista fija de tipos predefinidos de temarios. */
	private List<String> tipos = Arrays.asList("Algoritmo", "Estructura de Datos", "Matemáticas", "Grafos", "DP",
			"Greedy", "Backtracking", "Búsquedas", "Ordenamiento", "Otros");

	/** Objeto Gson para manejo de serialización y deserialización JSON. */
	private Gson gson = new Gson();

	/**
	 * Constructor que inicializa el bean y carga los temarios disponibles desde el
	 * servicio.
	 */
	public TemarioBean() {
		cargarTemarios();
	}

	/**
	 * Carga los temarios desde el servicio REST remoto.
	 * <p>
	 * Los datos obtenidos se filtran para evitar registros nulos o vacíos.
	 * </p>
	 */
	public void cargarTemarios() {
		try {
			String respuesta = TemarioService.doGet("http://localhost:8081/temario/getall");

			if (respuesta != null && !respuesta.contains("Error")) {
				String[] partes = respuesta.split("\n", 2);

				if (partes.length > 1 && !partes[1].equals("[]")) {
					try {
						Type listType = new TypeToken<List<TemarioDTO>>() {
						}.getType();
						List<TemarioDTO> lista = gson.fromJson(partes[1], listType);

						if (lista != null) {
							temarios = lista.stream()
									.filter(t -> t.getTemaAlgoritmo() != null && !t.getTemaAlgoritmo().trim().isEmpty())
									.collect(Collectors.toList());
						} else {
							temarios = new ArrayList<>();
						}

						filteredTemarios = new ArrayList<>(temarios);

					} catch (Exception e) {
						temarios = new ArrayList<>();
						filteredTemarios = new ArrayList<>();
					}
				} else {
					temarios = new ArrayList<>();
					filteredTemarios = new ArrayList<>();
				}
			} else {
				temarios = new ArrayList<>();
				filteredTemarios = new ArrayList<>();
			}

		} catch (Exception e) {
			temarios = new ArrayList<>();
			filteredTemarios = new ArrayList<>();
		}
	}

	/**
	 * Crea un nuevo temario y lo envía al servicio REST.
	 * <p>
	 * Valida que los campos obligatorios estén completos antes de enviar.
	 * </p>
	 */
	public void crearTemario() {
		try {
			if (temaAlgoritmo == null || temaAlgoritmo.trim().isEmpty()) {
				showMessage("Error", "El tema/algoritmo es obligatorio");
				return;
			}

			if (tipo == null || tipo.trim().isEmpty()) {
				showMessage("Error", "El tipo es obligatorio");
				return;
			}

			TemarioDTO nuevoTemario = new TemarioDTO(null, temaAlgoritmo.trim(), tipo.trim(),
					contenido != null ? contenido.trim() : "", codigo != null ? codigo.trim() : "");

			String json = gson.toJson(nuevoTemario);
			String respuesta = TemarioService.doPost("http://localhost:8081/temario/createtemariojson", json);

			if (respuesta != null && (respuesta.contains("exitosamente") || respuesta.contains("creado")
					|| respuesta.contains("message") || respuesta.contains("Temario creado"))) {

				showMessage("201", "Temario '" + temaAlgoritmo + "' creado exitosamente");
				limpiarCampos();
				cargarTemarios();

			} else if (respuesta != null && respuesta.contains("ya existe")) {
				showMessage("409", "El temario '" + temaAlgoritmo + "' ya existe");

			} else {
				showMessage("Error", "Error del servidor: " + respuesta);
			}

		} catch (Exception e) {
			showMessage("Error", "Error interno: " + e.getMessage());
		}
	}

	/**
	 * Elimina un temario por su ID, enviando la petición al servicio REST.
	 * 
	 * @param id ID del temario a eliminar.
	 */
	public void eliminarPorId(Long id) {
		try {
			if (id == null) {
				showMessage("Error", "Error: ID es null");
				return;
			}

			TemarioDTO temarioAEliminar = null;
			for (TemarioDTO t : temarios) {
				if (t.getId() != null && t.getId().equals(id)) {
					temarioAEliminar = t;
					break;
				}
			}

			if (temarioAEliminar == null) {
				showMessage("Error", "No se encontro el temario con ID: " + id);
				return;
			}

			String temaEncoded = java.net.URLEncoder.encode(temarioAEliminar.getTemaAlgoritmo(), "UTF-8");
			String url = "http://localhost:8081/temario/deletebyTema?temaAlgoritmo=" + temaEncoded;

			String respuesta = TemarioService.doDelete(url);

			if (respuesta != null && respuesta.contains("successfully")) {
				showMessage("200", "Temario '" + temarioAEliminar.getTemaAlgoritmo() + "' eliminado");
				cargarTemarios();
			} else {
				showMessage("Error", "Error eliminando: " + respuesta);
			}

		} catch (Exception e) {
			showMessage("Error", "Error: " + e.getMessage());
		}
	}

	/**
	 * Verifica si el usuario autenticado es un estudiante según el rol en sesión.
	 * 
	 * @return {@code true} si el usuario es estudiante, {@code false} en caso
	 *         contrario.
	 */
	public boolean puedeEditar() {
	    try {
	        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
	                .getExternalContext().getSession(false);
	        
	        if (session != null) {
	            String role = (String) session.getAttribute("role");
	            // Profesor y Administrador pueden editar temarios
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

	/**
	 * Limpia los campos del formulario para evitar datos residuales.
	 */
	private void limpiarCampos() {
		temaAlgoritmo = "";
		tipo = "";
		contenido = "";
		codigo = "";
	}

	/**
	 * Muestra un mensaje en la interfaz de usuario usando el componente Growl.
	 * 
	 * @param code    Código de estado o error (ejemplo: 201, 409, 404).
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
	


	/**
	 * @return Lista de temarios cargados.
	 */
	public List<TemarioDTO> getTemarios() {
		return temarios;
	}

	/**
	 * @param temarios Lista de temarios a establecer.
	 */
	public void setTemarios(List<TemarioDTO> temarios) {
		this.temarios = temarios;
	}

	/**
	 * @return Lista filtrada de temarios.
	 */
	public List<TemarioDTO> getFilteredTemarios() {
		return filteredTemarios;
	}

	/**
	 * @param filteredTemarios Lista de temarios filtrados a establecer.
	 */
	public void setFilteredTemarios(List<TemarioDTO> filteredTemarios) {
		this.filteredTemarios = filteredTemarios;
	}

	/**
	 * @return Tema o algoritmo actual.
	 */
	public String getTemaAlgoritmo() {
		return temaAlgoritmo;
	}

	/**
	 * @param temaAlgoritmo Tema o algoritmo a establecer.
	 */
	public void setTemaAlgoritmo(String temaAlgoritmo) {
		this.temaAlgoritmo = temaAlgoritmo;
	}

	/**
	 * @return Tipo de temario seleccionado.
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo Tipo de temario a establecer.
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return Contenido del temario.
	 */
	public String getContenido() {
		return contenido;
	}

	/**
	 * @param contenido Contenido del temario a establecer.
	 */
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	/**
	 * @return Código fuente asociado al temario.
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo Código fuente a establecer.
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return Lista de tipos disponibles.
	 */
	public List<String> getTipos() {
		return tipos;
	}

	/**
	 * @param tipos Lista de tipos a establecer.
	 */
	public void setTipos(List<String> tipos) {
		this.tipos = tipos;
	}
}
