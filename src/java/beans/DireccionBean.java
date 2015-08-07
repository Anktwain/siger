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
  public DireccionBean() {
    direccion = new Direccion();
    direccionDao = new DireccionIMPL();
  }

  public void agregar(Sujeto sujeto) {
    direccion.setCalle(calle);
    direccion.setColonia(colonia);
    direccion.setMunicipio(municipio);
    direccion.setEstado(estado);
    direccion.setCodigoPostal(cp);
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

  public void limpiarEntradas() {
    calle = null;
    colonia = null;
    municipio = null;
    estado = null;
    cp = null;
    tipo = null;
  }

  public String getCalle() {
    return calle;
  }

  public void setCalle(String calle) {
    this.calle = calle;
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

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

}
