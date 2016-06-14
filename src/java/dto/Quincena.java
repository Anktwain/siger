package dto;
// Generated 14/06/2016 11:17:20 AM by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Quincena generated by hbm2java
 */
public class Quincena  implements java.io.Serializable {


     private int idQuincena;
     private String nombre;
     private Date fechaInicio;
     private Date fechaFin;
     private Set pagos = new HashSet(0);

    public Quincena() {
    }

	
    public Quincena(int idQuincena, Date fechaInicio, Date fechaFin) {
        this.idQuincena = idQuincena;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
    public Quincena(int idQuincena, String nombre, Date fechaInicio, Date fechaFin, Set pagos) {
       this.idQuincena = idQuincena;
       this.nombre = nombre;
       this.fechaInicio = fechaInicio;
       this.fechaFin = fechaFin;
       this.pagos = pagos;
    }
   
    public int getIdQuincena() {
        return this.idQuincena;
    }
    
    public void setIdQuincena(int idQuincena) {
        this.idQuincena = idQuincena;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Date getFechaInicio() {
        return this.fechaInicio;
    }
    
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    public Date getFechaFin() {
        return this.fechaFin;
    }
    
    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
    public Set getPagos() {
        return this.pagos;
    }
    
    public void setPagos(Set pagos) {
        this.pagos = pagos;
    }




}


