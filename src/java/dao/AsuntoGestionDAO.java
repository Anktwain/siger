/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.AsuntoGestion;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface AsuntoGestionDAO {
  
  public List<AsuntoGestion> buscarTodo();
  
  public List<AsuntoGestion> buscarPorTipoGestion(int idTipoGestion);
  
  public AsuntoGestion buscarPorId(int idAsunto);
  
}
