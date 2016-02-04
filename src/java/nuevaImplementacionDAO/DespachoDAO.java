package nuevaImplementacionDAO;

import dto.Despacho;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public interface DespachoDAO {
  public boolean insert(Session session, Despacho despacho) throws Exception;
  public Despacho getById(Session session, int idDespacho) throws Exception;
  public List<Despacho> getAll(Session session) throws Exception;
  public boolean update(Session session, Despacho despacho) throws Exception;  
}
