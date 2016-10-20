/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.envioGestiones;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import util.constantes.Directorios;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
public class ProgramadorEnvioGestiones implements Runnable {

  @Override
  public void run() {
    System.out.println("ENTRO AL METODO");
    // DETECTAR LAS FECHAS DE LA SEMANA ANTERIOR
    Calendar c = Calendar.getInstance();
    c.add(Calendar.DATE, -3);
    Date ultimoViernes = c.getTime();
    c.add(Calendar.DATE, -4);
    Date ultimoLunes = c.getTime();
    // SELECCIONAR TODOS LOS DATOS DE INTERES EN EL RANGO ESPECIFICADO
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    // NOMBRAR LOS ARCHIVOS ADECUADAMENTE
    String rutaCT = Directorios.RUTA_WINDOWS_REPORTES_GESTIONES + "Gestiones-CT-Express_" + df.format(ultimoLunes) + "_" + df.format(ultimoViernes) + ".xls";
    String rutaNoCT = Directorios.RUTA_WINDOWS_REPORTES_GESTIONES + "Gestiones-No-CT-Express_" + df.format(ultimoLunes) + "_" + df.format(ultimoViernes) + ".xls";
    // ENVIAR EL CORREO
    EnviarCorreoGestiones ecg = new EnviarCorreoGestiones();
    if (ecg.enviarCorreoInbursa(rutaCT, rutaNoCT)) {
      Logs.log.info("Se enviaron los reportes de gestiones automaticamente.");
    } else {
      Logs.log.error("No se enviaron los reportes de gestiones.");
    }
  }

}
