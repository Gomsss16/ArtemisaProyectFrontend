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
import org.primefaces.model.file.UploadedFile;
import java.awt.Image;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Base64;

import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Named("libroBean")
public class LibroBean {

	private List<LibroDTO> books = new ArrayList<>();
	private String titulo = "";
	private String author = "";
	private String description = "";
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
	        
	        System.out.println("Respuesta completa length: " + (respuesta != null ? respuesta.length() : 0));

	        if (respuesta != null && !respuesta.contains("Error")) {
	            String[] partes = respuesta.split("\n", 2);
	            System.out.println("Partes split: " + partes.length);

	            if (partes.length > 1) {
	                String jsonData = partes[1];
	                System.out.println("JSON data length: " + jsonData.length());
	                System.out.println("JSON primeros 200 chars: " + 
	                    (jsonData.length() > 200 ? jsonData.substring(0, 200) : jsonData));

	                if (!jsonData.equals("[]")) {
	                    try {
	                        Type listType = new TypeToken<List<LibroDTO>>() {}.getType();
	                        List<LibroDTO> lista = gson.fromJson(jsonData, listType);

	                        if (lista != null) {
	                            System.out.println("=== LIBROS PROCESADOS ===");
	                            for (LibroDTO libro : lista) {
	                                System.out.println("- " + libro.getTitulo() + " por " + libro.getAuthor());
	                                System.out.println("  Imagen Base64: " + 
	                                    (libro.getImagenBase64() != null ? libro.getImagenBase64().length() + " chars" : "NULL"));
	                                System.out.println("  PDF Base64: " + 
	                                    (libro.getPdfBase64() != null ? libro.getPdfBase64().length() + " chars" : "NULL"));
	                            }
	                            
	                            books = lista;
	                            System.out.println("SUCCESS: " + books.size() + " libros cargados");
	                        } else {
	                            books = new ArrayList<>();
	                            System.out.println("Lista nula después de parsing");
	                        }

	                    } catch (JsonSyntaxException e) {
	                        System.err.println("Error parseando JSON: " + e.getMessage());
	                        System.err.println("JSON problemático (primeros 500 chars): " + 
	                            (jsonData.length() > 500 ? jsonData.substring(0, 500) : jsonData));
	                        books = new ArrayList<>();
	                    }
	                } else {
	                    books = new ArrayList<>();
	                    System.out.println("JSON vacío recibido");
	                }
	            } else {
	                books = new ArrayList<>();
	                System.out.println("No hay segunda parte en la respuesta");
	            }
	        } else {
	            books = new ArrayList<>();
	            System.out.println("Respuesta nula o con error");
	        }

	    } catch (Exception e) {
	        System.err.println("Error cargando libros: " + e.getMessage());
	        e.printStackTrace();
	        books = new ArrayList<>();
	    }
	}


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

	            // 🔹 Convertir portada a Base64
	            String coverBase64 = "";
	            if (coverFile != null) {
	                try (InputStream input = coverFile.getInputStream()) {
	                    byte[] bytes = input.readAllBytes();
	                    coverBase64 = Base64.getEncoder().encodeToString(bytes);
	                }
	            }

	            // 🔹 Convertir PDF a Base64
	            String pdfBase64 = "";
	            if (bookFile != null) {
	                try (InputStream input = bookFile.getInputStream()) {
	                    byte[] bytes = input.readAllBytes();
	                    pdfBase64 = Base64.getEncoder().encodeToString(bytes);
	                }
	            }

	            // Crear DTO
	            LibroDTO nuevo = new LibroDTO();
	            nuevo.setTitulo(titulo.trim());
	            nuevo.setAuthor(author.trim());
	            nuevo.setDescripcion(description != null ? description.trim() : "");
	            nuevo.setImagenBase64(coverBase64);
	            nuevo.setPdfBase64(pdfBase64);

	            // Convertir a JSON y enviar al backend
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
	        coverFile = null;
	        bookFile = null;
	    }
	  private LibroDTO libroSeleccionado = new LibroDTO();

	  public void seleccionarLibro(LibroDTO libro) {
	      this.libroSeleccionado = libro;
	      System.out.println("Libro seleccionado: " + libro.getTitulo());
	  }

	  public LibroDTO getLibroSeleccionado() {
	      return libroSeleccionado;
	  }

	  public void setLibroSeleccionado(LibroDTO libroSeleccionado) {
	      this.libroSeleccionado = libroSeleccionado;
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
	
}
