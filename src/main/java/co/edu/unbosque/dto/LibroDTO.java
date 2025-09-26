package co.edu.unbosque.dto;

import java.util.Objects;

/**
 * Data Transfer Object (DTO) que representa un libro dentro del sistema.
 * <p>
 * Esta clase se utiliza para transportar datos de los libros entre las capas de
 * la aplicación. Incluye información básica como título, autor, descripción,
 * enlace, e información en base64 para imágenes y archivos PDF asociados.
 * </p>
 * 
 * @author
 * @version 1.0
 */
public class LibroDTO {

	/** Identificador único del libro. */
	private Long id;

	/** Título del libro. */
	private String titulo;

	/** Autor del libro. */
	private String author;

	/** Descripción breve del contenido del libro. */
	private String descripcion;

	/** Enlace relacionado con el libro (ejemplo: recurso externo). */
	private String enlace;

	/** Imagen en formato Base64 asociada al libro (ejemplo: portada). */
	private String imagenBase64;

	/** Archivo PDF en formato Base64 asociado al libro. */
	private String pdfBase64;

	/**
	 * Constructor vacío requerido para serialización/deserialización.
	 */
	public LibroDTO() {
	}

	/**
	 * Constructor completo.
	 * 
	 * @param id           identificador único
	 * @param titulo       título del libro
	 * @param author       autor del libro
	 * @param descripcion  descripción breve
	 * @param enlace       enlace relacionado
	 * @param imagenBase64 imagen en formato Base64
	 * @param pdfBase64    archivo PDF en formato Base64
	 */
	public LibroDTO(Long id, String titulo, String author, String descripcion, String enlace, String imagenBase64,
			String pdfBase64) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.author = author;
		this.descripcion = descripcion;
		this.enlace = enlace;
		this.imagenBase64 = imagenBase64;
		this.pdfBase64 = pdfBase64;
	}

	/**
	 * @return identificador único del libro
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id identificador único a establecer
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return título del libro
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * @param titulo título del libro a establecer
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	/**
	 * @return autor del libro
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author autor del libro a establecer
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return descripción del libro
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion descripción a establecer
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return enlace relacionado con el libro
	 */
	public String getEnlace() {
		return enlace;
	}

	/**
	 * @param enlace enlace a establecer
	 */
	public void setEnlace(String enlace) {
		this.enlace = enlace;
	}

	/**
	 * @return imagen en formato Base64
	 */
	public String getImagenBase64() {
		return imagenBase64;
	}

	/**
	 * @param imagenBase64 imagen en formato Base64 a establecer
	 */
	public void setImagenBase64(String imagenBase64) {
		this.imagenBase64 = imagenBase64;
	}

	/**
	 * @return archivo PDF en formato Base64
	 */
	public String getPdfBase64() {
		return pdfBase64;
	}

	/**
	 * @param pdfBase64 archivo PDF en formato Base64 a establecer
	 */
	public void setPdfBase64(String pdfBase64) {
		this.pdfBase64 = pdfBase64;
	}

	/**
	 * Calcula el código hash del objeto en función de sus atributos.
	 * 
	 * @return código hash calculado
	 */
	@Override
	public int hashCode() {
		return Objects.hash(author, descripcion, enlace, id, imagenBase64, pdfBase64, titulo);
	}

	/**
	 * Compara este objeto con otro para determinar igualdad lógica.
	 * 
	 * @param obj objeto con el que se va a comparar
	 * @return {@code true} si los objetos son iguales, {@code false} en caso
	 *         contrario
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LibroDTO other = (LibroDTO) obj;
		return Objects.equals(author, other.author) && Objects.equals(descripcion, other.descripcion)
				&& Objects.equals(enlace, other.enlace) && Objects.equals(id, other.id)
				&& Objects.equals(imagenBase64, other.imagenBase64) && Objects.equals(pdfBase64, other.pdfBase64)
				&& Objects.equals(titulo, other.titulo);
	}

	/**
	 * Representación en cadena de texto del objeto.
	 * 
	 * @return cadena con los valores de los atributos
	 */
	@Override
	public String toString() {
		return "LibroDTO [id=" + id + ", titulo=" + titulo + ", author=" + author + ", descripcion=" + descripcion
				+ ", enlace=" + enlace + ", imagenBase64=" + imagenBase64 + ", pdfBase64=" + pdfBase64 + "]";
	}
}
