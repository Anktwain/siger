package nuevaImplementacionIMPL;

import dto.Email;
import java.util.List;
import nuevaImplementacionDAO.EmailDAO;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public class EmailIMPL implements EmailDAO {

  @Override
  public boolean insert(Session session, Email email) throws Exception {
    session.save(email);
    
    return true;
  }

  @Override
  public Email getById(Session session, int idEmail) throws Exception {
    return (Email) session.get(Email.class, idEmail);
  }

  @Override
  public List<Email> getAll(Session session) throws Exception {
    String hql = "from Email";
    Query query = session.createQuery(hql);
    
    List<Email> listaEmail = (List<Email>) query.list();
    return listaEmail;
  }

  @Override
  public boolean update(Session session, Email email) throws Exception {
    session.update(email);
    
    return true;
  }
  
}
