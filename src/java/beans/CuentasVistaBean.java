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
import dto.tablas.Creditos;
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
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;
import util.constantes.Devoluciones;
import util.constantes.Perfiles;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "cuentasVistaBean")
@SessionScoped
public class CuentasVistaBean implements Serializable{

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");
  DevolucionBean devolucionBean = (DevolucionBean) elContext.getELResolver().getValue(elContext, null, "devolucionBean");

  // VARIABLES DE CLASE
  private CreditoDAO creditoDao;
  private DevolucionDAO devolucionDao;
  private HistorialDAO historialDao;
  private ConceptoDevolucionDAO conceptoDevolucionDao;
  private MotivoDevolucionDAO motivoDevolucionDao;
  private List<Creditos> creditosGestionables;
  private List<Creditos> creditoSeleccionado;
  private List<ConceptoDevolucion> listaConceptos;
  private List<SelectItem> listaConceptosVista;
  private List<MotivoDevolucion> listaMotivos;
  private ConceptoDevolucion conceptoSeleccionado;
  private MotivoDevolucion motivoSeleccionado;
  private String observaciones;
  private final int idDespacho;
  private final String admin;

  //CONSTRUCTOR
  public CuentasVistaBean() {
    creditoDao = new CreditoIMPL();
    devolucionDao = new DevolucionIMPL();
    historialDao = new HistorialIMPL();
    conceptoDevolucionDao = new ConceptoDevolucionIMPL();
    motivoDevolucionDao = new MotivoDevolucionIMPL();
    creditosGestionables = new ArrayList();
    creditoSeleccionado = new ArrayList();
    conceptoSeleccionado = new ConceptoDevolucion();
    listaConceptos = new ArrayList();
    listaConceptosVista = new ArrayList();
    listaMotivos = new ArrayList();
    idDespacho = indexBean.getUsuario().getDespacho().getIdDespacho();
    admin = indexBean.getUsuario().getNombreLogin();
    obtenerListas();
  }

  // METODO QUE OBTIENE LA LISTA DE CREDITOS Y DE CONCEPTOS DE DEVOLUCION
  public final void obtenerListas() {
    // OBTENER LOS CREDITOS GESTIONABLES
    creditosGestionables = creditoDao.creditosEnGestionPorDespacho(idDespacho);
    listaConceptos = conceptoDevolucionDao.obtenerConceptos();
  }

  // METODO QUE OBTIENE LA LISTA DE MOTIVOS DE DEVOLUCION
  public void obtenerMotivos() {
    MotivoDevolucion md = new MotivoDevolucion();
    md.setMotivo("Seleccione un motivo");
    listaMotivos.add(md);
    List<MotivoDevolucion> lista;
    lista = new ArrayList();
    lista = motivoDevolucionDao.obtenerMotivosPorConcepto(conceptoSeleccionado);
    for (int i = 0; i < (lista.size()); i++) {
      listaMotivos.add(lista.get(i));
    }
  }

  // METODO PARA DEVOLVER UN CREDITO DIRECTAMENTE (ADMINISTRADOR ENVIA A DEVOLUCION)
  public void devolverDirecto() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    if (!creditoSeleccionado.isEmpty()) {
      boolean ok;
      if (conceptoSeleccionado != null) {
        Devolucion d = new Devolucion();
        d.setEstatus(Devoluciones.DEVUELTO);
        Date fecha = new Date();
        d.setFecha(fecha);
        d.setIdConceptoDevolucion(conceptoSeleccionado.getIdConceptoDevolucion());
        int idCredito = creditoDao.obtenerIdDelCredito(creditoSeleccionado.get(0).getNumeroCredito());
        d.setIdCredito(idCredito);
        d.setObservaciones(observaciones);
        d.setSolicitante(admin);
        d.setRevisor(admin);
        String evento = "El administrador: " + admin + ", devolvio el credito";
        ok = devolucionDao.insertar(d) && historialDao.insertarHistorial(idCredito, evento);
        if (ok) {
          RequestContext con = RequestContext.getCurrentInstance();
          obtenerListas();
          con.execute("PF('confirmacionDialog').hide();");
          contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se devolvio el credito seleccionado"));
        } else {
          contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo devolver el credito. Contacte con el administrador de base de datos"));
        }
      }
    }
  }
  
  public void selectorDeVista() throws IOException{
    if(!creditoSeleccionado.isEmpty()){
    System.out.println("Se selecciono el credito: " + creditoSeleccionado.get(0).getNumeroCredito() + " . Se abre detalle credito");
    FacesContext.getCurrentInstance().getExternalContext().redirect("vistaCredito.xhtml");
    }
    else{
      FacesContext contexto = FacesContext.getCurrentInstance();
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No ha seleccionado ningun credito"));
    }
  }

  // ***********************************************************************************************************************
  // ***********************************************************************************************************************
  // ***********************************************************************************************************************
  // GETTERS & SETTERS
  public ELContext getElContext() {
    return elContext;
  }

  public void setElContext(ELContext elContext) {
    this.elContext = elContext;
  }

  public IndexBean getIndexBean() {
    return indexBean;
  }

  public void setIndexBean(IndexBean indexBean) {
    this.indexBean = indexBean;
  }

  public CreditoDAO getCreditoDao() {
    return creditoDao;
  }

  public void setCreditoDao(CreditoDAO creditoDao) {
    this.creditoDao = creditoDao;
  }

  public List<Creditos> getCreditosGestionables() {
    return creditosGestionables;
  }

  public void setCreditosGestionables(List<Creditos> creditosGestionables) {
    this.creditosGestionables = creditosGestionables;
  }

  public List<Creditos> getCreditoSeleccionado() {
    return creditoSeleccionado;
  }

  public void setCreditoSeleccionado(List<Creditos> creditoSeleccionado) {
    this.creditoSeleccionado = creditoSeleccionado;
  }

  public List<ConceptoDevolucion> getListaConceptos() {
    return listaConceptos;
  }

  public void setListaConceptos(List<ConceptoDevolucion> listaConceptos) {
    this.listaConceptos = listaConceptos;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public DevolucionBean getDevolucionBean() {
    return devolucionBean;
  }

  public void setDevolucionBean(DevolucionBean devolucionBean) {
    this.devolucionBean = devolucionBean;
  }

  public DevolucionDAO getDevolucionDao() {
    return devolucionDao;
  }

  public void setDevolucionDao(DevolucionDAO devolucionDao) {
    this.devolucionDao = devolucionDao;
  }

  public HistorialDAO getHistorialDao() {
    return historialDao;
  }

  public void setHistorialDao(HistorialDAO historialDao) {
    this.historialDao = historialDao;
  }

  public ConceptoDevolucionDAO getConceptoDevolucionDao() {
    return conceptoDevolucionDao;
  }

  public void setConceptoDevolucionDao(ConceptoDevolucionDAO conceptoDevolucionDao) {
    this.conceptoDevolucionDao = conceptoDevolucionDao;
  }

  public ConceptoDevolucion getConceptoSeleccionado() {
    return conceptoSeleccionado;
  }

  public void setConceptoSeleccionado(ConceptoDevolucion conceptoSeleccionado) {
    this.conceptoSeleccionado = conceptoSeleccionado;
  }

  public MotivoDevolucionDAO getMotivoDevolucionDao() {
    return motivoDevolucionDao;
  }

  public void setMotivoDevolucionDao(MotivoDevolucionDAO motivoDevolucionDao) {
    this.motivoDevolucionDao = motivoDevolucionDao;
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

  public List<SelectItem> getListaConceptosVista() {
    listaConceptosVista = new ArrayList<>();
      for (ConceptoDevolucion c : listaConceptos) {
        listaConceptosVista.add(new SelectItem(c, c.getConcepto()));
      }
    return listaConceptosVista;
  }

  public void setListaConceptosVista(List<SelectItem> listaConceptosVista) {
    this.listaConceptosVista = listaConceptosVista;
  }

}
