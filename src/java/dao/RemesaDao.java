package dao;

import dto.Remesa;

/**
 *
 * @author brionvega
 */
public interface RemesaDao {
  public int buscarRemesaActual();
  public Remesa insertar(Remesa remesa);
}
