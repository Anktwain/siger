package nuevaImplementacionIMPL;

import dto.Campana;
import java.util.List;
import nuevaImplementacionDAO.CampanaDAO;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public class CampanaIMPL implements CampanaDAO {

  @Override
  public boolean insert(Session session, Campana campana) throws Exception {
    session.save(campana);
    
    return true;
  }

  @Override
  public Campana getById(Session session, int idCampana) throws Exception {
    return (Campana) session.get(Campana.class, idCampana);
  }

  @Override
  public List<Campana> getAll(Session session) throws Exception {
    String hql = "from Campana";
    Query query = session.createQuery(hql);
    
    List<Campana> listaCampanas = (List<Campana>) query.list();
    return listaCampanas;
  }
  
}
