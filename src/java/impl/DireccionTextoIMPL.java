/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.DireccionTextoDAO;
import dto.DireccionTexto;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.constantes.Direcciones;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
public class DireccionTextoIMPL implements DireccionTextoDAO{

  @Override
  public boolean insertar(DireccionTexto direccion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.save(direccion);
      tx.commit();
      ok = true;
    } catch (HibernateException he) {
      ok = false;
      if (tx != null) {
        tx.rollback();
      }
      Logs.log.error("No se pudo insertar direccion texto.");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return ok;
  }

  @Override
  public boolean editar(DireccionTexto direccion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.update(direccion);
      tx.commit();
      ok = true;
    } catch (HibernateException he) {
      ok = false;
      if (tx != null) {
        tx.rollback();
      }
      Logs.log.error("No se pudo editar direccion texto.");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return ok;
  }

  @Override
  public boolean eliminar(DireccionTexto direccion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.delete(direccion);
      tx.commit();
      ok = true;
    } catch (HibernateException he) {
      ok = false;
      if (tx != null) {
        tx.rollback();
      }
      Logs.log.error("No se pudo eliminar direccion texto.");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return ok;
  }

  @Override
  public List<DireccionTexto> buscarPorCredito(String numeroCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<DireccionTexto> direcciones = new ArrayList();
    String consulta = "SELECT * FROM direccion_texto WHERE numero_credito = '" + numeroCredito + "' ORDER BY fecha DESC;";
    try {
      direcciones = sesion.createSQLQuery(consulta).addEntity(DireccionTexto.class).list();
    } catch (HibernateException he) {
      Logs.log.error(consulta);
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return direcciones;
  }

  @Override
  public List<DireccionTexto> buscarTodas() {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<DireccionTexto> direcciones = new ArrayList();
    String consulta = "SELECT * FROM direccion_texto ORDER BY estado ASC;";
    try {
      direcciones = sesion.createSQLQuery(consulta).addEntity(DireccionTexto.class).list();
    } catch (HibernateException he) {
      Logs.log.error(consulta);
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return direcciones;
  }

  @Override
  public List<DireccionTexto> buscarSinValidar() {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<DireccionTexto> direcciones = new ArrayList();
    String consulta = "SELECT * FROM direccion_texto WHERE validada = " + Direcciones.SIN_VALIDAR + " ORDER BY estado ASC;";
    try {
      direcciones = sesion.createSQLQuery(consulta).addEntity(DireccionTexto.class).list();
    } catch (HibernateException he) {
      Logs.log.error(consulta);
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return direcciones;
  }
  
  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }
  
}
