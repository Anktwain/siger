package nuevaImplementacionDAO;

import dto.Actualizacion;
import dto.Credito;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public interface ActualizacionDAO {
  public boolean insert(Session session, Actualizacion actualizacion) throws Exception;
  public int getMaxId(Session session, Credito credito) throws Exception;
  public Actualizacion getById(Session session, int idActualizacion) throws Exception;
  public List<Actualizacion> getAll(Session session) throws Exception;
  public boolean update(Session session, Actualizacion actualizacion) throws Exception;
}
