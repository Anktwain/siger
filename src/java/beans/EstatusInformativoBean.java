/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.EstatusInformativoDAO;
import dto.EstatusInformativo;
import impl.EstatusInformativoIMPL;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "estatusInformativoBean")
@ViewScoped
public class EstatusInformativoBean implements Serializable {

  // VARIABLES DE CLASE
  private String nuevoEstatus;
  private List<EstatusInformativo> listaEstatus;
  private EstatusInformativoDAO estatusInformativoDAO;

  public EstatusInformativoBean() {
    listaEstatus = new ArrayList();
    estatusInformativoDAO = new EstatusInformativoIMPL();
    cargarEstatus();
  }

  // METODO  QUE CARGA LA LISTA DE ESTATUS INFORMATIVO
  public void cargarEstatus() {
    listaEstatus = estatusInformativoDAO.buscarTodos();
  }

  // METODO QUE CREA UN NUEVO ESTATUS
  public void crear() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    if (!nuevoEstatus.equals("")) {
      EstatusInformativo e = new EstatusInformativo();
      e.setEstatus(nuevoEstatus);
      boolean ok = estatusInformativoDAO.insertar(e);
      if (ok) {
        RequestContext con = RequestContext.getCurrentInstance();
        cargarEstatus();
        con.execute("PF('dialogoNuevoEstatus').hide();");
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se registro el nuevo estatus"));
      } else {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo registrar la informacion. Contacte con el administrador de base de datos"));
      }
    } else {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No ingreso informacion"));
    }
  }

  // ***********************************************************************************************************************
  // ***********************************************************************************************************************
  // ***********************************************************************************************************************
  // GETTERS & SETTERS
  public List<EstatusInformativo> getListaEstatus() {
    return listaEstatus;
  }

  public void setListaEstatus(List<EstatusInformativo> listaEstatus) {
    this.listaEstatus = listaEstatus;
  }

  public EstatusInformativoDAO getEstatusInformativoDAO() {
    return estatusInformativoDAO;
  }

  public void setEstatusInformativoDAO(EstatusInformativoDAO estatusInformativoDAO) {
    this.estatusInformativoDAO = estatusInformativoDAO;
  }

  public String getNuevoEstatus() {
    return nuevoEstatus;
  }

  public void setNuevoEstatus(String nuevoEstatus) {
    this.nuevoEstatus = nuevoEstatus;
  }

}
