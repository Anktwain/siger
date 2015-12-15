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
  public Number contarCreditosActivos(){
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    Number creditos;
    String consulta = "SELECT COUNT(*) FROM credito_remesa WHERE id_remesa IN (SELECT MAX(id_remesa) FROM remesa);";
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
  public Number contarCreditosActivosPorGestor(int idGestor){
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    Number creditos;
    String consulta = "SELECT COUNT(id_credito) FROM credito_remesa WHERE id_remesa = (SELECT MAX(id_remesa) FROM remesa) AND id_credito IN (select id_credito FROM credito WHERE id_gestor = " + idGestor + ");";
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
  public Credito buscar(String numeroCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    Credito credito;
    
    try {
      credito = (Credito) sesion.createSQLQuery("select * from credito where numero_credito = '" + numeroCredito + "';").addEntity(Credito.class).uniqueResult();
    } catch (HibernateException he) {
      credito = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return credito;
  }
  
  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }
  
}
