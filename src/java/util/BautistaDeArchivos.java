package util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author antonio
 */
public class BautistaDeArchivos {

  public static final int PREFIJO = 1;
  public static final int SUFIJO = 2;

  /**
   * Obtiene una cadena de caracteres formada por la fecha y hora actuales.
   *
   * @return cadena formada.
   */
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
   * Pone un nombre a un archivo. Este nombre se genera tomando como base la
   * fecha y hora actuales, de esa manera se evitan homonimias.
   *
   * @param nombre el nombre original del archivo.
   * @param afijo es un prefijo o sufijo que deberá contener el nuevo nombre.
   * @param tipoAfijo indica si el afijo es prefijo (1) o sufijo (2).
   * @return nombre generado para el archivo.
   */
  public static String bautizar(String nombre, String afijo, int tipoAfijo) {
    if (tipoAfijo == PREFIJO) {
      return afijo + obtenerCadenaFechaActual() + "." + FilenameUtils.getExtension(nombre);
    }
    if (tipoAfijo == SUFIJO) {
      return obtenerCadenaFechaActual() + afijo + "." + FilenameUtils.getExtension(nombre);
    } else {
      return null;
    }
  }

  /**
   * Pone un nombre a un archivo. El nombre del archivo es indicado por el
   * usuario por medio del parámetro enviado al método.
   *
   * @param nuevoNombre el nuevo nombre para del archivo.
   * @return nombre generado para el archivo.
   */
  public static String bautizar(String nuevoNombre) {
    return nuevoNombre;
  }

  /**
   * Pone un nombre a un archivo. Este nombre se genera tomando como base la
   * fecha y hora actuales, de esa manera se evitan homonimias.
   *
   * @param afijo es un prefijo o sufijo que deberá contener el nuevo nombre.
   * @param tipoAfijo indica si el afijo es prefijo (1) o sufijo (2).
   * @param extension la extensión que deberá tener el archivo.
   * @return nombre generado para el archivo.
   */
  public static String bautizar(String afijo, int tipoAfijo, String extension) {
    if (tipoAfijo == PREFIJO) {
      return afijo + obtenerCadenaFechaActual() + "." + extension;
    }
    if (tipoAfijo == SUFIJO) {
      return obtenerCadenaFechaActual() + afijo + "." + extension;
    } else {
      return null;
    }
  }
}
