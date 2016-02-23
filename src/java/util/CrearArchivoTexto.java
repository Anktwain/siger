/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.*;

/**
 *
 * @author Eduardo
 */
public class CrearArchivoTexto {
  
  // METODO QUE RECIBE UNA CADENA DE TEXTO, UNA RUTA Y REGRESA UN BOOLEANO CUANDO CREA UN ARCHIVO DE TEXTO
  public static boolean crearArchivo(String cadena, String ruta, String nombreArchivo){
  boolean ok = false;
  File archivo = new File (ruta + nombreArchivo);
  BufferedWriter bw;
  try {
  if (archivo.createNewFile()){
    bw = new BufferedWriter(new FileWriter(archivo));
    bw.write(cadena);
    ok = true;
  }
} catch (IOException ioe) {
  ok = false;
}
  return ok;
  }
}
