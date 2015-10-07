package carga;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author antonio
 */
public class CriteriosDeBusqueda {
  static String patronArticulos = "^(el|la|los|las)\\s+";
  static String patronAsentamientos = "^(colonia|col\\.|col|fraccionamiento|fracc\\.|fracc|frac\\.|frac|unidad habitacional|u\\. habitacional|u habitacional|unidad h|colunidad h\\.|u\\. h\\.|u h|u\\. hab\\.|u hab|rancheria|ranch|ranch\\.|ran|ran\\.|conjunto habitacional|c\\. habitacional|c habitacional|c\\. h\\.|c h|ampliacion|ampl|ampl\\.|amp|amp\\.|congregacion|cong|con|cong\\.|con\\.|hacienda|h\\.|hda|hda\\.|habitacional|hab|hab\\.)\\s+";

  public static String eliminarAsentamiento(String texto) {
    texto = texto.toLowerCase();
    texto = texto.replaceFirst(patronAsentamientos, "");
    return texto;
  }
  
  public static String eliminarArticulo(String texto) {
    texto = texto.toLowerCase(); // todo a minúsculas, para evitar confuciones
    texto = texto.replaceFirst(patronArticulos, ""); // Reemplaza artículos por cadena vacía
    return texto;
  }
  
  // Devuelve todas las coincidencias de textoaBuscar en textos
  public static List<String> buscarSubcadena(List<String> textos, String textoaBuscar) {
    List<String> coincidencias = new ArrayList<>();
    Pattern patron = Pattern.compile(".*" + textoaBuscar.toLowerCase() + ".*"); // formamos expresión regular
    for(String texto:textos){
      if(patron.matcher(texto.toLowerCase()).matches())
        coincidencias.add(texto); // agrega coincidencias
    }
    return coincidencias;
  }
  
  
  
}
