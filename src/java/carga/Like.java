/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carga;

import java.text.Normalizer;
import util.Levenshtein;

/**
 *
 * @author Eduardo
 */
public class Like {
  
  // METODO QUE ASEMEJA AL LIKE DE SQL, PARA COMPARAR LAS DIRECCIONES EN LA BASE DE DATOS CON LAS OBTENIDAS EN LA REMESA
  // SI EN LA REMESA VIENE PARTE DEL NOMBRE DE UNA COLONIA DE LA BASE DE DATOS, SE ASIGNARA DICHA COLONIA A LA DIRECCION
  public static boolean like(String coloniaRemesa, String coloniaBase){
    //  NORMALIZAMOS (QUITAMOS CARACTERES ESPECIALES) A LAS CADENAS RECIBIDAS
    coloniaRemesa = Normalizer.normalize(coloniaRemesa, Normalizer.Form.NFD);
    coloniaBase = Normalizer.normalize(coloniaBase, Normalizer.Form.NFD);
    // VARIABLE DE RETORNO
    boolean ok = false;
    // VARIABLE PARA GUARDAR SI LA SUBCADENA EXISTE
    int subcadena;
    // TOMAMOS EL NOMBRE DE COLONIA DE LA REMESA COMO SUBCADENA Y LO TRATAMOS
    String sub = EliminadorDeAsentaminetos.eliminaAsentamiento(coloniaRemesa);
    // VERIFICAMOS SI EL NOMBRE DE LA COLONIA ENVIADO EN LA REMESA ES UNA SUBCADENA DEL NOMBRE DE LA BASE DE DATOS
    subcadena = coloniaBase.indexOf(sub);
    if(subcadena != -1){
      System.out.println("SUBCADENA " + sub + " ENCONTRADA EN : " + coloniaBase);
      ok = true;
    }
    return ok;
  }
}
