/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.carga;

import beans.PrecargaBean.ColumnaUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
// 2A REESTRUCTURACION DEL MODULO DE CARGA
// AGOSTO 2016
public class NuevoLectorArchivoCreditosExcel {

  // ***************************************************************************
  // VARIABLES DE CLASE
  // ***************************************************************************
  private int columnaUtilCRE;
  private int columnaUtilNOM;
  private int columnaUtilREC;
  private int columnaUtilLIN;
  private int columnaUtilPRO;
  private int columnaUtilFAM;
  private int columnaUtilEST;
  private int columnaUtilMEV;
  private int columnaUtilDES;
  private int columnaUtilFIC;
  private int columnaUtilFVC;
  private int columnaUtilFQB;
  private int columnaUtilDIS;
  private int columnaUtilMEN;
  private int columnaUtilSAI;
  private int columnaUtilSAV;
  private int columnaUtilTIN;
  private int columnaUtilCUE;
  private int columnaUtilFUP;
  private int columnaUtilFUV;
  private int columnaUtilCTE;
  private int columnaUtilRFC;
  private int columnaUtilAJAN;
  private int columnaUtilAJAC;
  private int columnaUtilFAC_ENE;
  private int columnaUtilFAC_FEB;
  private int columnaUtilFAC_MAR;
  private int columnaUtilFAC_ABR;
  private int columnaUtilFAC_MAY;
  private int columnaUtilFAC_JUN;
  private int columnaUtilFAC_JUL;
  private int columnaUtilFAC_AGO;
  private int columnaUtilFAC_SEP;
  private int columnaUtilFAC_OCT;
  private int columnaUtilFAC_NOV;
  private int columnaUtilFAC_DIC;
  private int columnaUtilMO_ENE;
  private int columnaUtilMO_FEB;
  private int columnaUtilMO_MAR;
  private int columnaUtilMO_ABR;
  private int columnaUtilMO_MAY;
  private int columnaUtilMO_JUN;
  private int columnaUtilMO_JUL;
  private int columnaUtilMO_AGO;
  private int columnaUtilMO_SEP;
  private int columnaUtilMO_OCT;
  private int columnaUtilMO_NOV;
  private int columnaUtilMO_DIC;

  // ***************************************************************************
  // CONSTRUCTOR
  // ***************************************************************************
  public NuevoLectorArchivoCreditosExcel() {
  }

  // ***************************************************************************
  // METODO QUE LEE EL ARCHIVO DE EXCEL Y CREA LOS OBJETOS FILA
  // ***************************************************************************
  public void leerArchivoExcel(ColumnaUtil[] columnas, String ruta) {
    try {
      FileInputStream archivo = new FileInputStream(new File(ruta));
      HSSFWorkbook libro = new HSSFWorkbook(archivo);
      int hojas = libro.getNumberOfSheets();
      seleccionarColumnas(columnas, hojas);
      for (int i = 0; i < hojas; i++) {
        int numeroFilas = libro.getSheetAt(i).getPhysicalNumberOfRows();
        for (int j = 0; j < (numeroFilas); j++) {
          // TO DO:
          // AQUI SE DEBEN CONSEGUIR LOS DATOS PARA EL OBJETO CREDITO EXCEL
          // ORA' SI QUE EMPIEZE LA OBTENCION
        }
      }
    } catch (FileNotFoundException fnfe) {
      Logs.log.error("No se encontro el archivo en la ruta especificada.");
      Logs.log.error(fnfe.getMessage());
    } catch (IOException ioe) {
      Logs.log.error("Error de lectura.");
      Logs.log.error(ioe.getMessage());
    }
  }

  // ***************************************************************************
  // METODO QUE VERIFICA LAS COLUMNAS QUE SE UTILIZARAN EN LA PRECARGA
  // ***************************************************************************
  public void seleccionarColumnas(ColumnaUtil[] columnas, int hojas) {
    System.out.println("COLUMNAS INICIALES");
    for (int i = 0; i < (columnas.length); i++) {
      System.out.println(columnas[i].getAbreviatura() + " HOJA " + columnas[i].getHoja());
    }
    for (int i = 0; i < hojas; i++) {
      for (int j = 0; j < (columnas.length); j++) {
        if (columnas[j].getHoja() == i) {
          switch (columnas[j].getAbreviatura()) {
            case "CRE":
              columnaUtilCRE = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util CRE en la posicion " + (columnaUtilCRE + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "NOM":
              columnaUtilNOM = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util NOM en la posicion " + (columnaUtilNOM + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "REC":
              columnaUtilREC = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util REC en la posicion " + (columnaUtilREC + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "LIN":
              columnaUtilLIN = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util LIN en la posicion " + (columnaUtilLIN + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "PRO":
              columnaUtilPRO = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util PRO en la posicion " + (columnaUtilPRO + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "FAM":
              columnaUtilFAM = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util FAM en la posicion " + (columnaUtilFAM + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "EST":
              columnaUtilEST = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util EST en la posicion " + (columnaUtilEST + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "MEV":
              columnaUtilMEV = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util MEV en la posicion " + (columnaUtilMEV + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "DES":
              columnaUtilDES = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util DES en la posicion " + (columnaUtilDES + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "FIC":
              columnaUtilFIC = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util FIC en la posicion " + (columnaUtilFIC + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "FVC":
              columnaUtilFVC = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util FVC en la posicion " + (columnaUtilFVC + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "FQB":
              columnaUtilFQB = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util FQB en la posicion " + (columnaUtilFQB + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "DIS":
              columnaUtilDIS = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util DIS en la posicion " + (columnaUtilDIS + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "MEN":
              columnaUtilMEN = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util MEN en la posicion " + (columnaUtilMEN + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "SAI":
              columnaUtilSAI = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util SAI en la posicion " + (columnaUtilSAI + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "SAV":
              columnaUtilSAV = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util SAV en la posicion " + (columnaUtilSAV + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "TIN":
              columnaUtilTIN = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util TIN en la posicion " + (columnaUtilTIN + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "CUE":
              columnaUtilCUE = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util CUE en la posicion " + (columnaUtilCUE + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "FUP":
              columnaUtilFUP = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util FUP en la posicion " + (columnaUtilFUP + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "FUV":
              columnaUtilFUV = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util FUV en la posicion " + (columnaUtilFUV + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "CTE":
              columnaUtilCTE = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util CTE en la posicion " + (columnaUtilCTE + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "RFC":
              columnaUtilRFC = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util RFC en la posicion " + (columnaUtilRFC + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "AJAN":
              columnaUtilAJAN = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util AJAN en la posicion " + (columnaUtilAJAN + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "AJAC":
              columnaUtilAJAC = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util AJAC en la posicion " + (columnaUtilAJAC + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "FAC-ENE":
              columnaUtilFAC_ENE = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util FAC-ENE en la posicion " + (columnaUtilFAC_ENE + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "FAC-FEB":
              columnaUtilFAC_FEB = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util FAC-FEB en la posicion " + (columnaUtilFAC_FEB + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "FAC-MAR":
              columnaUtilFAC_MAR = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util FAC-MAR en la posicion " + (columnaUtilFAC_MAR + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "FAC-ABR":
              columnaUtilFAC_ABR = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util FAC-ABR en la posicion " + (columnaUtilFAC_ABR + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "FAC-MAY":
              columnaUtilFAC_MAY = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util FAC-MAY en la posicion " + (columnaUtilFAC_MAY + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "FAC-JUN":
              columnaUtilFAC_JUN = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util FAC-JUN en la posicion " + (columnaUtilFAC_JUN + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "FAC-JUL":
              columnaUtilFAC_JUL = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util FAC-JUL en la posicion " + (columnaUtilFAC_JUL + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "FAC-AGO":
              columnaUtilFAC_AGO = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util FAC-AGO en la posicion " + (columnaUtilFAC_AGO + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "FAC-SEP":
              columnaUtilFAC_SEP = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util FAC-SEP en la posicion " + (columnaUtilFAC_SEP + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
            case "FAC-OCT":
              columnaUtilFAC_OCT = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util FAC-OCT en la posicion " + (columnaUtilFAC_OCT + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "FAC-NOV":
              columnaUtilFAC_NOV = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util FAC-NOV en la posicion " + (columnaUtilFAC_NOV + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "FAC-DIC":
              columnaUtilFAC_DIC = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util FAC-DIC en la posicion " + (columnaUtilFAC_DIC + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "MO-ENE":
              columnaUtilMO_ENE = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util MO-ENE en la posicion " + (columnaUtilMO_ENE + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "MO-FEB":
              columnaUtilMO_FEB = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util MO-FEB en la posicion " + (columnaUtilMO_FEB + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "MO-MAR":
              columnaUtilMO_MAR = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util MO-MAR en la posicion " + (columnaUtilMO_MAR + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "MO-ABR":
              columnaUtilMO_ABR = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util MO-ABR en la posicion " + (columnaUtilMO_ABR + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "MO-MAY":
              columnaUtilMO_MAY = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util MO-MAY en la posicion " + (columnaUtilMO_MAY + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "MO-JUN":
              columnaUtilMO_JUN = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util MO-JUN en la posicion " + (columnaUtilMO_JUN + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "MO-JUL":
              columnaUtilMO_JUL = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util MO-JUL en la posicion " + (columnaUtilMO_JUL + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "MO-AGO":
              columnaUtilMO_AGO = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util MO-AGO en la posicion " + (columnaUtilMO_AGO + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "MO-SEP":
              columnaUtilMO_SEP = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util MO-SEP en la posicion " + (columnaUtilMO_SEP + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "MO-OCT":
              columnaUtilMO_OCT = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util MO-OCT en la posicion " + (columnaUtilMO_OCT + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "MO-NOV":
              columnaUtilMO_NOV = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util MO-NOV en la posicion " + (columnaUtilMO_NOV + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
            case "MO-DIC":
              columnaUtilMO_DIC = buscadorNumeroColumna(columnas[j].getColumna());
              Logs.log.info("Se encontro columna util MO-DIC en la posicion " + (columnaUtilMO_DIC + 1) + " de la hoja " + columnas[j].getHoja());
              columnas = ArrayUtils.remove(columnas, i);
              break;
          }
        }
      }
      System.out.println("COLUMNAS RESTANTES HOJA " + (i + 1) + ":");
      for (int j = 0; j < (columnas.length); j++) {
        System.out.println(columnas[j].getAbreviatura() + " HOJA " + columnas[j].getHoja());
      }
    }
  }

  // ***************************************************************************
  // METODO QUE REGRESA UN NUMERO DEPENDIENDO LA COLUMNA ENTREGADA
  // ***************************************************************************
  public int buscadorNumeroColumna(String columna) {
    int valorColumna = 0;
    switch (columna) {
      case "A":
        valorColumna = valorColumna + 0;
        break;
      case "B":
        valorColumna = valorColumna + 1;
        break;
      case "C":
        valorColumna = valorColumna + 2;
        break;
      case "D":
        valorColumna = valorColumna + 3;
        break;
      case "E":
        valorColumna = valorColumna + 4;
        break;
      case "F":
        valorColumna = valorColumna + 5;
        break;
      case "G":
        valorColumna = valorColumna + 6;
        break;
      case "H":
        valorColumna = valorColumna + 7;
        break;
      case "I":
        valorColumna = valorColumna + 8;
        break;
      case "J":
        valorColumna = valorColumna + 9;
        break;
      case "K":
        valorColumna = valorColumna + 10;
        break;
      case "L":
        valorColumna = valorColumna + 11;
        break;
      case "M":
        valorColumna = valorColumna + 12;
        break;
      case "N":
        valorColumna = valorColumna + 13;
        break;
      case "O":
        valorColumna = valorColumna + 14;
        break;
      case "P":
        valorColumna = valorColumna + 15;
        break;
      case "Q":
        valorColumna = valorColumna + 16;
        break;
      case "R":
        valorColumna = valorColumna + 17;
        break;
      case "S":
        valorColumna = valorColumna + 18;
        break;
      case "T":
        valorColumna = valorColumna + 19;
        break;
      case "U":
        valorColumna = valorColumna + 20;
        break;
      case "V":
        valorColumna = valorColumna + 21;
        break;
      case "W":
        valorColumna = valorColumna + 22;
        break;
      case "X":
        valorColumna = valorColumna + 23;
        break;
      case "Y":
        valorColumna = valorColumna + 24;
        break;
      case "Z":
        valorColumna = valorColumna + 25;
        break;
      case "AA":
        valorColumna = valorColumna + 26;
        break;
      case "AB":
        valorColumna = valorColumna + 27;
        break;
      case "AC":
        valorColumna = valorColumna + 28;
        break;
      case "AD":
        valorColumna = valorColumna + 29;
        break;
      case "AE":
        valorColumna = valorColumna + 30;
        break;
      case "AF":
        valorColumna = valorColumna + 31;
        break;
      case "AG":
        valorColumna = valorColumna + 32;
        break;
      case "AH":
        valorColumna = valorColumna + 33;
        break;
      case "AI":
        valorColumna = valorColumna + 34;
        break;
      case "AJ":
        valorColumna = valorColumna + 35;
        break;
      case "AK":
        valorColumna = valorColumna + 36;
        break;
      case "AL":
        valorColumna = valorColumna + 37;
        break;
      case "AM":
        valorColumna = valorColumna + 38;
        break;
      case "AN":
        valorColumna = valorColumna + 39;
        break;
      case "AO":
        valorColumna = valorColumna + 40;
        break;
      case "AP":
        valorColumna = valorColumna + 41;
        break;
      case "AQ":
        valorColumna = valorColumna + 42;
        break;
      case "AR":
        valorColumna = valorColumna + 43;
        break;
      case "AS":
        valorColumna = valorColumna + 44;
        break;
      case "AT":
        valorColumna = valorColumna + 45;
        break;
      case "AU":
        valorColumna = valorColumna + 46;
        break;
      case "AV":
        valorColumna = valorColumna + 47;
        break;
      case "AW":
        valorColumna = valorColumna + 48;
        break;
      case "AX":
        valorColumna = valorColumna + 49;
        break;
      case "AY":
        valorColumna = valorColumna + 50;
        break;
      case "AZ":
        valorColumna = valorColumna + 51;
        break;
    }
    return valorColumna;
  }

}
