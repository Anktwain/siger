/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.DireccionTexto;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface DireccionTextoDAO {

  public boolean insertar(DireccionTexto direccion);

  public boolean editar(DireccionTexto direccion);
  
  public boolean eliminar(DireccionTexto direccion);

  public List<DireccionTexto> buscarPorCredito(String numeroCredito);
  
  public List<DireccionTexto> buscarTodas();
  
  public List<DireccionTexto> buscarSinValidar();
  
}
