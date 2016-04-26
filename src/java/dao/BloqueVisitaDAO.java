/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.BloqueVisita;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface BloqueVisitaDAO {
  
  public boolean insertar(BloqueVisita bloque);
  
  public boolean editar(BloqueVisita bloque);
  
  public List<BloqueVisita> buscarPorGestor(int idGestor);
  
  public List<BloqueVisita> buscarPorFechaAsignada(Date fechaAsignada);
  
}
