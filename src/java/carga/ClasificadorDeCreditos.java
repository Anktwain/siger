/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carga;

import dto.carga.Fila;
import java.util.List;
import util.Levenshtein;
import util.constantes.Directorios;

/**
 *
 * @author brionvega
 */
public class ClasificadorDeCreditos {

  public static void enLaFiesta(Fila fila) {

  }

  public static void estabaEnLaFiesta(Fila fila) {

  }

  public static void nuevoCredito(Fila fila) {

  }

  public static String nuevoTotal(Fila fila) throws Exception {
    // CREAMOS LA CADENA QUE GUARDARA LA CONSULTA
//    String consulta = null;
    int[] ns = {0, 0};
//    // PRIMERO CREAMOS EL SUJETO
//    consulta = "INSERT INTO sujeto (nombre_razon_social, rfc, eliminado) VALUES ('" + fila.getNombre() + "', '" + fila.getRfc() + "', 1);\n";
//    consulta = consulta + "SET @idSujeto = (SELECT LAST_INSERT_ID());\n";
//    consulta = consulta + "INSERT INTO cliente (numero_cliente, sujetos_id_sujeto) VALUES ('" + fila.getIdCliente() + "', @idSujeto);\n";
//    consulta = consulta + "SET @idCliente = (SELECT id_cliente FROM cliente WHERE sujetos_id_sujeto = @idSujeto);\n";
//    consulta = consulta + "SET @idEmpresa = (SELECT id_empresa FROM empresa WHERE id_empresa = 1);\n";
//    consulta = consulta + "SET @idProducto = (SELECT id_producto FROM producto WHERE id_producto = 1);\n";
//    consulta = consulta + "SET @idSubproducto = (SELECT id_subproducto FROM subproducto WHERE id_subproducto = 1);\n";
////    consulta = consulta + "INSERT INTO credito (numero_credito, fecha_inicio, fecha_fin, fecha_quebranto, monto, mensualidad, tasa_interes, dias_mora, numero_cuenta, tipo_credito, empresas_id_empresa, productos_id_producto, subproductos_id_subproducto, clientes_id_cliente, gestores_id_gestor) VALUES ('" + credito + "', '" + fechaInicioCredito + "', '" + fechaVencimientoCred + "', '" + fechaQuebranto + "', " + disposicion + ", " + mensualidad + ", " + tasa + ", 0, " + cuenta + ", 1, @idEmpresa, @idProducto, @idSubproducto, @idCliente, 7);\n";
//    consulta = consulta + "INSERT INTO direccion (calle, sujetos_id_sujeto, municipio_id_municipio, estado_republica_id_estado, colonia_id_colonia) VALUES ('" + fila.getCalle() + "', @idSujeto, " + fila.getMunicipio() + ", " + fila.getEstado() + ", " + fila.getColonia() + ");\n";
//    consulta = consulta + "INSERT INTO telefono (numero, tipo, sujetos_id_sujeto) VALUES ('" + fila.getRefCobro() + "', 'Referencia', @idSujeto);\n";
//    //consulta = consulta + "INSERT INTO email (direccion, tipo, sujetos_id_sujeto) VALUES ('" + correos.get(0) + "', 'Referencia', @idSujeto);";
    int[] hola = obtenerDireccion(fila);
    if (hola[2] == 0) {
      return "n";
    } else {
      return "s";
    }
  }

  /**
   * Crea un arreglo de int que contiene las claves correspondientes al estado,
   * al municipio y a la colonia, en ese orden.
   *
   * @return direccion: La dirección física expresada como un int[] que contiene
   * los siguientes valores:
   * <ul>
   * <li>direccion[0]: La clave numérica, entre 1 y 32, del estado de la
   * república.</li>
   * <li>direccion[1]: La clave numérica del municipio</li>
   * <li>direccion[2]: La clave numérica de la colonia</li>
   * </ul>
   *
   */
  private static int[] obtenerDireccion(Fila fila) throws Exception {
    int[] direccion = new int[3];
    direccion[0] = direccion[1] = direccion[2] = 0;
    if (fila.getCp() == null || fila.getCp().length() < 5) {
      return direccion;
    }
    int estado = 0;
    String cp = fila.getCp();
    String prefijoCP = cp.substring(0, 2);
    String archivoAExaminar = Directorios.RUTA_COLONIAS + prefijoCP + ".csv";
    switch (prefijoCP) {
      case "01":
      case "02":
      case "03":
      case "04":
      case "05":
      case "06":
      case "07":
      case "08":
      case "09":
      case "10":
      case "11":
      case "12":
      case "13":
      case "14":
      case "15":
      case "16":
        estado = 9; // Distrito Federal
        break;
      case "20":
        estado = 1; // Aguascalientes
        break;
      case "21":
      case "22": // Baja California
        estado = 2;
        break;
      case "23": //Baja California Sur
        estado = 3;
        break;
      case "24": // Campeche
        estado = 4;
        break;
      case "25":
      case "26":
      case "27": // Coahuila
        estado = 5;
        break;
      case "28": // Colima
        estado = 6;
        break;
      case "29":
      case "30": // Chiapas
        estado = 7;
        break;
      case "31":
      case "32":
      case "33": // Chihuahua
        estado = 8;
        break;
      case "34":
      case "35": // Durango
        estado = 10;
        break;
      case "36":
      case "37":
      case "38": // Guanajuato
        estado = 11;
        break;
      case "39":
      case "40":
      case "41": // Guerrero
        estado = 12;
        break;
      case "42":
      case "43": // Hidalgo
        estado = 13;
        break;
      case "44":
      case "45":
      case "46":
      case "47":
      case "48":
      case "49": // Jalisco
        estado = 14;
        break;
      case "50":
      case "51":
      case "52":
      case "53":
      case "54":
      case "55":
      case "56":
      case "57": // México
        estado = 15;
        break;
      case "58":
      case "59":
      case "60":
      case "61": // Michoacán
        estado = 16;
        break;
      case "62": // Morelos
        estado = 17;
        break;
      case "63": // Nayarit
        estado = 18;
        break;
      case "64":
      case "65":
      case "66":
      case "67": // Nuevo León
        estado = 19;
        break;
      case "68":
      case "69":
      case "70":
      case "71": // Oaxaca
        estado = 20;
        break;
      case "72":
      case "73":
      case "74":
      case "75": // Puebla
        estado = 21;
        break;
      case "76": // Querétaro
        estado = 22;
        break;
      case "77": // Quintana Roo
        estado = 23;
        break;
      case "78":
      case "79": // San Luis Potosí
        estado = 24;
        break;
      case "80":
      case "81":
      case "82": // Sinaloa
        estado = 25;
        break;
      case "83":
      case "84":
      case "85": // Sonora
        estado = 26;
        break;
      case "86": // Tabasco
        estado = 27;
        break;
      case "87":
      case "88":
      case "89": // Tamaulipas
        estado = 28;
        break;
      case "90": // Tlaxcala
        estado = 29;
        break;
      case "91":
      case "92":
      case "93":
      case "94":
      case "95":
      case "96": //Veracruz
        estado = 30;
        break;
      case "97": // Yucatán
        estado = 31;
        break;
      case "98":
      case "99": // Zacatecas
        estado = 32;
        break;
    } // fin switch

    int resultado[] = obtenerColMpio(BuscadorTxt.buscarTxt(cp, archivoAExaminar), fila.getColonia());
    direccion[0] = estado;
    direccion[1] = resultado[0]; // municipio
    direccion[2] = resultado[1]; // colonia
    if (direccion[2] != 0) {
      System.out.println("Estado: " + direccion[0] + " Municipio: " + direccion[1] + " Colonia: " + direccion[2] + "-----------> " + fila.getColonia());
    } else {
      System.out.println("No se encontró colonia: " + fila.getColonia() + " para el codigo postal: " + fila.getCp());
    }
    return direccion;
  }

  /**
   *
   */
  private static int[] obtenerColMpio(List<String> coincidencias, String colonia) {
    int resultado[] = new int[2];
    resultado[0] = resultado[1] = 0;
    String[] registro;
    for (String coincidencia : coincidencias) {
      //coincidencia{id_colonia;id_municipio;tipo;nombre;codigo_postal}
      registro = coincidencia.split(";"); // PRIMER INTENTO: CADENA
      registro[3] = registro[3].toLowerCase();
      if (colonia.toLowerCase().equals(registro[3])) {
        resultado[1] = Integer.parseInt(registro[0]); // id colonia
        resultado[0] = Integer.parseInt(registro[1]); // id municipio
        System.out.println("COLONIA REMESA: " + colonia + " | COLONIA DB: " + registro[3] + " | COINCIDENCIA EXACTA");
        break;
      } else if (Levenshtein.computeLevenshteinDistance(colonia.toLowerCase(), registro[3]) < 4) {
        resultado[1] = Integer.parseInt(registro[0]); // id colonia
        resultado[0] = Integer.parseInt(registro[1]); // id municipio
        System.out.println("COLONIA REMESA: " + colonia + " | COLONIA DB: " + registro[3] + " | LEVENSHTEIN");
        break;
      } else if (EliminadorDeAsentaminetos.eliminaAsentamiento(colonia).equals(registro[3])) {
        resultado[1] = Integer.parseInt(registro[0]); // id colonia
        resultado[0] = Integer.parseInt(registro[1]); // id 
        System.out.println("COLONIA REMESA: " + colonia + " | COLONIA DB: " + registro[3] + " | ELIMINADOR ASENTAMIENTOS");
        break;
      } else if (EliminadorDeArticulos.eliminaArticulos(colonia).equals(registro[3])){
        resultado[1] = Integer.parseInt(registro[0]); // id colonia
        resultado[0] = Integer.parseInt(registro[1]); // id 
        System.out.println("COLONIA REMESA: " + colonia + " | COLONIA DB: " + registro[3] + " | ELIMINADOR ARTICULOS");
        break;
      } else if (Like.like(colonia, registro[3])) {
        resultado[1] = Integer.parseInt(registro[0]); // id colonia
        resultado[0] = Integer.parseInt(registro[1]); // id 
        System.out.println("COLONIA REMESA: " + colonia + " | COLONIA DB: " + registro[3] + " | LIKE");
        break;
      }
    }
    return resultado;
  }
}
