/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author antonio
 */
public class BautistaDeArchivos {

  private static String obtenerCadenaFechaActual() {
    Calendar calendario = new GregorianCalendar();

    return Integer.toString(calendario.get(Calendar.YEAR))
            + Integer.toString(1 + calendario.get(Calendar.MONTH))
            + Integer.toString(calendario.get(Calendar.DATE))
            + Integer.toString(calendario.get(Calendar.HOUR_OF_DAY))
            + Integer.toString(calendario.get(Calendar.MINUTE))
            + Integer.toString(calendario.get(Calendar.SECOND));
  }

  /**
   * Pone un nombre a un archivo
   * @param nombre el nombre original del archivo
   * @param afijo es un prefijo o sufijo que deberá contener el nuevo nombre
   * @param tipoAfijo indica si el afijo es prefijo (1) o sufijo (2)
   * @return nombre generado para el archivo
   */
  public static String bautizar(String nombre, String afijo, int tipoAfijo) {
    if (tipoAfijo == 1) {
      return afijo + obtenerCadenaFechaActual() + "." + FilenameUtils.getExtension(nombre);
    }
    if (tipoAfijo == 2) {
      return obtenerCadenaFechaActual() + afijo + "." + FilenameUtils.getExtension(nombre);
    } else {
      return null;
    }
  }

  public static String bautizar(String nombre, String nuevoNombre) {
    return nuevoNombre;
  }

  public static String bautizar(String nombre, String afijo, int tipoAfijo, String extension) {
    if (tipoAfijo == 1) {
      return afijo + obtenerCadenaFechaActual() + "." + extension;
    }
    if (tipoAfijo == 2) {
      return obtenerCadenaFechaActual() + afijo + "." + extension;
    } else {
      return null;
    }
  }
}
