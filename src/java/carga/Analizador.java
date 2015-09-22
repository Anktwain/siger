/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carga;

import dao.ColoniaDAO;
import dao.EstadoRepublicaDAO;
import dao.MunicipioDAO;
import dto.Colonia;
import dto.EstadoRepublica;
import dto.Fila;
import dto.Municipio;
import impl.ColoniaIMPL;
import impl.EstadoRepublicaIMPL;
import impl.MunicipioIMPL;
import util.log.Logs;

/**
 *
 * @author brionvega
 */
public class Analizador {
  private EstadoRepublicaDAO estadoDao = new EstadoRepublicaIMPL();
  private MunicipioDAO municipioDao = new MunicipioIMPL();
  private ColoniaDAO coloniaDao = new ColoniaIMPL();
  
  private EstadoRepublica estado = new EstadoRepublica();
  private Municipio municipio = new Municipio();
  private Colonia colonia = new Colonia();
  
  public boolean analizarDireccion(Fila fila) {
    String edo = fila.getEstado();
    String mpio = fila.getMunicipio();
    String col = fila.getColonia();
    String calle = fila.getCalle();
    String cp = fila.getCp();
    
    Logs.log.info("Validando dirección: " + calle + " " + col + " " + mpio + " " + edo + " " + cp);
    
    estado = estadoDao.buscar(edo);
    if(estado != null) { // El estado existe?
      municipio = municipioDao.buscar(mpio);
      if(municipio != null) { // El municipio existe?
        colonia = coloniaDao.buscar(col, cp);
        if(colonia != null) { // La colonia existe?
          Logs.log.info("La dirección es válida");
          return true;
        } else {
          Logs.log.error("No se encontró la colonia: " + col);
          return false;
        }
      } else {
        Logs.log.error("No se encontró el municipio: " + mpio);
        return false;
      }
    } else {
      Logs.log.error("No se encontró el estado: " + edo);
      return false;
    }
    
  }
}
