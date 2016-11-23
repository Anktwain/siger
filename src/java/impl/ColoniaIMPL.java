/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.ColoniaDAO;
import dto.Colonia;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.log.Logs;

/**
 *
 * @author antonio
 */
public class ColoniaIMPL implements ColoniaDAO {

  @Override
  public List<Colonia> buscarColoniasPorMunicipio(int idMunicipio) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Colonia> colonias;
    String consulta = "SELECT * FROM colonia WHERE id_municipio = " + idMunicipio + " ORDER BY nombre ASC;";
    try {
      colonias = sesion.createSQLQuery(consulta).addEntity(Colonia.class).list();
    } catch (HibernateException he) {
      colonias = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return colonias;
  }

  @Override
  public Colonia buscar(int idColonia) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Colonia colonia;
    try {
      colonia = (Colonia) sesion.get(Colonia.class, idColonia);
    } catch (HibernateException he) {
      colonia = null;
      Logs.log.error("No se pudo ontener lista de objetos: Colonia");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return colonia;
  }

  @Override
  public List<Colonia> buscar(String cadena) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Colonia> colonias;
    String consulta = "SELECT * FROM colonia WHERE nombre LIKE '%" + cadena + "%' ORDER BY nombre ASC;";
    try {
      colonias = sesion.createSQLQuery(consulta).addEntity(Colonia.class).list();
    } catch (HibernateException he) {
      colonias = null;
      Logs.log.error("No se pudo obtener objeto: Colonia");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return colonias;
  }

  @Override
  public Colonia buscar(String cadena, String cp) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Colonia colonia;
    try {
      colonia = (Colonia) sesion.createSQLQuery("SELECT * from colonia WHERE nombre LIKE '%" + cadena + "%' AND codigo_postal = '" + cp + "';").addEntity(Colonia.class).uniqueResult();
    } catch (HibernateException he) {
      colonia = null;
      Logs.log.error("No se pudo obtener objeto: Colonia");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return colonia;
  }

  @Override
  public List<Colonia> buscarPorCodigoPostal(String cp) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Colonia> colonias;
    String consulta = "SELECT * FROM colonia WHERE codigo_postal = '" + cp + "' ORDER BY nombre ASC;";
    try {
      colonias = sesion.createSQLQuery(consulta).addEntity(Colonia.class).list();
    } catch (HibernateException he) {
      colonias = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return colonias;
  }

  @Override
  public boolean insertar(Colonia colonia) {
    boolean ok = false;
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    try {
      sesion.save(colonia);
      tx.commit();
      ok = true;
    } catch (HibernateException he) {
      if (tx != null) {
        tx.rollback();
      }
      Logs.log.error("No se pudo insertar la colonia " + colonia.getNombre());
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return ok;
  }

  @Override
  public List<String> obtenerTiposColonia() {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<String> asentamientos = new ArrayList();
    String consulta = "SELECT DISTINCT tipo FROM colonia ORDER BY tipo ASC;";
    try {
      asentamientos = sesion.createSQLQuery(consulta).list();
    } catch (HibernateException he) {
      Logs.log.error("No se pudieron obtener los tipos de colonia.");
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return asentamientos;
  }

  @Override
  public List<Colonia> busquedaEspecialColonias(String consulta) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Colonia> colonias = new ArrayList();
    try {
      colonias = sesion.createSQLQuery(consulta).addEntity(Colonia.class).list();
    } catch (HibernateException he) {
      Logs.log.error(consulta);
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return colonias;
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}
