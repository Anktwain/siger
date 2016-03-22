/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.DondeGestionDAO;
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
public class DondeGestionIMPL implements DondeGestionDAO{
  
  @Override
  public List<DondeGestion> buscarTodo() {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<DondeGestion> dondes;
    try {
      dondes = sesion.createSQLQuery("SELECT * FROM donde_gestion;").addEntity(DondeGestion.class).list();
    } catch (HibernateException he) {
      dondes = null;
      Logs.log.error("No se pudo hacer la consulta");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return dondes;
  }
  
  @Override
  public List<DondeGestion> buscarPorTipoGestion(int idTipoGestion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<DondeGestion> dondes;
    try {
      dondes = sesion.createSQLQuery("SELECT * FROM donde_gestion WHERE id_tipo_gestion = " + idTipoGestion + ";").addEntity(DondeGestion.class).list();
    } catch (HibernateException he) {
      dondes = null;
      Logs.log.error("No se pudo hacer la consulta");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return dondes;
  }

  @Override
  public DondeGestion buscarPorId(int idDondeGestion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    DondeGestion donde;
    try {
      donde = (DondeGestion) sesion.createSQLQuery("SELECT * FROM donde_gestion WHERE id_donde_gestion = " + idDondeGestion + ";").addEntity(DondeGestion.class).uniqueResult();
    } catch (HibernateException he) {
      donde = null;
      Logs.log.error("No se pudo hacer la consulta");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return donde;
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}
