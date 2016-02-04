package nuevaImplementacionIMPL;

import dto.Deudor;
import java.util.List;
import nuevaImplementacionDAO.DeudorDAO;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public class DeudorIMPL implements DeudorDAO {

  @Override
  public Deudor insert(Session session, Deudor deudor) throws Exception {
    session.save(deudor);
    
    return deudor;
  }

  @Override
  public Deudor getById(Session session, int idDeudor) throws Exception {
    return (Deudor) session.get(Deudor.class, idDeudor);
  }

  @Override
  public Deudor getByNumeroDeudor(Session session, String numeroDeudor) throws Exception {
    String hql = "from Deudor where numeroDeudor=:numeroDeudor";
    Query query = session.createQuery(hql);
    query.setParameter("numeroDeudor", numeroDeudor);
    
    Deudor deudor = (Deudor) query.uniqueResult();
    
    return deudor;
  }

  @Override
  public List<Deudor> getAll(Session session) throws Exception {
    String hql = "from Deudor";
    Query query = session.createQuery(hql);
    
    List<Deudor> listaDeudor = (List<Deudor>) query.list();
    return listaDeudor;
  }

  @Override
  public List<String> getAllNoDeudor(Session session) throws Exception {
    String hql = "select numeroDeudor from Deudor";
    Query query = session.createQuery(hql);
    
    List<String> listaDeNumerosDeDeudor = (List<String>) query.list();
    return listaDeNumerosDeDeudor;
  }
  
  

  @Override
  public boolean update(Session session, Deudor deudor) throws Exception {
    session.update(deudor);
    
    return true;
  }
  
}