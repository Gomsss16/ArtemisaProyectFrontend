package co.edu.unbosque.dto;

public class LinkDTO {

	private Long id;
	private String titulo;
	private String descripcion;
	private String enlace;
	private String imagenUrl;

	public LinkDTO() {
	}

	public LinkDTO(String titulo, String descripcion, String enlace, String imagenUrl) {
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.enlace = enlace;
		this.imagenUrl = imagenUrl;
	}

	public LinkDTO(Long id, String titulo, String descripcion, String enlace, String imagenUrl) {
		this.id = id;
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.enlace = enlace;
		this.imagenUrl = imagenUrl;
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEnlace() {
		return enlace;
	}

	public void setEnlace(String enlace) {
		this.enlace = enlace;
	}

	public String getImagenUrl() {
		return imagenUrl;
	}

	public void setImagenUrl(String imagenUrl) {
		this.imagenUrl = imagenUrl;
	}

	// Métodos de compatibilidad
	public String getTitle() {
		return titulo;
	}

	public void setTitle(String title) {
		this.titulo = title;
	}

	public String getDescription() {
		return descripcion;
	}

	public void setDescription(String description) {
		this.descripcion = description;
	}

	public String getCoverUrl() {
		return imagenUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.imagenUrl = coverUrl;
	}

	@Override
	public String toString() {
		return "LinkDTO [id=" + id + ", titulo=" + titulo + ", descripcion=" + descripcion + ", enlace=" + enlace
				+ ", imagenUrl=" + imagenUrl + "]";
	}
}
