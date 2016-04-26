/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.CreditoDAO;
import dao.DireccionDAO;
import dao.ImpresionDAO;
import dto.Credito;
import dto.Direccion;
import dto.Impresion;
import impl.CreditoIMPL;
import impl.DireccionIMPL;
import impl.ImpresionIMPL;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import util.GeneradorPdf;
import util.ManejadorArchivosDeTexto;
import util.constantes.Directorios;
import util.constantes.Impresiones;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "visitasBean")
@ViewScoped
public class VisitasBean implements Serializable{

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");
  CreditoActualBean creditoActualBean = (CreditoActualBean) elContext.getELResolver().getValue(elContext, null, "creditoActualBean");

  // VARIABLES DE CLASE
  private final CreditoDAO creditoDao;
  private final DireccionDAO direccionDao;
  private final ImpresionDAO impresionDao;
  private final Credito creditoActual;
  private boolean periodoSepomexActivo;
  private boolean periodoVisitasActivo;
  private boolean descargarPdf;
  private String rutaPdf;
  StreamedContent archivo;
  private Direccion direccion;

  // CONSTRUCTOR
  public VisitasBean() {
    direccionDao = new DireccionIMPL();
    creditoDao = new CreditoIMPL();
    impresionDao = new ImpresionIMPL();
    creditoActual = creditoActualBean.getCreditoActual();
    direccion = obtenerDireccion();
    periodoSepomexActivo = false;
    periodoVisitasActivo = false;
    descargarPdf = false;
    verificarPeriodoSepomex();
    verificarPeriodoVisitas();
  }

  // METODO QUE OBTINE LA PRIMER DIRECCION DEL CREDITO
  public final Direccion obtenerDireccion(){
    List<Direccion> direcciones = direccionDao.buscarPorSujeto(creditoActual.getDeudor().getSujeto().getIdSujeto());
    if(direcciones.isEmpty()){
      return new Direccion();
    }
    else{
      return direcciones.get(0);
    }
  }
  
  // METODO QUE VERIFICA SI EL PERIODODE IMPRESIONES PARA CORREO ESTA ACTIVO
  public final void verificarPeriodoSepomex() {
    Date fechaActual = new Date();
    try {
      String cadena = ManejadorArchivosDeTexto.leerArchivo(Directorios.RUTA_WINDOWS_PERIODO_IMPRESIONES, "CORREO_ORDINARIO.txt");
      if (!cadena.isEmpty()) {
        String[] arreglo = cadena.split(";");
        Date fechaInicial, fechaFinal;
        fechaInicial = new Date(Long.parseLong(arreglo[0]));
        fechaFinal = new Date(Long.parseLong(arreglo[1]));
        if (fechaActual.after(fechaInicial) && (fechaActual.before(fechaFinal))) {
          periodoSepomexActivo = true;
        }
      }
    } catch (IOException ioe) {
      Logs.log.error("No se pudo leer el archivo en la ruta " + Directorios.RUTA_WINDOWS_PERIODO_IMPRESIONES + "CORREO_ORDINARIO.txt");
      Logs.log.error(ioe);
    }
  }

  // METODO QUE VERIFICA SI EL PERIODODE IMPRESIONES PARA VISITAS ESTA ACTIVO
  public final void verificarPeriodoVisitas() {
    Date fechaActual = new Date();
    try {
      String cadena = ManejadorArchivosDeTexto.leerArchivo(Directorios.RUTA_WINDOWS_PERIODO_IMPRESIONES, "VISITA_DOMICILIARIA.txt");
      if (!cadena.isEmpty()) {
        String[] arreglo = cadena.split(";");
        Date fechaInicial, fechaFinal;
        fechaInicial = new Date(Long.parseLong(arreglo[0]));
        fechaFinal = new Date(Long.parseLong(arreglo[1]));
        if (fechaActual.after(fechaInicial) && (fechaActual.before(fechaFinal))) {
          periodoVisitasActivo = true;
        }
      }
    } catch (IOException ioe) {
      Logs.log.error("No se pudo leer el archivo en la ruta " + Directorios.RUTA_WINDOWS_PERIODO_IMPRESIONES + "CORREO_ORDINARIO.txt");
      Logs.log.error(ioe);
    }
  }

  // METODO QUE GENERA UN NOMBRE DE ARCHIVO PARA EL PDF
  public String nombrarPdf() {
    DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    return creditoActual.getNumeroCredito() + "_" + df.format(new Date()) + ".pdf";
  }

  // METODO QUE GENERA UN ARCHIVO PDF PARA CORREO ORDINARIO
  public void generarCorreo() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    if (direccion.getIdDireccion() == null) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El credito no tiene ninguna direccion asociada."));
    } else {
      Impresion imp = new Impresion();
      imp.setCredito(creditoActual);
      imp.setFechaImpresion(new Date());
      imp.setTipoImpresion(Impresiones.CORREO_ORDINARIO);
      imp.setDireccion(direccion);
      if (impresionDao.insertar(imp)) {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se genero el archivo para ser impreso por el administrador."));
      } else {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "No se genero el archivo PDF. Contacte al equipo de sistemas."));
      }
    }
  }

// METODO QUE GENERA UN ARCHIVO PDF PARA VISITA DOMICILIARIA
  public void generarVisita() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    if (direccion.getIdDireccion() == null) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El credito no tiene ninguna direccion asociada."));
    } else {
      Impresion imp = new Impresion();
      imp.setCredito(creditoActual);
      imp.setFechaImpresion(new Date());
      imp.setTipoImpresion(Impresiones.VISITA_DOMICILIARIA);
      imp.setDireccion(direccion);
      if (impresionDao.insertar(imp)) {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se genero el archivo para ser impreso por el administrador."));
      } else {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "No se genero el archivo PDF. Contacte al equipo de sistemas."));
      }
    }
  }

  // METODO QUE GENERA UNA IMPRESION NORMAL
  public void generarNormal() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    if (direccion.getIdDireccion() == null) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El credito no tiene ninguna direccion asociada."));
    } else {
      Impresion imp = new Impresion();
      imp.setCredito(creditoActual);
      imp.setFechaImpresion(new Date());
      imp.setTipoImpresion(Impresiones.IMPRESION_NORMAL);
      imp.setDireccion(direccion);
      if (impresionDao.insertar(imp)) {
        if (generarPdf()) {
          contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se genero el archivo para ser impreso por el administrador."));
        } else {
          contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error.", "No se genero el archivo PDF. Contacte al equipo de sistemas."));
        }
      }
    }
  }

  // METODO QUE GENERA EL PDF
  public boolean generarPdf() {
    try {
      InputStream stream = new FileInputStream(GeneradorPdf.crearPdf(nombrarPdf(), creditoActual, direccion, creditoDao.buscarSaldoVencidoCredito(creditoActual.getIdCredito())));
      archivo = new DefaultStreamedContent(stream, "application/pdf", nombrarPdf());
      rutaPdf = "http://localhost:8080/pdfs/" + archivo.getName();
      descargarPdf = true;
      return true;
    } catch (Exception e) {
      Logs.log.error("No se genero el pdf de la visita");
      Logs.log.error(e);
      return false;
    }
  }

  // GETTERS & SETTERS
  public boolean isPeriodoSepomexActivo() {
    return periodoSepomexActivo;
  }
  
  public void setPeriodoSepomexActivo(boolean periodoSepomexActivo) {
    this.periodoSepomexActivo = periodoSepomexActivo;
  }
  
  public boolean isPeriodoVisitasActivo() {
    return periodoVisitasActivo;
  }
  
  public void setPeriodoVisitasActivo(boolean periodoVisitasActivo) {
    this.periodoVisitasActivo = periodoVisitasActivo;
  }
  
  public String getRutaPdf() {
    return rutaPdf;
  }
  
  public void setRutaPdf(String rutaPdf) {
    this.rutaPdf = rutaPdf;
  }
  
  public boolean isDescargarPdf() {
    return descargarPdf;
  }
  
  public void setDescargarPdf(boolean descargarPdf) {
    this.descargarPdf = descargarPdf;
  }
  
  public StreamedContent getArchivo() {
    return archivo;
  }
  
  public void setArchivo(StreamedContent archivo) {
    this.archivo = archivo;
  }

  public Direccion getDireccion() {
    return direccion;
  }

  public void setDireccion(Direccion direccion) {
    this.direccion = direccion;
  }
  
}
