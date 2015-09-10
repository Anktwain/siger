package dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Cofradia
 */
public class Fila implements Serializable {

  private String credito;
  private String nombre;
  private String refCobro;
  private String linea;
  private String tipoCredito;
  private String estatus;
  private String mesesVencidos;
  private String despacho;
  private String fechaInicioCredito;
  private String fechaVencimientoCred;
  private String disposicion;
  private String mensualidad;
  private String saldoInsoluto;
  private String saldoVencido;
  private String tasa;
  private String cuenta;
  private String fechaUltimoPago;
  private String fechaUltimoVencimientoPagado;
  private String idCliente;
  private String rfc;
  private String calle;
  private String colonia;
  private String estado;
  private String municipio;
  private String cp;

  private ArrayList<String> anio;
  private ArrayList<String> mes;
  private ArrayList<String> facMes;
  private ArrayList<String> monto;

  private ArrayList<String> refsAdicionales;

  private ArrayList<String> correos;

  private ArrayList<String> telsAdicionales;

  private ArrayList<String> direcsAdicionales;

  private String marcaje;
  private String fechaQuebranto;

  public Fila() {
  }

  /**
   * @return {@code credito} el campo de la columna 1 en el archivo estándar de
   * excel para la carga de remesas en SigerWeb1
   */
  public String getCredito() {
    return credito;
  }

  public void setCredito(String credito) {
    this.credito = credito;
  }

  /**
   * @return {@code nombre} el campo de la columna 2 en el archivo estándar de excel
   * para la carga de remesas en SigerWeb1
   */
  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  /**
   * @return {@code refCobro} el campo de la columna 3 en el archivo estándar de excel
   * para la carga de remesas en SigerWeb1
   */
  public String getRefCobro() {
    return refCobro;
  }

  public void setRefCobro(String refCobro) {
    this.refCobro = refCobro;
  }

  /**
   * @return {@code linea} el campo de la columna 4 en el archivo estándar de excel
   * para la carga de remesas en SigerWeb1
   */
  public String getLinea() {
    return linea;
  }

  public void setLinea(String linea) {
    this.linea = linea;
  }

  /**
   * @return {@code tipoCredito} el campo de la columna 5 en el archivo estándar de excel
   * para la carga de remesas en SigerWeb1
   */
  public String getTipoCredito() {
    return tipoCredito;
  }

  public void setTipoCredito(String tipoCredito) {
    this.tipoCredito = tipoCredito;
  }

  /**
   * @return {@code estatus} el campo de la columna 6 en el archivo estándar de excel
   * para la carga de remesas en SigerWeb1
   */
  public String getEstatus() {
    return estatus;
  }

  public void setEstatus(String estatus) {
    this.estatus = estatus;
  }

  /**
   * @return {@code mesesVencidos} el campo de la columna 7 en el archivo estándar de excel
   * para la carga de remesas en SigerWeb1
   */
  public String getMesesVencidos() {
    return mesesVencidos;
  }

  public void setMesesVencidos(String mesesVencidos) {
    this.mesesVencidos = mesesVencidos;
  }

  /**
   * @return {@code despacho} el campo de la columna 8 en el archivo estándar de excel
   * para la carga de remesas en SigerWeb1
   */
  public String getDespacho() {
    return despacho;
  }

  public void setDespacho(String despacho) {
    this.despacho = despacho;
  }

  /**
   * @return {@code fechaInicioCredito} el campo de la columna 9 en el archivo estándar de excel
   * para la carga de remesas en SigerWeb1
   */
  public String getFechaInicioCredito() {
    return fechaInicioCredito;
  }

  public void setFechaInicioCredito(String fechaInicioCredito) {
    this.fechaInicioCredito = fechaInicioCredito;
  }

  /**
   * @return {@code fechaVencimientoCred} el campo de la columna 10 en el archivo estándar de excel
   * para la carga de remesas en SigerWeb1
   */
  public String getFechaVencimientoCred() {
    return fechaVencimientoCred;
  }

  public void setFechaVencimientoCred(String fechaVencimientoCred) {
    this.fechaVencimientoCred = fechaVencimientoCred;
  }

  /**
   * @return {@code disposicion} el campo de la columna 11 en el archivo estándar de excel
   * para la carga de remesas en SigerWeb1
   */
  public String getDisposicion() {
    return disposicion;
  }

  public void setDisposicion(String disposicion) {
    this.disposicion = disposicion;
  }

  /**
   * @return {@code mensualidad} el campo de la columna 12 en el archivo estándar de excel
   * para la carga de remesas en SigerWeb1
   */
  public String getMensualidad() {
    return mensualidad;
  }

  public void setMensualidad(String mensualidad) {
    this.mensualidad = mensualidad;
  }

  /**
   * @return {@code saldoInsoluto} el campo de la columna 13 en el archivo estándar de excel
   * para la carga de remesas en SigerWeb1
   */
  public String getSaldoInsoluto() {
    return saldoInsoluto;
  }

  public void setSaldoInsoluto(String saldoInsoluto) {
    this.saldoInsoluto = saldoInsoluto;
  }

  /**
   * @return {@code saldoVencido} el campo de la columna 14 en el archivo estándar de excel
   * para la carga de remesas en SigerWeb1
   */
  public String getSaldoVencido() {
    return saldoVencido;
  }

  public void setSaldoVencido(String saldoVencido) {
    this.saldoVencido = saldoVencido;
  }

  /**
   * @return {@code tasa} el campo de la columna 15 en el archivo estándar de excel
   * para la carga de remesas en SigerWeb1
   */
  public String getTasa() {
    return tasa;
  }

  public void setTasa(String tasa) {
    this.tasa = tasa;
  }

  /**
   * @return {@code cuenta} el campo de la columna 16 en el archivo estándar de excel
   * para la carga de remesas en SigerWeb1
   */
  public String getCuenta() {
    return cuenta;
  }

  public void setCuenta(String cuenta) {
    this.cuenta = cuenta;
  }

  /**
   * @return {@code fechaUltimoPago} el campo de la columna 17 en el archivo estándar de excel
   * para la carga de remesas en SigerWeb1
   */
  public String getFechaUltimoPago() {
    return fechaUltimoPago;
  }

  public void setFechaUltimoPago(String fechaUltimoPago) {
    this.fechaUltimoPago = fechaUltimoPago;
  }

  /**
   * @return {@code fechaUltimoVencimientoPagado} el campo de la columna 18 en 
   * el archivo estándar de excel para la carga de remesas en SigerWeb1
   */
  public String getFechaUltimoVencimientoPagado() {
    return fechaUltimoVencimientoPagado;
  }

  public void setFechaUltimoVencimientoPagado(String fechaUltimoVencimientoPagado) {
    this.fechaUltimoVencimientoPagado = fechaUltimoVencimientoPagado;
  }

  /**
   * @return {@code idCliente} el campo de la columna 19 en el archivo estándar de excel
   * para la carga de remesas en SigerWeb1
   */
  public String getIdCliente() {
    return idCliente;
  }

  public void setIdCliente(String idCliente) {
    this.idCliente = idCliente;
  }

  /**
   * @return {@code rfc} el campo de la columna 20 en el archivo estándar de excel
   * para la carga de remesas en SigerWeb1
   */
  public String getRfc() {
    return rfc;
  }

  public void setRfc(String rfc) {
    this.rfc = rfc;
  }

  /**
   * @return {@code calle} el campo de la columna 21 en el archivo estándar de excel
   * para la carga de remesas en SigerWeb1
   */
  public String getCalle() {
    return calle;
  }

  public void setCalle(String calle) {
    this.calle = calle;
  }

  /**
   * @return {@code colonia} el campo de la columna 22 en el archivo estándar de excel
   * para la carga de remesas en SigerWeb1
   */
  public String getColonia() {
    return colonia;
  }

  public void setColonia(String colonia) {
    this.colonia = colonia;
  }

  /**
   * @return {@code estado} el campo de la columna 23 en el archivo estándar de excel
   * para la carga de remesas en SigerWeb1
   */
  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  /**
   * @return {@code municipio} el campo de la columna 24 en el archivo estándar de excel
   * para la carga de remesas en SigerWeb1
   */
  public String getMunicipio() {
    return municipio;
  }

  public void setMunicipio(String municipio) {
    this.municipio = municipio;
  }

  /**
   * @return {@code cp} el campo de la columna 25 en el archivo estándar de excel
   * para la carga de remesas en SigerWeb1
   */
  public String getCp() {
    return cp;
  }

  public void setCp(String cp) {
    this.cp = cp;
  }

  /**
   * @return {@code anio} el ArrayList que contiene los hasta 3 registros 
   * (columnas 26, 30 y 34 en el archivo estándar de excel) del año 
   * correspondientes a las fechas de facturacion de los créditos que se 
   * facturan por un tercero.
   */
  public ArrayList<String> getAnio() {
    return anio;
  }

  public void setAnio(ArrayList<String> anio) {
    this.anio = anio;
  }

  /**
   * @return {@code mes} el ArrayList que contiene los hasta 3 registros 
   * (columnas 27, 31 y 35 en el archivo estándar de excel) del mes 
   * correspondientes a las fechas de facturacion de los créditos que se 
   * facturan por un tercero.
   */
  public ArrayList<String> getMes() {
    return mes;
  }

  public void setMes(ArrayList<String> mes) {
    this.mes = mes;
  }

  /**
   * @return {@code facMes} el ArrayList que contiene los hasta 3 registros 
   * (columnas 28, 32 y 36 en el archivo estándar de excel) fac_mes 
   * correspondientes a las fechas de facturacion de los créditos que se 
   * facturan por un tercero.
   */
  public ArrayList<String> getFacMes() {
    return facMes;
  }

  public void setFacMes(ArrayList<String> facMes) {
    this.facMes = facMes;
  }

  /**
   * @return {@code facMes} el ArrayList que contiene los hasta 3 registros 
   * (columnas 29, 33 y 37 en el archivo estándar de excel) del monto
   * correspondientes a las fechas de facturacion de los créditos que se 
   * facturan por un tercero.
   */
  public ArrayList<String> getMonto() {
    return monto;
  }

  public void setMonto(ArrayList<String> monto) {
    this.monto = monto;
  }

  /**
   * @return {@code refsAdicionales} ArrayList que contiene los hasta 3 
   * registros (columnas 38, 39 y 40) de las referencias.
   */
  public ArrayList<String> getRefsAdicionales() {
    return refsAdicionales;
  }

  public void setRefsAdicionales(ArrayList<String> refsAdicionales) {
    this.refsAdicionales = refsAdicionales;
  }

  /**
   * @return {@code correos} ArrayList que contiene los correos electrónicos 
   * de contacto del deudor. <strong>Sólo existe una columna (la 41) en el 
   * archivo estándar de excel para la carga de remesas en SigerWeb1.</strong>
   */
  public ArrayList<String> getCorreos() {
    return correos;
  }

  public void setCorreos(ArrayList<String> correos) {
    this.correos = correos;
  }

  /**
   * @return {@code telsAdicionales} ArrayList con los hasta 2 registros de 
   * teléfonos de contacto adicionales del deudor, correspondientes a las 
   * columnas 42 y 43 en el archivo estándar de excel para la carga de remesas 
   * en SigerWeb1.
   */
  public ArrayList<String> getTelsAdicionales() {
    return telsAdicionales;
  }

  public void setTelsAdicionales(ArrayList<String> telsAdicionales) {
    this.telsAdicionales = telsAdicionales;
  }

  /**
   * @return {@code correos} ArrayList que contiene las direcciones adicionales
   * del deudor. <strong>Sólo existe una columna (la 44) en el archivo estándar 
   * de excel para la carga de remesas en SigerWeb1.</strong>
   */
  public ArrayList<String> getDirecsAdicionales() {
    return direcsAdicionales;
  }

  public void setDirecsAdicionales(ArrayList<String> direcsAdicionales) {
    this.direcsAdicionales = direcsAdicionales;
  }

  /**
   * @return {@code marcaje} el campo de la columna 46 en el archivo estándar 
   * de excel para la carga de remesas en SigerWeb1.
   */
  public String getMarcaje() {
    return marcaje;
  }

  public void setMarcaje(String marcaje) {
    this.marcaje = marcaje;
  }

  /**
   * @return {@code fechaQuebranto} el campo de la columna 47 en el archivo 
   * estándar de excel para la carga de remesas en SigerWeb1
   */
  public String getFechaQuebranto() {
    return fechaQuebranto;
  }

  public void setFechaQuebranto(String fechaQuebranto) {
    this.fechaQuebranto = fechaQuebranto;
  }

}
