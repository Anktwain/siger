/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.ConvenioPagoDAO;
import dto.ConvenioPago;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.constantes.Convenios;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
public class ConvenioPagoIMPL implements ConvenioPagoDAO {

  @Override
  public boolean insertar(ConvenioPago convenio) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.save(convenio);
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
  public boolean editar(ConvenioPago convenio) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.update(convenio);
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
  public List<ConvenioPago> buscarConveniosPorCredito(int idCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<ConvenioPago> convenios;
    try {
      convenios = sesion.createSQLQuery("SELECT * FROM convenio_pago WHERE id_credito = " + idCredito + ";").addEntity(ConvenioPago.class).list();
    } catch (HibernateException he) {
      convenios = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return convenios;
  }

  @Override
  public List<ConvenioPago> buscarConveniosEnCursoCredito(int idCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<ConvenioPago> convenios;
    try {
      convenios = sesion.createSQLQuery("SELECT * FROM convenio_pago WHERE id_credito = " + idCredito + " AND estatus != " + Convenios.FINALIZADO + ";").addEntity(ConvenioPago.class).list();
    } catch (HibernateException he) {
      convenios = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return convenios;
  }

  @Override
  public List<ConvenioPago> buscarConveniosFinalizadosCredito(int idCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<ConvenioPago> convenios;
    try {
      convenios = sesion.createSQLQuery("SELECT * FROM convenio_pago WHERE id_credito = " + idCredito + " AND estatus = " + Convenios.FINALIZADO + ";").addEntity(ConvenioPago.class).list();
    } catch (HibernateException he) {
      convenios = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return convenios;
  }

  @Override
  public Number calcularSaldoPendiente(int idConvenio) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Number pagos, convenio, saldo;
    String consulta = "SELECT SUM(monto) FROM pago WHERE id_convenio_pago = " + idConvenio + ";";
    String consulta2 = "SELECT saldo_negociado FROM convenio_pago WHERE id_convenio_pago = " + idConvenio + ";";
    try {
      pagos = (Number) sesion.createSQLQuery(consulta).uniqueResult();
      convenio = (Number) sesion.createSQLQuery(consulta2).uniqueResult();
      if ((pagos == null)) {
        saldo = convenio;
      } else {
        saldo = (convenio.doubleValue() - pagos.doubleValue());
      }
      Logs.log.info("Se ejecutó query: " + consulta);
    } catch (HibernateException he) {
      saldo = -1;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return saldo;
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}
