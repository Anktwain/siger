/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.CampanaDAO;
import dao.ConceptoDevolucionDAO;
import dao.CreditoDAO;
import dao.DevolucionDAO;
import dao.GestorDAO;
import dao.HistorialDAO;
import dao.MarcajeDAO;
import dao.MotivoDevolucionDAO;
import dto.Campana;
import dto.ConceptoDevolucion;
import dto.Credito;
import dto.Devolucion;
import dto.Gestor;
import dto.Historial;
import dto.Marcaje;
import dto.MotivoDevolucion;
import impl.CampanaIMPL;
import impl.ConceptoDevolucionIMPL;
import impl.CreditoIMPL;
import impl.DevolucionIMPL;
import impl.GestorIMPL;
import impl.HistorialIMPL;
import impl.MarcajeIMPL;
import impl.MotivoDevolucionIMPL;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import util.constantes.Devoluciones;
import util.log.Logs;

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
  private String observaciones;
  private String colorSeleccionado;
  private ConceptoDevolucion conceptoSeleccionado;
  private MotivoDevolucion motivoSeleccionado;
  private Credito creditoSeleccionado;
  private Gestor gestorSeleccionado;
  private Campana campanaSeleccionada;
  private Marcaje marcajeSeleccionado;
  private final CreditoDAO creditoDao;
  private final DevolucionDAO devolucionDao;
  private final HistorialDAO historialDao;
  private final ConceptoDevolucionDAO conceptoDevolucionDao;
  private final MotivoDevolucionDAO motivoDevolucionDao;
  private final GestorDAO gestorDao;
  private final CampanaDAO campanaDao;
  private final MarcajeDAO marcajeDao;
  private List<Credito> listaCreditos;
  private List<ConceptoDevolucion> listaConceptos;
  private List<MotivoDevolucion> listaMotivos;
  private List<Gestor> listaGestores;
  private List<Campana> listaCampanas;
  private List<Marcaje> listaMarcajes;

  //CONSTRUCTOR
  public CuentasBean() {
    creditoSeleccionado = new Credito();
    conceptoSeleccionado = new ConceptoDevolucion();
    motivoSeleccionado = new MotivoDevolucion();
    gestorSeleccionado =  new Gestor();
    campanaSeleccionada = new Campana();
    marcajeSeleccionado = new Marcaje();
    creditoDao = new CreditoIMPL();
    devolucionDao = new DevolucionIMPL();
    historialDao = new HistorialIMPL();
    conceptoDevolucionDao = new ConceptoDevolucionIMPL();
    motivoDevolucionDao = new MotivoDevolucionIMPL();
    gestorDao = new GestorIMPL();
    campanaDao = new CampanaIMPL();
    marcajeDao = new MarcajeIMPL();
    listaCreditos = new ArrayList();
    listaConceptos = new ArrayList();
    listaMotivos = new ArrayList();
    obtenerListas();
  }

  // METODO QUE OBTIENE LA LISTA DE CREDITOS Y DE CONCEPTOS DE DEVOLUCION
  public final void obtenerListas() {
    listaGestores = gestorDao.buscarPorDespacho(indexBean.getUsuario().getDespacho().getIdDespacho());
    listaCampanas = campanaDao.buscarTodas();
    listaMarcajes = marcajeDao.buscarTodos();
    listaConceptos = conceptoDevolucionDao.obtenerConceptos();
    conceptoSeleccionado = new ConceptoDevolucion();
    motivoSeleccionado = new MotivoDevolucion();
    observaciones = "";
  }

  // METODO QUE ABRE LA VISTA DEL DETALLE DEL CREDITO
  public void selectorDeVista() {
    if (creditoSeleccionado != null) {
      creditoActualBean.setCreditoActual(creditoSeleccionado);
      try {
        FacesContext.getCurrentInstance().getExternalContext().redirect("vistaCreditoAdmin.xhtml");
      } catch (IOException ioe) {
        Logs.log.error("No se pudo redirigir a la vista de credito del administrador.");
        Logs.log.error(ioe);
      }
    } else {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No ha seleccionado ningun credito"));
    }
  }

  // METODO QUE PREPARA LA LISTA DE MOTIVOS DE DEVOLUCION DEPENDIENDO DEL CONCEPTO SELECCIONADO
  public void preparaMotivos() {
    listaMotivos = motivoDevolucionDao.obtenerMotivosPorConcepto(conceptoSeleccionado.getIdConceptoDevolucion());
  }

  // METODO PARA DEVOLVER UN CREDITO DIRECTAMENTE (ADMINISTRADOR ENVIA A DEVOLUCION)
  public void devolverDirecto() {
    boolean ok;
    if ((conceptoSeleccionado.getIdConceptoDevolucion() != 0) && (motivoSeleccionado.getIdMotivoDevolucion() != 0)) {
      Devolucion d = new Devolucion();
      d.setEstatus(Devoluciones.DEVUELTO);
      d.setFecha(new Date());
      conceptoSeleccionado = conceptoDevolucionDao.buscarPorId(conceptoSeleccionado.getIdConceptoDevolucion());
      d.setConceptoDevolucion(conceptoSeleccionado);
      motivoSeleccionado = motivoDevolucionDao.buscarPorId(motivoSeleccionado.getIdMotivoDevolucion());
      d.setMotivoDevolucion(motivoSeleccionado);
      d.setCredito(creditoSeleccionado);
      d.setObservaciones(observaciones);
      d.setSolicitante(indexBean.getUsuario().getNombreLogin());
      d.setRevisor(indexBean.getUsuario().getNombreLogin());
      Historial h = new Historial();
      h.setEvento("El administrador: " + indexBean.getUsuario().getNombreLogin() + ", devolvio el credito");
      h.setCredito(creditoSeleccionado);
      h.setFecha(new Date());
      ok = devolucionDao.insertar(d) && historialDao.insertar(h);
      if (ok) {
        RequestContext con = RequestContext.getCurrentInstance();
        obtenerListas();
        con.execute("PF('confirmacionDialog').hide();");
        con.update("cuentas");
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se devolvio el credito seleccionado"));
      } else {
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo devolver el credito. Contacte con el administrador de base de datos"));
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
  
  // METODO QUE OBTIENE LA LISTA DE CREDITOS SEGUN LOS PARAMETROS DESEADOS
  public void prepararCreditos(){
    String consulta = "SELECT * FROM credito WHERE id_despacho = " + indexBean.getUsuario().getDespacho().getIdDespacho();
    if(gestorSeleccionado.getIdGestor() != 0){
      consulta = consulta + " AND id_gestor = " + gestorSeleccionado.getIdGestor();
    }
    if(campanaSeleccionada.getIdCampana() != 0){
      consulta = consulta + " AND id_campana = " + campanaSeleccionada.getIdCampana();
    }
    if(marcajeSeleccionado.getIdMarcaje() != 0){
      consulta = consulta + " AND id_marcaje = " + marcajeSeleccionado.getIdMarcaje();
    }
    if(!colorSeleccionado.equals("0")){
      switch(colorSeleccionado){
        case "Verde":
          consulta = consulta + " AND id_credito IN (SELECT DISTINCT id_credito FROM gestion WHERE DATE(fecha) >= DATE_SUB(CURDATE(), INTERVAL 3 DAY) AND id_tipo_gestion != 5)";
          break;
        case "Amarillo":
          consulta = consulta + " AND id_credito IN (SELECT DISTINCT id_credito FROM gestion WHERE DATE(fecha) >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) AND DATE(fecha) < DATE_SUB(CURDATE(), INTERVAL 3 DAY) AND id_tipo_gestion != 5)";
          break;
        case "Rojo":
          consulta = consulta + " AND id_credito NOT IN (SELECT DISTINCT id_credito FROM gestion WHERE DATE(fecha) > DATE_SUB(CURDATE(), INTERVAL 7 DAY) AND id_tipo_gestion != 5)";
          break;
      }
    }
    consulta = consulta + " AND id_credito NOT IN (SELECT id_credito FROM devolucion WHERE estatus IN (" + Devoluciones.DEVUELTO + ", " + Devoluciones.PENDIENTE + "));";
    listaCreditos = creditoDao.busquedaEspecialCreditos(consulta);
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

  public Gestor getGestorSeleccionado() {
    return gestorSeleccionado;
  }

  public void setGestorSeleccionado(Gestor gestorSeleccionado) {
    this.gestorSeleccionado = gestorSeleccionado;
  }

  public Campana getCampanaSeleccionada() {
    return campanaSeleccionada;
  }

  public void setCampanaSeleccionada(Campana campanaSeleccionada) {
    this.campanaSeleccionada = campanaSeleccionada;
  }

  public Marcaje getMarcajeSeleccionado() {
    return marcajeSeleccionado;
  }

  public void setMarcajeSeleccionado(Marcaje marcajeSeleccionado) {
    this.marcajeSeleccionado = marcajeSeleccionado;
  }

  public List<Gestor> getListaGestores() {
    return listaGestores;
  }

  public void setListaGestores(List<Gestor> listaGestores) {
    this.listaGestores = listaGestores;
  }

  public List<Campana> getListaCampanas() {
    return listaCampanas;
  }

  public void setListaCampanas(List<Campana> listaCampanas) {
    this.listaCampanas = listaCampanas;
  }

  public List<Marcaje> getListaMarcajes() {
    return listaMarcajes;
  }

  public void setListaMarcajes(List<Marcaje> listaMarcajes) {
    this.listaMarcajes = listaMarcajes;
  }

  public String getColorSeleccionado() {
    return colorSeleccionado;
  }

  public void setColorSeleccionado(String colorSeleccionado) {
    this.colorSeleccionado = colorSeleccionado;
  }

}
