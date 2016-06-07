/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.AjusteDAO;
import dto.Ajuste;
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
public class AjusteIMPL implements AjusteDAO{

  @Override
  public List<Ajuste> buscarAjustesPorCredito(int idCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Ajuste> ajustes = new ArrayList();
    String consulta = "SELECT DISTINCT * FROM ajuste WHERE id_actualizacion IN (SELECT id_actualizacion FROM actualizacion WHERE id_credito = " + idCredito + ");";
    try {
      ajustes = sesion.createSQLQuery(consulta).addEntity(Ajuste.class).list();
    } catch (HibernateException he) {
      Logs.log.error(consulta);
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return ajustes;
  }
  
  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }
  
}
