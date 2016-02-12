package impl;

import dao.EstadoRepublicaDAO;
import dto.EstadoRepublica;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
public class EstadoRepublicaIMPL implements EstadoRepublicaDAO {

  /**
   * Devuelve un {@code List<>} con todos los EstadoRepublica que se encuentren
   * dados de alta en el sistema.
   */
  @Override
  public List<EstadoRepublica> buscarTodo() {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<EstadoRepublica> estados;
    try {
      estados = sesion.createSQLQuery("SELECT * FROM estado_republica ORDER BY nombre ASC;").addEntity(EstadoRepublica.class).list();
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
    EstadoRepublica estado;

    try {
      estado = (EstadoRepublica) sesion.createSQLQuery("SELECT * from estado_republica WHERE nombre LIKE '%" + cadena + "%';").addEntity(EstadoRepublica.class).uniqueResult();
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
  public EstadoRepublica buscarPorId(int idEstado) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
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

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}
