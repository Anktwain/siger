package nuevaImplementacionDAO;

import dto.Credito;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public interface CreditoDAO {
  public boolean insert(Session session, Credito credito) throws Exception;
  public Credito getById(Session session, int idCredito) throws Exception;
  public Credito getByNumeroCredito(Session session, String credito) throws Exception;
  public List<Credito> getAll(Session session) throws Exception;
  public List<String> getAllNoCredito(Session session) throws Exception;
  public boolean update(Session session, Credito credito) throws Exception;
}
