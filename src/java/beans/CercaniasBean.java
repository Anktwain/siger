/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.CreditoDAO;
import impl.CreditoIMPL;
import java.io.Serializable;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "cercaniasBean")
@ViewScoped
public class CercaniasBean implements Serializable{
  
  //LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  ListaVisitasBean listaVisitasBean = (ListaVisitasBean) elContext.getELResolver().getValue(elContext, null, "listaVisitasBean");
  
  // VARIABLES DE CLASE
  private final CreditoDAO creditoDao;
  
  // CONSTRUCTOR
  public CercaniasBean() {
    creditoDao = new CreditoIMPL();
  }
  
  // METODO QUE OBTIENE EL SALDO VENCIDO DE UN CREDITO
  public float obtenerSaldoVencido(int idCredito){
    return creditoDao.buscarSaldoVencidoCredito(idCredito);
  }
  
  // GETTERS & SETTERS

}
