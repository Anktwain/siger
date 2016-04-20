package dto;
// Generated 29/03/2016 04:35:49 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * DescripcionGestion generated by hbm2java
 */
public class DescripcionGestion  implements java.io.Serializable {


     private Integer idDescripcionGestion;
     private AsuntoGestion asuntoGestion;
     private String abreviatura;
     private String textoGestion;
     private Integer calificacion;
     private Set gestions = new HashSet(0);

    public DescripcionGestion() {
    }

	
    public DescripcionGestion(AsuntoGestion asuntoGestion, String textoGestion) {
        this.asuntoGestion = asuntoGestion;
        this.textoGestion = textoGestion;
    }
    public DescripcionGestion(AsuntoGestion asuntoGestion, String abreviatura, String textoGestion, Integer calificacion, Set gestions) {
       this.asuntoGestion = asuntoGestion;
       this.abreviatura = abreviatura;
       this.textoGestion = textoGestion;
       this.calificacion = calificacion;
       this.gestions = gestions;
    }
   
    public Integer getIdDescripcionGestion() {
        return this.idDescripcionGestion;
    }
    
    public void setIdDescripcionGestion(Integer idDescripcionGestion) {
        this.idDescripcionGestion = idDescripcionGestion;
    }
    public AsuntoGestion getAsuntoGestion() {
        return this.asuntoGestion;
    }
    
    public void setAsuntoGestion(AsuntoGestion asuntoGestion) {
        this.asuntoGestion = asuntoGestion;
    }
    public String getAbreviatura() {
        return this.abreviatura;
    }
    
    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }
    public String getTextoGestion() {
        return this.textoGestion;
    }
    
    public void setTextoGestion(String textoGestion) {
        this.textoGestion = textoGestion;
    }
    public Integer getCalificacion() {
        return this.calificacion;
    }
    
    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }
    public Set getGestions() {
        return this.gestions;
    }
    
    public void setGestions(Set gestions) {
        this.gestions = gestions;
    }




}


