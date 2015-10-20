/**
 *
 * @author Eduardo
 */
package beans;

// importacion de librerias
// dao
import dao.ColoniaDAO;
import dao.InstitucionDAO;
import dao.EstadoRepublicaDAO;
import dao.MunicipioDAO;
import dao.ProductoDAO;
import dao.SubproductoDAO;
import dao.SujetoDAO;

// dto
import dto.Institucion;
import dto.EstadoRepublica;
import dto.Municipio;
import dto.Colonia;
import dto.Producto;
import dto.Subproducto;
import dto.Sujeto;

// implements
import impl.ColoniaIMPL;
import impl.InstitucionIMPL;
import impl.EstadoRepublicaIMPL;
import impl.MunicipioIMPL;
import impl.ProductoIMPL;
import impl.SubproductoIMPL;
import impl.SujetoIMPL;

// util
import util.constantes.Patrones;
import util.constantes.Sujetos;

// java
import java.io.Serializable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// faces
import javax.faces.bean.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

// primefaces
import org.primefaces.context.RequestContext;

/**
 * La clase {@code InstitucionesBean} permite ... y es el bean correspondiente a la
 * vista {@code instituciones.xhtml}
 *
 * @author
 * @author
 * @author Eduardo
 * @since SigerWeb2.0
 */
@ManagedBean(name = "institucionesBean")
@SessionScoped
public class InstitucionesBean implements Serializable {

  // declaracion de variables
  // variables dao
  private SujetoDAO sujetoDao;
  private ProductoDAO productoDao;
  private InstitucionDAO institucionDao;
  private SubproductoDAO subproductoDao;
  private EstadoRepublicaDAO estadoDao;
  private MunicipioDAO municipioDao;
  private ColoniaDAO coloniaDao;
  
  // variables Sujeto
  private Sujeto sujeto;
  private Sujeto institucion;
  private Sujeto nuevoSujeto;
  private Sujeto sujetoSeleccionado;
  
  //variables Institucion
  private Institucion institucionSeleccionada;
  private Institucion nuevaInstitucion;
  
  //variables Producto
  private Producto producto;
  private Producto productoSeleccionado;
  private Producto seleccionadoCombobox;
  private Producto nuevoProducto;
  
  // variables Subproducto
  private Subproducto subproduct;
  private Subproducto subproductoSeleccionado;
  private Subproducto nuevoSubproducto;
  
  // variables EstadoRepublica
  private EstadoRepublica estado;
  
  // variables Municipio
  private Municipio municipio;
  
  // variables Colonia
  private Colonia colonia;
  
  // listas
  private List<Sujeto> listaInstituciones;
  private List<Producto> listaProductos;
  private List<Subproducto> listaSubproductos;
  private List<EstadoRepublica> conjuntoEstados;
  private List<Municipio> conjuntoMunicipios;
  private List<Colonia> conjuntoColonias;
  
  // Strings
  private String nombreRazonSocial;
  private String rfc;
  private String prod;
  private String desc;
  private String subprod;
  private String subdesc;
  private String razonSocial;
  private String corto;
  private String auxRfc;
  private String nuevaRazonSocial;
  private String nuevoRfc;
  private String nuevoCorto;
  private String nuevoNumero;
  private String nuevaLada;
  private String nuevoTipoTel;
  private String nuevoHorarioAtencionTel;
  private String nuevoCorreo;
  private String nuevaCalle;
  private String nuevoExterior;
  private String nuevoInterior;
  private String nuevoProd;
  private String nuevaDesc;
  private String nuevoSubprod;
  private String nuevaSubdesc;
  
  // enteros
  private int id;
  private int idNuevoSujeto;
  private int idEstado;
  private int idMunicipio;
  private int idColonia;
  
  // Booleanos
  private boolean okNuevaInstitucion;
  private boolean okEditarInstitucion;
  private boolean okEditarSujeto;
  private boolean okBorrarInstitucion;
  private boolean okNuevoProducto;
  private boolean okEditarProducto;
  private boolean okNuevoSubproducto;
  private boolean okEditarSubproducto;
  
  /**
   *
   *
   *
   */
  public InstitucionesBean() {
    // constructores
    // sujeto
    sujetoDao = new SujetoIMPL();
    sujeto = new Sujeto();
    nuevoSujeto = new Sujeto();
    
    // Institucion
    institucionDao = new InstitucionIMPL();
    listaInstituciones = institucionDao.buscarInstituciones();
    nuevaInstitucion = new Institucion();
    
    // producto
    productoDao = new ProductoIMPL();
    nuevoProducto = new Producto();
    
    // subproducto
    subproductoDao = new SubproductoIMPL();
    nuevoSubproducto = new Subproducto();
    
    // estado
    estado = new EstadoRepublica();
    estadoDao = new EstadoRepublicaIMPL();
    conjuntoEstados = estadoDao.buscarTodo();
    
    // municipio
    municipio = new Municipio();
    municipioDao = new MunicipioIMPL();
    
    // colonia
    colonia = new Colonia();
    coloniaDao = new ColoniaIMPL();
  }

  // ***********************************************************************************************************************************************************
  // ***********************************************************************************************************************************************************
  // ***********************************************************************************************************************************************************
  // FUNCIONES DEL MODULO DE NUEVA Institucion
  
  // funcion para validar el rfc de una nueva Institucion
  public boolean validarRfc(String rfc) {
    Pattern patron = Pattern.compile(Patrones.PATRON_RFC_MORAL);
    Matcher matcher = patron.matcher(rfc);
    return matcher.matches();
  }
  
  // funcion para crear a la Institucion segun los datos primarios brindados
  // falta incluir los datos obtenidos de las vistas de telefonos, correos, direcciones y contactos
  public void crearInstitucion() {
    nuevoRfc = nuevoRfc.toUpperCase();
    boolean okRfc = validarRfc(nuevoRfc);
    nuevoSujeto.setNombreRazonSocial(nuevaRazonSocial);
    nuevoSujeto.setRfc(nuevoRfc);
    nuevoSujeto.setEliminado(Sujetos.ACTIVO);
    idNuevoSujeto = sujetoDao.insertar(nuevoSujeto).getIdSujeto();
    nuevaInstitucion.setNombreCorto(nuevoCorto);
    nuevaInstitucion.setSujeto(nuevoSujeto);
    FacesContext actual = FacesContext.getCurrentInstance();
    if (okRfc) {
      okNuevaInstitucion = institucionDao.insertar(nuevaInstitucion);
      if (okNuevaInstitucion) {
        actual.addMessage("somekey", new FacesMessage(FacesMessage.SEVERITY_INFO, "Insercion exitosa", "Se registro a la institucion " + nuevaRazonSocial + " en el sistema"));
        RequestContext.getCurrentInstance().update("formNuevaInstitucion");
        RequestContext.getCurrentInstance().update("formEditarInstitucion");
      } else {
        actual.addMessage("somekey", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se registro a la institucion " + nuevaRazonSocial + " en el sistema"));
      }
    } else {
      actual.addMessage("somekey", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "El RFC proporcionado no es valido, favor de verificarlo"));
    }
  }
  
  // funcion para llenar la lista de municipios segun el estado seleccionado
  public void cargaMunicipios() {
    estado.setIdEstado(idEstado);
    estado = estadoDao.buscar(estado.getIdEstado());
    conjuntoMunicipios = municipioDao.buscarMunicipiosPorEstado(estado.getIdEstado());
    int i = 0;
    for (i = 0; i < (conjuntoMunicipios.size()); i++) {
      System.out.println(conjuntoMunicipios.get(i).getNombre());
    }
    
  }

  public void cargaColonias() {
    municipio.setIdMunicipio(idMunicipio);
    municipio = municipioDao.buscar(municipio.getIdMunicipio());
    conjuntoColonias = coloniaDao.buscarColoniasPorMunicipio(idMunicipio);
    int i = 0;
    for (i = 0; i < (conjuntoColonias.size()); i++) {
      System.out.println(conjuntoColonias.get(i).getNombre());
    }
  }
  // ***********************************************************************************************************************************************************
  // ***********************************************************************************************************************************************************
  // ***********************************************************************************************************************************************************
  // FUNCIONES DEL MODULO DE EDITAR Institucion
  
  // funcion que guarda los datos de la Institucion seleccionada para editar
  public void guardarInstitucion() {
    institucionSeleccionada = institucionDao.buscarInstitucionPorSujeto(institucion.getIdSujeto());
    sujetoSeleccionado = sujetoDao.buscar(institucion.getIdSujeto());
    razonSocial = sujetoSeleccionado.getNombreRazonSocial();
    corto = institucionSeleccionada.getNombreCorto();
    auxRfc = sujetoSeleccionado.getRfc();
    id = institucionSeleccionada.getIdInstitucion();
    listaProductos = productoDao.buscarProductosPorInstitucion(id);
    listaSubproductos = subproductoDao.buscarSubproductosPorInstitucion(id);
  }

  // funcion que guarda los datos del producto a editar
  public void guardarProducto() {
    productoSeleccionado = productoDao.buscar(producto.getIdProducto());
    prod = productoSeleccionado.getNombre();
    desc = productoSeleccionado.getDescripcion();
  }

  // funcion que guarda los datos del subproducto a editar
  public void guardarSubproducto() {
    System.out.println("************ CONSOLA SIGERWEB ****************");
    System.out.println(subproduct.getIdSubproducto());
    subproductoSeleccionado = subproductoDao.buscar(subproduct.getIdSubproducto());
    subprod = subproductoSeleccionado.getNombre();
    subdesc = subproductoSeleccionado.getDescripcion();
  }

  // funcion que actualiza los datos primarios de la Institucion seleccionada
  public void editarInstitucion() {
    sujetoSeleccionado.setNombreRazonSocial(razonSocial);
    System.out.println(razonSocial);
    sujetoSeleccionado.setRfc(auxRfc);
    System.out.println(auxRfc);
    okEditarSujeto = sujetoDao.editar(sujetoSeleccionado);
    institucionSeleccionada.setNombreCorto(corto);
    System.out.println(corto);
    okEditarInstitucion = institucionDao.editar(institucionSeleccionada);
    FacesContext actual = FacesContext.getCurrentInstance();
    if (okEditarInstitucion && okEditarSujeto) {
      actual.addMessage("somekey", new FacesMessage(FacesMessage.SEVERITY_INFO, "Actualizacion exitosa", "Se edito a la institucion " + corto));
    } else {
      actual.addMessage("somekey", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se guardaron los cambios de la institucion " + corto));
    }
  }

  // funcion que elimina (con borrado logico) a la institucion seleccionada
  public void eliminarInstitucion() {
    institucion.setEliminado(Sujetos.ELIMINADO);
    okBorrarInstitucion = sujetoDao.eliminar(institucion);
    if (okBorrarInstitucion) {
      System.out.println("************ CONSOLA SIGERWEB ****************");
      System.out.println("Se elimino a la institucion " + corto);
      RequestContext.getCurrentInstance().update("editarInstitucions");
    } else {
      System.out.println("Error fatal. No se pudo borrar a la institucion " + institucion.getNombreRazonSocial());
    }
  }

  // funcion para crear un producto para la Institucion seleccionada
  public void crearProducto() {
    nuevoProducto.setNombre(nuevoProd);
    nuevoProducto.setDescripcion(nuevaDesc);
    okNuevoProducto = productoDao.insertar(nuevoProducto);
    if (okNuevoProducto) {
      System.out.println("************ CONSOLA SIGERWEB ****************");
      System.out.println("Se registro el producto " + nuevoProd + " exitosamente");
      /*
       RequestContext.getCurrentInstance().update("formNuevaInstitucion");
       RequestContext.getCurrentInstance().update("formEditarInstitucion");
       */
    } else {
      System.out.println("Error fatal. No se registro el producto " + nuevoProd + " en el sistema");
    }
  }

  // funcion que actualiza los datos primarios del producto seleccionado
  public void editarProducto() {
    System.out.println("************ CONSOLA SIGERWEB ****************");
    System.out.println("Se quiere editar el producto " + prod);
    RequestContext.getCurrentInstance().update("editarProductos");
  }

  // funcion para crear un subproducto para la Institucion seleccionada
  public void crearSubproducto() {
    nuevoSubproducto.setNombre(nuevoSubprod);
    nuevoSubproducto.setDescripcion(nuevaSubdesc);
    nuevoSubproducto.setProducto(seleccionadoCombobox);
    okNuevoSubproducto = subproductoDao.insertar(nuevoSubproducto);
    if (okNuevoSubproducto) {
      System.out.println("************ CONSOLA SIGERWEB ****************");
      System.out.println("Se registro el subproducto " + nuevoSubprod + " exitosamente");
      /*
       RequestContext.getCurrentInstance().update("formNuevaInstitucion");
       RequestContext.getCurrentInstance().update("formEditarInstitucion");
       */
    } else {
      System.out.println("Error fatal. No se registro el producto " + nuevoSubprod + " en el sistema");
    }
  }
  
  // funcion que actualiza los datos primarios del subproducto seleccionado
  public void editarSubproducto() {
    System.out.println("************ CONSOLA SIGERWEB ****************");
    System.out.println("Se quiere editar el subproducto " + subprod);
    RequestContext.getCurrentInstance().update("editarSubproductos");
  }

  // ***********************************************************************************************************************************************************
  // ***********************************************************************************************************************************************************
  // ***********************************************************************************************************************************************************
  // SETTERS Y GETTERS

  /**
   *
   *
   * @return
   */
  public Sujeto getSujeto() {
    return sujeto;
  }

  /**
   *
   *
   * @param sujeto
   */
  public void setSujeto(Sujeto sujeto) {
    this.sujeto = sujeto;
  }

  /**
   *
   *
   * @return
   */
  public SujetoDAO getSujetoDao() {
    return sujetoDao;
  }

  /**
   *
   *
   * @param sujetoDao
   */
  public void setSujetoDao(SujetoDAO sujetoDao) {
    this.sujetoDao = sujetoDao;
  }

  /**
   *
   *
   * @return
   */
  public String getNombreRazonSocial() {
    return nombreRazonSocial;
  }

  /**
   *
   *
   * @param nombreRazonSocial
   */
  public void setNombreRazonSocial(String nombreRazonSocial) {
    this.nombreRazonSocial = nombreRazonSocial;
  }

  /**
   *
   *
   * @return
   */
  public String getRfc() {
    return rfc;
  }

  /**
   *
   *
   * @param rfc
   */
  public void setRfc(String rfc) {
    this.rfc = rfc;
  }

  /**
   *
   *
   * @return
   */
  public List<Sujeto> getListaInstituciones() {
    return listaInstituciones;
  }

  /**
   *
   *
   * @param listaInstituciones
   */
  public void setListaInstituciones(List<Sujeto> listaInstituciones) {
    this.listaInstituciones = listaInstituciones;
  }

  /**
   *
   *
   * @return
   */
  public Sujeto getInstitucion() {
    return institucion;
  }

  /**
   *
   *
   * @param institucion
   */
  public void setInstitucion(Sujeto institucion) {
    this.institucion = institucion;
  }

  public Institucion getInstitucionSeleccionada() {
    return institucionSeleccionada;
  }

  /**
   *
   * @param institucionSeleccionada
   */
  public void setInstitucionSeleccionada(Institucion institucionSeleccionada) {
    this.institucionSeleccionada = institucionSeleccionada;
  }

  /**
   *
   * @return
   */
  public Sujeto getSujetoSeleccionado() {
    return sujetoSeleccionado;
  }

  /**
   *
   * @param sujetoSeleccionado
   */
  public void setSujetoSeleccionado(Sujeto sujetoSeleccionado) {
    this.sujetoSeleccionado = sujetoSeleccionado;
  }

  /**
   *
   * @return
   */
  public InstitucionDAO getInstitucionDao() {
    return institucionDao;
  }

  /**
   *
   * @param institucionDao
   */
  public void setInstitucionDao(InstitucionDAO institucionDao) {
    this.institucionDao = institucionDao;
  }

  /**
   *
   * @return
   */
  public String getRazonSocial() {
    return razonSocial;
  }

  /**
   *
   * @param razonSocial
   */
  public void setRazonSocial(String razonSocial) {
    this.razonSocial = razonSocial;
  }

  /**
   *
   * @return
   */
  public String getCorto() {
    return corto;
  }

  /**
   *
   * @param corto
   */
  public void setCorto(String corto) {
    this.corto = corto;
  }

  /**
   *
   * @return
   */
  public String getAuxRfc() {
    return auxRfc;
  }

  /**
   *
   * @param auxRfc
   */
  public void setAuxRfc(String auxRfc) {
    this.auxRfc = auxRfc;
  }

  public List<Subproducto> getListaSubproductos() {
    return listaSubproductos;
  }

  public void setListaSubproductos(List<Subproducto> listaSubproductos) {
    this.listaSubproductos = listaSubproductos;
  }

  public Producto getProducto() {
    return producto;
  }

  public void setProducto(Producto producto) {
    this.producto = producto;
  }

  public Producto getProductoSeleccionado() {
    return productoSeleccionado;
  }

  public void setProductoSeleccionado(Producto productoSeleccionado) {
    this.productoSeleccionado = productoSeleccionado;
  }

  public String getProd() {
    return prod;
  }

  public void setProd(String prod) {
    this.prod = prod;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public SubproductoDAO getSubproductoDao() {
    return subproductoDao;
  }

  public void setSubproductoDao(SubproductoDAO subproductoDao) {
    this.subproductoDao = subproductoDao;
  }

  public String getNuevaRazonSocial() {
    return nuevaRazonSocial;
  }

  public void setNuevaRazonSocial(String nuevaRazonSocial) {
    this.nuevaRazonSocial = nuevaRazonSocial;
  }

  public String getNuevoCorto() {
    return nuevoCorto;
  }

  public void setNuevoCorto(String nuevoCorto) {
    this.nuevoCorto = nuevoCorto;
  }

  public String getNuevoRfc() {
    return nuevoRfc;
  }

  public void setNuevoRfc(String nuevoRfc) {
    this.nuevoRfc = nuevoRfc;
  }

  public Sujeto getNuevoSujeto() {
    return nuevoSujeto;
  }

  public void setNuevoSujeto(Sujeto nuevoSujeto) {
    this.nuevoSujeto = nuevoSujeto;
  }

  public Institucion getNuevaInstitucion() {
    return nuevaInstitucion;
  }

  public void setNuevaInstitucion(Institucion nuevaInstitucion) {
    this.nuevaInstitucion = nuevaInstitucion;
  }

  public int getIdNuevoSujeto() {
    return idNuevoSujeto;
  }

  public void setIdNuevoSujeto(int idNuevoSujeto) {
    this.idNuevoSujeto = idNuevoSujeto;
  }

  public boolean isOkNuevaInstitucion() {
    return okNuevaInstitucion;
  }

  public void setOkNuevaInstitucion(boolean okNuevaInstitucion) {
    this.okNuevaInstitucion = okNuevaInstitucion;
  }

  public boolean isOkEditarInstitucion() {
    return okEditarInstitucion;
  }

  public void setOkEditarInstitucion(boolean okEditarInstitucion) {
    this.okEditarInstitucion = okEditarInstitucion;
  }

  public boolean isOkEditarSujeto() {
    return okEditarSujeto;
  }

  public void setOkEditarSujeto(boolean okEditarSujeto) {
    this.okEditarSujeto = okEditarSujeto;
  }

  public boolean isOkBorrarInstitucion() {
    return okBorrarInstitucion;
  }

  public void setOkBorrarInstitucion(boolean okBorrarInstitucion) {
    this.okBorrarInstitucion = okBorrarInstitucion;
  }

  public Producto getNuevoProducto() {
    return nuevoProducto;
  }

  public void setNuevoProducto(Producto nuevoProducto) {
    this.nuevoProducto = nuevoProducto;
  }

  public String getNuevoProd() {
    return nuevoProd;
  }

  public void setNuevoProd(String nuevoProd) {
    this.nuevoProd = nuevoProd;
  }

  public String getNuevaDesc() {
    return nuevaDesc;
  }

  public void setNuevaDesc(String nuevaDesc) {
    this.nuevaDesc = nuevaDesc;
  }

  public boolean isOkNuevoProducto() {
    return okNuevoProducto;
  }

  public void setOkNuevoProducto(boolean okNuevoProducto) {
    this.okNuevoProducto = okNuevoProducto;
  }

  public boolean isOkEditarProducto() {
    return okEditarProducto;
  }

  public void setOkEditarProducto(boolean okEditarProducto) {
    this.okEditarProducto = okEditarProducto;
  }

  public Subproducto getNuevoSubproducto() {
    return nuevoSubproducto;
  }

  public void setNuevoSubproducto(Subproducto nuevoSubproducto) {
    this.nuevoSubproducto = nuevoSubproducto;
  }

  public String getNuevoSubprod() {
    return nuevoSubprod;
  }

  public void setNuevoSubprod(String nuevoSubprod) {
    this.nuevoSubprod = nuevoSubprod;
  }

  public String getNuevaSubdesc() {
    return nuevaSubdesc;
  }

  public void setNuevaSubdesc(String nuevaSubdesc) {
    this.nuevaSubdesc = nuevaSubdesc;
  }

  public Producto getSeleccionadoCombobox() {
    return seleccionadoCombobox;
  }

  public void setSeleccionadoCombobox(Producto seleccionadoCombobox) {
    this.seleccionadoCombobox = seleccionadoCombobox;
  }

  public boolean isOkNuevoSubproducto() {
    return okNuevoSubproducto;
  }

  public void setOkNuevoSubproducto(boolean okNuevoSubproducto) {
    this.okNuevoSubproducto = okNuevoSubproducto;
  }

  public boolean isOkEditarSubproducto() {
    return okEditarSubproducto;
  }

  public void setOkEditarSubproducto(boolean okEditarSubproducto) {
    this.okEditarSubproducto = okEditarSubproducto;
  }

  public Subproducto getSubproduct() {
    return subproduct;
  }

  public void setSubproduct(Subproducto subproduct) {
    this.subproduct = subproduct;
  }

  public Subproducto getSubproductoSeleccionado() {
    return subproductoSeleccionado;
  }

  public void setSubproductoSeleccionado(Subproducto subproductoSeleccionado) {
    this.subproductoSeleccionado = subproductoSeleccionado;
  }

  public String getSubprod() {
    return subprod;
  }

  public void setSubprod(String subprod) {
    this.subprod = subprod;
  }

  public String getSubdesc() {
    return subdesc;
  }

  public void setSubdesc(String subdesc) {
    this.subdesc = subdesc;
  }

  public ProductoDAO getProductoDao() {
    return productoDao;
  }

  public void setProductoDao(ProductoDAO productoDao) {
    this.productoDao = productoDao;
  }

  public List<Producto> getListaProductos() {
    return listaProductos;
  }

  public void setListaProductos(List<Producto> listaProductos) {
    this.listaProductos = listaProductos;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNuevoNumero() {
    return nuevoNumero;
  }

  public void setNuevoNumero(String nuevoNumero) {
    this.nuevoNumero = nuevoNumero;
  }

  public String getNuevaLada() {
    return nuevaLada;
  }

  public void setNuevaLada(String nuevaLada) {
    this.nuevaLada = nuevaLada;
  }

  public String getNuevoTipoTel() {
    return nuevoTipoTel;
  }

  public void setNuevoTipoTel(String nuevoTipoTel) {
    this.nuevoTipoTel = nuevoTipoTel;
  }

  public String getNuevoHorarioAtencionTel() {
    return nuevoHorarioAtencionTel;
  }

  public void setNuevoHorarioAtencionTel(String nuevoHorarioAtencionTel) {
    this.nuevoHorarioAtencionTel = nuevoHorarioAtencionTel;
  }

  public String getNuevaCalle() {
    return nuevaCalle;
  }

  public void setNuevaCalle(String nuevaCalle) {
    this.nuevaCalle = nuevaCalle;
  }

  public String getNuevoExterior() {
    return nuevoExterior;
  }

  public void setNuevoExterior(String nuevoExterior) {
    this.nuevoExterior = nuevoExterior;
  }

  public String getNuevoInterior() {
    return nuevoInterior;
  }

  public void setNuevoInterior(String nuevoInterior) {
    this.nuevoInterior = nuevoInterior;
  }

  public int getIdEstado() {
    return idEstado;
  }

  public void setIdEstado(int idEstado) {
    this.idEstado = idEstado;
  }

  public int getIdMunicipio() {
    return idMunicipio;
  }

  public void setIdMunicipio(int idMunicipio) {
    this.idMunicipio = idMunicipio;
  }

  public int getIdColonia() {
    return idColonia;
  }

  public void setIdColonia(int idColonia) {
    this.idColonia = idColonia;
  }

  public String getNuevoCorreo() {
    return nuevoCorreo;
  }

  public void setNuevoCorreo(String nuevoCorreo) {
    this.nuevoCorreo = nuevoCorreo;
  }

  public List<EstadoRepublica> getConjuntoEstados() {
    return conjuntoEstados;
  }

  public void setConjuntoEstados(List<EstadoRepublica> conjuntoEstados) {
    this.conjuntoEstados = conjuntoEstados;
  }

  public List<Municipio> getConjuntoMunicipios() {
    return conjuntoMunicipios;
  }

  public void setConjuntoMunicipios(List<Municipio> conjuntoMunicipios) {
    this.conjuntoMunicipios = conjuntoMunicipios;
  }

  public List<Colonia> getConjuntoColonias() {
    return conjuntoColonias;
  }

  public void setConjuntoColonias(List<Colonia> conjuntoColonias) {
    this.conjuntoColonias = conjuntoColonias;
  }

  public EstadoRepublicaDAO getEstadoDao() {
    return estadoDao;
  }

  public void setEstadoDao(EstadoRepublicaDAO estadoDao) {
    this.estadoDao = estadoDao;
  }

  public MunicipioDAO getMunicipioDao() {
    return municipioDao;
  }

  public void setMunicipioDao(MunicipioDAO municipioDao) {
    this.municipioDao = municipioDao;
  }

  public ColoniaDAO getColoniaDao() {
    return coloniaDao;
  }

  public void setColoniaDao(ColoniaDAO coloniaDao) {
    this.coloniaDao = coloniaDao;
  }

  public EstadoRepublica getEstado() {
    return estado;
  }

  public void setEstado(EstadoRepublica estado) {
    this.estado = estado;
  }

  public Municipio getMunicipio() {
    return municipio;
  }

  public void setMunicipio(Municipio municipio) {
    this.municipio = municipio;
  }

  public Colonia getColonia() {
    return colonia;
  }

  public void setColonia(Colonia colonia) {
    this.colonia = colonia;
  }
}