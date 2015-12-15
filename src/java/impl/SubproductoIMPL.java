package impl;

import dao.SubproductoDAO;
import dto.Subproducto;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.log.Logs;

/**
 * La clase {@code SubproductoIMPL} permite ...
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public class SubproductoIMPL implements SubproductoDAO {

  /**
   *
   *
   * @return
   */
  @Override
  public boolean insertar(Subproducto subproducto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;

    try {
      sesion.save(subproducto);
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
   * @return
   */
  @Override
  public boolean editar(Subproducto subproducto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;

    try {
      sesion.update(subproducto);
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
  public boolean eliminar(Subproducto subproducto) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  /**
   *
   *
   * @return
   */
  @Override
  public Subproducto buscar(int idSubproducto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    Subproducto subproducto;
    try {
      subproducto = (Subproducto) sesion.createSQLQuery("select * from subproducto where id_subproducto = " + idSubproducto + ";").addEntity(Subproducto.class).uniqueResult();
    } catch (HibernateException he) {
      subproducto = null;
      he.printStackTrace();
    } finally {
      cerrar(sesion);
    }
    return subproducto;
  }

  @Override
  public Subproducto buscar(String nombreSubproducto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    Subproducto subproducto;
    try {
      subproducto = (Subproducto) sesion.createSQLQuery("select * from subproducto where nombre = '" + nombreSubproducto + "';").addEntity(Subproducto.class).uniqueResult();
    } catch (HibernateException he) {
      subproducto = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return subproducto;
  }

  /**
   *
   *
   * @return
   */
  @Override
  public List<Subproducto> buscarTodo() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List<Subproducto> buscarSubproductosPorInstitucion(int idInstitucion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    List<Subproducto> subproductos;
    try {
      subproductos = sesion.createSQLQuery("select distinct s.id_subproducto, s.nombre, s.descripcion, s.id_producto from subproducto s join producto p where s.id_producto in (select distinct x.id_producto from producto x join institucion y where x.id_institucion = " + idInstitucion + ");").addEntity(Subproducto.class).list();
    } catch (HibernateException he) {
      subproductos = null;
      he.printStackTrace();
    } finally {
      cerrar(sesion);
    }
    return subproductos;
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
