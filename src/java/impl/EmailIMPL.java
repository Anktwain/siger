package impl;

import dao.EmailDAO;
import dto.Email;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.constantes.Correos;
import util.log.Logs;

/**
 * La clase {@code EmailIMPL} permite ...
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public class EmailIMPL implements EmailDAO {

  /**
   *
   *
   * @return
   */
  @Override
  public Email insertar(Email email) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    try {
      sesion.save(email);
      tx.commit();
      Logs.log.info("Se insertó un nuevo Email: id = " + email.getIdEmail() +
              " asociado a Sujeto: id = " + email.getSujeto().getIdSujeto());
    } catch (HibernateException he) {
      email = null;
      if (tx != null) {
        tx.rollback();
      }
      Logs.log.error("No se pudo insertar Email:");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return email;
  }

  /**
   *
   *
   * @return
   */
  @Override
  public boolean editar(Email email) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.update(email);
      tx.commit();
      ok = true;
    } catch (HibernateException he) {
      ok = false;
      if (tx != null) {
        tx.rollback();
      }
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
  public boolean eliminar(Email email) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.delete(email);
      tx.commit();
      ok = true;
    } catch (HibernateException he) {
      ok = false;
      if (tx != null) {
        tx.rollback();
      }
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
  public Email buscar(int idEmail) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  /**
   *
   *
   * @return
   */
  @Override
  public List<Email> buscarTodo() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List<Email> buscarPorSujeto(int idSujeto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Email> listaEmails;
    String consulta = "select e.* from email e join sujeto s on s.id_sujeto=e.id_sujeto where s.id_sujeto=" + idSujeto + ";";
    try {
      listaEmails = sesion.createSQLQuery(consulta).addEntity(Email.class).list();
    } catch (HibernateException he) {
      listaEmails = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return listaEmails;
  }

  @Override
  public Email buscarPrincipalPorSujeto(int idSujeto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Email correo = new Email();
    String consulta = "SELECT * FROM email WHERE id_sujeto = " + idSujeto + " AND principal = " + Correos.PRINCIPAL + ";";
    try {
      correo = (Email) sesion.createSQLQuery(consulta).addEntity(Email.class).uniqueResult();
    } catch (HibernateException he) {
      Logs.log.error(consulta);
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return correo;
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