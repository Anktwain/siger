package dao;

import dto.Despacho;

/**
 *
 * @author brionvega
 */
public interface DespachoDAO {
  public Despacho buscar(String nombreCorto);
}
