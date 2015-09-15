package impl;

import dao.EstadoRepublicaDAO;
import dto.EstadoRepublica;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
public class EstadoRepublicaIMPL implements EstadoRepublicaDAO {

  @Override
  public List<EstadoRepublica> buscarTodo() {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    List<EstadoRepublica> estados;
    try {
      estados = sesion.createSQLQuery("select * from estado_republica order by nombre asc;").addEntity(EstadoRepublica.class).list();
    } catch (HibernateException he) {
      estados = null;
      Logs.log.error("No se pudo obtener lista: EstadoRepublica");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return estados;
  }

  @Override
  public EstadoRepublica buscar(int idEstado) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    EstadoRepublica estado;

    try {
      estado = (EstadoRepublica) sesion.get(EstadoRepublica.class, idEstado);
    } catch (HibernateException he) {
      estado = null;
      Logs.log.error("No se pudo obtener: EstadoRepublica");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }

    return estado;
  }

  @Override
  public EstadoRepublica buscar(String cadena) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    EstadoRepublica estado;

    try {
      estado = (EstadoRepublica) sesion.createSQLQuery("SELECT * from estado_republica WHERE nombre LIKE '%"+cadena+"%';").addEntity(EstadoRepublica.class).uniqueResult();
    } catch (HibernateException he) {
      estado = null;
      Logs.log.error("No se pudo obtener: EstadoRepublica");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }

    return estado;
  }


  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}