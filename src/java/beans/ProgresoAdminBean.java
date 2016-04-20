/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.CreditoDAO;
import dao.GestionDAO;
import dao.PagoDAO;
import impl.CreditoIMPL;
import impl.GestionIMPL;
import impl.PagoIMPL;
import java.io.Serializable;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "progresoAdminBean")
@SessionScoped

public class ProgresoAdminBean implements Serializable {

  private final CreditoDAO creditoDao;
  private final GestionDAO gestionDao;
  private final PagoDAO pagoDao;

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  public ProgresoAdminBean() {
    creditoDao = new CreditoIMPL();
    gestionDao = new GestionIMPL();
    pagoDao = new PagoIMPL();
  }

  public String calcularCreditos() {
    return creditoDao.contarCreditosActivos(indexBean.getUsuario().getDespacho().getIdDespacho()).toString();
  }

  public String calcularVisitas() {
    return "";
    //return gestionDao.calcularVisitasDomiciliariasPorDespacho(indexBean.getUsuario().getDespacho().getIdDespacho()).toString();
  }

  public String calcularPagosPorAprobar() {
    return pagoDao.calcularPagosPendientes(indexBean.getUsuario().getDespacho().getIdDespacho()).toString();
  }

  public String calcularRecuperacion() {
    return pagoDao.calcularRecuperacionDespacho(indexBean.getUsuario().getDespacho().getIdDespacho()).toString() + " %";
  }

  public String gestionesHoy() {
    return gestionDao.calcularGestionesHoyPorDespacho(indexBean.getUsuario().getDespacho().getIdDespacho()).toString();
  }

  public String saldoAprobadoHoy() {
    return pagoDao.calcularSaldoAprobadoHoy(indexBean.getUsuario().getDespacho().getIdDespacho()).toString();
  }

  public String gestorDelDiaPagos() {
    try {
      return pagoDao.obtenerGestorDelDia(indexBean.getUsuario().getDespacho().getIdDespacho());
    } catch (Exception e) {
      return "";
    }
  }

  public String gestorDelDiaGestiones() {
    try {
      return gestionDao.obtenerGestorDelDia(indexBean.getUsuario().getDespacho().getIdDespacho());
    } catch (Exception e) {
      return "";
    }
  }

}
