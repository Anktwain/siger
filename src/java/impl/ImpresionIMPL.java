/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.ImpresionDAO;
import dto.Impresion;
import java.util.ArrayList;
import java.util.Date;
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
public class ImpresionIMPL implements ImpresionDAO {

  @Override
  public boolean insertar(Impresion impresion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.save(impresion);
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

  @Override
  public boolean editar(Impresion impresion) {
    throw new UnsupportedOperationException("Este metodo aun no ha sido implementado");
    /*
     To change body of generated methods, choose Tools | Templates | Java | Code Snippets | Generated Method body
     */
  }

  @Override
  public List<Impresion> buscarPorCredito(int idCredito) {
    throw new UnsupportedOperationException("Este metodo aun no ha sido implementado");
    /*
     To change body of generated methods, choose Tools | Templates | Java | Code Snippets | Generated Method body
     */
  }

  @Override
  public List<Impresion> buscarPorBloque(int idBloque) {
    throw new UnsupportedOperationException("Este metodo aun no ha sido implementado");
    /*
     To change body of generated methods, choose Tools | Templates | Java | Code Snippets | Generated Method body
     */
  }

  @Override
  public List<Impresion> buscarPorFechaImpresion(Date fechaImpresion) {
    throw new UnsupportedOperationException("Este metodo aun no ha sido implementado");
    /*
     To change body of generated methods, choose Tools | Templates | Java | Code Snippets | Generated Method body
     */
  }

  @Override
  public List<Impresion> buscarPorTipoImpresion(int tipoImpresion, int idDespacho) {
    throw new UnsupportedOperationException("Este metodo aun no ha sido implementado");
    /*
     To change body of generated methods, choose Tools | Templates | Java | Code Snippets | Generated Method body
     */
  }

  @Override
  public List<Impresion> buscarImpresionesAreaMetropolitana(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    int[] municipios = {274, 266, 278, 267, 268, 279, 269, 270, 271, 272, 280, 273, 275, 276, 281, 277, 669, 676, 680, 777, 681, 685, 687, 689, 693, 695, 713, 714, 716, 726, 737, 760, 765, 778};
    List<Impresion> impresiones = new ArrayList();
    for (int i = 0; i < (municipios.length); i++) {
      System.out.println("VERIFICACION PARA EL MUNICIPIO ID: " + municipios[i]);
      List<Impresion> aux = new ArrayList();
      String consulta = "SELECT * FROM impresion WHERE id_credito IN (SELECT id_credito FROM credito WHERE id_deudor IN(SELECT id_deudor FROM deudor WHERE id_sujeto IN(SELECT id_sujeto FROM direccion WHERE id_municipio = " + municipios[i] + ")AND id_despacho = " + idDespacho + "));";
      try {
        aux = sesion.createSQLQuery(consulta).addEntity(Impresion.class).list();
      } catch (HibernateException he) {
        Logs.log.error(consulta);
        Logs.log.error(he.getStackTrace());
      } finally {
        if (!impresiones.isEmpty()) {
          impresiones.addAll(aux);
        }
        cerrar(sesion);
      }
    }
    return impresiones;
  }

  @Override
  public List<Impresion> buscarImpresionesInteriorRepublica(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Impresion> impresiones = new ArrayList();
    String consulta = "SELECT * FROM impresion WHERE id_credito IN (SELECT id_credito FROM credito WHERE id_deudor IN(SELECT id_deudor FROM deudor WHERE id_sujeto IN(SELECT id_sujeto FROM direccion WHERE id_municipio NOT IN (274, 266, 278, 267, 268, 279, 269, 270, 271, 272, 280, 273, 275, 276, 281, 277, 669, 676, 680, 777, 681, 685, 687, 689, 693, 695, 713, 714, 716, 726, 737, 760, 765, 778))AND id_despacho = 2));";
    try {
      impresiones = sesion.createSQLQuery(consulta).addEntity(Impresion.class).list();
    } catch (HibernateException he) {
      Logs.log.error(consulta);
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return impresiones;
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}
