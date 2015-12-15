/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.MotivoDevolucionDAO;
import dto.ConceptoDevolucion;
import dto.MotivoDevolucion;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
public class MotivoDevolucionIMPL implements MotivoDevolucionDAO{
  
  @Override
  public List<MotivoDevolucion> obtenerMotivosPorConcepto(ConceptoDevolucion concepto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<MotivoDevolucion> md;
    int id = concepto.getIdConceptoDevolucion();
    String consulta = "SELECT * FROM motivo_devolucion WHERE id_concepto_devolucion = " + id + ";";
    try {
      md = sesion.createSQLQuery(consulta).addEntity(MotivoDevolucion.class).list();
      Logs.log.info("Se ejecutó query: " + consulta);
    } catch (HibernateException he) {
      md = null;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return md;
  }
  
  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }
  
}
