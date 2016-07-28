/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.Carga;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
// REESTRUCTURACION DEL MODULO DE CARGA
public class EjecutorCargaSQL {

  // ***************************************************************************
  // VARIABLES DE CLASE
  // ***************************************************************************
  private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

  // ***************************************************************************
  // CONSTRUCTOR
  // ***************************************************************************
  public EjecutorCargaSQL() {
  }

  // ***************************************************************************
  // METODO QUE EJECUTA LAS CONSULTAS SQL 
  // ***************************************************************************
  public boolean ejecutor(String consulta, Connection conn) {
    boolean ok = false;
    Statement stmt = null;
    try {
      Class.forName(JDBC_DRIVER);
      stmt = conn.createStatement();
      stmt.executeUpdate(consulta);
      ok = true;
    } catch (ClassNotFoundException | SQLException e) {
      Logs.log.error("No se ejecuto consulta en proceso de carga.");
      Logs.log.error(consulta);
      Logs.log.error(e.getMessage());
      ok = false;
    } finally {
      if (stmt != null) {
        try {
          stmt.close();
        } catch (SQLException sqle) {
          Logs.log.error("No se pudo cerrar la consulta.");
          Logs.log.error(sqle.getMessage());
        }
      }
    }
    return ok;
  }

}
