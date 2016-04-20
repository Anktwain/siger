package dto;
// Generated 29/03/2016 04:35:49 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Institucion generated by hbm2java
 */
public class Institucion  implements java.io.Serializable {


     private Integer idInstitucion;
     private Sujeto sujeto;
     private String nombreCorto;
     private Set productos = new HashSet(0);

    public Institucion() {
    }

	
    public Institucion(Sujeto sujeto, String nombreCorto) {
        this.sujeto = sujeto;
        this.nombreCorto = nombreCorto;
    }
    public Institucion(Sujeto sujeto, String nombreCorto, Set productos) {
       this.sujeto = sujeto;
       this.nombreCorto = nombreCorto;
       this.productos = productos;
    }
   
    public Integer getIdInstitucion() {
        return this.idInstitucion;
    }
    
    public void setIdInstitucion(Integer idInstitucion) {
        this.idInstitucion = idInstitucion;
    }
    public Sujeto getSujeto() {
        return this.sujeto;
    }
    
    public void setSujeto(Sujeto sujeto) {
        this.sujeto = sujeto;
    }
    public String getNombreCorto() {
        return this.nombreCorto;
    }
    
    public void setNombreCorto(String nombreCorto) {
        this.nombreCorto = nombreCorto;
    }
    public Set getProductos() {
        return this.productos;
    }
    
    public void setProductos(Set productos) {
        this.productos = productos;
    }




}


