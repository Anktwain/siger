/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.CreditoDAO;
import dto.Credito;
import impl.CreditoIMPL;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import util.constantes.Perfiles;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "buscadorCreditoBean")
@ViewScoped
public class BuscadorCreditoBean implements Serializable {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");
  CreditoActualBean creditoActualBean = (CreditoActualBean) elContext.getELResolver().getValue(elContext, null, "creditoActualBean");

  // VARIABLES DE CLASE
  private String creditoABuscar;
  private final CreditoDAO creditoDao;

  // CONSTRUCTOR
  public BuscadorCreditoBean() {
    creditoDao = new CreditoIMPL();
  }

  // METODO QUE TRAE UNA LISTA DE CREDITOS SEGUN LA CADENA RECIBIDA
  public List<String> completar(String query) {
    List<String> resultados = new ArrayList();
    List<Credito> creditos = creditoDao.buscarCoincidenciasCredito(indexBean.getUsuario().getDespacho().getIdDespacho(), query);
    for (int i = 0; i < (creditos.size()); i++) {
      resultados.add(creditos.get(i).getNumeroCredito() + " | " + creditos.get(i).getGestor().getUsuario().getNombreLogin() + " | " + creditos.get(i).getDeudor().getSujeto().getNombreRazonSocial());
    }
    return resultados;
  }

  // METODO QUE ABRE EL CREDITO SELECCIONADO DEPENDIENDO EL PERFIL DEL USUARIO
  public void abrirVistaCredito(SelectEvent event) {
    String[] datos = event.getObject().toString().split(" | ");
    Credito c = creditoDao.buscar(datos[0]);
    creditoActualBean.setCreditoActual(c);
    if (indexBean.getUsuario().getPerfil() == Perfiles.GESTOR) {
      if (!indexBean.getUsuario().getNombreLogin().equals(c.getGestor().getUsuario().getNombreLogin())) {
        Logs.log.warn("El gestor " + indexBean.getUsuario().getNombreLogin() + " busco el credito " + creditoActualBean.getCreditoActual().getNumeroCredito() + " asignado al gestor " + datos[2] + ".");
      }
      try {
        FacesContext.getCurrentInstance().getExternalContext().redirect("vistaCreditoGestor.xhtml");
      } catch (IOException ioe) {
        Logs.log.error("No se pudo redirigir en la busqueda del credito por un gestor");
        Logs.log.error(ioe);
      }
    } else {
      try {
        FacesContext.getCurrentInstance().getExternalContext().redirect("vistaCreditoAdmin.xhtml");
      } catch (IOException ioe) {
        Logs.log.error("No se pudo redirigir en la busqueda del credito por un administrador");
        Logs.log.error(ioe);
      }
    }
  }

  // GETTERS & SETTERS
  public String getCreditoABuscar() {
    return creditoABuscar;
  }

  public void setCreditoABuscar(String creditoABuscar) {
    this.creditoABuscar = creditoABuscar;
  }

}
