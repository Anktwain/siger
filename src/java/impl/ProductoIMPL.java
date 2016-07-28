package impl;

import dao.ProductoDAO;
import dto.Producto;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.log.Logs;

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
    } catch (HibernateException he) {
      ok = false;
      if (tx != null) {
        tx.rollback();
      }
      Logs.log.error("No se pudo insertar el producto");
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
      Logs.log.error("No se pudo editar el producto");
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
      Logs.log.error("No se pudo hacer borrado logico al producto");
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
  public Producto buscar(int idProducto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Producto producto;
    String consulta = "SELECT * FROM producto WHERE id_producto = " + idProducto + ";";
    try {
      producto = (Producto) sesion.createSQLQuery(consulta).addEntity(Producto.class).uniqueResult();
    } catch (HibernateException he) {
      producto = null;
      Logs.log.error(consulta);
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return producto;
  }

  @Override
  public List<Producto> buscarProductosPorInstitucion(int idInstitucion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Producto> productos = new ArrayList();
    String consulta = "SELECT * FROM producto WHERE id_institucion = " + idInstitucion + ";";
    try {
      productos = sesion.createSQLQuery(consulta).addEntity(Producto.class).list();
    } catch (HibernateException he) {
      productos = null;
      Logs.log.error(consulta);
      Logs.log.error(he.getMessage());
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

  @Override
  public Producto buscar(String nombreProducto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Producto producto;
    try {
      producto = (Producto) sesion.createSQLQuery("select * from producto where nombre = '" + nombreProducto + "';").addEntity(Producto.class).uniqueResult();
    } catch (HibernateException he) {
      producto = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return producto;
  }

  @Override
  public String buscarProductoDelCredito(int idCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    String producto = "";
    String consulta = "SELECT nombre FROM producto where id_producto = (SELECT id_producto FROM credito WHERE id_credito = " + idCredito + ");";
    try {
      producto = (String) sesion.createSQLQuery(consulta).uniqueResult();
    } catch (HibernateException he) {
      Logs.log.error(consulta);
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return producto;
  }

  @Override
  public List<String> buscarFamiliasPorInstitucion(int idInstitucion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<String> familias = new ArrayList();
    String consulta = "SELECT DISTINCT familia FROM producto where id_institucion = " + idInstitucion + ";";
    try {
      familias = sesion.createSQLQuery(consulta).list();
    } catch (HibernateException he) {
      Logs.log.error(consulta);
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return familias;
  }

  @Override
  public List<Producto> buscarProductosPorDespacho(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Producto> productos = new ArrayList();
    String consulta = "SELECT * FROM producto WHERE id_producto IN (SELECT id_producto FROM credito WHERE id_despacho = " + idDespacho + ");";
    try {
      productos = sesion.createSQLQuery(consulta).addEntity(Producto.class).list();
    } catch (HibernateException he) {
      Logs.log.error(consulta);
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return productos;
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
