/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.MarcajeDAO;
import dto.Marcaje;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
public class MarcajeIMPL implements MarcajeDAO{

  @Override
  public Marcaje buscarMarcajePorId(int idMarcaje) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Marcaje marcaje;
    String consulta = "SELECT * FROM marcaje WHERE id_marcaje = " + idMarcaje + ";";
    try {
      marcaje = (Marcaje) sesion.createSQLQuery(consulta).addEntity(Marcaje.class).uniqueResult();
    } catch (HibernateException he) {
      marcaje = null;
      Logs.log.error(consulta);
      Logs.log.error(he);
    } finally {
      cerrar(sesion);
    }
    return marcaje;
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }
}
