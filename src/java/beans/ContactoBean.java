/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.ContactoDAO;
import dao.DireccionDAO;
import dao.EmailDAO;
import dao.SujetoDAO;
import dao.TelefonoDAO;
import dto.Cliente;
import dto.Contacto;
import dto.Direccion;
import dto.Email;
import dto.Sujeto;
import dto.Telefono;
import impl.ContactoIMPL;
import impl.DireccionIMPL;
import impl.EmailIMPL;
import impl.SujetoIMPL;
import impl.TelefonoIMPL;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import util.constantes.Perfiles;
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

  // Atributos de Contacto
  private String observaciones;

  // Atributos de Sujeto
  private String nombreRazonSocial;
  private String rfc;
  private int eliminado;

  // clases para acceso a datos
  private ContactoDAO contactoDao;
  private SujetoDAO sujetoDao;
  
  // Listas
  List<Contacto> listaDeContactos;
  List<Telefono> listaTelefonos;
  List<Direccion> listaDirecciones;
  List<Email> listaEmails;
  
  // Otros accesos a datos
  private TelefonoDAO telefonoDao;
  private EmailDAO emailDao;
  private DireccionDAO direccionDao;
  
  // Objetos seleccionados en la tabla
  private Telefono telefonoSeleccionado;
  private Email emailSeleccionado;
  private Direccion direccionSeleccionada;
  private Contacto contactoSeleccionado;  

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
    
    telefonoDao = new TelefonoIMPL();
    emailDao = new EmailIMPL();
    direccionDao = new DireccionIMPL();
  }
  
  public boolean editar(Contacto contacto) {
    boolean ok = false;
    FacesContext context = FacesContext.getCurrentInstance();
    if (sujetoDao.editar(contacto.getSujeto())) {
      if (contactoDao.editar(contacto)) {
        context.addMessage(null, new FacesMessage("Operación Exitosa",
                "Se actualizaron los datos del contacto: "
                + contacto.getSujeto().getNombreRazonSocial()));
        RequestContext.getCurrentInstance().execute("PF('dlgDetalleContacto').hide();");
        ok = true;
      }
    } else {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "Operación No Exitosa",
              "No se pudo eliminar el registro seleccionado."));
      ok = false;
    }
    return ok;
  }
  
  public boolean editarTelefono() {
    return telefonoBean.editar(telefonoSeleccionado);
  }

  public boolean eliminarTelefono() {
    if (telefonoBean.eliminar(telefonoSeleccionado)) {
      listaTelefonos.remove(telefonoSeleccionado);
      return true;
    }
    return false;
  }
  
  public boolean editarEmail() {
    return emailBean.editar(emailSeleccionado);
  }

  public boolean eliminarEmail() {
    if (emailBean.eliminar(emailSeleccionado)) {
      listaEmails.remove(emailSeleccionado);
      return true;
    }
    return false;
  }
  
  public boolean editarDireccion() {
    return direccionBean.editar(direccionSeleccionada);
  }

  public boolean eliminarDireccion() {
    if (direccionBean.eliminar(direccionSeleccionada)) {
      listaDirecciones.remove(direccionSeleccionada);
      return true;
    }
    return false;
  }  
    
  public boolean eliminar(Contacto contacto) {
    FacesContext context = FacesContext.getCurrentInstance();
    boolean ok = false;

    ok = contactoDao.eliminar(contacto);

    if (ok) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
              "Operación Exitosa",
              "Se eliminó el registro seleccionado"));
    } else {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "Operación Exitosa",
              "No se pudo eliminar el registro seleccionado."));
    }

    return ok;
  }

  public void agregarTelefono() {
    if (sujeto.getIdSujeto() == null) {
      FacesContext context = FacesContext.getCurrentInstance();
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se puede agregar un nuevo teléfono",
              "Antes debe agregar un nuevo contacto."));
    } else {
      telefonoBean.agregar(sujeto);
      RequestContext.getCurrentInstance().execute("PF('dlgOtroTelefonoParaContacto').hide();");
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
      RequestContext.getCurrentInstance().execute("PF('dlgOtroMailParaContacto').hide();");
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
      RequestContext.getCurrentInstance().execute("PF('dlgOtraDireccionParaContacto').hide();");
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
  
  public void onRowSelect(SelectEvent evento) {
    contactoSeleccionado = (Contacto) evento.getObject();
    sujeto = contactoSeleccionado.getSujeto();    
    listaTelefonos = telefonoDao.buscarPorSujeto(contactoSeleccionado.getSujeto().getIdSujeto());
    listaEmails = emailDao.buscarPorSujeto(contactoSeleccionado.getSujeto().getIdSujeto());
    listaDirecciones = direccionDao.buscarPorSujeto(contactoSeleccionado.getSujeto().getIdSujeto());
  }

  public void onRowSelectTel(SelectEvent evento) {
    telefonoSeleccionado = (Telefono) evento.getObject();
  }

  public void onRowSelectMail(SelectEvent evento) {
    emailSeleccionado = (Email) evento.getObject();
  }

  public void onRowSelectDir(SelectEvent evento) {
    direccionSeleccionada = (Direccion) evento.getObject();
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

  public List<Contacto> getListaDeContactos() {
    return listaDeContactos;
  }

  public void setListaDeContactos(List<Contacto> listaDeContactos) {
    this.listaDeContactos = listaDeContactos;
  }

  public List<Telefono> getListaTelefonos() {
    return listaTelefonos;
  }

  public void setListaTelefonos(List<Telefono> listaTelefonos) {
    this.listaTelefonos = listaTelefonos;
  }

  public List<Direccion> getListaDirecciones() {
    return listaDirecciones;
  }

  public void setListaDirecciones(List<Direccion> listaDirecciones) {
    this.listaDirecciones = listaDirecciones;
  }

  public List<Email> getListaEmails() {
    return listaEmails;
  }

  public void setListaEmails(List<Email> listaEmails) {
    this.listaEmails = listaEmails;
  }

  public Telefono getTelefonoSeleccionado() {
    return telefonoSeleccionado;
  }

  public void setTelefonoSeleccionado(Telefono telefonoSeleccionado) {
    this.telefonoSeleccionado = telefonoSeleccionado;
  }

  public Email getEmailSeleccionado() {
    return emailSeleccionado;
  }

  public void setEmailSeleccionado(Email emailSeleccionado) {
    this.emailSeleccionado = emailSeleccionado;
  }

  public Direccion getDireccionSeleccionada() {
    return direccionSeleccionada;
  }

  public void setDireccionSeleccionada(Direccion direccionSeleccionada) {
    this.direccionSeleccionada = direccionSeleccionada;
  }

  public Contacto getContactoSeleccionado() {
    return contactoSeleccionado;
  }

  public void setContactoSeleccionado(Contacto contactoSeleccionado) {
    this.contactoSeleccionado = contactoSeleccionado;
  }
  
  

}