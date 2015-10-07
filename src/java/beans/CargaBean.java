package beans;

import carga.ClasificadorDeCreditos;
import carga.EjecutarScript;
import dto.Fila;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import jxl.*;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import util.constantes.Directorios;
import util.log.Logs;
import util.Query;
import carga.GestionDeDirecciones;
import java.util.List;

/**
 *
 * @author Cofradía
 */
@ManagedBean
@ViewScoped
public class CargaBean implements Serializable {

  private String nombreArchivo;
  private int numeroDeFilas;
  private int numeroDeColumnas;
  private Workbook archivoExcel;
  private Sheet hojaExcel;
  private Fila fila;
  private String archivoSql;

  // Otro bean
  @ManagedProperty(value = "#{filaBean}")
  private FilaBean filaBean;

  public CargaBean() {
    fila = new Fila();
    filaBean = new FilaBean();
  }

  public boolean subirArchivo(FileUploadEvent e) throws IOException {
    UploadedFile archivoRecibido = e.getFile();

    nombreArchivo = nombrarArchivo(archivoRecibido.getFileName());

    byte[] bytes = null;

    boolean ok;

    try {
      if (archivoRecibido != null) {
        bytes = archivoRecibido.getContents();
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(nombreArchivo)));
        stream.write(bytes);
        stream.close();
      }
      Logs.log.info("Se carga archivo al Servidor: " + nombreArchivo);
      ok = true;
    } catch (IOException ioe) {
      Logs.log.error("No se pudo cargar el archivo de la remesa: " + nombreArchivo);
      Logs.log.error(ioe.getMessage());
      ok = false;
    }

    return ok;
  }

  private String nombrarArchivo(String elArchivo) {
    Calendar calendario = new GregorianCalendar();

    return Directorios.RUTA_REMESAS + "remesa"
            + Integer.toString(calendario.get(Calendar.YEAR))
            + Integer.toString(1 + calendario.get(Calendar.MONTH))
            + Integer.toString(calendario.get(Calendar.DATE))
            + Integer.toString(calendario.get(Calendar.HOUR_OF_DAY))
            + Integer.toString(calendario.get(Calendar.MINUTE))
            + Integer.toString(calendario.get(Calendar.SECOND)) + "."
            + FilenameUtils.getExtension(elArchivo);
  }

  public int leerArchivoExcel() {
    try {
      archivoExcel = Workbook.getWorkbook(new File(nombreArchivo));
      hojaExcel = archivoExcel.getSheet(0);
      numeroDeFilas = hojaExcel.getRows();
      numeroDeColumnas = hojaExcel.getColumns();
      Logs.log.info("Se lee: " + nombreArchivo + ". Filas: " + numeroDeFilas + ". Columnas = " + numeroDeColumnas);
    } catch (Exception ioe) {
      Logs.log.error("No se pudo leer archivo: " + nombreArchivo);
      Logs.log.error(ioe.getMessage());
      numeroDeFilas = -1;
    }

    return numeroDeFilas - 1;
  }

  public boolean crearArchivoSql() throws Exception {
    //String archivoSql = FilenameUtils.getBaseName(nombreArchivo);
    archivoSql = FilenameUtils.getBaseName(nombreArchivo);
    archivoSql = Directorios.RUTA_REMESAS + archivoSql + ".sql";
    int[] ns = {0,0}; // el primer elemento del arreglo indica los casos no exitosos, el segundo, los exitosos.
    String resultado;
    filaBean = new FilaBean();
    fila = new Fila();
    
    // Recorre el archivo xls, pasando por todas sus filas
    for (int i = 1; i < numeroDeFilas; i++) { // Comienza en la fila 1, dado que la fila 0 contiene los encabezados
      System.out.println("FILA " + i);
      crearFila(i); // Crea un objeto Fila para cada fila del archivo xls
      filaBean.setFilaActual(fila);
      // Valida la fila a través de filaBean. Suma 1 para mantener la concordancia
      // con el archivo xls en cuanto al etiquetado de las filas: 1, 2, 3, ...
      if (validarFila(i + 1)) { // i+1 el sólo para efectos de imprimir el número de línea en la cual ocurrió el error en caso de que lo hubiera. Se envía i + 1 para que el número de línea corresponda con el de excel, por ejemplo si el error ocurre en la línea 1, en excel corresponde a la línea 1+1=2, dado que la primera línea es de encabezados
        // Dado que la fila es válida, prosigue...
        // A continuación clasificamos a los créditos como: en la fiesta, estaba en la fiesta, nuevo crédito, nuevo total
        // AQUÍ SE INSERTA LA LLAMADA A UNA FUNCIÓN QUE BUSCA UNA CRÉDITO EN LA BD, RECIBE COMO PARÁMETRO EL CRÉDITO
        // A BUSCAR Y DEVUELVE TRUE O FALSE SI ENCONTRÓ O NO ENCONTRÓ EL CRÉDITO, RESPECTIVAMENTE
        if(buscarCredito(fila.getCredito())){ // Sí se encontró el crédito
          if(seGestionoEnElPeriodoAnterior(fila.getCredito())){
            ClasificadorDeCreditos.enLaFiesta(fila);
          } else {
            ClasificadorDeCreditos.estabaEnLaFiesta(fila);
          }
        } else { // No se encontró el crédito
          if(buscarCliente(fila.getIdCliente())){ // Sí se encontró el cliente
            ClasificadorDeCreditos.nuevoCredito(fila);
          } else { // No se encontró el cliente
            resultado = ClasificadorDeCreditos.nuevoTotal(fila);
            if(resultado.equals("n")){
              ns[0]++;
              Logs.log.error("No se encontró dirección de la fila " + (i + 1));
            }
            else if(resultado.equals("s"))
              ns[1]++;
          }
        }
      } else { // Si la fila no es válida...
        return false;
      }
    } // fin de for
    System.out.println(ns[1] + " direcciones encontradas");
    System.out.println(ns[0] + " direcciones NO encontradas");
    return true;
  }
  
  private boolean buscarCredito(String credito) {
    return false;
  }
  
  private boolean seGestionoEnElPeriodoAnterior(String credito) {
    return false;
  }
  
  private boolean buscarCliente(String cliente) {
    return false;
  }

  public boolean leerArchivoSql() {
    String archivoSql = FilenameUtils.getBaseName(nombreArchivo);
    archivoSql = Directorios.RUTA_REMESAS + archivoSql + ".sql";
    try {
      FileReader fr = new FileReader(archivoSql);
      int lineas = 0;
      BufferedReader bf = new BufferedReader(fr);
      String lineaSql = "";
      while ((lineaSql = bf.readLine()) != null) {
        lineas++;
        Query.ejecutaQuery(lineaSql);
        System.out.println(lineaSql);
      }
      return true;
    } catch (FileNotFoundException fnfe) {
      fnfe.printStackTrace();
      return false;
    } catch (IOException ioe) {
      ioe.printStackTrace();
      return false;
    }
  }

  public boolean ejecutarScriptSql() throws IOException, SQLException {
    EjecutarScript ejecutarScript = new EjecutarScript();
    ejecutarScript.setFile(archivoSql);
    ejecutarScript.ejecutar();
    return true;
  }

  private boolean validarFila(int fila) {
    try {
      filaBean.validarNumCred();
      filaBean.validarNombreRazonSoc();
      filaBean.validarCodPost();
      //filaBean.validarRefCobro();
      //filaBean.validarIdProducto();
      //filaBean.validarIdSubproducto();
      //filaBean.validarEstatus();
      //filaBean.validarMesesVencidos();
      //filaBean.validarFechaInicio();
      //filaBean.validarFechaFin();
      return true;
    } catch (Exception e) {
      Logs.log.error("Ha ocurrido un error en la fila: " + fila);
      Logs.log.error(e.getMessage());
      Logs.log.error(filaBean.getFilaActual().toString());
      return false;
    }
  }

  private void crearFila(int numFila) {

    fila.setCredito(hojaExcel.getCell(0, numFila).getContents());
    fila.setNombre(hojaExcel.getCell(1, numFila).getContents());
    fila.setRefCobro(hojaExcel.getCell(2, numFila).getContents());
    fila.setLinea(hojaExcel.getCell(3, numFila).getContents());
    fila.setTipoCredito(hojaExcel.getCell(4, numFila).getContents());
    fila.setEstatus(hojaExcel.getCell(5, numFila).getContents());
    fila.setMesesVencidos(hojaExcel.getCell(6, numFila).getContents());
    fila.setDespacho(hojaExcel.getCell(7, numFila).getContents());
    fila.setFechaInicioCredito(hojaExcel.getCell(8, numFila).getContents());
    fila.setFechaVencimientoCred(hojaExcel.getCell(9, numFila).getContents());
    fila.setDisposicion(hojaExcel.getCell(10, numFila).getContents());
    fila.setMensualidad(hojaExcel.getCell(11, numFila).getContents());
    fila.setSaldoInsoluto(hojaExcel.getCell(12, numFila).getContents());
    fila.setSaldoVencido(hojaExcel.getCell(13, numFila).getContents());
    fila.setTasa(hojaExcel.getCell(14, numFila).getContents());
    fila.setCuenta(hojaExcel.getCell(15, numFila).getContents());
    fila.setFechaUltimoPago(hojaExcel.getCell(16, numFila).getContents());
    fila.setFechaUltimoVencimientoPagado(hojaExcel.getCell(17, numFila).getContents());
    fila.setIdCliente(hojaExcel.getCell(18, numFila).getContents());
    fila.setRfc(hojaExcel.getCell(19, numFila).getContents());
    fila.setCalle(hojaExcel.getCell(20, numFila).getContents());
    fila.setCp(hojaExcel.getCell(24, numFila).getContents());
    fila.setColonia(hojaExcel.getCell(21, numFila).getContents());
    // VALIDACION POR CODIGO POSTAL
//    if ((fila.getCp() == null) && (fila.getColonia() == null)) { // Si el objeto Fila no tiene ni Colonia ni código postal...
//      try{
//        // RECIBIMOS LA LISTA CON IDS ENVIANDO EL CODIGO POSTAL
//        List<String> ids = GestionDeDirecciones.verificaPorCodigoPostal(fila.getCp(), fila.getColonia());
//        // VERIFICACION DE ERRORES
//        if(ids.get(2) == null){
//          System.out.println("NO SE VERIFICO EL CODIGO POSTAL " + fila.getCp() + " EN LA FILA " + numFila + " DEL ARCHIVO");
//        }
//        else{
//        // TOMAMOS LOS VALORES DE LA LISTA Y LOS ASIGNAMOS AL OBJETO FILA
//        fila.setEstado(ids.get(0));
//        fila.setMunicipio(ids.get(1));
//        fila.setColonia(ids.get(2));
//          System.out.println("FILA: " + numFila + ", COLONIA: " + fila.getColonia().toLowerCase() + " , ID ENCONTRADO:  " + ids.get(2));
//        }
//      }
//      catch (Exception e){
//        // VERIFICACION DE ERRORES
//        System.out.println("NO SE VERIFICO EL CODIGO POSTAL " + fila.getCp() + " EN LA FILA " + numFila + " DEL ARCHIVO. COLONIA " + fila.getColonia().toLowerCase());
//      }
//    }
//    else {
//      // VERIFICACION DE ERRORES
//      System.out.println("NO SE VERIFICO DIRECCION EN LA FILA " + numFila + " DEL ARCHIVO. CODIGO POSTAL VACIO");
//      // FALTA VALIDAR POR NOMBRE DE ESTADO, MUNICIPIO Y COLONIA
//    }
  }

  private void guadarQueryEnArchivo(String query, String nombreArchivoSql) {
    // SE CREA UN ARCHIVO "VIRTUAL" PARA COMPROBAR SU EXISTENCIA
    File fichero = new File(nombreArchivoSql);
    // VERIFICAMOS SI EL ARCHIVO YA EXISTE
    if (fichero.exists()) {
      // SE ESCRIBE AL FINAL DEL ARCHIVO
      try {
        // SE CREA UN ARCHIVO DEL TIPO FILEWRITER CON LA OPCION TRUE PARA PODER AGREGAR DATOS AL FINAL DEL ARCHIVO Y NO SOBREESCRIBIRLO
        FileWriter fileWriter = new FileWriter(nombreArchivoSql, true);
        // SE ENVIA EL TEXTO PARA AGREGAR
        fileWriter.append("\n" + query);
        // CIERRE DEL ARCHIVO
        fileWriter.close();
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }

    } else {
      // SE CREA EL ARCHIVO
      try {
        // SE CREA EL ARCHIVO DE TEXTO
        fichero.createNewFile();
        // SE CREAN VARIABLES (ESCRITOR Y BUFFER) PARA PODER ESCRIBIR EN EL NUEVO FICHERO
        FileWriter fileWriter = new FileWriter(fichero);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        PrintWriter printWriter = new PrintWriter(bufferedWriter);
        // SE ESCRIBE EN EL ARCHIVO
        printWriter.write(query);
        // CIERRE DEL ARCHIVO
        printWriter.close();
        bufferedWriter.close();
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }
  }

  public int getNumeroDeFilas() {
    return numeroDeFilas;
  }

  public void setNumeroDeFilas(int numeroDeFilas) {
    this.numeroDeFilas = numeroDeFilas;
  }

  public int getNumeroDeColumnas() {
    return numeroDeColumnas;
  }

  public void setNumeroDeColumnas(int numeroDeColumnas) {
    this.numeroDeColumnas = numeroDeColumnas;
  }

  public Workbook getArhivoExcel() {
    return archivoExcel;
  }

  public void setArhivoExcel(Workbook arhivoExcel) {
    this.archivoExcel = arhivoExcel;
  }

  public FilaBean getFilaBean() {
    return filaBean;
  }

  public void setFilaBean(FilaBean filaBean) {
    this.filaBean = filaBean;
  }

}
