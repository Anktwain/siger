package util;

import com.mysql.jdbc.log.Log;
import dto.Fila;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import util.constantes.Directorios;

import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;
import util.log.Logs;

/**
 *
 * @author Pablo
 */
public class BuscadorTxt {

  private static BufferedReader buferLectura;
  private static RandomAccessFile randomAccessFile;
  private SeekableByteChannel canalBytes;
  private static Stream<String> archPorLineas;

  public BuscadorTxt() throws Exception {

    try { // 
      buferLectura = new BufferedReader(new FileReader(Directorios.RUTA_COLONIAS));
    } catch (Exception e) {
      throw new Exception("Error al crear el archivo para lectura secuencial.", e);
    }
    try { // 
      // Crea un archivo de acceso aleatorio:
      randomAccessFile = new RandomAccessFile(Directorios.RUTA_COLONIAS, "r");
    } catch (Exception e) {
      throw new Exception("Error al crear el archivo de acceso aleatorio.", e);
    }
    try { // 
      canalBytes = Files.newByteChannel(Paths.get(Directorios.RUTA_COLONIAS), StandardOpenOption.READ);
    } catch (Exception e) {
      throw new Exception("Error al crear el archivo de lectura ByteChannel.", e);
    }
    try { // 
      archPorLineas = Files.lines(Paths.get(Directorios.RUTA_COLONIAS));
    } catch (Exception e) {
      throw new Exception("Error al crear el archivo de lectura por numero de lineas.", e);
    }
  }

  /**
   * La buena. 
   *
   * @return La {@code List<String>} que contiene todas las coincidencias
   * encontradas en el archivo de texto de colonias correspondiente. Cada uno de
   * los elementos en la lista es un solo {@code String} que describe un tipo de
   * asentamiento. dicha cadena es de la forma:
   * <strong>a;b;c;d;e</strong>
   * tal que:
   * <ul>
   * <li><strong>a</strong>: entero: clave de la colonia (o tipo de
   * asentamiento)</li>
   * <li><strong>b</strong>: entero; clave del municipio (o delegación)</li>
   * <li><strong>c</strong>: alfabético: Tipo de asentamiento </li>
   * <li><strong>d</strong>: alfabético: Nombre del tipo de asentamiento</li>
   * <li><strong>e</strong>: entero, 5 cifras: código postal</li>
   * </ul>
   */
  public static List<String> buscarTxt(String cp, String archivoaLeer) {
    List<String> coincidencias = new ArrayList<>(); // La lista de colonias cuyo CP coincide con el CP dado
    String lineaActual; // La línea leída en un momento determinado.

    try {
      BufferedReader buferLectura = new BufferedReader(new FileReader(archivoaLeer));

      while ((lineaActual = buferLectura.readLine()) != null) {
        if (lineaActual.substring(lineaActual.length() - 5).equals(cp)) {
          do {
            coincidencias.add(lineaActual);
            lineaActual = buferLectura.readLine();
            if (lineaActual == null) {
              break;
            }
          } while (lineaActual.substring(lineaActual.length() - 5).equals(cp));
          break; // Rompe el ciclo dado que no habrán más coincidencias
        } // fin de if
      } // fin de while
    } catch (IOException ioe) {
      Logs.log.error("Error de lectura/escritura");
      Logs.log.error(ioe.getMessage());
    }

    return coincidencias;
  }

  /**
   * Esta es una version sobrecargada de la anterior que se utilizara para solo
   * leer un archivo y dirigirse con base en el numero de linea
   */
  public static List<String> buscarTxt(String cp, int lineaInicio) throws Exception {
    List<String> coincidencias = new ArrayList<>();
    String lineaActual;

    
    String lineaEnCuestion;
    try {
      lineaEnCuestion = archPorLineas.skip(lineaInicio).findFirst().get();
    } catch (Exception e) {
      throw new Exception("Error de I/O en el archivo " + Directorios.RUTA_COLONIAS, e);
    }
    System.out.println("");
        
    
    try {
      for (int i = 1; i < lineaInicio; i++) {
        // Solo recorre el bufer de lectura hasta la línea indicada
        buferLectura.readLine();
      }
      while ((lineaActual = buferLectura.readLine()) != null) {
        if (lineaActual.substring(lineaActual.length() - 5).equals(cp)) {
          do {
            coincidencias.add(lineaActual);
            lineaActual = buferLectura.readLine();
          } while (lineaActual.substring(lineaActual.length() - 5).equals(cp));
          break;
        }
      }
    } catch (IOException ioe) {
      throw new Exception("Error de lectura/escritura.", ioe);
    }
    return coincidencias;
    
    
    
    
  }

  public static List<String> buscarTxt(String cp, long byteInicio) throws FileNotFoundException, IOException {
    List<String> listaCoincidencias = new ArrayList<>();

    // Almacena la líne leída actualmente
    String lineaActual;

    // coloca el puntero a donde indica byteInicio:
    randomAccessFile.seek(byteInicio);
    // deshecha la línea siguiente:
    randomAccessFile.readLine();

    while ((lineaActual = randomAccessFile.readLine()) != null) {
      if (lineaActual.substring(lineaActual.length() - 5).equals(cp)) {
        do {
          listaCoincidencias.add(lineaActual);
          lineaActual = randomAccessFile.readLine();
        } while (lineaActual.substring(lineaActual.length() - 5).equals(cp));
        break;
      }
    }

    return listaCoincidencias;
  }

  /**
   * Ejemplo 1
   */
  private static String currentLine(String filepath, long currentPosition) throws FileNotFoundException, IOException {
    RandomAccessFile f = new RandomAccessFile(Directorios.RUTA_COLONIAS, "rw");

    byte b = f.readByte();
    while (b != 10) {
      currentPosition -= 1;
      f.seek(currentPosition);
      b = f.readByte();
      if (currentPosition <= 0) {
        f.seek(0);
        String currentLine = f.readLine();
        f.close();
        return currentLine;
      }
    }
    String line = f.readLine();
    f.close();
    return line;

  }

  /**
   * Ejemplo 2
   */
  public void seekableByteChannelExample(String codigoPostal) throws IOException {

    ByteBuffer buferBytes = ByteBuffer.allocate(512);
    int posicionInicial = 1;
    String cadActual, coincidencia;

    for (int i = posicionInicial; canalBytes.read(buferBytes) > 0; i++) {
      cadActual = String.valueOf(buferBytes.flip().array());
      if (cadActual.equals(codigoPostal)) {

      }
      coincidencia = cadActual;
      buferBytes.clear();
    }
  }

  /**
   * Ejemplo 3.
   */
  public void irALinea(long numElem) throws Exception {
    String lineaEnCuestion;
    try {
      lineaEnCuestion = archPorLineas.skip(numElem).findFirst().get();
    } catch (Exception e) {
      throw new Exception("Error de I/O en el archivo " + Directorios.RUTA_COLONIAS, e);
    }
    System.out.println("");
  }

  /**
   * Cierra todos los archivos abiertos por el constructor de la clase
   */
  public void cerrar() throws Exception {
    try {
      buferLectura.close();
      randomAccessFile.close();
      canalBytes.close();
    } catch (IOException ioe) {
      throw new Exception("Error al cerrar alguno de los archivos abiertos.", ioe);
    }
    archPorLineas.close();
  }

}
