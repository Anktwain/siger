package nuevaImplementacionDAO;

import dto.Subproducto;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public interface SubproductoDAO {
  public boolean insert(Session session, Subproducto subproducto) throws Exception;
  public Subproducto getById(Session session, int idSubproducto) throws Exception;
  public List<Subproducto> getAll(Session session) throws Exception;
  public boolean update(Session session, Subproducto subproducto) throws Exception;
}
