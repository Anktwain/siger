package carga;

import dao.GestorDAO;
import dto.Fila;
import dto.Gestor;
import impl.GestorIMPL;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author brionvega
 */
public class Asignador {

  /**
   * Asigna los créditos de acuerdo a un determinado criterio. Para asignar se
   * vale de otros métodos a los que invoca de acuerdo al criterio especificado.
   *
   * @param filas Lista de objetos Filas que representan a los créditos que van
   * a ser asignados.
   * @param gestores Lista de las claves de los gestores que participarán de la
   * asignación.
   * @param criterio El criterio de asignación, puede ser "montos" o "zonas".
   */
  public static void asignar(List<Fila> filas, List<String> gestores, String criterio) {
    /* Evalúa el criterio de asignación. */
    if (criterio.equals("zonas")) {
      asignarPorZonas(filas, gestores); /* Asignación por zonas. */
    }
    if (criterio.equals("montos")) {
      asignarPorMontos(filas, gestores); /* Asignación por montos. */
    }
  }

  /**
   * Asigna los créditos por zonas.
   *
   * @param filas Lista de objetos Filas que representan a los créditos que van
   * a ser asignados.
   * @param gestores Lista de las claves de los gestores que participarán de la
   * asignación.
   */
  private static void asignarPorZonas(List<Fila> filas, List<String> gestores) {

  }

  /**
   * Asigna los créditos por montos.
   *
   * @param filas Lista de objetos Filas que representan a los créditos que van
   * a ser asignados.
   * @param gestores Lista de las claves de los gestores que participarán de la
   * asignación.
   */
  private static void asignarPorMontos(List<Fila> filas, List<String> gestores) {
    /* En primer lugar ordena las filas contenidas en la lista dada, en orden ascendente,
    de acuerdo al saldo vencido. Esto permite lograr un reparto más equitativo.*/
    Collections.sort(filas);
    
    /* Convierte la lista de claves de gestores en un arreglo que contendrá esas
    claves. */
    String[] participantes = gestores.toArray(new String[gestores.size()]);

    int i = 0; // inicio de la lista de filas
    int f = filas.size() - 1; // fin de la lista de filas
    
    /* Inicia algoritmo de asignación. */
    while (true) {
      /* Recorre el arreglo de las claves de los gestores participantes. */
      for (int g = 0; g < participantes.length; g++) {
        if (i > f) {
          return; /* Termina algoritmo. */
        }
        
        /* Hace una asignación lineal; al primer crédito le asigna el primer gestor
        cuyo id aparece en el arreglo, al segundo crédito le asigna el segundo
        gestor que aparece en el arreglo, etc. */
        filas.get(i++).setIdGestor(Integer.parseInt(participantes[g]));
      }

      /* Vuelve a recorrer el arreglo de las claves de los gestores participantes. */
      for (int g = 0; g < participantes.length; g++) {
        if (i > f) {
          return; /* Termina algoritmo. */
        }
        
        /* Hace una asignación lineal; esta vez al último crédito le asigna el primer
        gestor cuyo id aparece en el arreglo, al penúltimo crédito le asigna el segundo
        gestor que aparece en el arreglo, etc. */
        filas.get(f--).setIdGestor(Integer.parseInt(participantes[g]));
      }
      
      /* Una vez que se ejecuta la primera ronda de asignaciones, reacomoda el
      arreglo de participantes. */
      reacomodar(participantes);
    }

  }

  /**
   * Reacomoda un arreglo de Strings que representan ids de los gestores
   * participantes en la asignación.
   *
   * @param participantes Arreglo de los ids de los gestores participantes.
   */
  private static void reacomodar(String participantes[]) {
    String aux; /* Variable auxiliar*/
    
    /* En aux se guarda el primer elemento del arreglo. */
    aux = participantes[0];
    
    /* Recorre el arreglo de participantes. */
    for (int z = 0; z < participantes.length - 1; z++) {
      /* Recorre los elementos, colocando en la posición z el elemento contenido
      en la posición z+1. */
      participantes[z] = participantes[z + 1];
    }
    
    /* finalmente coloca en la última posición del arreglo el elemento que originalmente
    se encontraba en la primera posición. */
    participantes[participantes.length - 1] = aux;

  }

  /**
   * Obtiene una lista de objetos Gestor que corresponden a un despacho de un
   * determinado id.
   *
   * @param idDespacho Id del despacho del cual se quieren obtener sus gestores
   * asociados.
   * @return Lista de objetos Gestor pertenecientes al despacho dado.
   */
  public static List<Gestor> getGestores(int idDespacho) {
    GestorDAO gestorDao = new GestorIMPL();
    return gestorDao.buscarPorDespacho(idDespacho);
  }
}
