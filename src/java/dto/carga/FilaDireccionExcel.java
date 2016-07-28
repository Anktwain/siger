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
// REESTRUCTURACION DEL MODULO DE CARGA
public class FilaDireccionExcel {

  // VARIABLES DE CLASE
  private String numeroCredito;
  private String calle;
  private String exterior;
  private String interior;
  private String colonia;
  private String cp;
  private String municipio;
  private String estado;

  // CONSTRUCTOR
  public FilaDireccionExcel() {
    numeroCredito = null;
    calle = null;
    exterior = null;
    interior = null;
    colonia = null;
    cp = null;
    municipio = null;
    estado = null;
  }

  // GETTERS & SETTERS
  public String getNumeroCredito() {
    return numeroCredito;
  }

  public void setNumeroCredito(String numeroCredito) {
    this.numeroCredito = numeroCredito;
  }

  public String getCalle() {
    return calle;
  }

  public void setCalle(String calle) {
    this.calle = calle;
  }

  public String getExterior() {
    return exterior;
  }

  public void setExterior(String exterior) {
    this.exterior = exterior;
  }

  public String getInterior() {
    return interior;
  }

  public void setInterior(String interior) {
    this.interior = interior;
  }

  public String getColonia() {
    return colonia;
  }

  public void setColonia(String colonia) {
    this.colonia = colonia;
  }

  public String getCp() {
    return cp;
  }

  public void setCp(String cp) {
    this.cp = cp;
  }

  public String getMunicipio() {
    return municipio;
  }

  public void setMunicipio(String municipio) {
    this.municipio = municipio;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }
  
}
