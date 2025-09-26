package co.edu.unbosque.dto;

import java.util.Objects;

/**
 * Data Transfer Object (DTO) que representa a un Administrador dentro del
 * sistema.
 * <p>
 * Esta clase encapsula los datos básicos de un administrador, tales como su
 * identificador, nombre de usuario, contraseña, nivel de permiso y fecha de
 * nacimiento. Es utilizada para transportar datos entre las capas de la
 * aplicación sin exponer la lógica interna.
 * </p>
 * 
 * @author
 * @version 1.0
 */
public class AdministradorDTO {

	/** Identificador único del administrador. */
	private Long id;

	/** Nombre de usuario del administrador. */
	private String usuario;

	/** Contraseña del administrador. */
	private String contrasenia;

	/**
	 * Nivel de permisos asignado al administrador (ejemplo: "ADMIN", "SUPERADMIN").
	 */
	private String nivelDePermiso;

	/**
	 * Fecha de nacimiento del administrador en formato {@code String} (ejemplo:
	 * "1990-05-20").
	 */
	private String fechaDeNacimiento;

	/**
	 * Constructor vacío requerido para la serialización/deserialización.
	 */
	public AdministradorDTO() {
	}

	/**
	 * Constructor completo.
	 * 
	 * @param id                identificador único del administrador
	 * @param usuario           nombre de usuario
	 * @param contrasenia       contraseña del administrador
	 * @param nivelDePermiso    nivel de permiso asignado
	 * @param fechaDeNacimiento fecha de nacimiento en formato {@code String}
	 */
	public AdministradorDTO(Long id, String usuario, String contrasenia, String nivelDePermiso,
			String fechaDeNacimiento) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.contrasenia = contrasenia;
		this.nivelDePermiso = nivelDePermiso;
		this.fechaDeNacimiento = fechaDeNacimiento;
	}

	/**
	 * Calcula el valor hash basado en todos los atributos.
	 * 
	 * @return valor hash del objeto
	 */
	@Override
	public int hashCode() {
		return Objects.hash(contrasenia, fechaDeNacimiento, id, nivelDePermiso, usuario);
	}

	/**
	 * Compara si dos objetos {@link AdministradorDTO} son iguales en base a sus
	 * atributos.
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
		AdministradorDTO other = (AdministradorDTO) obj;
		return Objects.equals(contrasenia, other.contrasenia)
				&& Objects.equals(fechaDeNacimiento, other.fechaDeNacimiento) && Objects.equals(id, other.id)
				&& Objects.equals(nivelDePermiso, other.nivelDePermiso) && Objects.equals(usuario, other.usuario);
	}

	/**
	 * Representación en texto del objeto.
	 * 
	 * @return cadena con los valores de los atributos del administrador
	 */
	@Override
	public String toString() {
		return "AdministradorDTO [id=" + id + ", usuario=" + usuario + ", contrasenia=" + contrasenia
				+ ", nivelDePermiso=" + nivelDePermiso + ", fechaDeNacimiento=" + fechaDeNacimiento + "]";
	}

	/**
	 * @return identificador único del administrador
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
	 * @return nombre de usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario nombre de usuario a establecer
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return contraseña del administrador
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
	 * @return nivel de permisos del administrador
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
	 * @return fecha de nacimiento en formato {@code String}
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
}
