/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.QuincenaDAO;
import dto.Quincena;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
public class QuincenaIMPL implements QuincenaDAO{

  @Override
  public Quincena obtenerQuincenaActual() {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Quincena quincena;
    String consulta = "SELECT * FROM quincena WHERE id_quincena = (SELECT MAX(id_quincena) FROM quincena);";
    try {
      quincena = (Quincena) sesion.createSQLQuery(consulta).addEntity(Quincena.class).uniqueResult();
      Logs.log.info("Se ejecutó query: " + consulta);
    } catch (HibernateException he) {
      quincena = null;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return quincena;
  }
  
  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }
  
}
