package dto;
// Generated 12/05/2015 01:10:38 PM by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Remesas generated by hbm2java
 */
public class Remesas  implements java.io.Serializable {


     private Integer idRemesa;
     private Integer mesesVencidos;
     private Float saldoVencido;
     private String estatus;
     private Date fechaUltimoPago;
     private Set facses = new HashSet(0);
     private Set creditosRemesases = new HashSet(0);

    public Remesas() {
    }

    public Remesas(Integer mesesVencidos, Float saldoVencido, String estatus, Date fechaUltimoPago, Set facses, Set creditosRemesases) {
       this.mesesVencidos = mesesVencidos;
       this.saldoVencido = saldoVencido;
       this.estatus = estatus;
       this.fechaUltimoPago = fechaUltimoPago;
       this.facses = facses;
       this.creditosRemesases = creditosRemesases;
    }
   
    public Integer getIdRemesa() {
        return this.idRemesa;
    }
    
    public void setIdRemesa(Integer idRemesa) {
        this.idRemesa = idRemesa;
    }
    public Integer getMesesVencidos() {
        return this.mesesVencidos;
    }
    
    public void setMesesVencidos(Integer mesesVencidos) {
        this.mesesVencidos = mesesVencidos;
    }
    public Float getSaldoVencido() {
        return this.saldoVencido;
    }
    
    public void setSaldoVencido(Float saldoVencido) {
        this.saldoVencido = saldoVencido;
    }
    public String getEstatus() {
        return this.estatus;
    }
    
    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
    public Date getFechaUltimoPago() {
        return this.fechaUltimoPago;
    }
    
    public void setFechaUltimoPago(Date fechaUltimoPago) {
        this.fechaUltimoPago = fechaUltimoPago;
    }
    public Set getFacses() {
        return this.facses;
    }
    
    public void setFacses(Set facses) {
        this.facses = facses;
    }
    public Set getCreditosRemesases() {
        return this.creditosRemesases;
    }
    
    public void setCreditosRemesases(Set creditosRemesases) {
        this.creditosRemesases = creditosRemesases;
    }




}


