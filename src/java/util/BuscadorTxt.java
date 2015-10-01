package util;

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

/**
 *
 * @author Pablo
 */
public class BuscadorTxt {

  public static List<String> buscarTxt(String cp, int lineaInicio) throws Exception {

    List<String> coincidencias = new ArrayList<>();
    try (BufferedReader buferLectura = new BufferedReader(new FileReader(Directorios.RUTA_COLONIAS))) {
      String lineaActual;

      for (int i = 1; i < lineaInicio; i++) {
        buferLectura.readLine(); // Recorre el bufer de lectura hasta la línea indicada
      }
      while ((lineaActual = buferLectura.readLine()) != null) {
        if (lineaActual.substring(lineaActual.length() - 5).equals(cp)) {
          coincidencias.add(lineaActual);
        }
      }
    } catch (IOException ioe) {
      throw new Exception("Error de lectura/escritura.", ioe);
    }
    return coincidencias;
  }

  /**
   * Ejemplo 1
   */
  public static String getCurrentLine(String filepath, long currentPosition) throws FileNotFoundException, IOException {
    RandomAccessFile archivoAccAleat = new RandomAccessFile(Directorios.RUTA_COLONIAS, "rw");

    byte b = archivoAccAleat.readByte();
    while (b != 10) {
      currentPosition -= 1;
      archivoAccAleat.seek(currentPosition);
      b = archivoAccAleat.readByte();
      if (currentPosition <= 0) {
        archivoAccAleat.seek(0);
        String currentLine = archivoAccAleat.readLine();
        archivoAccAleat.close();
        return currentLine;
      }
    }
    String line = archivoAccAleat.readLine();
    archivoAccAleat.close();
    return line;

  }

  /**
   * Ejemplo 2
   */
  public void seekableByteChannelExample(String[] args) throws IOException {
    String cp = args[1];
    SeekableByteChannel canal = Files.newByteChannel(Paths.get(Directorios.RUTA_COLONIAS), StandardOpenOption.READ);
    ByteBuffer buferBytes = ByteBuffer.allocate(512);
    int posicionInicial = 1;
    String cadActual, coincidencia;
    for (int i = posicionInicial; canal.read(buferBytes) > 0; i++) {
      cadActual = String.valueOf(buferBytes.flip().array());
      if (cadActual.equals(cp)) {

      }
      coincidencia = cadActual;
      buferBytes.clear();
    }
  }

  /**
   * Ejemplo 3. 
   */
  public void irALinea(long numElem) throws Exception {

    try (Stream<String> lines = Files.lines(Paths.get(Directorios.RUTA_COLONIAS))) {
      String lineaEnCuestion = lines.skip(numElem).findFirst().get();
    } catch (IOException ioe) {
      throw new Exception("Error de I/O en el archivo " + Directorios.RUTA_COLONIAS, ioe);
    }
    System.out.println("");
  }

}
