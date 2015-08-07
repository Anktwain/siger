/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.ContactoDAO;
import dao.SujetoDAO;
import dto.Cliente;
import dto.Contacto;
import dto.Sujeto;
import impl.ContactoIMPL;
import impl.SujetoIMPL;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import util.constantes.Sujetos;
import util.log.Logs;

/**
 *
 * @author brionvega
 */
@ManagedBean
@ViewScoped
public class ContactoBean implements Serializable {

  // Objeto Contacto, sus propiedades y acceso a la BD
  private Contacto contacto;
  private Sujeto sujeto;

  private String observaciones;

  private String nombreRazonSocial;
  private String rfc;
  private int eliminado;

  private ContactoDAO contactoDao;
  private SujetoDAO sujetoDao;

  // Otros beans
  @ManagedProperty(value = "#{telefonoBean}")
  private TelefonoBean telefonoBean;
  @ManagedProperty(value = "#{emailBean}")
  private EmailBean emailBean;
  @ManagedProperty(value = "#{direccionBean}")
  private DireccionBean direccionBean;

  // Controles de la vista
  private boolean btnGuardarContactoDisabled;

  // Construyendo...
  public ContactoBean() {
    contacto = new Contacto();
    contactoDao = new ContactoIMPL();
    sujeto = new Sujeto();
    sujetoDao = new SujetoIMPL();
    eliminado = Sujetos.ACTIVO;
    btnGuardarContactoDisabled = false;
  }

  public void agregarTelefono() {
    if (sujeto.getIdSujeto() == null) {
      FacesContext context = FacesContext.getCurrentInstance();
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se puede agregar un nuevo teléfono",
              "Antes debe agregar un nuevo contacto."));
    } else {
      telefonoBean.agregar(sujeto);
    }
  }

  public void agregarEmail() {
    if (sujeto.getIdSujeto() == null) {
      FacesContext context = FacesContext.getCurrentInstance();
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se puede agregar un nuevo e-mail",
              "Antes debe agregar un nuevo contacto."));
    } else {
      emailBean.agregar(sujeto);
    }
  }

  public void agregarDireccion() {
    if (sujeto.getIdSujeto() == null) {
      FacesContext context = FacesContext.getCurrentInstance();
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se puede agregar una nueva dirección",
              "Antes debe agregar un nuevo contacto."));
    } else {
      direccionBean.agregar(sujeto);
    }
  }

  // Agregar un nuevo contacto
  public void agregar(Cliente cliente) {
    // En primera instancia agrega el Sujeto
    int idSujeto = agregarSujeto();

    if (idSujeto > 0) {
      Logs.log.info("Se agregó objeto: Sujeto; con id = " + idSujeto);
      if (agregarContacto(cliente)) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Operación Exitosa",
                "Se agregó un nuevo contacto: " + nombreRazonSocial));
        limpiarEntradas();
        Logs.log.info("Se agregó objeto: Contacto; asociado a Sujeto: " + idSujeto);
        deshabilitarBtnGuardarContacto();
      } else {
        sujetoDao.eliminarEnSerio(sujeto);
        Logs.log.error("No se pudo agregar objeto: Contacto");
      }
    } else {
      Logs.log.error("No se pudo agregar objeto: Sujeto.");
    }
  }

  private int agregarSujeto() {
    sujeto.setNombreRazonSocial(nombreRazonSocial);
    sujeto.setRfc(rfc);
    sujeto.setEliminado(eliminado);

    return sujetoDao.insertar(sujeto);
  }

  private boolean agregarContacto(Cliente cliente) {
    contacto.setObservaciones(observaciones);
    contacto.setCliente(cliente);
    contacto.setSujeto(sujeto);

    return contactoDao.insertar(contacto);
  }

  private void limpiarEntradas() {
    observaciones = null;
    nombreRazonSocial = null;
    rfc = null;
  }

  private void deshabilitarBtnGuardarContacto() {
    btnGuardarContactoDisabled = true;
  }

  private void habilitarBtnGuardarContacto() {
    btnGuardarContactoDisabled = false;
  }

  public boolean nombreEsValido() {
    return (nombreRazonSocial != null) && (!nombreRazonSocial.equals(""))
            && (nombreRazonSocial.length() <= Sujetos.LONGITUD_NOMBRE)
            && (!nombreRazonSocial.matches("[.*\\s*]*"));
  }

  public void nuevoContacto() {
    sujeto = new Sujeto();
    limpiarEntradas();
    habilitarBtnGuardarContacto();
  }

  public Sujeto getSujetoContacto() {
    return sujeto;
  }

  public void setSujetoContacto(Sujeto sujeto) {
    this.sujeto = sujeto;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getNombreRazonSocial() {
    return nombreRazonSocial;
  }

  public void setNombreRazonSocial(String nombreRazonSocial) {
    this.nombreRazonSocial = nombreRazonSocial;
  }

  public String getRfc() {
    return rfc;
  }

  public void setRfc(String rfc) {
    this.rfc = rfc;
  }

  public int getEliminado() {
    return eliminado;
  }

  public void setEliminado(int eliminado) {
    this.eliminado = eliminado;
  }

  public boolean isBtnGuardarContactoDisabled() {
    return btnGuardarContactoDisabled;
  }

  public void setBtnGuardarContactoDisabled(boolean btnGuardarContactoDisabled) {
    this.btnGuardarContactoDisabled = btnGuardarContactoDisabled;
  }

  public TelefonoBean getTelefonoBean() {
    return telefonoBean;
  }

  public void setTelefonoBean(TelefonoBean telefonoBean) {
    this.telefonoBean = telefonoBean;
  }

  public EmailBean getEmailBean() {
    return emailBean;
  }

  public void setEmailBean(EmailBean emailBean) {
    this.emailBean = emailBean;
  }

  public DireccionBean getDireccionBean() {
    return direccionBean;
  }

  public void setDireccionBean(DireccionBean direccionBean) {
    this.direccionBean = direccionBean;
  }

}
