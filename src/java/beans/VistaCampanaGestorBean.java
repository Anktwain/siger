/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.CreditoDAO;
import dao.GestionDAO;
import dto.Credito;
import impl.CreditoIMPL;
import impl.GestionIMPL;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "vistaCampanaGestorBean")
@ViewScoped
public class VistaCampanaGestorBean {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  CuentasGestorBean cuentasGestorBean = (CuentasGestorBean) elContext.getELResolver().getValue(elContext, null, "cuentasGestorBean");

  // VARIABLES DE CLASE
  private int progreso;
  private float porcentajeProgreso;
  private int posicion;
  private int totalCuentas;
  private Credito creditoActual;
  private List<Credito> creditos;
  private final CreditoDAO creditoDao;
  private final GestionDAO gestionDao;

  // CONSTRUCTOR
  public VistaCampanaGestorBean() {
    creditos = new ArrayList();
    creditoDao = new CreditoIMPL();
    gestionDao = new GestionIMPL();
    obtenerDatos();
  }

  // METODO QUE CARGA LOS DATOS NECESARIOS
  public final void obtenerDatos() {
    cuentasGestorBean.creditosCampana = creditoDao.buscarCreditosPorCampanaGestor(cuentasGestorBean.getSeleccion().getIdCampana(), cuentasGestorBean.indexBean.getUsuario().getIdUsuario());
    cuentasGestorBean.setCreditoActual(cuentasGestorBean.creditosCampana.get(cuentasGestorBean.getPosicion()));
    totalCuentas = cuentasGestorBean.creditosCampana.size();
    checarProgreso();
  }

  // METODO QUE VERIFICA EL PROGRESO DE ESTA CAMPAÑA
  public void checarProgreso() {
    int contador = 0;
    for (int i = 0; i < (cuentasGestorBean.creditosCampana.size()); i++) {
      boolean ok = gestionDao.buscarGestionHoy(cuentasGestorBean.creditosCampana.get(i).getIdCredito());
      if (ok) {
        contador = contador + 1;
      }
    }
    progreso = contador;
    float unidad = 100 / (cuentasGestorBean.creditosCampana.size());
    porcentajeProgreso = progreso * unidad;
    if (progreso == cuentasGestorBean.creditosCampana.size()) {
      porcentajeProgreso = 100;
    }
    RequestContext.getCurrentInstance().update("progresoGestorForm");
  }

  //
  public void anterior() {
    if (cuentasGestorBean.creditosCampana.size() > 1) {
      posicion = cuentasGestorBean.getPosicion();
      if (posicion == 0) {
        posicion = cuentasGestorBean.creditosCampana.size() - 1;
        obtenerDatos();
      } else if (posicion > 0 && posicion <= (cuentasGestorBean.creditosCampana.size() - 1)) {
        posicion = posicion - 1;
      }
      cuentasGestorBean.setPosicion(posicion);
      obtenerDatos();
    }
    actualiza();
  }

  //
  public void siguiente() {
    if (cuentasGestorBean.creditosCampana.size() > 1) {
      posicion = cuentasGestorBean.getPosicion();
      if (posicion >= 0 && (posicion < (cuentasGestorBean.creditosCampana.size() - 1))) {
        posicion = posicion + 1;
        obtenerDatos();
      } else if (posicion == (cuentasGestorBean.creditosCampana.size() - 1)) {
        posicion = 0;
      }
      cuentasGestorBean.setPosicion(posicion);
      obtenerDatos();
    }
    actualiza();
  }

  // METODO QUE ACTUALIZA LOS COMPONENTES DE LA VISTA
  public void actualiza() {
    RequestContext.getCurrentInstance().update("progresoGestorForm");
    RequestContext.getCurrentInstance().update("datosDeudorVistaCreditoGestorForm");
    RequestContext.getCurrentInstance().update("datosPrimariosVistaCreditoGestorForm");
    RequestContext.getCurrentInstance().update("formTablaMarcajesGestor");
    RequestContext.getCurrentInstance().update("dlgNuevaPromesaForm");
    FacesContext context = FacesContext.getCurrentInstance();
    try {
      context.getExternalContext().redirect("vistaCampanaActual.xhtml");
    } catch (IOException ioe) {
    }
  }

  // GETTERS & SETTERS
  public Credito getCreditoActual() {
    return creditoActual;
  }

  public void setCreditoActual(Credito creditoActual) {
    this.creditoActual = creditoActual;
  }

  public int getProgreso() {
    return progreso;
  }

  public void setProgreso(int progreso) {
    this.progreso = progreso;
  }

  public int getTotalCuentas() {
    return totalCuentas;
  }

  public void setTotalCuentas(int totalCuentas) {
    this.totalCuentas = totalCuentas;
  }

  public float getPorcentajeProgreso() {
    return porcentajeProgreso;
  }

  public void setPorcentajeProgreso(float porcentajeProgreso) {
    this.porcentajeProgreso = porcentajeProgreso;
  }

  public int getPosicion() {
    return posicion;
  }

  public void setPosicion(int posicion) {
    this.posicion = posicion;
  }

}
