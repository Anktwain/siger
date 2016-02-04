package nuevaImplementacionDAO;

import dto.Institucion;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public interface InstitucionDAO {

  public Institucion insert(Session session, Institucion institucion) throws Exception;
  public Institucion getById(Session session, int idInstitucion) throws Exception;
  public List<Institucion> getAll(Session session) throws Exception;
  public boolean update(Session session, Institucion institucion) throws Exception;
}
