/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.tablas;

/**
 *
 * @author Eduardo
 */
public class Direcciones implements java.io.Serializable {
  
  private String calleNumero;
  private String colonia;
  private String cp;
  private String municipio;
  private String estado;

  public Direcciones() {
  }

  public String getCalleNumero() {
    return calleNumero;
  }

  public void setCalleNumero(String calleNumero) {
    this.calleNumero = calleNumero;
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
