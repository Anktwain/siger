/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.DondeGestionDAO;
import dao.GestionDAO;
import dao.QuienGestionDAO;
import dao.TipoQuienGestionDAO;
import dto.DescripcionGestion;
import dto.Gestion;
import java.text.DateFormat;
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
public class GestionIMPL implements GestionDAO {

  @Override
  public Number calcularVisitasDomiciliariasPorDespacho(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Number visitas;
    String consulta = "SELECT COUNT(*) FROM gestion WHERE id_tipo_gestion = 1 AND id_credito IN (SELECT id_credito FROM credito WHERE id_despacho = " + idDespacho + ") AND id_credito NOT IN (SELECT id_credito FROM devolucion);";
    try {
      visitas = (Number) sesion.createSQLQuery(consulta).uniqueResult();
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
    String consulta = "SELECT COUNT(*) FROM gestion WHERE id_tipo_gestion = 1 AND id_usuario = " + idUsuario + " AND id_credito NOT IN (SELECT id_credito FROM devolucion);";
    try {
      visitas = (Number) sesion.createSQLQuery(consulta).uniqueResult();
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
      gestiones = sesion.createSQLQuery("SELECT * FROM gestion WHERE id_credito = " + idCredito + " AND id_usuario = " + idUsuario + " ORDER BY fecha DESC;").addEntity(Gestion.class).list();
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
    List<Gestion> gestiones = new ArrayList();
    try {
      String consulta = "SELECT * FROM gestion WHERE id_credito = " + idCredito + " ORDER BY fecha DESC;";
      gestiones = sesion.createSQLQuery(consulta).addEntity(Gestion.class).list();
    } catch (HibernateException he) {
      gestiones = null;
      System.out.println("MANDO GESTIONES A NULL");
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
    } catch (HibernateException he) {
      gestiones = null;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return gestiones;
  }

  @Override
  public Gestion obtenerGestionAutomaticaPorAbreviatura(String abreviatura) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Gestion gestion = new Gestion();
    DescripcionGestion desc;
    try {
      desc = (DescripcionGestion) sesion.createSQLQuery("SELECT * FROM descripcion_gestion WHERE abreviatura = '" + abreviatura + "';").addEntity(DescripcionGestion.class).uniqueResult();
      gestion.setDescripcionGestion(desc);
      gestion.setTipoGestion(desc.getAsuntoGestion().getTipoGestion());
      gestion.setAsuntoGestion(desc.getAsuntoGestion());
      DondeGestionDAO dondeGestionDao = new DondeGestionIMPL();
      TipoQuienGestionDAO tipoQuienGestionDao = new TipoQuienGestionIMPL();
      QuienGestionDAO quienGestionDao = new QuienGestionIMPL();
      gestion.setDondeGestion(dondeGestionDao.buscarPorId(51));
      gestion.setTipoQuienGestion(tipoQuienGestionDao.buscarPorId(12));
      gestion.setQuienGestion(quienGestionDao.buscarPorId(89));
      Date fecha = new Date();
      gestion.setFecha(fecha);
    } catch (HibernateException he) {
      gestion = null;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return gestion;
  }

  @Override
  public boolean buscarGestionHoy(int idCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Gestion> gestiones;
    boolean ok = false;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    Date fecha = new Date();
    String f = df.format(fecha);
    try {
      String consulta = "SELECT * FROM gestion WHERE id_credito = " + idCredito + " AND DATE(fecha) = '" + f + "' AND id_tipo_gestion != 5;";
      gestiones = sesion.createSQLQuery(consulta).addEntity(Gestion.class).list();
      if (gestiones.size() > 0) {
        ok = true;
      }
    } catch (HibernateException he) {
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return ok;
  }

  @Override
  public Number calcularGestionesHoyPorDespacho(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Number gestiones;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String fecha = df.format(new Date());
    String consulta = "SELECT COUNT(*) FROM gestion WHERE id_tipo_gestion != 5 AND DATE(fecha) = '" + fecha + "' AND id_credito IN (SELECT id_credito FROM credito WHERE id_despacho = " + idDespacho + ") AND id_credito NOT IN (SELECT id_credito FROM devolucion);";
    try {
      gestiones = (Number) sesion.createSQLQuery(consulta).uniqueResult();
    } catch (HibernateException he) {
      gestiones = -1;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return gestiones;
  }

  @Override
  public Number calcularGestionesHoyPorGestor(int idUsuario) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Number gestiones;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String fecha = df.format(new Date());
    String consulta = "SELECT COUNT(*) FROM gestion WHERE id_tipo_gestion != 5 AND DATE(fecha) = '" + fecha + "' AND id_credito IN (SELECT id_credito FROM credito WHERE id_gestor = (SELECT id_gestor FROM gestor WHERE id_usuario = " + idUsuario + ")) AND id_credito NOT IN (SELECT id_credito FROM devolucion);";
    try {
      gestiones = (Number) sesion.createSQLQuery(consulta).uniqueResult();
    } catch (HibernateException he) {
      gestiones = -1;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return gestiones;
  }

  @Override
  public String obtenerGestorDelDia(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    String gestor;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String fecha = df.format(new Date());
    String consulta = "SELECT u.nombre, u.paterno FROM usuario u WHERE id_usuario = (SELECT MAX(id_usuario) FROM gestion WHERE DATE(fecha) = '" + fecha + "') AND id_despacho = " + idDespacho + ";";
    try {
      gestor = (String) sesion.createSQLQuery(consulta).uniqueResult();
    } catch (HibernateException he) {
      gestor = "";
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return gestor;
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}
