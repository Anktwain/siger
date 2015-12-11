package dto;
// Generated 2/12/2015 01:29:58 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

/**
 * EstadoRepublica generated by hbm2java
 */
public class EstadoRepublica implements java.io.Serializable {

  private Integer idEstado;
  private String nombre;
  private Set municipios = new HashSet(0);
  private Set regions = new HashSet(0);
  private Set direccions = new HashSet(0);

  public EstadoRepublica() {
  }

  public EstadoRepublica(String nombre) {
    this.nombre = nombre;
  }

  public EstadoRepublica(String nombre, Set municipios, Set regions, Set direccions) {
    this.nombre = nombre;
    this.municipios = municipios;
    this.regions = regions;
    this.direccions = direccions;
  }

  public Integer getIdEstado() {
    return this.idEstado;
  }

  public void setIdEstado(Integer idEstado) {
    this.idEstado = idEstado;
  }

  public String getNombre() {
    return this.nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public Set getMunicipios() {
    return this.municipios;
  }

  public void setMunicipios(Set municipios) {
    this.municipios = municipios;
  }

  public Set getRegions() {
    return this.regions;
  }

  public void setRegions(Set regions) {
    this.regions = regions;
  }

  public Set getDireccions() {
    return this.direccions;
  }

  public void setDireccions(Set direccions) {
    this.direccions = direccions;
  }

  @Override
  public String toString() {
    return nombre;
  }

}
