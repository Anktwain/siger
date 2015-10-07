/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carga;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Eduardo
 */
public class EliminadorDeAsentaminetos {
  
/*
  public static void main (String [ ] args) {
    Scanner sc = new Scanner(System.in);
    System.out.print("INGRESE UNA COLONIA:\n");
    String alpha = sc.nextLine();
    System.out.println("CADENA TRATADA:" + eliminaAsentamiento(alpha));
  }
*/  
  
  // METODO QUE ELIMINA LA SUBCADENA QUE INDIQUE EL ASENTAMIENTO DE UNA COLONIA
  public static String eliminaAsentamiento(String nombreColonia){
    // VARIABLE DE RETORNO
    String cadena = "";
    // VARIABLE RECIBIDA
    //System.out.println("CADENA RECIBIDA: " + nombreColonia);
    // PASAMOS A MINUSCULAS LA CADENA RECIBIDA
    nombreColonia = nombreColonia.toLowerCase();
    // LISTA DE POSIBLES ABREVIATURAS DE COLONIAS
    List<String> colonia = new ArrayList<>();
    // LISTA DE POSIBLES ABREVIATURAS DE FRACCIONAMIENTOS
    List<String> fraccionamiento = new ArrayList<>();
    // LISTA DE POSIBLES ABREVIATURAS DE UNIDADES HABITACIONALES
    List<String> unidad = new ArrayList<>();
    // LISTA DE POSIBLES ABREVIATURAS DE RANCHERIA
    List<String> rancheria = new ArrayList<>();
    // LISTA DE POSIBLES ABREVIATURAS DE CONJUNTO HABITACIONAL
    List<String> conjunto = new ArrayList<>();
    // LISTA DE POSIBLES ABREVIATURAS DE AMPLIACION
    List<String> ampliacion = new ArrayList<>();
    // LISTA DE POSIBLES ABREVIATURAS DE VILLA
    List<String> villa = new ArrayList<>();
    // LISTA DE POSIBLES ABREVIATURAS DE CONGREGACION
    List<String> congregacion = new ArrayList<>();
    // AGREGANDO ABREVIATURAS DE COLONIA
    colonia.add("colonia ");
    colonia.add("col. ");
    colonia.add("col ");
    // AGREGANDO ABREVIATURAS DE FRACCIONAMIENTOS
    fraccionamiento.add("fraccionamiento ");
    fraccionamiento.add("fracc. ");
    fraccionamiento.add("fracc ");
    fraccionamiento.add("frac. ");
    fraccionamiento.add("frac ");
    // AGREGANDO ABREVIATURAS DE UNIDADES HABITACIONALES
    unidad.add("unidad habitacional ");
    unidad.add("u. habitacional ");
    unidad.add("u habitacional ");
    unidad.add("unidad h. ");
    unidad.add("unidad h ");
    unidad.add("unidad ");
    unidad.add("u. h. ");
    unidad.add("u h ");
    unidad.add("u. hab. ");
    unidad.add("u hab ");
    // AGREGANDO ABREVIATURAS DE RANCHERIA
    rancheria.add("rancheria ");
    rancheria.add("ranch. ");
    rancheria.add("ranch ");
    rancheria.add("ran. ");
    rancheria.add("ran ");
    // AGREGANDO ABREVIATURAS DE CONJUNTO HABITACIONAL
    conjunto.add("conjunto habitacional ");
    conjunto.add("c. habitacional ");
    conjunto.add("c habitacional ");
    conjunto.add("c. habitacional ");
    conjunto.add("c habitacional ");
    conjunto.add("c. h. ");
    conjunto.add("c h ");
    // AGREGANDO ABREVIATURAS DE AMPLIACION
    ampliacion.add("ampliacion ");
    ampliacion.add("ampl. ");
    ampliacion.add("ampl ");
    ampliacion.add("amp. ");
    ampliacion.add("amp ");
    // AGREGANDO ABREVIATURAS DE VILLA
    villa.add("villa ");
    villa.add("v. ");
    // AGREGANDO ABREVIATURAS DE CONGREGACION
    congregacion.add("congregacion ");
    congregacion.add("cong. ");
    congregacion.add("cong ");
    congregacion.add("con. ");
    congregacion.add("con ");
    // VERIFICAMOS EL PRIMER CARACTER DE LA CADENA RECIBIDA
    String tipo = nombreColonia.substring(0, 1);
    // DECLARAMOS UNA VARIABLE A USAR COMO SUBCADENA
    String sub;
    // DECLARAMOS UN ENTERO QUE NOS REGRESARA LA POSICION DONDE INICIA LA COINCIDENCIA
    int posicion;
    switch(tipo){
      // CUANDO LA CADENA COMIENCE CON A, SE VERIFICARA SI SE TRATA DE UNA AMPLIACION
      case "a":
        //BUSCAMOS PARA CADA COINCIDENCIA DE LA LISTA
        for (int i = 0; i < ampliacion.size(); i++) {
          // IGUALAMOS LA SUBCADENA A LA COINCIDENCIA DE LA LISTA
          sub = ampliacion.get(i);
          // REGRESA UN VALOR DISTINTO A -1 SI SE ENCUENTRA UNA COINCIDENCIA
          posicion = nombreColonia.indexOf(sub);
          // SI SE ENCONTRO LA COINCIDENCIA AL INICIO DEL ARREGLO
          if(posicion == 0){
            cadena = nombreColonia.replaceAll(sub, "");
            System.out.println("REEMPLAZO: " + nombreColonia + " POR: " + cadena);
            return cadena;
          }
        }
      // CUANDO LA CADENA COMIENCE CON B, SE VERIFICARA SI SE TRATA DE UN BARRIO
      case "b":
        // IGUALAMOS LA SUBCADENA A LA PALABRA BARRIO
        sub = "barrio ";
        // REGRESA UN VALOR DISTINTO A -1 SI SE ENCUENTRA UNA COINCIDENCIA
        posicion = nombreColonia.indexOf(sub);
        // SI SE ENCONTRO LA COINCIDENCIA AL INICIO DEL ARREGLO
        if(posicion == 0){
          cadena = nombreColonia.replaceAll(sub, "");
          System.out.println("REEMPLAZO: " + nombreColonia + " POR: " + cadena);
          return cadena;
        }
      // CUANDO LA CADENA COMIENCE CON C, SE VERIFICARA SI SE TRATA DE UNA COLONIA, CONJUNTO HABITACIONAL O CONGREGACION
      case "c":
        // COLONIA
        //BUSCAMOS PARA CADA COINCIDENCIA DE LA LISTA
        for (int i = 0; i < colonia.size(); i++) {
          // IGUALAMOS LA SUBCADENA A LA COINCIDENCIA DE LA LISTA
          sub = colonia.get(i);
          // REGRESA UN VALOR DISTINTO A -1 SI SE ENCUENTRA UNA COINCIDENCIA
          posicion = nombreColonia.indexOf(sub);
          // SI SE ENCONTRO LA COINCIDENCIA AL INICIO DEL ARREGLO
          if(posicion == 0){
            cadena = nombreColonia.replaceAll(sub, "");
            System.out.println("REEMPLAZO: " + nombreColonia + " POR: " + cadena);
            return cadena;
          }
        }
        // CONJUNTO HABITACIONAL
        //BUSCAMOS PARA CADA COINCIDENCIA DE LA LISTA
          for (int i = 0; i < conjunto.size(); i++) {
            // IGUALAMOS LA SUBCADENA A LA COINCIDENCIA DE LA LISTA
            sub = conjunto.get(i);
            // REGRESA UN VALOR DISTINTO A -1 SI SE ENCUENTRA UNA COINCIDENCIA
            posicion = nombreColonia.indexOf(sub);
            // SI SE ENCONTRO LA COINCIDENCIA AL INICIO DEL ARREGLO
            if(posicion == 0){
              cadena = nombreColonia.replaceAll(sub, "");
              System.out.println("REEMPLAZO: " + nombreColonia + " POR: " + cadena);
              return cadena;
            }
          }
        // CONGREGACION
        //BUSCAMOS PARA CADA COINCIDENCIA DE LA LISTA
        for (int i = 0; i < congregacion.size(); i++) {
          // IGUALAMOS LA SUBCADENA A LA COINCIDENCIA DE LA LISTA
          sub = congregacion.get(i);
          // REGRESA UN VALOR DISTINTO A -1 SI SE ENCUENTRA UNA COINCIDENCIA
          posicion = nombreColonia.indexOf(sub);
          // SI SE ENCONTRO LA COINCIDENCIA AL INICIO DEL ARREGLO
          if(posicion == 0){
            cadena = nombreColonia.replaceAll(sub, "");
            System.out.println("REEMPLAZO: " + nombreColonia + " POR: " + cadena);
            return cadena;
          }
        }
      // CUANDO LA CADENA COMIENCE CON E, SE VERIFICARA SI SE TRATA DE UN EJIDO
      case "e":
        // IGUALAMOS LA SUBCADENA A LA PALABRA BARRIO
        sub = "ejido ";
        // REGRESA UN VALOR DISTINTO A -1 SI SE ENCUENTRA UNA COINCIDENCIA
        posicion = nombreColonia.indexOf(sub);
        // SI SE ENCONTRO LA COINCIDENCIA AL INICIO DEL ARREGLO
        if(posicion == 0){
          cadena = nombreColonia.replaceAll(sub, "");
          System.out.println("REEMPLAZO: " + nombreColonia + " POR: " + cadena);
          return cadena;
        }
      // CUANDO LA CADENA COMIENCE CON F, SE VERIFICARA SI SE TRATA DE UN FRACCIONAMIENTO
      case "f":
        //BUSCAMOS PARA CADA COINCIDENCIA DE LA LISTA
        for (int i = 0; i < fraccionamiento.size(); i++) {
          // IGUALAMOS LA SUBCADENA A LA COINCIDENCIA DE LA LISTA
          sub = fraccionamiento.get(i);
          // REGRESA UN VALOR DISTINTO A -1 SI SE ENCUENTRA UNA COINCIDENCIA
          posicion = nombreColonia.indexOf(sub);
          // SI SE ENCONTRO LA COINCIDENCIA AL INICIO DEL ARREGLO
          if(posicion == 0){
            cadena = nombreColonia.replaceAll(sub, "");
            System.out.println("REEMPLAZO: " + nombreColonia + " POR: " + cadena);
            return cadena;
          }
        }
      // CUANDO LA CADENA COMIENCE CON R, SE VERIFICARA SI SE TRATA DE UNA RANCHERIA
      case "r":
        //BUSCAMOS PARA CADA COINCIDENCIA DE LA LISTA
        for (int i = 0; i < rancheria.size(); i++) {
          // IGUALAMOS LA SUBCADENA A LA COINCIDENCIA DE LA LISTA
          sub = rancheria.get(i);
          // REGRESA UN VALOR DISTINTO A -1 SI SE ENCUENTRA UNA COINCIDENCIA
          posicion = nombreColonia.indexOf(sub);
          // SI SE ENCONTRO LA COINCIDENCIA AL INICIO DEL ARREGLO
          if(posicion == 0){
            cadena = nombreColonia.replaceAll(sub, "");
            System.out.println("REEMPLAZO: " + nombreColonia + " POR: " + cadena);
            return cadena;
          }
        }
      // CUANDO LA CADENA COMIENCE CON U, SE VERIFICARA SI SE TRATA DE UNA UNIDAD HABITACIONAL
      case "u":
        //BUSCAMOS PARA CADA COINCIDENCIA DE LA LISTA
        for (int i = 0; i < unidad.size(); i++) {
          // IGUALAMOS LA SUBCADENA A LA COINCIDENCIA DE LA LISTA
          sub = unidad.get(i);
          // REGRESA UN VALOR DISTINTO A -1 SI SE ENCUENTRA UNA COINCIDENCIA
          posicion = nombreColonia.indexOf(sub);
          // SI SE ENCONTRO LA COINCIDENCIA AL INICIO DEL ARREGLO
          if(posicion == 0){
            cadena = nombreColonia.replaceAll(sub, "");
            System.out.println("REEMPLAZO: " + nombreColonia + " POR: " + cadena);
            return cadena;
          }
        }
      // CUANDO LA CADENA COMIENCE CON U, SE VERIFICARA SI SE TRATA DE UNA UNIDAD HABITACIONAL
      case "v":
        //BUSCAMOS PARA CADA COINCIDENCIA DE LA LISTA
        for (int i = 0; i < villa.size(); i++) {
          // IGUALAMOS LA SUBCADENA A LA COINCIDENCIA DE LA LISTA
          sub = villa.get(i);
          // REGRESA UN VALOR DISTINTO A -1 SI SE ENCUENTRA UNA COINCIDENCIA
          posicion = nombreColonia.indexOf(sub);
          // SI SE ENCONTRO LA COINCIDENCIA AL INICIO DEL ARREGLO
          if(posicion == 0){
            cadena = nombreColonia.replaceAll(sub, "");
            System.out.println("REEMPLAZO: " + nombreColonia + " POR: " + cadena);
            return cadena;
          }
        }
    }
    return cadena;
  }
}
