/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.ConvenioPagoDAO;
import dao.CreditoDAO;
import dao.PagoDAO;
import dto.ConvenioPago;
import dto.Credito;
import dto.Pago;
import impl.ConvenioPagoIMPL;
import impl.CreditoIMPL;
import impl.PagoIMPL;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import util.constantes.Pagos;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "pagosBean")
@SessionScoped
public class PagosBean implements Serializable {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  // VARIABLES DE CLASE
  private final int idDespacho;
  private String nombreImagen;
  private String revisor;
  private Pago pagoSeleccionado;
  private PagoDAO pagoDao;
  private CreditoDAO creditoDao;
  private ConvenioPagoDAO convenioPagoDao;
  private List<Pago> pagosPorRevisar;
  private List<Pago> revisionSeleccionados;
  private List<Pago> listaPagos;

  // CONSTRUCTOR
  public PagosBean() {
    idDespacho = indexBean.getUsuario().getDespacho().getIdDespacho();
    revisor = indexBean.getUsuario().getNombreLogin();
    pagoDao = new PagoIMPL();
    creditoDao = new CreditoIMPL();
    convenioPagoDao = new ConvenioPagoIMPL();
    pagosPorRevisar = new ArrayList();
    revisionSeleccionados = new ArrayList();
    listaPagos = new ArrayList();
    cargarListas();
  }

  // METODO QUE GENERA LAS LISTAS CON INFORMACION DE LA BASE DE DATOS
  public final void cargarListas() {
    pagosPorRevisar = pagoDao.pagosPorRevisarPorDespacho(idDespacho);
    listaPagos = pagoDao.pagosPorDespacho(idDespacho);
  }

  // METODO QUE TRAE LOS DATOS DEL PAGO SELECCIONADO
  public void visualizar() {
    pagoSeleccionado = revisionSeleccionados.get(0);
    nombreImagen = pagoSeleccionado.getNombreComprobante();
    System.out.println("COMPROBANTE SELECCIONADO: " + nombreImagen);
  }

// METODO QUE APRUEBA UN PAGO
  public void aprobarPago() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    pagoSeleccionado = revisionSeleccionados.get(0);
    Credito c = creditoDao.buscar(pagoSeleccionado.getConvenioPago().getCredito().getNumeroCredito());
    float monto = c.getMonto() - pagoSeleccionado.getMonto();
    c.setMonto(monto);
    boolean ok = creditoDao.editar(c);
    pagoSeleccionado.setEstatus(Pagos.APROBADO);
    pagoSeleccionado.setRevisor(revisor);
    ok = ok & (pagoDao.editar(pagoSeleccionado));
    ConvenioPago co = pagoSeleccionado.getConvenioPago();
    int numPagos = co.getPagosRealizados() + 1;
    co.setPagosRealizados(numPagos);
    ok = ok & (convenioPagoDao.editar(co));
    if (ok) {
      cargarListas();
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se aprobo el pago."));
    } else {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo aprobar el pago. Contacte al equipo de sistemas."));
    }
  }

  // METODO QUE RECHAZA UN PAGO
  public void rechazarPago() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    pagoSeleccionado = revisionSeleccionados.get(0);
    pagoSeleccionado.setEstatus(Pagos.RECHAZADO);
    pagoSeleccionado.setRevisor(revisor);
    boolean ok = pagoDao.editar(pagoSeleccionado);
    if (ok) {
      cargarListas();
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se rechazo el pago."));
    } else {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo rechazar el pago. Contacte al equipo de sistemas."));
    }
  }

  // METODO QUE LE DA UNA ETIQUETA A LOS VALORES NUMERICOS DEL ESTATUS DE PAGOS
  public String etiquetarEstatus(int estatus) {
    String estado = null;
    if (estatus == Pagos.APROBADO) {
      estado = "Aprobado";
    }
    if (estatus == Pagos.PENDIENTE) {
      estado = "Pendiente";
    }
    if (estatus == Pagos.RECHAZADO) {
      estado = "Rechazado";
    }
    return estado;
  }

// ***********************************************************************************************************************
// ***********************************************************************************************************************
// ***********************************************************************************************************************
// GETTERS & SETTERS
  public PagoDAO getPagoDao() {
    return pagoDao;
  }

  public void setPagoDao(PagoDAO pagoDao) {
    this.pagoDao = pagoDao;
  }

  public List<Pago> getPagosPorRevisar() {
    return pagosPorRevisar;
  }

  public void setPagosPorRevisar(List<Pago> pagosPorRevisar) {
    this.pagosPorRevisar = pagosPorRevisar;
  }

  public List<Pago> getRevisionSeleccionados() {
    return revisionSeleccionados;
  }

  public void setRevisionSeleccionados(List<Pago> revisionSeleccionados) {
    this.revisionSeleccionados = revisionSeleccionados;
  }

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

  public List<Pago> getListaPagos() {
    return listaPagos;
  }

  public void setListaPagos(List<Pago> listaPagos) {
    this.listaPagos = listaPagos;
  }

  public CreditoDAO getCreditoDao() {
    return creditoDao;
  }

  public void setCreditoDao(CreditoDAO creditoDao) {
    this.creditoDao = creditoDao;
  }

  public Pago getPagoSeleccionado() {
    return pagoSeleccionado;
  }

  public void setPagoSeleccionado(Pago pagoSeleccionado) {
    this.pagoSeleccionado = pagoSeleccionado;
  }

  public String getNombreImagen() {
    return nombreImagen;
  }

  public void setNombreImagen(String nombreImagen) {
    this.nombreImagen = nombreImagen;
  }
}
