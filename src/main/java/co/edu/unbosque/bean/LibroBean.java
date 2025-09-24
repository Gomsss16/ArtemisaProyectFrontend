package co.edu.unbosque.bean;

import co.edu.unbosque.dto.LibroDTO;
import co.edu.unbosque.service.LibroService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.reflect.TypeToken;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import org.primefaces.model.file.UploadedFile;
import java.awt.Image;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestScoped
@Named("libroBean")
public class LibroBean {

	private List<LibroDTO> books = new ArrayList<>();
	private String titulo = "";
	private String author = "";
	private String description = "";
	private String linkVirtual = "";
	private UploadedFile coverFile;
	private UploadedFile bookFile;

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

	public LibroBean() {
		cargarLibro();
	}

	public void cargarLibro() {
		try {
			System.out.println("=== CARGANDO LIBROS ===");
			String respuesta = LibroService.doGet("http://localhost:8081/libro/getall");

			if (respuesta != null && !respuesta.contains("Error")) {
				String[] partes = respuesta.split("\n", 2);

				if (partes.length > 1 && !partes[1].equals("[]")) {
					try {
						Type listType = new TypeToken<List<LibroDTO>>() {
						}.getType();
						List<LibroDTO> lista = gson.fromJson(partes[1], listType);

						if (lista != null) {
							books = lista.stream().filter(l -> l.getTitulo() != null && !l.getTitulo().trim().isEmpty())
									.collect(Collectors.toList());
						} else {
							books = new ArrayList<>();
						}

						System.out.println("Libros válidos cargados: " + books.size());

					} catch (Exception e) {
						System.err.println("Error parseando JSON: " + e.getMessage());
						books = new ArrayList<>();
					}
				} else {
					books = new ArrayList<>();
				}
			} else {
				books = new ArrayList<>();
			}

		} catch (Exception e) {
			System.err.println("Error cargando libros: " + e.getMessage());
			books = new ArrayList<>();
		}
	}

	public void addBook() {
		try {
			System.out.println("=== CREANDO LIBRO ===");

			if (titulo == null || titulo.trim().isEmpty()) {
				showMessage("Error", "El título es obligatorio");
				return;
			}

			if (author == null || author.trim().isEmpty()) {
				showMessage("Error", "El autor es obligatorio");
				return;
			}

			Map<String, Object> libroParaJson = new HashMap<>();
			libroParaJson.put("titulo", titulo.trim());
			libroParaJson.put("author", author.trim());
			libroParaJson.put("descricpcion", description != null ? description.trim() : "");
			libroParaJson.put("coverUrl", "https://via.placeholder.com/180x240?text=" + titulo.replace(" ", "+"));
			libroParaJson.put("bookUrl", "https://example.com/download/" + titulo + ".pdf");
			libroParaJson.put("onlineUrl", linkVirtual != null && !linkVirtual.trim().isEmpty() ? linkVirtual.trim()
					: "https://example.com/online/" + titulo);

			String json = gson.toJson(libroParaJson);
			String respuesta = LibroService.doPost("http://localhost:8081/libro/createlibrojson", json);

			if (respuesta != null && respuesta.startsWith("201")) {
				showMessage("201", "Libro '" + titulo + "' creado exitosamente");
				limpiarCampos();
				cargarLibro();

			} else if (respuesta != null && respuesta.contains("ya existe")) {
				showMessage("409", "El libro '" + titulo + "' ya existe");

			} else {
				showMessage("Error", "Error del servidor: " + respuesta);
			}

		} catch (Exception e) {
			showMessage("Error", "Error interno: " + e.getMessage());
		}
	}

	public void eliminarPorId(Long id) {
		try {
			System.out.println("=== ELIMINANDO LIBRO POR ID: " + id + " ===");

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
		author = "";
		description = "";
		linkVirtual = "";
		coverFile = null;
		bookFile = null;
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

	public String getLinkVirtual() {
		return linkVirtual;
	}

	public void setLinkVirtual(String linkVirtual) {
		this.linkVirtual = linkVirtual;
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
}
