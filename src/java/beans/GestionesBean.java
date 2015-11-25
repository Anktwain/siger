/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.GestorDAO;
import dto.Gestor;
import impl.GestorIMPL;
import java.io.Serializable;
import java.util.ArrayList;
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
public class GestionesBean implements Serializable{
  private List<String> listaTipos;
  private List<Gestor> listaGestores;
  private List<String> tipoSeleccionado;
  private List<Gestor> gestorSeleccionado;
  private List<Gestiones> listaGestiones;
  private GestorDAO gestorDao;

  public GestionesBean() {
    listaTipos = new ArrayList();
    listaGestores = new ArrayList();
    tipoSeleccionado = new ArrayList();
    gestorSeleccionado = new ArrayList();
    gestorDao = new GestorIMPL();
    listaGestores = gestorDao.buscarTodo();
    listaTipos = Gestiones.TIPO_GESTION;
  }
  
  // METODO QUE BUSCA TODAS LAS GESTIONES SEGUN PARAMETROS INGRESADOS
  public void buscar(){
    
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

  public List<String> getTipoSeleccionado() {
    return tipoSeleccionado;
  }

  public void setTipoSeleccionado(List<String> tipoSeleccionado) {
    this.tipoSeleccionado = tipoSeleccionado;
  }

  public List<Gestor> getGestorSeleccionado() {
    return gestorSeleccionado;
  }

  public void setGestorSeleccionado(List<Gestor> gestorSeleccionado) {
    this.gestorSeleccionado = gestorSeleccionado;
  }

  public GestorDAO getGestorDao() {
    return gestorDao;
  }

  public void setGestorDao(GestorDAO gestorDao) {
    this.gestorDao = gestorDao;
  }
  
}