package nuevaImplementacionDAO;

import dto.Fac;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public interface FacDAO {
  public boolean insert(Session session, Fac fac) throws Exception;
  public Fac getById(Session session, int idFac) throws Exception;
  public List<Fac> getAll(Session session) throws Exception;
  public boolean update(Session session, Fac fac) throws Exception;
}
