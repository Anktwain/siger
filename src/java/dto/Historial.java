package dto;
// Generated 21/04/2016 03:48:27 PM by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * Historial generated by hbm2java
 */
public class Historial  implements java.io.Serializable {


     private Integer idHistorial;
     private Credito credito;
     private Date fecha;
     private String evento;

    public Historial() {
    }

    public Historial(Credito credito, Date fecha, String evento) {
       this.credito = credito;
       this.fecha = fecha;
       this.evento = evento;
    }
   
    public Integer getIdHistorial() {
        return this.idHistorial;
    }
    
    public void setIdHistorial(Integer idHistorial) {
        this.idHistorial = idHistorial;
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
    public String getEvento() {
        return this.evento;
    }
    
    public void setEvento(String evento) {
        this.evento = evento;
    }




}


