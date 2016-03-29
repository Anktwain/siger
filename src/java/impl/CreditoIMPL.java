/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.CreditoDAO;
import dto.Actualizacion;
import dto.Credito;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class CreditoIMPL implements CreditoDAO {

  @Override
  public boolean editar(Credito credito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.update(credito);
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
  public Number contarCreditosActivos(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Number creditos;
    String consulta = "SELECT COUNT(*) FROM credito c WHERE id_credito NOT IN (SELECT id_credito FROM devolucion) AND c.id_despacho = " + idDespacho + " ORDER BY numero_credito ASC;";
    try {
      creditos = (Number) sesion.createSQLQuery(consulta).uniqueResult();
    } catch (HibernateException he) {
      creditos = -1;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return creditos;
  }

  @Override
  public Number contarCreditosActivosPorGestor(int idUsuario) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Number creditos;
    String consulta = "SELECT COUNT(*) FROM credito WHERE id_gestor IN (SELECT id_gestor FROM gestor WHERE id_usuario = 19) AND id_credito NOT IN (SELECT id_credito FROM devolucion);";
    try {
      creditos = (Number) sesion.createSQLQuery(consulta).uniqueResult();
    } catch (HibernateException he) {
      creditos = -1;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return creditos;
  }

  @Override
  public Credito buscarCreditoPorId(int idCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Credito c;
    String consulta = "SELECT * FROM credito WHERE id_credito = " + idCredito + ";";
    try {
      c = (Credito) sesion.createSQLQuery(consulta).addEntity(Credito.class).uniqueResult();
    } catch (HibernateException he) {
      c = null;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return c;
  }

  @Override
  public List<Credito> creditosEnGestionPorDespacho(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Credito> creditos = new ArrayList<>();
    String consulta = "SELECT c.* FROM credito c JOIN deudor d JOIN sujeto s JOIN despacho des JOIN institucion i JOIN producto p JOIN gestor g JOIN usuario u WHERE d.id_sujeto = s.id_sujeto AND u.id_usuario = g.id_usuario AND g.id_gestor = c.id_gestor AND p.id_producto = c.id_producto AND d.id_deudor = c.id_deudor AND i.id_institucion = c.id_institucion AND id_credito NOT IN (SELECT id_credito FROM devolucion) AND c.id_despacho = des.id_despacho AND des.id_despacho = " + idDespacho + " ORDER BY numero_credito ASC;";
    try {
      creditos = sesion.createSQLQuery(consulta).addEntity(Credito.class).list();
    } catch (HibernateException he) {
      creditos = null;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return creditos;
  }

  @Override
  public List<Credito> tablaCreditosEnGestionPorDespacho(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Credito> creditos = new ArrayList<>();
    String consulta = "SELECT c.* FROM credito c WHERE id_credito NOT IN (SELECT id_credito FROM devolucion) AND c.id_despacho = " + idDespacho + " ORDER BY numero_credito ASC;";
    try {
      creditos = sesion.createSQLQuery(consulta).addEntity(Credito.class).list();
    } catch (HibernateException he) {
      creditos = null;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return creditos;
  }

  @Override
  public int obtenerIdDelCredito(String numeroCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    int id;
    String consulta = "SELECT id_credito FROM credito WHERE numero_credito = '" + numeroCredito + "';";
    try {
      id = (int) sesion.createSQLQuery(consulta).uniqueResult();
    } catch (HibernateException he) {
      id = -1;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return id;
  }

  @Override
  public List<Credito> buscarCreditosPorCliente(String numeroDeudor) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List creditos;
    String consulta = "SELECT * FROM credito WHERE id_deudor IN (SELECT id_deudor FROM deudor WHERE numero_deudor ='" + numeroDeudor + "');";
    try {
      creditos = sesion.createSQLQuery(consulta).addEntity(Credito.class).list();
    } catch (HibernateException he) {
      creditos = null;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return creditos;
  }

  @Override
  public List<Credito> buscarCreditosRelacionados(Credito creditoActual) {
    // ESTE METODO BUSCA TODOS LOS CREDITOS RELACIONADOS AL CLIENTE DE UN CREDITO ESPECIFICADO.
    // REGRESARA LA LISTA CON LOS CREDITOS DE ESTE NUMERO DE CLIENTE EXCEPTO EL CREDITO ENVIADO EN EL ID
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    int cred = creditoActual.getIdCredito();
    int deudor = creditoActual.getDeudor().getIdDeudor();
    List creditos;
    String consulta = "SELECT * FROM credito WHERE id_credito != " + cred + " AND id_deudor = " + deudor + ";";
    try {
      creditos = sesion.createSQLQuery(consulta).addEntity(Credito.class).list();
    } catch (HibernateException he) {
      creditos = null;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return creditos;
  }

  @Override
  public List<String> barraBusquedaAdmin(String valor, int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<String> resultados = new ArrayList();
    List<Object[]> c;
    String consulta = "SELECT c.numero_credito, s.nombre_razon_social FROM credito c JOIN deudor d JOIN sujeto s JOIN despacho des JOIN gestor g WHERE d.id_sujeto = s.id_sujeto AND g.id_gestor = c.id_gestor AND d.id_deudor = c.id_deudor AND des.id_despacho = " + idDespacho + " AND (c.numero_credito LIKE '%" + valor + "%' OR s.nombre_razon_social LIKE '%" + valor + "%');";
    try {
      c = sesion.createSQLQuery(consulta).list();
      for (Object[] row : c) {
        String cadena = row[1].toString() + " - " + row[0].toString();
        resultados.add(cadena);
      }
    } catch (HibernateException he) {
      resultados = null;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return resultados;
  }

  @Override
  public Credito buscar(String numeroCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    Credito credito;

    try {
      credito = (Credito) sesion.createSQLQuery("SELECT * FROM credito WHERE numero_credito = '" + numeroCredito + "';").addEntity(Credito.class).uniqueResult();
    } catch (HibernateException he) {
      credito = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return credito;
  }

  @Override
  public Credito insertar(Credito credito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();

    try {
      sesion.save(credito);
      tx.commit();
      Logs.log.info("Se insertó un nuevo Credito");
    } catch (HibernateException he) {
      credito = null;
      if (tx != null) {
        tx.rollback();
      }
      Logs.log.error("No se pudo insertar Credito:");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return credito;
  }

  @Override
  public List<Credito> buscarPorMarcaje(int marcaje) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Credito> creditos;
    String consulta = "SELECT * FROM credito WHERE id_marcaje = " + marcaje + ";";
    try {
      creditos = sesion.createSQLQuery(consulta).addEntity(Credito.class).list();
    } catch (HibernateException he) {
      creditos = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return creditos;
  }

  @Override
  public List<Credito> buscarCreditosPorCampana(int idCampana) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Credito> creditos;
    String consulta = "SELECT * FROM credito WHERE id_campana = " + idCampana + ";";
    try {
      creditos = sesion.createSQLQuery(consulta).addEntity(Credito.class).list();
    } catch (HibernateException he) {
      creditos = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return creditos;
  }

  @Override
  public List<Credito> buscarCreditosConPromesaDePagoHoy() {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Credito> creditos;
    Date f = new Date();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String fecha = df.format(f);
    String consulta = "SELECT * FROM credito WHERE id_credito in (SELECT id_credito FROM convenio_pago WHERE estatus != " + Convenios.FINALIZADO + " AND id_convenio_pago IN (SELECT id_convenio_pago FROM promesa_pago WHERE fecha_prometida = '" + fecha + "'));";
    try {
      creditos = sesion.createSQLQuery(consulta).addEntity(Credito.class).list();
    } catch (HibernateException he) {
      creditos = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return creditos;
  }

  @Override
  public List<Credito> buscarCreditosPorCampanaGestor(int idCampana, int idUsuario) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Credito> creditos;
    String consulta = "SELECT * FROM credito WHERE id_campana = " + idCampana + " AND id_gestor = (SELECT id_gestor FROM gestor WHERE id_usuario = " + idUsuario + ");";
    try {
      creditos = sesion.createSQLQuery(consulta).addEntity(Credito.class).list();
    } catch (HibernateException he) {
      creditos = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return creditos;
  }

  @Override
  public List<Credito> buscarCreditosPorGestor(int idUsuario) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Credito> creditos;
    String consulta = "SELECT * FROM credito WHERE id_gestor = (SELECT id_gestor FROM gestor WHERE id_usuario = " + idUsuario + ");";
    try {
      creditos = sesion.createSQLQuery(consulta).addEntity(Credito.class).list();
    } catch (HibernateException he) {
      creditos = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return creditos;
  }

  @Override
  public List<Credito> buscarCreditosPorZona(int idZona) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Credito> creditos;
    String consulta = "SELECT * FROM credito WHERE id_deudor IN (SELECT id_deudor FROM deudor WHERE id_sujeto IN (SELECT id_sujeto FROM direccion WHERE id_direccion IN (SELECT id_direccion FROM direccion WHERE id_municipio IN (SELECT id_municipio FROM region WHERE id_zona = " + idZona + "))));";
    try {
      creditos = sesion.createSQLQuery(consulta).addEntity(Credito.class).list();
    } catch (HibernateException he) {
      creditos = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return creditos;
  }

  @Override
  public List<Credito> buscarCreditosPorSaldoVencido(float saldoInferior, float saldoSuperior) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Credito> creditos;
    String consulta = "SELECT DISTINCT * FROM credito WHERE id_credito IN (SELECT id_credito FROM actualizacion WHERE saldo_vencido > " + saldoInferior + " AND saldo_vencido < " + saldoSuperior + " ORDER BY id_actualizacion DESC);";
    try {
      creditos = sesion.createSQLQuery(consulta).addEntity(Credito.class).list();
    } catch (HibernateException he) {
      creditos = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return creditos;
  }

  @Override
  public float buscarSaldoVencidoCredito(int idCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Actualizacion> acts = new ArrayList();
    try {
      acts = sesion.createSQLQuery("SELECT * FROM actualizacion WHERE id_credito = " + idCredito + " ORDER BY id_actualizacion DESC LIMIT 1;").addEntity(Actualizacion.class).list();
    } catch (HibernateException he) {
      Logs.log.error(he.getStackTrace());
    }
    cerrar(sesion);
    if (!acts.isEmpty()) {
      return acts.get(0).getSaldoVencido();
    } else {
      return 0;
    }
  }

  @Override
  public List<Credito> buscarCreditosPorMesesVencidos(int mesesVencidos) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Credito> creditos;
    String consulta = "SELECT DISTINCT * FROM credito WHERE id_credito IN (SELECT id_credito FROM actualizacion WHERE meses_vencidos = " + mesesVencidos + " ORDER BY id_actualizacion DESC);";
    try {
      creditos = sesion.createSQLQuery(consulta).addEntity(Credito.class).list();
    } catch (HibernateException he) {
      creditos = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return creditos;
  }

  @Override
  public int buscarMesesVencidosCredito(int idCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    int mesesVencidos;
    String consulta = "SELECT meses_vencidos FROM actualizacion WHERE id_credito = " + idCredito + " ORDER BY id_actualizacion DESC LIMIT 1;";
    try {
      mesesVencidos = (int) sesion.createSQLQuery(consulta).uniqueResult();
    } catch (HibernateException he) {
      mesesVencidos = -1;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return mesesVencidos;
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}
