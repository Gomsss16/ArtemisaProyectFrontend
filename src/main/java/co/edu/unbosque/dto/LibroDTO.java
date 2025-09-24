package co.edu.unbosque.dto;

import java.awt.Image;
import java.io.File;
import java.util.Objects;

public class LibroDTO {

	private Long id;
	private String titulo;
	private String author;
	private String descripcion;
	private String imagenBase64;
	private String pdfBase64;

	public LibroDTO() {
	}

	public LibroDTO(Long id, String titulo, String author, String descripcion, String imagenBase64, String pdfBase64) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.author = author;
		this.descripcion = descripcion;
		this.imagenBase64 = imagenBase64;
		this.pdfBase64 = pdfBase64;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getImagenBase64() {
		return imagenBase64;
	}

	public void setImagenBase64(String imagenBase64) {
		this.imagenBase64 = imagenBase64;
	}

	public String getPdfBase64() {
		return pdfBase64;
	}

	public void setPdfBase64(String pdfBase64) {
		this.pdfBase64 = pdfBase64;
	}

	@Override
	public int hashCode() {
		return Objects.hash(author, descripcion, id, imagenBase64, pdfBase64, titulo);
	}

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
				&& Objects.equals(id, other.id) && Objects.equals(imagenBase64, other.imagenBase64)
				&& Objects.equals(pdfBase64, other.pdfBase64) && Objects.equals(titulo, other.titulo);
	}

	@Override
	public String toString() {
		return "LibroDTO [id=" + id + ", titulo=" + titulo + ", author=" + author + ", descripcion=" + descripcion
				+ ", imagenBase64=" + imagenBase64 + ", pdfBase64=" + pdfBase64 + "]";
	}

	
}
