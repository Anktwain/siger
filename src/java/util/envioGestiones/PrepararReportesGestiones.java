/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.envioGestiones;

import beans.ObtenerOracionCompletaGestionBean;
import dao.GestionDAO;
import dto.Gestion;
import impl.GestionIMPL;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import util.constantes.Directorios;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
public class PrepararReportesGestiones {

  // VARIABLES DE CLASE
  private final GestionDAO gestionDao;

  // CONSTRUCTOR
  public PrepararReportesGestiones() {
    gestionDao = new GestionIMPL();
  }

  // METODO QUE PREPARA LOS REPORTES DE GESTIONES
  public boolean crearReportesExcel(String ruta) {
    boolean ok;
    // DETECTAR LAS FECHAS DE LA SEMANA ANTERIOR
    Calendar c = Calendar.getInstance();
    c.add(Calendar.DATE, -3);
    Date ultimoViernes = c.getTime();
    c.add(Calendar.DATE, -4);
    Date ultimoLunes = c.getTime();
    // SELECCIONAR TODOS LOS DATOS DE INTERES EN EL RANGO ESPECIFICADO
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String fechaLunes = df.format(ultimoLunes);
    String fechaViernes = df.format(ultimoViernes);
    String consulta = "SELECT * FROM gestion WHERE DATE(fecha) BETWEEN '" + fechaLunes + "' AND '" + fechaViernes + "' ";
    consulta = consulta + "AND id_descripcion_gestion != 117 ";
    String consultaCT = consulta + "AND id_credito IN (SELECT id_credito FROM credito WHERE id_subproducto IN (SELECT id_subproducto FROM subproducto WHERE nombre IN ('CT EXPRESS PF', 'CT EXPRESS PM', 'EXPRESS ABIERTO PF', 'EXPRESS ABIERTO PM'))) ";
    String consultaNoCT = consulta + "AND id_credito IN (SELECT id_credito FROM credito WHERE id_subproducto NOT IN (SELECT id_subproducto FROM subproducto WHERE nombre IN ('CT EXPRESS PF', 'CT EXPRESS PM', 'EXPRESS ABIERTO PF', 'EXPRESS ABIERTO PM'))) ";
    List<Gestion> gestionesCT = gestionDao.busquedaReporteGestionesDespacho(consultaCT);
    List<Gestion> gestionesNoCT = gestionDao.busquedaReporteGestionesDespacho(consultaNoCT);
    // NOMBRAR LOS ARCHIVOS ADECUADAMENTE
    String rutaCT = Directorios.RUTA_WINDOWS_REPORTES_GESTIONES + "Gestiones-CT-Express_" + fechaLunes + "_" + fechaViernes + ".xls";
    String rutaNoCT = Directorios.RUTA_WINDOWS_REPORTES_GESTIONES + "Gestiones-No-CT-Express_" + fechaLunes + "_" + fechaViernes + ".xls";
    // CREAR 2 ARCHIVOS DE EXCEL. UNO PARA CT EXPRES Y OTRO PARA LAS QUE NO SON CT EXPRESS
    ok = crearArchivoExcel(gestionesCT, rutaCT);
    ok = ok & (crearArchivoExcel(gestionesNoCT, rutaNoCT));
    EnviarCorreoGestiones ecg = new EnviarCorreoGestiones();
    ok = ok & (ecg.enviarCorreoInbursa(rutaCT, rutaNoCT));
    if (ok) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se enviaron los reportes de gestiones automaticamente."));
      Logs.log.info("Se enviaron los reportes de gestiones automaticamente.");
    } else {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se enviaron los reportes de gestiones. Contacte al equipo de sistemas."));
      Logs.log.error("No se enviaron los reportes de gestiones.");
    }
    return ok;
  }

  // BUG:
  // se comenta porque aun no se habilitara el envio automatico de gestiones
  // METODO ORIGINAL
  // METODO QUE PREPARA LOS REPORTES DE GESTIONES EN EXCEL
  /*
   public boolean crearReportesExcel(String ruta) {
   boolean ok;
   // DETECTAR LAS FECHAS DE LA SEMANA ANTERIOR
   Calendar c = Calendar.getInstance();
   c.add(Calendar.DATE, -3);
   Date ultimoViernes = c.getTime();
   c.add(Calendar.DATE, -4);
   Date ultimoLunes = c.getTime();
   // SELECCIONAR TODOS LOS DATOS DE INTERES EN EL RANGO ESPECIFICADO
   SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
   String fechaLunes = df.format(ultimoLunes);
   String fechaViernes = df.format(ultimoViernes);
   String consulta = "SELECT * FROM gestion WHERE DATE(fecha) BETWEEN '" + fechaLunes + "' AND '" + fechaViernes + "' ";
   consulta = consulta + "AND id_descripcion_gestion != 117 ";
   String consultaCT = consulta + "AND id_credito IN (SELECT id_credito FROM credito WHERE id_subproducto IN (SELECT id_subproducto FROM subproducto WHERE nombre IN ('CT EXPRESS PF', 'CT EXPRESS PM', 'EXPRESS ABIERTO PF', 'EXPRESS ABIERTO PM'))) ";
   String consultaNoCT = consulta + "AND id_credito IN (SELECT id_credito FROM credito WHERE id_subproducto NOT IN (SELECT id_subproducto FROM subproducto WHERE nombre IN ('CT EXPRESS PF', 'CT EXPRESS PM', 'EXPRESS ABIERTO PF', 'EXPRESS ABIERTO PM'))) ";
   List<Gestion> gestionesCT = gestionDao.busquedaReporteGestionesDespacho(consultaCT);
   List<Gestion> gestionesNoCT = gestionDao.busquedaReporteGestionesDespacho(consultaNoCT);
   // NOMBRAR LOS ARCHIVOS ADECUADAMENTE
   String rutaCT = Directorios.RUTA_WINDOWS_REPORTES_GESTIONES + "Gestiones-CT-Express_" + fechaLunes + "_" + fechaViernes + ".xls";
   String rutaNoCT = Directorios.RUTA_WINDOWS_REPORTES_GESTIONES + "Gestiones-No-CT-Express_" + fechaLunes + "_" + fechaViernes + ".xls";
   // CREAR 2 ARCHIVOS DE EXCEL. UNO PARA CT EXPRES Y OTRO PARA LAS QUE NO SON CT EXPRESS
   ok = crearArchivoExcel(gestionesCT, rutaCT);
   ok = ok & (crearArchivoExcel(gestionesNoCT, rutaNoCT));
   if (ok) {
   // SE DEBERA VERIFICAR LA HORA
   c = Calendar.getInstance();
   // BUG:
   // el horario de envio es de 11:50 a 12:00
   if (((c.get(Calendar.HOUR_OF_DAY) > 0) && (c.get(Calendar.HOUR_OF_DAY) < 13)) || ((c.get(Calendar.HOUR_OF_DAY) == 13) && (c.get(Calendar.MINUTE) < 30))) {
   Logs.log.info("Aun no es tiempo de enviar el reporte de gestiones. Se ha programado el envio de gestiones.");
   // CREAR EL EVENTO DE ENVIO
   Calendar ahora = Calendar.getInstance();
   // BUG:
   // se debera cambiar el tiempo por 11 y 50 respectivamente
   // SE CALCULAN LOS MILISEGUNDOS DE LAS 0:00 HORAS A LAS 11:50
   long milis1150 = (13 * 3600000) + (30 * 60000);
   System.out.println("MILIS HASTA LAS 11:50 " + milis1150);
   // SE CALCULAN LOS MILISEGUNDOS DE LAS 0:00 HORAS AL MOMENTO ACTUAL
   long milisActuales = ((ahora.get(Calendar.HOUR_OF_DAY) * 3600000) + ((ahora.get(Calendar.MINUTE) * 60000)) + (ahora.get(Calendar.SECOND) * 1000));
   System.out.println("MILIS ACTUALES: " + milisActuales);
   // SE CALCULA EL RETARDO CON EL QUE INICIARA EL METODO
   long retardo = milis1150 - milisActuales;
   // SI EL RETARDO ES NEGATIVO, SE EMPEZARA AHORA CON EL METODO
   if (retardo < 0) {
   retardo = 0;
   }
   Logs.log.info("El retardo para el envio sera de " + retardo + " milisegundos");
   ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
   scheduler.scheduleAtFixedRate(new ProgramadorEnvioGestiones(), retardo, 2, TimeUnit.MINUTES);
   // BUG:
   // el horario de envio es de 11:50 a 12:00
   } else if ((c.get(Calendar.HOUR_OF_DAY) == 13) && (c.get(Calendar.MINUTE) > 30)) {
   Logs.log.info("Es tiempo de enviar el reporte de gestiones");
   // ENVIAR EL CORREO
   EnviarCorreoGestiones ecg = new EnviarCorreoGestiones();
   ok = ok & (ecg.enviarCorreoInbursa(rutaCT, rutaNoCT));
   if (ok) {
   Logs.log.info("Se enviaron los reportes de gestiones automaticamente.");
   } else {
   Logs.log.error("No se enviaron los reportes de gestiones.");
   }
   } else {
   Logs.log.info("El tiempo para enviar las gestiones ha concluido.");
   }
   } else {
   Logs.log.error("No se crearon los reportes de gestiones correctamente.");
   }
   return ok;
   }
   */
  
  // METODO QUE GENERA UN ARCHIVO DE EXCEL CON LOS DATOS DE LA LISTA OBTENIDA
  public boolean crearArchivoExcel(List<Gestion> gestiones, String ruta) {
    boolean ok;
    HSSFWorkbook libro = new HSSFWorkbook();
    HSSFSheet hoja = libro.createSheet("Gestiones");
    int filas = 0;
    Row fila = hoja.createRow(filas++);
    // SE CREA UN ESTILO PARA LOS ENCABEZADOS
    CellStyle estilo = libro.createCellStyle();
    estilo.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
    estilo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    // SE CREAN LOS ENCABEZADOS
    Cell celda = fila.createCell(0);
    celda.setCellStyle(estilo);
    celda.setCellValue("Credito");
    celda.setCellStyle(estilo);
    celda = fila.createCell(1);
    celda.setCellValue("Deudor");
    celda.setCellStyle(estilo);
    celda = fila.createCell(2);
    celda.setCellValue("Producto");
    celda.setCellStyle(estilo);
    celda = fila.createCell(3);
    celda.setCellValue("Fecha");
    celda.setCellStyle(estilo);
    celda = fila.createCell(4);
    celda.setCellValue("Gestion");
    celda.setCellStyle(estilo);
    celda = fila.createCell(5);
    celda.setCellValue("Estatus Banco");
    celda.setCellStyle(estilo);
    // SE CREAN TODOS LOS REGISTROS
    for (int i = 0; i < (gestiones.size()); i++) {
      Row filaGestion = hoja.createRow(filas++);
      escribirGestion(gestiones.get(i), filaGestion);
    }
    // SE AJUSTAN LAS COLUMNAS
    hoja.autoSizeColumn(0);
    hoja.setColumnWidth(1, 24 * 450);
    hoja.autoSizeColumn(2);
    hoja.autoSizeColumn(3);
    hoja.setColumnWidth(4, 24 * 950);
    hoja.autoSizeColumn(5);
    try {
      FileOutputStream outputStream = new FileOutputStream(ruta);
      libro.write(outputStream);
      ok = true;
    } catch (FileNotFoundException fnfe) {
      Logs.log.error("No se pudo generar el reporte de gestiones en la ruta especificada.");
      Logs.log.error(fnfe.getMessage());
      ok = false;
    } catch (IOException ioe) {
      Logs.log.error("No se pudo generar el reporte de gestiones.");
      Logs.log.error(ioe.getMessage());
      ok = false;
    }
    return ok;
  }

  // METODO QUE ESCRIBE UNA GESTION EN UNA FILA DE EXCEL
  private void escribirGestion(Gestion g, Row fila) {
    Cell celda = fila.createCell(0);
    celda.setCellValue(g.getCredito().getNumeroCredito());
    celda = fila.createCell(1);
    celda.setCellValue(g.getCredito().getDeudor().getSujeto().getNombreRazonSocial());
    celda = fila.createCell(2);
    celda.setCellValue(g.getCredito().getSubproducto().getNombre());
    celda = fila.createCell(3);
    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    celda.setCellValue(df.format(g.getFecha()));
    celda = fila.createCell(4);
    ObtenerOracionCompletaGestionBean oocgb = new ObtenerOracionCompletaGestionBean();
    celda.setCellValue(oocgb.obtenerOracion(g));
    celda = fila.createCell(5);
    celda.setCellValue(g.getEstatusInformativo().getEstatus());
  }

}
