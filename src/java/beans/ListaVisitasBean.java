/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.ImpresionDAO;
import dto.Impresion;
import impl.ImpresionIMPL;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import util.constantes.Impresiones;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "listaVisitasBean")
@ViewScoped
public class ListaVisitasBean implements Serializable{
  
  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");
  
  // VARIABLES DE CLASE
  private List<Visitas> listaVisitas;
  private List<Visitas> listaCorreo;
  private Visitas visitaSeleccionada;
  private final ImpresionDAO impresionDao;
  
  // CONSTRUCTOR
  public ListaVisitasBean() {
    impresionDao = new ImpresionIMPL();
    obtenerListas();
  }
  
  // METODO QUE CARGA LAS LISTAS INICIALES
  public final void obtenerListas(){
    List<Impresion> impresiones = obtenerVisitasOrdenadas();
    for (int i = 0; i <(impresiones.size()); i++) {
      Visitas v = new Visitas();
      v.setIdImpresion(impresiones.get(i).getIdImpresion());
      v.setNumeroCredito(impresiones.get(i).getCredito().getNumeroCredito());
      v.setDeudor(impresiones.get(i).getCredito().getDeudor().getSujeto().getNombreRazonSocial());
    }
  }
  
  // METODO QUE CARGA LA LISTA DE VISITAS DOMICILIARIAS Y LAS ORDENA DE LA SIGUIENTE FORMA:
  // PRIMERO LAS DE LA CIUDAD DE MEXICO ORDENADAS POR NOMBRE DE DELEGACION
  // DESPUES LAS DE LOS 18 MUNICIPIOS CONURBADOS ORDENADAS POR NOMBRE DEL MUNICIPIO
  public List<Impresion> obtenerVisitasOrdenadas(){
    List<Impresion> total = new ArrayList();
    total.addAll(impresionDao.buscarImpresionesAreaMetropolitana(indexBean.getUsuario().getDespacho().getIdDespacho()));
    total.addAll(impresionDao.buscarImpresionesInteriorRepublica(indexBean.getUsuario().getDespacho().getIdDespacho()));
    return total;
  }
  
  // GETTERS & SETTERS
  
  // CLASE MIEMBRO QUE SERVIRA PARA MOSTRAR LOS DATOS DE LA DIRECCION DEL DEUDOR
  public static class Visitas {
    
    // VARIABLES DE CLASE
    private int idImpresion;
    private String numeroCredito;
    private String deudor;
    private String calleNum;
    private String colonia;
    private String municipio;
    private String estado;
    private String cp;
    
    // CONSTRUCTOR
    public Visitas() {
    }
    
    // GETTERS & SETTERS
    public int getIdImpresion() {
      return idImpresion;
    }

    public void setIdImpresion(int idImpresion) {
      this.idImpresion = idImpresion;
    }

    public String getNumeroCredito() {
      return numeroCredito;
    }

    public void setNumeroCredito(String numeroCredito) {
      this.numeroCredito = numeroCredito;
    }

    public String getDeudor() {
      return deudor;
    }

    public void setDeudor(String deudor) {
      this.deudor = deudor;
    }

    public String getCalleNum() {
      return calleNum;
    }

    public void setCalleNum(String calleNum) {
      this.calleNum = calleNum;
    }

    public String getColonia() {
      return colonia;
    }

    public void setColonia(String colonia) {
      this.colonia = colonia;
    }

    public String getMunicipio() {
      return municipio;
    }

    public void setMunicipio(String municipio) {
      this.municipio = municipio;
    }

    public String getEstado() {
      return estado;
    }

    public void setEstado(String estado) {
      this.estado = estado;
    }

    public String getCp() {
      return cp;
    }

    public void setCp(String cp) {
      this.cp = cp;
    }
    
  }
  
}
