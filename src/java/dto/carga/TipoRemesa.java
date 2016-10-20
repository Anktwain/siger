/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.carga;

import util.constantes.TipoRemesas;

/**
 *
 * @author Eduardo
 */
public class TipoRemesa {

  // VARIABLES DE CLASE
  private int id;
  private String tipo;

  // CONSTRUCTOR
  public TipoRemesa() {
  }

  // METODO QUE ASIGNA EL TIPO DE REMESA SEGUN EL ID
  public TipoRemesa buscarTipo(int idTipo) {
    System.out.println(idTipo);
    TipoRemesa t = new TipoRemesa();
    switch (idTipo) {
      case TipoRemesas.ASIGNACION_MENSUAL:
        t.setId(TipoRemesas.ASIGNACION_MENSUAL);
        t.setTipo("Asignacion Mensual");
        break;
      case TipoRemesas.ASIGNACION_COMPLEMENTARIA:
        t.setId(TipoRemesas.ASIGNACION_COMPLEMENTARIA);
        t.setTipo("Asignacion Complementaria");
        break;
      case TipoRemesas.ASIGNACION_QUEBRANTO:
        t.setId(TipoRemesas.ASIGNACION_QUEBRANTO);
        t.setTipo("Asignacion Quebranto");
        break;
    }
    return t;
  }

  // GETTERS & SETTERS
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

}
