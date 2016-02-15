package util;

public class Fecha {

  /* Día, mes y año. El primer elemento del array representa el número de día; el
   segundo elemento es el número de mes; finalemnte el tercer elemento corresponde
   al año. */
  int dma[] = new int[3];

  /* Contiene el número de días que tiene cada mes. dmes[1]: días que contiene
   enero; dmes[2]: días que contiene febrero, dmes[3]: días que contiene marzo, ...*/
  int dmes[] = new int[13];
  int mes; // Número de mes. 

  /* Inicializa objeto colocando el número de días por mes. Excepto para el mes
   de frebrero. ese mes requiere un tratamiento especial. */
  public Fecha() {
    dmes[1] = dmes[3] = dmes[5] = dmes[7] = dmes[8] = dmes[10] = dmes[12] = 31;
    dmes[4] = dmes[6] = dmes[9] = dmes[11] = 30;
  }

  /**
   * Método que hace transforma un dato de fecha en su forma adecuada. La fecha
   * deberá mostrarse en el archivo de carga como una cadena que representa un
   * valor entero.
   *
   * @param fechaEntera La fecha, en forma de entero, que va a ser procesada.
   *
   * @return La fecha, en forma de String, ya procesada.
   */
  public String calcularFecha(int fechaEntera) {
    /* Si la fecha contiene el valor 0, simplemente se considera como una cadena
     vacía, y se regresa esa cadena. */
    if (fechaEntera == 0) {
      return "";
    }

    int fecha = fechaEntera;
    int i;
    mes = 1;

    /* Procesamiento para conocer el número de días que tiene el mes de febrero
     para un determinado año. */
    for (i = 0; i < 1000; i++) {
      if (i % 4 == 0) { // Es año bisiesto.
        if (fecha <= 366) {
          dmes[2] = 29; // Como es año bisiesto, febrero tiene 29 días.
          mes(fecha, dmes[mes]);
          break;
        } else {
          fecha -= 366; // Continúa procesamiento.
        }
      } else { // No es año bisiesto.
        if (fecha <= 365) {
          dmes[2] = 28; // Como no es año bisiesto, febrero tiene 28 días.
          mes(fecha, dmes[mes]);
          break;
        } else {
          fecha -= 365; // Continúa procesamiento.
        }
      }
    }

    dma[2] = 1900 + i;
    return formatearFecha(dma);
  }

  private void mes(int dias, int diasDelMes) {
    if (dias > diasDelMes) {
      mes(dias - diasDelMes, dmes[++mes]);
    } else {
      dma[0] = dias;
      dma[1] = mes;
    }
  }

  /**
   * Método que formatea una fecha entera a un formato más fácil de leer.
   *
   * @param fecha[] array que representa días mes y año, en números enteros.
   *
   * @return La fecha fomateada como "dd/mm/aaaa".
   */
  private String formatearFecha(int fecha[]) {
    String f;
    if (fecha[0] < 10) {
      f = "0" + fecha[0] + "/";
    } else {
      f = fecha[0] + "/";
    }

    if (fecha[1] < 10) {
      f += "0" + fecha[1] + "/";
    } else {
      f += fecha[1] + "/";
    }

    return f += fecha[2];
  }
  
  public String convertirFormatoMySQL(String fecha) {
    String[] f = fecha.split("/");
    
    return f[2] + "-" + f[1] + "-" + f[0];
  }
}