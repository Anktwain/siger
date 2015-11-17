package impl;

import dao.DireccionDAO;
import dto.Direccion;
import dto.tablas.Dir;
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
      Logs.log.info("Se insertó una nueva Direccion: id = " + direccion.getIdDireccion()
              + " asociado a Sujeto: id = " + direccion.getSujeto().getIdSujeto());
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
    Transaction tx = sesion.beginTransaction();
    List<Direccion> listaDirecciones;
    String consulta = "select d.* from direccion d join sujeto s on s.id_sujeto=d.id_sujeto where s.id_sujeto= " + idSujeto + ";";

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
  public Dir obtenerDireccionCompleta(int idDireccion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Object[]> d;
    Dir completa = new Dir();
    String consulta = "SELECT d.calle, c.nombre, m.nombre, e.nombre, c.codigo_postal FROM direccion d JOIN colonia c JOIN municipio m JOIN estado_republica e WHERE d.id_colonia = c.id_colonia AND d.id_municipio = m.id_municipio AND d.id_estado = e.id_estado AND d.id_direccion = " + idDireccion + ";";
    try {
      d = sesion.createSQLQuery(consulta).list();
      for (Object[] row : d) {
        completa.setCalleNumero(row[0].toString());
        completa.setColonia(row[1].toString());
        completa.setMunicipio(row[2].toString());
        completa.setEstado(row[3].toString());
        completa.setCp(row[4].toString());
        System.out.println("CALLE Y NUMERO: " + row[0].toString());
        System.out.println("COLONIA: " + row[1].toString());
        System.out.println("MUNICIPIO: " + row[2].toString());
        System.out.println("ESTADO: " + row[3].toString());
        System.out.println("CODIGO POSTAL: " + row[4].toString());
      }
      Logs.log.info("Se ejecutó query: " + consulta);
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
