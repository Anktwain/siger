package nuevaImplementacionDAO;

import dto.Sujeto;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public interface SujetoDAO {
  public Sujeto insert(Session session, Sujeto sujeto) throws Exception;
  public Sujeto getById(Session session, int idSujeto) throws Exception;
  public List<Sujeto> getAll(Session session) throws Exception;
  public boolean update(Session session, Sujeto sujeto) throws Exception;
}
