/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.FacDAO;
import dto.Fac;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
public class FacIMPL implements FacDAO{

  @Override
  public List<Fac> buscarPorCredito(int idCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Fac> facs = new ArrayList();
    try {
      facs = sesion.createSQLQuery("SELECT DISTINCT * FROM fac WHERE id_actualizacion = (SELECT id_actualizacion FROM actualizacion WHERE id_credito = " + idCredito + ") ORDER BY anio DESC, mes DESC;").addEntity(Fac.class).list();
    } catch (HibernateException he) {
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return facs;
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }
  
}
