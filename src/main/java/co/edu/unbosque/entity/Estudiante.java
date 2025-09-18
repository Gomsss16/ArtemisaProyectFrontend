package co.edu.unbosque.entity;




public class Estudiante extends Usuario {
	
	public Estudiante() {
		// TODO Auto-generated constructor stub
	}

	public Estudiante(Long id, String usuario, String contrasenia, String nivelDePermiso, String fechaDeNacimiento) {
		super(id, usuario, contrasenia, nivelDePermiso, fechaDeNacimiento);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Estudiante []";
	}
	
	
	

	
	

}
