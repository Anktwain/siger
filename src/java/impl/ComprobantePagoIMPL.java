/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.ComprobantePagoDAO;
import dto.ComprobantePago;
import java.util.ArrayList;
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
public class ComprobantePagoIMPL implements ComprobantePagoDAO{

  @Override
  public boolean insertar(ComprobantePago comprobante) {
    boolean ok;
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    try {
      sesion.save(comprobante);
      tx.commit();
      ok = true;
    } catch (HibernateException he) {
      ok = false;
      if (tx != null) {
        tx.rollback();
      }
      Logs.log.error("No se pudo insertar comprobante de pago");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return ok;
  }

  @Override
  public List<ComprobantePago> buscarPorPago(int idPago) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<ComprobantePago> comprobantes = new ArrayList();
    String consulta = "SELECT * FROM comprobante_pago WHERE id_pago = " + idPago + ";";
    try {
      comprobantes = sesion.createSQLQuery(consulta).addEntity(ComprobantePago.class).list();
    } catch (HibernateException he) {
      Logs.log.error(consulta);
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return comprobantes;
  }
  
   private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }
   
}
