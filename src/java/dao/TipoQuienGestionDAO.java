/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.TipoQuienGestion;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface TipoQuienGestionDAO {
  public List<TipoQuienGestion> buscarTodo();
  public List<TipoQuienGestion> buscarPorAsuntoDescripcion(int claveAsunto, String abreviatura);
  public TipoQuienGestion buscarPorId(int idTipoQuienGestion);
}
