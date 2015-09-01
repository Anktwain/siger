package beans;

import dto.Contacto;
import dto.Direccion;
import dto.Email;
import dto.Empresa;
import dto.Producto;
import dto.Subproducto;
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

/**
 *
 * @author Eduardo
 */

// ESTE BEAN SERA LA COMUNICACION ENTRE LA VISTA DE EMPRESAS Y LOS BEANS QUE PERMITAN INTERACTUAR CON LOS DAOS QUE INTERACTUAN CON LOS IMPLEMENTS QUE
// INTERACTUAN CON LA BASE DE DATOS

@ManagedBean(name = "vistaEmpresas")
@ViewScoped
public class VistaEmpresas implements Serializable {

  // objetos que se utilizaran unicamente en la vista
  private Empresa empresaSeleccionada;
  private Empresa nuevaEmpresa;
  private Sujeto sujetoSeleccionado;
  private Sujeto nuevoSujeto;
  private Producto nuevoProducto;
  private Producto productoSeleccionado;
  private Subproducto subproductoSeleccionado;
  private Subproducto nuevoSubproducto;
  private Contacto nuevoContacto;
  private Telefono nuevoTelefono;
  private Email nuevoEmail;
  private Direccion nuevaDireccion;
  
  // listas
  private List<Sujeto> listaEmpresas;
  private List<Producto> listaProductos;
  private List<Subproducto> listaSubproductos;
  
  // llamada a otros beans
  @ManagedProperty(value = "#{empresaBean}")
  private EmpresaBean empresaBean;
  @ManagedProperty(value = "#{sujetoBean}")
  private SujetoBean sujetoBean;
  @ManagedProperty(value = "#{contactoBean}")
  private ContactoBean contactoBean;
  @ManagedProperty(value = "#{telefonoBean}")
  private TelefonoBean telefonoBean;
  @ManagedProperty(value = "#{emailBean}")
  private EmailBean emailBean;
  @ManagedProperty(value = "#{direccionBean}")
  private DireccionBean direccionBean;
  
  // constructor
  public VistaEmpresas(){
    empresaSeleccionada = new Empresa();
    nuevaEmpresa = new Empresa();
    sujetoSeleccionado = new Sujeto();
    nuevoSujeto = new Sujeto();
    productoSeleccionado = new Producto();
    nuevoProducto = new Producto();
    subproductoSeleccionado = new Subproducto();
    nuevoSubproducto = new Subproducto();
    nuevoContacto = new Contacto();
    nuevoTelefono = new Telefono();
    nuevoEmail = new Email();
    nuevaDireccion = new Direccion();
  }
            
  // post constructor
  @PostConstruct
  public void buscarEmpresas(){
  listaEmpresas = empresaBean.buscarEmpresas();
  if (listaEmpresas == null) {
    FacesContext context = FacesContext.getCurrentInstance();
    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo obtener la lista de empresas registradas", "Comuniquese a Soporte Técnico"));
    }
  }
  
  // setters y getters
  
  public Empresa getEmpresaSeleccionada() {
    return empresaSeleccionada;
  }

  public void setEmpresaSeleccionada(Empresa empresaSeleccionada) {
    this.empresaSeleccionada = empresaSeleccionada;
  }

  public Empresa getNuevaEmpresa() {
    return nuevaEmpresa;
  }

  public void setNuevaEmpresa(Empresa nuevaEmpresa) {
    this.nuevaEmpresa = nuevaEmpresa;
  }

  public Sujeto getSujetoSeleccionado() {
    return sujetoSeleccionado;
  }

  public void setSujetoSeleccionado(Sujeto sujetoSeleccionado) {
    this.sujetoSeleccionado = sujetoSeleccionado;
  }

  public Sujeto getNuevoSujeto() {
    return nuevoSujeto;
  }

  public void setNuevoSujeto(Sujeto nuevoSujeto) {
    this.nuevoSujeto = nuevoSujeto;
  }

  public Producto getNuevoProducto() {
    return nuevoProducto;
  }

  public void setNuevoProducto(Producto nuevoProducto) {
    this.nuevoProducto = nuevoProducto;
  }

  public Producto getProductoSeleccionado() {
    return productoSeleccionado;
  }

  public void setProductoSeleccionado(Producto productoSeleccionado) {
    this.productoSeleccionado = productoSeleccionado;
  }

  public Subproducto getSubproductoSeleccionado() {
    return subproductoSeleccionado;
  }

  public void setSubproductoSeleccionado(Subproducto subproductoSeleccionado) {
    this.subproductoSeleccionado = subproductoSeleccionado;
  }

  public Subproducto getNuevoSubproducto() {
    return nuevoSubproducto;
  }

  public void setNuevoSubproducto(Subproducto nuevoSubproducto) {
    this.nuevoSubproducto = nuevoSubproducto;
  }

  public Contacto getNuevoContacto() {
    return nuevoContacto;
  }

  public void setNuevoContacto(Contacto nuevoContacto) {
    this.nuevoContacto = nuevoContacto;
  }

  public Telefono getNuevoTelefono() {
    return nuevoTelefono;
  }

  public void setNuevoTelefono(Telefono nuevoTelefono) {
    this.nuevoTelefono = nuevoTelefono;
  }

  public Email getNuevoEmail() {
    return nuevoEmail;
  }

  public void setNuevoEmail(Email nuevoEmail) {
    this.nuevoEmail = nuevoEmail;
  }

  public Direccion getNuevaDireccion() {
    return nuevaDireccion;
  }

  public void setNuevaDireccion(Direccion nuevaDireccion) {
    this.nuevaDireccion = nuevaDireccion;
  }

  public List<Sujeto> getListaEmpresas() {
    return listaEmpresas;
  }

  public void setListaEmpresas(List<Sujeto> listaEmpresas) {
    this.listaEmpresas = listaEmpresas;
  }

  public List<Producto> getListaProductos() {
    return listaProductos;
  }

  public void setListaProductos(List<Producto> listaProductos) {
    this.listaProductos = listaProductos;
  }

  public List<Subproducto> getListaSubproductos() {
    return listaSubproductos;
  }

  public void setListaSubproductos(List<Subproducto> listaSubproductos) {
    this.listaSubproductos = listaSubproductos;
  }

  public EmpresaBean getEmpresaBean() {
    return empresaBean;
  }

  public void setEmpresaBean(EmpresaBean empresaBean) {
    this.empresaBean = empresaBean;
  }

  public SujetoBean getSujetoBean() {
    return sujetoBean;
  }

  public void setSujetoBean(SujetoBean sujetoBean) {
    this.sujetoBean = sujetoBean;
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
