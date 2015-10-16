package carga;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import util.log.Logs;

/**
 *
 * @author Pablo
 */
public class BuscadorTxt {

  public BuscadorTxt() {
  }

  /**
   * La buena. Encuentra todas las líneas en el archivo de texto correspondiente
   * (de colonias) tales que el código postal escrito al final de ésta coincida
   * exactamente con el proporcionado como criterio de búsqueda.
   *
   * @return La {@code List<String>} que contiene todas las coincidencias
   * encontradas en el archivo de texto de colonias correspondiente. Cada uno de
   * los elementos en la lista es un solo {@code String} que describe un tipo de
   * asentamiento. dicha cadena es de la forma:
   * <strong>a;b;c;d;e</strong>
   * tal que:
   * <ul>
   * <li><strong>a</strong>: entero: clave de la colonia (o tipo de
   * asentamiento)</li>
   * <li><strong>b</strong>: entero; clave del municipio (o delegación)</li>
   * <li><strong>c</strong>: alfabético: Tipo de asentamiento </li>
   * <li><strong>d</strong>: alfabético: Nombre del tipo de asentamiento</li>
   * <li><strong>e</strong>: entero, 5 cifras: código postal</li>
   * </ul>
   */
  public static List<String> buscarTxt(String cp, String archivoaLeer) {
    List<String> coincidencias = new ArrayList<>(); // La lista de colonias cuyo CP coincide con el CP dado
    String lineaActual; // La línea leída en un momento determinado.

    try(BufferedReader buferLectura = new BufferedReader(new FileReader(archivoaLeer))) {
      while ((lineaActual = buferLectura.readLine()) != null) {
        if (lineaActual.substring(lineaActual.length() - 5).equals(cp)) {
          do {
            coincidencias.add(lineaActual);
            lineaActual = buferLectura.readLine();
            if (lineaActual == null) {
              break;
            }
          } while (lineaActual.substring(lineaActual.length() - 5).equals(cp));
          break; // Rompe el ciclo dado que no habrán más coincidencias
        } // fin de if
      } // fin de while
    } catch (IOException ioe) {
      Logs.log.error("Error de lectura/escritura");
      Logs.log.error(ioe.getMessage());
    }

    return coincidencias;
  }
}
