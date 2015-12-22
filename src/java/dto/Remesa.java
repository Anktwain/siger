package dto;
// Generated 11/12/2015 11:59:28 AM by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Remesa generated by hbm2java
 */
public class Remesa  implements java.io.Serializable {


     private Integer idRemesa;
     private Integer mesesVencidos;
     private Float saldoVencido;
     private String estatus;
     private Date fechaUltimoPago;
     private Date fechaUltimoVencimientoPagado;
     private Date vigencia;
     private Set creditoRemesas = new HashSet(0);
     private Set facs = new HashSet(0);

    public Remesa() {
    }

    public Remesa(Integer mesesVencidos, Float saldoVencido, String estatus, Date fechaUltimoPago, Date fechaUltimoVencimientoPagado, Date vigencia, Set creditoRemesas, Set facs) {
       this.mesesVencidos = mesesVencidos;
       this.saldoVencido = saldoVencido;
       this.estatus = estatus;
       this.fechaUltimoPago = fechaUltimoPago;
       this.fechaUltimoVencimientoPagado = fechaUltimoVencimientoPagado;
       this.vigencia = vigencia;
       this.creditoRemesas = creditoRemesas;
       this.facs = facs;
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
    public Date getFechaUltimoVencimientoPagado() {
        return this.fechaUltimoVencimientoPagado;
    }
    
    public void setFechaUltimoVencimientoPagado(Date fechaUltimoVencimientoPagado) {
        this.fechaUltimoVencimientoPagado = fechaUltimoVencimientoPagado;
    }
    public Date getVigencia() {
        return this.vigencia;
    }
    
    public void setVigencia(Date vigencia) {
        this.vigencia = vigencia;
    }
    public Set getCreditoRemesas() {
        return this.creditoRemesas;
    }
    
    public void setCreditoRemesas(Set creditoRemesas) {
        this.creditoRemesas = creditoRemesas;
    }
    public Set getFacs() {
        return this.facs;
    }
    
    public void setFacs(Set facs) {
        this.facs = facs;
    }




}


