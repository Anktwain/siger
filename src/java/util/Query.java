/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


/**
 *
 * @author Eduardo
 */
public class Query {

  public static boolean ejecutaQuery(String query) {
    try {
      DriverManager.registerDriver(new com.mysql.jdbc.Driver());
      Connection conexion = DriverManager.getConnection("jdbc:mysql://10.0.0.26:3306/sigerbd?zeroDateTimeBehavior=convertToNull", "cofradia", "cofradiaDB");
      Statement consulta = conexion.createStatement();
      consulta.executeUpdate(query);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}
