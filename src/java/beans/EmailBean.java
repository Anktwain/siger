/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.EmailDAO;
import dto.Email;
import dto.Sujeto;
import impl.EmailIMPL;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import util.log.Logs;

/**
 *
 * @author brionvega
 */
@ManagedBean
@ViewScoped
public class EmailBean implements Serializable {
// Objeto que gestiona este bean
  private Email email;
  
  // Atributos del objeto Email
  private String direccion;
  private String tipo;
  
  // Acceso a la BD
  private EmailDAO emailDao;
  
  // Construyendo...
  public EmailBean() {
    email = new Email();
    emailDao = new EmailIMPL();
  }
  
  // GESTIÓN DE EMAILS
  public Email insertar(Sujeto sujeto) {
    // Verfica que el sujeto sea válido
    if (sujeto.getIdSujeto() == null) {
      Logs.log.error("El método EmailBean.insertar(sujeto) recibe un sujeto null");
      return null;
    } else {
      // Crea el objeto Email
      email.setDireccion(direccion);
      email.setTipo(tipo);
      email.setSujeto(sujeto);

      return emailDao.insertar(email);
    }
  }
  
  // MÉTODOS AUXILIARES
  public void resetAtributos() {
    direccion = null;
    tipo = null;
  }
  
  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

}