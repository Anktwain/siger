package nuevaImplementacionDAO;

import dto.Telefono;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public interface TelefonoDAO {
  public boolean insert(Session session, Telefono telefono) throws Exception;
  public Telefono getById(Session session, int idTelefono) throws Exception;
  public List<Telefono> getAll(Session session) throws Exception;
  public boolean update(Session session, Telefono telefono) throws Exception;
}
