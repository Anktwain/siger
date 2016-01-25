package carga;

import dto.Fila;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import util.BautistaDeArchivos;
import util.constantes.Directorios;
import util.log.Logs;

/**
 *
 * @author antonio
 */
public class Cargador {

  // El nombre del archivo generado del lado del servidor.
  private String nombreArchivo;
  
  // Mes de carga
  private int mesCarga;

  public Cargador(int mesCarga) {
    this.mesCarga = mesCarga;
  }

  /**
   * Método que sube un archivo de carga al servidor. Este archivo deberá ser en
   * formato xls.
   *
   * @param evento el evento invocado desde el fileUpload.
   * @return El nombre original del archivo subido por el usuario. En caso de
   * que el archivo no pudiera cargarse, enviará null.
   */
  public String subirArchivo(FileUploadEvent evento) {
    UploadedFile archivoRecibido = evento.getFile(); // Obtiene el archivo.
    
    /* cambia el nombre original del archivo por un nombre generado por el método
      "bautizar" de la clase BautistaDeArchivos. Esa clase se encarga de generar
      nombres que no se repitan.
    */
    nombreArchivo = BautistaDeArchivos.bautizar(archivoRecibido.getFileName(),
            Directorios.RUTA_REMESAS + "Remesa", BautistaDeArchivos.PREFIJO);

    byte[] bytes = null;
    String ok; // Permite conocer si la operación fue o no exitosa.

    try {
      if (archivoRecibido != null) {
        bytes = archivoRecibido.getContents();
        BufferedOutputStream stream = new BufferedOutputStream(
                new FileOutputStream(new File(nombreArchivo)));
        stream.write(bytes);
        stream.close();
      }
      Logs.log.info("Se carga archivo al servidor: " + nombreArchivo);
      ok = archivoRecibido.getFileName();
    } catch (IOException ioe) {
      Logs.log.error("No se pudo cargar el archivo de la remesa: " + nombreArchivo);
      Logs.log.error(ioe.getMessage());
      ok = null;
    }

    return ok;
  }

  /**
   * Método que lee un archivo de carga del servidor. El archivo a leer será el
   * indicado por "nombreArchivo", este archivo deberá tener formato xls.
   *
   * @return Una lista de objetos Fila contenidos en el archivo.
   */
  public List<Fila> leerArchivo() throws IOException, BiffException {
    /* Objeto que aloja al libro de excel contenido en el archivo a leer */
    Workbook libroExcel = Workbook.getWorkbook(new File(nombreArchivo));
    
    /* Crea un objeto de tipo ProcesadorArchivoExcel, el cual se encarga de
    leer del archivo Excel exactamente los datos que necesitará para la carga. */
    ProcesadorArchivoExcel procesador = new ProcesadorArchivoExcel(libroExcel, mesCarga);
    
    /* Ahora obtiene todas las filas contenidas en el archivo Excel. */
    List<Fila> listaFilas = procesador.obtenerFilas();

    /* Regresa el resultado de leer filas.*/
    if (listaFilas.size() > 1) {
      return listaFilas;
    } else {
      return null;
    }
  }

  public void setMesCarga(int mesCarga) {
    this.mesCarga = mesCarga;
  }
}
