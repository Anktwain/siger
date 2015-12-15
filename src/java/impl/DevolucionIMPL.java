/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.DevolucionDAO;
import dto.Devolucion;
import dto.tablas.Devolucions;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.constantes.Devoluciones;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
public class DevolucionIMPL implements DevolucionDAO {

  @Override
  public List<Devolucions> retiradosBancoPorDespacho(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Devolucions> retirados = new ArrayList<>();
    List<Object[]> r;
    String consulta = "SELECT CAST(d.fecha AS DATE) AS fecha, d.estatus, c.numero_credito, s.nombre_razon_social, cd.concepto FROM deudor de JOIN sujeto s JOIN credito c JOIN devolucion d JOIN concepto_devolucion cd WHERE de.id_deudor = c.id_deudor AND d.estatus = " + Devoluciones.PENDIENTE +" AND cd.id_concepto_devolucion = 11 AND c.id_credito = d.id_credito AND cd.id_concepto_devolucion = d.id_concepto_devolucion AND de.id_sujeto = s.id_sujeto AND c.id_despacho = " + idDespacho + ";";
    try {
      r = sesion.createSQLQuery(consulta).list();
      for (Object[] row : r) {
        Devolucions d = new Devolucions();
        try {
          SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
          Date fecha = formatter.parse(row[0].toString());
          d.setFecha(fecha);
        } catch (ParseException ex) {
          ex.printStackTrace();
        }
        d.setConcepto(row[4].toString());
        d.setEstatus("Pendiente");
        d.setNombreRazonSocial(row[3].toString());
        d.setNumeroCredito(row[2].toString());
        retirados.add(d);
      }
      Logs.log.info("Se ejecutó query: " + consulta);
    } catch (HibernateException he) {
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return retirados;
  }

  @Override
  public List<Devolucions> bandejaDevolucionPorDespacho(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Devolucions> bandeja = new ArrayList<>();
    List<Object[]> b;
    String consulta = "SELECT CAST(d.fecha AS DATE) AS fecha, d.estatus, c.numero_credito, s.nombre_razon_social, cd.concepto, d.solicitante FROM deudor de JOIN sujeto s JOIN credito c JOIN devolucion d JOIN concepto_devolucion cd WHERE de.id_deudor = c.id_deudor AND ((d.estatus = " + Devoluciones.ESPERA_CONSERVACION + ") OR (d.estatus = " + Devoluciones.PENDIENTE + " AND d.id_concepto_devolucion != 11)) AND c.id_credito = d.id_credito AND cd.id_concepto_devolucion = d.id_concepto_devolucion AND de.id_sujeto = s.id_sujeto AND c.id_despacho = " + idDespacho +";";
    try {
      b = sesion.createSQLQuery(consulta).list();
      for (Object[] row : b) {
        Devolucions d = new Devolucions();
        try {
          SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
          Date fecha = formatter.parse(row[0].toString());
          d.setFecha(fecha);
        } catch (ParseException ex) {
          ex.printStackTrace();
        }
        d.setConcepto(row[4].toString());
        String status = row[1].toString();
        if(status.equals("2")){
          status = "Pendiente";
        }
        else{
          status = "Espera conservacion";
        }
        d.setEstatus(status);
        d.setNombreRazonSocial(row[3].toString());
        d.setNumeroCredito(row[2].toString());
        d.setSolicitante(row[5].toString());
        bandeja.add(d);
      }
      Logs.log.info("Se ejecutó query: " + consulta);
    } catch (HibernateException he) {
      bandeja = null;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return bandeja;
  }

  @Override
  public List<Devolucions> devueltosPorDespacho(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    List<Devolucions> devueltos = new ArrayList<>();
    List<Object[]> de;
    String consulta = "SELECT CAST(d.fecha AS DATE) AS fecha, d.estatus, c.numero_credito, s.nombre_razon_social, cd.concepto, d.solicitante, d.revisor FROM deudor de JOIN sujeto s JOIN credito c JOIN devolucion d JOIN concepto_devolucion cd WHERE de.id_deudor = c.id_deudor AND d.estatus = " + Devoluciones.DEVUELTO + " AND c.id_credito = d.id_credito AND cd.id_concepto_devolucion = d.id_concepto_devolucion AND de.id_sujeto = s.id_sujeto AND c.id_despacho = " + idDespacho + ";";
    try {
      de = sesion.createSQLQuery(consulta).list();
      for (Object[] row : de) {
        Devolucions d = new Devolucions();
        try {
          SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
          Date fecha = new Date();
                  formatter.parse(row[0].toString());
          d.setFecha(fecha);
        } catch (ParseException ex) {
          ex.printStackTrace();
        }
        d.setConcepto(row[4].toString());
        d.setEstatus("Devuelto");
        d.setNombreRazonSocial(row[3].toString());
        d.setNumeroCredito(row[2].toString());
        d.setSolicitante(row[5].toString());
        d.setRevisor(row[6].toString());
        devueltos.add(d);
      }
      Logs.log.info("Se ejecutó query: " + consulta);
    } catch (HibernateException he) {
      devueltos = null;
      Logs.log.error(he.getStackTrace());
    } finally {
      cerrar(sesion);
    }
    return devueltos;
  }

  @Override
  public boolean insertar(Devolucion devolucion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.save(devolucion);
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
  public boolean editar(Devolucion devolucion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.update(devolucion);
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
  public boolean eliminar(Devolucion devolucion) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    boolean ok;
    try {
      sesion.delete(devolucion);
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
  public Devolucion buscarDevolucionPorNumeroCredito(String numeroCredito) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        Devolucion dev;
        try { 
            dev = (Devolucion) sesion.createSQLQuery("select * from devolucion where id_credito = (SELECT id_credito FROM credito WHERE numero_credito = '" + numeroCredito + "');").addEntity(Devolucion.class).uniqueResult();
        } catch(HibernateException he) {
            dev = null;
            he.printStackTrace();
        } finally {
            cerrar(sesion);
        }
        return dev;
    }
  
  private void cerrar(Session sesion) {
    if (sesion.isOpen()) {
      sesion.close();
    }
  }

}