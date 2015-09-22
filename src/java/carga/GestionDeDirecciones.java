/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carga;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public class GestionDeDirecciones {
  
  // METODO QUE VERIFICARA UNA DIRECCION SEGUN EL CODIGO POSTAL
  public static List<String> verificaPorCodigoPostal(String cp){
    // CREAMOS UNA LISTA QUE CONTENDRA LOS DATOS VERIFICADOS SI ES QUE LOS ENCUENTRA
    List<String> verificados = new ArrayList<>();
    // VARIABLES PARA POSTERIORES CONSULTAS
    String municipio = "0";
    String colonia = "0";
    String estado = "0";
    // INICIAMOS UNA CONEXION A BASE DE DATOS
    try {
      DriverManager.registerDriver(new com.mysql.jdbc.Driver());
      Connection conexion = DriverManager.getConnection("jdbc:mysql://10.0.0.26:3306/sigerbd?zeroDateTimeBehavior=convertToNull", "cofradia", "cofradiaDB");
      Statement consulta = conexion.createStatement();
      // PARA OBTENER EL MUNICIPIO
      String query = "SELECT id_municipio_municipios FROM colonia WHERE codigo_postal = '" + cp + "' LIMIT 1;";
      ResultSet r = consulta.executeQuery(query);
      while(r.next()) {
        municipio = r.getString("id_municipio_municipios");
      }
      // PARA OBTENER EL ESTADO
      query = "SELECT id_estado_estados FROM municipio WHERE id_municipio = " + municipio + ";";
      r = consulta.executeQuery(query);
      while(r.next()) {
        estado = r.getString("id_estado_estados");
      }
      // PARA OBTENER LA COLONIA
      // CABE RESALTAR QUE EN CASO DE QUE LA BUSQUEDA ARROJE MAS DE UN RESULTADO, SE TOMARA EN CUENTA SOLO EL PRIMERO
      query = "SELECT id_colonia FROM colonia WHERE codigo_postal = '" + cp + "' AND (SELECT CONCAT(tipo, ' ', nombre) FROM colonia WHERE codigo_postal = '" + cp + "' LIMIT 1) LIKE (SELECT CONCAT('', '%', tipo, ' ', nombre, '%', '') FROM colonia WHERE codigo_postal = '" + cp + "' LIMIT 1) LIMIT 1;";
      r = consulta.executeQuery(query);
      while(r.next()) {
        colonia = r.getString("id_colonia");
      }
      // AGREGAMOS A LA LISTA
      verificados.add(estado);
      verificados.add(municipio);
      verificados.add(colonia);
    }
    finally {
      return verificados;
    }
  }
}
