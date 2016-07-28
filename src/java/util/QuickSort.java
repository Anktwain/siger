/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import dto.Impresion;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public class QuickSort {

  // METODO QUE ORDENA LA LISTA DE IMPRESIONES POR CODIGO POSTAL
  public List<Impresion> quickSortCp(List<Impresion> lista) {
    quickSortParaCp(lista, 0, (lista.size()) - 1);
    return lista;
  }

  // METODO QUE ES EL QUICKSORT BASICO CON ENTEROS
  public void quickSortParaCp(List<Impresion> arreglo, int inicio, int fin) {
    if (arreglo == null || arreglo.isEmpty()) {
      return;
    }
    if (inicio >= fin) {
      return;
    }
    int mitad = (inicio + fin) / 2;
    int pivote = Integer.parseInt(arreglo.get(mitad).getDireccion().getColonia().getCodigoPostal());
    int i = inicio, j = fin;
    while (i <= j) {
      while (Integer.parseInt(arreglo.get(i).getDireccion().getColonia().getCodigoPostal()) < pivote) {
        i++;
      }
      while (Integer.parseInt(arreglo.get(j).getDireccion().getColonia().getCodigoPostal()) > pivote) {
        j--;
      }
      if (i <= j) {
        int temp = Integer.parseInt(arreglo.get(i).getDireccion().getColonia().getCodigoPostal());
        arreglo.get(i).getDireccion().getColonia().setCodigoPostal(arreglo.get(j).getDireccion().getColonia().getCodigoPostal());
        arreglo.get(j).getDireccion().getColonia().setCodigoPostal(String.valueOf(temp));
        i++;
        j--;
      }
    }
    if (inicio < j) {
      quickSortParaCp(arreglo, inicio, j);
    }
    if (fin > i) {
      quickSortParaCp(arreglo, i, fin);
    }
  }

}
