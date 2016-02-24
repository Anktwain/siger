/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;


import dao.CreditoDAO;
import dao.EstatusInformativoDAO;
import dao.GestionDAO;
import dto.Credito;
import dto.Gestion;
import impl.CreditoIMPL;
import impl.EstatusInformativoIMPL;
import impl.GestionIMPL;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import util.ManejadorArchivosDeTexto;
import util.constantes.Directorios;
import util.constantes.Marcajes;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "marcajeBean")
@ViewScoped
public class MarcajesBean implements Serializable {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  // VARIABLES DE CLASE
  private String tipoImpresionSeleccionado;
  private String periodoActivoSepomex;
  private String periodoActivoVisitas;
  private String periodoActivoEmail;
  private Date fechaInicioImpresiones;
  private Date fechaFinImpresiones;
  private List<String> tipoImpresion;
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
  public MarcajesBean() {
    contexto = FacesContext.getCurrentInstance();
    tipoImpresion = new ArrayList();
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
    obtenerPeriodos();
  }

  // METODO QUE OBTIENE LAS LISTAS CON LOS CREDITOS MARCADOS
  public final void obtenerListas() {
    tipoImpresion = Arrays.asList("CORREO ORDINARIO", "VISITA DOMICILIARIA", "CORREO ELECTRONICO");
    listaSepomex = creditoDao.buscarPorMarcaje(Marcajes.CORREO_SEPOMEX);
    listaTelegrama = creditoDao.buscarPorMarcaje(Marcajes.TELEGRAMA);
    listaVisita = creditoDao.buscarPorMarcaje(Marcajes.VISITA_DOMICILIARIA);
    listaCorreo = creditoDao.buscarPorMarcaje(Marcajes.CORREO_ELECTRONICO);
    listaLocalizacion = creditoDao.buscarPorMarcaje(Marcajes.LOCALIZACION);
    listaInformacion = creditoDao.buscarPorMarcaje(Marcajes.ESPERA_INFORMACION_BANCO);
    listaCobroCelular = creditoDao.buscarPorMarcaje(Marcajes.COBRO_EN_CELULAR);
    listaWhatsapp = creditoDao.buscarPorMarcaje(Marcajes.WHATSAPP);
  }

  // METODO QUE BUSCA LOS PERIODOS DE IMPRESION
  public final void obtenerPeriodos() {
    // CORREO SEPOMEX
    try {
      String cadena = ManejadorArchivosDeTexto.leerArchivo(Directorios.RUTA_WINDOWS_PERIODO_IMPRESIONES, "CORREO_ORDINARIO.txt");
      if (!cadena.isEmpty()) {
        String[] arreglo = cadena.split(";");
        String df = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(df);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(arreglo[0]));
        String f1 = sdf.format(calendar.getTime());
        calendar.setTimeInMillis(Long.parseLong(arreglo[1]));
        String f2 = sdf.format(calendar.getTime());
        periodoActivoSepomex = f1 + " al " + f2;
      }
    } catch (IOException ioe) {
      System.out.println(ioe);
      periodoActivoSepomex = "No existe un periodo activo";
    }
    // VISITA DOMICILIARIA
    try {
      String cadena = ManejadorArchivosDeTexto.leerArchivo(Directorios.RUTA_WINDOWS_PERIODO_IMPRESIONES, "VISITA_DOMICILIARIA.txt");
      if (!cadena.isEmpty()) {
        String[] arreglo = cadena.split(";");
        String df = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(df);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(arreglo[0]));
        String f1 = sdf.format(calendar.getTime());
        calendar.setTimeInMillis(Long.parseLong(arreglo[1]));
        String f2 = sdf.format(calendar.getTime());
        periodoActivoVisitas = f1 + " al " + f2;
      }
    } catch (IOException ioe) {
      System.out.println(ioe);
      periodoActivoVisitas = "No existe un periodo activo";
    }
    // CORREO ELECTRONICO
    try {
      String cadena = ManejadorArchivosDeTexto.leerArchivo(Directorios.RUTA_WINDOWS_PERIODO_IMPRESIONES, "CORREO_ELECTRONICO.txt");
      if (!cadena.isEmpty()) {
        String[] arreglo = cadena.split(";");
        String df = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(df);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(arreglo[0]));
        String f1 = sdf.format(calendar.getTime());
        calendar.setTimeInMillis(Long.parseLong(arreglo[1]));
        String f2 = sdf.format(calendar.getTime());
        periodoActivoEmail = f1 + " al " + f2;
      }
    } catch (IOException ioe) {
      System.out.println(ioe);
      periodoActivoEmail = "No existe un periodo activo";
    }
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
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "La fecha inicial no puede ser menor o igual a la actual."));
    } else if (fechaFinImpresiones.before(fechaInicioImpresiones)) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "La fecha final no puede ser menor a la inicial."));
    } else {
      String periodo = fechaInicioImpresiones.getTime() + ";" + fechaFinImpresiones.getTime() + ";" + tipoImpresionSeleccionado;
      boolean ok = ManejadorArchivosDeTexto.crearArchivo(periodo, Directorios.RUTA_WINDOWS_PERIODO_IMPRESIONES, tipoImpresionSeleccionado.replace(" ", "_") + ".txt");
      if (ok) {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se establecio el periodo de impresion."));
      } else {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se establecio el periodo de impresion. Contacte al equipo de sistemas."));
      }
    }
    obtenerPeriodos();
    RequestContext.getCurrentInstance().update("periodosActivosForm");
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

  public List<String> getTipoImpresion() {
    return tipoImpresion;
  }

  public void setTipoImpresion(List<String> tipoImpresion) {
    this.tipoImpresion = tipoImpresion;
  }

  public String getTipoImpresionSeleccionado() {
    return tipoImpresionSeleccionado;
  }

  public void setTipoImpresionSeleccionado(String tipoImpresionSeleccionado) {
    this.tipoImpresionSeleccionado = tipoImpresionSeleccionado;
  }

  public String getPeriodoActivoSepomex() {
    return periodoActivoSepomex;
  }

  public void setPeriodoActivoSepomex(String periodoActivoSepomex) {
    this.periodoActivoSepomex = periodoActivoSepomex;
  }

  public String getPeriodoActivoVisitas() {
    return periodoActivoVisitas;
  }

  public void setPeriodoActivoVisitas(String periodoActivoVisitas) {
    this.periodoActivoVisitas = periodoActivoVisitas;
  }

  public String getPeriodoActivoEmail() {
    return periodoActivoEmail;
  }

  public void setPeriodoActivoEmail(String periodoActivoEmail) {
    this.periodoActivoEmail = periodoActivoEmail;
  }

}
