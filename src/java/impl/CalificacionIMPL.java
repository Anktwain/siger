/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.CalificacionDAO;
import dto.Calificacion;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author Eduardo
 */
public class CalificacionIMPL implements CalificacionDAO {

  @Override
  public Calificacion buscarPorId(int idCalificacion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Calificacion calif;
    String consulta = "SELECT * FROM calificacion WHERE id_calificacion = " + idCalificacion + ";";
    try {
      calif = (Calificacion) sesion.createSQLQuery(consulta).addEntity(Calificacion.class).uniqueResult();
    } catch (HibernateException he) {
      calif = null;
      he.printStackTrace();
    } finally {
      cerrar(sesion);
    }
    return calif;
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }
}
