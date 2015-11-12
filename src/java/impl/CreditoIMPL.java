/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.CreditoDAO;
import dto.Cred;
import dto.Credito;
import java.util.ArrayList;
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
public class CreditoIMPL implements CreditoDAO {

  @Override
  public Number contarCreditosActivos(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    Number creditos;
    String consulta = "SELECT COUNT(*) FROM credito_remesa WHERE id_remesa IN (SELECT MAX(id_remesa) FROM remesa) AND id_credito IN (SELECT id_credito FROM credito where id_despacho = " + idDespacho + ");";
    try {
      creditos = (Number) sesion.createSQLQuery(consulta).uniqueResult();
      Logs.log.info("Se ejecutó query: " + consulta);
    } catch (HibernateException he) {
      creditos = -1;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return creditos;
  }

  @Override
  public Number contarCreditosActivosPorGestor(int idUsuario) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    Number creditos;
    String consulta = "SELECT COUNT(*) FROM credito_remesa WHERE id_remesa = (SELECT MAX(id_remesa) FROM remesa) AND id_credito IN (select id_credito FROM credito WHERE id_gestor IN (SELECT id_gestor FROM gestor WHERE id_usuario = " + idUsuario + "));";
    try {
      creditos = (Number) sesion.createSQLQuery(consulta).uniqueResult();
      Logs.log.info("Se ejecutó query: " + consulta);
    } catch (HibernateException he) {
      creditos = -1;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return creditos;
  }

  @Override
  public Credito buscarCreditoPorId(int idCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Credito c;
    String consulta = "SELECT * FROM credito WHERE id_credito = " + idCredito + ";";
    try {
      c = (Credito) sesion.createSQLQuery(consulta).addEntity(Credito.class).uniqueResult();
      Logs.log.info("Se ejecutó query: " + consulta);
    } catch (HibernateException he) {
      c = null;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return c;
  }

  @Override
  public List<Cred> creditosEnGestionPorDespacho(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Cred> creditos = new ArrayList<>();
    List<Object[]> c;
    String consulta = "SELECT c.numero_credito, s.nombre_razon_social, i.nombre_corto, c.tipo_credito, p.nombre, c.monto, u.nombre_login  FROM credito c JOIN deudor d JOIN sujeto s JOIN despacho des JOIN institucion i JOIN producto p JOIN gestor g JOIN usuario u WHERE d.id_sujeto = s.id_sujeto AND u.id_usuario = g.id_usuario AND g.id_gestor = c.id_gestor AND p.id_producto = c.id_producto AND d.id_deudor = c.id_deudor AND i.id_institucion = c.id_institucion AND des.id_despacho = " + idDespacho + ";";
    try {
      c = sesion.createSQLQuery(consulta).list();
      for (Object[] row : c) {
        Cred cr = new Cred();
        cr.setNumeroCredito(row[0].toString());
        cr.setNombreRazonSocial(row[1].toString());
        cr.setNombreCortoInstitucion(row[2].toString());
        // CUANDO SE DEFINAN LOS TIPOS DE CREDITOS, QUITAR LA ASIGNACION DIRECTA E IMPLEMENTAR UN SWITCH
        cr.setTipoCredito("Linea Telefonica");
        cr.setNombreProducto(row[4].toString());
        cr.setSaldoVencido(Float.parseFloat(row[5].toString()));
        cr.setGestorAsignado(row[6].toString());
        creditos.add(cr);
      }
      Logs.log.info("Se ejecutó query: " + consulta);
    } catch (HibernateException he) {
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return creditos;
  }

  @Override
  public int obtenerIdDelCredito(String numeroCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    int id;
    String consulta = "SELECT id_credito FROM credito WHERE numero_credito = '" + numeroCredito + "';";
    try {
      id = (int) sesion.createSQLQuery(consulta).uniqueResult();
      Logs.log.info("Se ejecutó query: " + consulta);
    } catch (HibernateException he) {
      id = -1;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return id;
  }

  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}
