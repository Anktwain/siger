/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carga;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public class EliminadorDeArticulos {

  public static String eliminaArticulos(String colonia) {
    // VARIABLE DE RETORNO
    String cadena = colonia;
    // LISTA DE POSIBLES ARTICULOS Y PREPOSICIONES
    List<String> articulos = new ArrayList<>();
    // AGREGANDO ARTICULOS Y PREPOSICIONES
    articulos.add("la");
    articulos.add("las");
    articulos.add("los");
    articulos.add("el");
    articulos.add("de");
    // "PARTIMOS" EL NOMBRE DE LA COLONIA RECIBIDO EN PALABRAS
    String[] arreglo = colonia.split(" ");
    // VERIFICAMOS QUE LAS PALABRAS QUE VIENEN EN LA COLONIA NO SEAN ARTICULOS NI PREPOSICIONES
    for (int i = 0; i < arreglo.length; i++) {
      for (int j = 0; j < articulos.size(); j++) {
        if (arreglo[i].equals(articulos.get(j))) {
          System.out.println(arreglo[i] + " = " + articulos.get(j));
          // SI LO SON, QUE LAS ELIMINE DE LA CADENA ORIGINAL
          cadena = colonia.replaceAll(articulos.get(j), "");
          // VERIFICAMOS QUE NO EXISTAN DOBLES ESPACIOS (SI LA PALABRA REMOVIDA VENIA ENTRE 2 PALABRAS)
          int si = cadena.indexOf("  ");
          if (si != -1) {
            cadena = cadena.replace(cadena, " ");
          }
        }
      }
    }
    return cadena;
  }
}
