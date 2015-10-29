/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.DevolucionDAO;
import beans.DevolucionBean.Dev;
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
public class DevolucionIMPL implements DevolucionDAO{

  @Override
  public List<Dev> retiradosBancoPorDespacho(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    List<Dev> retirados;
    String consulta = "SELECT CAST(d.fecha AS DATE) AS fecha, d.estatus, c.numero_credito, s.nombre_razon_social, cd.concepto FROM deudor de JOIN sujeto s JOIN credito c JOIN devolucion d JOIN concepto_devolucion cd WHERE de.id_deudor = c.id_deudor AND (d.estatus = 2 OR d.estatus = 3) AND c.id_credito = d.id_credito AND cd.id_concepto_devolucion = d.id_concepto_devolucion AND de.id_sujeto = s.id_sujeto AND c.id_despacho = " + idDespacho + " ;";
    try {
      retirados = (List<Dev>) sesion.createSQLQuery(consulta).addEntity(Dev.class).list();
      Logs.log.info("Se ejecutó query: " + consulta);
    } catch (HibernateException he) {
      retirados = null;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return retirados;
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

  @Override
  public boolean aprobarDevolucion() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean rechazarDevolucion() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
