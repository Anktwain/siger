package carga;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author brionvega
 */
public class Analizador {

  private int[] metodo(List<String> coincidencias, String colonia, String cp) {
    int[] resultado = new int[3];
    for (String coincidencia : coincidencias) {
      //coincidencia{id_colonia;id_municipio;tipo;nombre;codigo_potal}
      String[] registro = coincidencia.split(";");
      System.out.println("Leyendo..." + coincidencia.toUpperCase());
      if (colonia.toUpperCase().equals(registro[3].toUpperCase())) {
        resultado[2] = Integer.parseInt(registro[0]);
        resultado[1] = Integer.parseInt(registro[1]);
        break;
      }

    }
    
    switch (cp.substring(0, 2)) {
      case "01":case "02":case "03":case "04":
      case "05":case "06":case "07":case "08":
      case "09":case "10":case "11":case "12":
      case "13":case "14":case "15":case "16":
        resultado[0] = 9;
        break;
      case "20":
        resultado[0] = 1;
        break;
      case "21":case "22":
        resultado[0] = 2;
        break;
      case "23":
        resultado[0] = 3;
        break;
      case "24":
        resultado[0] = 4;
        break;
      case "25":case "26":case "27":
        resultado[0] = 5;
        break;
      case "28":
        resultado[0] = 6;
        break;
      case "29":case "30":
        resultado[0] = 7;
        break;
      case "31":case "32":case "33":
        resultado[0] = 8;
        break;
      case "34":case "35":
        resultado[0] = 10;
        break;
      case "36":case "37":case "38":
        resultado[0] = 11;
        break;
      case "39":case "40":case "41":
        resultado[0] = 12;
        break;
      case "42":case "43":
        resultado[0] = 13;
        break;
      case "44":case "45":case "46":case "47":
      case "48":case "49":
        resultado[0] = 14;
        break;
      case "50":case "51":case "52":case "53":
      case "54":case "55":case "56":case "57":
        resultado[0] = 15;
        break;
      case "58":case "59":case "60":case "61":
        resultado[0] = 16;
        break;
      case "62":
        resultado[0] = 17;
        break;
      case "63":
        resultado[0] = 18;
        break;
      case "64":case "65":case "66":case "67":
        resultado[0] = 19;
        break;
      case "68":case "69":case "70":case "71":
        resultado[0] = 20;
        break;
      case "72":case "73":case "74":case "75":
        resultado[0] = 21;
        break;
      case "76":
        resultado[0] = 22;
        break;
      case "77":
        resultado[0] = 23;
        break;
      case "78":case "79":
        resultado[0] = 24;
        break;
      case "80":case "81":case "82":
        resultado[0] = 25;
        break;
      case "83":case "84":case "85":
        resultado[0] = 26;
        break;
      case "86":
        resultado[0] = 27;
        break;
      case "87":case "88":case "89":
        resultado[0] = 28;
        break;
      case "90":
        resultado[0] = 29;
        break;
      case "91":case "92":case "93":case "94":
      case "95":case "96":
        resultado[0] = 30;
        break;
      case "97":
        resultado[0] = 31;
        break;
      case "98":case "99":
        resultado[0] = 32;
        break;
    }

    return resultado;
  }

  public static void main(String[] args) {
    System.out.println("Comenzamos!!!");
    List<String> coincidencias = new ArrayList<>();
    coincidencias.add("92888;1316;Fraccionamiento;Agua Marina;71984");
    coincidencias.add("92889;1316;Colonia;Esperanza;71984");
    coincidencias.add("92890;1316;Fraccionamiento;Costa Chica;71984");
    coincidencias.add("92891;1316;Colonia;Sector Juárez;71984");
    coincidencias.add("92892;1316;Fraccionamiento;La Playa;71984");
    coincidencias.add("92893;1316;Fraccionamiento;Costa del Sol;71984");
    coincidencias.add("92894;1316;Fraccionamiento;Granjas de Pescador;71984");
    coincidencias.add("92895;1316;Colonia;Sector Reforma D;71984");
    coincidencias.add("92896;1316;Fraccionamiento;Los Mangos;71984");
    coincidencias.add("92897;1316;Fraccionamiento;Palmeras;71984");
    coincidencias.add("92898;1316;Fraccionamiento;Las Palmas;71984");
    coincidencias.add("92899;1316;Colonia;Libertad;71984");
    coincidencias.add("92900;1316;Fraccionamiento;Caminero;71984");
    coincidencias.add("92901;1316;Colonia;Sector Reforma;71984");
    coincidencias.add("92902;1316;Fraccionamiento;Los Mangales;71984");

    int[] info;

    Analizador analizador = new Analizador();
    info = analizador.metodo(coincidencias, "Sector Juárez","71984");

    System.out.println("Estado: " + info[0]);
    System.out.println("Municipio: " + info[1]);
    System.out.println("Colonia: " + info[2]);
  }
}
