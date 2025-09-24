package co.edu.unbosque.dto;

public class ProblemaDTO {
    
    private Long id;
    private String titulo;
    private Integer dificultad;
    private String tema;
    private String juez;
    private String link;
    
    public ProblemaDTO() {}
    
    public ProblemaDTO(Long id, String titulo, Integer dificultad, String tema, String juez, String link) {
        this.id = id;
        this.titulo = titulo;
        this.dificultad = dificultad;
        this.tema = tema;
        this.juez = juez;
        this.link = link;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public Integer getDificultad() { return dificultad; }
    public void setDificultad(Integer dificultad) { this.dificultad = dificultad; }
    
    public String getTema() { return tema; }
    public void setTema(String tema) { this.tema = tema; }
    
    public String getJuez() { return juez; }
    public void setJuez(String juez) { this.juez = juez; }
    
    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }
    
    @Override
    public String toString() {
        return "ProblemaDTO{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", dificultad=" + dificultad +
                ", tema='" + tema + '\'' +
                ", juez='" + juez + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
