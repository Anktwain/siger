/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.DevolucionDAO;
import dto.ConceptoDevolucion;
import dto.Credito;
import dto.Deudor;
import dto.Devolucion;
import dto.Sujeto;
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
public class DevolucionIMPL implements DevolucionDAO {

  @Override
  public List<Devolucion> retiradosBancoPorDespacho(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    List<Devolucion> retirados;
    String consulta = "SELECT {d.*}, {de.*}, {c.*}, {s.*}, {cd.*} FROM deudor de JOIN sujeto s JOIN credito c JOIN devolucion d JOIN concepto_devolucion cd WHERE de.id_deudor = c.id_deudor AND d.estatus = 2 AND cd.id_concepto_devolucion = 11 AND c.id_credito = d.id_credito AND cd.id_concepto_devolucion = d.id_concepto_devolucion AND de.id_sujeto = s.id_sujeto AND c.id_despacho = " + idDespacho + ";";
    try {
      retirados = sesion.createSQLQuery(consulta).addEntity("de", Deudor.class).addEntity("s", Sujeto.class).addEntity("c", Credito.class).addEntity("d", Devolucion.class).addEntity("cd", ConceptoDevolucion.class).list();
      //query = session.createSQLQuery("select {e.*}, {a.*} from Employee e join Address a ON e.emp_id=a.emp_id").addEntity("e",Employee.class).addJoin("a","e.address");
      Logs.log.info("Se ejecutó query: " + consulta);
    } catch (HibernateException he) {
      retirados = null;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return retirados;
  }

  @Override
  public List<Devolucion> bandejaDevolucionPorDespacho(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    List<Devolucion> bandeja;
    String consulta = "SELECT {d.*}, {de.*}, {c.*}, {s.*}, {cd.*} FROM deudor de JOIN sujeto s JOIN credito c JOIN devolucion d JOIN concepto_devolucion cd WHERE de.id_deudor = c.id_deudor AND (d.estatus = 2 OR d.estatus = 3) AND cd.id_concepto_devolucion != 11 AND c.id_credito = d.id_credito AND cd.id_concepto_devolucion = d.id_concepto_devolucion AND de.id_sujeto = s.id_sujeto AND c.id_despacho = " + idDespacho + ";";
    try {
      bandeja = sesion.createSQLQuery(consulta).addEntity("de", Deudor.class).addEntity("s", Sujeto.class).addEntity("c", Credito.class).addEntity("d", Devolucion.class).addEntity("cd", ConceptoDevolucion.class).list();
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
    Transaction tx = sesion.beginTransaction();
    List<Devolucion> devueltos;
    String consulta = "SELECT {d.*}, {de.*}, {c.*}, {s.*}, {cd.*} FROM deudor de JOIN sujeto s JOIN credito c JOIN devolucion d JOIN concepto_devolucion cd WHERE de.id_deudor = c.id_deudor AND d.estatus = 0 AND c.id_credito = d.id_credito AND cd.id_concepto_devolucion = d.id_concepto_devolucion AND de.id_sujeto = s.id_sujeto AND c.id_despacho = " + idDespacho + ";";
    try {
      devueltos = sesion.createSQLQuery(consulta).addEntity("de", Deudor.class).addEntity("s", Sujeto.class).addEntity("c", Credito.class).addEntity("d", Devolucion.class).addEntity("cd", ConceptoDevolucion.class).list();
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

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}
