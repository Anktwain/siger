package dto;
// Generated 11/12/2015 11:59:28 AM by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ConvenioPago generated by hbm2java
 */
public class ConvenioPago  implements java.io.Serializable {


     private Integer idConvenioPago;
     private Credito credito;
     private Date fecha;
     private int estatus;
     private float saldoNegociado;
     private int pagosRealizados;
     private Set pagos = new HashSet(0);

    public ConvenioPago() {
    }

	
    public ConvenioPago(Credito credito, Date fecha, int estatus, float saldoNegociado, int pagosRealizados) {
        this.credito = credito;
        this.fecha = fecha;
        this.estatus = estatus;
        this.saldoNegociado = saldoNegociado;
        this.pagosRealizados = pagosRealizados;
    }
    public ConvenioPago(Credito credito, Date fecha, int estatus, float saldoNegociado, int pagosRealizados, Set pagos) {
       this.credito = credito;
       this.fecha = fecha;
       this.estatus = estatus;
       this.saldoNegociado = saldoNegociado;
       this.pagosRealizados = pagosRealizados;
       this.pagos = pagos;
    }
   
    public Integer getIdConvenioPago() {
        return this.idConvenioPago;
    }
    
    public void setIdConvenioPago(Integer idConvenioPago) {
        this.idConvenioPago = idConvenioPago;
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
    public int getEstatus() {
        return this.estatus;
    }
    
    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }
    public float getSaldoNegociado() {
        return this.saldoNegociado;
    }
    
    public void setSaldoNegociado(float saldoNegociado) {
        this.saldoNegociado = saldoNegociado;
    }
    public int getPagosRealizados() {
        return this.pagosRealizados;
    }
    
    public void setPagosRealizados(int pagosRealizados) {
        this.pagosRealizados = pagosRealizados;
    }
    public Set getPagos() {
        return this.pagos;
    }
    
    public void setPagos(Set pagos) {
        this.pagos = pagos;
    }




}


