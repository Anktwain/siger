package nuevaImplementacionIMPL;

import dto.Institucion;
import java.util.List;
import nuevaImplementacionDAO.InstitucionDAO;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public class InstitucionIMPL implements InstitucionDAO {

  @Override
  public Institucion insert(Session session, Institucion institucion) throws Exception {
    session.save(institucion);
    
    return institucion;
  }

  @Override
  public Institucion getById(Session session, int idInstitucion) throws Exception {
    return (Institucion) session.get(Institucion.class, idInstitucion);
  }

  @Override
  public List<Institucion> getAll(Session session) throws Exception {
    String hql = "from Institucion";
    Query query = session.createQuery(hql);
    
    List<Institucion> listaInstitucion = (List<Institucion>) query.list();
    return listaInstitucion;
  }

  @Override
  public boolean update(Session session, Institucion institucion) throws Exception {
    session.update(institucion);
    
    return true;
  }
  
}
