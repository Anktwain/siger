/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.ConvenioPagoDAO;
import dto.ConvenioPago;
import dto.PromesaPago;
import java.util.ArrayList;
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
  public ConvenioPago insertar(ConvenioPago convenio) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    try {
      sesion.save(convenio);
      tx.commit();
    } catch (HibernateException he) {
      convenio = null;
      if (tx != null) {
        tx.rollback();
      }
      Logs.log.error("No se pudo insertar el nuevo convenio");
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return convenio;
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
      Logs.log.error("No se pudo editar el convenio");
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return ok;
  }

  @Override
  public boolean eliminar(ConvenioPago convenio) {
   Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.delete(convenio);
      tx.commit();
      ok = true;
    } catch (HibernateException he) {
      ok = false;
      if (tx != null) {
        tx.rollback();
      }
      Logs.log.error("No se pudo eliminar el convenio");
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return ok;
  }

  @Override
  public ConvenioPago buscarConvenioEnCursoCredito(int idCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    ConvenioPago convenio;
    try {
      convenio = (ConvenioPago) sesion.createSQLQuery("SELECT * FROM convenio_pago WHERE id_credito = " + idCredito + " AND estatus != " + Convenios.FINALIZADO + ";").addEntity(ConvenioPago.class).uniqueResult();
    } catch (HibernateException he) {
      convenio = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return convenio;
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
    Number saldoConvenio, saldo = 0;
    float cantidadPromesas = 0;
    List<PromesaPago> promesas;
    String consulta = "SELECT saldo_negociado FROM convenio_pago WHERE id_convenio_pago = " + idConvenio + ";";
    String consulta2 = "SELECT * FROM promesa_pago WHERE id_convenio_pago = " + idConvenio + ";";
    try {
      saldoConvenio = (Number) sesion.createSQLQuery(consulta).uniqueResult();
      promesas = sesion.createSQLQuery(consulta2).addEntity(PromesaPago.class).list();
      if (!promesas.isEmpty()) {
        for (int i = 0; i < (promesas.size()); i++) {
          cantidadPromesas = cantidadPromesas + promesas.get(i).getCantidadPrometida();
        }
      } else {
        saldo = saldoConvenio;
      }
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
