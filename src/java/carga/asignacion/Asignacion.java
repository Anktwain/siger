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
   * Ordena {@code nuevosTotales} de mayor a menor con base en el monto de su
   * saldo vencido.
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
     * Calculamos el número de iteraciones. Por iteracion se reparten dos
     * créditos a cada gestor.
     */
    int iteraciones = this.nuevosTotales.size() / (2 * gestores.size());

    /**
     * Se reparten de a dos en dos los créditos entre los gestores, dandole a
     * cada uno el mayor y el menor dispoinibles en cada iteración. Pueden
     * sobrar hasta (2*n)-1 creditos por repartir.
     */
    for (int i = 0; i < iteraciones; i++) {

    }

    int restantes = this.nuevosTotales.size() % (2 * gestores.size());

    if (restantes > 0) {
      ArrayList<Fila> disponibles = new ArrayList<Fila>();
      for (int i = 0; i < restantes; i++) {
        disponibles.add(nuevosTotales.get(iteraciones + i));
      }
      if (restantes % gestores.size() != 0) {
        /**
         * Se reparte una vez más entre todos los gestores, ahora comenzando por
         * el último en la lista. Pueden sobrar hasta n-1 creditos por repartir.
         */
        for (int i = 0; i < gestores.size(); i++) {

          disponibles.remove();
        }
      } else if (restantes > 0) {
        Collections.min(gestores, new Comparator<ArrayList<String>>() {

          @Override
          public int compare(ArrayList<String> o1, ArrayList<String> o2) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
          }

        });
      }

    }

  }

}
