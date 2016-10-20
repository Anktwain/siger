/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.carga;

import beans.NuevaCargaBean;
import dto.carga.FilaCreditoExcel;
import dto.carga.FilaCreditoExcel.Ajustes;
import dto.carga.FilaCreditoExcel.Facs;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
// REESTRUCTURACION DEL MODULO DE CARGA
public class LectorArchivoCreditosExcel {

  // ***************************************************************************
  // ***************************************************************************
  // LLAMADA A OTROS BEANS
  // ***************************************************************************
  // ***************************************************************************
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  NuevaCargaBean nuevaCargaBean = (NuevaCargaBean) elContext.getELResolver().getValue(elContext, null, "nuevaCargaBean");

  // ***************************************************************************
  // ***************************************************************************
  // VARIABLES DE CLASE
  // ***************************************************************************
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
  private int columnaUtilMON_ENE;
  private int columnaUtilMON_FEB;
  private int columnaUtilMON_MAR;
  private int columnaUtilMON_ABR;
  private int columnaUtilMON_MAY;
  private int columnaUtilMON_JUN;
  private int columnaUtilMON_JUL;
  private int columnaUtilMON_AGO;
  private int columnaUtilMON_SEP;
  private int columnaUtilMON_OCT;
  private int columnaUtilMON_NOV;
  private int columnaUtilMON_DIC;

  // ***************************************************************************
  // ***************************************************************************
  // CONSTRUCTOR
  // ***************************************************************************
  // ***************************************************************************
  public LectorArchivoCreditosExcel() {
    columnaUtilCRE = -1;
    columnaUtilNOM = -1;
    columnaUtilREC = -1;
    columnaUtilLIN = -1;
    columnaUtilPRO = -1;
    columnaUtilFAM = -1;
    columnaUtilEST = -1;
    columnaUtilMEV = -1;
    columnaUtilDES = -1;
    columnaUtilFIC = -1;
    columnaUtilFVC = -1;
    columnaUtilFQB = -1;
    columnaUtilDIS = -1;
    columnaUtilMEN = -1;
    columnaUtilSAI = -1;
    columnaUtilSAV = -1;
    columnaUtilTIN = -1;
    columnaUtilCUE = -1;
    columnaUtilFUP = -1;
    columnaUtilFUV = -1;
    columnaUtilCTE = -1;
    columnaUtilRFC = -1;
    columnaUtilAJAN = -1;
    columnaUtilAJAC = -1;
    columnaUtilFAC_ENE = -1;
    columnaUtilFAC_FEB = -1;
    columnaUtilFAC_MAR = -1;
    columnaUtilFAC_ABR = -1;
    columnaUtilFAC_MAY = -1;
    columnaUtilFAC_JUN = -1;
    columnaUtilFAC_JUL = -1;
    columnaUtilFAC_AGO = -1;
    columnaUtilFAC_SEP = -1;
    columnaUtilFAC_OCT = -1;
    columnaUtilFAC_NOV = -1;
    columnaUtilFAC_DIC = -1;
    columnaUtilMON_ENE = -1;
    columnaUtilMON_FEB = -1;
    columnaUtilMON_MAR = -1;
    columnaUtilMON_ABR = -1;
    columnaUtilMON_MAY = -1;
    columnaUtilMON_JUN = -1;
    columnaUtilMON_JUL = -1;
    columnaUtilMON_AGO = -1;
    columnaUtilMON_SEP = -1;
    columnaUtilMON_OCT = -1;
    columnaUtilMON_NOV = -1;
    columnaUtilMON_DIC = -1;
  }

  // ***************************************************************************
  // ***************************************************************************
  // METODO QUE ABRE EL ARCHIVO DE EXCEL
  // ***************************************************************************
  // ***************************************************************************
  public String leerArchivoExcel(String ruta) {
    String cadena;
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
          case "NOM":
            columnaUtilNOM = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util NOM en la posicion " + (columnaUtilNOM + 1));
            break;
          case "REC":
            columnaUtilREC = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util REC en la posicion " + (columnaUtilREC + 1));
            break;
          case "LIN":
            columnaUtilLIN = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util LIN en la posicion " + (columnaUtilLIN + 1));
            break;
          case "PRO":
            columnaUtilPRO = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util PRO en la posicion " + (columnaUtilPRO + 1));
            break;
          case "FAM":
            columnaUtilFAM = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util FAM en la posicion " + (columnaUtilFAM + 1));
            break;
          case "EST":
            columnaUtilEST = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util EST en la posicion " + (columnaUtilEST + 1));
            break;
          case "MEV":
            columnaUtilMEV = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util MEV en la posicion " + (columnaUtilMEV + 1));
            break;
          case "DES":
            columnaUtilDES = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util DES en la posicion " + (columnaUtilDES + 1));
            break;
          case "FIC":
            columnaUtilFIC = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util FIC en la posicion " + (columnaUtilFIC + 1));
            break;
          case "FVC":
            columnaUtilFVC = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util FVC en la posicion " + (columnaUtilFVC + 1));
            break;
          case "FQB":
            columnaUtilFQB = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util FQB en la posicion " + (columnaUtilFQB + 1));
            break;
          case "DIS":
            columnaUtilDIS = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util DIS en la posicion " + (columnaUtilDIS + 1));
            break;
          case "MEN":
            columnaUtilMEN = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util MEN en la posicion " + (columnaUtilMEN + 1));
            break;
          case "SAI":
            columnaUtilSAI = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util SAI en la posicion " + (columnaUtilSAI + 1));
            break;
          case "SAV":
            columnaUtilSAV = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util SAV en la posicion " + (columnaUtilSAV + 1));
            break;
          case "TIN":
            columnaUtilTIN = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util TIN en la posicion " + (columnaUtilTIN + 1));
            break;
          case "CUE":
            columnaUtilCUE = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util CUE en la posicion " + (columnaUtilCUE + 1));
            break;
          case "FUP":
            columnaUtilFUP = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util FUP en la posicion " + (columnaUtilFUP + 1));
            break;
          case "FUV":
            columnaUtilFUV = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util FUV en la posicion " + (columnaUtilFUV + 1));
            break;
          case "CTE":
            columnaUtilCTE = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util CTE en la posicion " + (columnaUtilCTE + 1));
            break;
          case "RFC":
            columnaUtilRFC = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util RFC en la posicion " + (columnaUtilRFC + 1));
            break;
          case "AJAN":
            columnaUtilAJAN = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util AJAN en la posicion " + (columnaUtilAJAN + 1));
            break;
          case "AJAC":
            columnaUtilAJAC = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util AJAC en la posicion " + (columnaUtilAJAC + 1));
            break;
          case "FAC-ENE":
            columnaUtilFAC_ENE = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util FAC-ENE en la posicion " + (columnaUtilFAC_ENE + 1));
            break;
          case "FAC-FEB":
            columnaUtilFAC_FEB = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util FAC-FEB en la posicion " + (columnaUtilFAC_FEB + 1));
            break;
          case "FAC-MAR":
            columnaUtilFAC_MAR = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util FAC-MAR en la posicion " + (columnaUtilFAC_MAR + 1));
            break;
          case "FAC-ABR":
            columnaUtilFAC_ABR = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util FAC-ABR en la posicion " + (columnaUtilFAC_ABR + 1));
            break;
          case "FAC-MAY":
            columnaUtilFAC_MAY = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util FAC-MAY en la posicion " + (columnaUtilFAC_MAY + 1));
            break;
          case "FAC-JUN":
            columnaUtilFAC_JUN = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util FAC-JUN en la posicion " + (columnaUtilFAC_JUN + 1));
            break;
          case "FAC-JUL":
            columnaUtilFAC_JUL = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util FAC-JUL en la posicion " + (columnaUtilFAC_JUL + 1));
            break;
          case "FAC-AGO":
            columnaUtilFAC_AGO = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util FAC-AGO en la posicion " + (columnaUtilFAC_AGO + 1));
            break;
          case "FAC-SEP":
            columnaUtilFAC_SEP = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util FAC-SEP en la posicion " + (columnaUtilFAC_SEP + 1));
            break;
          case "FAC-OCT":
            columnaUtilFAC_OCT = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util FAC-OCT en la posicion " + (columnaUtilFAC_OCT + 1));
            break;
          case "FAC-NOV":
            columnaUtilFAC_NOV = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util FAC-NOV en la posicion " + (columnaUtilFAC_NOV + 1));
            break;
          case "FAC-DIC":
            columnaUtilFAC_DIC = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util FAC-DIC en la posicion " + (columnaUtilFAC_DIC + 1));
            break;
          case "MON-ENE":
            columnaUtilMON_ENE = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util MON-ENE en la posicion " + (columnaUtilMON_ENE + 1));
            break;
          case "MON-FEB":
            columnaUtilMON_FEB = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util MON-FEB en la posicion " + (columnaUtilMON_FEB + 1));
            break;
          case "MON-MAR":
            columnaUtilMON_MAR = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util MON-MAR en la posicion " + (columnaUtilMON_MAR + 1));
            break;
          case "MON-ABR":
            columnaUtilMON_ABR = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util MON-ABR en la posicion " + (columnaUtilMON_ABR + 1));
            break;
          case "MON-MAY":
            columnaUtilMON_MAY = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util MON-MAY en la posicion " + (columnaUtilMON_MAY + 1));
            break;
          case "MON-JUN":
            columnaUtilMON_JUN = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util MON-JUN en la posicion " + (columnaUtilMON_JUN + 1));
            break;
          case "MON-JUL":
            columnaUtilMON_JUL = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util MON-JUL en la posicion " + (columnaUtilMON_JUL + 1));
            break;
          case "MON-AGO":
            columnaUtilMON_AGO = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util MON-AGO en la posicion " + (columnaUtilMON_AGO + 1));
            break;
          case "MON-SEP":
            columnaUtilMON_SEP = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util MON-SEP en la posicion " + (columnaUtilMON_SEP + 1));
            break;
          case "MON-OCT":
            columnaUtilMON_OCT = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util MON-OCT en la posicion " + (columnaUtilMON_OCT + 1));
            break;
          case "MON-NOV":
            columnaUtilMON_NOV = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util MON-NOV en la posicion " + (columnaUtilMON_NOV + 1));
            break;
          case "MON-DIC":
            columnaUtilMON_DIC = celda.getColumnIndex();
            Logs.log.info("Se encontro columna util MON-DIC en la posicion " + (columnaUtilMON_DIC + 1));
            break;
        }
      }
      int numeroFilas = hoja.getPhysicalNumberOfRows();
      List<FilaCreditoExcel> filasExcel = new ArrayList();
      DataFormatter fmt = new DataFormatter();
      for (int i = 1; i < numeroFilas; i++) {
        Row fila = hoja.getRow(i);
        FilaCreditoExcel f = new FilaCreditoExcel();
        for (int j = 0; j < numeroColumnas; j++) {
          Cell celdaActual = fila.getCell(j);
          if (celdaActual != null) {
            if (celdaActual.getColumnIndex() == columnaUtilCRE) {
              f.setNumeroCredito(fmt.formatCellValue(celdaActual));
            }
            if (celdaActual.getColumnIndex() == columnaUtilNOM) {
              String aux = fmt.formatCellValue(celdaActual);
              aux = aux.replace("  ", " ");
              if (aux.endsWith(" ")) {
                aux = aux.substring(0, aux.length() - 1);
                f.setNombreDeudor(aux);
              }
              f.setNombreDeudor(aux);
            }
            if (celdaActual.getColumnIndex() == columnaUtilREC) {
              f.setReferenciaCobro(fmt.formatCellValue(celdaActual));
            }
            if (celdaActual.getColumnIndex() == columnaUtilLIN) {
              f.setSubproducto(fmt.formatCellValue(celdaActual));
            }
            if (celdaActual.getColumnIndex() == columnaUtilPRO) {
              f.setProducto(fmt.formatCellValue(celdaActual));
            }
            if (celdaActual.getColumnIndex() == columnaUtilFAM) {
              f.setFamiliaProducto(fmt.formatCellValue(celdaActual));
            }
            if (celdaActual.getColumnIndex() == columnaUtilEST) {
              f.setEstatusCuenta(fmt.formatCellValue(celdaActual));
            }
            if (celdaActual.getColumnIndex() == columnaUtilMEV) {
              f.setMesesVencidos(fmt.formatCellValue(celdaActual));
            }
            if (celdaActual.getColumnIndex() == columnaUtilDES) {
              f.setDespachoAsignado(fmt.formatCellValue(celdaActual));
            }
            if (celdaActual.getColumnIndex() == columnaUtilFIC) {
              f.setFechaInicioCredito(fmt.formatCellValue(celdaActual));
            }
            if (celdaActual.getColumnIndex() == columnaUtilFVC) {
              f.setFechaVencimientoCredito(fmt.formatCellValue(celdaActual));
            }
            if (celdaActual.getColumnIndex() == columnaUtilFQB) {
              f.setFechaQuebranto(fmt.formatCellValue(celdaActual));
            }
            if (celdaActual.getColumnIndex() == columnaUtilDIS) {
              f.setMontoCredito(fmt.formatCellValue(celdaActual));
            }
            if (celdaActual.getColumnIndex() == columnaUtilMEN) {
              f.setMensualidad(fmt.formatCellValue(celdaActual));
            }
            if (celdaActual.getColumnIndex() == columnaUtilSAI) {
              f.setSaldoInsoluto(fmt.formatCellValue(celdaActual));
            }
            if (celdaActual.getColumnIndex() == columnaUtilSAV) {
              f.setSaldoVencido(fmt.formatCellValue(celdaActual));
            }
            if (celdaActual.getColumnIndex() == columnaUtilTIN) {
              f.setTasaInteres(fmt.formatCellValue(celdaActual));
            }
            if (celdaActual.getColumnIndex() == columnaUtilCUE) {
              f.setCuentaCredito(fmt.formatCellValue(celdaActual));
            }
            if (celdaActual.getColumnIndex() == columnaUtilFUP) {
              f.setFechaUltimoPago(fmt.formatCellValue(celdaActual));
            }
            if (celdaActual.getColumnIndex() == columnaUtilFUV) {
              f.setFechaUltimoVencimientoPagado(fmt.formatCellValue(celdaActual));
            }
            if (celdaActual.getColumnIndex() == columnaUtilCTE) {
              f.setNumeroDeudor(fmt.formatCellValue(celdaActual));
            }
            if (celdaActual.getColumnIndex() == columnaUtilRFC) {
              f.setRfc(fmt.formatCellValue(celdaActual));
            }
            List<Ajustes> listaAjustes = new ArrayList();
            if (celdaActual.getColumnIndex() == columnaUtilAJAN) {
              listaAjustes.addAll(crearAjustes(fmt.formatCellValue(celdaActual), util.constantes.Ajustes.ANTERIORES));
            }
            if (celdaActual.getColumnIndex() == columnaUtilAJAC) {
              listaAjustes.addAll(crearAjustes(fmt.formatCellValue(celdaActual), util.constantes.Ajustes.ACTUALES));
            }
            if (!listaAjustes.isEmpty()) {
              f.setListaAjustes(listaAjustes);
            }
            List<Facs> listaFacs = new ArrayList();
            if (celdaActual.getColumnIndex() == columnaUtilFAC_ENE) {
              for (int k = 0; k < numeroColumnas; k++) {
                Cell celdaMonto = fila.getCell(j);
                if (celdaMonto.getColumnIndex() == columnaUtilMON_ENE) {
                  listaFacs.add(crearFac(fmt.formatCellValue(celdaActual), fmt.formatCellValue(celdaMonto), 1));
                }
              }
            }
            if (celdaActual.getColumnIndex() == columnaUtilFAC_FEB) {
              for (int k = 0; k < numeroColumnas; k++) {
                Cell celdaMonto = fila.getCell(j);
                if (celdaMonto.getColumnIndex() == columnaUtilMON_FEB) {
                  listaFacs.add(crearFac(fmt.formatCellValue(celdaActual), fmt.formatCellValue(celdaMonto), 2));
                }
              }
            }
            if (celdaActual.getColumnIndex() == columnaUtilFAC_MAR) {
              for (int k = 0; k < numeroColumnas; k++) {
                Cell celdaMonto = fila.getCell(j);
                if (celdaMonto.getColumnIndex() == columnaUtilMON_MAR) {
                  listaFacs.add(crearFac(fmt.formatCellValue(celdaActual), fmt.formatCellValue(celdaMonto), 3));
                }
              }
            }
            if (celdaActual.getColumnIndex() == columnaUtilFAC_ABR) {
              for (int k = 0; k < numeroColumnas; k++) {
                Cell celdaMonto = fila.getCell(j);
                if (celdaMonto.getColumnIndex() == columnaUtilMON_ABR) {
                  listaFacs.add(crearFac(fmt.formatCellValue(celdaActual), fmt.formatCellValue(celdaMonto), 4));
                }
              }
            }
            if (celdaActual.getColumnIndex() == columnaUtilFAC_MAY) {
              for (int k = 0; k < numeroColumnas; k++) {
                Cell celdaMonto = fila.getCell(j);
                if (celdaMonto.getColumnIndex() == columnaUtilMON_MAY) {
                  listaFacs.add(crearFac(fmt.formatCellValue(celdaActual), fmt.formatCellValue(celdaMonto), 5));
                }
              }
            }
            if (celdaActual.getColumnIndex() == columnaUtilFAC_JUN) {
              for (int k = 0; k < numeroColumnas; k++) {
                Cell celdaMonto = fila.getCell(j);
                if (celdaMonto.getColumnIndex() == columnaUtilMON_JUN) {
                  listaFacs.add(crearFac(fmt.formatCellValue(celdaActual), fmt.formatCellValue(celdaMonto), 6));
                }
              }
            }
            if (celdaActual.getColumnIndex() == columnaUtilFAC_JUL) {
              for (int k = 0; k < numeroColumnas; k++) {
                Cell celdaMonto = fila.getCell(j);
                if (celdaMonto.getColumnIndex() == columnaUtilMON_JUL) {
                  listaFacs.add(crearFac(fmt.formatCellValue(celdaActual), fmt.formatCellValue(celdaMonto), 7));
                }
              }
            }
            if (celdaActual.getColumnIndex() == columnaUtilFAC_AGO) {
              for (int k = 0; k < numeroColumnas; k++) {
                Cell celdaMonto = fila.getCell(j);
                if (celdaMonto.getColumnIndex() == columnaUtilMON_AGO) {
                  listaFacs.add(crearFac(fmt.formatCellValue(celdaActual), fmt.formatCellValue(celdaMonto), 8));
                }
              }
            }
            if (celdaActual.getColumnIndex() == columnaUtilFAC_SEP) {
              for (int k = 0; k < numeroColumnas; k++) {
                Cell celdaMonto = fila.getCell(j);
                if (celdaMonto.getColumnIndex() == columnaUtilMON_SEP) {
                  listaFacs.add(crearFac(fmt.formatCellValue(celdaActual), fmt.formatCellValue(celdaMonto), 9));
                }
              }
            }
            if (celdaActual.getColumnIndex() == columnaUtilFAC_OCT) {
              for (int k = 0; k < numeroColumnas; k++) {
                Cell celdaMonto = fila.getCell(j);
                if (celdaMonto.getColumnIndex() == columnaUtilMON_OCT) {
                  listaFacs.add(crearFac(fmt.formatCellValue(celdaActual), fmt.formatCellValue(celdaMonto), 10));
                }
              }
            }
            if (celdaActual.getColumnIndex() == columnaUtilFAC_NOV) {
              for (int k = 0; k < numeroColumnas; k++) {
                Cell celdaMonto = fila.getCell(j);
                if (celdaMonto.getColumnIndex() == columnaUtilMON_NOV) {
                  listaFacs.add(crearFac(fmt.formatCellValue(celdaActual), fmt.formatCellValue(celdaMonto), 11));
                }
              }
            }
            if (celdaActual.getColumnIndex() == columnaUtilFAC_DIC) {
              for (int k = 0; k < numeroColumnas; k++) {
                Cell celdaMonto = fila.getCell(j);
                if (celdaMonto.getColumnIndex() == columnaUtilMON_DIC) {
                  listaFacs.add(crearFac(fmt.formatCellValue(celdaActual), fmt.formatCellValue(celdaMonto), 12));
                }
              }
            }
            if (!listaFacs.isEmpty()) {
              f.setListaFacs(listaFacs);
            }
          }
        }
        filasExcel.add(f);
      }
      Logs.log.info("Se compararan " + filasExcel.size() + " creditos");
      cadena = "Paso 2 completado. Se encontraron " + filasExcel.size() + " creditos";
      elContext = FacesContext.getCurrentInstance().getELContext();
      nuevaCargaBean.setFilas(filasExcel);
    } catch (FileNotFoundException fnfe) {
      Logs.log.error("No se encontro el archivo en la ruta especificada.");
      Logs.log.error(fnfe.getMessage());
      cadena = "No se encontro el archivo de la remesa. Contacte al equipo de sistemas.";
    } catch (IOException ioe) {
      Logs.log.error("Error de lectura.");
      Logs.log.error(ioe.getMessage());
      cadena = "Ocurrio un error de lectura en el archivo de la remesa. Contacte al equipo de sistemas.";
    }
    return cadena;
  }

  // ***************************************************************************
  // ***************************************************************************
  // METODO QUE CREA LAS ACTUALIZACIONES DEPENDIENDO DE LA LECTURA DEL ARCHIVO
  // ***************************************************************************
  // ***************************************************************************
  public List<Ajustes> crearAjustes(String informacion, int tipo) {
    String[] ajustes;
    ajustes = informacion.split(" ");
    List<Ajustes> lista = new ArrayList();
    for (int i = 0; i < (ajustes.length); i++) {
      Ajustes a = new Ajustes();
      a.setAjuste(ajustes[i]);
      a.setTipo(tipo);
      lista.add(a);
    }
    return lista;
  }

  // ***************************************************************************
  // ***************************************************************************
  // METODO QUE CREA LAS RESPUESTAS DE TELMEX
  // ***************************************************************************
  // ***************************************************************************
  public Facs crearFac(String facMes, String facPor, int columna) {
    int mes = 0;
    Facs f = new Facs();
    f.setMonto(facPor);
    f.setRespuesta(facMes);
    switch (columna) {
      case 1:
        f.setMes("01");
        mes = 1;
        break;
      case 2:
        f.setMes("02");
        mes = 2;
        break;
      case 3:
        f.setMes("03");
        mes = 3;
        break;
      case 4:
        f.setMes("04");
        mes = 4;
        break;
      case 5:
        f.setMes("05");
        mes = 5;
        break;
      case 6:
        f.setMes("06");
        mes = 6;
        break;
      case 7:
        f.setMes("07");
        mes = 7;
        break;
      case 8:
        f.setMes("08");
        mes = 8;
        break;
      case 9:
        f.setMes("09");
        mes = 9;
        break;
      case 10:
        f.setMes("10");
        mes = 10;
        break;
      case 11:
        f.setMes("11");
        mes = 11;
        break;
      case 12:
        f.setMes("12");
        mes = 12;
        break;
    }
    int anioActual = Calendar.getInstance().get(Calendar.YEAR);
    int mesActual = Calendar.getInstance().get(Calendar.MONTH) + 1;
    if ((mes > mesActual)) {
      f.setAnio(String.valueOf(anioActual - 1));
    } else {
      f.setAnio(String.valueOf(anioActual));
    }
    return f;
  }

}
