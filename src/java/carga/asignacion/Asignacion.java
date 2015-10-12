package carga.asignacion;

import dto.Fila;
import java.io.*;
import java.sql.*;
import java.util.List;
import util.constantes.Directorios;
import java.util.ArrayList;

/**
 *
 * @authors Pablo y el Cisne
 */
public class Asignacion {

  // METODO QUE RECIBE LAS FILAS DEL ARCHIVO DE EXCEL Y CREA UNA LISTA CON LOS DATOS QUE NOS INTERESAN
  public ArrayList<ArrayList<String>> recibeFilas(List<Fila> listaFilas) {
    // CREAMOS UNA LISTA. PARA GUARDAR LAS LISTAS AUXILIARES
    ArrayList<ArrayList<String>> listaPrincipal = new ArrayList<>();
    // LLENAMOS LA LISTA CON LOS DATOS 
    for (int i = 0; i < listaFilas.size(); i++) {
      // CREAMOS UNA LISTA AUXILIAR PARA GUARDAR LOS SIGUIENTES DATOS EN ESE ORDEN:
      //  - NUMERO DE CREDITO
      //  - SALDO VENCIDO
      //  - NUMERO DE CLIENTE
      //  - CLAVE DEL GESTOR ASIGNADO
      List<String> aux = new ArrayList<>();
      aux.add(listaFilas.get(i).getCredito());
      aux.add(listaFilas.get(i).getSaldoVencido());
      aux.add(listaFilas.get(i).getIdCliente());
      // LLENAMOS LA COLUMNA FALTANTE CON EL ID DEL GESTOR DEL CREDITO
      for (int j = 0; j < listaFilas.size(); j++) {
        // INICIAMOS UNA CONEXION A BASE DE DATOS
        try {
          DriverManager.registerDriver(new com.mysql.jdbc.Driver());
          Connection conexion = DriverManager.getConnection("jdbc:mysql://10.0.0.26:3306/sigerbd?zeroDateTimeBehavior=convertToNull", "cofradia", "cofradiaDB");
          Statement consulta = conexion.createStatement();
          // CONSULTA PARA OBTENER EL ID DEL GESTOR
          String query = "SELECT gestores_id_gestor FROM credito WHERE numero_credito = '" + aux.get(0) + "';";
          ResultSet r = consulta.executeQuery(query);
          // SI LA CONSULTA REGRESO ALGUN RESULTADO, LO ASIGNAMOS AL ARREGLO
          while (r.next()) {
            aux.add(r.getString("gestores_id_gestor"));
          }
          // AGREGAMOS LA LISTA AUXILIAR A LA LISTA
          listaPrincipal.add((ArrayList<String>) aux);
          // CERRAMOS LA CONEXION
          r.close();
          consulta.close();
          conexion.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return asignacionPorDefecto(listaPrincipal);
  }

  // METODO QUE REALIZA LAS ASIGNACIONES AUTOMATICAS: CREDITOS CONSERVADOS Y NUEVOS CREDITOS
  public ArrayList<ArrayList<String>> asignacionPorDefecto(ArrayList<ArrayList<String>> lista) {
    // OBTENEMOS EL ID DEL GESTOR DE CADA LISTA EN LA LISTA. ELIMINACION DE CONSERVADOS
    for (int i = 0; i < lista.size(); i++) {
      //// SI EL VALOR ES DIFERENTE DE 0, ES QUE YA TIENE GESTOR ASIGNADO
      if (!lista.get(i).get(3).equals("0")) {
        // ELIMINAMOS ESA FILA DE LA LISTA
        lista.remove(i);
        // DECREMENTAMOS LA VARIABLE i EN UNA UNIDAD PARA NO DESBALANCEAR EL FOR Y NO SALTARNOS ELEMENTOS DE LA LISTA
        i--;
      }
    }
    // VERIFICAMOS SI LOS CREDITOS YA TIENEN UN CLIENTE ASOCIADO. ELIMINACION DE NUEVOS CREDITOS
    for (int i = 0; i < lista.size(); i++) {
      // PONEMOS UN VALOR DEFAULT POR SI HAY NULL
      String idGestor = "0";
      // INICIAMOS UNA CONEXION A BASE DE DATOS
      try {
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        Connection conexion = DriverManager.getConnection("jdbc:mysql://10.0.0.26:3306/sigerbd?zeroDateTimeBehavior=convertToNull", "cofradia", "cofradiaDB");
        Statement consulta = conexion.createStatement();
        // CONSULTA PARA SABER SI UN CLIENTE YA TIENE CREDITOS EN EL SISTEMA, REGRESA EL ID DEL GESTOR
        String query = "SELECT gestores_id_gestor FROM credito WHERE clientes_id_cliente = (SELECT id_cliente FROM cliente WHERE numero_cliente = '" + lista.get(i).get(2) + "' LIMIT 1) LIMIT 1;";
        ResultSet r = consulta.executeQuery(query);
        while (r.next()) {
          idGestor = r.getString("gestores_id_gestor");
        }
        // CREAMOS UNA VARIABLE PARA GUARDAR EL ID DEL CREDITO A ACTUALIZAR
        String idCredito = null;
        // BUSCAMOS EL ID PARA ESTE NUMERO DE CREDITO
        query = "SELECT id_credito FROM credito WHERE numero_credito = '" + lista.get(i).get(2) + "' LIMIT 1;";
        r = consulta.executeQuery(query);
        while (r.next()) {
          idCredito = r.getString("id_credito");
        }
        // ACTUALIZAMOS EL ID DEL GESTOR EN LA BASE DE DATOS PARA ESTE CREDITO
        query = "UPDATE credito SET gestores_id_gestor = " + idGestor + " WHERE id_credito = " + idCredito + ";";
        consulta.executeUpdate(query);
        // ELIMINAMOS ESA FILA DE LA LISTA
        lista.remove(i);
        // DECREMENTAMOS LA VARIABLE i EN UNA UNIDAD PARA NO DESBALANCEAR EL FOR Y NO SALTARNOS ELEMENTOS DE LA LISTA
        i--;
        // CERRAMOS LA CONEXION
        r.close();
        consulta.close();
        conexion.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return lista;
  }

  // METODO QUE HACE LA ROTACION DE LOS GESTORES EN EL ARCHIVO Y PREPARA UN ARREGLO CON EL ORDEN DE LOS MISMOS
  public static List<String> rotacionGestores() {
    List<String> ordenGestores = new ArrayList<>();
    // LEEMOS EL ARCHIVO PARA OBTENER EL ORDEN ACTUAL DE LOS GESTORES
    String archivo = Directorios.RUTA_ASIGNACION + "rotacion.txt";
    try {
      FileReader fr = new FileReader(archivo);
      BufferedReader bf = new BufferedReader(fr);
      String linea;
      // MIENTRAS LA LINEA NO ESTE VACIA, AGREGAREMOS EL NUMERO DE GESTOR AL ARREGLO
      while ((linea = bf.readLine()) != null) {
        ordenGestores.add(linea);
      }
      // CERREMOS EL ARCHIVO PARA LECTURA
      bf.close();
      fr.close();
      // AHORA HACEMOS LA ROTACION DE LOS GESTORES UN LUGAR HACIA ABAJO
      // GUARDAMOS EL ID DEL GESTOR EN EL ULTIMO ELEMENTO
      String ultimoGestor = ordenGestores.get(ordenGestores.size()-1);
      System.out.println("ULTIMO GESTOR: " + ultimoGestor);
      // RECORREMOS LOS VALORES A PARTIR DEL ULTIMO ELEMENTO HASTA EL SEGUNDO
      for (int i = ordenGestores.size()-1; i > 0; i--) {
        ordenGestores.set(i, ordenGestores.get(i-1));
      }
      // RECORREMOS EL ULTIMO GESTOR QUE GUARDAMOS AL PRIMER LUGAR
      ordenGestores.set(0, ultimoGestor);
      // IMPRIMIMOS EL ID DE LOS GESTORES EN EL NUEVO ORDEN
      for (int i = 0; i < ordenGestores.size(); i++) {
        System.out.println(ordenGestores.get(i));
      }
      // ESCRIBIMOS ESTOS CAMBIOS EN EL ARCHIVO
      BufferedWriter bw = new BufferedWriter(new FileWriter(archivo));
      for (int i = 0; i < ordenGestores.size(); i++) {
        bw.write(ordenGestores.get(i) + "\n");
      }
      bw.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return ordenGestores;
  }

  // METODO QUE REALIZA LA ASIGNACION DE LOS CREDITOS NUEVOS TOTALES
  public void asignacionNuevosTotales(ArrayList<ArrayList<String>> nuevosCreditos, List<String> gestores) {
    for (int i = 0; i < nuevosCreditos.size(); i++) {
      for (int j = 0; j < nuevosCreditos.get(i).size(); j++) {
        System.out.println(nuevosCreditos.get(i).get(j));
      }
    }
    // RIFATE JAAAAAAAAAAI!!!
  }
}