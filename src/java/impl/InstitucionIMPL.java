package impl;

import dao.InstitucionDAO;
import dto.Institucion;
import dto.Sujeto;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.constantes.Sujetos;
import util.log.Logs;

/**
 * La clase {@code InstitucionIMPL} permite ...
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public class InstitucionIMPL implements InstitucionDAO {

  /**
   *
   *
   * @return
   */
  @Override
  public boolean insertar(Institucion institucion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.save(institucion);
      tx.commit();
      ok = true;
      //log.info("Se insertó un nuevo usuaario");
    } catch (HibernateException he) {
      ok = false;
      if (tx != null) {
        tx.rollback();
      }
      Logs.log.error("No se pudo editar la institucion");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return ok;
  }

  /**
   *
   *
   * @return
   */
  @Override
  public boolean editar(Institucion institucion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.update(institucion);
      tx.commit();
      ok = true;
    } catch (HibernateException he) {
      ok = false;
      if (tx != null) {
        tx.rollback();
      }
      Logs.log.error("No se pudo editar la institucion");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return ok;
  }

  /**
   *
   *
   * @return
   */
  @Override
  public boolean eliminar(Institucion institucion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      // Se colocará algo similar a esto: usuario.setPerfil(Perfiles.ELIMINADO);
      sesion.update(institucion);
      tx.commit();
      ok = true;
    } catch (HibernateException he) {
      ok = false;
      if (tx != null) {
        tx.rollback();
      }
      Logs.log.error("No se pudo eliminar la institucion");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return ok;
  }

  /**
   *
   *
   * @return
   */
  @Override
  public Institucion buscar(int idInstitucion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Institucion institucion;
    String consulta = "SELECT * FROM institucion WHERE id_institucion =" + idInstitucion + ";";
    try {
      institucion = (Institucion) sesion.createSQLQuery(consulta).addEntity(Institucion.class).uniqueResult();
    } catch (HibernateException he) {
      institucion = null;
      Logs.log.error(consulta);
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return institucion;
  }

  @Override
  public Institucion buscarInstitucionPorSujeto(int idInstitucion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Institucion institucion;
    String consulta = "SELECT * FROM institucion WHERE id_sujeto = " + Integer.toString(idInstitucion) + ";";
    try {
      institucion = (Institucion) sesion.createSQLQuery(consulta).addEntity(Institucion.class).uniqueResult();
    } catch (HibernateException he) {
      institucion = null;
      Logs.log.error(consulta);
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return institucion;
  }

  /**
   *
   *
   * @return
   */
  @Override
  public List<Institucion> buscarTodo() {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Institucion> listaInstituciones = new ArrayList();
    String consulta = "SELECT * FROM institucion;";
    try {
      listaInstituciones = sesion.createSQLQuery(consulta).addEntity(Institucion.class).list();
    } catch (HibernateException he) {
      listaInstituciones = null;
      Logs.log.error(consulta);
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return listaInstituciones;
  }

  /**
   *
   *
   * @return
   */

  @Override
  public List<Sujeto> buscarInstituciones() {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Sujeto> listaSujeto;
    String consulta = "SELECT * FROM sujeto s JOIN institucion e WHERE s.eliminado = " + Sujetos.ACTIVO + " AND s.id_sujeto = e.id_sujeto;";
    try {
      listaSujeto = sesion.createSQLQuery(consulta).addEntity(Sujeto.class).list();
    } catch (HibernateException he) {
      listaSujeto = null;
      Logs.log.error(consulta);
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return listaSujeto;
  }

  @Override
  public List<Institucion> buscarInstitucionesPorDespacho(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Institucion> listaInstituciones;
    String consulta = "SELECT * FROM institucion WHERE id_institucion IN (SELECT id_institucion FROM producto WHERE id_producto IN (SELECT id_producto FROM credito WHERE id_credito NOT IN (SELECT id_credito FROM devolucion) AND id_despacho = " + idDespacho + "));";
    try {
      listaInstituciones = sesion.createSQLQuery(consulta).addEntity(Institucion.class).list();
    } catch (HibernateException he) {
      listaInstituciones = null;
      Logs.log.error(consulta);
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return listaInstituciones;
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
