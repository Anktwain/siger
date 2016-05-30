package carga;

import dto.carga.Fila;
import java.util.List;
import util.constantes.Directorios;

/**
 *
 * @author brionvega
 */
public class Direccionador {

  public static Fila obtenerDireccion(Fila fila) {
    String cp = fila.getCp();
    if (cp == null || !CPValido(cp)) {
      fila.setIdColonia(0);
      fila.setIdMunicipio(0);
      fila.setIdEstado(0);
      return fila;
    }

    int estado = 0; // guarda el id del estado de acuerdo al CP encontrado
    String prefijoCP = cp.substring(0, 2); // Toma los dos primeros dígitos del CP
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
    } // fin de switch

    int resultado[]
            = obtenerColoniaYMunicipio(BuscadorTxt.buscarTxt(cp, archivoAExaminar), fila.getColonia());
    fila.setIdEstado(estado);
    fila.setIdMunicipio(resultado[0]);
    fila.setIdColonia(resultado[1]);
    
    return fila;
  }

  private static boolean CPValido(String cp) {
    if (cp == null) {
      return false;
    }

    return cp.matches("[0-9]{5}");
  }

  private static int[] obtenerColoniaYMunicipio(List<String> coincidencias, String colonia) {
    int resultado[] = new int[3];
    resultado[0] = resultado[1] = 0;
    String[] registro;
    
    for(String coincidencia : coincidencias) {
      //coincidencia{id_colonia;id_municipio;tipo;nombre;codigo_postal}
      registro = coincidencia.split(";");
      registro[3] = registro[3].toLowerCase(); // colonia en minúsculas evita problemas
      
      if(colonia.toLowerCase().equals(registro[3])){
        resultado[1] = Integer.parseInt(registro[0]); // id colonia
        resultado[0] = Integer.parseInt(registro[1]); // id municipio
        break;
      }
    }
    return resultado;
  }
}