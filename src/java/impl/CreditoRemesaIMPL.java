package impl;

import dao.CreditoRemesaDAO;
import dto.CreditoRemesa;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.log.Logs;

/**
 *
 * @author brionvega
 */
public class CreditoRemesaIMPL implements CreditoRemesaDAO {

  @Override
  public int buscarRemesaActual(String credito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    CreditoRemesa creditoRemesa;
    int r = 0;
    String query = "select * from credito_remesa where id_remesa "
            + "= (select max(id_remesa) from credito_remesa) and id_credito = '"
            + credito + "'";

    try {
      creditoRemesa = (CreditoRemesa) sesion.createSQLQuery(query).addEntity(CreditoRemesa.class).uniqueResult();
      if (creditoRemesa != null) {
        r = creditoRemesa.getRemesa().getIdRemesa();
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
