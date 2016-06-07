package carga;

import dto.Asignacion;
import dto.Fila;
import java.util.List;

/**
 *
 * @author brionvega
 */
public class Balanceador {

  /**
   * Reasigna un crédito, dado por el objeto Fila "credito", a un gestor cuyo id
   * viene dado por "gestorNuevo".
   *
   * @param credito Objeto Fila que representa el crédito a reasignar.
   * @param gestorNuevo Entero que representa el id del gestor al cual se le
   * reasignará el crédito dado.
   * @param asignaciones Arreglo de objetos Asignacion que representa a todas las
   * asignaciones existentes para esta carga.
   * @param asignacionActual Objeto Asignacion que representa la asignación actual
   * en la cual se encuenta el objeto Fila dado por "credito".
   */
  public static void reasignar(Fila credito, int gestorNuevo, List<Asignacion> asignaciones, Asignacion asignacionActual) {
    
    /* Recorre la lista de objetos Asignacion contenida en "asignaciones". */
    for(Asignacion a : asignaciones) {
      /* Compara el gestor asociado a la asignación con el gestor nuevo cuyo id
      fue pasado com parámetro. Si coinciden, entonces agrega a la lista "creditos"
      de "asignacion" el objeto Fila llamado "credito" que fue pasado como
      parámetro. */
      if(a.getGestor() == gestorNuevo) {
        a.getCreditos().add(credito);
      }
    }
    
    /* Ahora elimina el crédito reasignado del objeto asignacionActual puesto que
    ya no pertenece a la asignación actual sino a la asignación encontrada en el
    bucle anterior. */
    asignacionActual.getCreditos().remove(credito);
    
    /* Al crédito, además, le cambia el id del gestor. Ahora coloca el id gestorNuevo,
    enviado como parámetro al método. */
    credito.setIdGestor(gestorNuevo);
  }
}