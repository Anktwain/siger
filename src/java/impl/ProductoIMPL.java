package impl;

import dao.ProductoDAO;
import dto.Producto;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

/**
 * La clase {@code ProductoIMPL} permite ...
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public class ProductoIMPL implements ProductoDAO {

  /**
   *
   *
   * @return
   */
  @Override
  public boolean insertar(Producto producto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;

    try {
      sesion.save(producto);
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
  public boolean editar(Producto producto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;

    try {
      sesion.update(producto);
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
  public boolean eliminar(Producto producto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;

    try {
      // Se colocará algo similar a esto: usuario.setPerfil(Perfiles.ELIMINADO);
      sesion.update(producto);
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
  public Producto buscar(int idProducto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    Producto producto;
    try {
      producto = (Producto) sesion.createSQLQuery("select * from producto where id_producto = " + idProducto + ";").addEntity(Producto.class).uniqueResult();
    } catch (HibernateException he) {
      producto = null;
      he.printStackTrace();
    } finally {
      cerrar(sesion);
    }
    return producto;
  }

  @Override
  public List<Producto> buscarProductosPorInstitucion(int idInstitucion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    List<Producto> productos;
    try {
      productos = sesion.createSQLQuery("select * from producto where id_institucion = " + idInstitucion + ";").addEntity(Producto.class).list();
    } catch (HibernateException he) {
      productos = null;
      he.printStackTrace();
    } finally {
      cerrar(sesion);
    }
    return productos;
  }
  
  /**
   *
   *
   * @return
   */
  @Override
  public List<Producto> buscarTodo() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
