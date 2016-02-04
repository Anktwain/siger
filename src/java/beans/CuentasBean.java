/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.ConceptoDevolucionDAO;
import dao.CreditoDAO;
import dao.DevolucionDAO;
import dao.HistorialDAO;
import dto.ConceptoDevolucion;
import dto.Credito;
import dto.Devolucion;
import impl.ConceptoDevolucionIMPL;
import impl.CreditoIMPL;
import impl.DevolucionIMPL;
import impl.HistorialIMPL;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import util.constantes.Devoluciones;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "cuentasBean")
@SessionScoped
public class CuentasBean {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  // VARIABLES DE CLASE
  private List<Credito> listaCreditosGestionables;
  private List<Credito> filtrados;
  private Credito creditoSeleccionado;
  private List<ConceptoDevolucion> listaConceptos;
  private final CreditoDAO creditoDao;
  private final DevolucionDAO devolucionDao;
  private final HistorialDAO historialDao;
  private final ConceptoDevolucionDAO conceptoDevolucionDao;
  private ConceptoDevolucion conceptoSeleccionado;
  private String observaciones;
  private final String admin;
  private final int idDespacho;

  //CONSTRUCTOR
  public CuentasBean() {
    creditoDao = new CreditoIMPL();
    devolucionDao = new DevolucionIMPL();
    historialDao = new HistorialIMPL();
    conceptoDevolucionDao = new ConceptoDevolucionIMPL();
    listaCreditosGestionables = new ArrayList();
    creditoSeleccionado = new Credito();
    conceptoSeleccionado = new ConceptoDevolucion();
    listaConceptos = new ArrayList();
    admin = indexBean.getUsuario().getNombreLogin();
    idDespacho = indexBean.getUsuario().getDespacho().getIdDespacho();
    obtenerListas();
  }

  // METODO QUE OBTIENE LA LISTA DE CREDITOS Y DE CONCEPTOS DE DEVOLUCION
  public final void obtenerListas() {
    // OBTENER LOS CREDITOS GESTIONABLES
    listaCreditosGestionables = creditoDao.tablaCreditosEnGestionPorDespacho(idDespacho);
    listaConceptos = conceptoDevolucionDao.obtenerConceptos();
  }

  // METODO QUE ABRE LA VISTA DEL DETALLE DEL CREDITO
  public void selectorDeVista() throws IOException {
    FacesContext contexto = FacesContext.getCurrentInstance();
    if (creditoSeleccionado != null) {
      FacesContext.getCurrentInstance().getExternalContext().redirect("vistaCredito.xhtml");
    } else {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No ha seleccionado ningun credito"));
    }
  }

  // METODO PARA DEVOLVER UN CREDITO DIRECTAMENTE (ADMINISTRADOR ENVIA A DEVOLUCION)
  public void devolverDirecto() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    if (creditoSeleccionado != null) {
      boolean ok;
      if (conceptoSeleccionado != null) {
        Devolucion d = new Devolucion();
        d.setEstatus(Devoluciones.DEVUELTO);
        Date fecha = new Date();
        d.setFecha(fecha);
        d.setConceptoDevolucion(conceptoSeleccionado);
        d.setCredito(creditoSeleccionado);
        d.setObservaciones(observaciones);
        d.setSolicitante(admin);
        d.setRevisor(admin);
        String evento = "El administrador: " + admin + ", devolvio el credito";
        ok = devolucionDao.insertar(d) && historialDao.insertarHistorial(creditoSeleccionado.getIdCredito(), evento);
        if (ok) {
          RequestContext con = RequestContext.getCurrentInstance();
          obtenerListas();
          con.execute("PF('confirmacionDialog').hide();");
          con.update("cuentas");
          contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se devolvio el credito seleccionado"));
        } else {
          contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo devolver el credito. Contacte con el administrador de base de datos"));
        }
      }
    }
  }

  // SETTERS & GETTERS
  public List<Credito> getListaCreditosGestionables() {
    return listaCreditosGestionables;
  }

  public void setListaCreditosGestionables(List<Credito> listaCreditosGestionables) {
    this.listaCreditosGestionables = listaCreditosGestionables;
  }

  public List<Credito> getFiltrados() {
    return filtrados;
  }

  public void setFiltrados(List<Credito> filtrados) {
    this.filtrados = filtrados;
  }

  public Credito getCreditoSeleccionado() {
    return creditoSeleccionado;
  }

  public void setCreditoSeleccionado(Credito creditoSeleccionado) {
    this.creditoSeleccionado = creditoSeleccionado;
  }

  public List<ConceptoDevolucion> getListaConceptos() {
    return listaConceptos;
  }

  public void setListaConceptos(List<ConceptoDevolucion> listaConceptos) {
    this.listaConceptos = listaConceptos;
  }

  public ConceptoDevolucion getConceptoSeleccionado() {
    return conceptoSeleccionado;
  }

  public void setConceptoSeleccionado(ConceptoDevolucion conceptoSeleccionado) {
    this.conceptoSeleccionado = conceptoSeleccionado;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

}
