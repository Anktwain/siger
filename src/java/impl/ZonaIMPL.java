package impl;

import dao.ZonaDAO;
import dto.Zona;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

/**
 *
 * @author Pablo
 */
public class ZonaIMPL implements ZonaDAO {

  @Override
  public Zona insertar(Zona zona) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    int id;

    try {
      sesion.save(zona);
      tx.commit();
      //log.info("Se insertó un nuevo usuaario");
    } catch (HibernateException he) {
      id = 0;
      if (tx != null) {
        tx.rollback();
      }
      he.printStackTrace();
      //         log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return zona;
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }
}
