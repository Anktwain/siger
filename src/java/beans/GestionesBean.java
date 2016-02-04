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
import dao.UsuarioDAO;
import dto.Gestion;
import dto.Gestor;
import dto.Institucion;
import dto.Producto;
import impl.GestionIMPL;
import impl.GestorIMPL;
import impl.InstitucionIMPL;
import impl.ProductoIMPL;
import impl.UsuarioIMPL;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.el.ELContext;
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
  private final int idDespacho;
  private boolean permitirExport;
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
  private final GestorDAO gestorDao;
  private final GestionDAO gestionDao;
  private final ProductoDAO productoDao;
  private final InstitucionDAO institucionDao;
  private final UsuarioDAO usuarioDao;

  public GestionesBean() {
    permitirExport = true;
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
    usuarioDao = new UsuarioIMPL();
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
    int idInstitucion = institucionSeleccionada.getIdInstitucion();
    int idProducto = productoSeleccionado.getIdProducto();
    if(tipoSeleccionado == null){
      tipoSeleccionado = "";
    }
    String tipoGestion = tipoSeleccionado;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String f1 = df.format(fechaInicio);
    String f2 = df.format(fechaFin);
    if ((idGestor == 0) && (tipoGestion.equals("")) && (idInstitucion == 0) && (idProducto == 0)) {
      System.out.println("CASO 1");
      listaGestiones = gestionDao.buscarGestionesPorDespacho(idDespacho, fechaInicio, fechaFin);
      nombreArchivo = "Gestiones-TodosGestores-TodosTipos-TodasInstituciones-TodosProductos-" + f1 + "-" + f2;
    }
    if ((idGestor != 0) && (tipoGestion.equals("")) && (idInstitucion == 0) && (idProducto == 0)) {
      System.out.println("CASO 2");
      listaGestiones = gestionDao.buscarGestionesPorGestor(idDespacho, idGestor, fechaInicio, fechaFin);
      nombreArchivo = "Gestiones-" + usuarioDao.buscarUsuarioPorIdGestor(idGestor).getNombreLogin() + "-TodosTipos-TodasInstituciones-TodosProductos-" + f1 + "-" + f2;
    }
    if ((idGestor == 0) && (!tipoGestion.equals("")) && (idInstitucion == 0) && (idProducto == 0)) {
      System.out.println("CASO 3");
      listaGestiones = gestionDao.buscarGestionesPorTipo(idDespacho, tipoGestion, fechaInicio, fechaFin);
      nombreArchivo = "Gestiones-TodosGestores-" + tipoGestion + "-TodasInstituciones-TodosProductos-" + f1 + "-" + f2;
    }
    if ((idGestor == 0) && (tipoGestion.equals("")) && (idInstitucion != 0) && (idProducto == 0)) {
      System.out.println("CASO 4");
      listaGestiones = gestionDao.buscarGestionesPorInstitucion(idDespacho, idInstitucion, fechaInicio, fechaFin);
      nombreArchivo = "Gestiones-TodosGestores-TodosTipos-" + institucionSeleccionada.getSujeto().getNombreRazonSocial() + "-TodosProductos-" + f1 + "-" + f2;
    }
    if ((idGestor == 0) && (tipoGestion.equals("")) && (idInstitucion != 0) && (idProducto != 0)) {
      System.out.println("CASO 5");
      listaGestiones = gestionDao.buscarGestionesPorInstitucionProducto(idDespacho, idInstitucion, idProducto, fechaInicio, fechaFin);
      nombreArchivo = "Gestiones-TodosGestores-TodosTipos-" + institucionSeleccionada.getSujeto().getNombreRazonSocial() + "-" + productoSeleccionado.getNombre() + "-" + f1 + "-" + f2;
    }
    if ((idGestor != 0) && (!tipoGestion.equals("")) && (idInstitucion == 0) && (idProducto == 0)) {
      System.out.println("CASO 6");
      listaGestiones = gestionDao.buscarGestionesPorGestorTipo(idDespacho, idGestor, tipoGestion, fechaInicio, fechaFin);
      nombreArchivo = "Gestiones-" + usuarioDao.buscarUsuarioPorIdGestor(idGestor).getNombreLogin() + "-" + tipoGestion + "-TodasInstituciones-TodosProductos-" + f1 + "-" + f2;
    }
    if ((idGestor != 0) && (tipoGestion.equals("")) && (idInstitucion != 0) && (idProducto == 0)) {
      System.out.println("CASO 7");
      listaGestiones = gestionDao.buscarGestionesPorGestorInstitucion(idDespacho, idGestor, idInstitucion, fechaInicio, fechaFin);
      nombreArchivo = "Gestiones-" + usuarioDao.buscarUsuarioPorIdGestor(idGestor).getNombreLogin() + "-TodosTipos-" + institucionSeleccionada.getSujeto().getNombreRazonSocial() + "-TodosProductos-" + f1 + "-" + f2;
    }
    if ((idGestor != 0) && (tipoGestion.equals("")) && (idInstitucion != 0) && (idProducto != 0)) {
      System.out.println("CASO 8");
      listaGestiones = gestionDao.buscarGestionesPorGestorInstitucionProducto(idDespacho, idGestor, idInstitucion, idProducto, fechaInicio, fechaFin);
      nombreArchivo = "Gestiones-" + usuarioDao.buscarUsuarioPorIdGestor(idGestor).getNombreLogin() + "-TodosTipos-" + institucionSeleccionada.getSujeto().getNombreRazonSocial() + "-" + productoSeleccionado.getNombre() + "-" + f1 + "-" + f2;
    }
    if ((idGestor == 0) && (!tipoGestion.equals("")) && (idInstitucion != 0) && (idProducto == 0)) {
      System.out.println("CASO 9");
      listaGestiones = gestionDao.buscarGestionesPorTipoInstitucion(idDespacho, tipoGestion, idInstitucion, fechaInicio, fechaFin);
      nombreArchivo = "Gestiones-TodosGestores-" + tipoGestion + "-" + institucionSeleccionada.getSujeto().getNombreRazonSocial() + "-TodosProductos-" + f1 + "-" + f2;
    }
    if ((idGestor == 0) && (!tipoGestion.equals("")) && (idInstitucion != 0) && (idProducto != 0)) {
      System.out.println("CASO 10");
      listaGestiones = gestionDao.buscarGestionesPorTipoInstitucionProducto(idDespacho, tipoGestion, idInstitucion, idProducto, fechaInicio, fechaFin);
      nombreArchivo = "Gestiones-TodosGestores-" + tipoGestion + "-" + institucionSeleccionada.getSujeto().getNombreRazonSocial() + "-" + productoSeleccionado.getNombre() + "-" + f1 + "-" + f2;
    }
    if ((idGestor != 0) && (!tipoGestion.equals("")) && (idInstitucion != 0) && (idProducto != 0)) {
      System.out.println("CASO 11");
      listaGestiones = gestionDao.buscarGestionesPorGestorTipoInstitucionProducto(idDespacho, idGestor, tipoGestion, idInstitucion, idProducto, fechaInicio, fechaFin);
      nombreArchivo = "Gestiones-" + usuarioDao.buscarUsuarioPorIdGestor(idGestor).getNombreLogin() + "-" + tipoGestion + "-" + institucionSeleccionada.getSujeto().getNombreRazonSocial() + "-" + productoSeleccionado.getNombre() + "-" + f1 + "-" + f2;
    }
    RequestContext.getCurrentInstance().update("formGestionesAdmin");
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

}
