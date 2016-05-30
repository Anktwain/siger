/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.DespachoDAO;
import dto.Despacho;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import util.HibernateUtil;
import util.log.Logs;

/**
 *
 * @author brionvega
 */
public class DespachoIMPL implements DespachoDAO {

  @Override
  public Despacho buscar(String nombreCorto) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Despacho despacho;

    try {
      despacho = (Despacho) sesion.createQuery("from Despacho d where "
              + "d.nombreCorto = '" + nombreCorto + "'").uniqueResult();
    } catch (HibernateException he) {
      despacho = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return despacho;
  }

  @Override
  public Despacho buscarPorId(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Despacho despacho;
    try {
      despacho = (Despacho) sesion.createSQLQuery("SELECT * FROM despacho WHERE id_despacho = " + idDespacho + ";").addEntity(Despacho.class).uniqueResult();
    } catch (HibernateException he) {
      despacho = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return despacho;
  }
  
  @Override
  public List<Despacho> getAll() {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Despacho> listaDeudors;
    String hql = "from Despacho";

    try { // Buscamos a todos los usuarios que no hayan sido eliminados, un usuario eliminado tiene perfil = 0.
      Query query = sesion.createQuery(hql);
      listaDeudors = (List<Despacho>) query.list();
    } catch (HibernateException he) {
      listaDeudors = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }

    return listaDeudors;
  }
  
  
  
  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }
}
