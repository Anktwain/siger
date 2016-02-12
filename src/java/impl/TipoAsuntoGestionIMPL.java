/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.TipoAsuntoGestionDAO;
import dto.TipoAsuntoGestion;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
public class TipoAsuntoGestionIMPL implements TipoAsuntoGestionDAO{

  @Override
  public TipoAsuntoGestion buscarPorAsunto(int idTipoAsunto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    TipoAsuntoGestion tipoAsunto;
    try {
      tipoAsunto = (TipoAsuntoGestion) sesion.createSQLQuery("SELECT * FROM tipo_asunto_gestion WHERE id_tipo_asunto_gestion = " + idTipoAsunto + ";").addEntity(TipoAsuntoGestion.class).uniqueResult();
    } catch (HibernateException he) {
      tipoAsunto = null;
      Logs.log.error("No se pudo hacer la consulta");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return tipoAsunto;
  }
  
  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}
