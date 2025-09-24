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
	private String coverUrl;
	private String bookUrl;
	private String onlineUrl;

	public LibroDTO() {
	}

	public LibroDTO(String titulo, String author, String descricpcion, Image portadaDelLibro, File libroPDF) {
		super();
		this.titulo = titulo;
		this.author = author;
		this.descricpcion = descricpcion;
		this.portadaDelLibro = portadaDelLibro;
		this.libroPDF = libroPDF;
	}

	public LibroDTO(Long id, String titulo, String author, String descricpcion, Image portadaDelLibro, File libroPDF,
			String coverUrl, String bookUrl, String onlineUrl) {
		this.id = id;
		this.titulo = titulo;
		this.author = author;
		this.descricpcion = descricpcion;
		this.portadaDelLibro = portadaDelLibro;
		this.libroPDF = libroPDF;
		this.coverUrl = coverUrl;
		this.bookUrl = bookUrl;
		this.onlineUrl = onlineUrl;
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

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public String getBookUrl() {
		return bookUrl;
	}

	public void setBookUrl(String bookUrl) {
		this.bookUrl = bookUrl;
	}

	public String getOnlineUrl() {
		return onlineUrl;
	}

	public void setOnlineUrl(String onlineUrl) {
		this.onlineUrl = onlineUrl;
	}

	public String getTitle() {
		return titulo;
	}

	public void setTitle(String title) {
		this.titulo = title;
	}

	public String getDescription() {
		return descricpcion;
	}

	public void setDescription(String description) {
		this.descricpcion = description;
	}

	public String getSubtitle() {
		return author;
	}

	public void setSubtitle(String subtitle) {
		this.author = subtitle;
	}

	public String getAutor() {
		return author;
	}

	public void setAutor(String autor) {
		this.author = autor;
	}

	public String getDescripcion() {
		return descricpcion;
	}

	public void setDescripcion(String descripcion) {
		this.descricpcion = descripcion;
	}

	@Override
	public String toString() {
		return "LibroDTO [id=" + id + ", titulo=" + titulo + ", author=" + author + ", descricpcion=" + descricpcion
				+ ", portadaDelLibro=" + portadaDelLibro + ", libroPDF=" + libroPDF + ", coverUrl=" + coverUrl
				+ ", bookUrl=" + bookUrl + ", onlineUrl=" + onlineUrl + "]";
	}
}
