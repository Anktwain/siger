/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.QuienGestion;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface QuienGestionDAO {
  public List<QuienGestion> buscarTodo();
  public List<QuienGestion> buscarPorTipoQuien(int idTipoQuien);
  public QuienGestion buscarPorId(int idQuienGestion);
}
