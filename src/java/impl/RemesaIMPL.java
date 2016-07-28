package impl;

import dao.RemesaDAO;
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
public class RemesaIMPL implements RemesaDAO {

  @Override
  public int buscarRemesaActual() {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
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

  @Override
  public Remesa insertar(Remesa remesa) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();

    try {
      sesion.save(remesa);
      tx.commit();
      Logs.log.info("Se insertó Remesa ");
    } catch (HibernateException he) {
      remesa = null;
      if (tx != null) {
        tx.rollback();
      }
      Logs.log.error("No se pudo insertar Remesa:");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return remesa;
  }

  @Override
  public Remesa obtenerUltimaRemesa() {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Remesa remesa;
    String consulta = "SELECT * FROM remesa WHERE DATE(fecha_carga) = CURDATE() ORDER BY fecha_carga DESC LIMIT 1;";
    try {
      remesa = (Remesa) sesion.createSQLQuery(consulta).addEntity(Remesa.class).uniqueResult();
    } catch (HibernateException he) {
      remesa = null;
      Logs.log.error(consulta);
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return remesa;
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}
