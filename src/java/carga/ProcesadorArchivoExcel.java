package carga;

import dto.Fila;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import jxl.Sheet;
import jxl.Workbook;
import util.constantes.Directorios;
import util.log.Logs;

/**
 *
 * @author antonio
 */
public class ProcesadorArchivoExcel {

  /* Define la hoja principal del archivo, denominada "hoja pivote". Esta hoja es
   la primera hoja del libro Excel y es la que tomará como base para obtener las
   filas. */
  private final int HOJA_PIVOTE = 0;

  /* También se requiere una fila principal conocida como "columna pivote". Esta
   deberá contener el número de crédito, por ser el dato identificador de cada columna.
   Este dato deberá encontrarse en la primera columna de la hoja pivote.*/
  private final int COLUMNA_PIVOTE = 0;

  /* El libro Excel con el cual se estará trabajando. */
  private Workbook archivo;

  /* El número de hojas que contiene este libro Excel. */
  private int numeroDeHojas;

  /* Un arreglo de objetos de Tipo Sheet que guardará cada una de las hojas del
   libro Excel. */
  private Sheet hojas[];
  
  /* El mes al cual corresponde la carga */
  private int mesCarga;

  /* Constructor. Se requiere un objeto de tipo Workbook. */
  public ProcesadorArchivoExcel(Workbook archivo, int mes) {
    if (archivo != null) { // Inicializa las variables de instancia.
      this.archivo = archivo;
      numeroDeHojas = archivo.getNumberOfSheets();
      hojas = archivo.getSheets();
      mesCarga = mes;
    }
  }

  /**
   * Método que obtiene las filas del archivo Excel. No se obtienen todos los
   * datos de las filas, sino únicamente aquellos que serán utilizados para
   * realizar las gestiones. Los datos útiles serán aquellos que se encuentran
   * en las columnas cuyos encabezados se encuentran indicados en un archivo de
   * texto guardado en el servidor.
   *
   * @return Una lista de objetos Fila obtenidos directamente del archivo.
   */
  public List<Fila> obtenerFilas() {
    /* En primer lugar es preciso saber qué columnas van a tomarse en cuenta para
     cada objeto Fila. Para esto se invoca al método obtenerListaDeColumnasUtiles.
     Este método devuelve una lista que hace referencia a esas columnas. */
    List<String> columnasUtiles = obtenerListaDeColumnasUtiles();

    /* Una vez que se tiene la lista de columnas útiles, es necesario obtener una
     lista de claves que faciliten la creación de los objetos Fila */
    List<String> claves = obtenerClaves(columnasUtiles);

    /* Genera los objetos Fila y los almacena en la lista filas */
    List<Fila> filas = generarFilas();

    /* Dado que las filas contenidas en la lista 'filas' están 'incompletas',
     invoca al método completarFilas para agregar los datos restantes. */
    return completarFilas(filas, claves);
  }

  /**
   * Método que obtiene una lista de encabezados de las columnas útiles. Existe
   * un archivo de texto en donde se encuentran listados los encabezados de las
   * columnas que se utilizarán del archivo Excel, este método se encarga de
   * abrir ese archivo de texto y obtener esos encabezados.
   *
   * @return Una lista de los encabezados de las columas útiles.
   */
  private List<String> obtenerListaDeColumnasUtiles() {
    String lineaActual; // La línea leída en un momento determinado.
    List<String> lineas = new ArrayList<>(); // lista de líneas del archivo de texto.

    try (BufferedReader buferLectura = new BufferedReader(
            new FileReader(Directorios.RUTA_COLUMNAS_UTILES))) {
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

  /**
   * Método que obtiene una lista de claves que ayudan a determinar con
   * exactitud las columnas que van a tomarse para formar un determinado objeto
   * Fila. Una clave tiene la forma: COLUMNA_UTIL;HOJA;COLUMNA
   *
   * donde
   *
   * COLUMNA_UTIL es el encabezado de una columna que sí se tomará en cuenta
   * para formar el objeto Fila. HOJA es el nombre de la hoja en la cual se
   * encontró la COLUMNA_UTIL. COLUMNA es el número de columna de la HOJA en la
   * cual se encontró la COLUMNA_UTIL
   *
   * @param columnasUtiles Lista de los encabezados de las columnas útiles.
   * @return La lista de claves.
   */
  private List<String> obtenerClaves(List<String> columnasUtiles) {
    List<String> claves = new ArrayList<>(); // La lista de claves.
    String nombreHoja; // El nombre de la hoja Excel consultada en un momento dado.
    int numeroDeColumnas; // El número de columnas de la hoja nombreHoja.

    /* Recorre el arreglo de objetos Sheet, o las hojas del archivo Excel */
    for (Sheet hoja : hojas) {
      /* Obtiene nombre de hoja y número de columnas */
      nombreHoja = hoja.getName();
      numeroDeColumnas = hoja.getColumns();

      /* Recorre todas las columnas de la hoja nombreHoja */
      for (int i = 0; i < numeroDeColumnas; i++) {

        /* Recorre la lista que contiene los encabezados de las denominadas columnas
         útiles */
        for (int j = 0; j < columnasUtiles.size(); j++) {

          /* Compara el contenido de la columna ubicada en nombreHoja y cuyo número
           de columna es determinado por el contador del for correspondiente. Lo
           compara con la columna útil actual... */
          if (hoja.getCell(i, 0).getContents().equals(columnasUtiles.get(j))) {
            /*... si existe coincidencia, crea la clave con el formato establecido */
            claves.add(columnasUtiles.get(j) + ";" + nombreHoja + ";" + i);
            if (columnasUtiles.get(j).length() == 3) {
              /* Si además de existir coincidencia, el String actual de columnasUtiles
               sólo tiene 3 letras, elimina ese string de la lista */
              columnasUtiles.remove(j);
            }
          }

        } // Termina de recorrer la lista de columnasUtiles

      } // Termina de recorrer las columnas de nombreHoja

    } // Termina de recorrer las hojas del archivo Excel

    return claves;
  }

  /**
   * Método que crea los objetos Fila, creando tantos objetos como filas existan
   * en la hoja HOJA_PIVOTE. Cada objeto Fila que es creado por este método
   * contendrá únicamente su valor 'credito', los demás valores se agregan
   * posteriormente.
   *
   * @return La lista de objetos Fila.
   */
  private List<Fila> generarFilas() {
    /* Toma la hoja HOJA_PIVOTE como referencia para crear los objetos Fila */
    Sheet hoja = hojas[HOJA_PIVOTE];

    /* Variable para generar instancias de Fila */
    Fila fila;

    List<Fila> filas = new ArrayList<>(); /* Inicializa lista de objetos Fila*/

    int numeroDeFilas = hoja.getRows(); /* Obtiene número de filas */

    /* Recorre todas las filas de la hoja para crear los objetos Fila */
    for (int i = 1; i < numeroDeFilas; i++) {
      fila = new Fila(); /* Crea un objeto Fila en cada iteración */

      /* Agrega el crédito al objeto Fila recién creado, tomando como referencia
       la columna COLUMNA_PIVOTE y la fila dada por el bucle for */
      fila.setCredito(hoja.getCell(COLUMNA_PIVOTE, i).getContents());
      filas.add(fila); /* Agrega esta fila a la lista */

    }

    return filas;
  }

  /**
   * Método que agrega los datos faltantes a cada objeto Fila.
   *
   * @param filas La lista de objetos Fila creada anteriormente
   * @param claves La lista de claves que facilita la lectura del archivo Excel
   * @return La lista de objetos Fila completamente creados.
   */
  private List<Fila> completarFilas(List<Fila> filas, List<String> claves) {
    /* Guarda por separado cada uno de los elementos que conforman la clave */
    String[] splitClave;

    /* Guarda el dato que fungirá como identificador de cada objeto Fila. Los
     datos pivote se encuentran en las columnas COLUMNA_PIVOTE de cada fila que
     pertenece a la hoja HOJA_PIVOTE. El número de crédito es el dato pivote. */
    String datoPivote;

    /* Indica el número de fila en un momento dado. La fila 0 siempre deberá
     corresponder a los encabezados */
    int numeroFila = 0;

    /* Recorre la lista de objetos Fila y procesa cada objeto Fila */
    for (Fila fila : filas) {
      datoPivote = fila.getCredito(); /* Comienza por obtener el dato pivote */

      /* Recorre el arreglo de hojas del archivo Excel */
      for (Sheet hoja : hojas) {

        /* Compara el dato pivote con cada uno de los datos contenidos en la
         columna COLUMNA_PIVOTE de la hoja actual hasta que encuentra una coincidencia,
         en ese momento toma el número de la fila en la que encontró esa coincidencia
         y termina de comparar. Si no encuentra ninguna coincidencia, entonces
         indica esta situación tomando 0 como el numeroFila */
        for (int i = 1; i < hoja.getRows(); i++) {
          if (datoPivote.equals(hoja.getCell(COLUMNA_PIVOTE, i).getContents())) {
            numeroFila = i;
            break;
          }
          numeroFila = 0;
        }

        /* En caso de haber encontrado coincidencias anteriormente, toma los datos
         necesarios para completar el objeto Fila cuyo dato pivote se encuentra
         en la fila numeroFila. */
        if (numeroFila != 0) {

          /* Recorre la lista de claves y en cada iteración separa la clave en
           sus elementos que la conforman */
          for (String clave : claves) {

            /* splitcClave contiene los elementos de la clave: COLUMNA_UTIL, 
             HOJA y COLUMNA. Donde:
             splitClave[0] = COLUMNA_UTIL
             splitClave[1] = HOJA
             splitClave[2] = COLUMNA */
            splitClave = clave.split(";");

            /* Verifica si la clave actual hace referencia a la hoja actual. De 
             ser así, entonces termina de completar el objeto Fila. */
            if (hoja.getName().equals(splitClave[1])) {

              /* Ahora compara el encabezado que se indica en la clave con los
               encabezados existentes a fin de determinar el lugar en donde se
               almacenará la información del objeto Fila. */
              switch (splitClave[0]) {
                case "CRE": // Número de crédito, se guarda en credito
                  fila.setCredito(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "NOM": // Nombre o razón social, se guarda en nombre
                  fila.setNombre(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "REC": // Referencia de cobre, se guarda en refCobro
                  fila.setRefCobro(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "LIN": // Línea de crédito, se guarda en linea
                  fila.setLinea(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "TIC": // Tipo de crédito, se guarda en tipoCredito
                  fila.setTipoCredito(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "EST": // Estatus del crédito, se guarda en estatus
                  fila.setEstatus(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MEV": // Número de meses vencidos, se guarda en mesesVencidos
                  fila.setMesesVencidos(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "DES": // Nombre del despacho, se guarda en despacho
                  fila.setDespacho(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "FIC": // Fecha de inicio del crédito, se guarda en fechaInicioCredito
                  fila.setFechaInicioCredito(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "FVC": // Fecha de vencimiento del crédito, se guarda en fechaVencimientoCredito
                  fila.setFechaVencimientoCred(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "DIS": // Disposición o monto del crédito, se guarda en disposicion
                  fila.setDisposicion(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MEN": // Mensualidad del crédito, se guarda en mensualidad
                  fila.setMensualidad(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "SAI": // Saldo insoluto, se guarda en saldoInsoluto
                  fila.setSaldoInsoluto(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "SAV": // Salvo vencido, se guarda en saldoVencido
                  fila.setSaldoVencido(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "TAS": // Tasa de interés, se guarda en tasa
                  fila.setTasa(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "CUE": // Número de cuenta, se guarda en cuenta
                  fila.setCuenta(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "FUP": // Fecha de último pago, se guarda en fechaUltimoPago
                  fila.setFechaUltimoPago(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "FUV": // Fecha de último vencimiento pagado, se guarda en fechaUltimoVencimientoPagado
                  fila.setFechaUltimoVencimientoPagado(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "IDC": // Número o ID del deudor, se guarda en idCliente
                  fila.setIdCliente(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "RFC": // RFC del deudor, se guarda en rfc
                  fila.setRfc(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "CLL": // Calle del domicilio del deudor, se guarda en calle
                  fila.setCalle(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "EXT": // Número exterior del domicilio del deudor, se guarda en numeroExterior
                  fila.setNumeroExterior(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "INT": // Número interior del domicilio del deudor, se guarda en numeroInterior
                  fila.setNumeroInterior(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "COL": // Colonia en que se ubica domicilio del deudor, se guarda en colonia
                  fila.setColonia(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "EDO": // Estado de la república en donde se ubica domicilio del deudor, se guarda en estado
                  fila.setEstado(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MUN": // Municipio en donde se ubica domicilio del deudor, se guarda en municipio
                  fila.setMunicipio(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "COP": // Código postal del domicilio del deudor, se guarda en cp
                  fila.setCp(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MAR": // Marcaje del crédito, se guarda en marcaje
                  fila.setMarcaje(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "FEQ": // Fecha de quebranto, se guarda en fechaQuebranto
                  fila.setFechaQuebranto(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "REFS": // Referencias adicionales, se guarda en refsAdicionales
                  fila.setReferenciaAdicional(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "CORS": // Correos electrónicos del deudor, se guarda en correos
                  fila.setCorreoAdicional(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "TELS": // Teléfonos adicionales, se guarda en telsAdicionales
                  fila.setTelefonoAdicional(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "DIRS": // Direcciones adicionales, se guarda en direcsAdicionales
                  fila.setDireccionAdicional(hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "ENE-": // Crea objeto Fac correspondiente al mes de enero
                  crearFac(fila, 1, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "FEB-": // Crea objeto Fac correspondiente al mes de febrero
                  crearFac(fila, 2, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MAR-": // Crea objeto Fac correspondiente al mes de marzo
                  crearFac(fila, 3, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "ABR-": // Crea objeto Fac correspondiente al mes de abril
                  crearFac(fila, 4, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MAY-": // Crea objeto Fac correspondiente al mes de mayo
                  crearFac(fila, 5, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "JUN-": // Crea objeto Fac correspondiente al mes de junio
                  crearFac(fila, 6, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "JUL-": // Crea objeto Fac correspondiente al mes de julio
                  crearFac(fila, 7, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "AGO-": // Crea objeto Fac correspondiente al mes de agosto
                  crearFac(fila, 8, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "SEP-": // Crea objeto Fac correspondiente al mes de septiembre
                  crearFac(fila, 9, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "OCT-": // Crea objeto Fac correspondiente al mes de octubre
                  crearFac(fila, 10, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "NOV-": // Crea objeto Fac correspondiente al mes de noviembre
                  crearFac(fila, 11, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "DIC-": // Crea objeto Fac correspondiente al mes de dicembre
                  crearFac(fila, 12, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MO-ENE": // Agrega el monto correspondiente al Fac de enero
                  agregarFacPor(1, fila, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MO-FEB": // Agrega el monto correspondiente al Fac de febrero
                  agregarFacPor(2, fila, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MO-MAR": // Agrega el monto correspondiente al Fac de marzo
                  agregarFacPor(3, fila, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MO-ABR": // Agrega el monto correspondiente al Fac de abril
                  agregarFacPor(4, fila, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MO-MAY": // Agrega el monto correspondiente al Fac de mayo
                  agregarFacPor(5, fila, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MO-JUN": // Agrega el monto correspondiente al Fac de junio
                  agregarFacPor(6, fila, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MO-JUL": // Agrega el monto correspondiente al Fac de julio
                  agregarFacPor(7, fila, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MO-AGO": // Agrega el monto correspondiente al Fac de agosto
                  agregarFacPor(8, fila, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MO-SEP": // Agrega el monto correspondiente al Fac de septiembre
                  agregarFacPor(9, fila, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MO-OCT": // Agrega el monto correspondiente al Fac de octubre
                  agregarFacPor(10, fila, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MO-NOV": // Agrega el monto correspondiente al Fac de noviembre
                  agregarFacPor(11, fila, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
                case "MO-DIC": // Agrega el monto correspondiente al Fac de diciembre
                  agregarFacPor(12, fila, hoja.getCell(Integer.parseInt(splitClave[2]), numeroFila).getContents());
                  break;
              } // fin de switch
            } // fin de if(coincide el nombre de la hoja)
          } // fin de for(claves)
        } // fin de if(la fila no es 0)
      } // fin de for(hojas)
    } // fin de for(filas)

    return filas;
  }

  /**
   * Método que crea un objeto Fac. En cada remesa, los créditos van acompañados
   * de 0 o más objetos Fac, mismos se crean y guardan en una lista contenida en
   * cada objeto Fila
   *
   * @param fila El objeto Fila asociado a este objeto Fac
   * @param mes El número de mes correspondiente a este objeto Fac
   * @param facMes La cadena de testo que corresponde al dato "fac" del archivo
   * Excel
   */
  private void crearFac(Fila fila, int mes, String facMes) {
    /* Crea objeto Fac y agrega los datos respectivos a sus variables de instancia */
    Fac fac = new Fac();
    fac.setMes(mes);
    fac.setAnio(agregarFacAnio(mesCarga, mes)); // Agrega año
    fac.setFacMes(facMes);

    /* Finalmente asocia este objeto Fac con su objeto Fila correspondiente */
    fila.setFacAdicional(fac);
  }

  /**
   * Método que determina el año correspondiente para ser agregado a un objeto
   * Fac. De acuerdo al mes actual y al mes indicado en la remesa, determina el
   * año correspondiente
   *
   * @param mesActual El mes correspondiente a la remesa que se carga
   * @param mesDado El mes que se encontró en el archivo Excel
   * @return el año correspondiente
   */
  private int agregarFacAnio(int mesActual, int mesDado) {
    /* Obtiene el año actual del sistema */
    int anio = Calendar.getInstance().get(Calendar.YEAR);

    /* compara el mes actual con el mes dado. Si el mes actual es mayor o igual
     que el mes dado, el año será el mismo que anio. En otro caso se trata del año
     anterior, por lo que se resta 1 a anio */
    if (mesDado <= mesActual) {
      return anio;
    } else {
      return anio - 1;
    }
  }

  /**
   * Método que agrega el monto (facPor) a un objeto Fac determinado.
   *
   * @param mes El número correspondiente al mes del objeto Fac asociado
   * @param fila El objeto Fila al cual corresponde el objeto Fac asociado
   * @param facPor La cadena de testo que representa el monto a guardar para el
   * Fac asociado
   */
  private void agregarFacPor(int mes, Fila fila, String facPor) {
    /* Busca el Fac asociado en el objeto Fila. La búsqueda la hace por mes dado
    que en una remesa no debe aparecer un mes más de una vez */
    Fac fac = fila.buscarFac(mes);
    
    /* Agrega el monto, si encontró el Fac buscado */
    if (fac != null) {
      fac.setFacPor(facPor);
    }
  }
  
  /* Setters y Getters */

  public Workbook getArchivo() {
    return archivo;
  }

  public int getNumeroDeHojas() {
    return numeroDeHojas;
  }

  public Sheet[] getHojas() {
    return hojas;
  }

  public int getMesCarga() {
    return mesCarga;
  }

  public void setMesCarga(int mesCarga) {
    this.mesCarga = mesCarga;
  }
}
