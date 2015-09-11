/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dto.Fila;
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
      creacionArchivoSQL();
    } catch (Exception ioe) {
      Logs.log.error("No se pudo leer archivo:");
      Logs.log.error(ioe.getMessage());

    } finally {
      System.out.println("El archivo tiene " + numeroDeFilas + " filas y " + numeroDeColumnas + " columnas");
    }
    return numeroDeFilas - 1;
  }
  
  // METODO QUE LEE UNA X UNA LAS FILAS DEL ARCHIVO, LLENA UN OBJETO EN EL DTO Y LA MANDA A ESCRITURA EN EL ARCHIVO
  public void creacionArchivoSQL(){
  int i;
  // CREAMOS UN OBJETO FILA PARA LLENAR DE DATOS RECIBIDOS DE LAS CELDAS
  Fila filaSQL = new Fila(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
  for(i=0; i<numeroDeFilas; i++){
    // COLUMNA A (0): NUMERO DE CREDITO
    filaSQL.setCredito(hojaExcel.getCell(0, i).getContents());
    // COLUMNA B (1): NOMBRE DEL DEUDOR
    filaSQL.setNombre(hojaExcel.getCell(1, i).getContents());
    // COLUMNA C (2): REFERENCIA COBRO
    filaSQL.setRefCobro(hojaExcel.getCell(2, i).getContents());
    // COLUMNA D (3): PRODUCTO (LINEA)
    filaSQL.setLinea(hojaExcel.getCell(3, i).getContents());
    // COLUMNA E (4): SUBPRODUCTO (TIPO CREDITO)
    filaSQL.setTipoCredito(hojaExcel.getCell(4, i).getContents());
    // COLUMNA F (5): ESTATUS
    filaSQL.setEstatus(hojaExcel.getCell(5, i).getContents());
    // COLUMNA G (6): MESES VENCIDOS
    filaSQL.setMesesVencidos(hojaExcel.getCell(6, i).getContents());
    // COLUMNA H (7): EMPRESA (DESPACHO)
    filaSQL.setDespacho(hojaExcel.getCell(7, i).getContents());
    // COLUMNA I (8): FECHA DE INICIO DEL CREDITO
    filaSQL.setFechaInicioCredito(hojaExcel.getCell(8, i).getContents());
    // COLUMNA J (9): FECHA DE VENCIMIENTO DEL CREDITO
    filaSQL.setFechaVencimientoCred(hojaExcel.getCell(9, i).getContents());
    // COLUMNA K (10): DISPOSICION
    filaSQL.setDisposicion(hojaExcel.getCell(10, i).getContents());
    // COLUMNA L (11): MENSUALIDAD
    filaSQL.setMensualidad(hojaExcel.getCell(11, i).getContents());
    // COLUMNA M (12): SALDO INSOLUTO
    filaSQL.setSaldoInsoluto(hojaExcel.getCell(12, i).getContents());
    // COLUMNA N (13): SALDO VENCIDO
    filaSQL.setSaldoVencido(hojaExcel.getCell(13, i).getContents());
    // COLUMNA O (14): TASA
    filaSQL.setTasa(hojaExcel.getCell(14, i).getContents());
    // COLUMNA P (15): CUENTA
    filaSQL.setCuenta(hojaExcel.getCell(15, i).getContents());
    // COLUMNA Q (16): FECHA ULTIMO PAGO
    filaSQL.setFechaUltimoPago(hojaExcel.getCell(16, i).getContents());
    // COLUMNA R (17): FECHA ULTIMO VENCIMIENTO PAGADO
    filaSQL.setFechaUltimoVencimientoPagado(hojaExcel.getCell(17, i).getContents());
    // COLUMNA S (18): NUMERO DE CLIENTE
    filaSQL.setIdCliente(hojaExcel.getCell(18, i).getContents());
    // COLUMNA T (19): RFC
    filaSQL.setRfc(hojaExcel.getCell(19, i).getContents());
    // COLUMNA U (20): CALLE DIRECCION DEUDOR
    filaSQL.setCalle(hojaExcel.getCell(20, i).getContents());
    // COLUMNA V (21): COLONIA DIRECCION DEUDOR
    filaSQL.setCalle(hojaExcel.getCell(21, i).getContents());
    // COLUMNA W (22): ESTADO DIRECCION DEUDOR
    filaSQL.setEstado(hojaExcel.getCell(22, i).getContents());
    // COLUMNA X (23): MUNICIPIO DIRECCION DEUDOR
    filaSQL.setCalle(hojaExcel.getCell(23, i).getContents());
    // COLUMNA Y (24): CODIGO POSTAL DIRECCION DEUDOR
    filaSQL.setCp(hojaExcel.getCell(20, i).getContents());
    // SE CREA LA CONSULTA Y SE MANDA A ESCRITURA EN EL ARCHIVO
    String consulta = filaSQL.crearSQL();
    filaSQL.crearArchivoSQL(consulta, "cargaPrueba2");
  }
}
  
  public int getNumeroDeFilas() {
    return numeroDeFilas;
  }

  public void setNumeroDeFilas(int numeroDeFilas) {
    this.numeroDeFilas = numeroDeFilas;
  }

  public int getNumeroDeColumnas() {
    return numeroDeColumnas;
  }

  public void setNumeroDeColumnas(int numeroDeColumnas) {
    this.numeroDeColumnas = numeroDeColumnas;
  }

  public Workbook getArchivoExcel() {
    return archivoExcel;
  }

  public void setArchivoExcel(Workbook archivoExcel) {
    this.archivoExcel = archivoExcel;
  }

  public Sheet getHojaExcel() {
    return hojaExcel;
  }

  public void setHojaExcel(Sheet hojaExcel) {
    this.hojaExcel = hojaExcel;
  }

}