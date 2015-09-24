package util;

import dto.Fila;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Pablo
 */
public class BuscadorTxt {

  private ArrayList<String> lineasCoincidentes;

  public ArrayList<String> getLineasCoincidentes() {
    return lineasCoincidentes;
  }

  public void setLineasCoincidentes(ArrayList<String> lineasCoincidentes) {
    this.lineasCoincidentes = lineasCoincidentes;
  }

  public BuscadorTxt(Fila filaActual) throws Exception {

    try (BufferedReader buferLectura = new BufferedReader(new FileReader("C:\\Users\\Pablo.CORPDELRIO\\Documents\\NetBeansProjects\\Siger_web_2\\src\\java\\direcciones\\colonias.csv"))) {

      lineasCoincidentes = new ArrayList<>();
      String cp = filaActual.getCp();
      String lineaActual;

      while ((lineaActual = buferLectura.readLine()) != null) {
        if (lineaActual.substring(lineaActual.length() - 5).equals(cp)) {
          lineasCoincidentes.add(lineaActual);
        }
      }
    } catch (IOException ioe) {
      throw new Exception("Error de lectura/escritura.", ioe);
    }
  }
}
