package nuevaImplementacionIMPL;

import dto.Direccion;
import java.util.List;
import nuevaImplementacionDAO.DireccionDAO;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public class DireccionIMPL implements DireccionDAO {

  @Override
  public Direccion insert(Session session, Direccion direccion) throws Exception {
    session.save(direccion);
    return direccion;
  }

  @Override
  public Direccion getById(Session session, int idDireccion) throws Exception {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List<Direccion> getAll(Session session) throws Exception {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean update(Session session, Direccion direccion) throws Exception {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
}
