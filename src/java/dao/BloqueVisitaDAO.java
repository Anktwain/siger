/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.BloqueVisitas;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface BloqueVisitaDAO {
  
  public boolean insertar(BloqueVisitas bloque);
  
  public boolean editar(BloqueVisitas bloque);
  
  public List<BloqueVisitas> buscarPorGestor(int idGestor);
  
  public List<BloqueVisitas> buscarPorFechaAsignada(Date fechaAsignada);
  
}
