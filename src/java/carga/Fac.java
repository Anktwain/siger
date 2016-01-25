package carga;

import java.io.Serializable;

/** 
 * Clase que representa un Fac con su respectiva informacion. En cada remesa se
 * incluyen datos de meses previos, se incluyen generalmente tres o más de estos
 * "Fac". 
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
