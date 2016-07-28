/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;


import dao.CreditoDAO;
import dao.EstatusInformativoDAO;
import dao.GestionDAO;
import dao.MarcajeDAO;
import dto.Credito;
import dto.Gestion;
import dto.Marcaje;
import impl.CreditoIMPL;
import impl.EstatusInformativoIMPL;
import impl.GestionIMPL;
import impl.MarcajeIMPL;
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
import util.log.Logs;

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
  CreditoActualBean creditoActualBean = (CreditoActualBean) elContext.getELResolver().getValue(elContext, null, "creditoActualBean");

  // VARIABLES DE CLASE
  private String tipoImpresionSeleccionado;
  private String periodoActivoSepomex;
  private String periodoActivoVisitas;
  private String periodoActivoEmail;
  private Date fechaInicioImpresiones;
  private Date fechaFinImpresiones;
  private List<String> tipoImpresion;
  private List<Marcaje> listaMarcajes;
  private List<Credito> listaCreditos;
  private final CreditoDAO creditoDao;
  private final GestionDAO gestionDao;
  private final EstatusInformativoDAO estatusInformativoDao;
  private final MarcajeDAO marcajeDao;
  private Credito creditoSeleccionado;
  private Marcaje marcajeSeleccionado;

  // CONSTRUCTOR
  public MarcajeBean() {
    tipoImpresion = new ArrayList();
    listaCreditos = new ArrayList();
    listaMarcajes = new ArrayList();
    creditoDao = new CreditoIMPL();
    gestionDao = new GestionIMPL();
    estatusInformativoDao = new EstatusInformativoIMPL();
    marcajeDao = new MarcajeIMPL();
    creditoSeleccionado = new Credito();
    marcajeSeleccionado = new Marcaje();
    obtenerListas();
    obtenerPeriodos();
  }

  // METODO QUE OBTIENE LAS LISTAS CON LOS CREDITOS MARCADOS
  public final void obtenerListas() {
    tipoImpresion = Arrays.asList("CORREO ORDINARIO", "VISITA DOMICILIARIA", "CORREO ELECTRONICO");
    listaMarcajes = marcajeDao.buscarTodos();
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
      periodoActivoEmail = "No existe un periodo activo";
    }
  }

  // METODO QUE QUITA EL MARCAJE A LOS CREDITOS DE CORREO ORDINARIO
  public void quitarMarcaje() {
    // TO FIX:
    // VERIFICAR SI ES UN CREDITO CON MARCAJE DE CORREO ORDINARIO
    creditoSeleccionado.setMarcaje(marcajeDao.buscarMarcajePorId(Marcajes.SIN_MARCAJE));
    boolean ok = creditoDao.editar(creditoSeleccionado);
    Gestion g = gestionDao.obtenerGestionAutomaticaPorAbreviatura("4CADE");
    g.setCredito(creditoSeleccionado);
    g.setEstatusInformativo(estatusInformativoDao.buscar(7));
    g.setUsuario(indexBean.getUsuario());
    g.setGestion("RECHAZO DE CORRESPONDENCIA SEPOMEX");
    ok = ok & (gestionDao.insertarGestion(g));
    if (ok) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se actualizo la informacion del credito."));
      obtenerListas();
      RequestContext.getCurrentInstance().update("marcajeForm");
    } else {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo realizar la operacion. Contacte al equipo de sistemas."));
    }
    RequestContext.getCurrentInstance().execute("PF('dlgConfirmarDevolucionSepomex').hide;");
  }

  // METODO QUE ESTABLECE UN PERIODO DE IMPRESIONES
  public void establecerPeriodo() {
    Date fechaActual = new Date();
    if (fechaInicioImpresiones.before(fechaActual)) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "La fecha inicial no puede ser menor o igual a la actual."));
    } else if (fechaFinImpresiones.before(fechaInicioImpresiones)) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "La fecha final no puede ser menor a la inicial."));
    } else {
      String periodo = fechaInicioImpresiones.getTime() + ";" + fechaFinImpresiones.getTime() + ";" + tipoImpresionSeleccionado;
      boolean ok = ManejadorArchivosDeTexto.crearArchivo(periodo, Directorios.RUTA_WINDOWS_PERIODO_IMPRESIONES, tipoImpresionSeleccionado.replace(" ", "_") + ".txt");
      if (ok) {
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se establecio el periodo de impresion."));
      } else {
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se establecio el periodo de impresion. Contacte al equipo de sistemas."));
      }
    }
    obtenerPeriodos();
    RequestContext.getCurrentInstance().update("periodosActivosForm");
  }
  
  // METODO QUE CARGA LA VISTA DEL CREDITO SELECCIONADO
  public void abrirDetalleCredito() {
      creditoActualBean.setCreditoActual(creditoSeleccionado);
      try {
        FacesContext.getCurrentInstance().getExternalContext().redirect("vistaCreditoAdmin.xhtml");
      } catch (IOException ioe) {
        Logs.log.error("No se pudo redirigir a la vista de credito del administrador.");
        Logs.log.error(ioe);
      }
  }
  
  // METODO QUE OBTIENE LOS CREDITOS SEGUN EL MARCAJE SELECCIONADO
  public void prepararCreditos(){
    listaCreditos = creditoDao.buscarPorMarcaje(marcajeSeleccionado.getIdMarcaje());
  }
  
  // GETTER & SETTER
  public List<Marcaje> getListaMarcajes() {
    return listaMarcajes;
  }

  public void setListaMarcajes(List<Marcaje> listaMarcajes) {
    this.listaMarcajes = listaMarcajes;
  }

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

  public Marcaje getMarcajeSeleccionado() {
    return marcajeSeleccionado;
  }

  public void setMarcajeSeleccionado(Marcaje marcajeSeleccionado) {
    this.marcajeSeleccionado = marcajeSeleccionado;
  }

}
