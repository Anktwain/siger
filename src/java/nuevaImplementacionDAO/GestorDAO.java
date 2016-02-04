package nuevaImplementacionDAO;

import dto.Gestor;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public interface GestorDAO {
  public boolean insert(Session session, Gestor gestor) throws Exception;
  public Gestor getById(Session sesion, int idGEstor) throws Exception;
  public List<Gestor> getAll(Session session) throws Exception;
  public boolean update(Session session, Gestor gestor) throws Exception;
}
