/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.ClienteDAO;
import dao.SujetoDAO;
import dao.TelefonoDAO;
import dto.Cliente;
import dto.Direccion;
import dto.Email;
import dto.Sujeto;
import dto.Telefono;
import impl.ClienteIMPL;
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
import util.constantes.Sujetos;
import util.log.Logs;

/**
 *
 * @author brionvega
 */
@ManagedBean
@ViewScoped
public class ClienteBean implements Serializable {

  // Cliente y Sujeto, porque un cliente es un sujeto:
  private Cliente cliente;
  private Sujeto sujeto;

  // Clases para acceso a datos
  private ClienteDAO clienteDao;
  private SujetoDAO sujetoDao;

  // Atributos de cliente:
  private String numeroCliente;
  private String curp;
  private String numeroSeguroSocial;

  // Atributos de sujeto:
  private String nombreRazonSocial;
  private String rfc;
  private int eliminado;

  // Listas
  List<Cliente> listaDeClientes;
  List<Telefono> listaTelefonos;
  List<Direccion> listaDirecciones;
  List<Email> listaEmails;
  
  // Otros acceso a datos
  private TelefonoDAO telefonoDao;

  // Objetos seleccionados en la tabla
  private Cliente clienteSeleccionado;

  // Controles de la vista
  private boolean btnGuardarDeudorDisabled;

  // Otros beans
  @ManagedProperty(value = "#{telefonoBean}")
  private TelefonoBean telefonoBean;
  @ManagedProperty(value = "#{emailBean}")
  private EmailBean emailBean;
  @ManagedProperty(value = "#{direccionBean}")
  private DireccionBean direccionBean;
  @ManagedProperty(value = "#{contactoBean}")
  private ContactoBean contactoBean;

  // Construyendo...

  /**
   *
   */
    public ClienteBean() {
    cliente = new Cliente();
    clienteSeleccionado = new Cliente();
    clienteDao = new ClienteIMPL();
    sujeto = new Sujeto();
    sujetoDao = new SujetoIMPL();
    eliminado = Sujetos.ACTIVO;
    inicializarListaDeClientes();

    btnGuardarDeudorDisabled = false;
    
    telefonoDao = new TelefonoIMPL();
  }

  // Editar datos de un cliente

  /**
   *
   * @return
   */
    public boolean editar() {
    boolean ok = false;
    if (sujetoDao.editar(clienteSeleccionado.getSujeto())) {
      if (clienteDao.editar(clienteSeleccionado)) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Operación Exitosa",
                "Se actualizaron los datos del cliente: "
                + clienteSeleccionado.getSujeto().getNombreRazonSocial()));
        RequestContext.getCurrentInstance().execute("PF('dlgDetalle').hide();");
        ok = true;
      }
    } else {
      System.out.println("ERROR");
      ok = false;
    }
    return ok;
  }

  // Eliminar Cliente

  /**
   *
   */
    public void eliminar() {
    clienteSeleccionado.getSujeto().setEliminado(Sujetos.ELIMINADO);
    if (editar()) {
      listaDeClientes.remove(clienteSeleccionado);
    }
  }

  /**
   *
   */
  public void agregarTelefono() {
    if (sujeto.getIdSujeto() == null) {
      FacesContext context = FacesContext.getCurrentInstance();
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se puede agregar un nuevo teléfono",
              "Antes debe agregar un nuevo deudor."));
    } else {
      telefonoBean.agregar(sujeto);
    }
  }

  /**
   *
   */
  public void agregarEmail() {
    if (sujeto.getIdSujeto() == null) {
      FacesContext context = FacesContext.getCurrentInstance();
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se puede agregar un nuevo e-mail",
              "Antes debe agregar un nuevo deudor."));
    } else {
      emailBean.agregar(sujeto);
    }
  }

  /**
   *
   */
  public void agregarDireccion() {
    if (sujeto.getIdSujeto() == null) {
      FacesContext context = FacesContext.getCurrentInstance();
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se puede agregar una nueva dirección",
              "Antes debe agregar un nuevo deudor."));
    } else {
      direccionBean.agregar(sujeto);
    }
  }

  /**
   *
   */
  public void agregarContacto() {
    if (sujeto.getIdSujeto() == null) {
      FacesContext context = FacesContext.getCurrentInstance();
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "No se puede agregar un nuevo contacto",
              "Antes debe agregar un nuevo deudor."));
    } else {
      contactoBean.agregar(cliente);
    }
  }

  // Agregar un nuevo cliente

  /**
   *
   */
    public void agregar() {
    // Primero agrega el sujeto, si se agregó correctamente entonces agrega cliente
    int idSujeto = agregarSujeto();

    if (idSujeto > 0) {
      Logs.log.info("Se agregó objeto: Sujeto; con id = " + idSujeto);
      if (agregarCliente()) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Operación Exitosa",
                "Se agregó un nuevo cliente: " + nombreRazonSocial));
        limpiarEntradas();
        inicializarListaDeClientes();
        Logs.log.info("Se agregó objeto: Cliente; asociado a Sujeto: " + idSujeto);
        //RequestContext.getCurrentInstance().execute("PF('dlgAgregarCliente').hide();");
        deshabilitarBtnGuardarDeudor();
      } else {
        sujetoDao.eliminarEnSerio(sujeto);
        Logs.log.error("No se pudo agregar objeto: Cliente");
      }
    } else {
      Logs.log.error("No se pudo agregar objeto: Sujeto.");
    }
  }

  private int agregarSujeto() {
    // primero agregará un sujeto...
    sujeto.setNombreRazonSocial(nombreRazonSocial);
    sujeto.setRfc(rfc);
    sujeto.setEliminado(eliminado);

    return sujetoDao.insertar(sujeto);

  }

  private boolean agregarCliente() {
    cliente.setNumeroCliente(numeroCliente);
    cliente.setCurp(curp);
    cliente.setNumeroSeguroSocial(numeroSeguroSocial);
    cliente.setSujeto(sujeto);

    return clienteDao.insertar(cliente);
  }

  private void limpiarEntradas() {
    numeroCliente = null;
    numeroSeguroSocial = null;
    curp = null;
    nombreRazonSocial = null;
    rfc = null;
  }

  /**
   *
   */
  public void nuevoCliente() {
    sujeto = new Sujeto();
    limpiarEntradas();
    habilitarBtnGuardarDeudor();
    contactoBean.nuevoContacto();
  }

  /**
   *
   */
  public void terminarProceso() {
    nuevoCliente();
    limpiarEntradas();
    telefonoBean.limpiarEntradas();
    emailBean.limpiarEntradas();
    direccionBean.limpiarEntradas();
    contactoBean.nuevoContacto();
    habilitarBtnGuardarDeudor();
  }

  /**
   *
   * @return
   */
  public boolean nombreEsValido() {
    return (nombreRazonSocial != null) && (!nombreRazonSocial.equals(""))
            && (nombreRazonSocial.length() <= Sujetos.LONGITUD_NOMBRE)
            && (!nombreRazonSocial.matches("[.*\\s*]*"));
  }

  private void deshabilitarBtnGuardarDeudor() {
    btnGuardarDeudorDisabled = true;
  }

  private void habilitarBtnGuardarDeudor() {
    btnGuardarDeudorDisabled = false;
  }

  /**
   *
   * @param evento
   */
  public void onRowSelect(SelectEvent evento) {
    clienteSeleccionado = (Cliente) evento.getObject();
    listaTelefonos = telefonoDao.buscarPorSujeto(clienteSeleccionado.getSujeto().getIdSujeto());
  }

  private void inicializarListaDeClientes() {
    listaDeClientes = clienteDao.buscarTodo();
  }

  /**
   *
   * @return
   */
  public String getNumeroCliente() {
    return numeroCliente;
  }

  /**
   *
   * @param numeroCliente
   */
  public void setNumeroCliente(String numeroCliente) {
    this.numeroCliente = numeroCliente;
  }

  /**
   *
   * @return
   */
  public String getCurp() {
    return curp;
  }

  /**
   *
   * @param curp
   */
  public void setCurp(String curp) {
    this.curp = curp;
  }

  /**
   *
   * @return
   */
  public String getNumeroSeguroSocial() {
    return numeroSeguroSocial;
  }

  /**
   *
   * @param numeroSeguroSocial
   */
  public void setNumeroSeguroSocial(String numeroSeguroSocial) {
    this.numeroSeguroSocial = numeroSeguroSocial;
  }

  /**
   *
   * @return
   */
  public String getNombreRazonSocial() {
    return nombreRazonSocial;
  }

  /**
   *
   * @param nombreRazonSocial
   */
  public void setNombreRazonSocial(String nombreRazonSocial) {
    this.nombreRazonSocial = nombreRazonSocial;
  }

  /**
   *
   * @return
   */
  public String getRfc() {
    return rfc;
  }

  /**
   *
   * @param rfc
   */
  public void setRfc(String rfc) {
    this.rfc = rfc;
  }

  /**
   *
   * @return
   */
  public int getEliminado() {
    return eliminado;
  }

  /**
   *
   * @param eliminado
   */
  public void setEliminado(int eliminado) {
    this.eliminado = eliminado;
  }

  /**
   *
   * @return
   */
  public List<Cliente> getListaDeClientes() {
    return listaDeClientes;
  }

  /**
   *
   * @param listaDeClientes
   */
  public void setListaDeClientes(List<Cliente> listaDeClientes) {
    this.listaDeClientes = listaDeClientes;
  }

  /**
   *
   * @return
   */
  public Cliente getClienteSeleccionado() {
    return clienteSeleccionado;
  }

  /**
   *
   * @param clienteSeleccionado
   */
  public void setClienteSeleccionado(Cliente clienteSeleccionado) {
    this.clienteSeleccionado = clienteSeleccionado;
  }

  /**
   *
   * @return
   */
  public boolean isBtnGuardarDeudorDisabled() {
    return btnGuardarDeudorDisabled;
  }

  /**
   *
   * @param btnGuardarDeudorDisabled
   */
  public void setBtnGuardarDeudorDisabled(boolean btnGuardarDeudorDisabled) {
    this.btnGuardarDeudorDisabled = btnGuardarDeudorDisabled;
  }

  /**
   *
   * @return
   */
  public TelefonoBean getTelefonoBean() {
    return telefonoBean;
  }

  /**
   *
   * @param telefonoBean
   */
  public void setTelefonoBean(TelefonoBean telefonoBean) {
    this.telefonoBean = telefonoBean;
  }

  /**
   *
   * @return
   */
  public EmailBean getEmailBean() {
    return emailBean;
  }

  /**
   *
   * @param emailBean
   */
  public void setEmailBean(EmailBean emailBean) {
    this.emailBean = emailBean;
  }

  /**
   *
   * @return
   */
  public DireccionBean getDireccionBean() {
    return direccionBean;
  }

  /**
   *
   * @param direccionBean
   */
  public void setDireccionBean(DireccionBean direccionBean) {
    this.direccionBean = direccionBean;
  }

  /**
   *
   * @return
   */
  public ContactoBean getContactoBean() {
    return contactoBean;
  }

  /**
   *
   * @param contactoBean
   */
  public void setContactoBean(ContactoBean contactoBean) {
    this.contactoBean = contactoBean;
  }

  /**
   *
   * @return
   */
  public List<Telefono> getListaTelefonos() {
    return listaTelefonos;
  }

  /**
   *
   * @param listaTelefonos
   */
  public void setListaTelefonos(List<Telefono> listaTelefonos) {
    this.listaTelefonos = listaTelefonos;
  }

  /**
   *
   * @return
   */
  public List<Direccion> getListaDirecciones() {
    return listaDirecciones;
  }

  /**
   *
   * @param listaDirecciones
   */
  public void setListaDirecciones(List<Direccion> listaDirecciones) {
    this.listaDirecciones = listaDirecciones;
  }

  /**
   *
   * @return
   */
  public List<Email> getListaEmails() {
    return listaEmails;
  }

  /**
   *
   * @param listaEmails
   */
  public void setListaEmails(List<Email> listaEmails) {
    this.listaEmails = listaEmails;
  }
  
  

}
