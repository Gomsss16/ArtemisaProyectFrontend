package co.edu.unbosque.dto;

import java.util.Arrays;
import java.util.Objects;

public class LinkDTO {

	private Long id;
	private String titulo;
	private String descripcion;
	private String enlace;
	private byte[] imagen;
	private String imagenBase64;

	public LinkDTO() {
	}

	public LinkDTO(String titulo, String descripcion, String enlace, byte[] imagen) {
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.enlace = enlace;
		this.imagen = imagen;
	}

	// --- Getters y Setters ---
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

	public byte[] getImagen() {
		return imagen;
	}

	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}

	public String getImagenBase64() {
		return imagenBase64;
	}

	public void setImagenBase64(String imagenBase64) {
		this.imagenBase64 = imagenBase64;
	}

	@Override
	public String toString() {
		return "LinkDTO [id=" + id + ", titulo=" + titulo + ", descripcion=" + descripcion + ", enlace=" + enlace
				+ ", imagen=" + Arrays.toString(imagen) + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(descripcion, enlace, id, titulo) + Arrays.hashCode(imagen);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof LinkDTO))
			return false;
		LinkDTO other = (LinkDTO) obj;
		return Objects.equals(descripcion, other.descripcion) && Objects.equals(enlace, other.enlace)
				&& Objects.equals(id, other.id) && Arrays.equals(imagen, other.imagen)
				&& Objects.equals(titulo, other.titulo);
	}
}
