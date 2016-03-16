/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.CreditoDAO;
import dao.DireccionDAO;
import dto.Credito;
import dto.Direccion;
import impl.CreditoIMPL;
import impl.DireccionIMPL;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import util.constantes.Perfiles;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "visitasBean")
@ViewScoped
public class VisitasBean {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  // VARIABLES DE CLASE
  private final CreditoDAO creditoDao;
  private final DireccionDAO direccionDao;
  private Credito creditoActual;
  private CuentasBean cuentasBean;
  private CuentasGestorBean cuentasGestorBean;
  private boolean periodoSepomexActivo;
  private boolean periodoVisitasActivo;
  private boolean periodoEmailActivo;
  private boolean descargarPdf;
  private String pdf;
  StreamedContent archivo;

  // CONSTRUCTOR
  public VisitasBean() {
    direccionDao = new DireccionIMPL();
    creditoDao = new CreditoIMPL();
    creditoActual = new Credito();
    periodoSepomexActivo = false;
    periodoVisitasActivo = false;
    periodoEmailActivo = false;
    descargarPdf = false;
    verificaUsuario();
    verificarPeriodoSepomex();
    verificarPeriodoVisitas();
    verificarPeriodoEmail();
  }

  // METODO QUE VERIFICA SI ESTA GESTIONANDO UN ADMINISTRADOR O UN GESTOR
  public final void verificaUsuario() {
    if (indexBean.getUsuario().getPerfil() == Perfiles.GESTOR) {
      cuentasGestorBean = (CuentasGestorBean) elContext.getELResolver().getValue(elContext, null, "cuentasGestorBean");
      creditoActual = cuentasGestorBean.getCreditoActual();
    } else {
      cuentasBean = (CuentasBean) elContext.getELResolver().getValue(elContext, null, "cuentasBean");
      creditoActual = cuentasBean.getCreditoSeleccionado();
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
    }
  }

  // METODO QUE VERIFICA SI EL PERIODODE IMPRESIONES PARA VISITAS ESTA ACTIVO
  public final void verificarPeriodoEmail() {
    Date fechaActual = new Date();
    try {
      String cadena = ManejadorArchivosDeTexto.leerArchivo(Directorios.RUTA_WINDOWS_PERIODO_IMPRESIONES, "CORREO_ELECTRONICO.txt");
      if (!cadena.isEmpty()) {
        String[] arreglo = cadena.split(";");
        Date fechaInicial, fechaFinal;
        fechaInicial = new Date(Long.parseLong(arreglo[0]));
        fechaFinal = new Date(Long.parseLong(arreglo[1]));
        if (fechaActual.after(fechaInicial) && (fechaActual.before(fechaFinal))) {
          periodoEmailActivo = true;
        }
      }
    } catch (IOException ioe) {
    }
  }

  // METODO QUE GENERA EL PDF
  public void generarPdf() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    List<Direccion> direcciones = direccionDao.buscarPorSujeto(creditoActual.getDeudor().getSujeto().getIdSujeto());
    if (direcciones.isEmpty()) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El credito no tiene ninguna direccion asociada."));
    } else {
      try {
        InputStream stream = new FileInputStream(GeneradorPdf.crearPdf(nombrarPdf(), creditoActual, direcciones.get(0), creditoDao.buscarSaldoVencidoCredito(creditoActual.getIdCredito())));
        archivo = new DefaultStreamedContent(stream, "application/pdf", nombrarPdf());
        descargarPdf = true;
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se genero un archivo PDF con la visita."));
      } catch (Exception e) {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se genero el archivo PDF. Contacte al equipo de sistemas."));
        e.printStackTrace();
      }
    }
  }

  // METODO QUE GENERA UN NOMBRE DE ARCHIVO PARA EL PDF
  public String nombrarPdf() {
    DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    return creditoActual.getNumeroCredito() + "_" + df.format(new Date()) + ".pdf";
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

  public boolean isPeriodoEmailActivo() {
    return periodoEmailActivo;
  }

  public void setPeriodoEmailActivo(boolean periodoEmailActivo) {
    this.periodoEmailActivo = periodoEmailActivo;
  }

  public String getPdf() {
    return pdf;
  }

  public void setPdf(String pdf) {
    this.pdf = pdf;
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

}
