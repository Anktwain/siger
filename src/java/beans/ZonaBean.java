/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.ZonaDAO;
import dto.Colonia;
import dto.Despacho;
import dto.EstadoRepublica;
import dto.Gestor;
import dto.Municipio;
import dto.Zona;
import impl.ZonaIMPL;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author brionvega
 */
@ManagedBean
@ViewScoped
public class ZonaBean implements Serializable {
  // Objeto gestionado por esta bean:
  private Zona zona;
  
  // Atributos del objeto gestionado:
  private String nombreZona;
  private Despacho despacho;
  private Gestor gestor;
  
  // Acceso a la BD:
  private ZonaDAO zonaDao;
  
  // Otros beans:
  @ManagedProperty(value = "#{regionBean}")
  private RegionBean regionBean;
  
  // Construyendo...
  public ZonaBean() {
    zona = new Zona();
    zonaDao = new ZonaIMPL();
  }
  
  // Setters y Getters
  public Zona getZona() {
    return zona;
  }

  public void setZona(Zona zona) {
    this.zona = zona;
  }

  public String getNombreZona() {
    return nombreZona;
  }

  public void setNombreZona(String nombreZona) {
    this.nombreZona = nombreZona;
  }

  public Despacho getDespacho() {
    return despacho;
  }

  public void setDespacho(Despacho despacho) {
    this.despacho = despacho;
  }

  public Gestor getGestor() {
    return gestor;
  }

  public void setGestor(Gestor gestor) {
    this.gestor = gestor;
  }

  public RegionBean getRegionBean() {
    return regionBean;
  }

  public void setRegionBean(RegionBean regionBean) {
    this.regionBean = regionBean;
  }
  
  
}
