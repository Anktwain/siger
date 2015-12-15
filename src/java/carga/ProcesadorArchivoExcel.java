/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carga;

import dto.Fila;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import jxl.Sheet;
import jxl.Workbook;
import util.constantes.Directorios;
import util.log.Logs;

/**
 *
 * @author antonio
 */
public class ProcesadorArchivoExcel {

  private final int HOJA_PIVOTE = 0;
  private final int COLUMNA_PIVOTE = 0;

  private Workbook archivo;
  private int numeroDeHojas;
  private Sheet hojas[];

  public ProcesadorArchivoExcel(Workbook archivo) {
    if (archivo != null) {
      this.archivo = archivo;
      numeroDeHojas = archivo.getNumberOfSheets();
      hojas = archivo.getSheets();
    }
  }

  public List<Fila> obtenerFilas() {
    List<String> columnasUtiles = obtenerListaDeColumnasUtiles();
    List<String> claves = obtenerClaves(columnasUtiles);
    List<Fila> filas = generarFilas();

    return completarFilas(filas, claves);
  }

  private List<String> obtenerListaDeColumnasUtiles() {
    String lineaActual; // La línea leída en un momento determinado.
    List<String> lineas = new ArrayList<>();

    try (BufferedReader buferLectura = new BufferedReader(
            new FileReader(Directorios.RUTA_COLUMNAS_UTILES))) {
      while ((lineaActual = buferLectura.readLine()) != null) {
        lineas.add(lineaActual);
      }
      buferLectura.close();
    } catch (IOException ioe) {
      Logs.log.error("Error de lectura/escritura");
      Logs.log.error(ioe.getMessage());
      lineas = null;
    }

    return lineas;
  }

  private List<String> obtenerClaves(List<String> columnasUtiles) {
    List<String> claves = new ArrayList<>();
    String nombreHoja;
    int numeroDeColumnas;

    for (Sheet hoja : hojas) { // Recorre las hojas del archivo
      nombreHoja = hoja.getName();
      numeroDeColumnas = hoja.getColumns();

      for (int i = 0; i < numeroDeColumnas; i++) { // Recorre columnas
        for (int j = 0; j < columnasUtiles.size(); j++) {
          if (hoja.getCell(i, 0).getContents().equals(columnasUtiles.get(j))) {
            claves.add(columnasUtiles.get(j) + ";" + nombreHoja + ";" + i);
            if (columnasUtiles.get(j).length() == 3) {
              columnasUtiles.remove(j);
            }
          }
        }
      }
    }

    return claves;
  }

  private List<Fila> generarFilas() {
    Sheet hoja = hojas[HOJA_PIVOTE];
    Fila fila;
    List<Fila> filas = new ArrayList<>();
    int numeroDeFilas = hoja.getRows();

    for (int i = 1; i < numeroDeFilas; i++) {
      fila = new Fila();
      fila.setCredito(hoja.getCell(COLUMNA_PIVOTE, i).getContents());
      filas.add(fila);
    }

    return filas;
  }

  private List<Fila> completarFilas(List<Fila> filas, List<String> claves) {

    Date javaDate;
    String[] splitClave;
    //Sheet hoja;
    String datoPivote;
    int numeroFila = 0;

    for (Fila fila : filas) {
      datoPivote = fila.getCredito();
      for (Sheet hoja : hojas) {
        for (int i = 1; i < hoja.getRows(); i++) {
          if (datoPivote.equals(hoja.getCell(COLUMNA_PIVOTE, i).getContents())) {
            numeroFila = i;
            break;
          }
          numeroFila = 0;
        }

        if (numeroFila != 0) {
          for (String clave : claves) {
            splitClave = clave.split(";");
            if (hoja.getName().equals(splitClave[1])) {
              switch (splitClave[0]) {
                case "CRE":
                  fila.setCredito(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "NOM":
                  fila.setNombre(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "REC":
                  fila.setRefCobro(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "LIN":
                  fila.setLinea(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "TIC":
                  fila.setTipoCredito(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "EST":
                  fila.setEstatus(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MEV":
                  fila.setMesesVencidos(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "DES":
                  fila.setDespacho(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "FIC":
                  fila.setFechaInicioCredito(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "FVC":
                  fila.setFechaVencimientoCred(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "DIS":
                  fila.setDisposicion(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MEN":
                  fila.setMensualidad(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "SAI":
                  fila.setSaldoInsoluto(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "SAV":
                  fila.setSaldoVencido(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "TAS":
                  fila.setTasa(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "CUE":
                  fila.setCuenta(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "FUP":
                  fila.setFechaUltimoPago(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "FUV":
                  fila.setFechaUltimoVencimientoPagado(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "IDC":
                  fila.setIdCliente(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "RFC":
                  fila.setRfc(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "CLL":
                  fila.setCalle(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "EXT":
                  fila.setNumeroExterior(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "INT":
                  fila.setNumeroInterior(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "COL":
                  fila.setColonia(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "EDO":
                  fila.setEstado(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MUN":
                  fila.setMunicipio(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "COP":
                  fila.setCp(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MAR":
                  fila.setMarcaje(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "FEQ":
                  fila.setFechaQuebranto(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "REFS":
                  fila.setReferenciaAdicional(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "CORS":
                  fila.setCorreoAdicional(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "TELS":
                  fila.setTelefonoAdicional(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "DIRS":
                  fila.setDireccionAdicional(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "ENE-":
                  crearFac(fila, 1, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "FEB-":
                  crearFac(fila, 2, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MAR-":
                  crearFac(fila, 3, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "ABR-":
                  crearFac(fila, 4, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MAY-":
                  crearFac(fila, 5, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "JUN-":
                  crearFac(fila, 6, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "JUL-":
                  crearFac(fila, 7, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "AGO-":
                  crearFac(fila, 8, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "SEP-":
                  crearFac(fila, 9, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "OCT-":
                  crearFac(fila, 10, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "NOV-":
                  crearFac(fila, 11, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "DIC-":
                  crearFac(fila, 12, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MO-ENE":
                  agregarFacPor(1, fila, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MO-FEB":
                  agregarFacPor(2, fila, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MO-MAR":
                  agregarFacPor(3, fila, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MO-ABR":
                  agregarFacPor(4, fila, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MO-MAY":
                  agregarFacPor(5, fila, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MO-JUN":
                  agregarFacPor(6, fila, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MO-JUL":
                  agregarFacPor(7, fila, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MO-AGO":
                  agregarFacPor(8, fila, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MO-SEP":
                  agregarFacPor(9, fila, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MO-OCT":
                  agregarFacPor(10, fila, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MO-NOV":
                  agregarFacPor(11, fila, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MO-DIC":
                  agregarFacPor(12, fila, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
              } // fin de switch
            } // fin de if(coincide el nombre de la hoja)
          } // fin de for(claves)
        } // fin de if(la fila no es 0)
      } // fin de for(hojas)
    } // fin de for(filas)

    return filas;
  }

  private void crearFac(Fila fila, int mes, String facMes) {
    Fac fac = new Fac();
    fac.setMes(mes);
    fac.setAnio(agregarFacAnio(11, mes));
    fac.setFacMes(facMes);
    fila.setFacAdicional(fac);
  }

  private int agregarFacAnio(int mesActual, int mesDado) {
    int anio = Calendar.getInstance().get(Calendar.YEAR);

    if (mesDado <= mesActual) {
      return anio;
    } else {
      return anio - 1;
    }
  }

  private void agregarFacPor(int mes, Fila fila, String facPor) {
    Fac fac = fila.buscarFac(mes);
    if (fac != null) {
      fac.setFacPor(facPor);
    }
  }

  public Workbook getArchivo() {
    return archivo;
  }

  public int getNumeroDeHojas() {
    return numeroDeHojas;
  }

  public Sheet[] getHojas() {
    return hojas;
  }

}
