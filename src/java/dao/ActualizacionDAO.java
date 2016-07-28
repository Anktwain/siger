/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Actualizacion;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface ActualizacionDAO {
  
  public boolean insertar(Actualizacion actualizacion);
  
  public List<Actualizacion> buscarPorCredito(int idCredito);
  
  public Actualizacion buscarUltimaActualizacion(int idCredito);
  
  public boolean editar(Actualizacion actualizacion);
  
  public float obtenerMontoPrometidoGestor(int idGestor);
    
}
