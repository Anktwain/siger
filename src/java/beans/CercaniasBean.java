/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.CreditoDAO;
import dao.DireccionDAO;
import dto.Credito;
import dto.Direccion;
import impl.CreditoIMPL;
import impl.DireccionIMPL;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "cercaniasBean")
@ViewScoped
public class CercaniasBean implements Serializable {

  //LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  ListaVisitasBean listaVisitasBean = (ListaVisitasBean) elContext.getELResolver().getValue(elContext, null, "listaVisitasBean");

  // VARIABLES DE CLASE
  private boolean habilitaDirecciones;
  private boolean habilitaCercanias;
  private final CreditoDAO creditoDao;
  private final DireccionDAO direccionDao;
  private List<Direccion> listaCercanias;
  private int cantidadCercanias;
  private String radio;

  // CONSTRUCTOR
  public CercaniasBean() {
    cantidadCercanias = 10;
    habilitaCercanias = false;
    listaCercanias = new ArrayList();
    creditoDao = new CreditoIMPL();
    direccionDao = new DireccionIMPL();
    cargarDatos();
  }

  // METODO QUE CARGA LOS DATOS DE INICIO
  public final void cargarDatos() {
    checarDirecciones();
  }

  // METODO QUE VERIFICA SI HAY DIRECCIONES DIFERENTES
  public final void checarDirecciones() {
    habilitaDirecciones = !listaVisitasBean.getListaDireccionesEncontradas().isEmpty();
  }

  // METODO QUE REFRESCA LA VISTA Y CARGA LA DIRECCION SELECCIONADA
  public void recargar() {
    listaVisitasBean.setDireccionSeleccionada(listaVisitasBean.getListaDireccionesEncontradas().get(listaVisitasBean.getDireccionSeleccionada().getIdResultado() + 1));
    listaVisitasBean.setLatitud(listaVisitasBean.getDireccionSeleccionada().getLat());
    listaVisitasBean.setLongitud(listaVisitasBean.getDireccionSeleccionada().getLon());
    listaVisitasBean.setListaDireccionesEncontradas(new ArrayList());
    try {
      FacesContext.getCurrentInstance().getExternalContext().redirect("cercanias.xhtml");
    } catch (IOException ioe) {
      Logs.log.error("No se pudo redirigir a la vista cercanias.");
      Logs.log.error(ioe);
    }
  }

  // METODO QUE OBTIENE EL SALDO VENCIDO DE UN CREDITO
  public float obtenerSaldoVencido(int idCredito) {
    return creditoDao.buscarSaldoVencidoCredito(idCredito);
  }
  
  // METODO QUE OBTIENE LOS DATOS DE CREDITOS CERCANOS
  public void cargarCercanias() {
    System.out.println("ENTRO AL METODO");
    listaCercanias = direccionDao.obtenerDireccionesPorRadio(new BigDecimal(listaVisitasBean.getLatitud()), new BigDecimal(listaVisitasBean.getLongitud()), Integer.parseInt(radio), cantidadCercanias, listaVisitasBean.indexBean.getUsuario().getDespacho().getIdDespacho());
    if (!listaCercanias.isEmpty()) {
      habilitaCercanias = true;
      for (int i = 0; i < (listaCercanias.size()); i++) {
        Credito c = creditoDao.buscarPorSujeto(listaCercanias.get(i).getSujeto().getIdSujeto());
        String texto = "'" + listaCercanias.get(i).getSujeto().getNombreRazonSocial() + "<br />Credito: " + c.getNumeroCredito() + "<br />Adeudo: $" + obtenerSaldoVencido(c.getIdCredito()) + "'";
        System.out.println(texto);
        RequestContext.getCurrentInstance().execute("agregarCercania(" + listaCercanias.get(i).getLatitud().toString() + ", " + listaCercanias.get(i).getLongitud().toString() + ", " + texto + ");");
      }
    }
  }

  // GETTERS & SETTERS
  public boolean isHabilitaDirecciones() {
    return habilitaDirecciones;
  }

  public void setHabilitaDirecciones(boolean habilitaDirecciones) {
    this.habilitaDirecciones = habilitaDirecciones;
  }

  public boolean isHabilitaCercanias() {
    return habilitaCercanias;
  }

  public void setHabilitaCercanias(boolean habilitaCercanias) {
    this.habilitaCercanias = habilitaCercanias;
  }

  public List<Direccion> getListaCercanias() {
    return listaCercanias;
  }

  public void setListaCercanias(List<Direccion> listaCercanias) {
    this.listaCercanias = listaCercanias;
  }

  public int getCantidadCercanias() {
    return cantidadCercanias;
  }

  public void setCantidadCercanias(int cantidadCercanias) {
    this.cantidadCercanias = cantidadCercanias;
  }

  public String getRadio() {
    return radio;
  }

  public void setRadio(String radio) {
    this.radio = radio;
  }

}
