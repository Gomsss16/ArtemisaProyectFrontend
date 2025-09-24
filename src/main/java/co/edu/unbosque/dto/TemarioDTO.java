// co.edu.unbosque.dto.TemarioDTO.java (FRONTEND)
package co.edu.unbosque.dto;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;

public class TemarioDTO {

    @SerializedName("id")
    private Long id;
    
    @SerializedName("temaAlgoritmo")  // Mapea exactamente como en el backend
    private String temaAlgoritmo;
    
    @SerializedName("tipo")
    private String tipo;
    
    @SerializedName("contenido")
    private String contenido;
    
    @SerializedName("codigo")
    private String codigo;
    
    public TemarioDTO(Long id, String temaAlgoritmo, String tipo, String contenido, String codigo) {
        super();
        this.id = id;
        this.temaAlgoritmo = temaAlgoritmo;
        this.tipo = tipo;
        this.contenido = contenido;
        this.codigo = codigo;
    }

    public TemarioDTO() {
        // Constructor vacío
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo, contenido, id, temaAlgoritmo, tipo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TemarioDTO other = (TemarioDTO) obj;
        return Objects.equals(codigo, other.codigo) && Objects.equals(contenido, other.contenido)
                && Objects.equals(id, other.id) && Objects.equals(temaAlgoritmo, other.temaAlgoritmo)
                && Objects.equals(tipo, other.tipo);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemaAlgoritmo() {
        return temaAlgoritmo;
    }

    public void setTemaAlgoritmo(String temaAlgoritmo) {
        this.temaAlgoritmo = temaAlgoritmo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Override
    public String toString() {
        return "TemarioDTO [id=" + id + ", temaAlgoritmo=" + temaAlgoritmo + ", tipo=" + tipo + ", contenido="
                + contenido + ", codigo=" + codigo + "]";
    }
}
