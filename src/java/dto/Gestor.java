package dto;
// Generated 4/02/2016 12:05:50 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Gestor generated by hbm2java
 */
public class Gestor  implements java.io.Serializable {


     private Integer idGestor;
     private Usuario usuario;
     private String extension;
     private Set creditos = new HashSet(0);
     private Set zonas = new HashSet(0);
     private Set pagos = new HashSet(0);

    public Gestor() {
    }

	
    public Gestor(Usuario usuario) {
        this.usuario = usuario;
    }
    public Gestor(Usuario usuario, String extension, Set creditos, Set zonas, Set pagos) {
       this.usuario = usuario;
       this.extension = extension;
       this.creditos = creditos;
       this.zonas = zonas;
       this.pagos = pagos;
    }
   
    public Integer getIdGestor() {
        return this.idGestor;
    }
    
    public void setIdGestor(Integer idGestor) {
        this.idGestor = idGestor;
    }
    public Usuario getUsuario() {
        return this.usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public String getExtension() {
        return this.extension;
    }
    
    public void setExtension(String extension) {
        this.extension = extension;
    }
    public Set getCreditos() {
        return this.creditos;
    }
    
    public void setCreditos(Set creditos) {
        this.creditos = creditos;
    }
    public Set getZonas() {
        return this.zonas;
    }
    
    public void setZonas(Set zonas) {
        this.zonas = zonas;
    }
    public Set getPagos() {
        return this.pagos;
    }
    
    public void setPagos(Set pagos) {
        this.pagos = pagos;
    }




}


