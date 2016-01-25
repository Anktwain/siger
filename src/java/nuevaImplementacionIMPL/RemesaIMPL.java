package nuevaImplementacionIMPL;

import dto.Remesa;
import java.util.List;
import nuevaImplementacionDAO.RemesaDAO;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public class RemesaIMPL implements RemesaDAO {

  @Override
  public boolean insert(Session session, Remesa remesa) throws Exception {
    session.save(remesa);
    return true;
  }

  @Override
  public Remesa getById(Session session, int idRemesa) throws Exception {
    return (Remesa) session.get(Remesa.class, idRemesa);
  }

  @Override
  public List<Remesa> getAll(Session session) throws Exception {
    String hql = "from Remesa";
    Query query = session.createQuery(hql);
    
    List<Remesa> listaRemesa = query.list();
    return listaRemesa;
  }

  @Override
  public boolean update(Session session, Remesa remesa) throws Exception {
    session.update(remesa);
    return true;
  }
  
}
