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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import util.constantes.Gestiones;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "gestionesBean")
@ViewScoped
public class GestionesBean implements Serializable {

  private String tipoSeleccionado;
  private Gestor gestorSeleccionado;
  private Date fechaInicio;
  private Date fechaFin;
  private List<String> listaTipos;
  private List<Gestor> listaGestores;
  private List<Gestion> listaGestiones;
  private GestorDAO gestorDao;
  private GestionDAO gestionDAO;

  public GestionesBean() {
    listaTipos = new ArrayList();
    listaGestores = new ArrayList();
    gestorDao = new GestorIMPL();
    gestionDAO = new GestionIMPL();
    listaGestores = gestorDao.buscarTodo();
    listaTipos = Gestiones.TIPO_GESTION;
  }

  // METODO QUE BUSCA TODAS LAS GESTIONES SEGUN PARAMETROS INGRESADOS
  public void buscar() {
    System.out.println("ENTRO A LA FUNCION BUSCAR!!!");
    if (tipoSeleccionado == null) {
      System.out.println("TODAS LAS GESTIONES");
      tipoSeleccionado = "";
    } else {
      if (gestorSeleccionado.getIdGestor() == 0) {
        System.out.println("TODOS LOS GESTORES");
        listaGestiones = gestionDAO.buscarTodosGestoresPorDespacho(fechaInicio, fechaFin, tipoSeleccionado);
        for (int i = 0; i < listaGestiones.size(); i++) {
          System.out.println("GESTION " + (i+1) + ": " + listaGestiones.get(i).getGestion());
        }
        
      } else {
        System.out.println("GESTOR: " + gestorSeleccionado.getUsuario().getNombreLogin());
        listaGestiones = gestionDAO.buscarGestionesPorGestor(gestorSeleccionado.getIdGestor(), fechaInicio, fechaFin, tipoSeleccionado);
        for (int i = 0; i < listaGestiones.size(); i++) {
          System.out.println("GESTION " + (i+1) + ": " + listaGestiones.get(i).getGestion());
        }
      }
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

}
