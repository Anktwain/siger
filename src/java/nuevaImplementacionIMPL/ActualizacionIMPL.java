package nuevaImplementacionIMPL;

import dto.Actualizacion;
import dto.Credito;
import java.util.List;
import nuevaImplementacionDAO.ActualizacionDAO;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public class ActualizacionIMPL implements ActualizacionDAO {

  @Override
  public boolean insert(Session session, Actualizacion actualizacion) throws Exception {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public int getMaxId(Session session, Credito credito) throws Exception {
    String hql = "select max(idActualizacion) from Actualizacion where credito=:credito";
    Query query = session.createQuery(hql);
    query.setParameter("credito", credito);
    
    int maxId = (int) query.uniqueResult();
    return maxId;
  }

  @Override
  public Actualizacion getById(Session session, int idActualizacion) throws Exception {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List<Actualizacion> getAll(Session session) throws Exception {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean update(Session session, Actualizacion actualizacion) throws Exception {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
}
