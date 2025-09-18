package co.edu.unbosque.entity;




public class Profesor extends Usuario{

	public Profesor() {
		// TODO Auto-generated constructor stub
	}

	public Profesor(Long id, String usuario, String contrasenia, String nivelDePermiso, String fechaDeNacimiento) {
		super(id, usuario, contrasenia, nivelDePermiso, fechaDeNacimiento);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Profesor []";
	}
	
	

	


	
}
