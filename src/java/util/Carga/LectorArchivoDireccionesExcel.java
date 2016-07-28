/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.Carga;

import beans.ValidarDireccionesBean;
import dto.carga.FilaDireccionExcel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.el.ELContext;
import javax.faces.context.FacesContext;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
public class LectorArchivoDireccionesExcel {

  // VARIABLES DE CLASE
  private int columnaUtilCRE;
  private int columnaUtilCLL;
  private int columnaUtilEXT;
  private int columnaUtilINT;
  private int columnaUtilCOL;
  private int columnaUtilCOP;
  private int columnaUtilMUN;
  private int columnaUtilEDO;

  //CONSTRUCTOR
  public LectorArchivoDireccionesExcel() {
    columnaUtilCRE = -1;
    columnaUtilCLL = -1;
    columnaUtilEXT = -1;
    columnaUtilINT = -1;
    columnaUtilCOL = -1;
    columnaUtilCOP = -1;
    columnaUtilMUN = -1;
    columnaUtilEDO = -1;
  }

  // METODO QUE ABRE EL ARCHIVO DE EXCEL
  public String[] leerArchivoExcel(String ruta) {
    String[] arreglo = new String[2];
    try {
      FileInputStream archivo = new FileInputStream(new File(ruta));
      HSSFWorkbook libro = new HSSFWorkbook(archivo);
      HSSFSheet hoja = libro.getSheetAt(0);
      Row filaPivote = hoja.getRow(0);
      int numeroColumnas = filaPivote.getPhysicalNumberOfCells();
      for (int i = 0; i < numeroColumnas; i++) {
        Cell celda = filaPivote.getCell(i);
        switch (celda.getStringCellValue()) {
          case "CRE":
            columnaUtilCRE = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util CRE en la posicion " + (columnaUtilCRE + 1));
            break;
          case "CLL":
            columnaUtilCLL = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util CLL en la posicion " + (columnaUtilCLL + 1));
            break;
          case "EXT":
            columnaUtilEXT = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util REC en la posicion " + (columnaUtilEXT + 1));
            break;
          case "INT":
            columnaUtilINT = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util LIN en la posicion " + (columnaUtilINT + 1));
            break;
          case "COL":
            columnaUtilCOL = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util COL en la posicion " + (columnaUtilCOL + 1));
            break;
          case "COP":
            columnaUtilCOP = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util FAM en la posicion " + (columnaUtilCOP + 1));
            break;
          case "MUN":
            columnaUtilMUN = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util EST en la posicion " + (columnaUtilMUN + 1));
            break;
          case "EDO":
            columnaUtilEDO = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util MEV en la posicion " + (columnaUtilEDO + 1));
            break;
        }
      }
      int numeroFilas = hoja.getPhysicalNumberOfRows();
      List<FilaDireccionExcel> filasExcel = new ArrayList();
      DataFormatter fmt = new DataFormatter();
      for (int i = 1; i < numeroFilas; i++) {
        Row fila = hoja.getRow(i);
        FilaDireccionExcel f = new FilaDireccionExcel();
        for (int j = 0; j < numeroColumnas; j++) {
          Cell celdaActual = fila.getCell(j);
          if (celdaActual != null) {
            if (celdaActual.getColumnIndex() == columnaUtilCRE) {
              f.setNumeroCredito(fmt.formatCellValue(celdaActual));
            }
            if (celdaActual.getColumnIndex() == columnaUtilCLL) {
              f.setCalle(fmt.formatCellValue(celdaActual));
            }
            if (celdaActual.getColumnIndex() == columnaUtilEXT) {
              f.setExterior(fmt.formatCellValue(celdaActual));
            }
            if (celdaActual.getColumnIndex() == columnaUtilINT) {
              String aux = fmt.formatCellValue(celdaActual);
              switch (aux) {
                case "0":
                  f.setInterior(null);
                  break;
                case "SN":
                  f.setInterior(null);
                  break;
                case "S/N":
                  f.setInterior(null);
                  break;
                default:
                  f.setInterior(aux);
                  break;
              }
            }
            if (celdaActual.getColumnIndex() == columnaUtilCOL) {
              f.setColonia(fmt.formatCellValue(celdaActual));
            }
            if (celdaActual.getColumnIndex() == columnaUtilCOP) {
              f.setCp(fmt.formatCellValue(celdaActual));
            }
            if (celdaActual.getColumnIndex() == columnaUtilMUN) {
              f.setMunicipio(fmt.formatCellValue(celdaActual));
            }
            if (celdaActual.getColumnIndex() == columnaUtilEDO) {
              f.setEstado(fmt.formatCellValue(celdaActual));
            }
          }
        }
        filasExcel.add(f);
      }
      Logs.log.info("Se encontraron " + filasExcel.size() + " direcciones");
      arreglo[0] = "Paso 2 completado. Se encontraron " + filasExcel.size() + " direcciones.";
      arreglo[1] = "00CC33";
      ELContext elContext = FacesContext.getCurrentInstance().getELContext();
      ValidarDireccionesBean validarDireccionesBean = (ValidarDireccionesBean) elContext.getELResolver().getValue(elContext, null, "validarDireccionesBean");
      validarDireccionesBean.setDireccionesArchivo(filasExcel);
    } catch (FileNotFoundException fnfe) {
      Logs.log.error("No se encontro el archivo en la ruta especificada.");
      Logs.log.error(fnfe.getMessage());
      arreglo[0] = "Error. No se encontro el archivo de direcciones. Contacte al equipo de sistemas.";
      arreglo[1] = "FF0000";
    } catch (IOException ioe) {
      Logs.log.error("Error de lectura.");
      Logs.log.error(ioe.getMessage());
      arreglo[0] = "Error. Ocurrio un error de lectura en el archivo de direcciones. Contacte al equipo de sistemas.";
      arreglo[1] = "FF0000";
    }
    return arreglo;
  }

}
