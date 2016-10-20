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
// 2A REESTRUCTURACION DEL MODULO DE CARGA
public class ContactoExcel {

  // ***************************************************************************
  // VARIABLES DE CLASE
  // ***************************************************************************
  private String nombre;
  private String tipo;
  
  // ***************************************************************************
  // CONSTRUCTOR
  // ***************************************************************************
  public ContactoExcel() {
  }
  
  // ***************************************************************************
  // GETTERS & SETTERS
  // ***************************************************************************
  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }
  
}
