/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.CreditoDAO;
import dao.InstitucionDAO;
import dao.GestionDAO;
import dao.SujetoDAO;
import impl.CreditoIMPL;
import impl.InstitucionIMPL;
import impl.GestionIMPL;
import impl.SujetoIMPL;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "barraProgresoGestorBean")
@SessionScoped

public class BarraProgresoGestorBean {

  private CreditoDAO creditoDao;
  private GestionDAO gestionDao;
  private SujetoDAO sujetoDao;
  private InstitucionDAO empresaDao;

  public BarraProgresoGestorBean() {
    creditoDao = new CreditoIMPL();
    gestionDao = new GestionIMPL();
    sujetoDao = new SujetoIMPL();
    empresaDao = new InstitucionIMPL();
  }

  // llamada a otros beans
  @ManagedProperty(value = "#{indexBean}")
  private IndexBean indexBean;

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
    Number total = sujetoDao.calcularPagosPorAprobarPorGestor(indexBean.getUsuario().getIdUsuario());
    String pagos = total.toString();
    Logs.log.info("El gestor " + indexBean.getUsuario().getNombre() + " " + indexBean.getUsuario().getPaterno() + " tiene " + total + " pagos por aprobar");
    return pagos;
  }

  public String calcularRecuperacionPorGestor() {
    Number total = empresaDao.calcularRecuperacionPorGestor(indexBean.getUsuario().getIdUsuario());
    String pagos = total.toString();
    Logs.log.info("El gestor " + indexBean.getUsuario().getNombre() + " " + indexBean.getUsuario().getPaterno() + " ha recuperado $" + total + " este mes");
    return pagos;
  }

  public CreditoDAO getCreditoDao() {
    return creditoDao;
  }

  public void setCreditoDao(CreditoDAO creditoDao) {
    this.creditoDao = creditoDao;
  }

  public GestionDAO getGestionDao() {
    return gestionDao;
  }

  public void setGestionDao(GestionDAO gestionDao) {
    this.gestionDao = gestionDao;
  }

  public SujetoDAO getSujetoDao() {
    return sujetoDao;
  }

  public void setSujetoDao(SujetoDAO sujetoDao) {
    this.sujetoDao = sujetoDao;
  }

  public InstitucionDAO getEmpresaDao() {
    return empresaDao;
  }

  public void setEmpresaDao(InstitucionDAO empresaDao) {
    this.empresaDao = empresaDao;
  }

  public IndexBean getIndexBean() {
    return indexBean;
  }

  public void setIndexBean(IndexBean indexBean) {
    this.indexBean = indexBean;
  }

}
