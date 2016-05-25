/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.MotivoDevolucionDAO;
import dto.MotivoDevolucion;
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
public class MotivoDevolucionIMPL implements MotivoDevolucionDAO {

  @Override
  public List<MotivoDevolucion> obtenerMotivosPorConcepto(int idConcepto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<MotivoDevolucion> md = new ArrayList();
    String consulta = "SELECT * FROM motivo_devolucion WHERE id_concepto_devolucion = " + idConcepto + ";";
    try {
      md = sesion.createSQLQuery(consulta).addEntity(MotivoDevolucion.class).list();
    } catch (HibernateException he) {
      md = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return md;
  }

  @Override
  public MotivoDevolucion buscarPorId(int idMotivo) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    MotivoDevolucion md;
    String consulta = "SELECT * FROM motivo_devolucion WHERE id_motivo_devolucion = " + idMotivo + ";";
    try {
      md = (MotivoDevolucion) sesion.createSQLQuery(consulta).addEntity(MotivoDevolucion.class).uniqueResult();
    } catch (HibernateException he) {
      md = null;
      Logs.log.error(he.getMessage());
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
