/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.ConceptoDevolucionDAO;
import dto.ConceptoDevolucion;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;
import util.log.Logs;
/**
 *
 * @author Eduardo
 */
public class ConceptoDevolucionIMPL implements ConceptoDevolucionDAO{

  @Override
  public List<ConceptoDevolucion> obtenerConceptos() {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<ConceptoDevolucion> cd;
    String consulta = "SELECT * FROM concepto_devolucion;";
    try {
      cd = sesion.createSQLQuery(consulta).addEntity(ConceptoDevolucion.class).list();
    } catch (HibernateException he) {
      cd = null;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return cd;
  }

  @Override
  public ConceptoDevolucion buscarPorId(int idConcepto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    ConceptoDevolucion cd;
    String consulta = "SELECT * FROM concepto_devolucion WHERE id_concepto_devolucion = " + idConcepto + ";";
    try {
      cd = (ConceptoDevolucion) sesion.createSQLQuery(consulta).addEntity(ConceptoDevolucion.class).uniqueResult();
    } catch (HibernateException he) {
      cd = null;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return cd;
  }
  
  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }
  
}
