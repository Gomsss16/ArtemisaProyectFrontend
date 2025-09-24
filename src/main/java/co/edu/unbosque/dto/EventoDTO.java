package co.edu.unbosque.dto;

import java.util.Date;

public class EventoDTO {

    private Long id;
    private String titulo;
    private String descripcion;
    private String tipo;
    private Date fecha;
    private String enlace;
    private String ubicacion;

    public EventoDTO() {}

    public EventoDTO(String titulo, String descripcion, String tipo, Date fecha, String enlace, String ubicacion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.fecha = fecha;
        this.enlace = enlace;
        this.ubicacion = ubicacion;
    }

    public EventoDTO(Long id, String titulo, String descripcion, String tipo, Date fecha, String enlace, String ubicacion) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.fecha = fecha;
        this.enlace = enlace;
        this.ubicacion = ubicacion;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public String getEnlace() { return enlace; }
    public void setEnlace(String enlace) { this.enlace = enlace; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    // Métodos de compatibilidad
    public String getTitle() { return titulo; }
    public void setTitle(String title) { this.titulo = title; }

    public String getDescription() { return descripcion; }
    public void setDescription(String description) { this.descripcion = description; }

    @Override
    public String toString() {
        return "EventoDTO [id=" + id + ", titulo=" + titulo + ", descripcion=" + descripcion + 
               ", tipo=" + tipo + ", fecha=" + fecha + ", enlace=" + enlace + ", ubicacion=" + ubicacion + "]";
    }
}
