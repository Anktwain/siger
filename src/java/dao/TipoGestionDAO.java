/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.TipoGestion;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface TipoGestionDAO {
  public List<TipoGestion> buscarTodo();
  public TipoGestion buscarPorId(int idTipoGestion);
}
