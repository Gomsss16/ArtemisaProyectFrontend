package co.edu.unbosque.dto;

import java.time.LocalDate;

public class EventoDTO {

	private Long id;
	private String nombre;
	private LocalDate FechaDelEvento;
	private String link;

	public EventoDTO() {
		// TODO Auto-generated constructor stub
	}

	public EventoDTO(String nombre, LocalDate fechaDelEvento, String link) {
		super();
		this.nombre = nombre;
		FechaDelEvento = fechaDelEvento;
		this.link = link;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public LocalDate getFechaDelEvento() {
		return FechaDelEvento;
	}

	public void setFechaDelEvento(LocalDate fechaDelEvento) {
		FechaDelEvento = fechaDelEvento;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public String toString() {
		return "EventoDTO [id=" + id + ", nombre=" + nombre + ", FechaDelEvento=" + FechaDelEvento + ", link=" + link
				+ "]";
	}

}
