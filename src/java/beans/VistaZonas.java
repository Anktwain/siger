/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dto.Region;
import dto.Zona;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author brionvega
 */
@ManagedBean
@ViewScoped
public class VistaZonas implements Serializable {
  private Zona zonaActual;
  private Region regionActual;
  
  // Otros beans
  @ManagedProperty(value = "#{zonaBean}")
  private ZonaBean zonaBean;
  @ManagedProperty(value = "#{regionBean}")
  private RegionBean regionBean;

  public VistaZonas() {
    zonaActual = new Zona();
    regionActual = new Region();
  }
  
  // Setter & Getter
  public Zona getZonaActual() {
    return zonaActual;
  }

  public void setZonaActual(Zona zonaActual) {
    this.zonaActual = zonaActual;
  }

  public Region getRegionActual() {
    return regionActual;
  }

  public void setRegionActual(Region regionActual) {
    this.regionActual = regionActual;
  }

  public ZonaBean getZonaBean() {
    return zonaBean;
  }

  public void setZonaBean(ZonaBean zonaBean) {
    this.zonaBean = zonaBean;
  }

  public RegionBean getRegionBean() {
    return regionBean;
  }

  public void setRegionBean(RegionBean regionBean) {
    this.regionBean = regionBean;
  }
  
}
