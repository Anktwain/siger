/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.ConvenioPagoDAO;
import dao.EstatusInformativoDAO;
import dao.GestionDAO;
import dto.ConvenioPago;
import dto.Credito;
import dto.EstatusInformativo;
import dto.Gestion;
import impl.ConvenioPagoIMPL;
import impl.EstatusInformativoIMPL;
import impl.GestionIMPL;
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
import util.constantes.Convenios;
import util.constantes.Gestiones;

/**
 *
 * @author Eduardo
 */

@ManagedBean(name = "conveniosBean")
@ViewScoped
public class ConveniosBean implements Serializable {
  
  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  VistaCreditoBean vistaCreditoBean = (VistaCreditoBean) elContext.getELResolver().getValue(elContext, null, "vistaCreditoBean");
  
  // VARIABLES DE CLASE
  private float saldoNuevoConvenio;
  private Credito creditoActual;
  private ConvenioPagoDAO convenioPagoDao;
  private List<ConvenioPago> listaConvenios;
  private List<ConvenioPago> convenioSeleccionado;
  private List<ConvenioPago> listaHistorialConvenios;
  private EstatusInformativoDAO estatusInformativoDao;
  private GestionDAO gestionDao;
  
  // CONSTRUCTOR
  public ConveniosBean() {
    saldoNuevoConvenio = 0;
    creditoActual = vistaCreditoBean.getCreditoActual();
    convenioPagoDao = new ConvenioPagoIMPL();
    listaConvenios = new ArrayList();
    listaHistorialConvenios = new ArrayList();
    estatusInformativoDao = new EstatusInformativoIMPL();
    gestionDao = new GestionIMPL();
    cargarListas();
  }
  
  // METODO QUE TRAE LA LISTA DE CONVENIOS
  public final void cargarListas(){
    int idCredito = creditoActual.getIdCredito();
    listaConvenios = convenioPagoDao.buscarConveniosEnCursoCredito(idCredito);
    listaHistorialConvenios = convenioPagoDao.buscarConveniosFinalizadosCredito(idCredito);
  }
  
  // METODO QUE CARGA UN PAGO A UN CONVENIO EN ESPECIFICO
  // NOTA: SE UTILIZARA EL CONCEPTO DE PROMESA DE PAGO PARA HACER LAS INSERCIONES A LA BASE DE DATOS
  // SIN EMBARGO ESTE CONCEPTO DESAPARECERA EN LA SIGUIENTE VERSION
  public void agregarPago(){
    
  }  
  
  // METODO QUE AGREGA UN CONVENIO DE PAGO
  public void agregarConvenio(){
    ConvenioPago convenio = new ConvenioPago();
    convenio.setCredito(creditoActual);
    convenio.setEstatus(Convenios.EN_CURSO);
    Date fecha = new Date();
    convenio.setFecha(fecha);
    convenio.setPagosRealizados(0);
    convenio.setSaldoNegociado(saldoNuevoConvenio);
    FacesContext contexto = FacesContext.getCurrentInstance();
    boolean ok = convenioPagoDao.insertar(convenio);
    if(ok){
      Gestion g = new Gestion();
      g.setTipoGestion(Gestiones.ASUNTO.get(2));
      g.setLugarGestion(Gestiones.DONDE_CORPORATIVO.get(0));
      g.setAsuntoGestion(Gestiones.ASUNTO.get(6));
      g.setDescripcionGestion("SE REALIZA CONVENIO DE PAGO CON ");
      g.setSujetoGestion(Gestiones.TIPO_SUJETOS.get(0));
      EstatusInformativo e = estatusInformativoDao.buscar(2);
      g.setEstatusInformativo(e);
      g.setGestion("SALDO NEGOCIADO: " + saldoNuevoConvenio);
      ok = ok & (gestionDao.insertarGestion(g));
      if (ok){
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se creo un nuevo convenio."));
      cargarListas();
      }
      else{
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se creo el convenio. Contacte al equipo de sistemas."));
      }
    }
    else{
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se creo el convenio. Contacte al equipo de sistemas."));
    }
    RequestContext.getCurrentInstance().execute("PF('agregarConvenioDialog').hide();");
  }

  // ***********************************************************************************************************************
  // ***********************************************************************************************************************
  // ***********************************************************************************************************************
  // GETTERS & SETTERS
  
  public List<ConvenioPago> getConvenioSeleccionado() {
    return convenioSeleccionado;
  }

  public void setConvenioSeleccionado(List<ConvenioPago> convenioSeleccionado) {
    this.convenioSeleccionado = convenioSeleccionado;
  }

  public ConvenioPagoDAO getConvenioPagoDao() {
      return convenioPagoDao;
  }

  public void setConvenioPagoDao(ConvenioPagoDAO convenioPagoDao) {
    this.convenioPagoDao = convenioPagoDao;
  }

  public List<ConvenioPago> getListaConvenios() {
    return listaConvenios;
  }

  public void setListaConvenios(List<ConvenioPago> listaConvenios) {
    this.listaConvenios = listaConvenios;
  }

  public ELContext getElContext() {
    return elContext;
  }

  public void setElContext(ELContext elContext) {
    this.elContext = elContext;
  }

  public VistaCreditoBean getVistaCreditoBean() {
    return vistaCreditoBean;
  }

  public void setVistaCreditoBean(VistaCreditoBean vistaCreditoBean) {
    this.vistaCreditoBean = vistaCreditoBean;
  }

  public List<ConvenioPago> getListaHistorialConvenios() {
    return listaHistorialConvenios;
  }

  public void setListaHistorialConvenios(List<ConvenioPago> listaHistorialConvenios) {
    this.listaHistorialConvenios = listaHistorialConvenios;
  }

  public float getSaldoNuevoConvenio() {
    return saldoNuevoConvenio;
  }

  public void setSaldoNuevoConvenio(float saldoNuevoConvenio) {
    this.saldoNuevoConvenio = saldoNuevoConvenio;
  }

  public Credito getCreditoActual() {
    return creditoActual;
  }

  public void setCreditoActual(Credito creditoActual) {
    this.creditoActual = creditoActual;
  }
  
}
