/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carga;

import beans.FilaBean;
import dto.Fila;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 *
 * @author antonio
 */
public class Pruebas {

  public static void main(String[] args) throws IOException, BiffException {
    FilaBean filaBean= new FilaBean();
    List<Fila> filas;
    Workbook libro = Workbook.getWorkbook(new File("/home/brionvega/Escritorio/CargasAlSistema/noviembre2015/ASIGNACION DELRIO NOVIEMBRE 2015.xls"));
    ProcesadorArchivoExcel procesador = new ProcesadorArchivoExcel(libro);
    filas = procesador.obtenerFilas();
    // Validar
for(Fila fila:filas){
    System.out.println(fila);
  }
}
}