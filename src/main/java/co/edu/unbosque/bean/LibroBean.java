package co.edu.unbosque.bean;

import co.edu.unbosque.dto.LibroDTO;
import co.edu.unbosque.service.LibroService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.reflect.TypeToken;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Bean encargado de la gestión de libros dentro del sistema Artemisa.
 * <p>
 * Se comunica con el backend mediante {@link LibroService} para realizar
 * operaciones de CRUD. Además, mantiene el estado de la vista en JSF.
 * </p>
 * 
 * <ul>
 * <li>Cargar libros desde el servidor.</li>
 * <li>Agregar nuevos libros con portada y PDF.</li>
 * <li>Eliminar libros por título.</li>
 * <li>Seleccionar y descargar libros.</li>
 * <li>Mostrar mensajes de notificación al usuario.</li>
 * </ul>
 * 
 * Anotado con {@link RequestScoped} y {@link Named} para ser usado como Managed
 * Bean en JSF.
 * 
 * @author
 */
@RequestScoped
@Named("libroBean")
public class LibroBean {

	/** Lista de libros cargados desde el backend. */
	private List<LibroDTO> books = new ArrayList<>();
	/** Título del libro en edición o creación. */
	private String titulo = "";
	/** Autor del libro. */
	private String author = "";
	/** Descripción del libro. */
	private String description = "";
	/** Enlace externo al libro. */
	private String enlace = "";
	/** Archivo de portada cargado por el usuario. */
	private UploadedFile coverFile;
	/** Archivo PDF cargado por el usuario. */
	private UploadedFile bookFile;

	/** Objeto Gson configurado para serialización y deserialización de libros. */
	private Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(new ExclusionStrategy() {
		@Override
		public boolean shouldSkipField(FieldAttributes fieldAttributes) {
			return fieldAttributes.getDeclaredClass() == Image.class || fieldAttributes.getDeclaredClass() == File.class
					|| fieldAttributes.getName().equals("portadaDelLibro")
					|| fieldAttributes.getName().equals("libroPDF");
		}

		@Override
		public boolean shouldSkipClass(Class<?> aClass) {
			return aClass == Image.class || aClass == File.class;
		}
	}).create();

	/** Libro actualmente seleccionado. */
	private LibroDTO libroSeleccionado = new LibroDTO();

	/**
	 * Constructor por defecto. Inicializa el bean cargando los libros desde el
	 * backend.
	 */
	public LibroBean() {
		cargarLibro();
	}

	/**
	 * Carga todos los libros disponibles desde el backend. En caso de error o
	 * respuesta vacía, inicializa la lista como vacía.
	 */
	public void cargarLibro() {
		try {
			String respuesta = LibroService.doGet("http://localhost:8081/libro/getall");

			if (respuesta != null && !respuesta.contains("Error")) {
				String[] partes = respuesta.split("\n", 2);

				if (partes.length > 1) {
					String jsonData = partes[1];

					if (!jsonData.equals("[]")) {
						try {
							Type listType = new TypeToken<List<LibroDTO>>() {
							}.getType();
							List<LibroDTO> lista = gson.fromJson(jsonData, listType);

							if (lista != null) {
								books = lista;
							} else {
								books = new ArrayList<>();
							}

						} catch (JsonSyntaxException e) {
							books = new ArrayList<>();
						}
					} else {
						books = new ArrayList<>();
					}
				} else {
					books = new ArrayList<>();
				}
			} else {
				books = new ArrayList<>();
			}

		} catch (Exception e) {
			e.printStackTrace();
			books = new ArrayList<>();
		}
	}

	/**
	 * Agrega un nuevo libro validando título y autor. Convierte la portada y el PDF
	 * a Base64 antes de enviarlos al backend.
	 */
	public void addBook() {
		try {
			if (titulo == null || titulo.trim().isEmpty()) {
				showMessage("Error", "El título es obligatorio");
				return;
			}

			if (author == null || author.trim().isEmpty()) {
				showMessage("Error", "El autor es obligatorio");
				return;
			}

			String coverBase64 = "";
			if (coverFile != null) {
				try (InputStream input = coverFile.getInputStream()) {
					byte[] bytes = input.readAllBytes();
					coverBase64 = Base64.getEncoder().encodeToString(bytes);
				}
			}

			String pdfBase64 = "";
			if (bookFile != null) {
				try (InputStream input = bookFile.getInputStream()) {
					byte[] bytes = input.readAllBytes();
					pdfBase64 = Base64.getEncoder().encodeToString(bytes);
				}
			}

			LibroDTO nuevo = new LibroDTO();
			nuevo.setTitulo(titulo.trim());
			nuevo.setAuthor(author.trim());
			nuevo.setDescripcion(description != null ? description.trim() : "");
			nuevo.setEnlace(enlace != null ? enlace.trim() : "");
			nuevo.setImagenBase64(coverBase64);
			nuevo.setPdfBase64(pdfBase64);

			String json = gson.toJson(nuevo);
			String respuesta = LibroService.doPost("http://localhost:8081/libro/createlibrojson", json);

			if (respuesta != null && respuesta.startsWith("201")) {
				showMessage("201", "Libro '" + titulo + "' creado exitosamente");
				limpiarCampos();
				cargarLibro();
			} else {
				showMessage("Error", "Error del servidor: " + respuesta);
			}

		} catch (Exception e) {
			showMessage("Error", "Error interno: " + e.getMessage());
		}
	}

	/**
	 * Elimina un libro según su ID.
	 *
	 * @param id Identificador del libro a eliminar.
	 */
	public void eliminarPorId(Long id) {
		try {
			if (id == null) {
				showMessage("Error", "Error: ID es null");
				return;
			}

			LibroDTO libroAEliminar = null;
			for (LibroDTO l : books) {
				if (l.getId() != null && l.getId().equals(id)) {
					libroAEliminar = l;
					break;
				}
			}

			if (libroAEliminar == null) {
				showMessage("Error", "No se encontró el libro con ID: " + id);
				return;
			}

			String titleEncoded = java.net.URLEncoder.encode(libroAEliminar.getTitulo(), "UTF-8");
			String url = "http://localhost:8081/libro/deletebyTitle?title=" + titleEncoded;

			String respuesta = LibroService.doDelete(url);
			if (respuesta != null && (respuesta.startsWith("200") || respuesta.startsWith("202"))) {
				showMessage("200", "Libro '" + libroAEliminar.getTitulo() + "' eliminado");
				cargarLibro();
			} else {
				showMessage("Error", "Error eliminando: " + respuesta);
			}

		} catch (Exception e) {
			showMessage("Error", "Error: " + e.getMessage());
		}
	}

	/**
	 * Verifica si el usuario actual tiene rol de estudiante.
	 * 
	 * @return true si el rol en sesión es "Estudiante", false en otro caso.
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

	/**
	 * Limpia los campos del formulario de creación de libro.
	 */
	private void limpiarCampos() {
		titulo = "";
		author = "";
		description = "";
		coverFile = null;
		bookFile = null;
	}

	/**
	 * Selecciona un libro desde la vista.
	 *
	 * @param libro {@link LibroDTO} seleccionado.
	 */
	public void seleccionarLibro(LibroDTO libro) {
		this.libroSeleccionado = libro;
	}

	/**
	 * Muestra un mensaje en la interfaz dependiendo del código de estado recibido.
	 *
	 * @param code    Código del mensaje (ejemplo: 200, 201, 404, 409).
	 * @param content Contenido del mensaje.
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
	public boolean puedeEditar() {
	    try {
	        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
	                .getExternalContext().getSession(false);
	        
	        if (session != null) {
	            String role = (String) session.getAttribute("role");
	            return "Profesor".equals(role) || "Administrador".equals(role);
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


	public List<LibroDTO> getBooks() {
		return books;
	}

	public void setBooks(List<LibroDTO> books) {
		this.books = books;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UploadedFile getCoverFile() {
		return coverFile;
	}

	public void setCoverFile(UploadedFile coverFile) {
		this.coverFile = coverFile;
	}

	public UploadedFile getBookFile() {
		return bookFile;
	}

	public void setBookFile(UploadedFile bookFile) {
		this.bookFile = bookFile;
	}

	public Gson getGson() {
		return gson;
	}

	public void setGson(Gson gson) {
		this.gson = gson;
	}

	public String getEnlace() {
		return enlace;
	}

	public void setEnlace(String enlace) {
		this.enlace = enlace;
	}

	public LibroDTO getLibroSeleccionado() {
		return libroSeleccionado;
	}

	public void setLibroSeleccionado(LibroDTO libroSeleccionado) {
		this.libroSeleccionado = libroSeleccionado;
	}

	/**
	 * Descarga el PDF de un libro en formato {@link StreamedContent}.
	 *
	 * @param libro Libro cuyo PDF será descargado.
	 * @return PDF en {@link StreamedContent} o null si no existe.
	 */
	public StreamedContent descargarPDF(LibroDTO libro) {
		if (libro != null && libro.getPdfBase64() != null && !libro.getPdfBase64().isEmpty()) {
			byte[] pdfBytes = Base64.getDecoder().decode(libro.getPdfBase64());
			return DefaultStreamedContent.builder().name(libro.getTitulo() + ".pdf").contentType("application/pdf")
					.stream(() -> new ByteArrayInputStream(pdfBytes)).build();
		}
		return null;
	}
}
