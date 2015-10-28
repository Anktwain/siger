/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.DevolucionDAO;
import dto.Devolucion;
import impl.DevolucionIMPL;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "devolucionBean")
@ViewScoped
public class DevolucionBean implements Serializable {

  // CLASE DEV PARA PODER LLENAR LAS TABLAS DE LA VISTA
  public static class Dev{

    private Date fecha;
    private int estatus;
    private String numeroCredito;
    private String nombreRazonSocial;
    private String concepto;
    
    public Dev(){}
    
    public Date getFecha() {
      return fecha;
    }

    public void setFecha(Date fecha) {
      this.fecha = fecha;
    }

    public int getEstatus() {
      return estatus;
    }

    public void setEstatus(int estatus) {
      this.estatus = estatus;
    }

    public String getNumeroCredito() {
      return numeroCredito;
    }

    public void setNumeroCredito(String numeroCredito) {
      this.numeroCredito = numeroCredito;
    }

    public String getNombreRazonSocial() {
      return nombreRazonSocial;
    }

    public void setNombreRazonSocial(String nombreRazonSocial) {
      this.nombreRazonSocial = nombreRazonSocial;
    }

    public String getConcepto() {
      return concepto;
    }

    public void setConcepto(String concepto) {
      this.concepto = concepto;
    }
    
  }
  
  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  // VARIABLES DE CLASE
  private DevolucionDAO devolucionDao;
  private List<Dev> listaDevoluciones;

  // CONSTRUCTOR
  public DevolucionBean() {
    devolucionDao = new DevolucionIMPL();
    listaDevoluciones = new ArrayList<>();
    obtenerDevoluciones();
  }

  // METODO PARA APROBAR UNA DEVOLUCION
  public void aprobar() {

  }

  // METODO PARA RECHAZAR UNA DEVOLUCION
  public void rechazar() {

  }

  // METODO PARA PEDIR AL BANCO UNA CONSERVACION
  public void conservar() {

  }

  // METODO PARA OBTENER LOS CREDITOS PENDIENTES O EN ESPERA DE CONSERVACION
  public void obtenerDevoluciones() {
    listaDevoluciones = devolucionDao.retiradosBancoPorDespacho(indexBean.getUsuario().getDespacho().getIdDespacho());
    for (int i = 0; i < listaDevoluciones.size(); i++) {
      System.out.println(listaDevoluciones.get(i).getFecha());
      System.out.println(listaDevoluciones.get(i).getEstatus());
      System.out.println(listaDevoluciones.get(i).getNumeroCredito());
      System.out.println(listaDevoluciones.get(i).getNombreRazonSocial());
      System.out.println(listaDevoluciones.get(i).getConcepto());
    }
  }

  // ***********************************************************************************************************************
  // ***********************************************************************************************************************
  // ***********************************************************************************************************************
  // GETTERS & SETTERS
  
  public List<Dev> getListaDevoluciones() {
    return listaDevoluciones;
  }

  public void setListaDevoluciones(List<Dev> listaDevoluciones) {
    this.listaDevoluciones = listaDevoluciones;
  }

  public DevolucionDAO getDevolucionDao() {
    return devolucionDao;
  }

  public void setDevolucionDao(DevolucionDAO devolucionDao) {
    this.devolucionDao = devolucionDao;
  }

  public IndexBean getIndexBean() {
    return indexBean;
  }

  public void setIndexBean(IndexBean indexBean) {
    this.indexBean = indexBean;
  }
  
  public ELContext getElContext() {
    return elContext;
  }

  public void setElContext(ELContext elContext) {
    this.elContext = elContext;
  }

}
