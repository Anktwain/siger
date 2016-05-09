/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.CreditoDAO;
import dao.GestionDAO;
import dao.ImpresionDAO;
import dao.PagoDAO;
import impl.CreditoIMPL;
import impl.GestionIMPL;
import impl.ImpresionIMPL;
import impl.PagoIMPL;
import java.io.Serializable;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "progresoAdminBean")
@ViewScoped

public class ProgresoAdminBean implements Serializable {
  
  private final CreditoDAO creditoDao;
  private final GestionDAO gestionDao;
  private final PagoDAO pagoDao;
  private final ImpresionDAO impresionDao;

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");
  
  public ProgresoAdminBean() {
    creditoDao = new CreditoIMPL();
    gestionDao = new GestionIMPL();
    pagoDao = new PagoIMPL();
    impresionDao = new ImpresionIMPL();
  }
  
  public String calcularCreditos() {
    return creditoDao.contarCreditosActivos(indexBean.getUsuario().getDespacho().getIdDespacho()).toString();
  }
  
  public String calcularVisitas() {
    return impresionDao.calcularVisitasDomiciliariasPorDespacho(indexBean.getUsuario().getDespacho().getIdDespacho()).toString();
  }
  
  public String calcularPagosPorAprobar() {
    return pagoDao.calcularPagosPendientes(indexBean.getUsuario().getDespacho().getIdDespacho()).toString();
  }
  
  public String calcularRecuperacion() {
    return String.format("%,2.6f", pagoDao.calcularRecuperacionDespacho(indexBean.getUsuario().getDespacho().getIdDespacho())) + " %";
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
