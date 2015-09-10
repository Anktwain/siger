/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.File;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import jxl.Sheet;
import jxl.Workbook;
import util.log.Logs;

/**
 *
 * @author brionvega
 */
@ManagedBean
@ViewScoped
public class LeerExcel implements Serializable{

  private int numeroDeFilas;
  private int numeroDeColumnas;
  private Workbook archivoExcel;
  private Sheet hojaExcel;

  public int leerArchivoExcel(String nombreArchivo) {
    try {
      archivoExcel = Workbook.getWorkbook(new File(nombreArchivo));
      System.out.println("Leyendo: " + nombreArchivo);
      hojaExcel = archivoExcel.getSheet(0);
      numeroDeFilas = hojaExcel.getRows();
      numeroDeColumnas = hojaExcel.getColumns();
    } catch (Exception ioe) {
      Logs.log.error("No se pudo leer archivo:");
      Logs.log.error(ioe.getMessage());

    } finally {
      System.out.println("El archivo tiene " + numeroDeFilas + " filas y " + numeroDeColumnas + " columnas");
    }
    return numeroDeFilas - 1;
  }
  
}
