package nuevaImplementacionIMPL;

import dto.Despacho;
import java.util.List;
import nuevaImplementacionDAO.DespachoDAO;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public class DespachoIMPL implements DespachoDAO {

  @Override
  public boolean insert(Session session, Despacho despacho) throws Exception {
    session.save(despacho);
    
    return true;
  }

  @Override
  public Despacho getById(Session session, int idDespacho) throws Exception {
    return (Despacho) session.get(Despacho.class, idDespacho);
  }

  @Override
  public List<Despacho> getAll(Session session) throws Exception {
    String hql = "from Despacho";
    Query query = session.createQuery(hql);
    
    List<Despacho> listaDespacho = (List<Despacho>) query.list();
    return listaDespacho;
  }

  @Override
  public boolean update(Session session, Despacho despacho) throws Exception {
    session.update(despacho);
    
    return true;
  }
  
}
