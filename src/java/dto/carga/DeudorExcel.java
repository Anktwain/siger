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
public class DeudorExcel {
  
  // ***************************************************************************
  // VARIABLES DE CLASE
  // ***************************************************************************
  private String nombreDeudor;
  private String numeroCliente;
  private String rfc;

  // ***************************************************************************
  // CONSTRUCTOR
  // ***************************************************************************
  public DeudorExcel() {
  }
  
  // ***************************************************************************
  // GETTERS & SETTERS
  // ***************************************************************************
  public String getNombreDeudor() {
    return nombreDeudor;
  }

  public void setNombreDeudor(String nombreDeudor) {
    this.nombreDeudor = nombreDeudor;
  }

  public String getNumeroCliente() {
    return numeroCliente;
  }

  public void setNumeroCliente(String numeroCliente) {
    this.numeroCliente = numeroCliente;
  }

  public String getRfc() {
    return rfc;
  }

  public void setRfc(String rfc) {
    this.rfc = rfc;
  }
  
}
