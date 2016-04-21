/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.DetalleVisita;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface DetalleVisitaDAO {
  
  public boolean insertar(DetalleVisita detalle);
  
  public boolean editar(DetalleVisita detalle);
  
  public List<DetalleVisita> buscarPorBloque(int idBloque);
  
}
