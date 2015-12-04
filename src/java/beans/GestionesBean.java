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
import dto.Gestion;
import dto.Gestor;
import dto.Institucion;
import dto.Producto;
import impl.GestionIMPL;
import impl.GestorIMPL;
import impl.InstitucionIMPL;
import impl.ProductoIMPL;
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
import util.constantes.Gestiones;

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
  private int idDespacho;
  private boolean permitirExport;
  private boolean permitirBoton;
  private String tipoSeleccionado;
  private String nombreArchivo;
  private Gestor gestorSeleccionado;
  private Date fechaInicio;
  private Date fechaFin;
  private Producto productoSeleccionado;
  private Institucion institucionSeleccionada;
  private List<String> listaTipos;
  private List<Gestor> listaGestores;
  private List<Gestion> listaGestiones;
  private List<Producto> listaProductos;
  private List<Institucion> listaInstituciones;
  private GestorDAO gestorDao;
  private GestionDAO gestionDao;
  private ProductoDAO productoDao;
  private InstitucionDAO institucionDao;

  public GestionesBean() {
    permitirExport = false;
    permitirBoton = false;
    idDespacho = indexBean.getUsuario().getDespacho().getIdDespacho();
    gestorSeleccionado = new Gestor();
    productoSeleccionado = new Producto();
    institucionSeleccionada = new Institucion();
    listaGestiones = new ArrayList();
    listaTipos = new ArrayList();
    listaGestores = new ArrayList();
    listaProductos = new ArrayList();
    listaInstituciones = new ArrayList();
    gestorDao = new GestorIMPL();
    gestionDao = new GestionIMPL();
    productoDao = new ProductoIMPL();
    institucionDao = new InstitucionIMPL();
    listaGestores = gestorDao.buscarPorDespacho(idDespacho);
    listaTipos = Gestiones.TIPO_GESTION;
    listaInstituciones = institucionDao.buscarInstitucionesPorDespacho(idDespacho);
  }

  // METODO QUE OBTIENE LA LISTA DE PRODUCTOS DE ACUERDO A LA INSTITUCION SELECCIONADA
  public void obtenerProductos() {
    listaProductos = productoDao.buscarProductosPorInstitucion(institucionSeleccionada.getIdInstitucion());
  }

  // METODO QUE BUSCA TODAS LAS GESTIONES SEGUN PARAMETROS INGRESADOS
  public void buscar() {
    int idGestor = gestorSeleccionado.getIdGestor();
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String f1 = df.format(fechaInicio);
    String f2 = df.format(fechaFin);
    String tipo;
    String producto;
    String institucion;
    if (tipoSeleccionado == null) {
      tipoSeleccionado = "";
      tipo = "Todas las gestiones";
    } else {
      tipo = tipoSeleccionado;
    }
    if (productoSeleccionado.getNombre() == null) {
      producto = "";
    } else {
      producto = productoSeleccionado.getNombre();
    }
    if (institucionSeleccionada.getNombreCorto() == null) {
      institucion = "";
    } else {
      institucion = institucionSeleccionada.getNombreCorto();
    }
    if (idGestor == 0) {
      listaGestiones = gestionDao.buscarGestionesPorDespacho(idDespacho, fechaInicio, fechaFin, tipoSeleccionado, institucion, producto);
      nombreArchivo = "Gestiones-Todos los gestores-" + tipo + "-" + producto + "-" + f1 + "-" + f2;
    } else {
      listaGestiones = gestionDao.buscarGestionesPorGestor(idGestor, fechaInicio, fechaFin, tipoSeleccionado, institucion, producto);
      nombreArchivo = "Gestiones-" + listaGestores.get(idGestor).toString() + "-" + tipo + "-" + producto + "-" + f1 + "-" + f2;
    }
    if (!listaGestiones.isEmpty()) {
      permitirExport = true;
    }
    permitirBoton = true;
    RequestContext.getCurrentInstance().update("formGestionesAdmin");
  }

  // METODO QUE MANDA EL NOMBRE DEL ARCHIVO YA PRECARGADO. ADVIERTE SI NO HAY DATOS EN EL ARCHIVO
  public String nombrarArchivo() {
    if (listaGestiones.isEmpty()) {
      FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta.", "Se ha exportado un archivo sin gestiones");
      FacesContext.getCurrentInstance().addMessage(null, msg);
      return "Sin gestiones";
    } else {
      FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se exportaron las gestiones correctamente.");
      FacesContext.getCurrentInstance().addMessage(null, msg);
      return nombreArchivo;
    }
  }

// ***********************************************************************************************************************
// ***********************************************************************************************************************
// ***********************************************************************************************************************
// GETTERS & SETTERS
  public List<String> getListaTipos() {
    return listaTipos;
  }

  public void setListaTipos(List<String> listaTipos) {
    this.listaTipos = listaTipos;
  }

  public List<Gestor> getListaGestores() {
    return listaGestores;
  }

  public void setListaGestores(List<Gestor> listaGestores) {
    this.listaGestores = listaGestores;
  }

  public String getTipoSeleccionado() {
    return tipoSeleccionado;
  }

  public void setTipoSeleccionado(String tipoSeleccionado) {
    this.tipoSeleccionado = tipoSeleccionado;
  }

  public Gestor getGestorSeleccionado() {
    return gestorSeleccionado;
  }

  public void setGestorSeleccionado(Gestor gestorSeleccionado) {
    this.gestorSeleccionado = gestorSeleccionado;
  }

  public GestorDAO getGestorDao() {
    return gestorDao;
  }

  public void setGestorDao(GestorDAO gestorDao) {
    this.gestorDao = gestorDao;
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

  public GestionDAO getGestionDao() {
    return gestionDao;
  }

  public void setGestionDao(GestionDAO gestionDao) {
    this.gestionDao = gestionDao;
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

  public ELContext getElContext() {
    return elContext;
  }

  public void setElContext(ELContext elContext) {
    this.elContext = elContext;
  }

  public IndexBean getIndexBean() {
    return indexBean;
  }

  public void setIndexBean(IndexBean indexBean) {
    this.indexBean = indexBean;
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

  public ProductoDAO getProductoDao() {
    return productoDao;
  }

  public void setProductoDao(ProductoDAO productoDao) {
    this.productoDao = productoDao;
  }

  public InstitucionDAO getInstitucionDao() {
    return institucionDao;
  }

  public void setInstitucionDao(InstitucionDAO institucionDao) {
    this.institucionDao = institucionDao;
  }

  public boolean isPermitirBoton() {
    return permitirBoton;
  }

  public void setPermitirBoton(boolean permitirBoton) {
    this.permitirBoton = permitirBoton;
  }

}
