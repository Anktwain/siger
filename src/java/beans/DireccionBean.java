/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.DireccionDAO;
import dto.Direccion;
import dto.Sujeto;
import impl.DireccionIMPL;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import util.log.Logs;

/**
 *
 * @author brionvega
 */
@ManagedBean
@ViewScoped
public class DireccionBean implements Serializable {

  // Objeto Direccion, sus propiedades y acceso a la BD
  private Direccion direccion;

  private String calle;
  private String colonia;
  private String municipio;
  private String estado;
  private String cp;
  private String tipo;

  private DireccionDAO direccionDao;

  // Construyendo...

  /**
   *
   */
    public DireccionBean() {
    direccion = new Direccion();
    direccionDao = new DireccionIMPL();
  }
  
  public boolean editar(Direccion dir) {
    FacesContext context = FacesContext.getCurrentInstance();
    boolean ok = false;

    ok = direccionDao.editar(dir);
    if (ok) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
              "Operación Exitosa",
              "Se modificó el registro seleccionado"));
    } else {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "Operación Exitosa",
              "No se pudo editar el registro seleccionado."));
    }

    return ok;
  }

  public boolean eliminar(Direccion dir) {
    FacesContext context = FacesContext.getCurrentInstance();
    boolean ok = false;

    ok = direccionDao.eliminar(dir);

    if (ok) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
              "Operación Exitosa",
              "Se eliminó el registro seleccionado"));
    } else {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "Operación Exitosa",
              "No se pudo eliminar el registro seleccionado."));
    }

    return ok;
  }

  /**
   *
   * @param sujeto
   */
  public void agregar(Sujeto sujeto) {
    direccion.setCalle(calle);
//    direccion.setColonia(colonia);
//    direccion.setMunicipio(municipio);
//    direccion.setEstado(estado);
//    direccion.setCodigoPostal(cp);
    direccion.setTipo(tipo);
    direccion.setSujeto(sujeto);

    if (direccionDao.insertar(direccion)) {
      FacesContext context = FacesContext.getCurrentInstance();
      context.addMessage(null, new FacesMessage("Operación Exitosa",
              "Se agregó una nueva Dirección: " + calle + "... para: "
              + " " + sujeto.getNombreRazonSocial()));
      limpiarEntradas();
      Logs.log.info("Se agregó objeto: Direccion");
    } else {
      Logs.log.error("No se pudo agregar objeto: Direccion.");
    }
  }

  /**
   *
   */
  public void limpiarEntradas() {
    calle = null;
    colonia = null;
    municipio = null;
    estado = null;
    cp = null;
    tipo = null;
  }

  /**
   *
   * @return
   */
  public String getCalle() {
    return calle;
  }

  /**
   *
   * @param calle
   */
  public void setCalle(String calle) {
    this.calle = calle;
  }

  /**
   *
   * @return
   */
  public String getColonia() {
    return colonia;
  }

  /**
   *
   * @param colonia
   */
  public void setColonia(String colonia) {
    this.colonia = colonia;
  }

  /**
   *
   * @return
   */
  public String getMunicipio() {
    return municipio;
  }

  /**
   *
   * @param municipio
   */
  public void setMunicipio(String municipio) {
    this.municipio = municipio;
  }

  /**
   *
   * @return
   */
  public String getEstado() {
    return estado;
  }

  /**
   *
   * @param estado
   */
  public void setEstado(String estado) {
    this.estado = estado;
  }

  /**
   *
   * @return
   */
  public String getCp() {
    return cp;
  }

  /**
   *
   * @param cp
   */
  public void setCp(String cp) {
    this.cp = cp;
  }

  /**
   *
   * @return
   */
  public String getTipo() {
    return tipo;
  }

  /**
   *
   * @param tipo
   */
  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

}
