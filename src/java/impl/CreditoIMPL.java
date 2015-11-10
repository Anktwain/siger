/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.CreditoDAO;
import dto.Credito;
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
public class CreditoIMPL implements CreditoDAO {

  @Override
  public Number contarCreditosActivos(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    Number creditos;
    String consulta = "SELECT COUNT(*) FROM credito_remesa WHERE id_remesa IN (SELECT MAX(id_remesa) FROM remesa) AND id_credito IN (SELECT id_credito FROM credito where id_despacho = " + idDespacho + ");";
    try {
      creditos = (Number) sesion.createSQLQuery(consulta).uniqueResult();
      Logs.log.info("Se ejecutó query: " + consulta);
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
    Transaction tx = sesion.beginTransaction();
    Number creditos;
    String consulta = "SELECT COUNT(*) FROM credito_remesa WHERE id_remesa = (SELECT MAX(id_remesa) FROM remesa) AND id_credito IN (select id_credito FROM credito WHERE id_gestor IN (SELECT id_gestor FROM gestor WHERE id_usuario = " + idUsuario + "));";
    try {
      creditos = (Number) sesion.createSQLQuery(consulta).uniqueResult();
      Logs.log.info("Se ejecutó query: " + consulta);
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
      Logs.log.info("Se ejecutó query: " + consulta);
    } catch (HibernateException he) {
      c = null;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return c;
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}
