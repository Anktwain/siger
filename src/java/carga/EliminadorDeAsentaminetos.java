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
  
  public static void main (String [ ] args) {
    Scanner sc = new Scanner(System.in);
    System.out.print("INGRESE UNA COLONIA:\n");
    String alpha = sc.nextLine();
    System.out.println("CADENA TRATADA:" + eliminaAsentamiento(alpha));
  }
  
  // METODO QUE ELIMINA LA SUBCADENA QUE INDIQUE EL ASENTAMIENTO DE UNA COLONIA
  public static String eliminaAsentamiento(String nombreColonia){
    // VARIABLE DE RETORNO
    String cadena = "";
    // VARIABLE RECIBIDA
    System.out.println("CADENA RECIBIDA: " + nombreColonia);
    // PASAMOS A MINUSCULAS LA CADENA RECIBIDA
    nombreColonia = nombreColonia.toLowerCase();
    // LISTA DE POSIBLES ABREVIATURAS DE COLONIAS
    List<String> colonia = new ArrayList<>();
    // LISTA DE POSIBLES ABREVIATURAS DE FRACCIONAMIENTOS
    List<String> fraccionamiento = new ArrayList<>();
    // LISTA DE POSIBLES ABREVIATURAS DE UNIDADES HABITACIONALES
    List<String> unidad = new ArrayList<>();
    // AGREGANDO ABREVIATURAS DE COLONIAS
    colonia.add("colonia");
    colonia.add("col.");
    colonia.add("col");
    // AGREGANDO ABREVIATURAS DE FRACCIONAMIENTOS
    fraccionamiento.add("fraccionamiento");
    fraccionamiento.add("fracc.");
    fraccionamiento.add("fracc");
    fraccionamiento.add("frac.");
    fraccionamiento.add("frac");
    // AGREGANDO ABREVIATURAS DE UNIDADES HABITACIONALES
    unidad.add("unidad habitacional");
    unidad.add("u. habitacional");
    unidad.add("u habitacional");
    unidad.add("unidad h.");
    unidad.add("unidad h");
    unidad.add("u. h.");
    unidad.add("u h");
    unidad.add("u. hab.");
    unidad.add("u hab");
    // VERIFICAMOS EL PRIMER CARACTER DE LA CADENA RECIBIDA
    String tipo = nombreColonia.substring(0, 1);
    // DECLARAMOS UNA VARIABLE A USAR COMO SUBCADENA
    String sub;
    // DECLARAMOS UN ENTERO QUE NOS REGRESARA LA POSICION DONDE INICIA LA COINCIDENCIA
    int posicion;
    switch(tipo){
      // CUANDO LA CADENA COMIENCE CON C, SE VERIFICARA SI SE TRATA DE UNA COLONIA
      case "c":
        //BUSCAMOS PARA CADA COINCIDENCIA DE LA LISTA
        for (int i = 0; i < colonia.size(); i++) {
          // IGUALAMOS LA SUBCADENA A LA COINCIDENCIA DE LA LISTA
          sub = colonia.get(i);
          // REGRESA UN VALOR DISTINTO A -1 SI SE ENCUENTRA UNA COINCIDENCIA
          posicion = nombreColonia.indexOf(sub);
          // SI SE ENCONTRO LA COINCIDENCIA AL INICIO DEL ARREGLO
          if(posicion == 0){
            cadena = nombreColonia.replaceAll(sub, "");
            return cadena;
          }
          // SI NO SE ENCONTRO LA COINCIDENCIA
          else{
            System.out.println("NO SE ENCONTRO EL TIPO DE ASENTAMIENTO COLONIA");
            System.out.println(nombreColonia + " NO CONTIENE " + sub);
          }
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
            return cadena;
          }
          // SI NO SE ENCONTRO LA COINCIDENCIA
          else{
            System.out.println("NO SE ENCONTRO EL TIPO DE ASENTAMIENTO FRACCIONAMIENTO");
            System.out.println(nombreColonia + " NO CONTIENE " + sub);
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
            return cadena;
          }
          // SI NO SE ENCONTRO LA COINCIDENCIA
          else{
            System.out.println("NO SE ENCONTRO EL TIPO DE ASENTAMIENTO UNIDAD HABITACIONAL");
            System.out.println(nombreColonia + " NO CONTIENE " + sub);
          }
        }
    }
    return cadena;
  }
}
