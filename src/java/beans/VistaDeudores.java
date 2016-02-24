/*
 * Esta clase va unida a la vista deudors.xhtml
 * Se muestran las variables necesarias para interactuar con esa vista...
 */
package beans;

import dto.Deudor;
import dto.Contacto;
import dto.Direccion;
import dto.Email;
import dto.Sujeto;
import dto.Telefono;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import util.constantes.Sujetos;

/**
 *
 * @author antonio
 */
@ManagedBean
@ViewScoped
public class VistaDeudores implements Serializable {

  // Datos seleccionados de alguna tabla
  private Deudor deudorActual;
  private Contacto contactoActual;
  private Sujeto sujetoActual;
  private Telefono telefonoActual;
  private Email emailActual;
  private Direccion direccionlActual;

  // Listas para desplegarse en las tablas
  private List<Deudor> listaDeudores;
  private List<Telefono> listaTelefonos;
  private List<Email> listaEmails;
  private List<Direccion> listaDirecciones;
  private List<Contacto> listaContactos;

  // Activar/Desactivar controles de la vista
  private boolean btnAltaDeudorDisabled;
  private boolean btnAltaContactoDisabled;
  private boolean txtNombreDisabled;
  private boolean txtRfcDisabled;
  private boolean txtCurpDisabled;
  private boolean txtNssDisabled;
  private boolean txtNcteDisabled;
  private boolean txtNombreContactoDisabled;
  private boolean txtRfcContactoDisabled;
  private boolean txtObservacionesDisabled;

  // Indica tipo sujeto seleccionado actualmente
  private int tipoSujetoActual;

  // Otros beans
  @ManagedProperty(value = "#{deudorBean}")
  private DeudorBean deudorBean;
  @ManagedProperty(value = "#{contactoBean}")
  private ContactoBean contactoBean;
  @ManagedProperty(value = "#{telefonoBean}")
  private TelefonoBean telefonoBean;
  @ManagedProperty(value = "#{emailBean}")
  private EmailBean emailBean;
  @ManagedProperty(value = "#{direccionBean}")
  private DireccionBean direccionBean;

  // Construyendo...
  public VistaDeudores() {
    deudorActual = new Deudor();
    contactoActual = new Contacto();
    sujetoActual = new Sujeto();
    telefonoActual = new Telefono();
    emailActual = new Email();
    direccionlActual = new Direccion();

    btnAltaDeudorDisabled = false;
    btnAltaContactoDisabled = false;
    txtCurpDisabled = false;
    txtNcteDisabled = false;
    txtNombreDisabled = false;
    txtNssDisabled = false;
    txtRfcDisabled = false;
    txtNombreContactoDisabled = false;
    txtRfcContactoDisabled = false;
    txtObservacionesDisabled = false;

    tipoSujetoActual = 0;

  }

  // Método que se llama inmediatamente después de crear el bean
  @PostConstruct
  public void listarDeudors() {
    listaDeudores = deudorBean.listar();

    if (listaDeudores == null) {
      FacesContext context = FacesContext.getCurrentInstance();
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se pudo obtener la lista de deudors",
              "Comunique esta situación a Soporte Técnico"));
    }
  }
  
  public boolean listaDeudoresMayorQue5(){
    return listaDeudores.size() > 5;
  }

  // EVENTOS DE LA VISTA
  public void onRowDeudorSelect(SelectEvent evento) {
    tipoSujetoActual = Sujetos.CLIENTE;
    deudorActual = (Deudor) evento.getObject();
    listaTelefonos = telefonoBean.listar(deudorActual.getSujeto().getIdSujeto());
    listaEmails = emailBean.listar(deudorActual.getSujeto().getIdSujeto());
    listaDirecciones = direccionBean.listar(deudorActual.getSujeto().getIdSujeto());
    listaContactos = contactoBean.listar(deudorActual.getSujeto().getIdSujeto());
  }
  
  public void onRowContactoSelect(SelectEvent evento) {
    tipoSujetoActual = Sujetos.CONTACTO;
    contactoActual = (Contacto) evento.getObject();
    listaTelefonos = telefonoBean.listar(contactoActual.getSujeto().getIdSujeto());
    listaEmails = emailBean.listar(contactoActual.getSujeto().getIdSujeto());
    listaDirecciones = direccionBean.listar(contactoActual.getSujeto().getIdSujeto());
  }
  
  public void onRowTelefonoSelect(SelectEvent evento) {
    telefonoActual = (Telefono) evento.getObject();
  }
  
  public void onRowEmailSelect(SelectEvent evento) {
    emailActual = (Email) evento.getObject();
  }
  
  public void onRowDireccionSelect(SelectEvent evento) {
    direccionlActual = (Direccion) evento.getObject();
  }

  // OTROS MÉTODOS
  public void terminarProceso() {
    inicializarDeudor();
    telefonoBean.resetAtributos();
    emailBean.resetAtributos();
    direccionBean.resetAtributos();
    deudorActual = new Deudor();
    contactoActual = new Contacto();
  }

  public void inicializarDeudor() {
    deudorBean.getSujetoBean().resetAtributos();
    deudorBean.resetAtributos();
    setBtnAltaDeudorDisabled(false);
    setTxtCurpDisabled(false);
    setTxtNcteDisabled(false);
    setTxtNombreDisabled(false);
    setTxtNssDisabled(false);
    setTxtRfcDisabled(false);
    inicializarContacto();
  }

  public void inicializarContacto() {
    contactoBean.getSujetoBean().resetAtributos();
    contactoBean.resetAtributos();
    setBtnAltaContactoDisabled(false);
    setTxtNombreContactoDisabled(false);
    setTxtRfcContactoDisabled(false);
    setTxtObservacionesDisabled(false);
  }

  // Gestión de deudors
  // Gestión de Teléfonos de Deudors
  // Gestión de E-mails de Deudors
  // Gestión de Direcciones de Deudors
  // Gestión de Contactos de Deudors
  // Gestión de Teléfonos de Contactos
  // Gestión de E-mails de Contactos
  // Gestión de Direcciones de Contactos
  // Gestión de Sujetos
  // GESTIÓN DE CLIENTES
  public void agregarDeudor() {
    FacesContext context = FacesContext.getCurrentInstance();
    deudorActual = deudorBean.insertar();

    if (deudorActual == null) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se pudo agregar el nuevo Deudor",
              "Reporte esta situación a Soporte Técnico"));
    } else {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
              "Se agregó un nuevo Deudor:",
              deudorActual.getSujeto().getNombreRazonSocial()));
//      deudorBean.resetAtributos();
//      deudorBean.getSujetoBean().resetAtributos();
      setBtnAltaDeudorDisabled(true);
      setTxtCurpDisabled(true);
      setTxtNcteDisabled(true);
      setTxtNombreDisabled(true);
      setTxtNssDisabled(true);
      setTxtRfcDisabled(true);
      listarDeudors();
      direccionBean.listarEstados();
    }
  }

  public void editarDeudor() {
  }

  public void eliminarDeudor() {
    FacesContext context = FacesContext.getCurrentInstance();
    
    /* Primero vamos a cerciorarnos de que existe un deudor al que se va a eliminar,
      se supone que ese objeto Deudor está almacenado en deudorActual, por lo tanto,
      verificamos:
    */
    if(deudorActual == null) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se pudo eliminar el Deudor",
              "Primero deberá seleccionar un deudor para ser eliminado"));
    } else {
      if(deudorBean.eliminar(deudorActual)) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
              "Se eliminó el Deudor: ",
              deudorActual.getSujeto().getNombreRazonSocial()));
      listaDeudores.remove(deudorActual);
        RequestContext.getCurrentInstance().execute("PF('dlgDetalleDeudor').hide();");
      } else {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se pudo eliminar el Deudor",
              "Reporte esta situación a Soporte Técnico"));
      }
    }
    
  }

  public void agregarContacto() {
    FacesContext context = FacesContext.getCurrentInstance();

    // Si no se ha dado de alta un deudor:
    if (deudorActual.getSujeto() == null) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se puede agregar el Contacto",
              "Primero debe agregar un nuevo Deudor"));
      contactoBean.resetAtributos();

    } else {
      contactoActual = contactoBean.insertar(deudorActual);
      if (contactoActual == null) {
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "No se pudo agregar el nuevo Contacto",
                "Reporte esta situación a Soporte Técnico"));
        contactoBean.resetAtributos();
      } else {
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Se agregó un nuevo Contacto:",
                contactoActual.getSujeto().getNombreRazonSocial() + "\n" + deudorActual.getSujeto().getNombreRazonSocial()));
        setBtnAltaContactoDisabled(true);
        setTxtNombreContactoDisabled(true);
        setTxtRfcContactoDisabled(true);
        setTxtObservacionesDisabled(true);
      }
    }
  }

  public void editarContacto() {
  }

  public void eliminarContacto() {
  }

  public void agregarDireccionDeudor() {
    FacesContext context = FacesContext.getCurrentInstance();

    // Si no se ha dado de alta un deudor:
    if (deudorActual.getSujeto() == null) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se puede agregar la Dirección",
              "Primero debe agregar un nuevo Deudor"));
      direccionBean.resetAtributos();
    } else {
      agregarDireccion(deudorActual.getSujeto());
    }
  }

  public void agregarDireccionContacto() {
    FacesContext context = FacesContext.getCurrentInstance();

    // Si no se ha dado de alta un contacto:
    if (contactoActual.getSujeto() == null) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se puede agregar la Dirección",
              "Primero debe agregar un nuevo Contacto"));
      direccionBean.resetAtributos();
    } else {
      agregarDireccion(contactoActual.getSujeto());
    }
  }

  private void agregarDireccion(Sujeto sujeto) {
    FacesContext context = FacesContext.getCurrentInstance();

    direccionlActual = direccionBean.insertar(sujeto);
    if (direccionlActual == null) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se pudo agregar la nueva Dirección",
              "Reporte esta situación a Soporte Técnico"));
    } else {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
              "Se agregó una nueva Dirección:",
              direccionlActual.getCalle() + "...\n" + sujeto.getNombreRazonSocial()));
      direccionBean.resetAtributos();

    }

  }

  public void editarDireccion() {
  }

  public void eliminarDireccion() {
  }

  public void agregarTelefonoDeudor() {
    FacesContext context = FacesContext.getCurrentInstance();

    // Si no se ha dado de alta un deudor:
    if (deudorActual.getSujeto() == null) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se puede agregar el Teléfono",
              "Primero debe agregar un nuevo Deudor"));
      telefonoBean.resetAtributos();
    } else {
      agregarTelefono(deudorActual.getSujeto());
    }
  }

  public void agregarTelefonoContacto() {
    FacesContext context = FacesContext.getCurrentInstance();

    // Si no se ha dado de alta un contacto:
    if (contactoActual.getSujeto() == null) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se puede agregar el Teléfono",
              "Primero debe agregar un nuevo Contacto"));
      telefonoBean.resetAtributos();
    } else {
      agregarTelefono(contactoActual.getSujeto());
    }
  }

  private void agregarTelefono(Sujeto sujeto) {
    FacesContext context = FacesContext.getCurrentInstance();

    telefonoActual = telefonoBean.insertar(sujeto);
    if (telefonoActual == null) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se pudo agregar el nuevo Teléfono",
              "Reporte esta situación a Soporte Técnico"));
    } else {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
              "Se agregó un nuevo Teléfono:",
              telefonoActual.getNumero() + "\n" + sujeto.getNombreRazonSocial()));
      telefonoBean.resetAtributos();

    }
  }

  public void editarTelefono() {
  }

  public void eliminarTelefono() {
  }

  public void agregarEmailDeudor() {
    FacesContext context = FacesContext.getCurrentInstance();

    // Si no se ha dado de alta un deudor:
    if (deudorActual.getSujeto() == null) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se puede agregar E-mail",
              "Primero debe agregar un nuevo Deudor"));
      emailBean.resetAtributos();
    } else {
      agregarEmail(deudorActual.getSujeto());
    }
  }

  public void agregarEmailContacto() {
    FacesContext context = FacesContext.getCurrentInstance();

    // Si no se ha dado de alta un contacto:
    if (contactoActual.getSujeto() == null) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se puede agregar E-mail",
              "Primero debe agregar un nuevo Contacto"));
      emailBean.resetAtributos();
    } else {
      agregarEmail(contactoActual.getSujeto());
    }
  }

  private void agregarEmail(Sujeto sujeto) {
    FacesContext context = FacesContext.getCurrentInstance();

    emailActual = emailBean.insertar(sujeto);
    if (emailActual == null) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se pudo agregar el nuevo E-mail",
              "Reporte esta situación a Soporte Técnico"));
    } else {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
              "Se agregó un nuevo E-mail:",
              emailActual.getDireccion() + "\n" + sujeto.getNombreRazonSocial()));
      emailBean.resetAtributos();

    }
  }

  public void editarEmail() {
  }

  public void eliminarEmail() {
  }

  // Setters & Getters
  public Deudor getDeudorActual() {
    return deudorActual;
  }

  public void setDeudorActual(Deudor deudorActual) {
    this.deudorActual = deudorActual;
  }

  public Contacto getContactoActual() {
    return contactoActual;
  }

  public void setContactoActual(Contacto contactoActual) {
    this.contactoActual = contactoActual;
  }

  public Sujeto getSujetoActual() {
    return sujetoActual;
  }

  public void setSujetoActual(Sujeto sujetoActual) {
    this.sujetoActual = sujetoActual;
  }

  public Telefono getTelefonoActual() {
    return telefonoActual;
  }

  public void setTelefonoActual(Telefono telefonoActual) {
    this.telefonoActual = telefonoActual;
  }

  public Email getEmailActual() {
    return emailActual;
  }

  public void setEmailActual(Email emailActual) {
    this.emailActual = emailActual;
  }

  public Direccion getDireccionlActual() {
    return direccionlActual;
  }

  public void setDireccionlActual(Direccion direccionlActual) {
    this.direccionlActual = direccionlActual;
  }

  public List<Deudor> getListaDeudores() {
    return listaDeudores;
  }

  public void setListaDeudores(List<Deudor> listaDeudores) {
    this.listaDeudores = listaDeudores;
  }

  public List<Telefono> getListaTelefonos() {
    return listaTelefonos;
  }

  public void setListaTelefonos(List<Telefono> listaTelefonos) {
    this.listaTelefonos = listaTelefonos;
  }

  public List<Email> getListaEmails() {
    return listaEmails;
  }

  public void setListaEmails(List<Email> listaEmails) {
    this.listaEmails = listaEmails;
  }

  public List<Direccion> getListaDirecciones() {
    return listaDirecciones;
  }

  public void setListaDirecciones(List<Direccion> listaDirecciones) {
    this.listaDirecciones = listaDirecciones;
  }

  public List<Contacto> getListaContactos() {
    return listaContactos;
  }

  public void setListaContactos(List<Contacto> listaContactos) {
    this.listaContactos = listaContactos;
  }

  public boolean isBtnAltaDeudorDisabled() {
    return btnAltaDeudorDisabled;
  }

  public void setBtnAltaDeudorDisabled(boolean btnAltaDeudorDisabled) {
    this.btnAltaDeudorDisabled = btnAltaDeudorDisabled;
  }

  public boolean isBtnAltaContactoDisabled() {
    return btnAltaContactoDisabled;
  }

  public void setBtnAltaContactoDisabled(boolean btnAltaContactoDisabled) {
    this.btnAltaContactoDisabled = btnAltaContactoDisabled;
  }

  public boolean isTxtNombreDisabled() {
    return txtNombreDisabled;
  }

  public void setTxtNombreDisabled(boolean txtNombreDisabled) {
    this.txtNombreDisabled = txtNombreDisabled;
  }

  public boolean isTxtRfcDisabled() {
    return txtRfcDisabled;
  }

  public void setTxtRfcDisabled(boolean txtRfcDisabled) {
    this.txtRfcDisabled = txtRfcDisabled;
  }

  public boolean isTxtCurpDisabled() {
    return txtCurpDisabled;
  }

  public void setTxtCurpDisabled(boolean txtCurpDisabled) {
    this.txtCurpDisabled = txtCurpDisabled;
  }

  public boolean isTxtNssDisabled() {
    return txtNssDisabled;
  }

  public void setTxtNssDisabled(boolean txtNssDisabled) {
    this.txtNssDisabled = txtNssDisabled;
  }

  public boolean isTxtNcteDisabled() {
    return txtNcteDisabled;
  }

  public void setTxtNcteDisabled(boolean txtNcteDisabled) {
    this.txtNcteDisabled = txtNcteDisabled;
  }

  public boolean isTxtNombreContactoDisabled() {
    return txtNombreContactoDisabled;
  }

  public void setTxtNombreContactoDisabled(boolean txtNombreContactoDisabled) {
    this.txtNombreContactoDisabled = txtNombreContactoDisabled;
  }

  public boolean isTxtRfcContactoDisabled() {
    return txtRfcContactoDisabled;
  }

  public void setTxtRfcContactoDisabled(boolean txtRfcContactoDisabled) {
    this.txtRfcContactoDisabled = txtRfcContactoDisabled;
  }

  public boolean isTxtObservacionesDisabled() {
    return txtObservacionesDisabled;
  }

  public void setTxtObservacionesDisabled(boolean txtObservacionesDisabled) {
    this.txtObservacionesDisabled = txtObservacionesDisabled;
  }

  public int getTipoSujetoActual() {
    return tipoSujetoActual;
  }

  public void setTipoSujetoActual(int tipoSujetoActual) {
    this.tipoSujetoActual = tipoSujetoActual;
  }

  public DeudorBean getDeudorBean() {
    return deudorBean;
  }

  public void setDeudorBean(DeudorBean deudorBean) {
    this.deudorBean = deudorBean;
  }

  public ContactoBean getContactoBean() {
    return contactoBean;
  }

  public void setContactoBean(ContactoBean contactoBean) {
    this.contactoBean = contactoBean;
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
