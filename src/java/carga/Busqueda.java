package carga;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import util.FuncionLike;
import util.Levenshtein;

/**
 *
 * @author brionvega
 */
public class Busqueda {

  private static List<String> resultado = new ArrayList<>();
  private static String[] coloniaDivididaEnPalabras;

  public static List<String> buscarDireccion(List<String> coincidencias, String coloniaABuscar) {
    String colonia = coloniaABuscar.toLowerCase();
    String coloniaSinArticulo;
    String coloniaSinAsentamiento;
 //   List<String> resultados = new ArrayList<>();

    System.out.println("Para la colonia " + coloniaABuscar);
//    List<String> colonias = FuncionLike.buscarLikeLevenshtein(coincidencias, coloniaABuscar, 4);
//
//    if (colonias != null) {
//      resultados = buscarExacto(colonias, colonia);
//      if (resultados != null) {
//        return resultados;
//      }
//    }

    List<String> colonias = buscarExacto(coincidencias, colonia);

    if(colonias != null){
      return colonias;
    }
    
    coloniaSinArticulo = CriteriosDeBusqueda.eliminarArticulo(colonia);
    if(!colonia.equals(coloniaSinArticulo)){
      colonias = buscarExacto(coincidencias, coloniaSinArticulo);
      if(colonias != null)
        return colonias;
    }
    
    coloniaSinAsentamiento = CriteriosDeBusqueda.eliminarAsentamiento(colonia);
    if(!colonia.equals(coloniaSinAsentamiento)){
      colonias = buscarExacto(coincidencias, coloniaSinAsentamiento);
      if(colonias != null)
        return colonias;
    }
    
    colonias = buscarLike(coincidencias, colonia);
    if(colonias != null)
      return colonias;
    
    colonias = FuncionLike.buscarLikeLevenshtein(coincidencias, colonia, 4);
        if(colonias != null)
      return colonias;
    
    return null;
  }

  private static List<String> buscarExacto(List<String> coincidencias, String colonia) {
    for (String coincidencia : coincidencias) {
      //coincidencia{id_colonia;id_municipio;tipo;nombre;codigo_postal}
      coloniaDivididaEnPalabras = coincidencia.split(";"); // punto y coma como elemento divisor
      System.out.println("buscarExacto:Compara: " + colonia + " con: " + coloniaDivididaEnPalabras[3].toLowerCase());
      if (colonia.equals(coloniaDivididaEnPalabras[3].toLowerCase())) {
        resultado.add(coincidencia);
        return resultado;
      }
    } // fin de for
    return buscarLevenshtein(coincidencias, colonia);
  }

  private static List<String> buscarLevenshtein(List<String> coincidencias, String colonia) {
    for (String coincidencia : coincidencias) {
      coloniaDivididaEnPalabras = coincidencia.split(";");
      System.out.println("buscarLevenshtein:Compara: " + colonia + " con: " + coloniaDivididaEnPalabras[3].toLowerCase());
      if (Levenshtein.computeLevenshteinDistance(colonia, coloniaDivididaEnPalabras[3].toLowerCase()) < 4) {
        resultado.add(coincidencia);
        return resultado;
      }
    } // fin de for
    return null;
  }

  private static List<String> buscarLike(List<String> coincidencias, String colonia) {
    for (String coincidencia : coincidencias) {
      coloniaDivididaEnPalabras = coincidencia.split(";");
      Pattern patron = Pattern.compile(".*" + colonia + ".*"); // formamos expresión regular
      System.out.println("buscarLike:Compara: " + colonia + " con: " + coloniaDivididaEnPalabras[3].toLowerCase());
      if (patron.matcher(coloniaDivididaEnPalabras[3].toLowerCase()).matches()) {
        resultado.add(coincidencia);
      }
    }

    if (resultado.size() > 0) {
      return resultado;
    }

    return null;
  }

}
