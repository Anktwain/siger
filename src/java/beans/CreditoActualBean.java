/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dto.Credito;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "creditoActualBean")
@SessionScoped
public class CreditoActualBean implements Serializable {
  
  // VARIABLE DE CLASE
  private Credito creditoActual;
  
  // CONSTRUCTOR
  public CreditoActualBean() {
    creditoActual = new Credito();
  }
  
  //GETTER & SETTER
  public Credito getCreditoActual() {
    return creditoActual;
  }

  public void setCreditoActual(Credito creditoActual) {
    this.creditoActual = creditoActual;
  }
  
}
