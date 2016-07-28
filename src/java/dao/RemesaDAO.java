package dao;

import dto.Remesa;

/**
 *
 * @author brionvega
 */
public interface RemesaDAO {
  
  public int buscarRemesaActual();
  
  public Remesa insertar(Remesa remesa);
  
  public Remesa obtenerUltimaRemesa();
  
}
