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
import util.log.Logs;

/**
 *
 * @author Pablo
 */
public class BuscadorTxt {

  public static List<String> buscarTxt(String cp, String archivoaLeer)
  {
    List<String> coincidencias = new ArrayList<>(); // La lista de colonias cuyo CP coincide con el CP dado
    String lineaActual; // La línea leída en un momento determinado.
    
    try(BufferedReader buferLectura = new BufferedReader(new FileReader(archivoaLeer))){
      
      while((lineaActual = buferLectura.readLine()) != null){
        if(lineaActual.substring(lineaActual.length() - 5).equals(cp)){
          do{
            coincidencias.add(lineaActual);
            lineaActual = buferLectura.readLine();
            if(lineaActual == null) break;
          } while(lineaActual.substring(lineaActual.length() - 5).equals(cp));
          break; // Rompe el ciclo dado que no habrán más coincidencias
        } // fin de if
      } // fin de while
    } catch(IOException ioe){
      Logs.log.error("Error de lectura/escritura");
      Logs.log.error(ioe.getMessage());
    }
    
    return coincidencias;
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

}

/**
 * Ejemplo 2
 */
class SeekableByteChannelExample {

  public static void main(String[] args) throws IOException {
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
}
