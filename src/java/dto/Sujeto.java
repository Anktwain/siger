package dto;
// Generated 4/02/2016 12:05:50 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Sujeto generated by hbm2java
 */
public class Sujeto  implements java.io.Serializable {


     private Integer idSujeto;
     private String nombreRazonSocial;
     private String rfc;
     private int eliminado;
     private Set direccions = new HashSet(0);
     private Set emails = new HashSet(0);
     private Set institucions = new HashSet(0);
     private Set despachos = new HashSet(0);
     private Set deudors = new HashSet(0);
     private Set telefonos = new HashSet(0);
     private Set contactos = new HashSet(0);

    public Sujeto() {
    }

	
    public Sujeto(String nombreRazonSocial, int eliminado) {
        this.nombreRazonSocial = nombreRazonSocial;
        this.eliminado = eliminado;
    }
    
    public Sujeto(String nombreRazonSocial, String rfc, int eliminado) {
        this.nombreRazonSocial = nombreRazonSocial;
        this.rfc = rfc;
        this.eliminado = eliminado;
    }
    
    public Sujeto(String nombreRazonSocial, String rfc, int eliminado, Set direccions, Set emails, Set institucions, Set despachos, Set deudors, Set telefonos, Set contactos) {
       this.nombreRazonSocial = nombreRazonSocial;
       this.rfc = rfc;
       this.eliminado = eliminado;
       this.direccions = direccions;
       this.emails = emails;
       this.institucions = institucions;
       this.despachos = despachos;
       this.deudors = deudors;
       this.telefonos = telefonos;
       this.contactos = contactos;
    }
   
    public Integer getIdSujeto() {
        return this.idSujeto;
    }
    
    public void setIdSujeto(Integer idSujeto) {
        this.idSujeto = idSujeto;
    }
    public String getNombreRazonSocial() {
        return this.nombreRazonSocial;
    }
    
    public void setNombreRazonSocial(String nombreRazonSocial) {
        this.nombreRazonSocial = nombreRazonSocial;
    }
    public String getRfc() {
        return this.rfc;
    }
    
    public void setRfc(String rfc) {
        this.rfc = rfc;
    }
    public int getEliminado() {
        return this.eliminado;
    }
    
    public void setEliminado(int eliminado) {
        this.eliminado = eliminado;
    }
    public Set getDireccions() {
        return this.direccions;
    }
    
    public void setDireccions(Set direccions) {
        this.direccions = direccions;
    }
    public Set getEmails() {
        return this.emails;
    }
    
    public void setEmails(Set emails) {
        this.emails = emails;
    }
    public Set getInstitucions() {
        return this.institucions;
    }
    
    public void setInstitucions(Set institucions) {
        this.institucions = institucions;
    }
    public Set getDespachos() {
        return this.despachos;
    }
    
    public void setDespachos(Set despachos) {
        this.despachos = despachos;
    }
    public Set getDeudors() {
        return this.deudors;
    }
    
    public void setDeudors(Set deudors) {
        this.deudors = deudors;
    }
    public Set getTelefonos() {
        return this.telefonos;
    }
    
    public void setTelefonos(Set telefonos) {
        this.telefonos = telefonos;
    }
    public Set getContactos() {
        return this.contactos;
    }
    
    public void setContactos(Set contactos) {
        this.contactos = contactos;
    }




}


