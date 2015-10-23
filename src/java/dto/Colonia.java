package dto;
// Generated 19/10/2015 12:46:44 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

/**
 * Colonia generated by hbm2java
 */
public class Colonia implements java.io.Serializable {

  private Integer idColonia;
  private Municipio municipio;
  private String tipo;
  private String nombre;
  private String codigoPostal;
  private Set direccions = new HashSet(0);
  private Set regions = new HashSet(0);

  public Colonia() {
  }

  public Colonia(Municipio municipio, String nombre, String codigoPostal) {
    this.municipio = municipio;
    this.nombre = nombre;
    this.codigoPostal = codigoPostal;
  }

  public Colonia(Municipio municipio, String tipo, String nombre, String codigoPostal, Set direccions, Set regions) {
    this.municipio = municipio;
    this.tipo = tipo;
    this.nombre = nombre;
    this.codigoPostal = codigoPostal;
    this.direccions = direccions;
    this.regions = regions;
  }

  @Override
  public String toString() {
    return nombre;
  }

  public Integer getIdColonia() {
    return this.idColonia;
  }

  public void setIdColonia(Integer idColonia) {
    this.idColonia = idColonia;
  }

  public Municipio getMunicipio() {
    return this.municipio;
  }

  public void setMunicipio(Municipio municipio) {
    this.municipio = municipio;
  }

  public String getTipo() {
    return this.tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getNombre() {
    return this.nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getCodigoPostal() {
    return this.codigoPostal;
  }

  public void setCodigoPostal(String codigoPostal) {
    this.codigoPostal = codigoPostal;
  }

  public Set getDireccions() {
    return this.direccions;
  }

  public void setDireccions(Set direccions) {
    this.direccions = direccions;
  }

  public Set getRegions() {
    return this.regions;
  }

  public void setRegions(Set regions) {
    this.regions = regions;
  }

}
