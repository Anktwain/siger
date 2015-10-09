package carga.asignacion;

import dto.Fila;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.constantes.Directorios;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author Pablo
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
      // CREAMOS UNA VARIABLE PARA GUARDAR EL ID GESTOR
      String idGestor;
      // LLENAMOS LA COLUMNA FALTANTE CON EL ID DEL GESTOR DEL CREDITO
      for (int j = 0; j < listaFilas.size(); j++) {
        // PONEMOS UN VALOR DEFAULT POR SI HAY NULL
        idGestor = "0";
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
        // AUMENTOMOS LA VARIABLE i EN UNA UNIDAD PARA NO DESBALANCEAR EL FOR
        i++;
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
        // AUMENTOMOS LA VARIABLE i EN UNA UNIDAD PARA NO DESBALANCEAR EL FOR
        i++;
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
  public List<String> rotacionGestores() {
    List<String> ordenGestores = new ArrayList<>();
    // LEEMOS EL ARCHIVO PARA OBTENER EL ORDEN ACTUAL DE LOS GESTORES
    String archivo = Directorios.RUTA_REMESAS + "rotacion.txt";
    try {
      FileReader fr = new FileReader(archivo);
      BufferedReader bf = new BufferedReader(fr);
      String linea;
      // MIENTRAS LA LINEA NO ESTE VACIA, AGREGAREMOS EL NUMERO DE GESTOR AL ARREGLO
      while ((linea = bf.readLine()) != null) {
        ordenGestores.add(linea);
      }
      // AHORA HACEMOS LA ROTACION DE LOS GESTORES UN LUGAR HACIA ABAJO
      // OBTENEMOS LA POSICION DEL ULTIMO ELEMENTO
      int ultimo = (ordenGestores.size() - 1);
      // GUARDAMOS EL ID DEL GESTOR EN EL ULTIMO ELEMENTO
      String ultimoGestor = ordenGestores.get(ultimo);
      // RECORREMOS LOS VALORES A PARTIR DEL PRIMER ELEMENTO HASTA EL PENULTIMO
      for (int i = 0; i < (ordenGestores.size() - 1); i++) {
        ordenGestores.set(i+1, ordenGestores.get(i));
      }
      // RECORREMOS EL ULTIMO ELEMENTO AL PRIMER LUGAR
      ordenGestores.set(0, ultimoGestor);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return ordenGestores;
  }

  // METODO QUE REALIZA LA ASIGNACION DE LOS CREDITOS NUEVOS TOTALES
  public void asignacionNuevosTotales(ArrayList<ArrayList<String>> nuevosCreditos, List<String> gestores) {
    // RIFATE JAAAAAAAAAAI!!!
  }
  private ArrayList<Fila> nuevosTotales;
  private ArrayList<ArrayList<String>> gestores;

  /**
   * Ordena {@code nuevosTotales} de mayor a menor con base en el monto de su
   * saldo vencido.
   */
  public void ordenarDecreceiente() {
    Collections.sort(nuevosTotales, new Comparator<Fila>() {
      @Override
      public int compare(Fila f1, Fila f2) {
        if (Float.valueOf(f1.getSaldoVencido()) < Float.valueOf(f2.getSaldoVencido())) {
          return -1;
        } else if (Float.parseFloat(f1.getSaldoVencido()) == Float.parseFloat(f2.getSaldoVencido())) {
          return 0;
        } else {
          return 1;
        }
      }
    });
  }

  /**
   *
   */
  public void asignarNuevosTotales() {
    /**
     * Calculamos el número de iteraciones. Por iteracion se reparten dos
     * créditos a cada gestor.
     */
    int iteraciones = this.nuevosTotales.size() / (2 * gestores.size());

    /**
     * Se reparten de a dos en dos los créditos entre los gestores, dandole a
     * cada uno el mayor y el menor dispoinibles en cada iteración. Pueden
     * sobrar hasta (2*n)-1 creditos por repartir.
     */
    for (int i = 0; i < iteraciones; i++) {

    }

    int restantes = this.nuevosTotales.size() % (2 * gestores.size());

    if (restantes > 0) {
      ArrayList<Fila> disponibles = new ArrayList<Fila>();
      for (int i = 0; i < restantes; i++) {
        disponibles.add(nuevosTotales.get(iteraciones + i));
      }
      if (restantes % gestores.size() != 0) {
        /**
         * Se reparte una vez más entre todos los gestores, ahora comenzando por
         * el último en la lista. Pueden sobrar hasta n-1 creditos por repartir.
         */
        for (int i = 0; i < gestores.size(); i++) {

          disponibles.remove();
        }
      } else if (restantes > 0) {
        Collections.min(gestores, new Comparator<ArrayList<String>>() {

          @Override
          public int compare(ArrayList<String> o1, ArrayList<String> o2) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
          }

        });
      }

    }

  }

}
