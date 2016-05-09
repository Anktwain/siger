package dto;
// Generated 27/04/2016 09:32:53 AM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Producto generated by hbm2java
 */
public class Producto  implements java.io.Serializable {


     private Integer idProducto;
     private Institucion institucion;
     private String nombre;
     private String descripcion;
     private Set creditos = new HashSet(0);
     private Set subproductos = new HashSet(0);

    public Producto() {
    }

	
    public Producto(Institucion institucion, String nombre) {
        this.institucion = institucion;
        this.nombre = nombre;
    }
    public Producto(Institucion institucion, String nombre, String descripcion, Set creditos, Set subproductos) {
       this.institucion = institucion;
       this.nombre = nombre;
       this.descripcion = descripcion;
       this.creditos = creditos;
       this.subproductos = subproductos;
    }
   
    public Integer getIdProducto() {
        return this.idProducto;
    }
    
    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }
    public Institucion getInstitucion() {
        return this.institucion;
    }
    
    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
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
    public Set getCreditos() {
        return this.creditos;
    }
    
    public void setCreditos(Set creditos) {
        this.creditos = creditos;
    }
    public Set getSubproductos() {
        return this.subproductos;
    }
    
    public void setSubproductos(Set subproductos) {
        this.subproductos = subproductos;
    }




}


