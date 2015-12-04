/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.ConvenioPagoDAO;
import dto.ConvenioPago;
import dto.Historial;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
public class ConvenioPagoIMPL implements ConvenioPagoDAO {

  @Override
  public List<ConvenioPago> buscarConveniosPorCredito(int idCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<ConvenioPago> convenios;
    try {
      convenios = sesion.createSQLQuery("SELECT * FROM convenio_pago WHERE id_credito = " + idCredito + ";").addEntity(ConvenioPago.class).list();
    } catch (HibernateException he) {
      convenios = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return convenios;
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}
