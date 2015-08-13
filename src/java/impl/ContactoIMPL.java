package impl;

import dao.ContactoDAO;
import dto.Contacto;
import dto.Sujeto;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.constantes.Perfiles;
import util.constantes.Sujetos;
import util.log.Logs;

/**
 * La clase {@code ContactoIMPL} permite ...
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public class ContactoIMPL implements ContactoDAO {

  /**
   *
   *
   * @return
   */
  @Override
  public boolean insertar(Contacto contacto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;

    try {
      sesion.save(contacto);
      tx.commit();
      ok = true;
      //log.info("Se insertó un nuevo usuaario");
    } catch (HibernateException he) {
      ok = false;
      if (tx != null) {
        tx.rollback();
      }
//            he.printStackTrace();
      Logs.log.trace(he.getMessage());
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
  public boolean editar(Contacto contacto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;

    try {
      sesion.update(contacto);
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
  public boolean eliminar(Contacto contacto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;

    try {
      // Se colocará algo similar a esto: usuario.setPerfil(Perfiles.ELIMINADO);
      contacto.getSujeto().setEliminado(Perfiles.ELIMINADO);
      sesion.update(contacto.getSujeto());
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
  public Contacto buscar(int idContacto) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  /**
   *
   *
   * @return
   */
  @Override
  public List<Contacto> buscarTodo() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List<Contacto> buscarPorSujeto(int idSujeto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    List<Contacto> listaContactos;

    String consulta = "select c.* from sujeto s join contacto c on s.id_sujeto = c.sujetos_id_sujeto "
            + "join cliente l on l.id_cliente = c.clientes_id_cliente join sujeto s2 on s2.id_sujeto = l.sujetos_id_sujeto "
            + "where s2.id_sujeto=" + idSujeto + " and s.eliminado = " + Sujetos.ACTIVO + ";";

    try {
      listaContactos = sesion.createSQLQuery(consulta).addEntity(Contacto.class).list();
    } catch (HibernateException he) {
      listaContactos = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }

    return listaContactos;
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
