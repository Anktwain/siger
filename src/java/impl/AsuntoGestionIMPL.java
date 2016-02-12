/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.AsuntoGestionDAO;
import dto.AsuntoGestion;
import dto.DondeGestion;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
public class AsuntoGestionIMPL implements AsuntoGestionDAO{

  @Override
  public List<AsuntoGestion> buscarTodo() {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<AsuntoGestion> asuntos;
    try {
      asuntos = sesion.createSQLQuery("SELECT * FROM asunto_gestion;").addEntity(AsuntoGestion.class).list();
    } catch (HibernateException he) {
      asuntos = null;
      Logs.log.error("No se pudo hacer la consulta");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return asuntos;
  }

  @Override
  public List<AsuntoGestion> buscarPorTipoGestion(int idTipoGestion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<AsuntoGestion> asuntos;
    try {
      asuntos = sesion.createSQLQuery("SELECT * FROM asunto_gestion WHERE id_tipo_gestion = " + idTipoGestion + ";").addEntity(AsuntoGestion.class).list();
    } catch (HibernateException he) {
      asuntos = null;
      Logs.log.error("No se pudo hacer la consulta");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return asuntos;

  }

  @Override
  public AsuntoGestion buscarPorId(int idAsunto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    AsuntoGestion asunto;
    try {
      asunto = (AsuntoGestion) sesion.createSQLQuery("SELECT * FROM asunto_gestion WHERE id_asunto_gestion = " + idAsunto + ";").addEntity(AsuntoGestion.class).uniqueResult();
    } catch (HibernateException he) {
      asunto = null;
      Logs.log.error("No se pudo hacer la consulta");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return asunto;
  }
    
  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}
