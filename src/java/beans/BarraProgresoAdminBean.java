/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.CreditoDAO;
import dao.EmpresaDAO;
import dao.SujetoDAO;
import dao.GestionDAO;
import impl.CreditoIMPL;
import impl.EmpresaIMPL;
import impl.GestionIMPL;
import impl.SujetoIMPL;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "barraProgresoAdminBean")
@SessionScoped

public class BarraProgresoAdminBean implements Serializable {

  private CreditoDAO creditoDao;
  private GestionDAO gestionDao;
  private SujetoDAO sujetoDao;
  private EmpresaDAO empresaDao;

  public BarraProgresoAdminBean() {
    creditoDao = new CreditoIMPL();
    gestionDao = new GestionIMPL();
    sujetoDao = new SujetoIMPL();
    empresaDao = new EmpresaIMPL();
  }

  public String calcularCreditos() {
    Number total = creditoDao.contarCreditosActivos();
    String creditos = total.toString();
    Logs.log.debug("************ CONSOLA SIGERWEB ****************");
    Logs.log.debug("Existen " + total + " creditos activos en el sistema");
    return creditos;
  }

  public String calcularVisitas() {
    Number total = gestionDao.calcularVisitasDomiciliarias();
    String visitas = total.toString();
    Logs.log.debug("************ CONSOLA SIGERWEB ****************");
    Logs.log.debug("Existen " + total + " creditos activos en el sistema");
    return visitas;
  }

  public String calcularPagos() {
    Number total = sujetoDao.calcularPagosRealizados();
    String pagos = total.toString();
    Logs.log.debug("************ CONSOLA SIGERWEB ****************");
    Logs.log.debug("Existen " + total + " creditos activos en el sistema");
    return pagos;
  }

  public String calcularRecuperacion() {
    Number total = empresaDao.calcularRecuperacionDeEmpresa();
    String recuperacion = total.toString();
    Logs.log.debug("************ CONSOLA SIGERWEB ****************");
    Logs.log.debug("Existen " + total + " creditos activos en el sistema");
    return (recuperacion + " %");
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

  public EmpresaDAO getEmpresaDao() {
    return empresaDao;
  }

  public void setEmpresaDao(EmpresaDAO empresaDao) {
    this.empresaDao = empresaDao;
  }

}