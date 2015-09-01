/*
 * Esta clase va unida a la vista clientes.xhtml
 * Se muestran las variables necesarias para interactuar con esa vista...
 */
package beans;

import dto.Cliente;
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
import org.primefaces.event.SelectEvent;
import util.constantes.Sujetos;

/**
 *
 * @author antonio
 */
@ManagedBean
@ViewScoped
public class VistaClientes implements Serializable {

  // Datos seleccionados de alguna tabla
  private Cliente clienteActual;
  private Contacto contactoActual;
  private Sujeto sujetoActual;
  private Telefono telefonoActual;
  private Email emailActual;
  private Direccion direccionlActual;

  // Listas para desplegarse en las tablas
  private List<Cliente> listaClientes;
  private List<Telefono> listaTelefonos;
  private List<Email> listaEmails;
  private List<Direccion> listaDirecciones;
  private List<Contacto> listaContactos;

  // Activar/Desactivar controles de la vista
  private boolean btnAltaClienteDisabled;
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
  @ManagedProperty(value = "#{clienteBean}")
  private ClienteBean clienteBean;
  @ManagedProperty(value = "#{contactoBean}")
  private ContactoBean contactoBean;
  @ManagedProperty(value = "#{telefonoBean}")
  private TelefonoBean telefonoBean;
  @ManagedProperty(value = "#{emailBean}")
  private EmailBean emailBean;
  @ManagedProperty(value = "#{direccionBean}")
  private DireccionBean direccionBean;

  // Construyendo...
  public VistaClientes() {
    clienteActual = new Cliente();
    contactoActual = new Contacto();
    sujetoActual = new Sujeto();
    telefonoActual = new Telefono();
    emailActual = new Email();
    direccionlActual = new Direccion();

    btnAltaClienteDisabled = false;
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
  public void listarClientes() {
    listaClientes = clienteBean.listar();

    if (listaClientes == null) {
      FacesContext context = FacesContext.getCurrentInstance();
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se pudo obtener la lista de clientes",
              "Comunique esta situación a Soporte Técnico"));
    }
  }

  // EVENTOS DE LA VISTA
  public void onRowClienteSelect(SelectEvent evento) {
    tipoSujetoActual = Sujetos.CLIENTE;
    clienteActual = (Cliente) evento.getObject();
    listaTelefonos = telefonoBean.listar(clienteActual.getSujeto().getIdSujeto());
    listaEmails = emailBean.listar(clienteActual.getSujeto().getIdSujeto());
    listaDirecciones = direccionBean.listar(clienteActual.getSujeto().getIdSujeto());
    listaContactos = contactoBean.listar(clienteActual.getSujeto().getIdSujeto());
  }

  // OTROS MÉTODOS
  public void terminarProceso() {
    inicializarCliente();
    telefonoBean.resetAtributos();
    emailBean.resetAtributos();
    direccionBean.resetAtributos();
    clienteActual = new Cliente();
    contactoActual = new Contacto();
  }

  public void inicializarCliente() {
    clienteBean.getSujetoBean().resetAtributos();
    clienteBean.resetAtributos();
    setBtnAltaClienteDisabled(false);
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

  // Gestión de clientes
  // Gestión de Teléfonos de Clientes
  // Gestión de E-mails de Clientes
  // Gestión de Direcciones de Clientes
  // Gestión de Contactos de Clientes
  // Gestión de Teléfonos de Contactos
  // Gestión de E-mails de Contactos
  // Gestión de Direcciones de Contactos
  // Gestión de Sujetos
  // GESTIÓN DE CLIENTES
  public void agregarCliente() {
    FacesContext context = FacesContext.getCurrentInstance();
    clienteActual = clienteBean.insertar();

    if (clienteActual == null) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se pudo agregar el nuevo Cliente",
              "Reporte esta situación a Soporte Técnico"));
    } else {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
              "Se agregó un nuevo Cliente:",
              clienteActual.getSujeto().getNombreRazonSocial()));
//      clienteBean.resetAtributos();
//      clienteBean.getSujetoBean().resetAtributos();
      setBtnAltaClienteDisabled(true);
      setTxtCurpDisabled(true);
      setTxtNcteDisabled(true);
      setTxtNombreDisabled(true);
      setTxtNssDisabled(true);
      setTxtRfcDisabled(true);
      listarClientes();
      direccionBean.listarEstados();
    }
  }

  public void editarCliente() {
  }

  public void eliminarCliente() {
  }

  public void agregarContacto() {
    FacesContext context = FacesContext.getCurrentInstance();

    // Si no se ha dado de alta un cliente:
    if (clienteActual.getSujeto() == null) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se puede agregar el Contacto",
              "Primero debe agregar un nuevo Cliente"));
      contactoBean.resetAtributos();

    } else {
      System.out.println("Cliente actual: " + clienteActual.getSujeto().getNombreRazonSocial()); // BÓRRAME...............
      contactoActual = contactoBean.insertar(clienteActual);
      if (contactoActual == null) {
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "No se pudo agregar el nuevo Contacto",
                "Reporte esta situación a Soporte Técnico"));
        contactoBean.resetAtributos();
      } else {
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Se agregó un nuevo Contacto:",
                contactoActual.getSujeto().getNombreRazonSocial() + "\n" + clienteActual.getSujeto().getNombreRazonSocial()));
        setBtnAltaContactoDisabled(true);
        setTxtNombreContactoDisabled(true);
        setTxtRfcContactoDisabled(true);
        setTxtObservacionesDisabled(true);
        System.out.println("Cliente actual: " + clienteActual.getSujeto().getNombreRazonSocial()); // BÓRRAME...............
      }
    }
  }

  public void editarContacto() {
  }

  public void eliminarContacto() {
  }

  public void agregarDireccionCliente() {
    FacesContext context = FacesContext.getCurrentInstance();

    // Si no se ha dado de alta un cliente:
    if (clienteActual.getSujeto() == null) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se puede agregar la Dirección",
              "Primero debe agregar un nuevo Cliente"));
      direccionBean.resetAtributos();
    } else {
      agregarDireccion(clienteActual.getSujeto());
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

  public void agregarTelefonoCliente() {
    FacesContext context = FacesContext.getCurrentInstance();

    // Si no se ha dado de alta un cliente:
    if (clienteActual.getSujeto() == null) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se puede agregar el Teléfono",
              "Primero debe agregar un nuevo Cliente"));
      telefonoBean.resetAtributos();
    } else {
      agregarTelefono(clienteActual.getSujeto());
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

  public void agregarEmailCliente() {
    FacesContext context = FacesContext.getCurrentInstance();

    // Si no se ha dado de alta un cliente:
    if (clienteActual.getSujeto() == null) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se puede agregar E-mail",
              "Primero debe agregar un nuevo Cliente"));
      emailBean.resetAtributos();
    } else {
      agregarEmail(clienteActual.getSujeto());
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
  public Cliente getClienteActual() {
    return clienteActual;
  }

  public void setClienteActual(Cliente clienteActual) {
    this.clienteActual = clienteActual;
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

  public List<Cliente> getListaClientes() {
    return listaClientes;
  }

  public void setListaClientes(List<Cliente> listaClientes) {
    this.listaClientes = listaClientes;
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

  public boolean isBtnAltaClienteDisabled() {
    return btnAltaClienteDisabled;
  }

  public void setBtnAltaClienteDisabled(boolean btnAltaClienteDisabled) {
    this.btnAltaClienteDisabled = btnAltaClienteDisabled;
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

  public ClienteBean getClienteBean() {
    return clienteBean;
  }

  public void setClienteBean(ClienteBean clienteBean) {
    this.clienteBean = clienteBean;
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
