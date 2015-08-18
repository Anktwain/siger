package dto;
// Generated 18/08/2015 11:07:25 AM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Subproducto generated by hbm2java
 */
public class Subproducto  implements java.io.Serializable {


     private Integer idSubproducto;
     private Producto producto;
     private String nombre;
     private String descripcion;
     private Set creditos = new HashSet(0);

    public Subproducto() {
    }

	
    public Subproducto(Producto producto, String nombre) {
        this.producto = producto;
        this.nombre = nombre;
    }
    public Subproducto(Producto producto, String nombre, String descripcion, Set creditos) {
       this.producto = producto;
       this.nombre = nombre;
       this.descripcion = descripcion;
       this.creditos = creditos;
    }
   
    public Integer getIdSubproducto() {
        return this.idSubproducto;
    }
    
    public void setIdSubproducto(Integer idSubproducto) {
        this.idSubproducto = idSubproducto;
    }
    public Producto getProducto() {
        return this.producto;
    }
    
    public void setProducto(Producto producto) {
        this.producto = producto;
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




}


