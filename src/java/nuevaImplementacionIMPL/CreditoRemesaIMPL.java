package nuevaImplementacionIMPL;

import dto.CreditoRemesa;
import nuevaImplementacionDAO.CreditoRemesaDAO;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public class CreditoRemesaIMPL implements CreditoRemesaDAO {

  @Override
  public boolean insert(Session session, CreditoRemesa creditoRemesa) throws Exception {
    session.save(creditoRemesa);
    
    return true;
  }
  
}
