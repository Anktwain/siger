package dto;
// Generated 27/04/2016 09:32:53 AM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Marcaje generated by hbm2java
 */
public class Marcaje  implements java.io.Serializable {


     private Integer idMarcaje;
     private String marcaje;
     private String descripcion;
     private Set creditos = new HashSet(0);

    public Marcaje() {
    }

	
    public Marcaje(String marcaje) {
        this.marcaje = marcaje;
    }
    public Marcaje(String marcaje, String descripcion, Set creditos) {
       this.marcaje = marcaje;
       this.descripcion = descripcion;
       this.creditos = creditos;
    }
   
    public Integer getIdMarcaje() {
        return this.idMarcaje;
    }
    
    public void setIdMarcaje(Integer idMarcaje) {
        this.idMarcaje = idMarcaje;
    }
    public String getMarcaje() {
        return this.marcaje;
    }
    
    public void setMarcaje(String marcaje) {
        this.marcaje = marcaje;
    }
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public Set getCreditos() {
        return this.creditos;
    }
    
    public void setCreditos(Set creditos) {
        this.creditos = creditos;
    }




}


