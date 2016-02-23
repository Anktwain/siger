/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.*;
import dto.*;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.log.Logs;

/**
 *
 * @author brionvega
 */
public class RegionIMPL implements RegionDAO {

  @Override
  public Region insertar(Region region) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    try {
      sesion.save(region);
      tx.commit();
      //log.info("Se insertó un nuevo usuaario");
    } catch (HibernateException he) {
      region = null;
      if (tx != null) {
        tx.rollback();
      }
      he.printStackTrace();
      //         log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return region;
  }

  @Override
  public List<Region> buscarPorZona(int idZona) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Region> regiones = new ArrayList();
    String consulta = "SELECT * FROM region WHERE id_zona = " + idZona + ";";
    try {
      regiones = sesion.createSQLQuery(consulta).addEntity(Region.class).list();
      Logs.log.info("Se ejecutó query: " + consulta);
    } catch (HibernateException he) {
      regiones = null;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return regiones;
  }

  @Override
  public List<Integer> buscarMunicipiosRegion(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Integer> idsMunicipio = new ArrayList<>();
    String consulta = "SELECT id_municipio FROM region WHERE id_zona IN (SELECT id_zona FROM zona WHERE id_despacho = " + idDespacho + ");";
    try {
      idsMunicipio = sesion.createSQLQuery(consulta).list();
      Logs.log.info("Se ejecutó query: " + consulta);
    } catch (HibernateException he) {
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return idsMunicipio;
  }

  @Override
  public List<Municipio> buscarMunicipiosZona(int idZona) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Municipio> municipios = new ArrayList();
    List<Integer> muns;
    String consulta = "SELECT id_municipio FROM region WHERE id_zona = " + idZona + ";";
    try {
      muns = sesion.createSQLQuery(consulta).list();
      for (int i = 0; i < (muns.size()); i++) {
        MunicipioDAO mdao = new MunicipioIMPL();
        Municipio m = mdao.buscarPorId(muns.get(i));
        municipios.add(m);
      }
      Logs.log.info("Se ejecutó query: " + consulta);
    } catch (HibernateException he) {
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return municipios;
  }

  @Override
  public List<EstadoRepublica> buscarEstadosZona(int idZona) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<EstadoRepublica> estados = new ArrayList();
    List<Integer> edos;
    String consulta = "SELECT DISTINCT id_estado FROM region WHERE id_zona = " + idZona + ";";
    try {
      edos = sesion.createSQLQuery(consulta).list();
      for (int i = 0; i < (edos.size()); i++) {
        EstadoRepublicaDAO edao = new EstadoRepublicaIMPL();
        EstadoRepublica e = edao.buscarPorId(edos.get(i));
        estados.add(e);
      }
      Logs.log.info("Se ejecutó query: " + consulta);
    } catch (HibernateException he) {
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return estados;
  }

  @Override
  public boolean eliminar(Region region) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.delete(region);
      tx.commit();
      ok = true;
    } catch (HibernateException he) {
      ok = false;
      if (tx != null) {
        tx.rollback();
      }
      he.printStackTrace();
    } finally {
      cerrar(sesion);
    }
    return ok;
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }
}
