package impl;

import dao.DireccionDAO;
import dto.Direccion;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.log.Logs;

/**
 * La clase {@code DireccionIMPL} permite ...
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public class DireccionIMPL implements DireccionDAO {

  /**
   *
   *
   * @return
   */
  @Override
  public Direccion insertar(Direccion direccion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();

    try {
      sesion.save(direccion);
      tx.commit();
    } catch (HibernateException he) {
      direccion = null;
      if (tx != null) {
        tx.rollback();
      }
      Logs.log.error("No se pudo insertar Direccion:");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return direccion;
  }

  /**
   *
   *
   * @return
   */
  @Override
  public boolean editar(Direccion direccion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;

    try {
      sesion.update(direccion);
      tx.commit();
      ok = true;
    } catch (HibernateException he) {
      ok = false;
      if (tx != null) {
        tx.rollback();
      }
      he.printStackTrace();
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
  public boolean eliminar(Direccion direccion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;

    try {
      sesion.delete(direccion);
      tx.commit();
      ok = true;
    } catch (HibernateException he) {
      ok = false;
      if (tx != null) {
        tx.rollback();
      }
      he.printStackTrace();
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
  public Direccion buscar(int idDireccion) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  /**
   *
   *
   * @return
   */
  @Override
  public List<Direccion> buscarTodo() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List<Direccion> buscarPorSujeto(int idSujeto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Direccion> listaDirecciones;
    String consulta = "SELECT d.* FROM direccion d JOIN sujeto s ON s.id_sujeto = d.id_sujeto WHERE s.id_sujeto = " + idSujeto + ";";

    try {
      listaDirecciones = sesion.createSQLQuery(consulta).addEntity(Direccion.class).list();
    } catch (HibernateException he) {
      listaDirecciones = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return listaDirecciones;
  }

  @Override
  public Direccion obtenerDireccionCompleta(int idDireccion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Direccion completa = new Direccion();
    String consulta = "SELECT d.* FROM direccion d JOIN colonia c JOIN municipio m JOIN estado_republica e WHERE d.id_colonia = c.id_colonia AND d.id_municipio = m.id_municipio AND d.id_estado = e.id_estado AND d.id_direccion = " + idDireccion + ";";
    try {
      completa = (Direccion) sesion.createSQLQuery(consulta).addEntity(Direccion.class).uniqueResult();
    } catch (HibernateException he) {
      completa = null;
    } finally {
      cerrar(sesion);
    }
    return completa;
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
