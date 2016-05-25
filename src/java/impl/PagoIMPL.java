/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.PagoDAO;
import dto.Pago;
import dto.Quincena;
import dto.Usuario;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
  public Pago insertar(Pago pago) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    try {
      sesion.save(pago);
      tx.commit();
      Logs.log.info("Se agrego un pago");
    } catch (HibernateException he) {
      pago = null;
      if (tx != null) {
        tx.rollback();
      }
      Logs.log.error("No se pudo insertar pago");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return pago;
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
      Logs.log.error("No se pudo editar pago");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return ok;
  }

  @Override
  public boolean eliminar(Pago pago) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.delete(pago);
      tx.commit();
      Logs.log.info("Se elimino un pago");
      ok = true;
    } catch (HibernateException he) {
      ok = false;
      if (tx != null) {
        tx.rollback();
      }
      Logs.log.error("No se pudo eliminar pago");
      Logs.log.error(he.getMessage());
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
    List<Pago> pagos = new ArrayList();
    String consulta = "SELECT * FROM pago WHERE id_promesa_pago IN (SELECT id_promesa_pago FROM promesa_pago WHERE id_convenio_pago IN (SELECT id_convenio_pago FROM convenio_pago WHERE id_credito = " + idCredito + "));";
    try {
      pagos = sesion.createSQLQuery(consulta).addEntity(Pago.class).list();
    } catch (HibernateException he) {
      Logs.log.error(consulta);
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return pagos;
  }

  @Override
  public List<Pago> pagosPorDespacho(int idDespacho, String fechaInicio, String fechaFin) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Pago> pagos;
    try {
      pagos = sesion.createSQLQuery("SELECT * FROM pago WHERE id_gestor IN (SELECT id_gestor FROM gestor WHERE id_usuario IN (SELECT id_usuario FROM usuario WHERE id_despacho = " + idDespacho + ")) AND estatus NOT IN (" + Pagos.PENDIENTE + ", " + Pagos.REVISION_BANCO + ") AND fecha_registro BETWEEN '" + fechaInicio + "' AND '" + fechaFin + "';").addEntity(Pago.class).list();
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
      pagos = sesion.createSQLQuery("SELECT * FROM pago WHERE id_gestor IN (SELECT id_gestor FROM gestor WHERE id_usuario IN (SELECT id_usuario FROM usuario WHERE id_despacho = " + idDespacho + ")) AND estatus IN (" + Pagos.PENDIENTE + ", " + Pagos.REVISION_BANCO + ");").addEntity(Pago.class).list();
    } catch (HibernateException he) {
      pagos = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return pagos;
  }

  @Override
  public Pago buscarPagoHoy(int idCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Pago pago = new Pago();
    String consulta = "SELECT * FROM pago WHERE id_promesa_pago IN (SELECT id_promesa_pago FROM convenio_pago WHERE id_credito = " + idCredito + ") AND fecha_registro = CURDATE() AND estatus = " + Pagos.APROBADO + ";";
    try {
      pago = (Pago) sesion.createSQLQuery(consulta).addEntity(Pago.class).uniqueResult();
    } catch (HibernateException he) {
      pago = null;
      Logs.log.error(consulta);
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return pago;
  }

  @Override
  public String obtenerGestorDelDia(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Usuario gestor = new Usuario();
    gestor.setNombre("");
    gestor.setPaterno("");
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String fecha = df.format(new Date());
    String consulta = "SELECT * FROM usuario WHERE id_usuario = (SELECT id_usuario FROM gestor WHERE id_gestor = (SELECT id_gestor FROM pago WHERE fecha_deposito = '" + fecha + "' AND estatus = " + Pagos.APROBADO + " GROUP BY id_gestor ORDER BY COUNT(*) DESC LIMIT 1)) AND id_despacho = " + idDespacho + ";";
    try {
      gestor = (Usuario) sesion.createSQLQuery(consulta).addEntity(Usuario.class).uniqueResult();
    } catch (HibernateException he) {
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return gestor.getNombre() + " " + gestor.getPaterno();
  }

  @Override
  public Number calcularPagosPendientes(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Number pagos;
    String consulta = "SELECT COUNT(*) FROM pago WHERE estatus = " + Pagos.PENDIENTE + " AND id_gestor IN (SELECT id_gestor FROM gestor WHERE id_usuario IN (SELECT id_usuario FROM usuario WHERE id_despacho = " + idDespacho + "));";
    try {
      pagos = (Number) sesion.createSQLQuery(consulta).uniqueResult();
    } catch (HibernateException he) {
      pagos = -1;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return pagos;
  }

  @Override
  public Number calcularSaldoAprobadoHoy(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Number saldo;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String fecha = df.format(new Date());
    String consulta = "SELECT ROUND(SUM(monto_aprobado), 2) FROM pago WHERE estatus = " + Pagos.APROBADO + " AND fecha_deposito = '" + fecha + "' AND id_gestor IN (SELECT id_gestor FROM gestor WHERE id_usuario IN (SELECT id_usuario FROM usuario WHERE id_despacho = " + idDespacho + "));";
    try {
      saldo = (Number) sesion.createSQLQuery(consulta).uniqueResult();
      if (saldo == null) {
        saldo = 0;
      }
    } catch (HibernateException he) {
      saldo = -1;
      Logs.log.error(consulta);
      Logs.log.error(he);
    } finally {
      cerrar(sesion);
    }
    return saldo;
  }

  @Override
  public double calcularRecuperacionDespacho(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    double rec;
    String consulta = "SELECT ROUND((((SELECT SUM(monto_aprobado) FROM pago WHERE id_promesa_pago IN (SELECT id_promesa_pago FROM convenio_pago WHERE id_credito IN (SELECT id_credito FROM credito WHERE id_despacho = " + idDespacho + " AND id_credito NOT IN (SELECT id_credito FROM devolucion))))*100)/(SELECT SUM(monto) FROM credito WHERE id_despacho = " + idDespacho + " AND id_credito NOT IN (SELECT id_credito FROM devolucion))),4);";
    try {
      if (sesion.createSQLQuery(consulta).uniqueResult() == null) {
        rec = 0;
      } else {
        rec = (double) sesion.createSQLQuery(consulta).uniqueResult();
      }
    } catch (HibernateException he) {
      rec = -1;
      Logs.log.error(consulta);
      Logs.log.error(he);
    } finally {
      cerrar(sesion);
    }
    return rec;
  }

  @Override
  public List<Pago> pagosPorRevisarBanco(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Pago> pagos;
    try {
      pagos = sesion.createSQLQuery("SELECT * FROM pago WHERE id_gestor IN (SELECT id_gestor FROM gestor WHERE id_usuario IN (SELECT id_usuario FROM usuario WHERE id_despacho = " + idDespacho + ")) AND estatus = " + Pagos.REVISION_BANCO + ";").addEntity(Pago.class).list();
    } catch (HibernateException he) {
      pagos = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return pagos;
  }

  @Override
  public float calcularMontoGestor(int idGestor) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Pago> pagos = new ArrayList();
    try {
      String consulta = "SELECT * FROM pago WHERE id_gestor = " + idGestor + " AND id_quincena = " + new QuincenaIMPL().obtenerQuincenaActual().getIdQuincena() + " AND estatus = " + Pagos.APROBADO + ";";
      pagos = sesion.createSQLQuery(consulta).addEntity(Pago.class).list();
    } catch (HibernateException he) {
      Logs.log.error(he.getMessage());
    }
    cerrar(sesion);
    if (!pagos.isEmpty()) {
      float monto = 0;
      for (int i = 0; i < (pagos.size()); i++) {
        monto = monto + pagos.get(i).getMontoAprobado();
      }
      return monto;
    } else {
      return 0;
    }
  }

  @Override
  public Pago buscarPagoFechaCredito(Date fecha, int idCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Pago pago = new Pago();
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String f = df.format(fecha);
    String consulta = "SELECT * FROM pago WHERE id_promesa_pago IN (SELECT id_promesa_pago FROM convenio_pago WHERE id_credito = " + idCredito + " AND estatus != " + Convenios.FINALIZADO + ") AND fecha_registro = '" + f + "' AND estatus = " + Pagos.APROBADO + ";";
    try {
      pago = (Pago) sesion.createSQLQuery(consulta).addEntity(Pago.class).uniqueResult();
    } catch (HibernateException he) {
      pago = null;
      Logs.log.error(consulta);
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return pago;
  }

  @Override
  public List<Pago> buscarTodosPagosGestor(int idUsuario) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Pago> pagos = new ArrayList();
    Quincena q = new QuincenaIMPL().obtenerQuincenaActual();
    String consulta = "SELECT * FROM pago WHERE id_gestor IN (SELECT id_gestor FROM gestor WHERE id_usuario = " + idUsuario + ") AND fecha_deposito BETWEEN '" + q.getFechaInicio() + "' AND '" + q.getFechaFin() + "';";
    try {
      pagos = sesion.createSQLQuery(consulta).addEntity(Pago.class).list();
    } catch (HibernateException he) {
      pagos = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return pagos;
  }

  @Override
  public List<Pago> buscarPagosQuincenActual() {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Pago> pagos = new ArrayList();
    Quincena q = new QuincenaIMPL().obtenerQuincenaActual();
    String consulta = "SELECT * FROM pago WHERE id_quincena = " + q.getIdQuincena() + " AND estatus = " + Pagos.APROBADO + " AND pagado = " + Pagos.NO_PAGADO + ";";
    try {
      pagos = sesion.createSQLQuery(consulta).addEntity(Pago.class).list();
    } catch (HibernateException he) {
      pagos = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return pagos;
  }

  @Override
  public List<Pago> buscarPagosQuincenaActualGestor(int idGestor) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Pago> pagos = new ArrayList();
    Quincena q = new QuincenaIMPL().obtenerQuincenaActual();
    String consulta = "SELECT * FROM pago WHERE id_quincena = " + q.getIdQuincena() + " AND estatus = " + Pagos.APROBADO + " AND pagado = " + Pagos.NO_PAGADO + " AND id_gestor = " + idGestor + ";";
    try {
      pagos = sesion.createSQLQuery(consulta).addEntity(Pago.class).list();
    } catch (HibernateException he) {
      Logs.log.error(consulta);
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return pagos;
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}
