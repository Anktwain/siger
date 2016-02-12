package nuevaImplementacionDAO;

import dto.Campana;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public interface CampanaDAO {
  public boolean insert(Session session, Campana campana) throws Exception;
  public Campana getById(Session session, int idCampana) throws Exception;
  public List<Campana> getAll(Session session) throws Exception;
}
