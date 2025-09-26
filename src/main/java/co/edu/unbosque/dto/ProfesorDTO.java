package co.edu.unbosque.dto;

import java.util.Objects;

/**
 * Data Transfer Object (DTO) que representa a un profesor en el sistema.
 * <p>
 * Contiene los datos básicos de un profesor como su usuario, contraseña, nivel
 * de permisos y fecha de nacimiento. Este DTO se utiliza para transferir
 * información entre capas sin exponer directamente la entidad.
 * </p>
 * 
 * @author
 * @version 1.0
 */
public class ProfesorDTO {

	/** Identificador único del profesor. */
	private Long id;

	/** Nombre de usuario del profesor. */
	private String usuario;

	/** Contraseña asociada al usuario. */
	private String contrasenia;

	/** Nivel de permisos del profesor (ejemplo: ADMIN, USER). */
	private String nivelDePermiso;

	/** Fecha de nacimiento del profesor en formato String. */
	private String fechaDeNacimiento;

	/**
	 * Constructor vacío requerido para serialización/deserialización.
	 */
	public ProfesorDTO() {
		// Constructor por defecto
	}

	/**
	 * Constructor con todos los parámetros.
	 *
	 * @param id                identificador único del profesor
	 * @param usuario           nombre de usuario
	 * @param contrasenia       contraseña del usuario
	 * @param nivelDePermiso    nivel de permisos del profesor
	 * @param fechaDeNacimiento fecha de nacimiento del profesor
	 */
	public ProfesorDTO(Long id, String usuario, String contrasenia, String nivelDePermiso, String fechaDeNacimiento) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.contrasenia = contrasenia;
		this.nivelDePermiso = nivelDePermiso;
		this.fechaDeNacimiento = fechaDeNacimiento;
	}

	/**
	 * @return identificador único del profesor
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
	 * @return usuario del profesor
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario usuario a establecer
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return contraseña del profesor
	 */
	public String getContrasenia() {
		return contrasenia;
	}

	/**
	 * @param contrasenia contraseña a establecer
	 */
	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	/**
	 * @return nivel de permisos del profesor
	 */
	public String getNivelDePermiso() {
		return nivelDePermiso;
	}

	/**
	 * @param nivelDePermiso nivel de permisos a establecer
	 */
	public void setNivelDePermiso(String nivelDePermiso) {
		this.nivelDePermiso = nivelDePermiso;
	}

	/**
	 * @return fecha de nacimiento del profesor
	 */
	public String getFechaDeNacimiento() {
		return fechaDeNacimiento;
	}

	/**
	 * @param fechaDeNacimiento fecha de nacimiento a establecer
	 */
	public void setFechaDeNacimiento(String fechaDeNacimiento) {
		this.fechaDeNacimiento = fechaDeNacimiento;
	}

	/**
	 * Genera un código hash basado en los atributos del objeto.
	 * 
	 * @return valor hash calculado
	 */
	@Override
	public int hashCode() {
		return Objects.hash(contrasenia, fechaDeNacimiento, id, nivelDePermiso, usuario);
	}

	/**
	 * Compara si dos objetos ProfesorDTO son iguales.
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
		ProfesorDTO other = (ProfesorDTO) obj;
		return Objects.equals(contrasenia, other.contrasenia)
				&& Objects.equals(fechaDeNacimiento, other.fechaDeNacimiento) && Objects.equals(id, other.id)
				&& Objects.equals(nivelDePermiso, other.nivelDePermiso) && Objects.equals(usuario, other.usuario);
	}

	/**
	 * Devuelve una representación en texto del objeto.
	 * 
	 * @return cadena con los valores de los atributos
	 */
	@Override
	public String toString() {
		return "ProfesorDTO [id=" + id + ", usuario=" + usuario + ", contrasenia=" + contrasenia + ", nivelDePermiso="
				+ nivelDePermiso + ", fechaDeNacimiento=" + fechaDeNacimiento + "]";
	}
}
