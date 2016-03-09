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
import util.log.Logs;

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
    Number total = creditoDao.contarCreditosActivosPorGestor(indexBean.getUsuario().getIdUsuario());
    String creditos = total.toString();
    Logs.log.info("Existen " + total + " creditos activos en el sistema para el gestor " + indexBean.getUsuario().getNombre() + " " + indexBean.getUsuario().getPaterno());
    return creditos;
  }

  public String calcularVisitasPorGestor() {
    Number total = gestionDao.calcularVisitasDomiciliariasPorGestor(indexBean.getUsuario().getIdUsuario());
    String visitas = total.toString();
    Logs.log.info("El gestor " + indexBean.getUsuario().getNombre() + " " + indexBean.getUsuario().getPaterno() + " ha realizado " + total + " visitas este mes");
    return visitas;

  }

  public String calcularPagosPorAprobarPorGestor() {
    Number total = pagoDao.calcularPagosPorAprobarPorGestor(indexBean.getUsuario().getIdUsuario());
    String pagos = total.toString();
    Logs.log.info("El gestor " + indexBean.getUsuario().getNombre() + " " + indexBean.getUsuario().getPaterno() + " tiene " + total + " pagos por aprobar");
    return pagos;
  }

  public String calcularRecuperacionPorGestor() {
    Number total = pagoDao.calcularRecuperacionPorGestor(indexBean.getUsuario().getIdUsuario());
    String pagos = total.toString();
    Logs.log.info("El gestor " + indexBean.getUsuario().getNombre() + " " + indexBean.getUsuario().getPaterno() + " ha recuperado $" + total + " este mes");
    return pagos;
  }

}
