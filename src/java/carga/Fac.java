/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package carga;

import java.io.Serializable;

/**
 *
 * @author antonio
 */
public class Fac implements Serializable{
  private int mes;
  private int anio;
  private String facPor;
  private String facMes;

  @Override
  public String toString() {
    return "Fac{" + "mes=" + mes + ", anio=" + anio + ", facPor=" + facPor + ", facMes=" + facMes + '}';
  }

  public int getMes() {
    return mes;
  }

  public void setMes(int mes) {
    this.mes = mes;
  }

  public int getAnio() {
    return anio;
  }

  public void setAnio(int anio) {
    this.anio = anio;
  }

  public String getFacPor() {
    return facPor;
  }

  public void setFacPor(String facPor) {
    this.facPor = facPor;
  }

  public String getFacMes() {
    return facMes;
  }

  public void setFacMes(String facMes) {
    this.facMes = facMes;
  }
  
  
}
