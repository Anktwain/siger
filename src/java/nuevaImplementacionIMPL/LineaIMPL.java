package nuevaImplementacionIMPL;

import dto.Linea;
import nuevaImplementacionDAO.LineaDAO;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public class LineaIMPL implements LineaDAO {

  @Override
  public boolean insert(Session session, Linea linea) throws Exception {
    session.save(linea);
    
    return true;
  }
  
}
