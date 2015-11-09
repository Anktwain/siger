package impl;

import dao.GestorDAO;
import dto.Deudor;
import dto.Gestor;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.constantes.Sujetos;
import util.log.Logs;

/**
 * La clase {@code GestorIMPL} permite ...
 *
 * @author
 * @author
 * @author brionvega, Dedalo
 * @since SigerWeb2.0
 */
public class GestorIMPL implements GestorDAO {

  /**
   *
   *
   * @return
   */
  @Override
  public boolean insertar(Gestor gestor) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;

    try {
      sesion.save(gestor);
      tx.commit();
      ok = true;
      //log.info("Se insertó un nuevo usuaario");
    } catch (HibernateException he) {
      ok = false;
      if (tx != null) {
        tx.rollback();
      }
      he.printStackTrace();
      //         log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return ok;
  }

  /**
   *
   *
   * @param
   */
  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

  @Override
  public List<Gestor> buscarTodo() {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    List<Gestor> gestores;

    try { // Buscamos a todos los usuarios que no hayan sido eliminados, un usuario eliminado tiene perfil = 0.
      gestores = sesion.createSQLQuery("SELECT u.*, g.* FROM usuario u JOIN gestor g ON u.id_usuario = g.id_usuario;").addEntity(Gestor.class).list();
    } catch (HibernateException he) {
      gestores = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }

    return gestores;
  }
}
