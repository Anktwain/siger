package dao;

import dto.Despacho;
import java.util.List;

/**
 *
 * @author brionvega
 */
public interface DespachoDAO {
  public Despacho buscar(String nombreCorto);
  public List<Despacho> getAll();
}
