package nuevaImplementacionDAO;

import dto.Remesa;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public interface RemesaDAO {
  public boolean insert(Session session, Remesa remesa) throws Exception;
  public Remesa getById(Session session, int idRemesa) throws Exception;
  public Remesa getUltimaRemesa(Session session) throws Exception;
  public List<Remesa> getAll(Session session) throws Exception;
  public boolean update(Session session, Remesa remesa) throws Exception;
}
