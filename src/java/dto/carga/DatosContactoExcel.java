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
public class DatosContactoExcel {

  // ***************************************************************************
  // VARIABLES DE CLASE
  // ***************************************************************************
  private DireccionExcel direccion;
  private TelefonoExcel telefono;
  private ContactoExcel contacto;

  // ***************************************************************************
  // CONSTRUCTOR
  // ***************************************************************************
  public DatosContactoExcel() {
  }

  // ***************************************************************************
  // GETTERS & SETTERS
  // ***************************************************************************
  public DireccionExcel getDireccion() {
    return direccion;
  }

  public void setDireccion(DireccionExcel direccion) {
    this.direccion = direccion;
  }

  public TelefonoExcel getTelefono() {
    return telefono;
  }

  public void setTelefono(TelefonoExcel telefono) {
    this.telefono = telefono;
  }

  public ContactoExcel getContacto() {
    return contacto;
  }

  public void setContacto(ContactoExcel contacto) {
    this.contacto = contacto;
  }

}
