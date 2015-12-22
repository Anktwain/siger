package impl;

import dao.RemesaDao;
import dto.Remesa;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.log.Logs;

/**
 *
 * @author brionvega
 */
public class RemesaIMPL implements RemesaDao {

  @Override
  public int buscarRemesaActual() {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    Remesa remesa;
    int r = 0;
    String query = "select * from remesa where id_remesa = (select max(id_remesa) from remesa)";

    try {
      remesa = (Remesa) sesion.createSQLQuery(query).addEntity(Remesa.class).uniqueResult();
      if (remesa != null) {
        r = remesa.getIdRemesa();
      }
    } catch (HibernateException he) {
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }

    return r;
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}
