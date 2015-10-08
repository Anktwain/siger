package morfeo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
//import java.util.function.Function;
//import java.util.stream.Stream;
import util.BuscadorTxt;
import util.constantes.Directorios;

/**
 *
 * @author John Doe
 */
public class BuscadorTXT {

  private static BufferedReader buferLectura;
  private static RandomAccessFile randomAccessFile;
  private SeekableByteChannel canalBytes;
//  private static Stream<String> archPorLineas;

  /**
   *
   * @throws Exception
   */
  public BuscadorTXT() throws Exception {
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
//    try { // 
//      archPorLineas = Files.lines(Paths.get(Directorios.RUTA_COLONIAS));
//    } catch (Exception e) {
//      throw new Exception("Error al crear el archivo de lectura por numero de lineas.", e);
//    }
  }

//  static Function<String, BuscadorTxt.Person> mapToPerson = (line) -> {
//    String[] p = line.split(", ");
//    return new BuscadorTxt.Person(p[0], Integer.parseInt(p[1]), p[2], p[3]);
//  };

  /**
   *
   * Esta es una version sobrecargada de la anterior que se utilizara para solo
   * leer un archivo y dirigirse con base en el numero de linea
   * @param cp
   * @param lineaInicio
   * @return 
   * @throws java.lang.Exception 
   */
  public static List<String> buscarTxt(String cp, int lineaInicio) throws Exception {
    List<String> coincidencias = new ArrayList<>();
    String lineaActual;
    String lineaEnCuestion;

    InputStream is = new FileInputStream(new File("persons.csv"));
    BufferedReader br = new BufferedReader(new InputStreamReader(is));

//    List<BuscadorTxt.Person> persons = br.lines()
//            .substream(1)
//            .map(mapToPerson)
//            .filter(person -> person.getAge() > 17)
//            .limit(50)
//            .collect(toList());
    //In the example we see that we skip the first line (this is the header line in our CSV file), using the substream(1) function.
    //Next we map the person from a CSV line to a Person object. We use a predefined lambda function for this:
    //A bit hackish

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

  /**
   *
   * @param cp
   * @param byteInicio
   * @return
   * @throws FileNotFoundException
   * @throws IOException
   */
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
   * @param codigoPostal
   * @throws java.io.IOException
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
   * @param numElem
   * @throws java.lang.Exception
   */
  public void irALinea(long numElem) throws Exception {
    String lineaEnCuestion;
//    try {
//      lineaEnCuestion = archPorLineas.skip(numElem).findFirst().get();
//    } catch (Exception e) {
//      throw new Exception("Error de I/O en el archivo " + Directorios.RUTA_COLONIAS, e);
//    }

//    try {
////            lineaEnCuestion = archPorLineas.skip(lineaInicio).findFirst().get();
//      archPorLineas.forEach(s -> System.out.println(s));
//
//    } catch (Exception e) {
//      throw new Exception("Error de I/O en el archivo " + Directorios.RUTA_COLONIAS, e);
//    }

    System.out.println("");
  }

  private static class Person {

    public Person() {
    }

    /**
     *
     */
    private Person(String name, int age, String city, String country) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

  }

  /**
   * Cierra todos los archivos abiertos por el constructor de la clase
   * @throws java.lang.Exception
   */
  public void cerrar() throws Exception {
    try {
      buferLectura.close();
      randomAccessFile.close();
      canalBytes.close();
    } catch (IOException ioe) {
      throw new Exception("Error al cerrar alguno de los archivos abiertos.", ioe);
    }
//    archPorLineas.close();
  }
}
