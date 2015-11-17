/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

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
@ManagedBean(name = "gestionBean")
@ViewScoped

public class GestionBean implements Serializable {

  // VARIABLES DE CLASE
  private List<String> listaTipos;
  private List<String> listaDonde;
  private List<String> listaAsuntos;
  private String tipoSeleccionado;
  private String lugarSeleccionado;
  private String asuntoSeleccionado;

  // CONSTRUCTOR
  public GestionBean() {
    listaTipos = new ArrayList();
    listaDonde = new ArrayList();
    listaAsuntos = new ArrayList();
    listaTipos = Gestiones.TIPO_GESTION;
  }

  public void preparaDonde() {
    listaDonde = Gestiones.DONDE_VISITA;
    if (tipoSeleccionado.equals("TELEFONIA")) {
      listaDonde = Gestiones.DONDE_TELEFONIA;
    }
  }
  
  public void preparaAsunto(){
    listaAsuntos = Gestiones.ASUNTO;
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

  public String getTipoSeleccionado() {
    return tipoSeleccionado;
  }

  public void setTipoSeleccionado(String tipoSeleccionado) {
    this.tipoSeleccionado = tipoSeleccionado;
  }

  public List<String> getListaDonde() {
    return listaDonde;
  }

  public void setListaDonde(List<String> listaDonde) {
    this.listaDonde = listaDonde;
  }

  public String getLugarSeleccionado() {
    return lugarSeleccionado;
  }

  public void setLugarSeleccionado(String lugarSeleccionado) {
    this.lugarSeleccionado = lugarSeleccionado;
  }

  public List<String> getListaAsuntos() {
    return listaAsuntos;
  }

  public void setListaAsuntos(List<String> listaAsuntos) {
    this.listaAsuntos = listaAsuntos;
  }

  public String getAsuntoSeleccionado() {
    return asuntoSeleccionado;
  }

  public void setAsuntoSeleccionado(String asuntoSeleccionado) {
    this.asuntoSeleccionado = asuntoSeleccionado;
  }

}
