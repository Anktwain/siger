/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import dao.PagoDAO;
import dto.Pago;
import impl.PagoIMPL;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.model.chart.PieChartModel;
import util.constantes.Pagos;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "progresoTotalGestorBean")
@ViewScoped
public class ProgresoTotalGestorBean implements Serializable {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  // VARIABLES DE CLASE
  private boolean permitirExport;
  private PieChartModel graficaGestionesHoy;
  private PieChartModel graficaPagosHoy;
  private PieChartModel graficaPagosQuincena;
  private List<Pago> listaPagosQuincena;
  private final PagoDAO pagoDao;

  // CONSTRUCTOR
  public ProgresoTotalGestorBean() {
    permitirExport = false;
    listaPagosQuincena = new ArrayList();
    pagoDao = new PagoIMPL();
    cargarDatos();
  }
  
  // METODO QUE CARGA TODOS LOS DATOS PARA LOS GESTORES
  public final void cargarDatos(){
    cargarGraficas();
    listaPagosQuincena = pagoDao.buscarTodosPagosGestor(indexBean.getUsuario().getIdUsuario());
    if(!listaPagosQuincena.isEmpty()){
      permitirExport = true;
      RequestContext.getCurrentInstance().update("pagosQuincenaForm");
    }
  }

  // METODO QUE CARGA LAS GRAFICAS
  public final void cargarGraficas() {
    // GRAFICA DE GESTIONES HOY
    graficaGestionesHoy = new PieChartModel();
    graficaGestionesHoy.set("Con contacto", 25);
    graficaGestionesHoy.set("Sin contacto", 34);
    graficaGestionesHoy.set("Convenios de pago", 59);
    graficaGestionesHoy.setTitle("Gestiones hoy");
    graficaGestionesHoy.setLegendPosition("w");
    // GRAFICA DE PAGOS HOY
    graficaPagosHoy = new PieChartModel();
    graficaPagosHoy.set("Aprobados", 6);
    graficaPagosHoy.set("Rechazados", 3);
    graficaPagosHoy.set("Pendientes", 1);
    graficaPagosHoy.setTitle("Pagos hoy");
    graficaPagosHoy.setLegendPosition("w");
    // GRAFICA DE PAGOS QUINCENA
    graficaPagosQuincena = new PieChartModel();
    graficaPagosQuincena.set("Monto aprobado", 913480);
    graficaPagosQuincena.set("Monto no aplicado", 86520);
    graficaPagosQuincena.setTitle("Pagos quincena");
    graficaPagosQuincena.setLegendPosition("w");
    RequestContext.getCurrentInstance().update("gestionesHoyForm");
    RequestContext.getCurrentInstance().update("pagosHoyForm");
    RequestContext.getCurrentInstance().update("pagosQuincenaForm");
  }

  // METODO QUE PREPARA EL PDF PARA QUE TENGA MEJOR ESTETICA
  public void preparaPdf(Object document) {
    Document pdf = (Document) document;
    pdf.setPageSize(PageSize.LETTER.rotate());
    pdf.setMargins(10, 10, 10, 10);
    pdf.open();
  }
  
  // METODO QUE LE DA UNA ETIQUETA A LOS VALORES NUMERICOS DEL ESTATUS DE PAGOS
  public String etiquetarEstatus(int estatus) {
    String estado = "";
    if (estatus == Pagos.APROBADO) {
      estado = "Aprobado";
    }
    if (estatus == Pagos.PENDIENTE) {
      estado = "Pendiente";
    }
    if (estatus == Pagos.RECHAZADO) {
      estado = "Rechazado";
    }
    if (estatus == Pagos.REVISION_BANCO) {
      estado = "Revision banco";
    }
    return estado;
  }
  
  // METODO QUE LE DA UNA ETIQUETA AL ESTATUS DE PAGO
  public String etiquetarEstatusPago(int pagado){
    String estado = "";
    if(pagado == Pagos.PAGADO){
      estado = "Pagado";
    }
    if(pagado == Pagos.NO_PAGADO){
      estado = "Por pagar";
    }
    return estado;
  }
  
  // METODO QUE DEVUELVE EL NUMERO DE PAGOS PENDIENTES PARA ESTE GESTOR HOY
  public int pagosPendientesHoy() {
    return 0;
  }

  // GETTERS & SETTERS
  public PieChartModel getGraficaGestionesHoy() {
    return graficaGestionesHoy;
  }

  public PieChartModel getGraficaPagosHoy() {
    return graficaPagosHoy;
  }

  public PieChartModel getGraficaPagosQuincena() {
    return graficaPagosQuincena;
  }

  public List<Pago> getListaPagosQuincena() {
    return listaPagosQuincena;
  }

  public void setListaPagosQuincena(List<Pago> listaPagosQuincena) {
    this.listaPagosQuincena = listaPagosQuincena;
  }

  public boolean isPermitirExport() {
    return permitirExport;
  }

  public void setPermitirExport(boolean permitirExport) {
    this.permitirExport = permitirExport;
  }

}
