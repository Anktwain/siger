/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


/**
 *
 * @author Eduardo
 */
public class ManejadorArchivosDeTexto {

  // METODO QUE RECIBE UNA CADENA DE TEXTO, UNA RUTA Y REGRESA UN BOOLEANO CUANDO CREA UN ARCHIVO DE TEXTO
  public static boolean crearArchivo(String cadena, String ruta, String nombreArchivo) {
    boolean ok;
    File archivo = new File(ruta + nombreArchivo);
    BufferedWriter bw;
    try {
      if (archivo.createNewFile()) {
        bw = new BufferedWriter(new FileWriter(archivo));
        bw.write(cadena);
        bw.close();
        ok = true;
      } else {
        bw = new BufferedWriter(new FileWriter(archivo));
        bw.write("");
        bw.write(cadena);
        bw.close();
        ok = true;
      }
    } catch (IOException ioe) {
      System.out.println(ioe);
      ok = false;
    }
    return ok;
  }

  // METODO QUE LEE UN ARCHIVO ESPECIFICADO
  public static String leerArchivo(String ruta, String nombreArchivo) throws FileNotFoundException, IOException {
    String cadena;
    File archivo = new File(ruta + nombreArchivo);
    FileReader f = new FileReader(archivo);
    BufferedReader b = new BufferedReader(f);
    cadena = b.readLine();
    if (cadena == null) {
      cadena = "";
    }
    b.close();
    return cadena;
  }  
}
