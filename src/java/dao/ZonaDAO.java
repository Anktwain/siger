
package dao;

import dto.Zona;
import java.util.List;

/**
 *
 * @author Pablo
 */
public interface ZonaDAO {
  public Zona insertar(Zona zona);
  public List<Zona> buscarPorDespacho(int idDespacho);
  public List<String> buscarNombresZonas(int idDespacho);
  public List<Integer> buscarIdsGestoresZonas();
}
