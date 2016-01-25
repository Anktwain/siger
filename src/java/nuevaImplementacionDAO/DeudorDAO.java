package nuevaImplementacionDAO;

import dto.Deudor;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public interface DeudorDAO {
  public Deudor insert(Session session, Deudor deudor) throws Exception;
  public Deudor getById(Session session, int idDeudor) throws Exception;
  public Deudor getByNumeroDeudor(Session session, String numeroDeudor) throws Exception;
  public List<Deudor> getAll(Session session) throws Exception;
  public boolean update(Session session, Deudor deudor) throws Exception;
}