package nuevaImplementacionIMPL;

import dto.Fac;
import java.util.List;
import nuevaImplementacionDAO.FacDAO;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public class FacIMPL implements FacDAO {

  @Override
  public boolean insert(Session session, Fac fac) throws Exception {
    session.save(fac);
    
    return true;
  }

  @Override
  public Fac getById(Session session, int idFac) throws Exception {
    return (Fac) session.get(Fac.class, idFac);
  }

  @Override
  public List<Fac> getAll(Session session) throws Exception {
    String hql = "from Fac";
    Query query = session.createQuery(hql);
    
    List<Fac> listaFac = (List<Fac>) query.list();
    return listaFac;
  }

  @Override
  public boolean update(Session session, Fac fac) throws Exception {
    session.update(fac);
    
    return true;
  }
  
}
