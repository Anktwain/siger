package nuevaImplementacionDAO;

import dto.CreditoRemesa;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public interface CreditoRemesaDAO {
  public boolean insert(Session session, CreditoRemesa creditoRemesa) throws Exception;
}
