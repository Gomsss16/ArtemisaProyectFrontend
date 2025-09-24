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

@RequestScoped
@Named("temarioBean")
public class TemarioBean {

	private List<TemarioDTO> temarios = new ArrayList<>();
	private List<TemarioDTO> filteredTemarios = new ArrayList<>();
	private String temaAlgoritmo = "";
	private String tipo = "";
	private String contenido = "";
	private String codigo = "";
	private List<String> tipos = Arrays.asList("Algoritmo", "Estructura de Datos", "Matemáticas", "Grafos", "DP",
			"Greedy", "Backtracking", "Búsquedas", "Ordenamiento", "Otros");
	private Gson gson = new Gson();

	public TemarioBean() {
		cargarTemarios();
	}

	public void cargarTemarios() {
		try {
			System.out.println("=== CARGANDO TEMARIOS ===");
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
						System.out.println("Temarios validos cargados: " + temarios.size());

					} catch (Exception e) {
						System.err.println("Error parseando JSON: " + e.getMessage());
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
			System.err.println("Error cargando temarios: " + e.getMessage());
			temarios = new ArrayList<>();
			filteredTemarios = new ArrayList<>();
		}
	}

	public void crearTemario() {
		try {
			System.out.println("=== CREANDO TEMARIO ===");

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

	public void eliminarPorId(Long id) {
		try {
			System.out.println("=== ELIMINANDO TEMARIO POR ID: " + id + " ===");

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
		temaAlgoritmo = "";
		tipo = "";
		contenido = "";
		codigo = "";
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

	// Getters y Setters normales
	public List<TemarioDTO> getTemarios() {
		return temarios;
	}

	public void setTemarios(List<TemarioDTO> temarios) {
		this.temarios = temarios;
	}

	public List<TemarioDTO> getFilteredTemarios() {
		return filteredTemarios;
	}

	public void setFilteredTemarios(List<TemarioDTO> filteredTemarios) {
		this.filteredTemarios = filteredTemarios;
	}

	public String getTemaAlgoritmo() {
		return temaAlgoritmo;
	}

	public void setTemaAlgoritmo(String temaAlgoritmo) {
		this.temaAlgoritmo = temaAlgoritmo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public List<String> getTipos() {
		return tipos;
	}

	public void setTipos(List<String> tipos) {
		this.tipos = tipos;
	}
}
