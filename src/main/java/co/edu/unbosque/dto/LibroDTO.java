package co.edu.unbosque.dto;

import java.awt.Image;
import java.io.File;

public class LibroDTO {

	private Long id;
	private String titulo;
	private String author;
	private String descricpcion;
	private Image portadaDelLibro;
	private File libroPDF;

	public LibroDTO(String titulo, String author, String descricpcion, Image portadaDelLibro, File libroPDF) {
		super();
		this.titulo = titulo;
		this.author = author;
		this.descricpcion = descricpcion;
		this.portadaDelLibro = portadaDelLibro;
		this.libroPDF = libroPDF;
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

	public String getDescricpcion() {
		return descricpcion;
	}

	public void setDescricpcion(String descricpcion) {
		this.descricpcion = descricpcion;
	}

	public Image getPortadaDelLibro() {
		return portadaDelLibro;
	}

	public void setPortadaDelLibro(Image portadaDelLibro) {
		this.portadaDelLibro = portadaDelLibro;
	}

	public File getLibroPDF() {
		return libroPDF;
	}

	public void setLibroPDF(File libroPDF) {
		this.libroPDF = libroPDF;
	}

	@Override
	public String toString() {
		return "LibroDTO [id=" + id + ", titulo=" + titulo + ", author=" + author + ", descricpcion=" + descricpcion
				+ ", portadaDelLibro=" + portadaDelLibro + ", libroPDF=" + libroPDF + "]";
	}

}
