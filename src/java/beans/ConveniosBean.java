/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.ConvenioPagoDAO;
import dto.ConvenioPago;
import dto.Credito;
import dto.Gestor;
import impl.ConvenioPagoIMPL;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

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
  private Credito creditoSeleccionado;
  private ConvenioPago convenioSeleccionado;
  private ConvenioPagoDAO convenioPagoDao;
  private List<ConvenioPago> listaConvenios;
  
  // CONSTRUCTOR
  public ConveniosBean() {
    creditoSeleccionado = vistaCreditoBean.getCreditoActual();
    convenioPagoDao = new ConvenioPagoIMPL();
    listaConvenios = new ArrayList();
    listaConvenios = convenioPagoDao.buscarConveniosPorCredito(creditoSeleccionado.getIdCredito());
  }
  
  // ***********************************************************************************************************************
  // ***********************************************************************************************************************
  // ***********************************************************************************************************************
  // GETTERS & SETTERS

  public ConvenioPago getConvenioSeleccionado() {
    return convenioSeleccionado;
  }

  public void setConvenioSeleccionado(ConvenioPago convenioSeleccionado) {
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

  public Credito getCreditoSeleccionado() {
    return creditoSeleccionado;
  }

  public void setCreditoSeleccionado(Credito creditoSeleccionado) {
    this.creditoSeleccionado = creditoSeleccionado;
  }
  
}
