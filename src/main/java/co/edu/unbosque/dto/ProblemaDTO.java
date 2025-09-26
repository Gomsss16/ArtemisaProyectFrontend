package co.edu.unbosque.dto;

/**
 * Data Transfer Object (DTO) que representa un problema dentro del sistema.
 * <p>
 * Esta clase encapsula la información asociada a un problema de programación o
 * ejercicio académico, incluyendo su título, dificultad, tema, juez (plataforma
 * en la que se encuentra) y el enlace correspondiente.
 * </p>
 * 
 * @author
 * @version 1.0
 */
public class ProblemaDTO {

	/** Identificador único del problema. */
	private Long id;

	/** Título o nombre del problema. */
	private String titulo;

	/** Nivel de dificultad del problema (por ejemplo, 1 a 10). */
	private Integer dificultad;

	/** Tema o categoría del problema (ejemplo: matemáticas, grafos, DP). */
	private String tema;

	/** Nombre del juez o plataforma donde se encuentra el problema. */
	private String juez;

	/** Enlace directo al problema en la plataforma correspondiente. */
	private String link;

	/**
	 * Constructor vacío requerido para serialización/deserialización.
	 */
	public ProblemaDTO() {
	}

	/**
	 * Constructor con todos los parámetros.
	 * 
	 * @param id         identificador único del problema
	 * @param titulo     título o nombre del problema
	 * @param dificultad nivel de dificultad
	 * @param tema       tema o categoría del problema
	 * @param juez       juez o plataforma
	 * @param link       enlace al problema
	 */
	public ProblemaDTO(Long id, String titulo, Integer dificultad, String tema, String juez, String link) {
		this.id = id;
		this.titulo = titulo;
		this.dificultad = dificultad;
		this.tema = tema;
		this.juez = juez;
		this.link = link;
	}

	/**
	 * @return identificador único del problema
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
	 * @return título del problema
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
	 * @return dificultad del problema
	 */
	public Integer getDificultad() {
		return dificultad;
	}

	/**
	 * @param dificultad nivel de dificultad a establecer
	 */
	public void setDificultad(Integer dificultad) {
		this.dificultad = dificultad;
	}

	/**
	 * @return tema del problema
	 */
	public String getTema() {
		return tema;
	}

	/**
	 * @param tema tema a establecer
	 */
	public void setTema(String tema) {
		this.tema = tema;
	}

	/**
	 * @return juez o plataforma
	 */
	public String getJuez() {
		return juez;
	}

	/**
	 * @param juez juez o plataforma a establecer
	 */
	public void setJuez(String juez) {
		this.juez = juez;
	}

	/**
	 * @return enlace al problema
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @param link enlace a establecer
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * Devuelve una representación en texto del objeto.
	 * 
	 * @return cadena con los valores de los atributos
	 */
	@Override
	public String toString() {
		return "ProblemaDTO{" + "id=" + id + ", titulo='" + titulo + '\'' + ", dificultad=" + dificultad + ", tema='"
				+ tema + '\'' + ", juez='" + juez + '\'' + ", link='" + link + '\'' + '}';
	}
}
