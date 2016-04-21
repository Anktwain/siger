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
import dao.MotivoDevolucionDAO;
import dto.ConceptoDevolucion;
import dto.Credito;
import dto.Devolucion;
import dto.MotivoDevolucion;
import impl.ConceptoDevolucionIMPL;
import impl.CreditoIMPL;
import impl.DevolucionIMPL;
import impl.HistorialIMPL;
import impl.MotivoDevolucionIMPL;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import util.constantes.Devoluciones;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "cuentasBean")
@ViewScoped
public class CuentasBean implements Serializable {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");
  CreditoActualBean creditoActualBean = (CreditoActualBean) elContext.getELResolver().getValue(elContext, null, "creditoActualBean");

  // VARIABLES DE CLASE
  private List<Credito> listaCreditos;
  private Credito creditoSeleccionado;
  private List<ConceptoDevolucion> listaConceptos;
  private final CreditoDAO creditoDao;
  private final DevolucionDAO devolucionDao;
  private final HistorialDAO historialDao;
  private final ConceptoDevolucionDAO conceptoDevolucionDao;
  private ConceptoDevolucion conceptoSeleccionado;
  private final MotivoDevolucionDAO motivoDevolucionDao;
  private List<MotivoDevolucion> listaMotivos;
  private MotivoDevolucion motivoSeleccionado;
  private String observaciones;
  private final String admin;
  private final int idDespacho;

  //CONSTRUCTOR
  public CuentasBean() {
    creditoDao = new CreditoIMPL();
    devolucionDao = new DevolucionIMPL();
    historialDao = new HistorialIMPL();
    conceptoDevolucionDao = new ConceptoDevolucionIMPL();
    listaCreditos = new ArrayList();
    creditoSeleccionado = new Credito();
    conceptoSeleccionado = new ConceptoDevolucion();
    listaConceptos = new ArrayList();
    motivoSeleccionado = new MotivoDevolucion();
    listaMotivos = new ArrayList();
    motivoDevolucionDao = new MotivoDevolucionIMPL();
    admin = indexBean.getUsuario().getNombreLogin();
    idDespacho = indexBean.getUsuario().getDespacho().getIdDespacho();
    obtenerListas();
  }

  // METODO QUE OBTIENE LA LISTA DE CREDITOS Y DE CONCEPTOS DE DEVOLUCION
  public final void obtenerListas() {
    listaCreditos = creditoDao.tablaCreditosEnGestionPorDespacho(idDespacho);
    listaConceptos = conceptoDevolucionDao.obtenerConceptos();
    conceptoSeleccionado = new ConceptoDevolucion();
    motivoSeleccionado = new MotivoDevolucion();
    observaciones = "";
  }

  // METODO QUE ABRE LA VISTA DEL DETALLE DEL CREDITO
  public void selectorDeVista() throws IOException {
    FacesContext contexto = FacesContext.getCurrentInstance();
    if (creditoSeleccionado != null) {
      creditoActualBean.setCreditoActual(creditoSeleccionado);
      FacesContext.getCurrentInstance().getExternalContext().redirect("vistaCreditoAdmin.xhtml");
    } else {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No ha seleccionado ningun credito"));
    }
  }

  // METODO QUE PREPARA LA LISTA DE MOTIVOS DE DEVOLUCION DEPENDIENDO DEL CONCEPTO SELECCIONADO
  public void preparaMotivos() {
    listaMotivos = motivoDevolucionDao.obtenerMotivosPorConcepto(conceptoSeleccionado.getIdConceptoDevolucion());
  }

  // METODO PARA DEVOLVER UN CREDITO DIRECTAMENTE (ADMINISTRADOR ENVIA A DEVOLUCION)
  public void devolverDirecto() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    boolean ok;
    if ((conceptoSeleccionado.getIdConceptoDevolucion() != 0) && (motivoSeleccionado.getIdMotivoDevolucion() != 0)) {
      Devolucion d = new Devolucion();
      d.setEstatus(Devoluciones.DEVUELTO);
      Date fecha = new Date();
      d.setFecha(fecha);
      conceptoSeleccionado = conceptoDevolucionDao.buscarPorId(conceptoSeleccionado.getIdConceptoDevolucion());
      d.setConceptoDevolucion(conceptoSeleccionado);
      motivoSeleccionado = motivoDevolucionDao.buscarPorId(motivoSeleccionado.getIdMotivoDevolucion());
      d.setMotivoDevolucion(motivoSeleccionado);
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

  // METODO QUE OBTIENE EL SALDO VENCIDO DEL CREDITO
  public float calcularSaldoVencido(Credito credito) {
    float f = creditoDao.buscarSaldoVencidoCredito(credito.getIdCredito());
    if (f == 0) {
      return 0;
    } else {
      return f;
    }
  }

  // SETTERS & GETTERS
  public List<Credito> getListaCreditos() {
    return listaCreditos;
  }

  public void setListaCreditos(List<Credito> listaCreditos) {
    this.listaCreditos = listaCreditos;
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

  public List<MotivoDevolucion> getListaMotivos() {
    return listaMotivos;
  }

  public void setListaMotivos(List<MotivoDevolucion> listaMotivos) {
    this.listaMotivos = listaMotivos;
  }

  public MotivoDevolucion getMotivoSeleccionado() {
    return motivoSeleccionado;
  }

  public void setMotivoSeleccionado(MotivoDevolucion motivoSeleccionado) {
    this.motivoSeleccionado = motivoSeleccionado;
  }

}
