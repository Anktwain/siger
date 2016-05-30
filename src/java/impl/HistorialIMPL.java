/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.HistorialDAO;
import dto.Historial;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class HistorialIMPL implements HistorialDAO {

  @Override
  public boolean insertar(Historial historial) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.save(historial);
      tx.commit();
      ok = true;
    } catch (HibernateException he) {
      ok = false;
      historial = null;
      if (tx != null) {
        tx.rollback();
      }
      Logs.log.error("No se pudo insertar historial");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return ok;
  }

  @Override
  public List<Historial> buscarPorCredito(int idCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Historial> historial = new ArrayList();
    try {
      historial = sesion.createSQLQuery("SELECT * FROM historial WHERE id_credito = " + idCredito + ";").addEntity(Historial.class).list();
    } catch (HibernateException he) {
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return historial;
  }

  @Override
  public boolean verificarCampioCampañaCredito(int idCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Historial> historial;
    boolean ok = false;
    Date f = new Date();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String fecha = df.format(f);
    String consulta = "SELECT * FROM historial WHERE id_credito = " + idCredito + " AND fecha = '" + fecha + "' AND evento = 'Automatico. Cambio de campaña.';";
    try {
      historial = sesion.createSQLQuery(consulta).addEntity(Historial.class).list();
      if (historial.size() > 0) {
        ok = true;
      }
    } catch (HibernateException he) {
      ok = false;
      Logs.log.error(consulta);
      Logs.log.error(he);
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return ok;
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }
}
