package nuevaImplementacionDAO;

import dto.Direccion;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public interface DireccionDAO {
  public Direccion insert(Session session, Direccion direccion) throws Exception;
  public Direccion getById(Session session, int idDireccion) throws Exception;
  public List<Direccion> getAll(Session session) throws Exception;
  public boolean update(Session session, Direccion direccion) throws Exception;
}
