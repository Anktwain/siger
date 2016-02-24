/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.CampanaDAO;
import dto.Campana;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
public class CampanaIMPL implements CampanaDAO {

  @Override
  public List<Campana> buscarTodas() {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Campana> campanas;
    try {
      campanas = sesion.createSQLQuery("SELECT * FROM campana;").addEntity(Campana.class).list();
    } catch (HibernateException he) {
      campanas = null;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return campanas;
  }

  @Override
  public Campana buscarPorId(int idCampana) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Campana campana;
    try {
      campana = (Campana) sesion.createSQLQuery("SELECT * FROM campana WHERE id_campana = " + idCampana + ";").addEntity(Campana.class).uniqueResult();
    } catch (HibernateException he) {
      campana = null;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return campana;
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}
