package nuevaImplementacionDAO;

import dto.Email;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public interface EmailDAO {
  public boolean insert(Session session, Email email) throws Exception;
  public Email getById(Session session, int idEmail) throws Exception;
  public List<Email> getAll(Session session) throws Exception;
  public boolean update(Session session, Email email) throws Exception;
}
