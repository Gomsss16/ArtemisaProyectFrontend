package co.edu.unbosque.dto;

import java.util.Date;

/**
 * Data Transfer Object (DTO) que representa un evento dentro del sistema.
 * <p>
 * Esta clase encapsula la información básica de un evento, como su
 * identificador, título, descripción, tipo, fecha, enlace y ubicación.
 * Se utiliza para transportar datos entre las capas de la aplicación.
 * </p>
 * 
 * @author  
 * @version 1.0
 */
public class EventoDTO {

    /** Identificador único del evento. */
    private Long id;

    /** Título del evento. */
    private String titulo;

    /** Descripción detallada del evento. */
    private String descripcion;

    /** Tipo del evento (ejemplo: "Conferencia", "Taller", "Seminario"). */
    private String tipo;

    /** Fecha en la que se realiza el evento. */
    private Date fecha;

    /** Enlace relacionado con el evento (ejemplo: URL de reunión o streaming). */
    private String enlace;

    /** Ubicación física o virtual del evento. */
    private String ubicacion;

    /**
     * Constructor vacío requerido para serialización/deserialización.
     */
    public EventoDTO() {
    }

    /**
     * Constructor parcial.
     * 
     * @param titulo      título del evento
     * @param descripcion descripción del evento
     * @param tipo        tipo del evento
     * @param fecha       fecha del evento
     * @param enlace      enlace del evento
     * @param ubicacion   ubicación del evento
     */
    public EventoDTO(String titulo, String descripcion, String tipo, Date fecha, String enlace, String ubicacion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.fecha = fecha;
        this.enlace = enlace;
        this.ubicacion = ubicacion;
    }

    /**
     * Constructor completo.
     * 
     * @param id          identificador único
     * @param titulo      título del evento
     * @param descripcion descripción del evento
     * @param tipo        tipo del evento
     * @param fecha       fecha del evento
     * @param enlace      enlace del evento
     * @param ubicacion   ubicación del evento
     */
    public EventoDTO(Long id, String titulo, String descripcion, String tipo, Date fecha, String enlace,
            String ubicacion) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.fecha = fecha;
        this.enlace = enlace;
        this.ubicacion = ubicacion;
    }

    /**
     * @return identificador único del evento
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
     * @return título del evento
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
     * @return descripción del evento
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion descripción a establecer
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return tipo del evento
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo tipo de evento a establecer
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return fecha del evento
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha fecha a establecer
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @return enlace del evento
     */
    public String getEnlace() {
        return enlace;
    }

    /**
     * @param enlace enlace a establecer
     */
    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }

    /**
     * @return ubicación del evento
     */
    public String getUbicacion() {
        return ubicacion;
    }

    /**
     * @param ubicacion ubicación a establecer
     */
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    // Métodos de compatibilidad (alias para otros frameworks/librerías)

    /**
     * @return título del evento (alias de {@link #getTitulo()})
     */
    public String getTitle() {
        return titulo;
    }

    /**
     * @param title título del evento a establecer (alias de {@link #setTitulo(String)})
     */
    public void setTitle(String title) {
        this.titulo = title;
    }

    /**
     * @return descripción del evento (alias de {@link #getDescripcion()})
     */
    public String getDescription() {
        return descripcion;
    }

    /**
     * @param description descripción del evento a establecer (alias de {@link #setDescripcion(String)})
     */
    public void setDescription(String description) {
        this.descripcion = description;
    }

    /**
     * Representación en texto del objeto.
     * 
     * @return cadena con los valores de los atributos del evento
     */
    @Override
    public String toString() {
        return "EventoDTO [id=" + id + ", titulo=" + titulo + ", descripcion=" + descripcion + ", tipo=" + tipo
                + ", fecha=" + fecha + ", enlace=" + enlace + ", ubicacion=" + ubicacion + "]";
    }
}
