/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dto.carga.TipoRemesa;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import util.carga.NuevoLectorArchivoCreditosExcel;
import util.constantes.DatosPrecarga;
import util.constantes.Directorios;
import util.constantes.TipoRemesas;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "precargaBean")
@SessionScoped
public class PrecargaBean implements Serializable {

  // VARIABLES DE CLASE
  private boolean precargaValida;
  private int idTipoSeleccionado;
  private List<TipoRemesa> listaTipos;
  private ColumnaUtil[] columnas;
  private ColumnaUtil[] columnasFac;
  private ColumnaUtil[] columnasAjustes;
  private UploadedFile archivo;
  private static String ruta;

  // CONSTRUCTOR
  public PrecargaBean() {
    precargaValida = false;
    listaTipos = new ArrayList();
    obtenerListas();
  }

  // METODO QUE CARGA LA LISTA DE TIPOS DE REMESA
  public final void obtenerListas() {
    // LISTA DE TIPOS DE CARGA
    List<String> tipos = Arrays.asList("Asignacion mensual", "Asignacion complementaria", "Asignacion quebranto");
    List<Integer> ids = Arrays.asList(TipoRemesas.ASIGNACION_MENSUAL, TipoRemesas.ASIGNACION_COMPLEMENTARIA, TipoRemesas.ASIGNACION_QUEBRANTO);
    for (int i = 0; i < (tipos.size()); i++) {
      TipoRemesa t = new TipoRemesa();
      t.setId(ids.get(i));
      t.setTipo(tipos.get(i));
      listaTipos.add(t);
    }
    // LISTA DE COLUMNAS UTILES
    List<ColumnaUtil> columnasUtiles = new ArrayList();
    ColumnaUtil c = new ColumnaUtil();
    c.setActivo(true);
    c.setAbreviatura("CRE");
    c.setNombre("Numero de credito");
    c.setColumna("A");
    c.setHoja(1);
    c.setLongitud(10);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnasUtiles.add(c);
    c = new ColumnaUtil();
    c.setActivo(true);
    c.setAbreviatura("NOM");
    c.setNombre("Nombre del deudor");
    c.setColumna("B");
    c.setHoja(1);
    c.setLongitud(150);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnasUtiles.add(c);
    c = new ColumnaUtil();
    c.setActivo(true);
    c.setAbreviatura("PROD");
    c.setNombre("Producto");
    c.setColumna("E");
    c.setHoja(1);
    c.setLongitud(50);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnasUtiles.add(c);
    c = new ColumnaUtil();
    c.setActivo(true);
    c.setAbreviatura("SUB");
    c.setNombre("Subproducto");
    c.setColumna("D");
    c.setHoja(1);
    c.setLongitud(100);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnasUtiles.add(c);
    c = new ColumnaUtil();
    c.setActivo(true);
    c.setAbreviatura("EST");
    c.setNombre("Estatus del credito");
    c.setColumna("G");
    c.setHoja(1);
    c.setLongitud(2);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnasUtiles.add(c);
    c = new ColumnaUtil();
    c.setActivo(true);
    c.setAbreviatura("MEV");
    c.setNombre("Meses vencidos");
    c.setColumna("H");
    c.setHoja(1);
    c.setLongitud(2);
    c.setTipo(DatosPrecarga.TIPO_DATO_NUMERICO);
    columnasUtiles.add(c);
    c = new ColumnaUtil();
    c.setAbreviatura("FIC");
    c.setActivo(true);
    c.setNombre("Fecha inicio del credito");
    c.setColumna("I");
    c.setHoja(1);
    c.setLongitud(10);
    c.setTipo(DatosPrecarga.TIPO_DATO_FECHA);
    columnasUtiles.add(c);
    c = new ColumnaUtil();
    c.setActivo(true);
    c.setAbreviatura("FVC");
    c.setNombre("Fecha vencimiento del credito");
    c.setColumna("J");
    c.setHoja(1);
    c.setLongitud(10);
    c.setTipo(DatosPrecarga.TIPO_DATO_FECHA);
    columnasUtiles.add(c);
    c = new ColumnaUtil();
    c.setActivo(false);
    c.setAbreviatura("FQB");
    c.setNombre("Fecha quebranto");
    c.setColumna("");
    c.setHoja(1);
    c.setLongitud(10);
    c.setTipo(DatosPrecarga.TIPO_DATO_FECHA);
    columnasUtiles.add(c);
    c = new ColumnaUtil();
    c.setActivo(true);
    c.setAbreviatura("DIS");
    c.setNombre("Monto del credito");
    c.setColumna("K");
    c.setHoja(1);
    c.setLongitud(9);
    c.setTipo(DatosPrecarga.TIPO_DATO_NUMERICO);
    columnasUtiles.add(c);
    c = new ColumnaUtil();
    c.setActivo(true);
    c.setAbreviatura("MEN");
    c.setNombre("Mensualidad");
    c.setColumna("L");
    c.setHoja(1);
    c.setLongitud(9);
    c.setTipo(DatosPrecarga.TIPO_DATO_NUMERICO);
    columnasUtiles.add(c);
    c = new ColumnaUtil();
    c.setActivo(true);
    c.setAbreviatura("SAI");
    c.setNombre("Saldo insoluto");
    c.setColumna("M");
    c.setHoja(1);
    c.setLongitud(9);
    c.setTipo(DatosPrecarga.TIPO_DATO_NUMERICO);
    columnasUtiles.add(c);
    c = new ColumnaUtil();
    c.setActivo(true);
    c.setAbreviatura("SAV");
    c.setNombre("Saldo vencido");
    c.setColumna("N");
    c.setHoja(1);
    c.setLongitud(9);
    c.setTipo(DatosPrecarga.TIPO_DATO_NUMERICO);
    columnasUtiles.add(c);
    c = new ColumnaUtil();
    c.setActivo(true);
    c.setAbreviatura("CUE");
    c.setNombre("Numero de cuenta");
    c.setColumna("O");
    c.setHoja(1);
    c.setLongitud(10);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    c = new ColumnaUtil();
    c.setActivo(true);
    c.setAbreviatura("IDC");
    c.setNombre("Numero de cliente");
    c.setColumna("P");
    c.setHoja(1);
    c.setLongitud(10);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnasUtiles.add(c);
    c = new ColumnaUtil();
    c.setActivo(true);
    c.setAbreviatura("FUP");
    c.setNombre("Fecha ultimo pago");
    c.setColumna("Q");
    c.setHoja(1);
    c.setLongitud(10);
    c.setTipo(DatosPrecarga.TIPO_DATO_FECHA);
    columnasUtiles.add(c);
    c = new ColumnaUtil();
    c.setActivo(true);
    c.setAbreviatura("FUV");
    c.setNombre("Fecha ultimo vencimiento pagado");
    c.setColumna("R");
    c.setHoja(1);
    c.setLongitud(10);
    c.setTipo(DatosPrecarga.TIPO_DATO_FECHA);
    columnasUtiles.add(c);
    c = new ColumnaUtil();
    c.setActivo(true);
    c.setAbreviatura("RFC");
    c.setNombre("RFC");
    c.setColumna("S");
    c.setHoja(1);
    c.setLongitud(10);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnasUtiles.add(c);
    c = new ColumnaUtil();
    c.setActivo(true);
    c.setAbreviatura("CRE");
    c.setNombre("Numero de credito");
    c.setColumna("A");
    c.setHoja(2);
    c.setLongitud(10);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnasUtiles.add(c);
    c = new ColumnaUtil();
    c.setActivo(true);
    c.setAbreviatura("CLL");
    c.setNombre("Calle");
    c.setColumna("F");
    c.setHoja(2);
    c.setLongitud(100);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnasUtiles.add(c);
    c = new ColumnaUtil();
    c.setActivo(true);
    c.setAbreviatura("EXT");
    c.setNombre("Numero exterior");
    c.setColumna("G");
    c.setHoja(2);
    c.setLongitud(10);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnasUtiles.add(c);
    c = new ColumnaUtil();
    c.setActivo(true);
    c.setAbreviatura("INT");
    c.setNombre("Numero interior");
    c.setColumna("H");
    c.setHoja(2);
    c.setLongitud(10);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnasUtiles.add(c);
    c = new ColumnaUtil();
    c.setActivo(true);
    c.setAbreviatura("COL");
    c.setNombre("Colonia");
    c.setColumna("I");
    c.setHoja(2);
    c.setLongitud(100);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnasUtiles.add(c);
    c = new ColumnaUtil();
    c.setActivo(true);
    c.setAbreviatura("COP");
    c.setNombre("Codigo postal");
    c.setColumna("J");
    c.setHoja(2);
    c.setLongitud(5);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnasUtiles.add(c);
    c = new ColumnaUtil();
    c.setActivo(true);
    c.setAbreviatura("MUN");
    c.setNombre("Municipio");
    c.setColumna("K");
    c.setHoja(2);
    c.setLongitud(100);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnasUtiles.add(c);
    c = new ColumnaUtil();
    c.setActivo(true);
    c.setAbreviatura("EDO");
    c.setNombre("Estado");
    c.setColumna("M");
    c.setHoja(2);
    c.setLongitud(50);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnasUtiles.add(c);
    c = new ColumnaUtil();
    c.setActivo(true);
    c.setAbreviatura("TEL");
    c.setNombre("Telefono");
    c.setColumna("N");
    c.setHoja(2);
    c.setLongitud(10);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnasUtiles.add(c);
    c = new ColumnaUtil();
    c.setActivo(true);
    c.setAbreviatura("COR");
    c.setNombre("Correo");
    c.setColumna("O");
    c.setHoja(2);
    c.setLongitud(100);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnasUtiles.add(c);
    c = new ColumnaUtil();
    c.setActivo(true);
    c.setAbreviatura("CRE");
    c.setNombre("Numero de credito");
    c.setColumna("A");
    c.setHoja(3);
    c.setLongitud(10);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnasUtiles.add(c);
    c = new ColumnaUtil();
    c.setActivo(true);
    c.setAbreviatura("REF");
    c.setNombre("Referencia");
    c.setColumna("E");
    c.setHoja(3);
    c.setLongitud(100);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnasUtiles.add(c);
    c = new ColumnaUtil();
    c.setActivo(true);
    c.setAbreviatura("TIP");
    c.setNombre("Tipo");
    c.setColumna("F");
    c.setHoja(3);
    c.setLongitud(30);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnasUtiles.add(c);
    columnas = new ColumnaUtil[columnasUtiles.size()];
    columnas = columnasUtiles.toArray(columnas);
    // LISTA DE COLUMNAS FAC
    List<ColumnaUtil> columnaFacs = new ArrayList();
    c = new ColumnaUtil();
    c.setActivo(false);
    c.setAbreviatura("FAC-ENE");
    c.setNombre("Respuesta Telmex Enero");
    c.setColumna("");
    c.setHoja(1);
    c.setLongitud(50);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnaFacs.add(c);
    c = new ColumnaUtil();
    c.setActivo(false);
    c.setAbreviatura("MO-ENE");
    c.setNombre("Monto respuesta Telmex Enero");
    c.setColumna("");
    c.setHoja(1);
    c.setLongitud(7);
    c.setTipo(DatosPrecarga.TIPO_DATO_NUMERICO);
    columnaFacs.add(c);
    c = new ColumnaUtil();
    c.setActivo(false);
    c.setAbreviatura("FAC-FEB");
    c.setNombre("Respuesta Telmex Febrero");
    c.setColumna("");
    c.setHoja(1);
    c.setLongitud(50);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnaFacs.add(c);
    c = new ColumnaUtil();
    c.setActivo(false);
    c.setAbreviatura("MO-FEB");
    c.setNombre("Monto respuesta Telmex Febrero");
    c.setColumna("");
    c.setHoja(1);
    c.setLongitud(7);
    c.setTipo(DatosPrecarga.TIPO_DATO_NUMERICO);
    columnaFacs.add(c);
    c = new ColumnaUtil();
    c.setActivo(false);
    c.setAbreviatura("FAC-MAR");
    c.setNombre("Respuesta Telmex Marzo");
    c.setColumna("");
    c.setHoja(1);
    c.setLongitud(50);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnaFacs.add(c);
    c = new ColumnaUtil();
    c.setActivo(false);
    c.setAbreviatura("MO-MAR");
    c.setNombre("Monto respuesta Telmex Marzo");
    c.setColumna("");
    c.setHoja(1);
    c.setLongitud(7);
    c.setTipo(DatosPrecarga.TIPO_DATO_NUMERICO);
    columnaFacs.add(c);
    c = new ColumnaUtil();
    c.setActivo(false);
    c.setAbreviatura("FAC-ABR");
    c.setNombre("Respuesta Telmex Abril");
    c.setColumna("");
    c.setHoja(1);
    c.setLongitud(50);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnaFacs.add(c);
    c = new ColumnaUtil();
    c.setActivo(false);
    c.setAbreviatura("MO-ABR");
    c.setNombre("Monto respuesta Telmex Abril");
    c.setColumna("");
    c.setHoja(1);
    c.setLongitud(7);
    c.setTipo(DatosPrecarga.TIPO_DATO_NUMERICO);
    columnaFacs.add(c);
    c = new ColumnaUtil();
    c.setActivo(false);
    c.setAbreviatura("FAC-MAY");
    c.setNombre("Respuesta Telmex Mayo");
    c.setColumna("");
    c.setHoja(1);
    c.setLongitud(50);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnaFacs.add(c);
    c = new ColumnaUtil();
    c.setActivo(false);
    c.setAbreviatura("MO-MAY");
    c.setNombre("Monto respuesta Telmex Mayo");
    c.setColumna("");
    c.setHoja(1);
    c.setLongitud(7);
    c.setTipo(DatosPrecarga.TIPO_DATO_NUMERICO);
    columnaFacs.add(c);
    c = new ColumnaUtil();
    c.setActivo(false);
    c.setAbreviatura("FAC-JUN");
    c.setNombre("Respuesta Telmex Junio");
    c.setColumna("");
    c.setHoja(1);
    c.setLongitud(50);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnaFacs.add(c);
    c = new ColumnaUtil();
    c.setActivo(false);
    c.setAbreviatura("MO-JUN");
    c.setNombre("Monto respuesta Telmex Junio");
    c.setColumna("");
    c.setHoja(1);
    c.setLongitud(7);
    c.setTipo(DatosPrecarga.TIPO_DATO_NUMERICO);
    columnaFacs.add(c);
    c = new ColumnaUtil();
    c.setActivo(false);
    c.setAbreviatura("FAC-JUL");
    c.setNombre("Respuesta Telmex Julio");
    c.setColumna("");
    c.setHoja(1);
    c.setLongitud(50);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnaFacs.add(c);
    c = new ColumnaUtil();
    c.setActivo(false);
    c.setAbreviatura("MO-JUL");
    c.setNombre("Monto respuesta Telmex Julio");
    c.setColumna("");
    c.setHoja(1);
    c.setLongitud(7);
    c.setTipo(DatosPrecarga.TIPO_DATO_NUMERICO);
    columnaFacs.add(c);
    c = new ColumnaUtil();
    c.setActivo(false);
    c.setAbreviatura("FAC-AGO");
    c.setNombre("Respuesta Telmex Agosto");
    c.setColumna("");
    c.setHoja(1);
    c.setLongitud(50);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnaFacs.add(c);
    c = new ColumnaUtil();
    c.setActivo(false);
    c.setAbreviatura("MO-AGO");
    c.setNombre("Monto respuesta Telmex Agosto");
    c.setColumna("");
    c.setHoja(1);
    c.setLongitud(7);
    c.setTipo(DatosPrecarga.TIPO_DATO_NUMERICO);
    columnaFacs.add(c);
    c = new ColumnaUtil();
    c.setActivo(false);
    c.setAbreviatura("FAC-SEP");
    c.setNombre("Respuesta Telmex Septiembre");
    c.setColumna("");
    c.setHoja(1);
    c.setLongitud(50);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnaFacs.add(c);
    c = new ColumnaUtil();
    c.setActivo(false);
    c.setAbreviatura("MO-SEP");
    c.setNombre("Monto respuesta Telmex Septiembre");
    c.setColumna("");
    c.setHoja(1);
    c.setLongitud(7);
    c.setTipo(DatosPrecarga.TIPO_DATO_NUMERICO);
    columnaFacs.add(c);
    c = new ColumnaUtil();
    c.setActivo(false);
    c.setAbreviatura("FAC-OCT");
    c.setNombre("Respuesta Telmex Octubre");
    c.setColumna("");
    c.setHoja(1);
    c.setLongitud(50);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnaFacs.add(c);
    c = new ColumnaUtil();
    c.setActivo(false);
    c.setAbreviatura("MO-OCT");
    c.setNombre("Monto respuesta Telmex Octubre");
    c.setColumna("");
    c.setHoja(1);
    c.setLongitud(7);
    c.setTipo(DatosPrecarga.TIPO_DATO_NUMERICO);
    columnaFacs.add(c);
    c = new ColumnaUtil();
    c.setActivo(false);
    c.setAbreviatura("FAC-NOV");
    c.setNombre("Respuesta Telmex Noviembre");
    c.setColumna("");
    c.setHoja(1);
    c.setLongitud(50);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnaFacs.add(c);
    c = new ColumnaUtil();
    c.setActivo(false);
    c.setAbreviatura("MO-NOV");
    c.setNombre("Monto respuesta Telmex Noviembre");
    c.setColumna("");
    c.setHoja(1);
    c.setLongitud(7);
    c.setTipo(DatosPrecarga.TIPO_DATO_NUMERICO);
    columnaFacs.add(c);
    c = new ColumnaUtil();
    c.setActivo(false);
    c.setAbreviatura("FAC-DIC");
    c.setNombre("Respuesta Telmex Diciembre");
    c.setColumna("");
    c.setHoja(1);
    c.setLongitud(50);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnaFacs.add(c);
    c = new ColumnaUtil();
    c.setActivo(false);
    c.setAbreviatura("MO-DIC");
    c.setNombre("Monto respuesta Telmex Diciembre");
    c.setColumna("");
    c.setHoja(1);
    c.setLongitud(7);
    c.setTipo(DatosPrecarga.TIPO_DATO_NUMERICO);
    columnaFacs.add(c);
    columnasFac = new ColumnaUtil[columnaFacs.size()];
    columnasFac = columnaFacs.toArray(columnasFac);
    // LISTA DE COLUMNAS AJUSTES
    List<ColumnaUtil> columnaAjuste = new ArrayList();
    c = new ColumnaUtil();
    c.setActivo(false);
    c.setAbreviatura("AJAN");
    c.setNombre("Ajustes anteriores");
    c.setColumna("AG");
    c.setHoja(1);
    c.setLongitud(50);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnaAjuste.add(c);
    c = new ColumnaUtil();
    c.setActivo(false);
    c.setAbreviatura("AJAC");
    c.setNombre("Ajustes actuales");
    c.setColumna("AH");
    c.setHoja(1);
    c.setLongitud(50);
    c.setTipo(DatosPrecarga.TIPO_DATO_TEXTO);
    columnaAjuste.add(c);
    columnasAjustes = new ColumnaUtil[columnaAjuste.size()];
    columnasAjustes = columnaAjuste.toArray(columnasAjustes);
  }

  // METODO AUXILIAR QUE OBTIENE EL ARCHIVO
  public void eventoDeCarga(FileUploadEvent evento) {
    archivo = evento.getFile();
    cargarArchivoAlServidor(archivo);
  }

  // METODO QUE CARGA EL ARCHIVO AL SERVIDOR
  public void cargarArchivoAlServidor(UploadedFile archivo) {
    byte[] bytes;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    String extension = archivo.getFileName().substring(archivo.getFileName().indexOf(".")).toLowerCase();
    ruta = "Precarga" + "_" + df.format(new Date()) + extension;
    ruta = ruta.replace(" ", "-");
    ruta = Directorios.RUTA_WINDOWS_CARGA_REMESA + ruta;
    bytes = archivo.getContents();
    BufferedOutputStream stream;
    try {
      stream = new BufferedOutputStream(new FileOutputStream(new File(ruta)));
      stream.write(bytes);
      Logs.log.info("Se carga archivo de remesa al servidor: " + ruta);
      abrirSeleccionColumnas();
    } catch (FileNotFoundException fnfe) {
      Logs.log.error("No se cargo el archivo al servidor.");
      Logs.log.error(ruta);
      Logs.log.error(fnfe.getMessage());
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se cargo el archivo. Contacte al equipo de sistemas."));
    } catch (IOException ioe) {
      Logs.log.error("No se cargo el archivo al servidor.");
      Logs.log.error(ruta);
      Logs.log.error(ioe.getMessage());
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se cargo el archivo. Contacte al equipo de sistemas."));
    }
  }

  // METODO QUE ABRE LA VISTA DONDE SE ESCOGEN LAS COLUMNAS
  public void abrirSeleccionColumnas() {
    try {
      FacesContext.getCurrentInstance().getExternalContext().redirect("precargaColumnas.xhtml");
    } catch (IOException ioe) {
      Logs.log.error("No se pudo redirigir a la eleccion de columnas de precarga.");
      Logs.log.error(ioe);
    }
  }

  // METODO QUE ABRE LA VISTA DONDE SE PODRAN SELECCIONAR LAS RESPUESTAS DE TELMEX
  // TO OPTIMIZE:
  public void procesarColumnas() {
    List<ColumnaUtil> l1 = new ArrayList();
    for (int i = 0; i < (columnas.length); i++) {
      if (columnas[i].isActivo()) {
        l1.add(columnas[i]);
      }
    }
    for (int i = 0; i < (columnasFac.length); i++) {
      if (columnasFac[i].isActivo()) {
        l1.add(columnasFac[i]);
      }
    }
    for (int i = 0; i < (columnasAjustes.length); i++) {
      if (columnasAjustes[i].isActivo()) {
        l1.add(columnasAjustes[i]);
      }
    }
    NuevoLectorArchivoCreditosExcel lector = new NuevoLectorArchivoCreditosExcel();
    lector.leerArchivoExcel(l1.toArray(new ColumnaUtil[l1.size()]), ruta);
    // LLAMADA AL SELECTOR DE PRECARGA
    RequestContext.getCurrentInstance().execute("PF('dialogoSelectorPrecarga').show();");
  }

  // METODO QUE SELECCIONA EL TIPO DE ARCHIVO DE PRECARGA QUE SE OBTENDRA
  public void selectorPrecarga() {
    RequestContext.getCurrentInstance().execute("PF('dialogoSelectorPrecarga').hide();");
    switch (idTipoSeleccionado) {
      case TipoRemesas.ASIGNACION_MENSUAL:
        precargaMensual();
        break;
      case TipoRemesas.ASIGNACION_COMPLEMENTARIA:
        precargaComplementaria();
        break;
      case TipoRemesas.ASIGNACION_QUEBRANTO:
        precargaQuebranto();
        break;
    }
    try {
      FacesContext.getCurrentInstance().getExternalContext().redirect("resultadoPrecarga.xhtml");
    } catch (IOException ioe) {
      Logs.log.error("No se pudo redirigir a la vista resultados de precarga.");
      Logs.log.error(ioe);
    }
  }

  // METODO QUE HACE LA PRECARGA DE UNA ASIGNACION MENSUAL
  public void precargaMensual() {
    Logs.log.info("Se realizara un archivo de precarga normal");
  }

  // METODO QUE HACE LA PRECARGA DE UNA ASIGNACION COMPLEMENTARIA
  public void precargaComplementaria() {
    Logs.log.info("Se realizara un archivo de precarga complementaria");
  }

  // METODO QUE HACE LA PRECARGA DE UNA ASIGNACION DE QUEBRANTO
  public void precargaQuebranto() {
    Logs.log.info("Se realizara un archivo de precarga quebranto");
  }

  // GETTERS & SETTERS
  public UploadedFile getArchivo() {
    return archivo;
  }

  public void setArchivo(UploadedFile archivo) {
    this.archivo = archivo;
  }

  public int getIdTipoSeleccionado() {
    return idTipoSeleccionado;
  }

  public void setIdTipoSeleccionado(int idTipoSeleccionado) {
    this.idTipoSeleccionado = idTipoSeleccionado;
  }

  public List<TipoRemesa> getListaTipos() {
    return listaTipos;
  }

  public void setListaTipos(List<TipoRemesa> listaTipos) {
    this.listaTipos = listaTipos;
  }

  public ColumnaUtil[] getColumnas() {
    return columnas;
  }

  public void setColumnas(ColumnaUtil[] columnas) {
    this.columnas = columnas;
  }

  public ColumnaUtil[] getColumnasFac() {
    return columnasFac;
  }

  public void setColumnasFac(ColumnaUtil[] columnasFac) {
    this.columnasFac = columnasFac;
  }

  public boolean isPrecargaValida() {
    return precargaValida;
  }

  public void setPrecargaValida(boolean precargaValida) {
    this.precargaValida = precargaValida;
  }

  public ColumnaUtil[] getColumnasAjustes() {
    return columnasAjustes;
  }

  public void setColumnasAjustes(ColumnaUtil[] columnasAjustes) {
    this.columnasAjustes = columnasAjustes;
  }

  // CLASE MIEMBRO QUE GUARDARA LA POSICION DE LAS COLUMNAS UTILES Y OTROS DATOS DE UTILIDAD
  public static class ColumnaUtil {

    // VARIABLES DE CLASE
    private boolean activo;
    private String nombre;
    private int hoja;
    private String columna;
    private String abreviatura;
    private int longitud;
    private int tipo;

    // CONSTRUCTOR
    public ColumnaUtil() {
    }

    // GETTERS & SETTERS
    public int getHoja() {
      return hoja;
    }

    public void setHoja(int hoja) {
      this.hoja = hoja;
    }

    public String getColumna() {
      return columna;
    }

    public void setColumna(String columna) {
      this.columna = columna;
    }

    public String getAbreviatura() {
      return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
      this.abreviatura = abreviatura;
    }

    public String getNombre() {
      return nombre;
    }

    public void setNombre(String nombre) {
      this.nombre = nombre;
    }

    public boolean isActivo() {
      return activo;
    }

    public void setActivo(boolean activo) {
      this.activo = activo;
    }

    public int getLongitud() {
      return longitud;
    }

    public void setLongitud(int longitud) {
      this.longitud = longitud;
    }

    public int getTipo() {
      return tipo;
    }

    public void setTipo(int tipo) {
      this.tipo = tipo;
    }

  }

}
