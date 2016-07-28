/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Colonia;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface ColoniaDAO {
  
  public List<Colonia> buscarColoniasPorMunicipio(int idMunicipio);
  
  public Colonia buscar(int idColonia);
  
  public List<Colonia> buscar(String cadena);
  
  public Colonia buscar(String cadena, String cp);
  
  public List<Colonia> buscarPorCodigoPostal(String cp);
  
  public boolean insertar(Colonia colonia);
  
  public List<String> obtenerTiposColonia();
  
}