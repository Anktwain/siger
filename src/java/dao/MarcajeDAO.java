/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Marcaje;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface MarcajeDAO {
  
  public Marcaje buscarMarcajePorId(int idMarcaje);
  
  public List<Marcaje> buscarTodos();
  
}
