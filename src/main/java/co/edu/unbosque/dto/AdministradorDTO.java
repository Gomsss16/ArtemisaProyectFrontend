package co.edu.unbosque.dto;

import java.util.Objects;


public class AdministradorDTO {

	
	private Long id;
	private String usuario; 
	private String contrasenia;
	private String nivelDePermiso;
	private String fechaDeNacimiento;
	
	
	
	public AdministradorDTO() {
		
	}



	public AdministradorDTO(Long id, String usuario, String contrasenia, String nivelDePermiso,
			String fechaDeNacimiento) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.contrasenia = contrasenia;
		this.nivelDePermiso = nivelDePermiso;
		this.fechaDeNacimiento = fechaDeNacimiento;
	}



	@Override
	public int hashCode() {
		return Objects.hash(contrasenia, fechaDeNacimiento, id, nivelDePermiso, usuario);
	}



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



	@Override
	public String toString() {
		return "AdministradorDTO [id=" + id + ", usuario=" + usuario + ", contrasenia=" + contrasenia
				+ ", nivelDePermiso=" + nivelDePermiso + ", fechaDeNacimiento=" + fechaDeNacimiento + "]";
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getUsuario() {
		return usuario;
	}



	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}



	public String getContrasenia() {
		return contrasenia;
	}



	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}



	public String getNivelDePermiso() {
		return nivelDePermiso;
	}



	public void setNivelDePermiso(String nivelDePermiso) {
		this.nivelDePermiso = nivelDePermiso;
	}



	public String getFechaDeNacimiento() {
		return fechaDeNacimiento;
	}



	public void setFechaDeNacimiento(String fechaDeNacimiento) {
		this.fechaDeNacimiento = fechaDeNacimiento;
	}
	
	
	



}
