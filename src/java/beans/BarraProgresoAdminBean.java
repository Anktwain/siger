/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.CreditoDAO;
import dao.InstitucionDAO;
import dao.SujetoDAO;
import dao.GestionDAO;
import impl.CreditoIMPL;
import impl.InstitucionIMPL;
import impl.GestionIMPL;
import impl.SujetoIMPL;
import java.io.Serializable;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "barraProgresoAdminBean")
@SessionScoped

public class BarraProgresoAdminBean implements Serializable {

  private final CreditoDAO creditoDao;
  private final GestionDAO gestionDao;
  private final SujetoDAO sujetoDao;
  private final InstitucionDAO institucionDao;
  private final int idDespacho;

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  public BarraProgresoAdminBean() {
    creditoDao = new CreditoIMPL();
    gestionDao = new GestionIMPL();
    sujetoDao = new SujetoIMPL();
    institucionDao = new InstitucionIMPL();
    idDespacho = indexBean.getUsuario().getDespacho().getIdDespacho();
  }

  public String calcularCreditos() {
    Number total = creditoDao.contarCreditosActivos(idDespacho);
    String creditos = total.toString();
    Logs.log.info("Existen " + total + " creditos activos en el sistema");
    return creditos;
  }

  public String calcularVisitas() {
    Number total = gestionDao.calcularVisitasDomiciliariasPorDespacho(idDespacho);
    String visitas = total.toString();
    Logs.log.info("Se han hecho " + total + " visitas domiciliarias");
    return visitas;
  }

  public String calcularPagos() {
    Number total = sujetoDao.calcularPagosRealizados();
    String pagos = total.toString();
    Logs.log.info("Se han realizado " + total + " pagos");
    return pagos;
  }

  public String calcularRecuperacion() {
    Number total = institucionDao.calcularRecuperacionDeInstitucion();
    String recuperacion = total.toString();
    Logs.log.info("Se ha recuperado un %" + total + " del saldo a recuperar");
    return (recuperacion + " %");
  }

}
