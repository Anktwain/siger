/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.GestionDAO;
import dao.GestorDAO;
import dto.Gestion;
import dto.Gestor;
import impl.GestionIMPL;
import impl.GestorIMPL;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

  // VARIABLES DE CLASE
  private String tipoSeleccionado;
  private String nombreArchivo;
  private Gestor gestorSeleccionado;
  private Date fechaInicio;
  private Date fechaFin;
  private List<String> listaTipos;
  private List<Gestor> listaGestores;
  private List<Gestion> listaGestiones;
  private GestorDAO gestorDao;
  private GestionDAO gestionDAO;

  public GestionesBean() {
    gestorSeleccionado = new Gestor();
    listaTipos = new ArrayList();
    listaGestores = new ArrayList();
    gestorDao = new GestorIMPL();
    gestionDAO = new GestionIMPL();
    listaGestores = gestorDao.buscarTodo();
    listaTipos = Gestiones.TIPO_GESTION;
  }

  // METODO QUE BUSCA TODAS LAS GESTIONES SEGUN PARAMETROS INGRESADOS
  public void buscar() {
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String f1 = df.format(fechaInicio);
    String f2 = df.format(fechaFin);
    String tipo;
    String gestor;
    if (tipoSeleccionado == null) {
      tipoSeleccionado = "";
      tipo = "Todas las gestiones";
    } else {
      tipo = tipoSeleccionado;
    }
    if (gestorSeleccionado.getIdGestor() == 0) {
      listaGestiones = gestionDAO.buscarTodosGestoresPorDespacho(fechaInicio, fechaFin, tipoSeleccionado);
      nombreArchivo = "Gestiones-Todos los gestores-" + tipo + "-" + f1 + "-" + f2;
    } else {

      if (gestorSeleccionado != null) {
        listaGestiones = gestionDAO.buscarGestionesPorGestor(gestorSeleccionado.getIdGestor(), fechaInicio, fechaFin, tipoSeleccionado);
        nombreArchivo = "Gestiones-" + gestorSeleccionado.getUsuario().getNombreLogin() + "-" + tipo + "-" + f1 + "-" + f2;
      } else {
        nombreArchivo = "Gestiones-Todos los gestores-" + tipo + "-" + f1 + "-" + f2;
      }
    }
  }

  // METODO QUE MANDA EL NOMBRE DEL ARCHIVO YA PRECARGADO. ADVIERTE SI NO HAY DATOS EN EL ARCHIVO
  public String nombrarArchivo() {
    if (listaGestiones == null) {
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

  public GestionDAO getGestionDAO() {
    return gestionDAO;
  }

  public void setGestionDAO(GestionDAO gestionDAO) {
    this.gestionDAO = gestionDAO;
  }

  public String getNombreArchivo() {
    return nombreArchivo;
  }

  public void setNombreArchivo(String nombreArchivo) {
    this.nombreArchivo = nombreArchivo;
  }

}
