package nuevaImplementacionIMPL;

import dto.Credito;
import java.util.List;
import nuevaImplementacionDAO.CreditoDAO;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public class CreditoIMPL implements CreditoDAO {

  @Override
  public boolean insert(Session session, Credito credito) throws Exception {
    session.save(credito);
    
    return true;
  }

  @Override
  public Credito getById(Session session, int idCredito) throws Exception {
    return (Credito) session.get(Credito.class, idCredito);
  }

  @Override
  public Credito getByNumeroCredito(Session session, String credito) throws Exception {
    String hql = "from Credito where numeroCredito=:credito";
    Query query = session.createQuery(hql);
    query.setParameter("credito", credito);
    
    Credito creditoDTO = (Credito) query.uniqueResult();
    
    return creditoDTO;
  }
  
  @Override
  public List<Credito> getAll(Session session) throws Exception {
    String hql = "from Credito";
    Query query = session.createQuery(hql);
    
    List<Credito> listaCredito = (List<Credito>) query.list();
    return listaCredito;
  }

  @Override
  public boolean update(Session session, Credito credito) throws Exception {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
}
