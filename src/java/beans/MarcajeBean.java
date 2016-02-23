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
import util.CrearArchivoTexto;
import util.constantes.Directorios;
import util.constantes.Marcajes;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "marcajeBean")
@ViewScoped
public class MarcajeBean implements Serializable {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  // VARIABLES DE CLASE
  private Date fechaInicioImpresiones;
  private Date fechaFinImpresiones;
  private List<Credito> listaSepomex;
  private List<Credito> listaTelegrama;
  private List<Credito> listaVisita;
  private List<Credito> listaCorreo;
  private List<Credito> listaLocalizacion;
  private List<Credito> listaInformacion;
  private List<Credito> listaCobroCelular;
  private List<Credito> listaWhatsapp;
  private final CreditoDAO creditoDao;
  private final GestionDAO gestionDao;
  private final EstatusInformativoDAO estatusInformativoDao;
  private Credito seleccionadoSepomex;
  private FacesContext contexto;

  // CONSTRUCTOR
  public MarcajeBean() {
    contexto = FacesContext.getCurrentInstance();
    listaSepomex = new ArrayList();
    listaTelegrama = new ArrayList();
    listaVisita = new ArrayList();
    listaCorreo = new ArrayList();
    listaLocalizacion = new ArrayList();
    listaInformacion = new ArrayList();
    listaCobroCelular = new ArrayList();
    listaWhatsapp = new ArrayList();
    creditoDao = new CreditoIMPL();
    gestionDao = new GestionIMPL();
    estatusInformativoDao = new EstatusInformativoIMPL();
    seleccionadoSepomex = new Credito();
    obtenerListas();
  }

  // METODO QUE OBTIENE LAS LISTAS CON LOS CREDITOS MARCADOS
  public final void obtenerListas() {
    listaSepomex = creditoDao.buscarPorMarcaje(Marcajes.CORREO_SEPOMEX);
    listaTelegrama = creditoDao.buscarPorMarcaje(Marcajes.TELEGRAMA);
    listaVisita = creditoDao.buscarPorMarcaje(Marcajes.VISITA_DOMICILIARIA);
    listaCorreo = creditoDao.buscarPorMarcaje(Marcajes.CORREO_ELECTRONICO);
    listaLocalizacion = creditoDao.buscarPorMarcaje(Marcajes.LOCALIZACION);
    listaInformacion = creditoDao.buscarPorMarcaje(Marcajes.ESPERA_INFORMACION_BANCO);
    listaCobroCelular = creditoDao.buscarPorMarcaje(Marcajes.COBRO_EN_CELULAR);
    listaWhatsapp = creditoDao.buscarPorMarcaje(Marcajes.WHATSAPP);
  }

  // METODO QUE QUITA EL MARCAJE A LOS CREDITOS DE CORREO ORDINARIO
  public void quitarMarcaje() {
    seleccionadoSepomex.setMarcaje(Marcajes.SIN_MARCAJE);
    boolean ok = creditoDao.editar(seleccionadoSepomex);
    Gestion g = gestionDao.obtenerGestionAutomaticaPorAbreviatura("4CADE");
    g.setCredito(seleccionadoSepomex);
    g.setEstatusInformativo(estatusInformativoDao.buscar(7));
    g.setUsuario(indexBean.getUsuario());
    g.setGestion("RECHAZO DE CORRESPONDENCIA SEPOMEX");
    ok = ok & (gestionDao.insertarGestion(g));
    if (ok) {
      contexto = FacesContext.getCurrentInstance();
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se actualizo la informacion del credito."));
      obtenerListas();
      RequestContext.getCurrentInstance().update("marcajeForm");
    } else {
      contexto = FacesContext.getCurrentInstance();
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo realizar la operacion. Contacte al equipo de sistemas."));
    }
    RequestContext.getCurrentInstance().execute("PF('dlgConfirmarDevolucionSepomex').hide;");
  }

  // METODO QUE ESTABLECE UN PERIODO DE IMPRESIONES
  public void establecerPeriodo() {
    contexto = FacesContext.getCurrentInstance();
    Date fechaActual = new Date();
    if (fechaInicioImpresiones.before(fechaActual)) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "La fecha inicial no puede ser menor a la actual."));
    } else if (fechaFinImpresiones.before(fechaInicioImpresiones)) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "La fecha final no puede ser menor a la inicial."));
    } else {
      String periodo = fechaInicioImpresiones.toString() + ";" + fechaFinImpresiones.toString();
      boolean ok = CrearArchivoTexto.crearArchivo(periodo, Directorios.RUTA_WINDOWS_PERIODO_IMPRESIONES, "periodoActual.txt");
      if(ok){
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se establecio el periodo de impresion."));
      }
      else{
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se establecio el periodo de impresion. Contacte al equipo de sistemas."));
      }
    }
  }

  // GETTER & SETTER
  public List<Credito> getListaSepomex() {
    return listaSepomex;
  }

  public void setListaSepomex(List<Credito> listaSepomex) {
    this.listaSepomex = listaSepomex;
  }

  public List<Credito> getListaTelegrama() {
    return listaTelegrama;
  }

  public void setListaTelegrama(List<Credito> listaTelegrama) {
    this.listaTelegrama = listaTelegrama;
  }

  public List<Credito> getListaVisita() {
    return listaVisita;
  }

  public void setListaVisita(List<Credito> listaVisita) {
    this.listaVisita = listaVisita;
  }

  public List<Credito> getListaCorreo() {
    return listaCorreo;
  }

  public void setListaCorreo(List<Credito> listaCorreo) {
    this.listaCorreo = listaCorreo;
  }

  public List<Credito> getListaLocalizacion() {
    return listaLocalizacion;
  }

  public void setListaLocalizacion(List<Credito> listaLocalizacion) {
    this.listaLocalizacion = listaLocalizacion;
  }

  public List<Credito> getListaInformacion() {
    return listaInformacion;
  }

  public void setListaInformacion(List<Credito> listaInformacion) {
    this.listaInformacion = listaInformacion;
  }

  public List<Credito> getListaWhatsapp() {
    return listaWhatsapp;
  }

  public void setListaWhatsapp(List<Credito> listaWhatsapp) {
    this.listaWhatsapp = listaWhatsapp;
  }

  public Credito getSeleccionadoSepomex() {
    return seleccionadoSepomex;
  }

  public void setSeleccionadoSepomex(Credito seleccionadoSepomex) {
    this.seleccionadoSepomex = seleccionadoSepomex;
  }

  public Date getFechaInicioImpresiones() {
    return fechaInicioImpresiones;
  }

  public void setFechaInicioImpresiones(Date fechaInicioImpresiones) {
    this.fechaInicioImpresiones = fechaInicioImpresiones;
  }

  public Date getFechaFinImpresiones() {
    return fechaFinImpresiones;
  }

  public void setFechaFinImpresiones(Date fechaFinImpresiones) {
    this.fechaFinImpresiones = fechaFinImpresiones;
  }

  public List<Credito> getListaCobroCelular() {
    return listaCobroCelular;
  }

  public void setListaCobroCelular(List<Credito> listaCobroCelular) {
    this.listaCobroCelular = listaCobroCelular;
  }

}
