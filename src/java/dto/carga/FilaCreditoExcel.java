/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.carga;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eduardo
 */
// REESTRUCTURACION DEL MODULO DE CARGA
public class FilaCreditoExcel {

  // ***************************************************************************
  // ***************************************************************************
  // VARIABLES DE CLASE
  // ***************************************************************************
  // ***************************************************************************
  private String numeroCredito;
  private String nombreDeudor;
  private String referenciaCobro;
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
  private String numeroDeudor;
  private String rfc;
  private List<Facs> listaFacs;
  private List<Ajustes> listaAjustes;

  // ***************************************************************************
  // ***************************************************************************
  // CONSTRUCTOR
  // ***************************************************************************
  // ***************************************************************************
  public FilaCreditoExcel() {
    numeroCredito = null;
    nombreDeudor = null;
    referenciaCobro = null;
    subproducto = null;
    producto = null;
    familiaProducto = null;
    estatusCuenta = null;
    mesesVencidos = null;
    despachoAsignado = null;
    fechaInicioCredito = null;
    fechaVencimientoCredito = null;
    fechaQuebranto = null;
    montoCredito = null;
    mensualidad = null;
    saldoInsoluto = null;
    saldoVencido = null;
    tasaInteres = null;
    cuentaCredito = null;
    fechaUltimoPago = null;
    fechaUltimoVencimientoPagado = null;
    numeroDeudor = null;
    rfc = null;
    listaFacs = new ArrayList();
    listaAjustes = new ArrayList();
  }

  // ***************************************************************************
  // ***************************************************************************
  // GETTERS & SETTERS
  // ***************************************************************************
  // ***************************************************************************
  public String getNumeroCredito() {
    return numeroCredito;
  }

  public void setNumeroCredito(String numeroCredito) {
    this.numeroCredito = numeroCredito;
  }

  public String getNombreDeudor() {
    return nombreDeudor;
  }

  public void setNombreDeudor(String nombreDeudor) {
    this.nombreDeudor = nombreDeudor;
  }

  public String getReferenciaCobro() {
    return referenciaCobro;
  }

  public void setReferenciaCobro(String referenciaCobro) {
    this.referenciaCobro = referenciaCobro;
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

  public String getNumeroDeudor() {
    return numeroDeudor;
  }

  public void setNumeroDeudor(String numeroDeudor) {
    this.numeroDeudor = numeroDeudor;
  }

  public String getRfc() {
    return rfc;
  }

  public void setRfc(String rfc) {
    this.rfc = rfc;
  }

  public List<Facs> getListaFacs() {
    return listaFacs;
  }

  public void setListaFacs(List<Facs> listaFacs) {
    this.listaFacs = listaFacs;
  }

  public List<Ajustes> getListaAjustes() {
    return listaAjustes;
  }

  public void setListaAjustes(List<Ajustes> listaAjustes) {
    this.listaAjustes = listaAjustes;
  }

  // ***************************************************************************
  // ***************************************************************************
  // CLASE MIEMBRO PARA REPRESENTAR LAS ACTUALIZACIONES DE TELMEX
  // ***************************************************************************
  // ***************************************************************************
  public static class Facs {

    // VARIABLES DE CLASE
    private String anio;
    private String mes;
    private String respuesta;
    private String monto;

    //CONSTRUCTOR
    public Facs() {
    }

    //GETTERS & SETTERS
    public String getAnio() {
      return anio;
    }

    public void setAnio(String anio) {
      this.anio = anio;
    }

    public String getMes() {
      return mes;
    }

    public void setMes(String mes) {
      this.mes = mes;
    }

    public String getRespuesta() {
      return respuesta;
    }

    public void setRespuesta(String respuesta) {
      this.respuesta = respuesta;
    }

    public String getMonto() {
      return monto;
    }

    public void setMonto(String monto) {
      this.monto = monto;
    }
  }

  // ***************************************************************************
  // ***************************************************************************
  // CLASE MIEMBRO PARA REPRESENTAR LOS AJUSTES DE TELMEX
  // ***************************************************************************
  // ***************************************************************************
  public static class Ajustes {

    // VARIABLES DE CLASE
    private int tipo;
    private String ajuste;

    // CONSTRUCTOR
    public Ajustes() {
    }

    //GETTERS & SETTERS
    public int getTipo() {
      return tipo;
    }

    public void setTipo(int tipo) {
      this.tipo = tipo;
    }

    public String getAjuste() {
      return ajuste;
    }

    public void setAjuste(String ajuste) {
      this.ajuste = ajuste;
    }
  }

}
