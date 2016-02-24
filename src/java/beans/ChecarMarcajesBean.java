/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.CampanaDAO;
import dto.Campana;
import dto.Credito;
import impl.CampanaIMPL;
import java.io.Serializable;
import java.util.List;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "checarMarcajesBean")
@ViewScoped
public class ChecarMarcajesBean implements Serializable{
  
  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  CuentasBean cuentasBean = (CuentasBean) elContext.getELResolver().getValue(elContext, null, "cuentasBean");
  
  // VARIABLES DE CLASE
  private String marcaje;
  private String cambioCampana;
  private String gestionHoy;
  private String pagoProximo;
  private final Credito creditoActual;
  private final CampanaDAO campanaDao;
  
  //CONSTRUCTOR
  public ChecarMarcajesBean() {
    creditoActual = cuentasBean.getCreditoSeleccionado();
    campanaDao = new CampanaIMPL();
  }
  
  // METODO QUE VERIFICA EN QUE CAMPAÑA SE ENCUENTRA EL CREDITO
  public String verificarCampaña(){
    List<Campana> campanas = campanaDao.buscarTodas();
    for (int i = 0; i <(campanas.size()); i++) {
      
    }
    return "Campaña";
  }
  
  // GETTERS & SETTERS
  public String getMarcaje() {
    return marcaje;
  }

  public void setMarcaje(String marcaje) {
    this.marcaje = marcaje;
  }

  public String getCambioCampana() {
    return cambioCampana;
  }

  public void setCambioCampana(String cambioCampana) {
    this.cambioCampana = cambioCampana;
  }

  public String getGestionHoy() {
    return gestionHoy;
  }

  public void setGestionHoy(String gestionHoy) {
    this.gestionHoy = gestionHoy;
  }

  public String getPagoProximo() {
    return pagoProximo;
  }

  public void setPagoProximo(String pagoProximo) {
    this.pagoProximo = pagoProximo;
  }
  
}
