package impl;

import dao.SujetoDAO;
import dto.Sujeto;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.constantes.Sujetos;
import util.log.Logs;

/**
 * La clase {@code SujetoIMPL} permite ...
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public class SujetoIMPL implements SujetoDAO {

  /**
   *
   *
   * @return
   */
  @Override
  public Sujeto insertar(Sujeto sujeto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();

    try {
      sesion.save(sujeto);
      tx.commit();
      Logs.log.info("Se insertó un nuevo sujeto: id = " + sujeto.getIdSujeto());
    } catch (HibernateException he) {
      sujeto = null;
      if (tx != null) {
        tx.rollback();
      }
      Logs.log.error("No se pudo insertar Sujeto:");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }

    return sujeto;
  }

  /**
   *
   *
   * @return
   */
  @Override
  public boolean editar(Sujeto sujeto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;

    try {
      sesion.update(sujeto);
      tx.commit();
      ok = true;
    } catch (HibernateException he) {
      ok = false;
      if (tx != null) {
        tx.rollback();
      }
      Logs.log.error("No se pudo editar el sujeto");
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
  public boolean eliminar(Sujeto sujeto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;

    try {
      // Se colocará algo similar a esto: usuario.setPerfil(Perfiles.ELIMINADO);
      sujeto.setEliminado(Sujetos.ELIMINADO);
      sesion.update(sujeto);
      tx.commit();
      ok = true;
    } catch (HibernateException he) {
      ok = false;
      if (tx != null) {
        tx.rollback();
      }
      Logs.log.error("No se pudo hacer borrado logico del sujeto");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }

    return ok;
  }

  /**
   *
   *
   * @param sujeto
   * @return
   */
  @Override
  public boolean eliminarEnSerio(Sujeto sujeto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;

    try {
      sesion.delete(sujeto);
      tx.commit();
      ok = true;
    } catch (HibernateException he) {
      ok = false;
      if (tx != null) {
        tx.rollback();
      }
      Logs.log.error("No se pudo eliminar el sujeto");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }

    return ok;
  }

  /**
   *
   *
   * @param idSujeto
   * @return
   */
  @Override
  public Sujeto buscar(int idSujeto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Sujeto sujeto;
    String consulta = "SELECT * FROM sujeto WHERE id_sujeto = " + Integer.toString(idSujeto) + ";";
    /*
     try {
     sujeto = (Sujeto) sesion.get(Sujeto.class, idSujeto);
     // obtuvo el usuario, solo se muestra si no ha sido eliminado:
     if (sujeto != null) // Colocar algo similar a esto: if(usuario.getPerfil() == Perfiles.ELIMINADO)
     {
     sujeto = null;
     }
     } catch (HibernateException he) {
     sujeto = null;
     he.printStackTrace();
     } finally {
     cerrar(sesion);
     }
        
     return sujeto;
     */
    try {
      sujeto = (Sujeto) sesion.createSQLQuery(consulta).addEntity(Sujeto.class).uniqueResult();
    } catch (HibernateException he) {
      sujeto = null;
      Logs.log.error(consulta);
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return sujeto;
  }

  /**
   *
   *
   * @return
   */
  @Override
  public List<Sujeto> buscarTodo() {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Sujeto> listaSujeto;
    String consulta = "SELECT * FROM SUJETO;";
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
    /*
     return null;
     */
  }

  @Override
  public Sujeto ultimoInsertado() {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    Sujeto sujeto;
    
    try {
      sujeto = (Sujeto) sesion.createSQLQuery("SELECT * from sujeto where id_sujeto = (SELECT MAX(id_sujeto) from sujeto);").addEntity(Sujeto.class).uniqueResult();
    } catch (HibernateException he) {
      sujeto = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return sujeto;
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
