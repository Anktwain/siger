/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "visitasBean")
@ViewScoped
public class VisitasBean {
  
  // VARIABLES DE CLASE
  private boolean periodoActivo;
  
  // CONSTRUCTOR

  public VisitasBean() {
    periodoActivo = false;
  }
    
  // METODO QUE VERIFICA SI EL PERIODO ESTA ACTIVO
  public void verificarPeriodo(){
    
  }
  
  // GETTERS & SETTERS

  public boolean isPeriodoActivo() {
    return periodoActivo;
  }

  public void setPeriodoActivo(boolean periodoActivo) {
    this.periodoActivo = periodoActivo;
  }
  
}
