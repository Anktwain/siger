package dto;
// Generated 26/10/2015 11:55:09 AM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Despacho generated by hbm2java
 */
public class Despacho  implements java.io.Serializable {


     private Integer idDespacho;
     private Sujeto sujeto;
     private String nombreCorto;
     private Set zonas = new HashSet(0);
     private Set usuarios = new HashSet(0);
     private Set creditos = new HashSet(0);

    public Despacho() {
    }

	
    public Despacho(Sujeto sujeto) {
        this.sujeto = sujeto;
    }
    public Despacho(Sujeto sujeto, String nombreCorto, Set zonas, Set usuarios, Set creditos) {
       this.sujeto = sujeto;
       this.nombreCorto = nombreCorto;
       this.zonas = zonas;
       this.usuarios = usuarios;
       this.creditos = creditos;
    }
   
    public Integer getIdDespacho() {
        return this.idDespacho;
    }
    
    public void setIdDespacho(Integer idDespacho) {
        this.idDespacho = idDespacho;
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
    public Set getZonas() {
        return this.zonas;
    }
    
    public void setZonas(Set zonas) {
        this.zonas = zonas;
    }
    public Set getUsuarios() {
        return this.usuarios;
    }
    
    public void setUsuarios(Set usuarios) {
        this.usuarios = usuarios;
    }
    public Set getCreditos() {
        return this.creditos;
    }
    
    public void setCreditos(Set creditos) {
        this.creditos = creditos;
    }




}


