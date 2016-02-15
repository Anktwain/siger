/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.GestionDAO;
import dto.Gestion;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
public class GestionIMPL implements GestionDAO {

  @Override
  public Number calcularVisitasDomiciliariasPorDespacho(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Number visitas;
    String consulta = "SELECT COUNT(*) FROM gestion WHERE id_tipo_gestion = 1 AND id_credito IN (SELECT id_credito FROM credito WHERE id_despacho = " + idDespacho + ") AND id_credito NOT IN (SELECT id_credito FROM devolucion);";
    try {
      visitas = (Number) sesion.createSQLQuery(consulta).uniqueResult();
      Logs.log.info("Se ejecutó query: " + consulta);
    } catch (HibernateException he) {
      visitas = -1;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return visitas;
  }

  @Override
  public Number calcularVisitasDomiciliariasPorGestor(int idUsuario) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Number visitas;
    String consulta = "SELECT COUNT(*) FROM gestion WHERE tipo_gestion = 'VISITA DOMICILIARIA' AND id_usuario = " + idUsuario + " AND id_credito NOT IN (SELECT id_credito FROM devolucion);";
    try {
      visitas = (Number) sesion.createSQLQuery(consulta).uniqueResult();
      Logs.log.info("Se ejecutó query: " + consulta);
    } catch (HibernateException he) {
      visitas = -1;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return visitas;
  }

  @Override
  public boolean insertarGestion(Gestion gestion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.save(gestion);
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

  @Override
  public List<Gestion> buscarGestionesCreditoGestor(int idUsuario, int idCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Gestion> gestiones;
    try {
      gestiones = sesion.createSQLQuery("SELECT * FROM gestion WHERE id_credito = " + idCredito + " AND id_usuario = " + idUsuario + ";").addEntity(Gestion.class).list();
      Logs.log.info("Se ejecutó query: ");
    } catch (HibernateException he) {
      gestiones = null;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return gestiones;
  }

  @Override
  public List<Gestion> buscarGestionesCredito(int idCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Gestion> gestiones;
    try {
      gestiones = sesion.createSQLQuery("SELECT * FROM gestion WHERE id_credito = " + idCredito + ";").addEntity(Gestion.class).list();
      Logs.log.info("Se ejecutó query: ");
    } catch (HibernateException he) {
      gestiones = null;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return gestiones;
  }

  @Override
  public List<Gestion> busquedaReporteGestiones(String consulta) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Gestion> gestiones;
    try {
      gestiones = sesion.createSQLQuery(consulta).addEntity(Gestion.class).list();
      Logs.log.info("Se ejecutó query: " + consulta);
    } catch (HibernateException he) {
      gestiones = null;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return gestiones;
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }
}
