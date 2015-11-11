/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author Eduardo
 */
public class Cred {
  
  private String numeroCredito;
  private String nombreRazonSocial;
  private String nombreCortoInstitucion;
  private String tipoCredito;
  private String nombreProducto;
  private float saldoVencido;
  private String gestorAsignado;

  public Cred() {
  }

  public String getNumeroCredito() {
    return numeroCredito;
  }

  public void setNumeroCredito(String numeroCredito) {
    this.numeroCredito = numeroCredito;
  }

  public String getNombreRazonSocial() {
    return nombreRazonSocial;
  }

  public void setNombreRazonSocial(String nombreRazonSocial) {
    this.nombreRazonSocial = nombreRazonSocial;
  }

  public String getNombreCortoInstitucion() {
    return nombreCortoInstitucion;
  }

  public void setNombreCortoInstitucion(String nombreCortoInstitucion) {
    this.nombreCortoInstitucion = nombreCortoInstitucion;
  }

  public String getTipoCredito() {
    return tipoCredito;
  }

  public void setTipoCredito(String tipoCredito) {
    this.tipoCredito = tipoCredito;
  }

  public String getNombreProducto() {
    return nombreProducto;
  }

  public void setNombreProducto(String nombreProducto) {
    this.nombreProducto = nombreProducto;
  }

  public float getSaldoVencido() {
    return saldoVencido;
  }

  public void setSaldoVencido(float saldoVencido) {
    this.saldoVencido = saldoVencido;
  }

  public String getGestorAsignado() {
    return gestorAsignado;
  }

  public void setGestorAsignado(String gestorAsignado) {
    this.gestorAsignado = gestorAsignado;
  }
  
}
