/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.tablas;

import java.util.Date;

/**
 *
 * @author Eduardo
 */
public class Devolucions implements java.io.Serializable {

  private Date fecha;
  private String estatus;
  private String numeroCredito;
  private String nombreRazonSocial;
  private String concepto;
  private String solicitante;
  private String revisor;

  public Devolucions() {
  }

  public Date getFecha() {
    return fecha;
  }

  public void setFecha(Date fecha) {
    this.fecha = fecha;
  }

  public String getEstatus() {
    return estatus;
  }

  public void setEstatus(String estatus) {
    this.estatus = estatus;
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

  public String getConcepto() {
    return concepto;
  }

  public void setConcepto(String concepto) {
    this.concepto = concepto;
  }

  public String getSolicitante() {
    return solicitante;
  }

  public void setSolicitante(String solicitante) {
    this.solicitante = solicitante;
  }

  public String getRevisor() {
    return revisor;
  }

  public void setRevisor(String revisor) {
    this.revisor = revisor;
  }

}
