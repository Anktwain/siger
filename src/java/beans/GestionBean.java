/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.*;
import dto.*;
import impl.*;
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

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "gestionBean")
@ViewScoped

public class GestionBean implements Serializable {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  VistaCreditoBean vistaCreditoBean = (VistaCreditoBean) elContext.getELResolver().getValue(elContext, null, "vistaCreditoBean");
  ObtenerOracionCompletaGestionBean obtenerOracionCompletaGestionBean = (ObtenerOracionCompletaGestionBean) elContext.getELResolver().getValue(elContext, null, "obtenerOracionCompletaBean");

  // VARIABLES DE CLASE
  private List<TipoGestion> listaTipos;
  private List<DondeGestion> listaDonde;
  private List<AsuntoGestion> listaAsuntos;
  private List<DescripcionGestion> listaDescripciones;
  private List<TipoQuienGestion> listaTipoSujetos;
  private List<QuienGestion> listaSujetos;
  private List<EstatusInformativo> listaEstatus;
  private Gestion nueva;
  private TipoGestion tipoSeleccionado;
  private DondeGestion dondeSeleccionado;
  private AsuntoGestion asuntoSeleccionado;
  private DescripcionGestion descripcionSeleccionada;
  private TipoQuienGestion tipoSujetoSeleccionado;
  private QuienGestion sujetoSeleccionado;
  private EstatusInformativo estatusSeleccionado;
  private String gestion;
  private final GestionDAO gestionDao;
  private final EstatusInformativoDAO estatusInformativoDao;
  private final TipoGestionDAO tipoGestionDao;
  private final DondeGestionDAO dondeGestionDao;
  private final AsuntoGestionDAO asuntoGestionDao;
  private final TipoQuienGestionDAO tipoQuienGestionDao;
  private final QuienGestionDAO quienGestionDao;
  private final DescripcionGestionDAO descripcionGestionDao;

  // CONSTRUCTOR
  public GestionBean() {
    listaTipos = new ArrayList();
    listaDonde = new ArrayList();
    listaAsuntos = new ArrayList();
    listaTipoSujetos = new ArrayList();
    listaSujetos = new ArrayList();
    listaEstatus = new ArrayList();
    listaDescripciones = new ArrayList();
    gestionDao = new GestionIMPL();
    estatusInformativoDao = new EstatusInformativoIMPL();
    tipoGestionDao = new TipoGestionIMPL();
    dondeGestionDao = new DondeGestionIMPL();
    asuntoGestionDao = new AsuntoGestionIMPL();
    descripcionGestionDao = new DescripcionGestionIMPL();
    tipoQuienGestionDao = new TipoQuienGestionIMPL();
    quienGestionDao = new QuienGestionIMPL();
    tipoSeleccionado = new TipoGestion();
    dondeSeleccionado = new DondeGestion();
    asuntoSeleccionado = new AsuntoGestion();
    tipoSujetoSeleccionado = new TipoQuienGestion();
    sujetoSeleccionado = new QuienGestion();
    estatusSeleccionado = new EstatusInformativo();
    descripcionSeleccionada = new DescripcionGestion();
    nueva = new Gestion();
    listaTipos = tipoGestionDao.buscarTodo();
  }

  public void preparaDonde() {
    tipoSeleccionado = tipoGestionDao.buscarPorId(tipoSeleccionado.getIdTipoGestion());
    listaDonde = dondeGestionDao.buscarPorTipoGestion(tipoSeleccionado.getIdTipoGestion());
  }

  public void preparaAsunto() {
    dondeSeleccionado = dondeGestionDao.buscarPorId(dondeSeleccionado.getIdDondeGestion());
    listaAsuntos = asuntoGestionDao.buscarPorTipoGestion(tipoSeleccionado.getIdTipoGestion());
  }

  public void preparaDescripcion() {
    asuntoSeleccionado = asuntoGestionDao.buscarPorId(asuntoSeleccionado.getIdAsuntoGestion());
    listaDescripciones = descripcionGestionDao.buscarPorAsuntoTipo(tipoSeleccionado.getIdTipoGestion(), asuntoSeleccionado.getIdAsuntoGestion());
  }

  public void preparaTipoSujeto() {
    descripcionSeleccionada = descripcionGestionDao.buscarPorId(descripcionSeleccionada.getIdDescripcionGestion());
    listaTipoSujetos = tipoQuienGestionDao.buscarPorAsuntoDescripcion(asuntoSeleccionado.getTipoAsuntoGestion().getClaveAsunto(), descripcionSeleccionada.getAbreviatura());
  }

  public void preparaSujetos() {
    tipoSujetoSeleccionado = tipoQuienGestionDao.buscarPorId(tipoSujetoSeleccionado.getIdTipoQuienGestion());
    listaSujetos = quienGestionDao.buscarPorTipoQuien(tipoSujetoSeleccionado.getIdTipoQuienGestion());
  }

  public void preparaEstatus() {
    sujetoSeleccionado = quienGestionDao.buscarPorId(sujetoSeleccionado.getIdQuienGestion());
    listaEstatus = estatusInformativoDao.buscarTodos();
  }

  public void crearNuevaGestion() {
    preparaGestion();
    boolean ok = gestionDao.insertarGestion(nueva);
    FacesContext contexto = FacesContext.getCurrentInstance();
    if (ok) {
      limpiarCampos();
      vistaCreditoBean.obtenerGestionesAnteriores();
      RequestContext.getCurrentInstance().update("DetalleCredito");
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se guardo la gestion: "));
    } else {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se guardo la gestion. Contacte al equipo de sistemas."));
    }
  }
  
  // METODO QUE MUESTRA LA ORACION COMPLETA
  public void preparaGestion(){
    estatusSeleccionado = estatusInformativoDao.buscar(estatusSeleccionado.getIdEstatusInformativo());
    nueva.setTipoGestion(tipoSeleccionado);
    nueva.setDondeGestion(dondeSeleccionado);
    nueva.setAsuntoGestion(asuntoSeleccionado);
    nueva.setDescripcionGestion(descripcionSeleccionada);
    nueva.setTipoQuienGestion(tipoSujetoSeleccionado);
    nueva.setQuienGestion(sujetoSeleccionado);
    nueva.setGestion(gestion.toUpperCase());
    nueva.setEstatusInformativo(estatusSeleccionado);
    nueva.setCredito(vistaCreditoBean.getCreditoActual());
    Date fecha = new Date();
    nueva.setFecha(fecha);
    nueva.setUsuario(vistaCreditoBean.cuentasBean.indexBean.getUsuario());
  }

  public void limpiarCampos() {
    listaTipos = new ArrayList();
    listaDonde = new ArrayList();
    listaAsuntos = new ArrayList();
    listaTipoSujetos = new ArrayList();
    listaDescripciones = new ArrayList();
    listaSujetos = new ArrayList();
    listaEstatus = new ArrayList();
    gestion = "";
    listaTipos = tipoGestionDao.buscarTodo();
    RequestContext.getCurrentInstance().update("formNuevaGestion");
  }

  // ***********************************************************************************************************************
  // ***********************************************************************************************************************
  // ***********************************************************************************************************************
  // GETTERS & SETTERS
  public List<TipoGestion> getListaTipos() {
    return listaTipos;
  }

  public void setListaTipos(List<TipoGestion> listaTipos) {
    this.listaTipos = listaTipos;
  }

  public List<DondeGestion> getListaDonde() {
    return listaDonde;
  }

  public void setListaDonde(List<DondeGestion> listaDonde) {
    this.listaDonde = listaDonde;
  }

  public List<AsuntoGestion> getListaAsuntos() {
    return listaAsuntos;
  }

  public void setListaAsuntos(List<AsuntoGestion> listaAsuntos) {
    this.listaAsuntos = listaAsuntos;
  }

  public List<TipoQuienGestion> getListaTipoSujetos() {
    return listaTipoSujetos;
  }

  public void setListaTipoSujetos(List<TipoQuienGestion> listaTipoSujetos) {
    this.listaTipoSujetos = listaTipoSujetos;
  }

  public List<QuienGestion> getListaSujetos() {
    return listaSujetos;
  }

  public void setListaSujetos(List<QuienGestion> listaSujetos) {
    this.listaSujetos = listaSujetos;
  }

  public List<EstatusInformativo> getListaEstatus() {
    return listaEstatus;
  }

  public void setListaEstatus(List<EstatusInformativo> listaEstatus) {
    this.listaEstatus = listaEstatus;
  }

  public TipoGestion getTipoSeleccionado() {
    return tipoSeleccionado;
  }

  public void setTipoSeleccionado(TipoGestion tipoSeleccionado) {
    this.tipoSeleccionado = tipoSeleccionado;
  }

  public DondeGestion getDondeSeleccionado() {
    return dondeSeleccionado;
  }

  public void setDondeSeleccionado(DondeGestion dondeSeleccionado) {
    this.dondeSeleccionado = dondeSeleccionado;
  }

  public AsuntoGestion getAsuntoSeleccionado() {
    return asuntoSeleccionado;
  }

  public void setAsuntoSeleccionado(AsuntoGestion asuntoSeleccionado) {
    this.asuntoSeleccionado = asuntoSeleccionado;
  }

  public TipoQuienGestion getTipoSujetoSeleccionado() {
    return tipoSujetoSeleccionado;
  }

  public void setTipoSujetoSeleccionado(TipoQuienGestion tipoSujetoSeleccionado) {
    this.tipoSujetoSeleccionado = tipoSujetoSeleccionado;
  }

  public QuienGestion getSujetoSeleccionado() {
    return sujetoSeleccionado;
  }

  public void setSujetoSeleccionado(QuienGestion sujetoSeleccionado) {
    this.sujetoSeleccionado = sujetoSeleccionado;
  }

  public EstatusInformativo getEstatusSeleccionado() {
    return estatusSeleccionado;
  }

  public void setEstatusSeleccionado(EstatusInformativo estatusSeleccionado) {
    this.estatusSeleccionado = estatusSeleccionado;
  }

  public String getGestion() {
    return gestion;
  }

  public void setGestion(String gestion) {
    this.gestion = gestion;
  }
  
  public List<DescripcionGestion> getListaDescripciones() {
    return listaDescripciones;
  }

  public void setListaDescripciones(List<DescripcionGestion> listaDescripciones) {
    this.listaDescripciones = listaDescripciones;
  }

  public DescripcionGestion getDescripcionSeleccionada() {
    return descripcionSeleccionada;
  }

  public void setDescripcionSeleccionada(DescripcionGestion descripcionSeleccionada) {
    this.descripcionSeleccionada = descripcionSeleccionada;
  }
  
}
