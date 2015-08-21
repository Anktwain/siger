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
public class VistaClientes implements Serializable{
  // Datos seleccionados de alguna tabla
  private Cliente clienteActual;
  private Contacto contactoActual;
  private Sujeto sujetoActual;
  private Telefono telefonoActual;
  private Email emailActual;
  private Direccion direccionlActual;
  
  // Listas para desplegarse en las tablas
  private List<Cliente> listaClientes;
  
  // Activar/Desactivar controles de la vista
  private boolean btnAltaClienteDisabled;
  
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
    
    tipoSujetoActual = 0;
    
  }
  
  // Método que se llama inmediatamente después de crear el bean
  @PostConstruct
  public void listarClientes() {
    listaClientes = clienteBean.listar();
    
    if(listaClientes == null) {
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
    
    if(clienteActual == null) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se pudo agregar el nuevo Cliente",
              "Reporte esta situación a Soporte Técnico"));
    } else {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
              "Se agregó un nuevo Cliente:",
              clienteActual.getSujeto().getNombreRazonSocial()));
      clienteBean.resetAtributos();
      clienteBean.getSujetoBean().resetAtributos();
      setBtnAltaClienteDisabled(true);
      listarClientes();
    }
  }
  
  public void editarCliente() {
  }
  
  public void eliminarCliente() {
  }
  


  public void agregarContacto() {
  }
  
  public void editarContacto() {
  }
  
  public void eliminarContacto() {
  }
  
  public void agregarDireccion() {
  }
  
  public void editarDireccion() {
  }
  
  public void eliminarDireccion() {
  }
  
  public void agregarTelefono() {
  }
  
  public void editarTelefono() {
  }
  
  public void eliminarTelefono() {
  }
  
  public void agregarEmail() {
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

  public boolean isBtnAltaClienteDisabled() {
    return btnAltaClienteDisabled;
  }

  public void setBtnAltaClienteDisabled(boolean btnAltaClienteDisabled) {
    this.btnAltaClienteDisabled = btnAltaClienteDisabled;
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
