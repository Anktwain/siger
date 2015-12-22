/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.RegionDAO;
import dto.Region;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

/**
 *
 * @author brionvega
 */
public class RegionIMPL implements RegionDAO {

  @Override
  public Region insertar(Region region) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    int id;

    try {
      sesion.save(region);
      tx.commit();
      //log.info("Se insertó un nuevo usuaario");
    } catch (HibernateException he) {
      id = 0;
      if (tx != null) {
        tx.rollback();
      }
      he.printStackTrace();
      //         log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return region;
  }
  
    private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }
}
