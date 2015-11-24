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
  private List<String> listaTipoSujetos;
  private List<String> listaSujetos;
  private List<String> listaEstatus;
  private String tipoSeleccionado;
  private String lugarSeleccionado;
  private String asuntoSeleccionado;
  private String tipoSujetoSeleccionado;
  private String sujetoSeleccionado;
  private String estatusSeleccionado;

  // CONSTRUCTOR
  public GestionBean() {
    listaTipos = new ArrayList();
    listaDonde = new ArrayList();
    listaAsuntos = new ArrayList();
    listaTipoSujetos = new ArrayList();
    listaSujetos = new ArrayList();
    listaEstatus = new ArrayList();
    listaTipos = Gestiones.TIPO_GESTION;
  }

  public void preparaDonde() {
    switch(tipoSeleccionado){
      case "VISITA DOMICILIARIA":
        listaDonde = Gestiones.DONDE_VISITA;
        break;
      case "TELEFONIA":
        listaDonde = Gestiones.DONDE_TELEFONIA;
        break;
      case "CORPORATIVO":
        listaDonde = Gestiones.DONDE_CORPORATIVO;
        break;
    }
  }

  public void preparaAsunto() {
    listaAsuntos = Gestiones.ASUNTO;
  }

  public void preparaTipoSujeto() {
    listaTipoSujetos = Gestiones.TIPO_SUJETOS;
  }

  public void preparaSujetos() {
    switch (tipoSujetoSeleccionado) {
      case "TITULAR":
        break;
      case "DIRECTOS":
        listaSujetos = Gestiones.SUJETOS_DIRECTOS;
        break;
      case "LATERALES":
        listaSujetos = Gestiones.SUJETOS_LATERALES;
        break;
      case "LEGALES":
        listaSujetos = Gestiones.SUJETOS_LEGALES;
        break;
      case "AMISTADES":
        listaSujetos = Gestiones.SUJETOS_AMISTADES;
        break;
      case "LABORALES":
        listaSujetos = Gestiones.SUJETOS_LABORALES;
        break;
      case "REFERENCIAS":
        listaSujetos = Gestiones.SUJETOS_REFERENCIAS;
        break;
    }
  }
  
  public void preparaEstatus(){
    listaEstatus = Gestiones.ESTATUS_INFORMATIVO;
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

  public List<String> getListaTipoSujetos() {
    return listaTipoSujetos;
  }

  public void setListaTipoSujetos(List<String> listaTipoSujetos) {
    this.listaTipoSujetos = listaTipoSujetos;
  }

  public List<String> getListaSujetos() {
    return listaSujetos;
  }

  public void setListaSujetos(List<String> listaSujetos) {
    this.listaSujetos = listaSujetos;
  }

  public String getTipoSujetoSeleccionado() {
    return tipoSujetoSeleccionado;
  }

  public void setTipoSujetoSeleccionado(String tipoSujetoSeleccionado) {
    this.tipoSujetoSeleccionado = tipoSujetoSeleccionado;
  }

  public String getSujetoSeleccionado() {
    return sujetoSeleccionado;
  }

  public void setSujetoSeleccionado(String sujetoSeleccionado) {
    this.sujetoSeleccionado = sujetoSeleccionado;
  }

  public List<String> getListaEstatus() {
    return listaEstatus;
  }

  public void setListaEstatus(List<String> listaEstatus) {
    this.listaEstatus = listaEstatus;
  }

  public String getEstatusSeleccionado() {
    return estatusSeleccionado;
  }

  public void setEstatusSeleccionado(String estatusSeleccionado) {
    this.estatusSeleccionado = estatusSeleccionado;
  }

}
