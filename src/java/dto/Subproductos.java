package dto;
// Generated 12/05/2015 01:10:38 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Subproductos generated by hbm2java
 */
public class Subproductos  implements java.io.Serializable {


     private Integer idSubproducto;
     private Productos productos;
     private String nombre;
     private String descripcion;
     private Set creditoses = new HashSet(0);

    public Subproductos() {
    }

	
    public Subproductos(Productos productos, String nombre) {
        this.productos = productos;
        this.nombre = nombre;
    }
    public Subproductos(Productos productos, String nombre, String descripcion, Set creditoses) {
       this.productos = productos;
       this.nombre = nombre;
       this.descripcion = descripcion;
       this.creditoses = creditoses;
    }
   
    public Integer getIdSubproducto() {
        return this.idSubproducto;
    }
    
    public void setIdSubproducto(Integer idSubproducto) {
        this.idSubproducto = idSubproducto;
    }
    public Productos getProductos() {
        return this.productos;
    }
    
    public void setProductos(Productos productos) {
        this.productos = productos;
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
    public Set getCreditoses() {
        return this.creditoses;
    }
    
    public void setCreditoses(Set creditoses) {
        this.creditoses = creditoses;
    }




}


