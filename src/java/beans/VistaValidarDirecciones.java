package beans;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import util.constantes.Directorios;
import util.log.Logs;

/**
 *
 * @author brionvega
 */
@ManagedBean
@ViewScoped
public class VistaValidarDirecciones implements Serializable {
  private List<String> direccionesPorValidar;

  public VistaValidarDirecciones() {
    this.direccionesPorValidar = obtenerListaDeDirecciones();
  }
  
  /**
   * Método que obtiene una lista de direcciones por validar.
   *
   * @return Una lista de las direcciones a validar
   */
  private List<String> obtenerListaDeDirecciones() {
    String lineaActual; // La línea leída en un momento determinado.
    List<String> lineas = new ArrayList<>(); // lista de líneas del archivo de texto.

    try (BufferedReader buferLectura = new BufferedReader(
            new FileReader(Directorios.RUTA_REMESAS + "direccionar2016212123737.txt"))) {
      while ((lineaActual = buferLectura.readLine()) != null) {
        lineas.add(lineaActual); // Agrega la linea recién leída a la lista.
      }
      buferLectura.close();
    } catch (IOException ioe) {
      Logs.log.error("Error de lectura/escritura");
      Logs.log.error(ioe.getMessage());
      lineas = null;
    }

    // Si la operación fue exitosa, envía la lista los encabezados de las columnas útiles.
    return lineas;
  }

  public List<String> getDireccionesPorValidar() {
    return direccionesPorValidar;
  }
  
}
