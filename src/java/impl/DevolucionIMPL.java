/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.DevolucionDAO;
import dto.Devolucion;
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
public class DevolucionIMPL implements DevolucionDAO {

  @Override
  public List<Devolucion> retiradosBancoPorDespacho(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Devolucion> retirados = new ArrayList<>();
    String consulta = "SELECT d.* FROM deudor de JOIN sujeto s JOIN credito c JOIN devolucion d JOIN concepto_devolucion cd WHERE de.id_deudor = c.id_deudor AND d.estatus = " + Devoluciones.PENDIENTE + " AND cd.id_concepto_devolucion = 11 AND c.id_credito = d.id_credito AND cd.id_concepto_devolucion = d.id_concepto_devolucion AND de.id_sujeto = s.id_sujeto AND c.id_despacho = " + idDespacho + ";";
    try {
      retirados = sesion.createSQLQuery(consulta).addEntity(Devolucion.class).list();
      Logs.log.info("Se ejecutó query: " + consulta);
    } catch (HibernateException he) {
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return retirados;
  }

  @Override
  public List<Devolucion> bandejaDevolucionPorDespacho(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Devolucion> bandeja = new ArrayList<>();
    String consulta = "SELECT d.* FROM deudor de JOIN sujeto s JOIN credito c JOIN devolucion d JOIN concepto_devolucion cd WHERE de.id_deudor = c.id_deudor AND ((d.estatus = " + Devoluciones.ESPERA_CONSERVACION + ") OR (d.estatus = " + Devoluciones.PENDIENTE + " AND d.id_concepto_devolucion != 11)) AND c.id_credito = d.id_credito AND cd.id_concepto_devolucion = d.id_concepto_devolucion AND de.id_sujeto = s.id_sujeto AND c.id_despacho = " + idDespacho + ";";
    try {
      bandeja = sesion.createSQLQuery(consulta).addEntity(Devolucion.class).list();
      Logs.log.info("Se ejecutó query: " + consulta);
    } catch (HibernateException he) {
      bandeja = null;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return bandeja;
  }

  @Override
  public List<Devolucion> devueltosPorDespacho(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Devolucion> devueltos = new ArrayList<>();
    String consulta = "SELECT d.* FROM deudor de JOIN sujeto s JOIN credito c JOIN devolucion d JOIN concepto_devolucion cd WHERE de.id_deudor = c.id_deudor AND d.estatus = " + Devoluciones.DEVUELTO + " AND c.id_credito = d.id_credito AND cd.id_concepto_devolucion = d.id_concepto_devolucion AND de.id_sujeto = s.id_sujeto AND c.id_despacho = " + idDespacho + ";";
    try {
      devueltos = sesion.createSQLQuery(consulta).addEntity(Devolucion.class).list();
      Logs.log.info("Se ejecutó query: " + consulta);
    } catch (HibernateException he) {
      devueltos = null;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return devueltos;
  }

  @Override
  public boolean insertar(Devolucion devolucion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.save(devolucion);
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
  public boolean editar(Devolucion devolucion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.update(devolucion);
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
  public boolean eliminar(Devolucion devolucion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.delete(devolucion);
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
  public Devolucion buscarDevolucionPorNumeroCredito(String numeroCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    Devolucion dev;
    try {
      dev = (Devolucion) sesion.createSQLQuery("SELECT * FROM devolucion WHERE id_credito = (SELECT id_credito FROM credito WHERE numero_credito = '" + numeroCredito + "') AND estatus = " + Devoluciones.DEVUELTO + ";").addEntity(Devolucion.class).uniqueResult();
    } catch (HibernateException he) {
      dev = null;
      he.printStackTrace();
    } finally {
      cerrar(sesion);
    }
    return dev;
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}
