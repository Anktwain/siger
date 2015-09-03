/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.ContactoDAO;
import dto.Cliente;
import dto.Contacto;
import dto.Sujeto;
import impl.ContactoIMPL;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import util.log.Logs;

/**
 *
 * @author brionvega
 */
@ManagedBean
@ViewScoped
public class ContactoBean implements Serializable {

  // Objeto que gestiona este bean
  private Contacto contacto;

  // Atributos del objeto Contacto
  private String observaciones;
  private Sujeto sujeto;

  // Acceso a la BD
  private ContactoDAO contactoDao;

  // Otros beans
  @ManagedProperty(value = "#{sujetoBean}")
  private SujetoBean sujetoBean;

  // Construyendo...
  public ContactoBean() {
    contacto = new Contacto();
    contactoDao = new ContactoIMPL();
  }

  // GESTIÓN DE CONTACTOS
  public List<Contacto> listar() {
    return contactoDao.buscarTodo();
  }

  public List<Contacto> listar(int idSujeto) {
    return contactoDao.buscarPorSujeto(idSujeto);
  }

  public Contacto insertar(Cliente cliente) {
    if (cliente.getIdCliente() == null) {
      Logs.log.error("El método ContactoBean.insertar(cliente) recibe un cliente null");
      return null;
    } else {
      System.out.println("Cliente actual(ContactoBean.insertar): " + cliente.getSujeto().getNombreRazonSocial()); // BÓRRAME...............
      // Crea el sujeto asociado al Contacto
      sujeto = sujetoBean.insertar();
      System.out.println("Cliente actual(ContactoBean.insertar), después de insertar sujeto contacto: " + cliente.getSujeto().getNombreRazonSocial()); // BÓRRAME...............
      // Verifica que el sujeto se haya creado correctamente
      if (sujeto == null) {
        Logs.log.error("El método ContactoBean.insertar(cliente) recibe un sujeto null");
        return null;
      } else {
        // Crea el objeto Contacto
        contacto.setObservaciones(observaciones);
        contacto.setCliente(cliente);
        contacto.setSujeto(sujeto);

        return contactoDao.insertar(contacto);
      }
    }
  }

  // MÉTODOS AUXILIARES
  public void resetAtributos() {
    observaciones = null;
    sujeto = new Sujeto();
  }

  // SETTERS & GETTERS

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public Sujeto getSujeto() {
    return sujeto;
  }

  public void setSujeto(Sujeto sujeto) {
    this.sujeto = sujeto;
  }

  public SujetoBean getSujetoBean() {
    return sujetoBean;
  }

  public void setSujetoBean(SujetoBean sujetoBean) {
    this.sujetoBean = sujetoBean;
  }

}
