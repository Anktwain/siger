/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.carga;

import dto.Credito;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eduardo
 */
// REESTRUCTURACION DEL MODULO DE CARGA
public class FilaCreditoClasificado {

  // VARIABLES DE CLASE
  private int creditosEnGestion;
  private List<FilaCreditoExcel> reasignados;
  private List<FilaCreditoExcel> conservados;
  private List<FilaCreditoExcel> nuevosCreditos;
  private List<FilaCreditoExcel> nuevosTotales;
  private List<Credito> retirados;

  // CONSTRUCTOR
  public FilaCreditoClasificado() {
    reasignados = new ArrayList();
    conservados = new ArrayList();
    retirados = new ArrayList();
    nuevosCreditos = new ArrayList();
    nuevosTotales = new ArrayList();
  }
  
  public int getCreditosEnGestion() {
    return creditosEnGestion;
  }

  // GETTERS & SETTERS
  public void setCreditosEnGestion(int creditosEnGestion) {  
    this.creditosEnGestion = creditosEnGestion;
  }

  public List<FilaCreditoExcel> getReasignados() {
      return reasignados;
  }

  public void setReasignados(List<FilaCreditoExcel> reasignados) {
    this.reasignados = reasignados;
  }

  public List<FilaCreditoExcel> getConservados() {
    return conservados;
  }

  public void setConservados(List<FilaCreditoExcel> conservados) {
    this.conservados = conservados;
  }

  public List<FilaCreditoExcel> getNuevosCreditos() {
    return nuevosCreditos;
  }

  public void setNuevosCreditos(List<FilaCreditoExcel> nuevosCreditos) {
    this.nuevosCreditos = nuevosCreditos;
  }

  public List<FilaCreditoExcel> getNuevosTotales() {
    return nuevosTotales;
  }

  public void setNuevosTotales(List<FilaCreditoExcel> nuevosTotales) {
    this.nuevosTotales = nuevosTotales;
  }

  public List<Credito> getRetirados() {
    return retirados;
  }

  public void setRetirados(List<Credito> retirados) {
    this.retirados = retirados;
  }
  

}
