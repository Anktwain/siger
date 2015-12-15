/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.DespachoDAO;
import dto.Despacho;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.log.Logs;

/**
 *
 * @author brionvega
 */
public class DespachoIMPL implements DespachoDAO {

  @Override
  public Despacho buscar(String nombreCorto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    Despacho despacho;

    try {
      despacho = (Despacho) sesion.createQuery("from Despacho d where "
              + "d.nombreCorto = '" + nombreCorto + "'").uniqueResult();
    } catch (HibernateException he) {
      despacho = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return despacho;
  }
  
  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }
}
