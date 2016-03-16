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
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "barraProgresoGestorBean")
@SessionScoped

public class BarraProgresoGestorBean {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  // VARIABLES DE CLASE
  private final CreditoDAO creditoDao;
  private final GestionDAO gestionDao;
  private final PagoDAO pagoDao;

  // CONSTRUCTOR
  public BarraProgresoGestorBean() {
    creditoDao = new CreditoIMPL();
    gestionDao = new GestionIMPL();
    pagoDao = new PagoIMPL();
  }

  public String calcularCuentasActivas() {
    return creditoDao.contarCreditosActivosPorGestor(indexBean.getUsuario().getIdUsuario()).toString();
  }

  public String calcularVisitasPorGestor() {
    return gestionDao.calcularVisitasDomiciliariasPorGestor(indexBean.getUsuario().getIdUsuario()).toString();

  }

  public String calcularPagosPorAprobarPorGestor() {
    return pagoDao.calcularPagosPorAprobarPorGestor(indexBean.getUsuario().getIdUsuario()).toString();
  }

  public String calcularRecuperacionPorGestor() {
    return pagoDao.calcularRecuperacionPorGestor(indexBean.getUsuario().getIdUsuario()).toString();
  }

}
