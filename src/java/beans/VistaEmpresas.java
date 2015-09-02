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
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import util.constantes.Patrones;

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
  
  // objetos necesarios para obtener la direccion completa
  public String calle;
  public String numext;
  public String numint;

  // listas
  private List<Sujeto> listaEmpresas;
  private List<Producto> listaProductos;
  private List<Subproducto> listaSubproductos;

  // llamada a otros beans
  @ManagedProperty(value = "#{empresaBean}")
  private EmpresaBean empresaBean;
  @ManagedProperty(value = "#{contactoBean}")
  private ContactoBean contactoBean;
  @ManagedProperty(value = "#{telefonoBean}")
  private TelefonoBean telefonoBean;
  @ManagedProperty(value = "#{emailBean}")
  private EmailBean emailBean;
  @ManagedProperty(value = "#{direccionBean}")
  private DireccionBean direccionBean;
  @ManagedProperty(value = "#{sujetoBean}")
  private SujetoBean sujetoBean;

  // constructor
  public VistaEmpresas() {
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

  // post constructor para cargar la lista de empresas en madriza
  @PostConstruct
  public void buscarEmpresas() {
    // aprovechamos la funcion de postconstruccion y precargamos la lista de estados
    direccionBean.listarEstados();
    // cargamos la lista de empresas
    listaEmpresas = empresaBean.buscarEmpresas();
    if (listaEmpresas == null) {
      FacesContext context = FacesContext.getCurrentInstance();
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo obtener la lista de empresas registradas"));
    }
  }

  // es el metodo que se encarga de la gestion integral de las empresas
  // verifica todos los campos de datos adicionales, los inserta si son validos
  public void crearEmpresa() {
    // primero se crea el sujeto al cual podremos añadir datos adicionales
    insertarSujeto();
    // se llama a la funcion de verificar telefonos para saber si los campos no estan vacios
    if (verificaTelefono()) {
      // mandamos a llamar al metodo que inserta un telefono
      crearTelefono();
    }
    else {
      if (verificaCorreo()) {
        // mandamos a llamar al metodo que inserta un correo
        crearCorreo();
      }
      else {
        if (verificaDireccion()) {
          // mandamos a llamar al metodo que inserta una direccion
          crearDireccion();
        }
        else {
          if (verificaContacto()) {
            // mandamos a llamar al metodo que inserta un contacto
            crearContacto();
          }
        }
      }
    }
  }
    // es el metodo que inserta un sujeto
  public void insertarSujeto() {
    // validamos el rfc de la persona moral
    boolean okRfc = sujetoBean.validarRfc();
    // creamos una instancia de FacesContext para poder utilizar el growl
    FacesContext actual = FacesContext.getCurrentInstance();
    // si el rfc proporcionado es una cadena valida
    if (okRfc) {
      // invocamos al metodo de creacion de sujetos y esperamos el resultado de la transaccion
      nuevoSujeto = sujetoBean.insertar();
      // si se creo el sujeto
      if (nuevoSujeto != null) {
        insertarEmpresa(nuevoSujeto);
      } // no se creo el sujeto
      else {
        actual.addMessage("somekey", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se registro al sujeto vinculado a la empresa en el sistema"));
      }
    } // el rfc no es valido
    else {
      actual.addMessage("somekey", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "El RFC proporcionado no es valido, favor de verificarlo"));
    }
  }

  // es el metodo para crear empresas, interactua con los beans de cliente y sujeto
  public void insertarEmpresa(Sujeto nuevoSujeto) {
    // invocamos al metodo de creacion de empresas y esperamos el resultado de la transaccion
    int resultado = empresaBean.crearEmpresa(nuevoSujeto);
    // creamos una instancia de FacesContext para poder utilizar el growl
    FacesContext actual = FacesContext.getCurrentInstance();
    // switch para desplegar el mensaje correspondiente
    switch (resultado) {
      case 1:
        actual.addMessage("somekey", new FacesMessage(FacesMessage.SEVERITY_INFO, "Insercion exitosa", "Se registro a la empresa en el sistema"));
        verificaTelefono();
        break;
      case 0:
        actual.addMessage("somekey", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se registro a la empresa en el sistema"));
        break;
    }
  }

  // es el metodo que verifica los campos de telefono para agregar
  public boolean verificaTelefono() {
    boolean verificacion = false;
    // creamos una instancia de FacesContext para poder utilizar el growl
    FacesContext actual = FacesContext.getCurrentInstance();
    // se verifica que los campos requeridos no esten vacios
    // si estan vacios
    if ((telefonoBean.getNumero() == null) && (telefonoBean.getLada() == null)) {
      verificacion = false;
    } // si no estan vacios, se verifica que sean validos
    else {
      String num = telefonoBean.getNumero();
      String clave = telefonoBean.getLada();
      // si es numero y es mayor a 7 caracteres, se considera un telefono valido
      if (Pattern.matches("[0-9]+", num) == true && num.length() >= 7) {
        // se verifica la clave lada
        // si es un numero menor a 3 digitos se considera una clave lada valida
        if (Pattern.matches("[0-9]+", clave) == true && num.length() <= 3) {
          verificacion = true;
        }
      } else {
        actual.addMessage("somekey", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "El numero telefonico no es valido. Favor de verificarlo"));
      }
    }
    return verificacion;
  }

  // es el metodo para crear un telefono
  public void crearTelefono() {
    // creamos una instancia de FacesContext para poder utilizar el growl
    FacesContext actual = FacesContext.getCurrentInstance();
    // invocamos al metodo de creacion de telefonos y esperamos el resultado de la transaccion
    nuevoTelefono = telefonoBean.insertar(nuevoSujeto);
    // si se creo el telefono
    if (nuevoTelefono != null) {
      actual.addMessage("somekey", new FacesMessage(FacesMessage.SEVERITY_INFO, "Insercion exitosa", "El numero telefonico se agrego a la empresa"));
    } // no se creo el telefono
    else {
      actual.addMessage("somekey", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo agregar el numero telefonico en el sistema"));
    }
  }
  
  // es el metodo para verificar el campo de correo electronico
  public boolean verificaCorreo(){
    boolean verificacion = false;
    // creamos una instancia de FacesContext para poder utilizar el growl
    FacesContext actual = FacesContext.getCurrentInstance();
    // si el campo esta vacio
    if(emailBean.getDireccion() == null){
      verificacion = false;
    }
    else{
      String correo = emailBean.getDireccion();
      // se verifica que sea una direccion de correo valida
      if(emailBean.validarCorreo(correo)){
        verificacion = true;
      }
      else{
        actual.addMessage("somekey", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "El correo electronico no es valido. Favor de verificarlo"));
      }
    }    
    return verificacion;
  }
  
  // es el metodo que inserta un correo al sistema
  public void crearCorreo(){
    // creamos una instancia de FacesContext para poder utilizar el growl
    FacesContext actual = FacesContext.getCurrentInstance();
    nuevoEmail = emailBean.insertar(nuevoSujeto);
    // si se creo el correo
    if (nuevoEmail != null) {
      actual.addMessage("somekey", new FacesMessage(FacesMessage.SEVERITY_INFO, "Insercion exitosa", "El correo electronico se agrego a la empresa"));
    } // no se creo el telefono
    else {
      actual.addMessage("somekey", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo agregar el correo electronico en el sistema"));
    }
  }

  //
  public boolean verificaDireccion(){
    boolean verificacion = false;
    return verificacion;
  }
  
  //
  public void crearDireccion(){
  }
  
  //
  public boolean verificaContacto(){
    boolean verificacion = false;
    return verificacion;
  }
  
  //
  public void crearContacto(){
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

  public SujetoBean getSujetoBean() {
    return sujetoBean;
  }

  public void setSujetoBean(SujetoBean sujetoBean) {
    this.sujetoBean = sujetoBean;
  }

  public String getNumext() {
    return numext;
  }

  public void setNumext(String numext) {
    this.numext = numext;
  }

  public String getNumint() {
    return numint;
  }

  public void setNumint(String numint) {
    this.numint = numint;
  }

  public String getCalle() {
    return calle;
  }

  public void setCalle(String calle) {
    this.calle = calle;
  }

}
