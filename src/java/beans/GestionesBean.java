/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;


import dao.GestionDAO;
import dao.GestorDAO;
import dao.InstitucionDAO;
import dao.ProductoDAO;
import dao.TipoGestionDAO;
import dto.Gestion;
import dto.Gestor;
import dto.Institucion;
import dto.Producto;
import dto.TipoGestion;
import impl.GestionIMPL;
import impl.GestorIMPL;
import impl.InstitucionIMPL;
import impl.ProductoIMPL;
import impl.TipoGestionIMPL;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "gestionesBean")
@ViewScoped
public class GestionesBean implements Serializable {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  // VARIABLES DE CLASE
  private final int idDespacho;
  private boolean permitirExport;
  private boolean habilitaGestor;
  private boolean habilitaTipoGestion;
  private boolean habilitaInstitucion;
  private boolean habilitaProducto;
  private boolean habilitaTabla;
  private String nombreArchivo;
  private String fi;
  private String ff;
  private Date fechaInicio;
  private Date fechaFin;
  private TipoGestion tipoSeleccionado;
  private Gestor gestorSeleccionado;
  private Producto productoSeleccionado;
  private Institucion institucionSeleccionada;
  private List<TipoGestion> listaTipos;
  private List<Gestor> listaGestores;
  private List<Gestion> listaGestiones;
  private List<Producto> listaProductos;
  private List<Institucion> listaInstituciones;
  private final GestorDAO gestorDao;
  private final GestionDAO gestionDao;
  private final ProductoDAO productoDao;
  private final InstitucionDAO institucionDao;
  private final TipoGestionDAO tipoGestionDao;

  public GestionesBean() {
    permitirExport = false;
    habilitaGestor = false;
    habilitaTipoGestion = false;
    habilitaInstitucion = false;
    habilitaProducto = false;
    habilitaTabla = false;
    idDespacho = indexBean.getUsuario().getDespacho().getIdDespacho();
    gestorSeleccionado = new Gestor();
    productoSeleccionado = new Producto();
    institucionSeleccionada = new Institucion();
    tipoSeleccionado = new TipoGestion();
    listaGestiones = new ArrayList();
    listaTipos = new ArrayList();
    listaGestores = new ArrayList();
    listaProductos = new ArrayList();
    listaInstituciones = new ArrayList();
    gestorDao = new GestorIMPL();
    gestionDao = new GestionIMPL();
    productoDao = new ProductoIMPL();
    institucionDao = new InstitucionIMPL();
    tipoGestionDao = new TipoGestionIMPL();
  }

  // METODO QUE HABILITA LA LISTA DE GESTORES
  public void habilitaGestores() {
    listaGestores = gestorDao.buscarPorDespacho(idDespacho);
    RequestContext.getCurrentInstance().update("formGestionesAdmin");
  }

  // METODO QUE HABILITA LA LISTA DE TIPOS DE GESTION
  public void habilitaTipoGestion() {
    listaTipos = tipoGestionDao.buscarTodo();
    RequestContext.getCurrentInstance().update("formGestionesAdmin");
  }

  // METODO QUE HABILITA LA LISTA DE INSTITUCIONES
  public void habilitaInstituciones() {
    listaInstituciones = institucionDao.buscarInstitucionesPorDespacho(idDespacho);
    RequestContext.getCurrentInstance().update("formGestionesAdmin");
  }

  // METODO QUE OBTIENE LA LISTA DE PRODUCTOS DE ACUERDO A LA INSTITUCION SELECCIONADA
  public void habilitaProductos() {
    listaProductos = productoDao.buscarProductosPorInstitucion(institucionSeleccionada.getIdInstitucion());
    RequestContext.getCurrentInstance().update("formGestionesAdmin");
  }

  // METODO QUE BUSCA TODAS LAS GESTIONES SEGUN PARAMETROS INGRESADOS
  public void buscar() {
    if (validarFechas()) {
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    fi = df.format(fechaInicio) + " 00:00:00";
    ff = df.format(fechaFin) + " 23:59:59";
    nombreArchivo = "GESTIONES_" + fi + "_" + ff.replace(":", "-");
    String consulta = "SELECT * FROM gestion WHERE fecha BETWEEN '" + fi + "' AND '" + ff + "' ";
    if (habilitaTipoGestion) {
      tipoSeleccionado = tipoGestionDao.buscarPorId(tipoSeleccionado.getIdTipoGestion());
      consulta = consulta + "AND id_tipo_gestion = " + tipoSeleccionado.getIdTipoGestion().toString() + " ";
      nombreArchivo = nombreArchivo + "_" + tipoSeleccionado.getNombre();
    }
    if (habilitaGestor) {
      gestorSeleccionado = gestorDao.buscar(gestorSeleccionado.getIdGestor());
      consulta = consulta + "AND id_usuario = (SELECT id_usuario FROM usuario WHERE id_usuario = " + gestorSeleccionado.getUsuario().getIdUsuario().toString() + ") ";
      nombreArchivo = nombreArchivo + "_" + gestorSeleccionado.getUsuario().getNombreLogin();
    } else {
      consulta = consulta + "AND id_usuario IN (SELECT id_usuario FROM usuario WHERE id_despacho = " + idDespacho + ") ";
      nombreArchivo = nombreArchivo + "_Todos los usuarios";
    }
    if (habilitaInstitucion) {
      institucionSeleccionada = institucionDao.buscar(institucionSeleccionada.getIdInstitucion());
      consulta = consulta + "AND id_credito IN (SELECT id_credito FROM credito WHERE id_producto IN (SELECT id_producto FROM producto WHERE id_institucion = " + institucionSeleccionada.getIdInstitucion().toString() + " )) ";
      nombreArchivo = nombreArchivo + "_" + institucionSeleccionada.getNombreCorto();
    }
    if (habilitaProducto) {
      productoSeleccionado = productoDao.buscar(productoSeleccionado.getIdProducto());
      consulta = consulta + "AND id_credito IN (SELECT id_credito FROM credito WHERE id_producto IN (SELECT id_producto FROM producto WHERE id_producto = " + productoSeleccionado.getIdProducto().toString() + ")) ";
      nombreArchivo = nombreArchivo + "_" + productoSeleccionado.getNombre();
    }
    consulta = consulta + ";";
    listaGestiones = gestionDao.busquedaReporteGestiones(consulta);
    habilitaTabla = true;
    if (listaGestiones.isEmpty()) {
      permitirExport = false;
    } else {
      permitirExport = true;
    }
    RequestContext.getCurrentInstance().update("formGestionesAdmin");
    }
  }

  // METODO QUE VALIDA LAS FECHAS
  public boolean validarFechas() {
    boolean ok;
    FacesContext contexto = FacesContext.getCurrentInstance();
    Date fechaActual = new Date();
    if(fechaInicio.after(fechaActual)){
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "La fecha inicial no puede ser mayor a la actual."));
      ok = false;
    }
    if(fechaFin.after(fechaActual)){
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "La fecha final no puede ser mayor a la actual."));
      ok = false;
    }
    if(fechaFin.before(fechaInicio)){
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "La fecha final no puede ser menor a la fecha inicial."));
      ok = false;
    }
    return true;
  }

// ***********************************************************************************************************************
// ***********************************************************************************************************************
// ***********************************************************************************************************************
// GETTERS & SETTERS
  public List<TipoGestion> getListaTipos() {
    return listaTipos;
  }

  public void setListaTipos(List<TipoGestion> listaTipos) {
    this.listaTipos = listaTipos;
  }

  public List<Gestor> getListaGestores() {
    return listaGestores;
  }

  public void setListaGestores(List<Gestor> listaGestores) {
    this.listaGestores = listaGestores;
  }

  public TipoGestion getTipoSeleccionado() {
    return tipoSeleccionado;
  }

  public void setTipoSeleccionado(TipoGestion tipoSeleccionado) {
    this.tipoSeleccionado = tipoSeleccionado;
  }

  public Gestor getGestorSeleccionado() {
    return gestorSeleccionado;
  }

  public void setGestorSeleccionado(Gestor gestorSeleccionado) {
    this.gestorSeleccionado = gestorSeleccionado;
  }

  public List<Gestion> getListaGestiones() {
    return listaGestiones;
  }

  public void setListaGestiones(List<Gestion> listaGestiones) {
    this.listaGestiones = listaGestiones;
  }

  public Date getFechaInicio() {
    return fechaInicio;
  }

  public void setFechaInicio(Date fechaInicio) {
    this.fechaInicio = fechaInicio;
  }

  public Date getFechaFin() {
    return fechaFin;
  }

  public void setFechaFin(Date fechaFin) {
    this.fechaFin = fechaFin;
  }

  public String getNombreArchivo() {
    return nombreArchivo;
  }

  public void setNombreArchivo(String nombreArchivo) {
    this.nombreArchivo = nombreArchivo;
  }

  public boolean isPermitirExport() {
    return permitirExport;
  }

  public void setPermitirExport(boolean permitirExport) {
    this.permitirExport = permitirExport;
  }

  public Producto getProductoSeleccionado() {
    return productoSeleccionado;
  }

  public void setProductoSeleccionado(Producto productoSeleccionado) {
    this.productoSeleccionado = productoSeleccionado;
  }

  public List<Producto> getListaProductos() {
    return listaProductos;
  }

  public void setListaProductos(List<Producto> listaProductos) {
    this.listaProductos = listaProductos;
  }

  public Institucion getInstitucionSeleccionada() {
    return institucionSeleccionada;
  }

  public void setInstitucionSeleccionada(Institucion institucionSeleccionada) {
    this.institucionSeleccionada = institucionSeleccionada;
  }

  public List<Institucion> getListaInstituciones() {
    return listaInstituciones;
  }

  public void setListaInstituciones(List<Institucion> listaInstituciones) {
    this.listaInstituciones = listaInstituciones;
  }

  public boolean isHabilitaGestor() {
    return habilitaGestor;
  }

  public void setHabilitaGestor(boolean habilitaGestor) {
    this.habilitaGestor = habilitaGestor;
  }

  public boolean isHabilitaTipoGestion() {
    return habilitaTipoGestion;
  }

  public void setHabilitaTipoGestion(boolean habilitaTipoGestion) {
    this.habilitaTipoGestion = habilitaTipoGestion;
  }

  public boolean isHabilitaInstitucion() {
    return habilitaInstitucion;
  }

  public void setHabilitaInstitucion(boolean habilitaInstitucion) {
    this.habilitaInstitucion = habilitaInstitucion;
  }

  public boolean isHabilitaTabla() {
    return habilitaTabla;
  }

  public void setHabilitaTabla(boolean habilitaTabla) {
    this.habilitaTabla = habilitaTabla;
  }

  public boolean isHabilitaProducto() {
    return habilitaProducto;
  }

  public void setHabilitaProducto(boolean habilitaProducto) {
    this.habilitaProducto = habilitaProducto;
  }

}
