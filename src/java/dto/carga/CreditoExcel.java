/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.carga;

/**
 *
 * @author Eduardo
 */
// 2A REESTRUCTURACION DEL MODULO DE CARGA
public class CreditoExcel {

  // ***************************************************************************
  // VARIABLES DE CLASE
  // ***************************************************************************
  private String numeroCredito;
  private String subproducto;
  private String producto;
  private String familiaProducto;
  private String estatusCuenta;
  private String mesesVencidos;
  private String despachoAsignado;
  private String fechaInicioCredito;
  private String fechaVencimientoCredito;
  private String fechaQuebranto;
  private String montoCredito;
  private String mensualidad;
  private String saldoInsoluto;
  private String saldoVencido;
  private String tasaInteres;
  private String cuentaCredito;
  private String fechaUltimoPago;
  private String fechaUltimoVencimientoPagado;
  private DeudorExcel deudor;
  private DatosContactoExcel datosContacto;

  // ***************************************************************************
  // CONSTRUCTOR
  // ***************************************************************************
  public CreditoExcel() {
    
  }
  
  // ***************************************************************************
  // GETTERS & SETTERS
  // ***************************************************************************

  public String getNumeroCredito() {
    return numeroCredito;
  }

  public void setNumeroCredito(String numeroCredito) {
    this.numeroCredito = numeroCredito;
  }

  public String getSubproducto() {
    return subproducto;
  }

  public void setSubproducto(String subproducto) {
    this.subproducto = subproducto;
  }

  public String getProducto() {
    return producto;
  }

  public void setProducto(String producto) {
    this.producto = producto;
  }

  public String getFamiliaProducto() {
    return familiaProducto;
  }

  public void setFamiliaProducto(String familiaProducto) {
    this.familiaProducto = familiaProducto;
  }

  public String getEstatusCuenta() {
    return estatusCuenta;
  }

  public void setEstatusCuenta(String estatusCuenta) {
    this.estatusCuenta = estatusCuenta;
  }

  public String getMesesVencidos() {
    return mesesVencidos;
  }

  public void setMesesVencidos(String mesesVencidos) {
    this.mesesVencidos = mesesVencidos;
  }

  public String getDespachoAsignado() {
    return despachoAsignado;
  }

  public void setDespachoAsignado(String despachoAsignado) {
    this.despachoAsignado = despachoAsignado;
  }

  public String getFechaInicioCredito() {
    return fechaInicioCredito;
  }

  public void setFechaInicioCredito(String fechaInicioCredito) {
    this.fechaInicioCredito = fechaInicioCredito;
  }

  public String getFechaVencimientoCredito() {
    return fechaVencimientoCredito;
  }

  public void setFechaVencimientoCredito(String fechaVencimientoCredito) {
    this.fechaVencimientoCredito = fechaVencimientoCredito;
  }

  public String getFechaQuebranto() {
    return fechaQuebranto;
  }

  public void setFechaQuebranto(String fechaQuebranto) {
    this.fechaQuebranto = fechaQuebranto;
  }

  public String getMontoCredito() {
    return montoCredito;
  }

  public void setMontoCredito(String montoCredito) {
    this.montoCredito = montoCredito;
  }

  public String getMensualidad() {
    return mensualidad;
  }

  public void setMensualidad(String mensualidad) {
    this.mensualidad = mensualidad;
  }

  public String getSaldoInsoluto() {
    return saldoInsoluto;
  }

  public void setSaldoInsoluto(String saldoInsoluto) {
    this.saldoInsoluto = saldoInsoluto;
  }

  public String getSaldoVencido() {
    return saldoVencido;
  }

  public void setSaldoVencido(String saldoVencido) {
    this.saldoVencido = saldoVencido;
  }

  public String getTasaInteres() {
    return tasaInteres;
  }

  public void setTasaInteres(String tasaInteres) {
    this.tasaInteres = tasaInteres;
  }

  public String getCuentaCredito() {
    return cuentaCredito;
  }

  public void setCuentaCredito(String cuentaCredito) {
    this.cuentaCredito = cuentaCredito;
  }

  public String getFechaUltimoPago() {
    return fechaUltimoPago;
  }

  public void setFechaUltimoPago(String fechaUltimoPago) {
    this.fechaUltimoPago = fechaUltimoPago;
  }

  public String getFechaUltimoVencimientoPagado() {
    return fechaUltimoVencimientoPagado;
  }

  public void setFechaUltimoVencimientoPagado(String fechaUltimoVencimientoPagado) {
    this.fechaUltimoVencimientoPagado = fechaUltimoVencimientoPagado;
  }

  public DeudorExcel getDeudor() {
    return deudor;
  }

  public void setDeudor(DeudorExcel deudor) {
    this.deudor = deudor;
  }

  public DatosContactoExcel getDatosContacto() {
    return datosContacto;
  }

  public void setDatosContacto(DatosContactoExcel datosContacto) {
    this.datosContacto = datosContacto;
  }
  
}
