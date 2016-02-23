/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.PromesaPagoDAO;
import dto.PromesaPago;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

/**
 *
 * @author Eduardo
 */
public class PromesaPagoIMPL implements PromesaPagoDAO{

  @Override
  public boolean insertar(PromesaPago promesa) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.save(promesa);
      tx.commit();
      ok = true;
    } catch (HibernateException he) {
      ok = false;
      if (tx != null) {
        tx.rollback();
      }
      he.printStackTrace();
      //         log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return ok;
  }

  @Override
  public List<PromesaPago> buscarPorConvenio(int idConvenio) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<PromesaPago> promesas = new ArrayList();
    String consulta = "SELECT * FROM promesa_pago WHERE id_convenio_pago = " + idConvenio + ";";
    try {
      promesas = sesion.createSQLQuery(consulta).addEntity(PromesaPago.class).list();
    } catch (HibernateException he) {
      promesas = null;
    } finally {
      cerrar(sesion);
    }
    return promesas;
  }
  
  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }
  
}
