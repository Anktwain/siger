/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.ColoniaDAO;
import dao.EstadoRepublicaDAO;
import dao.MunicipioDAO;
import dao.RegionDAO;
import dao.ZonaDAO;
import dto.Colonia;
import dto.EstadoRepublica;
import dto.Municipio;
import dto.Region;
import dto.Zona;
import impl.ColoniaIMPL;
import impl.EstadoRepublicaIMPL;
import impl.MunicipioIMPL;
import impl.RegionIMPL;
import impl.ZonaIMPL;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author brionvega
 */
@ManagedBean
@ViewScoped
public class RegionBean implements Serializable {
  // Objeto gestionado por este bean
  private Region region;
  
  // Atributos de Region:
  private Colonia colonia;
  private Municipio municipio;
  private EstadoRepublica estado;
  private Zona zona;
  
  // Acceso a la BD
  private RegionDAO regionDao;
  private ColoniaDAO coloniaDao;
  private MunicipioDAO municipioDao;
  private EstadoRepublicaDAO estadoDao;
  private ZonaDAO zonaDao;
  
  // Lista para construir regiones
  private List<EstadoRepublica> estados;
  private List<Municipio> municipios;
  private List<Colonia> colonias;
  
  
  // Construyendo...
  public RegionBean() {
    regionDao = new RegionIMPL();
    coloniaDao = new ColoniaIMPL();
    municipioDao = new MunicipioIMPL();
    estadoDao = new EstadoRepublicaIMPL();
    zonaDao = new ZonaIMPL();
  }
  
  // AUXILIARES
  public void listarEstados() {
    System.out.println("Hola: Listando todos los estados...");
    estados = estadoDao.buscarTodo();
    System.out.println("Terminó de listar");
  }
  
  // Setter & Getter
  public Region getRegion() {
    return region;
  }

  public void setRegion(Region region) {
    this.region = region;
  }

  public Colonia getColonia() {
    return colonia;
  }

  public void setColonia(Colonia colonia) {
    this.colonia = colonia;
  }

  public Municipio getMunicipio() {
    return municipio;
  }

  public void setMunicipio(Municipio municipio) {
    this.municipio = municipio;
  }

  public EstadoRepublica getEstado() {
    return estado;
  }

  public void setEstado(EstadoRepublica estado) {
    this.estado = estado;
  }

  public Zona getZona() {
    return zona;
  }

  public void setZona(Zona zona) {
    this.zona = zona;
  }

  public List<EstadoRepublica> getEstados() {
    return estados;
  }

  public void setEstados(List<EstadoRepublica> estados) {
    this.estados = estados;
  }

  public List<Municipio> getMunicipios() {
    return municipios;
  }

  public void setMunicipios(List<Municipio> municipios) {
    this.municipios = municipios;
  }

  public List<Colonia> getColonias() {
    return colonias;
  }

  public void setColonias(List<Colonia> colonias) {
    this.colonias = colonias;
  }
  
  
}
