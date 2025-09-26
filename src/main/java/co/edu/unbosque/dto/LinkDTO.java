package co.edu.unbosque.dto;

import java.util.Arrays;
import java.util.Objects;

/**
 * Data Transfer Object (DTO) que representa un enlace dentro del sistema.
 * <p>
 * Esta clase se utiliza para transportar información de recursos externos
 * (links) que pueden incluir título, descripción, enlace y una imagen
 * representativa tanto en formato binario como en base64.
 * </p>
 * 
 * @author
 * @version 1.0
 */
public class LinkDTO {

	/** Identificador único del enlace. */
	private Long id;

	/** Título descriptivo del enlace. */
	private String titulo;

	/** Descripción breve del enlace. */
	private String descripcion;

	/** URL o dirección del recurso externo. */
	private String enlace;

	/** Imagen asociada en formato de bytes. */
	private byte[] imagen;

	/** Imagen asociada en formato Base64 (útil para transmisión en JSON). */
	private String imagenBase64;

	/**
	 * Constructor vacío requerido para serialización/deserialización.
	 */
	public LinkDTO() {
	}

	/**
	 * Constructor con parámetros (sin Base64).
	 * 
	 * @param titulo      título del enlace
	 * @param descripcion descripción breve
	 * @param enlace      URL del recurso
	 * @param imagen      imagen en formato de bytes
	 */
	public LinkDTO(String titulo, String descripcion, String enlace, byte[] imagen) {
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.enlace = enlace;
		this.imagen = imagen;
	}

	/**
	 * @return identificador único del enlace
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
	 * @return título del enlace
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * @param titulo título a establecer
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	/**
	 * @return descripción del enlace
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
	 * @return URL o dirección del enlace
	 */
	public String getEnlace() {
		return enlace;
	}

	/**
	 * @param enlace URL a establecer
	 */
	public void setEnlace(String enlace) {
		this.enlace = enlace;
	}

	/**
	 * @return imagen en formato de bytes
	 */
	public byte[] getImagen() {
		return imagen;
	}

	/**
	 * @param imagen imagen en formato de bytes a establecer
	 */
	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
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
	 * Devuelve una representación en texto del objeto.
	 * 
	 * @return cadena con los valores de los atributos
	 */
	@Override
	public String toString() {
		return "LinkDTO [id=" + id + ", titulo=" + titulo + ", descripcion=" + descripcion + ", enlace=" + enlace
				+ ", imagen=" + Arrays.toString(imagen) + "]";
	}

	/**
	 * Calcula el código hash del objeto en función de sus atributos.
	 * 
	 * @return código hash calculado
	 */
	@Override
	public int hashCode() {
		return Objects.hash(descripcion, enlace, id, titulo) + Arrays.hashCode(imagen);
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
		if (!(obj instanceof LinkDTO))
			return false;
		LinkDTO other = (LinkDTO) obj;
		return Objects.equals(descripcion, other.descripcion) && Objects.equals(enlace, other.enlace)
				&& Objects.equals(id, other.id) && Arrays.equals(imagen, other.imagen)
				&& Objects.equals(titulo, other.titulo);
	}
}
