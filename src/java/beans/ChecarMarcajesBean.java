/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.CampanaDAO;
import dao.CreditoDAO;
import dao.GestionDAO;
import dao.HistorialDAO;
import dao.ProductoDAO;
import dto.Credito;
import impl.CampanaIMPL;
import impl.CreditoIMPL;
import impl.GestionIMPL;
import impl.HistorialIMPL;
import impl.ProductoIMPL;
import java.io.Serializable;
import java.util.List;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import util.constantes.Marcajes;
import util.constantes.Perfiles;

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

  // VARIABLES DE CLASE
  private CuentasBean cuentasBean;
  private CuentasGestorBean cuentasGestorBean;
  private Credito creditoActual;
  private final CampanaDAO campanaDao;
  private final GestionDAO gestionDao;
  private final CreditoDAO creditoDao;
  private final ProductoDAO productoDao;
  private final HistorialDAO historialDao;
  private int idCredito;

  //CONSTRUCTOR
  public ChecarMarcajesBean() {
    creditoActual = new Credito();
    campanaDao = new CampanaIMPL();
    gestionDao = new GestionIMPL();
    creditoDao = new CreditoIMPL();
    productoDao = new ProductoIMPL();
    historialDao = new HistorialIMPL();
    verificaUsuario();
  }

  // METODO QUE VERIFICA SI ESTA GESTIONANDO UN ADMINISTRADOR O UN GESTOR
  public final void verificaUsuario() {
    if (indexBean.getUsuario().getPerfil() == Perfiles.GESTOR) {
      cuentasGestorBean = (CuentasGestorBean) elContext.getELResolver().getValue(elContext, null, "cuentasGestorBean");
      creditoActual = cuentasGestorBean.getCreditoActual();
    } else {
      cuentasBean = (CuentasBean) elContext.getELResolver().getValue(elContext, null, "cuentasBean");
      creditoActual = cuentasBean.getCreditoSeleccionado();
    }
    idCredito = creditoActual.getIdCredito();
  }

  // METODO QUE VERIFICA EN QUE CAMPAÑA SE ENCUENTRA EL CREDITO
  public String verificarCampaña() {
    return campanaDao.buscarCampanaDelCredito(creditoActual.getIdCredito()).getNombre();
  }

  // METODO QUE VERIFICA SI EL CREDITO TIENE ALGUN MARCAJE
  public String verificaMarcaje() {
    List<Credito> listaCorreos = creditoDao.buscarPorMarcaje(Marcajes.CORREO_SEPOMEX);
    String cadena = "";
    for (int i = 0; i < (listaCorreos.size()); i++) {
      if (creditoActual.getNumeroCredito().equals(listaCorreos.get(i).getNumeroCredito())) {
        cadena = "Visita domiciliaria";
        break;
      }
    }
    return cadena;
  }

  // METODO QUE AVISA AL GESTOR SI LA CUENTA SE HA GESTIONADO HOY
  public String verificaGestionHoy() {
    if (gestionDao.buscarGestionHoy(idCredito)) {
      return "Se ha gestionado el dia de hoy";
    } else {
      return "";
    }
  }

  // METODO QUE VERIFICA SI EL CREDITO CAMBIO DE CAMPAÑA EL DIA DE HOY
  public String verificaCambioDeCampaña() {
    if (historialDao.verificarCampioCampañaCredito(idCredito)) {
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
    String producto = productoDao.buscarProductoDelCredito(idCredito);
    if (producto.contains("CREDITO EXPRESS CT")) {
      return "SOFOM CT";
    } else {
      return "";
    }
  }
}
