package dto;
// Generated 9/02/2016 02:44:04 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Deudor generated by hbm2java
 */
public class Deudor  implements java.io.Serializable {


     private Integer idDeudor;
     private Sujeto sujeto;
     private String numeroDeudor;
     private String curp;
     private String numeroSeguroSocial;
     private Set contactos = new HashSet(0);
     private Set creditos = new HashSet(0);

    public Deudor() {
    }

	
    public Deudor(Sujeto sujeto) {
        this.sujeto = sujeto;
    }
    public Deudor(Sujeto sujeto, String numeroDeudor, String curp, String numeroSeguroSocial, Set contactos, Set creditos) {
       this.sujeto = sujeto;
       this.numeroDeudor = numeroDeudor;
       this.curp = curp;
       this.numeroSeguroSocial = numeroSeguroSocial;
       this.contactos = contactos;
       this.creditos = creditos;
    }
   
    public Integer getIdDeudor() {
        return this.idDeudor;
    }
    
    public void setIdDeudor(Integer idDeudor) {
        this.idDeudor = idDeudor;
    }
    public Sujeto getSujeto() {
        return this.sujeto;
    }
    
    public void setSujeto(Sujeto sujeto) {
        this.sujeto = sujeto;
    }
    public String getNumeroDeudor() {
        return this.numeroDeudor;
    }
    
    public void setNumeroDeudor(String numeroDeudor) {
        this.numeroDeudor = numeroDeudor;
    }
    public String getCurp() {
        return this.curp;
    }
    
    public void setCurp(String curp) {
        this.curp = curp;
    }
    public String getNumeroSeguroSocial() {
        return this.numeroSeguroSocial;
    }
    
    public void setNumeroSeguroSocial(String numeroSeguroSocial) {
        this.numeroSeguroSocial = numeroSeguroSocial;
    }
    public Set getContactos() {
        return this.contactos;
    }
    
    public void setContactos(Set contactos) {
        this.contactos = contactos;
    }
    public Set getCreditos() {
        return this.creditos;
    }
    
    public void setCreditos(Set creditos) {
        this.creditos = creditos;
    }




}


