/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.ActualizacionDAO;
import dto.Actualizacion;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.constantes.Devoluciones;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
public class ActualizacionIMPL implements ActualizacionDAO {

  @Override
  public boolean insertar(Actualizacion actualizacion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.save(actualizacion);
      tx.commit();
      ok = true;
    } catch (HibernateException he) {
      ok = false;
      if (tx != null) {
        tx.rollback();
      }
      Logs.log.error("No se pudo insertar la actualizacion");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return ok;
  }

  @Override
  public List<Actualizacion> buscarPorCredito(int idCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Actualizacion> actualizaciones;
    try {
      actualizaciones = sesion.createSQLQuery("SELECT DISTINCT * FROM actualizacion WHERE id_credito = " + idCredito + " ORDER BY id_actualizacion DESC;").addEntity(Actualizacion.class).list();
    } catch (HibernateException he) {
      actualizaciones = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return actualizaciones;
  }

  @Override
  public Actualizacion buscarUltimaActualizacion(int idCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Actualizacion actualizacion = new Actualizacion();
    String consulta = "SELECT * FROM actualizacion WHERE id_credito = " + idCredito + " ORDER BY id_actualizacion DESC LIMIT 1;";
    try {
      actualizacion = (Actualizacion) sesion.createSQLQuery(consulta).addEntity(Actualizacion.class).uniqueResult();
    } catch (HibernateException he) {
      actualizacion = null;
      Logs.log.error(consulta);
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return actualizacion;
  }

  @Override
  public boolean editar(Actualizacion actualizacion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.update(actualizacion);
      tx.commit();
      ok = true;
    } catch (HibernateException he) {
      ok = false;
      if (tx != null) {
        tx.rollback();
      }
      Logs.log.error("No se pudo editar la actualizacion");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return ok;
  }

  @Override
  public float obtenerMontoPrometidoGestor(int idGestor) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Actualizacion> acts = new ArrayList();
    try {
      acts = sesion.createSQLQuery("SELECT * FROM actualizacion WHERE id_credito IN (SELECT id_credito FROM credito WHERE id_gestor = " + idGestor + ") AND id_credito NOT IN (SELECT id_credito FROM devolucion WHERE estatus = " + Devoluciones.DEVUELTO + ");").addEntity(Actualizacion.class).list();
    } catch (HibernateException he) {
      Logs.log.error(he.getMessage());
    }
    cerrar(sesion);
    if (!acts.isEmpty()) {
      float monto = 0;
      for (int i = 0; i < (acts.size()); i++) {
        monto = monto + acts.get(i).getSaldoVencido();
      }
      return monto;
    } else {
      return 0;
    }
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}
