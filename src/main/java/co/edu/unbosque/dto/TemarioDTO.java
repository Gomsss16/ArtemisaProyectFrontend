// co.edu.unbosque.dto.TemarioDTO.java (FRONTEND)
package co.edu.unbosque.dto;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;

/**
 * Data Transfer Object (DTO) que representa un temario dentro del sistema.
 * <p>
 * Contiene información sobre un tema de algoritmos, su tipo, contenido y un
 * posible fragmento de código asociado. Se utiliza para la comunicación entre
 * frontend y backend, asegurando que las propiedades coincidan gracias a las
 * anotaciones {@link SerializedName}.
 * </p>
 * 
 * @author
 * @version 1.0
 */
public class TemarioDTO {

	/** Identificador único del temario. */
	@SerializedName("id")
	private Long id;

	/** Nombre del tema de algoritmo. */
	@SerializedName("temaAlgoritmo")
	private String temaAlgoritmo;

	/** Tipo de recurso o categoría del temario. */
	@SerializedName("tipo")
	private String tipo;

	/** Contenido descriptivo o explicativo del temario. */
	@SerializedName("contenido")
	private String contenido;

	/** Fragmento de código relacionado con el temario. */
	@SerializedName("codigo")
	private String codigo;

	/**
	 * Constructor completo con parámetros.
	 *
	 * @param id            identificador único del temario
	 * @param temaAlgoritmo nombre del tema de algoritmo
	 * @param tipo          tipo o categoría del temario
	 * @param contenido     contenido descriptivo
	 * @param codigo        código relacionado con el temario
	 */
	public TemarioDTO(Long id, String temaAlgoritmo, String tipo, String contenido, String codigo) {
		super();
		this.id = id;
		this.temaAlgoritmo = temaAlgoritmo;
		this.tipo = tipo;
		this.contenido = contenido;
		this.codigo = codigo;
	}

	/**
	 * Constructor vacío requerido para serialización/deserialización.
	 */
	public TemarioDTO() {
		// Constructor por defecto
	}

	/**
	 * Genera un valor hash basado en los atributos del objeto.
	 *
	 * @return valor hash calculado
	 */
	@Override
	public int hashCode() {
		return Objects.hash(codigo, contenido, id, temaAlgoritmo, tipo);
	}

	/**
	 * Compara si dos objetos {@code TemarioDTO} son iguales.
	 *
	 * @param obj objeto a comparar
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
		TemarioDTO other = (TemarioDTO) obj;
		return Objects.equals(codigo, other.codigo) && Objects.equals(contenido, other.contenido)
				&& Objects.equals(id, other.id) && Objects.equals(temaAlgoritmo, other.temaAlgoritmo)
				&& Objects.equals(tipo, other.tipo);
	}

	/**
	 * @return identificador único del temario
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
	 * @return tema de algoritmo
	 */
	public String getTemaAlgoritmo() {
		return temaAlgoritmo;
	}

	/**
	 * @param temaAlgoritmo tema de algoritmo a establecer
	 */
	public void setTemaAlgoritmo(String temaAlgoritmo) {
		this.temaAlgoritmo = temaAlgoritmo;
	}

	/**
	 * @return tipo del temario
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo tipo del temario a establecer
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return contenido del temario
	 */
	public String getContenido() {
		return contenido;
	}

	/**
	 * @param contenido contenido del temario a establecer
	 */
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	/**
	 * @return fragmento de código asociado al temario
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo fragmento de código a establecer
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * Devuelve una representación en texto del objeto.
	 *
	 * @return cadena con los valores de los atributos
	 */
	@Override
	public String toString() {
		return "TemarioDTO [id=" + id + ", temaAlgoritmo=" + temaAlgoritmo + ", tipo=" + tipo + ", contenido="
				+ contenido + ", codigo=" + codigo + "]";
	}
}
