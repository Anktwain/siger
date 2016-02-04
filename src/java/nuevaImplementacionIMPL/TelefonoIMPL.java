package nuevaImplementacionIMPL;

import dto.Telefono;
import java.util.List;
import nuevaImplementacionDAO.TelefonoDAO;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public class TelefonoIMPL implements TelefonoDAO {

  @Override
  public boolean insert(Session session, Telefono telefono) throws Exception {
    session.save(telefono);
    
    return true;
  }

  @Override
  public Telefono getById(Session session, int idTelefono) throws Exception {
    return (Telefono) session.get(Telefono.class, idTelefono);
  }

  @Override
  public List<Telefono> getAll(Session session) throws Exception {
    String hql = "from Telefono";
    Query query = session.createQuery(hql);
    
    List<Telefono> listaTelefono = (List<Telefono>) query.list();
    return listaTelefono;
  }

  @Override
  public boolean update(Session session, Telefono telefono) throws Exception {
    session.update(telefono);
    
    return true;
  }
  
}
