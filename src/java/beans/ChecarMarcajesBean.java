/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.CampanaDAO;
import dao.CreditoDAO;
import dao.DevolucionDAO;
import dao.GestionDAO;
import dao.HistorialDAO;
import dao.SubproductoDAO;
import dto.Credito;
import impl.CampanaIMPL;
import impl.CreditoIMPL;
import impl.DevolucionIMPL;
import impl.GestionIMPL;
import impl.HistorialIMPL;
import impl.SubproductoIMPL;
import java.io.Serializable;
import java.util.List;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import util.constantes.Marcajes;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "checarMarcajesBean")
@ViewScoped
public class ChecarMarcajesBean implements Serializable {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");
  CreditoActualBean creditoActualBean = (CreditoActualBean) elContext.getELResolver().getValue(elContext, null, "creditoActualBean");

  // VARIABLES DE CLASE
  private final Credito creditoActual;
  private final CampanaDAO campanaDao;
  private final GestionDAO gestionDao;
  private final CreditoDAO creditoDao;
  private final HistorialDAO historialDao;
  private final DevolucionDAO devolucionDao;

  //CONSTRUCTOR
  public ChecarMarcajesBean() {
    campanaDao = new CampanaIMPL();
    gestionDao = new GestionIMPL();
    creditoDao = new CreditoIMPL();
    historialDao = new HistorialIMPL();
    devolucionDao = new DevolucionIMPL();
    creditoActual = creditoActualBean.getCreditoActual();
  }

  // METODO QUE VERIFICA EN QUE CAMPAÑA SE ENCUENTRA EL CREDITO
  public String verificarCampaña() {
    return campanaDao.buscarCampanaDelCredito(creditoActual.getIdCredito()).getNombre();
  }

  // METODO QUE VERIFICA SI EL CREDITO TIENE ALGUN MARCAJE
  public String verificaMarcaje() {
    if (creditoActual.getMarcaje().getIdMarcaje() != Marcajes.SIN_MARCAJE) {
      return creditoActual.getMarcaje().getMarcaje();
    } else {
      return "";
    }
  }

  // METODO QUE AVISA AL GESTOR SI LA CUENTA SE HA GESTIONADO HOY
  public String verificaGestionHoy() {
    if (gestionDao.buscarGestionHoy(creditoActual.getIdCredito())) {
      return "Se ha gestionado el dia de hoy";
    } else {
      return "";
    }
  }

  // METODO QUE VERIFICA SI EL CREDITO CAMBIO DE CAMPAÑA EL DIA DE HOY
  public String verificaCambioDeCampaña() {
    if (historialDao.verificarCampioCampañaCredito(creditoActual.getIdCredito())) {
      return "Cambio de campaña hoy";
    } else {
      return "";
    }
    // FALTA IMPLEMENTAR UNA ENTRADA AL HISTORIAL CUANDO CAMBIE DE CAMPAÑA
    // CHECAMOS SI HAY UNA ENTRADA EN EL HISTORIAL CON EL TEXTO "CAMBIO DE CAMPAÑA"

  }

  // METODO QUE VERIFICA SI EL CREDITO TIENE UNA PROMESA DE PAGO EL DIA DE HOY
  public String verificaPagoHoy() {
    List<Credito> lista = creditoDao.buscarCreditosConPromesaDePagoHoy();
    String cadena = "";
    for (int i = 0; i < (lista.size()); i++) {
      if (creditoActual.getNumeroCredito().equals(lista.get(i).getNumeroCredito())) {
        cadena = "Promesa de pago hoy";
      } else {
        cadena = "";
      }
    }
    return cadena;
  }

  // METODO QUE VERIFICA SI UNA CUENTA ES DE SOFOM
  public String verificaSOFOM() {
    if (creditoActual.getSubproducto().getNombre().contains("CREDITO EXPRESS CT")) {
      return "SOFOM CT";
    } else {
      return "";
    }
  }

  //METODO QUE VERIFICA SI LA CUENTA CAMBIO DE GESTOR EL DIA DE HOY
  public String verificaReasignacionHoy() {
    if (gestionDao.buscarReasignacionHoy(creditoActual.getIdCredito())) {
      return "Cambio de gestor hoy";
    } else {
      return "";
    }
  }
  
  // METODO QUE VERIFICA SI EL CREDITO HA SIDO DEVUELTO
  public String verificaDevuelto() {
    if (devolucionDao.esGestionable(creditoActual.getIdCredito())) {
      return "";
    } else {
      return "Inactivo";
    }
  }
}
