package util;

public class Fecha {

  int dma[] = new int[3];
  int dmes[] = new int[13];
  int mes;

  public Fecha() {
    dmes[1] = dmes[3] = dmes[5] = dmes[7] = dmes[8] = dmes[10] = dmes[12] = 31;
    dmes[4] = dmes[6] = dmes[9] = dmes[11] = 30;
  }

  public String calcularFecha(int fechaEntera) {
    if(fechaEntera == 0)
      return "";
    
    int fecha = fechaEntera;
    int i;
    mes = 1;

    for (i = 0; i < 1000; i++) {
      if (i % 4 == 0) { // Es año bisiesto
        if (fecha <= 366) {
          dmes[2] = 29;
          mes(fecha, dmes[mes]);
          break;
        } else {
          fecha -= 366;
        }
      } else { // No es año bisiesto
        if (fecha <= 365) {
          dmes[2] = 28;
          mes(fecha, dmes[mes]);
          break;
        } else {
          fecha -= 365;
        }
      }
    }

    dma[2] = 1900 + i;
    return formatearFecha(dma);
  }
  
  private void mes(int dias, int diasDelMes) {
    if(dias > diasDelMes){
      mes(dias - diasDelMes, dmes[++mes]);
    } else {
      dma[0] = dias;
      dma[1] = mes;
    }
  }
  
  private String formatearFecha(int fecha[]) {
    String f;
    if(fecha[0] < 10)
      f = "0" + fecha[0] + "/";
    else
      f = fecha[0] + "/";
    
    if(fecha[1] < 10)
      f += "0" + fecha[1] + "/";
    else
      f += fecha[1] + "/";
    
    return f += fecha[2];
  }
}