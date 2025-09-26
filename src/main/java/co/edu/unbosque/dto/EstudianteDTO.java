package co.edu.unbosque.dto;

import java.util.Objects;

/**
 * Data Transfer Object (DTO) que representa a un Estudiante dentro del sistema.
 * <p>
 * Esta clase encapsula los datos básicos de un estudiante, tales como su
 * identificador, nombre de usuario, contraseña, nivel de permiso y fecha de
 * nacimiento. Se utiliza para transportar datos entre las capas de la
 * aplicación sin exponer la lógica interna.
 * </p>
 * 
 * @author
 * @version 1.0
 */
public class EstudianteDTO {

	/** Identificador único del estudiante. */
	private Long id;

	/** Nombre de usuario del estudiante. */
	private String usuario;

	/** Contraseña del estudiante. */
	private String contrasenia;

	/** Nivel de permisos asignado al estudiante (ejemplo: "Estudiante"). */
	private String nivelDePermiso;

	/**
	 * Fecha de nacimiento del estudiante en formato {@code String} (ejemplo:
	 * "2000-08-15").
	 */
	private String fechaDeNacimiento;

	/**
	 * Constructor vacío requerido para la serialización/deserialización.
	 */
	public EstudianteDTO() {
		// Constructor por defecto
	}

	/**
	 * Constructor completo.
	 * 
	 * @param id                identificador único del estudiante
	 * @param usuario           nombre de usuario
	 * @param contrasenia       contraseña del estudiante
	 * @param nivelDePermiso    nivel de permiso asignado
	 * @param fechaDeNacimiento fecha de nacimiento en formato {@code String}
	 */
	public EstudianteDTO(Long id, String usuario, String contrasenia, String nivelDePermiso, String fechaDeNacimiento) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.contrasenia = contrasenia;
		this.nivelDePermiso = nivelDePermiso;
		this.fechaDeNacimiento = fechaDeNacimiento;
	}

	/**
	 * @return identificador único del estudiante
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
	 * @return contraseña del estudiante
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
	 * @return nivel de permisos del estudiante
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
	 * Compara si dos objetos {@link EstudianteDTO} son iguales en base a sus
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
		EstudianteDTO other = (EstudianteDTO) obj;
		return Objects.equals(contrasenia, other.contrasenia)
				&& Objects.equals(fechaDeNacimiento, other.fechaDeNacimiento) && Objects.equals(id, other.id)
				&& Objects.equals(nivelDePermiso, other.nivelDePermiso) && Objects.equals(usuario, other.usuario);
	}

	/**
	 * Representación en texto del objeto.
	 * 
	 * @return cadena con los valores de los atributos del estudiante
	 */
	@Override
	public String toString() {
		return "EstudianteDTO [id=" + id + ", usuario=" + usuario + ", contrasenia=" + contrasenia + ", nivelDePermiso="
				+ nivelDePermiso + ", fechaDeNacimiento=" + fechaDeNacimiento + "]";
	}
}
