/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.DevolucionDAO;
import dao.HistorialDAO;
import dto.tablas.Dev;
import dto.Devolucion;
import impl.DevolucionIMPL;
import impl.HistorialIMPL;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import util.constantes.Devoluciones;

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
  private final HistorialDAO historialDao;
  public List<Dev> retiradosSeleccionados;
  public List<Dev> bandejaSeleccionados;
  public List<Dev> devolucionesSeleccionadas;
  public List<Dev> listaRetirados;
  public List<Dev> listaDevoluciones;
  public List<Dev> listaDevueltos;
  private final int idDespacho;
  private final String admin;

  // CONSTRUCTOR
  public DevolucionBean() {
    devolucionDao = new DevolucionIMPL();
    historialDao = new HistorialIMPL();
    retiradosSeleccionados = new ArrayList<>();
    bandejaSeleccionados = new ArrayList<>();
    devolucionesSeleccionadas = new ArrayList<>();
    listaRetirados = new ArrayList<>();
    listaDevoluciones = new ArrayList<>();
    listaDevueltos = new ArrayList<>();
    idDespacho = indexBean.getUsuario().getDespacho().getIdDespacho();
    admin = indexBean.getUsuario().getNombreLogin();
    obtenerListas();
  }

  // METODO PARA APROBAR UNA DEVOLUCION
  public void aprobar(List<Dev> lista) {
    FacesContext contexto = FacesContext.getCurrentInstance();
    int tam = lista.size();
    if ((!lista.isEmpty()) && (tam >= 1)) {
      boolean ok = true;
      for (int i = 0; i < tam; i++) {
        Devolucion devolucion = devolucionDao.buscarDevolucionPorNumeroCredito(lista.get(i).getNumeroCredito());
        devolucion.setEstatus(Devoluciones.DEVUELTO);
        String quien = indexBean.getUsuario().getNombreLogin();
        devolucion.setRevisor(quien);
        String evento = "El administrador: " + admin + ", aprobo la devolucion del credito";
        ok = devolucionDao.editar(devolucion) && historialDao.insertarHistorial(devolucion.getIdCredito(), evento);
      }
      if (ok) {
        obtenerListas();
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se aprobaron las devoluciones seleccionadas"));
      } else {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudieron aprobar las devoluciones. Contacte con el administrador de base de datos"));
      }
    } else {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No ha seleccionado ninguna devolucion"));
    }
  }

  // METODO PARA RECHAZAR UNA DEVOLUCION
  public void rechazar(List<Dev> lista) {
    FacesContext contexto = FacesContext.getCurrentInstance();
    int tam = lista.size();
    if ((!lista.isEmpty()) && (tam >= 1)) {
      boolean ok = true;
      for (int i = 0; i < tam; i++) {
        Devolucion devolucion = devolucionDao.buscarDevolucionPorNumeroCredito(lista.get(i).getNumeroCredito());
        String evento = "El administrador: " + admin + ", rechazo la devolucion del credito";
        ok = devolucionDao.eliminar(devolucion) && historialDao.insertarHistorial(devolucion.getIdCredito(), evento);
      }
      if (ok) {
        obtenerListas();
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se rechazaron las devoluciones seleccionadas"));
      } else {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudieron rechazar las devoluciones. Contacte con el administrador de base de datos"));
      }
    } else {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No ha seleccionado ninguna devolucion"));
    }
  }

  // METODO PARA PEDIR AL BANCO UNA CONSERVACION
  public void conservar(List<Dev> lista) {
    FacesContext contexto = FacesContext.getCurrentInstance();
    int tam = lista.size();
    if ((!lista.isEmpty()) && (tam >= 1)) {
      boolean ok = true;
      for (int i = 0; i < tam; i++) {
        Devolucion devolucion = devolucionDao.buscarDevolucionPorNumeroCredito(lista.get(i).getNumeroCredito());
        devolucion.setEstatus(Devoluciones.ESPERA_CONSERVACION);
        devolucion.setSolicitante(indexBean.getUsuario().getNombreLogin());
        String evento = "El administrador: " + admin + ", solicito la conservacion del credito";
        ok = devolucionDao.editar(devolucion) && historialDao.insertarHistorial(devolucion.getIdCredito(), evento);
      }
      if (ok) {
        obtenerListas();
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se han conservado los creditos seleccionados"));
      } else {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudieron aprobar las devoluciones. Contacte con el administrador de base de datos"));
      }
    } else {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No ha seleccionado ninguna devolucion"));
    }
  }

  // METODO PARA REACTIVAR UN CREDITO QUE ESTUVO DEVUELTO PREVIAMENTE
  public void reactivar() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    int tam = devolucionesSeleccionadas.size();
    if ((!devolucionesSeleccionadas.isEmpty()) && (tam >= 1)) {
      boolean ok = true;
      for (int i = 0; i < tam; i++) {
        Devolucion devolucion = devolucionDao.buscarDevolucionPorNumeroCredito(devolucionesSeleccionadas.get(i).getNumeroCredito());
        String evento = "El administrador: " + admin + ", reactivo el credito";
        ok = devolucionDao.eliminar(devolucion) && historialDao.insertarHistorial(devolucion.getIdCredito(), evento);
      }
      if (ok) {
        obtenerListas();
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se reactivaron los creditos seleccionados"));
      } else {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudieron reactivar los creditos. Contacte con el administrador de base de datos"));
      }
    } else {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No ha seleccionado ningun credito"));
    }
  }
  
  public final void obtenerListas() {
    // OBTENER LOS CREDITOS PENDIENTES O EN ESPERA DE CONSERVACION
    listaDevoluciones = devolucionDao.bandejaDevolucionPorDespacho(idDespacho);
    // OBTENER LOS CREDITOS PENDIENTES O EN ESPERA DE CONSERVACION
    listaDevueltos = devolucionDao.devueltosPorDespacho(idDespacho);
    // OBTENER LOS CREDITOS PENDIENTES O EN ESPERA DE CONSERVACION
    listaRetirados = devolucionDao.retiradosBancoPorDespacho(idDespacho);
  }

  // ***********************************************************************************************************************
  // ***********************************************************************************************************************
  // ***********************************************************************************************************************
  // GETTERS & SETTERS
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

  public DevolucionDAO getDevolucionDao() {
    return devolucionDao;
  }

  public void setDevolucionDao(DevolucionDAO devolucionDao) {
    this.devolucionDao = devolucionDao;
  }

  public List<Dev> getRetiradosSeleccionados() {
    return retiradosSeleccionados;
  }

  public void setRetiradosSeleccionados(List<Dev> retiradosSeleccionados) {
    this.retiradosSeleccionados = retiradosSeleccionados;
  }

  public List<Dev> getBandejaSeleccionados() {
    return bandejaSeleccionados;
  }

  public void setBandejaSeleccionados(List<Dev> bandejaSeleccionados) {
    this.bandejaSeleccionados = bandejaSeleccionados;
  }

  public List<Dev> getListaRetirados() {
    return listaRetirados;
  }

  public void setListaRetirados(List<Dev> listaRetirados) {
    this.listaRetirados = listaRetirados;
  }

  public List<Dev> getListaDevoluciones() {
    return listaDevoluciones;
  }

  public void setListaDevoluciones(List<Dev> listaDevoluciones) {
    this.listaDevoluciones = listaDevoluciones;
  }

  public List<Dev> getListaDevueltos() {
    return listaDevueltos;
  }

  public void setListaDevueltos(List<Dev> listaDevueltos) {
    this.listaDevueltos = listaDevueltos;
  }

  public List<Dev> getDevolucionesSeleccionadas() {
    return devolucionesSeleccionadas;
  }

  public void setDevolucionesSeleccionadas(List<Dev> devolucionesSeleccionadas) {
    this.devolucionesSeleccionadas = devolucionesSeleccionadas;
  }

}
