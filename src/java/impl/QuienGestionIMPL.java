/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.QuienGestionDAO;
import dto.QuienGestion;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
public class QuienGestionIMPL implements QuienGestionDAO{

  @Override
  public List<QuienGestion> buscarTodo() {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<QuienGestion> tipos;
    try {
      tipos = sesion.createSQLQuery("SELECT * FROM quien_gestion;").addEntity(QuienGestion.class).list();
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
  public List<QuienGestion> buscarPorTipoQuien(int idTipoQuien) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<QuienGestion> tipos;
    try {
      tipos = sesion.createSQLQuery("SELECT * FROM quien_gestion WHERE id_tipo_quien_gestion = " + idTipoQuien + ";").addEntity(QuienGestion.class).list();
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
  public QuienGestion buscarPorId(int idQuienGestion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    QuienGestion tipo;
    try {
      tipo = (QuienGestion) sesion.createSQLQuery("SELECT * FROM quien_gestion WHERE id_quien_gestion = " + idQuienGestion + ";").addEntity(QuienGestion.class).uniqueResult();
    } catch (HibernateException he) {
      tipo = null;
      Logs.log.error("No se pudo hacer la consulta");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return tipo;
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}
