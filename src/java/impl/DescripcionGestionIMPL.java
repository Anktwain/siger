/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.DescripcionGestionDAO;
import dto.DescripcionGestion;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
public class DescripcionGestionIMPL implements DescripcionGestionDAO{

  @Override
  public List<DescripcionGestion> buscarPorAsuntoTipo(int idTipoGestion, int idAsunto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<DescripcionGestion> descripciones;
    try {
      descripciones = sesion.createSQLQuery("SELECT * FROM descripcion_gestion WHERE id_asunto_gestion  = " + idAsunto + " AND id_asunto_gestion IN (SELECT id_asunto_gestion FROM asunto_gestion WHERE id_tipo_gestion = " + idTipoGestion + ");").addEntity(DescripcionGestion.class).list();
    } catch (HibernateException he) {
      descripciones = null;
      Logs.log.error("No se pudo hacer la consulta");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return descripciones;
  }

  @Override
  public DescripcionGestion buscarPorId(int idDescripcionGestion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    DescripcionGestion descripcion;
    try {
      descripcion = (DescripcionGestion) sesion.createSQLQuery("SELECT * FROM descripcion_gestion WHERE id_descripcion_gestion = " + idDescripcionGestion + ";").addEntity(DescripcionGestion.class).uniqueResult();
    } catch (HibernateException he) {
      descripcion = null;
      Logs.log.error("No se pudo hacer la consulta");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return descripcion;
  }

  @Override
  public DescripcionGestion buscarPorAbreviatura(String abreviatura) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    DescripcionGestion descripcion;
    try {
      descripcion = (DescripcionGestion) sesion.createSQLQuery("SELECT * FROM descripcion_gestion WHERE abreviatura = '" + abreviatura + "';").addEntity(DescripcionGestion.class).uniqueResult();
    } catch (HibernateException he) {
      descripcion = null;
      Logs.log.error("No se pudo hacer la consulta");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return descripcion;
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}
