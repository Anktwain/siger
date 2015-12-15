/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.tablas.Devolucions;
import dto.Devolucion;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface DevolucionDAO {
  
  public List<Devolucions> retiradosBancoPorDespacho(int idDespacho);
  
  public List<Devolucions> bandejaDevolucionPorDespacho(int idDespacho);
  
  public List<Devolucions> devueltosPorDespacho(int idDespacho);
  
  public boolean insertar(Devolucion devolucion);
  
  public boolean editar(Devolucion devolucion);
  
  public boolean eliminar(Devolucion devolucion);
  
  public Devolucion buscarDevolucionPorNumeroCredito(String numeroCredito);
  
}
