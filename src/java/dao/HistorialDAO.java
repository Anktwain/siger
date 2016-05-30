/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Historial;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface HistorialDAO {
  
  public boolean insertar(Historial historial);
  
  public List<Historial> buscarPorCredito(int idCredito);
  
  public boolean verificarCampioCampañaCredito(int idCredito);
    
}
