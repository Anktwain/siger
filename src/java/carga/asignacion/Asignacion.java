package carga.asignacion;

import dto.Fila;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author Pablo
 */
public class Asignacion {

  private ArrayList<Fila> nuevosTotales;
  private ArrayList<ArrayList<String>> gestores;

  /**
   * Ordena {@code nuevosTotales} de mayor a menor con base en el monto de su saldo
   * vencido.
   */
  public void ordenarDecreceiente() {
    Collections.sort(nuevosTotales, new Comparator<Fila>() {
      @Override
      public int compare(Fila f1, Fila f2) {
        if (Float.valueOf(f1.getSaldoVencido()) < Float.valueOf(f2.getSaldoVencido())) {
          return -1;
        } else if (Float.parseFloat(f1.getSaldoVencido()) == Float.parseFloat(f2.getSaldoVencido())) {
          return 0;
        } else {
          return 1;
        }
      }
    });
  }
/**
 * 
 */
  public void asignarNuevosTotales() {
   
    /**
     * 
     *
     */
    
    int iteraciones = this.nuevosTotales.size() / (2 * gestores.size());

    for (int i = 0; i < iteraciones; i++) {

    }

  }

}
