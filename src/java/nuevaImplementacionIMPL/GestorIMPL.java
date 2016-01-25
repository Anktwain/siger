package nuevaImplementacionIMPL;

import dto.Gestor;
import java.util.List;
import nuevaImplementacionDAO.GestorDAO;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public class GestorIMPL implements GestorDAO {

  @Override
  public boolean insert(Session session, Gestor gestor) throws Exception {
    session.save(gestor);
    
    return true;
  }

  @Override
  public Gestor getById(Session sesion, int idGEstor) throws Exception {
    return (Gestor) sesion.get(Gestor.class, idGEstor);
  }

  @Override
  public List<Gestor> getAll(Session session) throws Exception {
    String hql = "from Gestor";
    Query query = session.createQuery(hql);
    
    List<Gestor> listaGestor = (List<Gestor>) query.list();
    return listaGestor;
  }

  @Override
  public boolean update(Session session, Gestor gestor) throws Exception {
    session.update(gestor);
    
    return true;
  }
  
}