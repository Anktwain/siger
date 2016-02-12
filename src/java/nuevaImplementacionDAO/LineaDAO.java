package nuevaImplementacionDAO;

import dto.Linea;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public interface LineaDAO {
  public boolean insert(Session session, Linea linea) throws Exception;
}
