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
@ManagedBean(name = "barraProgresoAdminBean")
@SessionScoped

public class BarraProgresoAdminBean implements Serializable {

  private final CreditoDAO creditoDao;
  private final GestionDAO gestionDao;
  private final PagoDAO pagoDao;

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  public BarraProgresoAdminBean() {
    creditoDao = new CreditoIMPL();
    gestionDao = new GestionIMPL();
    pagoDao = new PagoIMPL();
  }

  public String calcularCreditos() {
    return creditoDao.contarCreditosActivos(indexBean.getUsuario().getDespacho().getIdDespacho()).toString();
  }

  public String calcularVisitas() {
    return gestionDao.calcularVisitasDomiciliariasPorDespacho(indexBean.getUsuario().getDespacho().getIdDespacho()).toString();
  }

  public String calcularPagos() {
    return pagoDao.calcularPagosRealizados().toString();
  }

  public String calcularRecuperacion() {
    return pagoDao.calcularRecuperacionDeInstitucion().toString() + " %";
  }

}
