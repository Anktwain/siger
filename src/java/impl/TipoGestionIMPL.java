/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.TipoGestionDAO;
import dto.TipoGestion;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
public class TipoGestionIMPL implements TipoGestionDAO {

  @Override
  public List<TipoGestion> buscarTodo() {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<TipoGestion> tipos;
    try {
      tipos = sesion.createSQLQuery("SELECT * FROM tipo_gestion WHERE id_tipo_gestion != 5;").addEntity(TipoGestion.class).list();
    } catch (HibernateException he) {
      tipos = null;
      Logs.log.error("No se pudo hacer la consulta");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return tipos;
  }

  @Override
  public TipoGestion buscarPorId(int idTipoGestion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    TipoGestion tipo;
    try {
      tipo = (TipoGestion) sesion.createSQLQuery("SELECT * FROM tipo_gestion WHERE id_tipo_gestion = " + idTipoGestion + ";").addEntity(TipoGestion.class).uniqueResult();
    } catch (HibernateException he) {
      tipo = null;
      Logs.log.error("No se pudo hacer la consulta");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return tipo;
  }

  @Override
  public List<TipoGestion> buscarParaConvenio() {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<TipoGestion> tipos;
    try {
      tipos = sesion.createSQLQuery("SELECT * FROM tipo_gestion WHERE id_tipo_gestion NOT IN (4, 5);").addEntity(TipoGestion.class).list();
    } catch (HibernateException he) {
      tipos = null;
      Logs.log.error("No se pudo hacer la consulta");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return tipos;
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}
