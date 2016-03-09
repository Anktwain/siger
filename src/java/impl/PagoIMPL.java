/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.PagoDAO;
import dto.Pago;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.constantes.Convenios;
import util.constantes.Pagos;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
public class PagoIMPL implements PagoDAO {

  @Override
  public boolean insertar(Pago pago) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.save(pago);
      tx.commit();
      Logs.log.info("Se agrego un pago");
      ok = true;
    } catch (HibernateException he) {
      pago = null;
      if (tx != null) {
        tx.rollback();
      }
      Logs.log.error("No se pudo insertar pago");
      Logs.log.error(he.getMessage());
      ok = false;
    } finally {
      cerrar(sesion);
    }
    return ok;
  }

  @Override
  public boolean editar(Pago pago) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.update(pago);
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
  public List<Pago> buscarPagosPorConvenioActivo(int idConvenio) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Pago> pagos;
    try {
      pagos = sesion.createSQLQuery("SELECT * FROM pago WHERE id_promesa_pago IN (SELECT id_promesa_pago FROM promesa_pago WHERE id_convenio_pago = " + idConvenio + ");").addEntity(Pago.class).list();
    } catch (HibernateException he) {
      pagos = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return pagos;
  }

  @Override
  public List<Pago> buscarPagosPorCredito(int idCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Pago> pagos;
    try {
      pagos = sesion.createSQLQuery("SELECT * FROM pago WHERE id_promesa_pago IN (SELECT id_promesa_pago FROM convenio_pago WHERE id_credito = " + idCredito + ");").addEntity(Pago.class).list();
    } catch (HibernateException he) {
      pagos = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return pagos;
  }

  @Override
  public List<Pago> pagosPorDespacho(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Pago> pagos;
    try {
      pagos = sesion.createSQLQuery("SELECT * FROM pago WHERE id_gestor IN (SELECT id_gestor FROM gestor WHERE id_usuario IN (SELECT id_usuario FROM usuario WHERE id_despacho = " + idDespacho + ")) AND estatus != " + Pagos.PENDIENTE + ";").addEntity(Pago.class).list();
    } catch (HibernateException he) {
      pagos = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return pagos;
  }

  @Override
  public List<Pago> pagosPorRevisarPorDespacho(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Pago> pagos;
    try {
      pagos = sesion.createSQLQuery("SELECT * FROM pago WHERE id_gestor IN (SELECT id_gestor FROM gestor WHERE id_usuario IN (SELECT id_usuario FROM usuario WHERE id_despacho = " + idDespacho + ")) AND estatus = " + Pagos.PENDIENTE + ";").addEntity(Pago.class).list();
    } catch (HibernateException he) {
      pagos = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return pagos;
  }

  @Override
  public Number calcularPagosRealizados() {
    return 0;
  }

  @Override
  public Number calcularPagosPorAprobarPorGestor(int idGestor) {
    return 0;
  }

  @Override
  public Number calcularRecuperacionDeInstitucion() {
    return 0;
  }

  @Override
  public Number calcularRecuperacionPorGestor(int idGestor) {
    return 0;
  }

  @Override
  public Pago buscarPagoHoy(int idCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Pago pago = new Pago();
    Date f = new Date();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String fecha = df.format(f);
    try {
      pago = (Pago) sesion.createSQLQuery("SELECT * FROM pago WHERE id_promesa_pago IN (SELECT id_promesa_pago FROM convenio_pago WHERE id_credito = " + idCredito + ") AND fecha_registro = '" + fecha + "';").addEntity(Pago.class).uniqueResult();
    } catch (HibernateException he) {
      pago = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return pago;
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}
