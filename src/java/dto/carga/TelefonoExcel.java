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
public class TelefonoExcel {

  // ***************************************************************************
  // VARIABLES DE CLASE
  // ***************************************************************************
  private String telefono;
  private String tipo;
  private String horario;
  private int principal;

  // ***************************************************************************
  // CONSTRUCTOR
  // ***************************************************************************
  public TelefonoExcel() {

  }

  // ***************************************************************************
  // GETTERS & SETTERS
  // ***************************************************************************
  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getHorario() {
    return horario;
  }

  public void setHorario(String horario) {
    this.horario = horario;
  }

  public int getPrincipal() {
    return principal;
  }

  public void setPrincipal(int principal) {
    this.principal = principal;
  }

}
