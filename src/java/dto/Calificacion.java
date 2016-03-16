package dto;
// Generated 9/03/2016 10:01:38 AM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Calificacion generated by hbm2java
 */
public class Calificacion  implements java.io.Serializable {


     private Integer idCalificacion;
     private String nombre;
     private String descripcion;
     private Set deudors = new HashSet(0);

    public Calificacion() {
    }

	
    public Calificacion(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    public Calificacion(String nombre, String descripcion, Set deudors) {
       this.nombre = nombre;
       this.descripcion = descripcion;
       this.deudors = deudors;
    }
   
    public Integer getIdCalificacion() {
        return this.idCalificacion;
    }
    
    public void setIdCalificacion(Integer idCalificacion) {
        this.idCalificacion = idCalificacion;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public Set getDeudors() {
        return this.deudors;
    }
    
    public void setDeudors(Set deudors) {
        this.deudors = deudors;
    }




}


