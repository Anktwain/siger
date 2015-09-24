package util;

import dto.Fila;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import util.constantes.Directorios;

/**
 *
 * @author Pablo
 */
public class BuscadorTxt {

  public static List<String> buscarTxt(String cp, int lineaInicio) {

    List<String> coincidencias = new ArrayList<>();
    try (BufferedReader buferLectura = new BufferedReader(new FileReader(Directorios.RUTA_COLONIAS))) {
      String lineaActual;
      
      for(int i = 1; i < lineaInicio; i++)
        buferLectura.readLine(); // Recorre el bufer de lectura hasta la línea indicada

      while ((lineaActual = buferLectura.readLine()) != null) {
        if (lineaActual.substring(lineaActual.length() - 5).equals(cp)) {
          coincidencias.add(lineaActual);
        }
      }
    } catch (IOException ioe) {
      throw new Exception("Error de lectura/escritura.", ioe);
    }
    return coincidencias;
  }
}
