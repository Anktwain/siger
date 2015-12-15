package carga;

import dto.Fila;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import jxl.Sheet;
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

  private String nombreArchivo;
  private int numeroFilas;
  private int numeroColumnas;
  private Workbook libroExcel;
  private Sheet hojaExcel;
  private Fila fila;
  private List<Fila> listaFilas;

  public String subirArchivo(FileUploadEvent evento) {
    UploadedFile archivoRecibido = evento.getFile();
    nombreArchivo = BautistaDeArchivos.bautizar(archivoRecibido.getFileName(),
            Directorios.RUTA_REMESAS + "Remesa", 1);

    byte[] bytes = null;
    String ok;

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

  public List<Fila> leerArchivo() throws IOException, BiffException {
    libroExcel = Workbook.getWorkbook(new File(nombreArchivo));
    ProcesadorArchivoExcel procesador = new ProcesadorArchivoExcel(libroExcel);
    listaFilas = procesador.obtenerFilas();

    if (listaFilas.size() > 1) {
      return listaFilas;
    } else {
      return null;
    }

  }
}
