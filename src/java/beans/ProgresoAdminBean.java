/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import util.constantes.Devoluciones;
import util.constantes.Marcajes;
import util.constantes.Pagos;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "progresoAdminBean")
@ViewScoped

public class ProgresoAdminBean implements Serializable {

  private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
  private final String DB_URL = "jdbc:mysql://127.0.0.1:3306/sigerbd";
  private final String USER = "root";
  private final String PASS = "root";
  private String creditosActivos;
  private String visitasDomiciliarias;
  private String pagosPorAprobar;
  private String porcentajeRecuperacion;
  private String creditosQuebrantoPermanencia;
  private String impresiones;
  private String montoPagosPorAprobar;
  private String montoRecuperacionQuincena;
  private String montoPagosHoy;
  private String gestionesHoy;
  private String gestorDelDiaPagos;
  private String gestorDelDiaGestiones;

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  // CONSTRUCTOR
  public ProgresoAdminBean() {
    creditosActivos = "";
    visitasDomiciliarias = "";
    pagosPorAprobar = "";
    porcentajeRecuperacion = "";
    creditosQuebrantoPermanencia = "";
    impresiones = "";
    montoPagosPorAprobar = "";
    montoRecuperacionQuincena = "";
    montoPagosHoy = "";
    gestionesHoy = "";
    gestorDelDiaPagos = "";
    gestorDelDiaGestiones = "";
    obtenerCalculos();
  }

  // METODO QUE OBTIENE LOS CALCULOS
  public final void obtenerCalculos() {
    Connection conn = null;
    Statement stmt = null;
    String consulta = "";
    try {
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      stmt = conn.createStatement();
      // CONSULTA DE CREDITOS ACTIVOS
      consulta = "SELECT COUNT(*) AS conteo FROM credito WHERE id_credito NOT IN (SELECT id_credito FROM devolucion WHERE estatus IN (" + Devoluciones.DEVUELTO + ", " + Devoluciones.PENDIENTE + ")) AND id_despacho = " + indexBean.getUsuario().getDespacho().getIdDespacho() + ";";
      try (ResultSet rs = stmt.executeQuery(consulta)) {
        while (rs.next()) {
          creditosActivos = rs.getString("conteo");
        }
      } catch (SQLException sqle) {
        Logs.log.error("No se realizo correctamente la consulta de creditos activos.");
        Logs.log.error(consulta);
        Logs.log.error(sqle.getMessage());
      }
      // CONSULTA DE VISITAS DOMICILIARIAS
      consulta = "SELECT COUNT(*) AS conteo FROM gestion WHERE id_estatus_informativo = 8 AND id_credito IN (SELECT id_credito FROM credito WHERE id_despacho = " + indexBean.getUsuario().getDespacho().getIdDespacho() + ");";
      try (ResultSet rs = stmt.executeQuery(consulta)) {
        while (rs.next()) {
          visitasDomiciliarias = rs.getString("conteo");
        }
      } catch (SQLException sqle) {
        Logs.log.error("No se realizo correctamente la consulta de visitas domiciliarias.");
        Logs.log.error(consulta);
        Logs.log.error(sqle.getMessage());
      }
      // CONSULTA DE PAGOS POR APROBAR
      consulta = "SELECT COUNT(*) AS conteo FROM pago WHERE estatus = " + Pagos.PENDIENTE + " AND id_gestor IN (SELECT id_gestor FROM gestor WHERE id_usuario IN (SELECT id_usuario FROM usuario WHERE id_despacho = " + indexBean.getUsuario().getDespacho().getIdDespacho() + "));";
      try (ResultSet rs = stmt.executeQuery(consulta)) {
        while (rs.next()) {
          pagosPorAprobar = rs.getString("conteo");
        }
      } catch (SQLException sqle) {
        Logs.log.error("No se realizo correctamente la consulta de pagos por aprobar.");
        Logs.log.error(consulta);
        Logs.log.error(sqle.getMessage());
      }
      // CONSULTA DE PORCENTAJE DE RECUPERACION
      consulta = "SELECT ROUND(((SELECT ROUND(SUM(monto_aprobado), 2) FROM pago WHERE id_pago IN (SELECT id_pago FROM promesa_pago WHERE id_promesa_pago IN (SELECT id_promesa_pago FROM convenio_pago WHERE id_credito IN (SELECT id_credito FROM credito WHERE id_credito NOT IN (SELECT id_credito FROM devolucion WHERE estatus = " + Devoluciones.DEVUELTO + ") AND id_despacho = " + indexBean.getUsuario().getDespacho().getIdDespacho() + "))))*100)/(SELECT ROUND(SUM(saldo_vencido), 2) FROM actualizacion WHERE id_credito NOT IN (SELECT id_credito FROM devolucion WHERE estatus = " + Devoluciones.DEVUELTO + ") AND id_credito IN (SELECT id_credito FROM credito WHERE id_despacho = " + indexBean.getUsuario().getDespacho().getIdDespacho() + ")),6) as porcentaje;";
      try (ResultSet rs = stmt.executeQuery(consulta)) {
        while (rs.next()) {
          if (rs.getString("porcentaje") == null) {
            porcentajeRecuperacion = "% 0.0";
          } else {
            porcentajeRecuperacion = "% " + rs.getString("porcentaje");
          }
        }
      } catch (SQLException sqle) {
        Logs.log.error("No se realizo correctamente la consulta de porcentaje de recuperacion.");
        Logs.log.error(consulta);
        Logs.log.error(sqle.getMessage());
        porcentajeRecuperacion = "% NaN";
      }
      // CONSULTA DE CREDITOS DE QUEBRANTO Y PERMANENCIA
      consulta = "SELECT COUNT(*) AS quebrantoPermanencia FROM credito c WHERE id_credito NOT IN (SELECT id_credito FROM devolucion WHERE estatus = " + Devoluciones.DEVUELTO + ") AND c.id_despacho = " + indexBean.getUsuario().getDespacho().getIdDespacho() + " AND id_marcaje IN (" + Marcajes.QUEBRANTO + ", " + Marcajes.PERMANENCIA + ");";
      try (ResultSet rs = stmt.executeQuery(consulta)) {
        while (rs.next()) {
          creditosQuebrantoPermanencia = rs.getString("quebrantoPermanencia");
        }
      }
      // CONSULTA DE IMPRESIONES
      consulta = "SELECT COUNT(*) AS impresiones FROM impresion WHERE id_credito IN (SELECT id_credito FROM credito WHERE id_despacho = " + indexBean.getUsuario().getDespacho().getIdDespacho() + ");";
      try (ResultSet rs = stmt.executeQuery(consulta)) {
        while (rs.next()) {
          impresiones = rs.getString("impresiones");
        }
      } catch (SQLException sqle) {
        Logs.log.error("No se realizo correctamente la consulta de impresiones.");
        Logs.log.error(consulta);
        Logs.log.error(sqle.getMessage());
      }
      // CONSULTA DE PAGOS POR APROBAR
      consulta = "SELECT ROUND(SUM(monto_pago), 2) AS montoPorAprobar FROM pago WHERE id_pago IN (SELECT id_pago FROM promesa_pago WHERE id_promesa_pago IN (SELECT id_promesa_pago FROM convenio_pago WHERE id_credito IN (SELECT id_credito FROM credito WHERE id_despacho = " + indexBean.getUsuario().getDespacho().getIdDespacho() + ") AND id_credito NOT IN (SELECT id_credito FROM devolucion WHERE estatus = " + Devoluciones.DEVUELTO + "))) AND estatus in (" + Pagos.PENDIENTE + ", " + Pagos.REVISION_BANCO + ");";
      try (ResultSet rs = stmt.executeQuery(consulta)) {
        while (rs.next()) {
          if (rs.getString("montoPorAprobar") == null) {
            montoPagosPorAprobar = "$ 0.0";
          } else {
            montoPagosPorAprobar = "$ " + rs.getString("montoPorAprobar");
          }
        }
      } catch (SQLException sqle) {
        Logs.log.error("No se realizo correctamente la consulta de pagos por aprobar.");
        Logs.log.error(consulta);
        Logs.log.error(sqle.getMessage());
      }
      // CONSULTA DE MONTO RECUPERACION QUINCENA
      consulta = "SELECT ROUND(SUM(monto_aprobado), 2) AS recuperacionQuincena FROM pago WHERE id_pago IN (SELECT id_pago FROM promesa_pago WHERE id_promesa_pago IN (SELECT id_promesa_pago FROM convenio_pago WHERE id_credito IN (SELECT id_credito FROM credito WHERE id_credito NOT IN (SELECT id_credito FROM devolucion WHERE estatus = 0) AND id_despacho = " + indexBean.getUsuario().getDespacho().getIdDespacho() + "))) AND id_quincena = (SELECT id_quincena FROM quincena WHERE CURDATE() BETWEEN fecha_inicio AND fecha_fin);";
      try (ResultSet rs = stmt.executeQuery(consulta)) {
        while (rs.next()) {
          if (rs.getString("recuperacionQuincena") == null) {
            montoRecuperacionQuincena = "$ 0.0";
          } else {
            montoRecuperacionQuincena = "$ " + rs.getString("recuperacionQuincena");
          }
        }
      } catch (SQLException sqle) {
        Logs.log.error("No se realizo correctamente la consulta de recuperacion quincena.");
        Logs.log.error(consulta);
        Logs.log.error(sqle.getMessage());
      }
      // CONSULTA DE MONTO PAGOS HOY
      consulta = "SELECT ROUND(SUM(monto_aprobado), 2) AS pagosHoy FROM pago WHERE estatus = " + Pagos.APROBADO + " AND fecha_deposito = CURDATE() AND id_gestor IN (SELECT id_gestor FROM gestor WHERE id_usuario IN (SELECT id_usuario FROM usuario WHERE id_despacho = " + indexBean.getUsuario().getDespacho().getIdDespacho() + "));";
      try (ResultSet rs = stmt.executeQuery(consulta)) {
        while (rs.next()) {
          montoPagosHoy = rs.getString("pagosHoy");
        }
      } catch (SQLException sqle) {
        Logs.log.error("No se realizo correctamente la consulta de pagos hoy.");
        Logs.log.error(consulta);
        Logs.log.error(sqle.getMessage());
      }
      // CONSULTA DE GESTIONES HOY
      consulta = "SELECT COUNT(*) AS gestionesHoy FROM gestion WHERE id_tipo_gestion != 5 AND DATE(fecha) = CURDATE() AND id_credito IN (SELECT id_credito FROM credito WHERE id_despacho = " + indexBean.getUsuario().getDespacho().getIdDespacho() + ") AND id_credito NOT IN (SELECT id_credito FROM devolucion WHERE estatus = " + Devoluciones.DEVUELTO + ");";
      try (ResultSet rs = stmt.executeQuery(consulta)) {
        while (rs.next()) {
          gestionesHoy = rs.getString("gestionesHoy");
        }
      } catch (SQLException sqle) {
        Logs.log.error("No se realizo correctamente la consulta de gestiones hoy.");
        Logs.log.error(consulta);
        Logs.log.error(sqle.getMessage());
      }
      // CONSULTA DE GESTOR DIA PAGOS
      consulta = "SELECT nombre, paterno FROM usuario WHERE id_usuario = (SELECT id_usuario FROM gestor WHERE id_gestor = (SELECT id_gestor FROM pago WHERE fecha_registro = CURDATE() AND estatus = " + Pagos.APROBADO + " GROUP BY id_gestor ORDER BY COUNT(*) DESC LIMIT 1)) AND id_despacho = " + indexBean.getUsuario().getDespacho().getIdDespacho() + ";";
      try (ResultSet rs = stmt.executeQuery(consulta)) {
        while (rs.next()) {
          gestorDelDiaPagos = rs.getString("nombre") + " " + rs.getString("paterno");
        }
      } catch (SQLException sqle) {
        Logs.log.error("No se realizo correctamente la consulta de gestor del dia pagos.");
        Logs.log.error(consulta);
        Logs.log.error(sqle.getMessage());
      }
      // CONSULTA DE GESTOR DEL DIA GESTIONES
      consulta = "SELECT nombre, paterno FROM usuario WHERE id_usuario = (SELECT id_usuario FROM gestion WHERE DATE(fecha) = CURDATE() AND id_tipo_gestion != 5 GROUP BY id_usuario ORDER BY COUNT(*) DESC LIMIT 1) AND id_despacho = " + indexBean.getUsuario().getDespacho().getIdDespacho() + ";";
      try (ResultSet rs = stmt.executeQuery(consulta)) {
        while (rs.next()) {
          gestorDelDiaGestiones = rs.getString("nombre") + " " + rs.getString("paterno");
        }
      } catch (SQLException sqle) {
        Logs.log.error("No se realizo correctamente la consulta de gestor del dia gestiones.");
        Logs.log.error(consulta);
        Logs.log.error(sqle.getMessage());
      }
    } catch (ClassNotFoundException | SQLException e) {
      Logs.log.error(consulta);
      Logs.log.error(e.getMessage());
    } finally {
      if (stmt != null) {
        try {
          stmt.close();
        } catch (SQLException sqle) {
          Logs.log.error("No se pudo cerrar la consulta.");
          Logs.log.error(sqle.getMessage());
        }
      }
      if (conn != null) {
        try {
          conn.close();
        } catch (SQLException sqle) {
          Logs.log.error("No se pudo cerrar la conexion.");
          Logs.log.error(sqle.getMessage());
        }
      }
    }
  }

  //GETTERS & SETTERS
  public String getCreditosActivos() {
    return creditosActivos;
  }

  public void setCreditosActivos(String creditosActivos) {
    this.creditosActivos = creditosActivos;
  }

  public String getVisitasDomiciliarias() {
    return visitasDomiciliarias;
  }

  public void setVisitasDomiciliarias(String visitasDomiciliarias) {
    this.visitasDomiciliarias = visitasDomiciliarias;
  }

  public String getPagosPorAprobar() {
    return pagosPorAprobar;
  }

  public void setPagosPorAprobar(String pagosPorAprobar) {
    this.pagosPorAprobar = pagosPorAprobar;
  }

  public String getPorcentajeRecuperacion() {
    return porcentajeRecuperacion;
  }

  public void setPorcentajeRecuperacion(String porcentajeRecuperacion) {
    this.porcentajeRecuperacion = porcentajeRecuperacion;
  }

  public String getCreditosQuebrantoPermanencia() {
    return creditosQuebrantoPermanencia;
  }

  public void setCreditosQuebrantoPermanencia(String creditosQuebrantoPermanencia) {
    this.creditosQuebrantoPermanencia = creditosQuebrantoPermanencia;
  }

  public String getImpresiones() {
    return impresiones;
  }

  public void setImpresiones(String impresiones) {
    this.impresiones = impresiones;
  }

  public String getMontoPagosPorAprobar() {
    return montoPagosPorAprobar;
  }

  public void setMontoPagosPorAprobar(String montoPagosPorAprobar) {
    this.montoPagosPorAprobar = montoPagosPorAprobar;
  }

  public String getMontoRecuperacionQuincena() {
    return montoRecuperacionQuincena;
  }

  public void setMontoRecuperacionQuincena(String montoRecuperacionQuincena) {
    this.montoRecuperacionQuincena = montoRecuperacionQuincena;
  }

  public String getMontoPagosHoy() {
    return montoPagosHoy;
  }

  public void setMontoPagosHoy(String montoPagosHoy) {
    this.montoPagosHoy = montoPagosHoy;
  }

  public String getGestionesHoy() {
    return gestionesHoy;
  }

  public void setGestionesHoy(String gestionesHoy) {
    this.gestionesHoy = gestionesHoy;
  }

  public String getGestorDelDiaPagos() {
    return gestorDelDiaPagos;
  }

  public void setGestorDelDiaPagos(String gestorDelDiaPagos) {
    this.gestorDelDiaPagos = gestorDelDiaPagos;
  }

  public String getGestorDelDiaGestiones() {
    return gestorDelDiaGestiones;
  }

  public void setGestorDelDiaGestiones(String gestorDelDiaGestiones) {
    this.gestorDelDiaGestiones = gestorDelDiaGestiones;
  }

}
