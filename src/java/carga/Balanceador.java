package carga;

import dto.Asignacion;
import dto.Fila;
import java.util.List;

/**
 *
 * @author brionvega
 */
public class Balanceador {

  public static void reasignar(Fila credito, int gestorNuevo, List<Asignacion> asignaciones, Asignacion asignacionActual) {
    int gestorActual = credito.getIdGestor();
    
    for(Asignacion a : asignaciones) {
      if(a.getGestor() == gestorNuevo) {
        a.getCreditos().add(credito);
      }
    }
    
    // elimina
    asignacionActual.getCreditos().remove(credito);
    // Asigna el id del nuevo gestor:
    credito.setIdGestor(gestorNuevo);
  }
}