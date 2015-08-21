package dto;
// Generated 19/08/2015 01:24:43 PM by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * Gestion generated by hbm2java
 */
public class Gestion  implements java.io.Serializable {


     private Integer idGestion;
     private Credito credito;
     private Date fecha;

    public Gestion() {
    }

	
    public Gestion(Credito credito) {
        this.credito = credito;
    }
    public Gestion(Credito credito, Date fecha) {
       this.credito = credito;
       this.fecha = fecha;
    }
   
    public Integer getIdGestion() {
        return this.idGestion;
    }
    
    public void setIdGestion(Integer idGestion) {
        this.idGestion = idGestion;
    }
    public Credito getCredito() {
        return this.credito;
    }
    
    public void setCredito(Credito credito) {
        this.credito = credito;
    }
    public Date getFecha() {
        return this.fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }




}


