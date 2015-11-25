/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.GestionDAO;
import dto.Gestion;
import java.util.Calendar;
import java.util.Date;
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

  private String fechaInicio;
  private String fechaFin;

  public GestionIMPL() {
  }

  @Override
  public Number calcularVisitasDomiciliariasPorDespacho(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    Number visitas;
    calcularFechas();
    String consulta = "SELECT COUNT(*) FROM gestion WHERE tipo_gestion = 'VISITA DOMICILIARIA' AND fecha BETWEEN '" + fechaInicio + "' AND '" + fechaFin + "' AND id_credito IN (SELECT id_credito FROM credito WHERE id_despacho = " + idDespacho + ") AND id_credito NOT IN (SELECT id_credito FROM devolucion);";
    try {
      visitas = (Number) sesion.createSQLQuery(consulta).uniqueResult();
      Logs.log.info("Se ejecutó query: " + consulta);
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
    Transaction tx = sesion.beginTransaction();
    Number visitas;
    calcularFechas();
    String consulta = "SELECT COUNT(*) FROM gestion WHERE tipo_gestion = 'VISITA DOMICILIARIA' AND fecha BETWEEN '2015-11-01' AND '2015-11-31' AND id_gestor = (SELECT id_gestor FROM gestor WHERE id_usuario = " + idUsuario + ") AND id_credito NOT IN (SELECT id_credito FROM devolucion);";
    try {
      visitas = (Number) sesion.createSQLQuery(consulta).uniqueResult();
      Logs.log.info("Se ejecutó query: " + consulta);
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

  private void calcularFechas() {
    Date date = new Date();
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int mes = cal.get(Calendar.MONTH) + 1;
    int año = cal.get(Calendar.YEAR);
    switch (mes) {
      case 1:
        fechaInicio = año + "-0" + mes + "-01";
        fechaFin = año + "-0" + mes + "-31";
        break;
      case 2:
        if ((año % 4 == 0) && ((año % 100 != 0) || (año % 400 == 0))) {
          fechaInicio = año + "-0" + mes + "-01";
          fechaFin = año + "-0" + mes + "-29";
        } else {
          fechaInicio = año + "-0" + mes + "-01";
          fechaFin = año + "-0" + mes + "-28";
        }
        break;
      case 3:
        fechaInicio = año + "-0" + mes + "-01";
        fechaFin = año + "-0" + mes + "-31";
        break;
      case 4:
        fechaInicio = año + "-0" + mes + "-01";
        fechaFin = año + "-0" + mes + "-30";
        break;
      case 5:
        fechaInicio = año + "-0" + mes + "-01";
        fechaFin = año + "-0" + mes + "-31";
        break;
      case 6:
        fechaInicio = año + "-0" + mes + "-01";
        fechaFin = año + "-0" + mes + "-30";
        break;
      case 7:
        fechaInicio = año + "-0" + mes + "-01";
        fechaFin = año + "-0" + mes + "-31";
        break;
      case 8:
        fechaInicio = año + "-0" + mes + "-01";
        fechaFin = año + "-0" + mes + "-31";
        break;
      case 9:
        fechaInicio = año + "-0" + mes + "-01";
        fechaFin = año + "-0" + mes + "-30";
        break;
      case 10:
        fechaInicio = año + "-" + mes + "-01";
        fechaFin = año + "-" + mes + "-31";
        break;
      case 11:
        fechaInicio = año + "-" + mes + "-01";
        fechaFin = año + "-" + mes + "-30";
        break;
      case 12:
        fechaInicio = año + "-" + mes + "-01";
        fechaFin = año + "-" + mes + "-31";
        break;
    }
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }
}
