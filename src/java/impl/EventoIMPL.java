/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.EventoDAO;
import dto.Evento;
import java.util.ArrayList;
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
public class EventoIMPL implements EventoDAO {

  @Override
  public boolean insertar(Evento evento) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.save(evento);
      tx.commit();
      ok = true;
      Logs.log.info("Se insertó un nuevo evento");
    } catch (HibernateException he) {
      ok = false;
      if (tx != null) {
        tx.rollback();
      }
      Logs.log.error("No se pudo insertar evento");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return ok;
  }

  @Override
  public List<Evento> buscarEventosVigentes(int idUsuario) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Evento> eventos = new ArrayList();
    String consulta = "SELECT * FROM evento WHERE DATE(fecha_inicio) >= CURDATE();";
    try {
      eventos = sesion.createSQLQuery(consulta).addEntity(Evento.class).list();
    } catch (HibernateException he) {
      Logs.log.error(consulta);
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return eventos;
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}
