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
@ManagedBean(name = "progresoGestorBean")
@ViewScoped

public class ProgresoGestorBean implements Serializable{

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  // VARIABLES DE CLASE
  private final CreditoDAO creditoDao;
  private final GestionDAO gestionDao;
  private final PagoDAO pagoDao;
  private final ImpresionDAO impresionDao;

  // CONSTRUCTOR
  public ProgresoGestorBean() {
    creditoDao = new CreditoIMPL();
    gestionDao = new GestionIMPL();
    pagoDao = new PagoIMPL();
    impresionDao = new ImpresionIMPL();
  }

  public String calcularCuentasActivas() {
    return creditoDao.contarCreditosActivosPorGestor(indexBean.getUsuario().getIdUsuario()).toString();
  }

  public String calcularVisitasPorGestor() {
    return impresionDao.calcularVisitasDomiciliariasPorGestor(indexBean.getUsuario().getDespacho().getIdDespacho(), indexBean.getUsuario().getIdUsuario()).toString();
  }

  public String calcularPagosPorAprobarPorGestor() {
    return Float.toString(pagoDao.calcularSaldoPendienteGestor(indexBean.getUsuario().getIdUsuario()));
  }

  public String calcularRecuperacionPorGestor() {
    return "NaN";
    //return String.format("%,2.6f", pagoDao.calcularRecuperacionGestor(indexBean.getUsuario().getIdUsuario())) + " %";
  }
  
  public String gestionesHoy(){
    return gestionDao.calcularGestionesHoyPorGestor(indexBean.getUsuario().getIdUsuario()).toString();
  }
  
  public String saldoAprobadoHoy(){
    return Float.toString(pagoDao.calcularMontoAprobadoGestorHoy(indexBean.getUsuario().getIdUsuario()));
  }
  
}
