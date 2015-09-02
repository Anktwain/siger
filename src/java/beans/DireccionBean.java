/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.ColoniaDAO;
import dao.DireccionDAO;
import dao.EstadoRepublicaDAO;
import dao.MunicipioDAO;
import dto.Colonia;
import dto.Direccion;
import dto.EstadoRepublica;
import dto.Municipio;
import dto.Sujeto;
import impl.ColoniaIMPL;
import impl.DireccionIMPL;
import impl.EstadoRepublicaIMPL;
import impl.MunicipioIMPL;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import util.log.Logs;

/**
 *
 * @author brionvega
 */
@ManagedBean
@ViewScoped
public class DireccionBean implements Serializable {

  // Objeto que gestiona este bean
  private Direccion direccion;
  
  // Atributos del objeto Direccion
  private String tipo;
  private String calle;
  private Colonia colonia;
  private Municipio municipio;
  private EstadoRepublica estado;
  
  // Acceso a la BD
  private DireccionDAO direccionDao;
  private ColoniaDAO coloniaDao;
  private MunicipioDAO municipioDao;
  private EstadoRepublicaDAO estadoDao;
  
  // Listas para construir direcciones
  private List<EstadoRepublica> estados;
  private List<Municipio> municipos;
  private List<Colonia> colonias;
  
  // Construyendo...
  public DireccionBean() {
    direccion = new Direccion();
    direccionDao = new DireccionIMPL();
    colonia = new Colonia();
    coloniaDao = new ColoniaIMPL();
    municipio = new Municipio();
    municipioDao = new MunicipioIMPL();
    estado = new EstadoRepublica();
    estadoDao = new EstadoRepublicaIMPL();
  }
  
  // GESTIÓN DE DIRECCIONES
  public Direccion insertar(Sujeto sujeto) {
    // Verfica que el sujeto sea válido
    if (sujeto.getIdSujeto() == null) {
      Logs.log.error("El método DireccionBean.insertar(sujeto) recibe un sujeto null");
      return null;
    } else {
      // Crea el objeto Telefono
      direccion.setCalle(calle);
      direccion.setColonia(colonia);
      direccion.setEstadoRepublica(estado);
      direccion.setMunicipio(municipio);
      direccion.setTipo(tipo);
      direccion.setSujeto(sujeto);

      return direccionDao.insertar(direccion);
  }
  }
  
  // AUXILIARES
  public void listarEstados() {
    estados = estadoDao.buscarTodo();
  }
  
  public void listarMunicipiosPorEstado() {
    estado = estadoDao.buscar(estado.getIdEstado());
    municipos = municipioDao.buscarMunicipiosPorEstado(estado.getIdEstado());
    System.out.println(estado.getNombre());
  }
  
  public void listarColoniasPorMunicipio() {
    municipio = municipioDao.buscar(municipio.getIdMunicipio());
    colonias = coloniaDao.buscarColoniasPorMunicipio(municipio.getIdMunicipio());
  }
  
  public void agregarCP() {
    colonia = coloniaDao.buscar(colonia.getIdColonia());
  }
  
  public void resetAtributos() {
    tipo = null;
    calle = null;
    colonia.setIdColonia(null);
    municipio.setIdMunicipio(null);
    estado.setIdEstado(null);
  }
  
  // SETTERS & GETTERS
  public Direccion getDireccion() {
    return direccion;
  }

  public void setDireccion(Direccion direccion) {
    this.direccion = direccion;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getCalle() {
    return calle;
  }

  public void setCalle(String calle) {
    this.calle = calle;
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

  public List<EstadoRepublica> getEstados() {
    return estados;
  }

  public void setEstados(List<EstadoRepublica> estados) {
    this.estados = estados;
  }

  public List<Municipio> getMunicipos() {
    return municipos;
  }

  public void setMunicipos(List<Municipio> municipos) {
    this.municipos = municipos;
  }

  public List<Colonia> getColonias() {
    return colonias;
  }

  public void setColonias(List<Colonia> colonias) {
    this.colonias = colonias;
  }
  
  public DireccionDAO getDireccionDao() {
    return direccionDao;
  }

  public void setDireccionDao(DireccionDAO direccionDao) {
    this.direccionDao = direccionDao;
  }

  public ColoniaDAO getColoniaDao() {
    return coloniaDao;
  }

  public void setColoniaDao(ColoniaDAO coloniaDao) {
    this.coloniaDao = coloniaDao;
  }

  public MunicipioDAO getMunicipioDao() {
    return municipioDao;
  }

  public void setMunicipioDao(MunicipioDAO municipioDao) {
    this.municipioDao = municipioDao;
  }

  public EstadoRepublicaDAO getEstadoDao() {
    return estadoDao;
  }

  public void setEstadoDao(EstadoRepublicaDAO estadoDao) {
    this.estadoDao = estadoDao;
  }
  
}