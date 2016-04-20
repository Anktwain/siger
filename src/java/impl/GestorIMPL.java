package impl;

import dao.GestorDAO;
import dto.Gestor;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.constantes.Perfiles;
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

  @Override
  public Gestor buscar(int idGestor) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Gestor gestor;
    try {
      gestor = (Gestor) sesion.createSQLQuery("SELECT * FROM gestor WHERE id_gestor = " + idGestor + ";").addEntity(Gestor.class).uniqueResult();
    } catch (HibernateException he) {
      gestor = null;
      he.printStackTrace();
    } finally {
      cerrar(sesion);
    }
    return gestor;
  }
  
@Override
  public List<Gestor> buscarTodo() {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
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

  @Override
  public List<Gestor> buscarPorDespacho(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Gestor> gestores;
    try {
      gestores = sesion.createSQLQuery("SELECT g.* FROM usuario u JOIN gestor g ON u.id_usuario = g.id_usuario AND u.id_despacho = " + idDespacho + " AND u.perfil NOT IN (" + Perfiles.ADMINISTRADOR_NO_CONFIRMADO + ", " + Perfiles.ELIMINADO + ", " + Perfiles.GESTOR_NO_CONFIRMADO + ");").addEntity(Gestor.class).list();
    } catch (HibernateException he) {
      gestores = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return gestores;
  }

  @Override
  public List<Gestor> buscarPorDespachoExceptoEste(int idDespacho, int idGestor) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Gestor> gestores;
    try {
      gestores = sesion.createSQLQuery("SELECT g.* FROM usuario u JOIN gestor g ON u.id_usuario = g.id_usuario AND u.id_despacho = " + idDespacho + " AND g.id_gestor != " + idGestor + ";").addEntity(Gestor.class).list();
    } catch (HibernateException he) {
      gestores = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return gestores;
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
  
}
