package carga;

import dto.Fila;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author brionvega
 */
public class Asignador {
  
  public static void asignar(List<Fila> filas, List<String> gestores, String criterio) {
    if(criterio.equals("zonas"))
      asignarPorZonas(filas, gestores);
    if(criterio.equals("montos"))
      asignarPorMontos(filas, gestores);
  }
  
  private static void asignarPorZonas(List<Fila> filas, List<String> gestores) {
    
  }
  
  private static void asignarPorMontos(List<Fila> filas, List<String> gestores) {
    Collections.sort(filas);
    String[] participantes = gestores.toArray(new String[gestores.size()]);
    
    int i = 0; // inicio de la lista de filas
    int f = filas.size() - 1; // fin de la lista de filas
    while (true) {
      for (int g = 0; g < participantes.length; g++) {
        if (i > f) {
          return;
        }
        filas.get(i++).setIdGestor(Integer.parseInt(participantes[g]));
      }

      for (int g = 0; g < participantes.length; g++) {
        if (i > f) {
          return;
        }
        filas.get(f--).setIdGestor(Integer.parseInt(participantes[g]));
      }
      reacomodar(participantes);
    }
    
  }
  
  private static void reacomodar(String participantes[]) {
    String aux;
      aux = participantes[0];
      for(int z = 0; z < participantes.length - 1; z++){
        participantes[z] = participantes[z+1];
      }
      participantes[participantes.length - 1] = aux;
    
  }
}