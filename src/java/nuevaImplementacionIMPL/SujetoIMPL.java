package nuevaImplementacionIMPL;

import dto.Sujeto;
import java.util.List;
import nuevaImplementacionDAO.SujetoDAO;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public class SujetoIMPL implements SujetoDAO {

  @Override
  public Sujeto insert(Session session, Sujeto sujeto) throws Exception {
    session.save(sujeto);
    
    return sujeto;
  }

  @Override
  public Sujeto getById(Session session, int idSujeto) throws Exception {
    return (Sujeto) session.get(Sujeto.class, idSujeto);
  }

  @Override
  public List<Sujeto> getAll(Session session) throws Exception {
    String hql = "from Sujeto";
    Query query = session.createQuery(hql);
    
    List<Sujeto> listaSujetos = (List<Sujeto>) query.list();
    return listaSujetos;
  }

  @Override
  public boolean update(Session session, Sujeto sujeto) throws Exception {
    session.update(sujeto);
    return true;
  }
  
}
