/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.PromesaPagoDAO;
import dto.PromesaPago;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

/**
 *
 * @author Eduardo
 */
public class PromesaPagoIMPL implements PromesaPagoDAO {

  @Override
  public boolean insertar(PromesaPago promesa) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.save(promesa);
      tx.commit();
      ok = true;
    } catch (HibernateException he) {
      ok = false;
      if (tx != null) {
        tx.rollback();
      }
      he.printStackTrace();
      //         log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return ok;
  }

  @Override
  public List<PromesaPago> buscarPorConvenio(int idConvenio) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<PromesaPago> promesas = new ArrayList();
    String consulta = "SELECT * FROM promesa_pago WHERE id_convenio_pago = " + idConvenio + ";";
    try {
      promesas = sesion.createSQLQuery(consulta).addEntity(PromesaPago.class).list();
    } catch (HibernateException he) {
      promesas = null;
    } finally {
      cerrar(sesion);
    }
    return promesas;
  }

  @Override
  public boolean buscarPromesasHoy(int idCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<PromesaPago> promesas = new ArrayList();
    boolean ok = false;
    Date f = new Date();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String fecha = df.format(f);
    String consulta = "SELECT * FROM promesa_pago WHERE id_convenio_pago IN (SELECT id_convenio_pago FROM convenio_pago WHERE id_credito = " + idCredito + ") AND fecha_prometida = '" + fecha + "' LIMIT 1;";
    try {
      promesas = sesion.createSQLQuery(consulta).addEntity(PromesaPago.class).list();
    } catch (HibernateException he) {
    } finally {
      cerrar(sesion);
      if (!promesas.isEmpty()) {
        ok = true;
      }
    }
    return ok;
  }

  @Override
  public boolean buscarPromesasAnticipaFecha(int idCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<PromesaPago> promesas = new ArrayList();
    boolean ok = false;
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    cal.add(Calendar.DATE, 4);
    Date f = cal.getTime();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String fecha = df.format(f);
    String consulta = "SELECT * FROM promesa_pago WHERE id_convenio_pago IN (SELECT id_convenio_pago FROM convenio_pago WHERE id_credito = " + idCredito + ") AND fecha_prometida <= '" + fecha + "';";
    try {
      promesas = sesion.createSQLQuery(consulta).addEntity(PromesaPago.class).list();
    } catch (HibernateException he) {
    } finally {
      cerrar(sesion);
      if (!promesas.isEmpty()) {
        ok = true;
      }
    }
    return ok;
  }

  @Override
  public boolean buscarPromesasPorCumplirse(int idCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<PromesaPago> promesas = new ArrayList();
    boolean ok = false;
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    cal.add(Calendar.DATE, 4);
    Date f = cal.getTime();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String fecha = df.format(f);
    String consulta = "SELECT * FROM promesa_pago WHERE id_convenio_pago IN (SELECT id_convenio_pago FROM convenio_pago WHERE id_credito = " + idCredito + ") AND fecha_prometida > '" + fecha + "';";
    try {
      promesas = sesion.createSQLQuery(consulta).addEntity(PromesaPago.class).list();
    } catch (HibernateException he) {
      promesas = null;
    } finally {
      cerrar(sesion);
      if (!promesas.isEmpty()) {
        ok = true;
      }
    }
    return ok;
  }

  @Override
  public List<PromesaPago> promesasPagoHoy(int idCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<PromesaPago> promesas = new ArrayList();
    Date f = new Date();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String fecha = df.format(f);
    String consulta = "SELECT * FROM promesa_pago WHERE id_convenio_pago = (SELECT id_convenio_pago FROM convenio_pago WHERE id_credito = " + idCredito + ") AND fecha_prometida > '" + fecha + "';";
    try {
      promesas = sesion.createSQLQuery(consulta).addEntity(PromesaPago.class).list();
    } catch (HibernateException he) {
      promesas = null;
    } finally {
      cerrar(sesion);
    }
    return promesas;
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}
