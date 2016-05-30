/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.EstatusInformativoDAO;
import dto.EstatusInformativo;
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
public class EstatusInformativoIMPL implements EstatusInformativoDAO {

  @Override
  public List<EstatusInformativo> buscarTodos() {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<EstatusInformativo> ei;
    String consulta = "SELECT * FROM estatus_informativo;";
    try {
      ei = sesion.createSQLQuery(consulta).addEntity(EstatusInformativo.class).list();
    } catch (HibernateException he) {
      ei = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return ei;
  }

  @Override
  public boolean insertar(EstatusInformativo estatus) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.save(estatus);
      tx.commit();
      Logs.log.info("Se insertó un nuevo estatus informativo: " + estatus.getEstatus());
      ok = true;
    } catch (HibernateException he) {
      ok = false;
      if (tx != null) {
        tx.rollback();
      }
      Logs.log.error("No se pudo insertar estatus");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return ok;
  }

  @Override
  public EstatusInformativo buscar(int idEstatus) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    EstatusInformativo ei = new EstatusInformativo();
    String consulta = "SELECT * FROM estatus_informativo WHERE id_estatus_informativo = " + idEstatus + ";";
    try {
      ei = (EstatusInformativo) sesion.createSQLQuery(consulta).addEntity(EstatusInformativo.class).uniqueResult();
    } catch (HibernateException he) {
      ei = null;
      Logs.log.error(consulta);
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return ei;
  }
  
  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}
