/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Ajuste;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface AjusteDAO {
  
  public boolean insertar(Ajuste ajuste);
  
  public List<Ajuste> buscarAjustesPorCredito(int idCredito);
  
}
