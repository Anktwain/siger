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
import util.constantes.Impresiones;
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
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Impresion> impresiones = new ArrayList();
    String consulta = "SELECT * FROM impresion WHERE id_credito IN (SELECT id_credito FROM credito WHERE id_despacho = " + idDespacho + ") AND tipo_impresion = " + tipoImpresion + ";";
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

  @Override
  public Number calcularVisitasDomiciliariasPorDespacho(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Number visitas;
    String consulta = "SELECT COUNT(*) FROM impresion WHERE id_impresion NOT IN (SELECT id_impresion FROM detalle_visita) AND id_credito IN (SELECT id_credito FROM credito WHERE id_despacho = " + idDespacho + ") AND tipo_impresion = " + Impresiones.VISITA_DOMICILIARIA + ";";
    try {
      visitas = (Number) sesion.createSQLQuery(consulta).uniqueResult();
    } catch (HibernateException he) {
      visitas = -1;
      Logs.log.error(consulta);
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return visitas;
  }

  @Override
  public Number calcularVisitasDomiciliariasPorGestor(int idDespacho, int idGestor) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Number visitas;
    String consulta = "SELECT COUNT(*) FROM impresion WHERE id_impresion NOT IN (SELECT id_impresion FROM detalle_visita) AND id_credito IN (SELECT id_credito FROM credito WHERE id_despacho = " + idDespacho + " AND id_gestor = " + idGestor + ") AND tipo_impresion = " + Impresiones.VISITA_DOMICILIARIA + ";";
    try {
      visitas = (Number) sesion.createSQLQuery(consulta).uniqueResult();
    } catch (HibernateException he) {
      visitas = -1;
      Logs.log.error(consulta);
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return visitas;
  }

  @Override
  public Number calcularVisitasPorEstado(int idDespacho, int idEstado) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Number visitas;
    String consulta = "SELECT COUNT(*) FROM impresion WHERE id_credito IN (SELECT id_credito FROM credito WHERE id_despacho = " + idDespacho + ") AND id_direccion IN (SELECT id_direccion FROM direccion WHERE id_estado = " + idEstado + ") AND tipo_impresion = " + Impresiones.VISITA_DOMICILIARIA + ";";
    try {
      visitas = (Number) sesion.createSQLQuery(consulta).uniqueResult();
    } catch (HibernateException he) {
      visitas = -1;
      Logs.log.error(consulta);
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return visitas;
  }

  @Override
  public Number calcularCorreoPorEstado(int idDespacho, int idEstado) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Number visitas;
    String consulta = "SELECT COUNT(*) FROM impresion WHERE id_credito IN (SELECT id_credito FROM credito WHERE id_despacho = " + idDespacho + ") AND id_direccion IN (SELECT id_direccion FROM direccion WHERE id_estado = " + idEstado + ") AND tipo_impresion = " + Impresiones.CORREO_ORDINARIO + ";";
    try {
      visitas = (Number) sesion.createSQLQuery(consulta).uniqueResult();
    } catch (HibernateException he) {
      visitas = -1;
      Logs.log.error(consulta);
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return visitas;
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}
