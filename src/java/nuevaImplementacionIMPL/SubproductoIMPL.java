package nuevaImplementacionIMPL;

import dto.Subproducto;
import java.util.List;
import nuevaImplementacionDAO.SubproductoDAO;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public class SubproductoIMPL implements SubproductoDAO {

  @Override
  public boolean insert(Session session, Subproducto subproducto) throws Exception {
    session.save(subproducto);
    
    return true;
  }

  @Override
  public Subproducto getById(Session session, int idSubproducto) throws Exception {
    return (Subproducto) session.get(Subproducto.class, idSubproducto);
  }

  @Override
  public List<Subproducto> getAll(Session session) throws Exception {
    String hql = "from Subproducto";
    Query query = session.createQuery(hql);
    
    List<Subproducto> listaSubproducto = (List<Subproducto>) query.list();
    return listaSubproducto;
  }

  @Override
  public boolean update(Session session, Subproducto subproducto) throws Exception {
    session.update(subproducto);
    
    return true;
  }
  
}
