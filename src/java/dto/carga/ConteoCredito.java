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
public class ConteoCredito {
  
  // VARIABLES DE CLASE
  private String tipoCredito;
  private int cantidad;
  
  // CONSTRUCTOR
  public ConteoCredito() {
  }
  
  // GETTERS & SETTERS
  public String getTipoCredito() {
    return tipoCredito;
  }

  public void setTipoCredito(String tipoCredito) {
    this.tipoCredito = tipoCredito;
  }

  public int getCantidad() {
    return cantidad;
  }

  public void setCantidad(int cantidad) {
    this.cantidad = cantidad;
  }
  
  
}
