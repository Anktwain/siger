package dto;
// Generated 9/03/2016 10:01:38 AM by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Remesa generated by hbm2java
 */
public class Remesa  implements java.io.Serializable {


     private Integer idRemesa;
     private Integer mes;
     private Integer anio;
     private Integer totalCreditos;
     private Float totalSaldoVencido;
     private Date fechaCarga;
     private Integer quienCarga;
     private Set actualizacions = new HashSet(0);

    public Remesa() {
    }

    public Remesa(Integer mes, Integer anio, Integer totalCreditos, Float totalSaldoVencido, Date fechaCarga, Integer quienCarga, Set actualizacions) {
       this.mes = mes;
       this.anio = anio;
       this.totalCreditos = totalCreditos;
       this.totalSaldoVencido = totalSaldoVencido;
       this.fechaCarga = fechaCarga;
       this.quienCarga = quienCarga;
       this.actualizacions = actualizacions;
    }
   
    public Integer getIdRemesa() {
        return this.idRemesa;
    }
    
    public void setIdRemesa(Integer idRemesa) {
        this.idRemesa = idRemesa;
    }
    public Integer getMes() {
        return this.mes;
    }
    
    public void setMes(Integer mes) {
        this.mes = mes;
    }
    public Integer getAnio() {
        return this.anio;
    }
    
    public void setAnio(Integer anio) {
        this.anio = anio;
    }
    public Integer getTotalCreditos() {
        return this.totalCreditos;
    }
    
    public void setTotalCreditos(Integer totalCreditos) {
        this.totalCreditos = totalCreditos;
    }
    public Float getTotalSaldoVencido() {
        return this.totalSaldoVencido;
    }
    
    public void setTotalSaldoVencido(Float totalSaldoVencido) {
        this.totalSaldoVencido = totalSaldoVencido;
    }
    public Date getFechaCarga() {
        return this.fechaCarga;
    }
    
    public void setFechaCarga(Date fechaCarga) {
        this.fechaCarga = fechaCarga;
    }
    public Integer getQuienCarga() {
        return this.quienCarga;
    }
    
    public void setQuienCarga(Integer quienCarga) {
        this.quienCarga = quienCarga;
    }
    public Set getActualizacions() {
        return this.actualizacions;
    }
    
    public void setActualizacions(Set actualizacions) {
        this.actualizacions = actualizacions;
    }




}


