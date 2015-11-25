/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.CreditoDAO;
import impl.CreditoIMPL;
import java.io.IOException;
import java.util.List;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import util.constantes.Perfiles;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "busquedaBean")
@ViewScoped
public class BusquedaBean {
  
  // VARIABLES DE CLASE
  private String creditoBuscado;
  private CreditoDAO creditoDAO;
  
  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  public BusquedaBean() {
    creditoDAO = new CreditoIMPL();
  }
  
  // METODO QUE TRAE TODOS LOS RESULTADOS DE LA BASE DE DATOS EN CUANTO A NOMBRES Y NUMEROS DE CREDITO
  public List<String> autocompletar(String query){
    System.out.println("VALOR A BUSCAR: " + creditoBuscado);
    int perfil = indexBean.getUsuario().getPerfil();
    if(perfil == Perfiles.ADMINISTRADOR){
      return creditoDAO.barraBusquedaAdmin(query, indexBean.getUsuario().getDespacho().getIdDespacho());
    }
    else {
      return creditoDAO.barraBusquedaGestor(query, indexBean.getUsuario().getIdUsuario());
    }
  }

  public void handleSelect(SelectEvent event) throws IOException {
    String nombre = event.getObject().toString();
    System.out.println("NOMBRE HANDLE SELECT: " + nombre);
    List<String> resultados = autocompletar(nombre);
  }
  
  // METODO QUE REDIRIGE A LA VISTA DEL DETALLE CREDITO CON LOS DATOS YA PRECARGADOS
  /*
  public void buscarCredito() throws IOException{
    vistaCreditoBean.setCreditoActual(creditoDAO.buscarCreditoPorId(creditoDAO.obtenerIdDelCredito(creditoBuscado)));
    FacesContext.getCurrentInstance().getExternalContext().redirect("vistaCredito.xhtml");
  }
  */
  
  // ***********************************************************************************************************************
  // ***********************************************************************************************************************
  // ***********************************************************************************************************************
  // GETTERS & SETTERS
  
  public String getCreditoBuscado() {
    return creditoBuscado;
  }

  public void setCreditoBuscado(String creditoBuscado) {
    this.creditoBuscado = creditoBuscado;
  }
  /*
  public ELContext getElContext() {
    return elContext;
  }

  public void setElContext(ELContext elContext) {
    this.elContext = elContext;
  }
  */
  public CreditoDAO getCreditoDAO() {
    return creditoDAO;
  }

  public void setCreditoDAO(CreditoDAO creditoDAO) {
    this.creditoDAO = creditoDAO;
  }
  /*
  public VistaCreditoBean getVistaCreditoBean() {
    return vistaCreditoBean;
  }

  public void setVistaCreditoBean(VistaCreditoBean vistaCreditoBean) {
    this.vistaCreditoBean = vistaCreditoBean;
  }
  */
  
}
