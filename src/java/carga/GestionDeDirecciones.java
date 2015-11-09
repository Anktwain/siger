/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carga;

import java.sql.*;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public class GestionDeDirecciones {
  
  // METODO QUE VERIFICARA UNA DIRECCION SEGUN EL CODIGO POSTAL
  public static List<String> verificaPorCodigoPostal(String cp, String col){
    // CREAMOS UNA LISTA QUE CONTENDRA LOS DATOS VERIFICADOS SI ES QUE LOS ENCUENTRA
    List<String> verificados = new ArrayList<>();
    // UNA LISTA PARA TRAER LAS COLONIAS DE DETERMINADO CODIGO POSTAL
    List<String> colonias = new ArrayList<>();
    // VARIABLES PARA POSTERIORES CONSULTAS
    String municipio = null;
    String colonia = null;
    String estado = null;
    String aux;
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
      // PARA OBTENER LAS COLONIAS QUE PERTENECEN A ESE CODIGO POSTAL
      query = "SELECT CONCAT(tipo, ' ', nombre) FROM colonia WHERE codigo_postal = '" + cp + "';";
      r = consulta.executeQuery(query);
      while(r.next()) {
        // OBTENEMOS EL VALOR DE LA UNICA COLUMNA DEL RESULT SET
        aux = r.getString(1);
        // ELIMINAMOS LOS PUNTOS
        aux = aux.replaceAll("\\.","");
        // CONVERTIMOS LA CADENA A UNA CON LETRAS MINUSCULAS
        aux = aux.toLowerCase();
        // QUITAMOS LOS ACENTOS
        aux = Normalizer.normalize(aux, Normalizer.Form.NFD);
        // AGREGAMOS AL ARREGLO QUE CONTIENE LAS COLONIAS
        colonias.add(aux);
      }
      // PARA OBTENER LA COLONIA
      for (int i = 0; i < colonias.size(); i++) {
        String colonialower = col.toLowerCase();
        query = "SELECT id_colonia FROM colonia WHERE codigo_postal = '" + cp + "' AND nombre LIKE '%" + col + "%';";
        r = consulta.executeQuery(query);
        // SI NO COINCIDIO
        if(!r.isBeforeFirst()){        }
        // SI COINCIDIO
        else{
          while(r.next()) {
            colonia = r.getString("id_colonia");
            i = colonias.size();
            break;
          }
        }
      }
      // AGREGAMOS A LA LISTA
      verificados.add(estado);
      verificados.add(municipio);
      verificados.add(colonia);
      // CERRAMOS LA CONEXION
      r.close();
      consulta.close();
      conexion.close();
    }
    finally {
      return verificados;
    }
  }
}
