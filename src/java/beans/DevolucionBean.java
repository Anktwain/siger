/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.DevolucionDAO;
import dto.Devolucion;
import impl.DevolucionIMPL;
import java.io.Serializable;
import java.text.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "devolucionBean")
@ViewScoped
public class DevolucionBean implements Serializable {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  // VARIABLES DE CLASE
  private DevolucionDAO devolucionDao;
  private Devolucion devolucionSeleccionada;
  private List<Devolucion> listaRetirados;
  private List<Devolucion> listaDevoluciones;
  private List<Devolucion> listaDevueltos;

  // CONSTRUCTOR
  public DevolucionBean() {
    devolucionDao = new DevolucionIMPL();
    devolucionSeleccionada = new Devolucion();
    listaRetirados = new ArrayList<>();
    listaDevoluciones = new ArrayList<>();
    listaDevueltos = new ArrayList<>();
    obtenerDevoluciones();
    obtenerDevueltos();
    obtenerRetirados();
  }

  // METODO PARA APROBAR UNA DEVOLUCION
  public void aprobar() {

  }

  // METODO PARA RECHAZAR UNA DEVOLUCION
  public void rechazar() {

  }

  // METODO PARA PEDIR AL BANCO UNA CONSERVACION
  public void conservar() {

  }

  // METODO PARA OBTENER LOS CREDITOS PENDIENTES O EN ESPERA DE CONSERVACION
  public final void obtenerDevoluciones() {
    System.out.println("ENTRO A OBTENER DEVOLUCIONES");
    listaDevoluciones = devolucionDao.bandejaDevolucionPorDespacho(indexBean.getUsuario().getDespacho().getIdDespacho());
  }

  // METODO PARA OBTENER LOS CREDITOS PENDIENTES O EN ESPERA DE CONSERVACION
  public final void obtenerDevueltos() {
    System.out.println("ENTRO A OBTENER DEVUELTOS");
    listaDevueltos = devolucionDao.devueltosPorDespacho(indexBean.getUsuario().getDespacho().getIdDespacho());
  }

  // METODO PARA OBTENER LOS CREDITOS PENDIENTES O EN ESPERA DE CONSERVACION
  public final void obtenerRetirados() {
    System.out.println("ENTRO A OBTENER RETIRADOS");
    listaRetirados = devolucionDao.retiradosBancoPorDespacho(indexBean.getUsuario().getDespacho().getIdDespacho());
  }

  // ***********************************************************************************************************************
  // ***********************************************************************************************************************
  // ***********************************************************************************************************************
  // GETTERS & SETTERS
  public List<Devolucion> getListaDevoluciones() {
    return listaDevoluciones;
  }

  public void setListaDevoluciones(List<Devolucion> listaDevoluciones) {
    this.listaDevoluciones = listaDevoluciones;
  }

  public DevolucionDAO getDevolucionDao() {
    return devolucionDao;
  }

  public void setDevolucionDao(DevolucionDAO devolucionDao) {
    this.devolucionDao = devolucionDao;
  }

  public IndexBean getIndexBean() {
    return indexBean;
  }

  public void setIndexBean(IndexBean indexBean) {
    this.indexBean = indexBean;
  }

  public ELContext getElContext() {
    return elContext;
  }

  public void setElContext(ELContext elContext) {
    this.elContext = elContext;
  }

  public List<Devolucion> getListaRetirados() {
    return listaRetirados;
  }

  public void setListaRetirados(List<Devolucion> listaRetirados) {
    this.listaRetirados = listaRetirados;
  }

  public List<Devolucion> getListaDevueltos() {
    return listaDevueltos;
  }

  public void setListaDevueltos(List<Devolucion> listaDevueltos) {
    this.listaDevueltos = listaDevueltos;
  }

  public Devolucion getDevolucionSeleccionada() {
    return devolucionSeleccionada;
  }

  public void setDevolucionSeleccionada(Devolucion devolucionSeleccionada) {
    this.devolucionSeleccionada = devolucionSeleccionada;
  }

}
