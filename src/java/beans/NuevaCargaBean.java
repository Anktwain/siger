/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.RemesaDAO;
import dto.Remesa;
import dto.carga.ConteoCredito;
import dto.carga.FilaCreditoClasificado;
import dto.carga.FilaCreditoExcel;
import dto.carga.TipoRemesa;
import impl.RemesaIMPL;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import util.carga.ComparadorCreditos;
import util.carga.LectorArchivoCreditosExcel;
import util.MD5;
import util.constantes.Directorios;
import util.constantes.TipoRemesas;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "nuevaCargaBean")
@ViewScoped
public class NuevaCargaBean implements Serializable {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  // VARIABLES DE CLASE
  private boolean leyendaCargaExitosa;
  private boolean leyendaValidacionExitosa;
  private boolean leyendaConteoExitoso;
  private boolean cargaExitosa;
  private float saldoVencido;
  private String pswd;
  private String creditosEncontrados;
  private int idTipoSeleccionado;
  private UploadedFile archivo;
  private List<ConteoCredito> listaCreditosClasificados;
  private List<TipoRemesa> listaTipos;
  private static FilaCreditoClasificado creditosClasificados;
  private static List<FilaCreditoExcel> filas;
  private static String ruta;
  private final RemesaDAO remesaDao;

  // CONSTRUCTOR
  public NuevaCargaBean() {
    listaTipos = new ArrayList();
    remesaDao = new RemesaIMPL();
    obtenerListas();
  }

  // METODO QUE OBTIENE LA LISTA DE TIPOS DE CARGA
  private void obtenerListas() {
    List<String> tipos = Arrays.asList("Asignacion Mensual", "Asignacion Complementaria", "Asignacion Quebranto");
    List<Integer> ids = Arrays.asList(TipoRemesas.ASIGNACION_MENSUAL, TipoRemesas.ASIGNACION_COMPLEMENTARIA, TipoRemesas.ASIGNACION_QUEBRANTO);
    for (int i = 0; i < (tipos.size()); i++) {
      TipoRemesa tr = new TipoRemesa();
      tr.setId(ids.get(i));
      tr.setTipo(tipos.get(i));
      listaTipos.add(tr);
    }
  }

  // METODO AUXILIAR QUE OBTIENE EL ARCHIVO
  public void eventoDeCarga(FileUploadEvent evento) {
    archivo = evento.getFile();
    cargarArchivoAlServidor(archivo);
  }

  // METODO QUE CARGA AL SERVIDOR EL ARCHIVO OBTENIDO DEL EVENTO FILE UPLOAD
  public void cargarArchivoAlServidor(UploadedFile archivo) {
    byte[] bytes;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String extension = archivo.getFileName().substring(archivo.getFileName().indexOf(".")).toLowerCase();
    ruta = "Remesa" + "_" + df.format(new Date()) + extension;
    ruta = ruta.replace(" ", "-");
    ruta = Directorios.RUTA_WINDOWS_CARGA_REMESA + ruta;
    bytes = archivo.getContents();
    BufferedOutputStream stream;
    try {
      stream = new BufferedOutputStream(new FileOutputStream(new File(ruta)));
      stream.write(bytes);
      Logs.log.info("Se carga archivo de remesa al servidor: " + ruta);
      leyendaCargaExitosa = true;
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se cargo archivo al servidor."));
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

  // METODO QUE GUARDA EL VALOR SELECCIONADO DEL TIPO DE REMESA
  public void guardarTipo() {
    
    System.out.println(idTipoSeleccionado);
  }
  
  // METODO QUE HACE LA LECTURA DEL ARCHIVO DE EXCEL
  public void validarCreditos() {
    leyendaCargaExitosa = true;
    leyendaValidacionExitosa = true;
    LectorArchivoCreditosExcel lector = new LectorArchivoCreditosExcel();
    creditosEncontrados = lector.leerArchivoExcel(ruta);
  }

  // METODO QUE HABILITA LA VISUALIZACION DE LA TABLA DE CLASIFICACION DE CREDITOS
  public void mostrarClasificacion() {
    System.out.println("TIPO SELECCIONADO");
    System.out.println(idTipoSeleccionado);
    leyendaCargaExitosa = true;
    leyendaValidacionExitosa = true;
    leyendaConteoExitoso = true;
    listaCreditosClasificados = new ArrayList();
    switch (idTipoSeleccionado) {
      case TipoRemesas.ASIGNACION_MENSUAL:
        System.out.println("ASIGNACION MENSUAL");
        // AQUI SI SE HACEN LAS COMPARACIONES Y SE RETIRAN CREDITOS
        break;
      case TipoRemesas.ASIGNACION_COMPLEMENTARIA:
        System.out.println("ASIGNACION COMPLEMENTARIA");
        // AQUI SOLO SE CARGAN NUEVOS CREDITOS Y NUEVOS TOTALES
        break;
      case TipoRemesas.ASIGNACION_QUEBRANTO:
        System.out.println("ASIGNACION QUEBRANTO");
        // AQUI SE VERIFICA SI EXISTEN CREDITOS ACTIVOS, SI ES ASI SE ACTUALIZAN
        // LOS QUE NO SE CARGAN
        break;
      default:
        System.out.println("NINGUNO DE LOS CASOS ANTES MENCIONADOS");
        System.out.println("PERO PORQUE VERGA NO ESTA GUARDANDO EL VALOR");
        break;
    }
    /*
     ComparadorCreditos comparador = new ComparadorCreditos();
     creditosClasificados = comparador.compararCreditos(filas);
     ConteoCredito cc = new ConteoCredito();
     cc.setTipoCredito("Creditos en gestion (Estan en la fiesta).");
     cc.setCantidad(creditosClasificados.getCreditosEnGestion());
     listaCreditosClasificados.add(cc);
     cc = new ConteoCredito();
     cc.setTipoCredito("Creditos reasignados (Regresan a la fiesta).");
     cc.setCantidad(creditosClasificados.getReasignados().size());
     listaCreditosClasificados.add(cc);
     cc = new ConteoCredito();
     cc.setTipoCredito("Creditos conservados (Siguen en la fiesta)");
     cc.setCantidad(creditosClasificados.getConservados().size());
     listaCreditosClasificados.add(cc);
     cc = new ConteoCredito();
     cc.setTipoCredito("Nuevos creditos (Con invitado en la fiesta)");
     cc.setCantidad(creditosClasificados.getNuevosCreditos().size());
     listaCreditosClasificados.add(cc);
     cc = new ConteoCredito();
     cc.setTipoCredito("Nuevos totales (Nuevos en la fiesta)");
     cc.setCantidad(creditosClasificados.getNuevosTotales().size());
     listaCreditosClasificados.add(cc);
     cc = new ConteoCredito();
     cc.setTipoCredito("Creditos retirados (Se van de la fiesta)");
     cc.setCantidad(creditosClasificados.getRetirados().size());
     listaCreditosClasificados.add(cc);
     cc = new ConteoCredito();
     cc.setTipoCredito("Total de creditos en gestion despues de carga");
     cc.setCantidad(creditosClasificados.getConservados().size() + creditosClasificados.getReasignados().size() + creditosClasificados.getNuevosCreditos().size() + creditosClasificados.getNuevosTotales().size());
     listaCreditosClasificados.add(cc);
     */
  }

  // METODO QUE PIDE LA CONTRASEÑA PARA COMENZAR LA CARGA
  public void pedirContrasena() {
    RequestContext.getCurrentInstance().execute("PF('dialogoContrasena').show()");
  }

  // METODO QUE VERIFICA SI LA CONTRASEÑA ES CORRECTA
  public void verificarContrasena() {
    String passMd5 = MD5.encriptar(pswd);
    if (passMd5.equals(indexBean.getUsuario().getPassword())) {
      RequestContext.getCurrentInstance().execute("PF('dialogoContrasena').hide()");
      ejecutarCarga();
    } else {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Acceso denegado.", "La contraseña proporcionada es incorrecta. Verifique por favor."));
    }
  }

  // METODO QUE CONFIRMA LA CARGA Y HACE LAS MODIFICACIONES EN LA BASE DE DATOS
  public void ejecutarCarga() {
    if (crearRemesa()) {
      leyendaCargaExitosa = true;
      leyendaValidacionExitosa = true;
      leyendaConteoExitoso = true;
      cargaExitosa = true;
      RequestContext.getCurrentInstance().update("leyendaPaso1Carga, leyendaPaso2Carga, leyendaPaso3Carga, paso4Carga");
      ComparadorCreditos comparador = new ComparadorCreditos();
      comparador.confirmarCarga(creditosClasificados);
    } else {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo ejecutar el proceso de carga. Contacte al equipo de sistemas."));
    }
  }

  // METODO QUE CREA LA REMESA EN LA BASE DE DATOS
  public boolean crearRemesa() {
    Remesa r = new Remesa();
    r.setMes(Calendar.getInstance().get(Calendar.MONTH) + 1);
    r.setAnio(Calendar.getInstance().get(Calendar.YEAR));
    saldoVencido = 0;
    for (int i = 0; i < (filas.size()); i++) {
      saldoVencido = saldoVencido + Float.valueOf(filas.get(i).getSaldoVencido());
    }
    r.setTotalSaldoVencido(saldoVencido);
    r.setTotalCreditos(filas.size());
    r.setFechaCarga(new Date());
    r.setQuienCarga(indexBean.getUsuario().getIdUsuario());
    return remesaDao.insertar(r) != null;
  }

  // METODO QUE REDIRIGE AL PANEL DE CONTROL UNA VEZ INICIADA LA CARGA
  public void aceptar() {
    try {
      FacesContext.getCurrentInstance().getExternalContext().redirect(indexBean.getVista());
    } catch (IOException ioe) {
      Logs.log.error("No se pudo redirigir al " + indexBean.getVista() + ".");
      Logs.log.error(ioe);
    }
  }

  // GETTERS & SETTERS
  public UploadedFile getArchivo() {
    return archivo;
  }

  public void setArchivo(UploadedFile archivo) {
    this.archivo = archivo;
  }

  public String getPswd() {
    return pswd;
  }

  public void setPswd(String pswd) {
    this.pswd = pswd;
  }

  public boolean isLeyendaCargaExitosa() {
    return leyendaCargaExitosa;
  }

  public void setLeyendaCargaExitosa(boolean leyendaCargaExitosa) {
    this.leyendaCargaExitosa = leyendaCargaExitosa;
  }

  public boolean isLeyendaValidacionExitosa() {
    return leyendaValidacionExitosa;
  }

  public void setLeyendaValidacionExitosa(boolean leyendaValidacionExitosa) {
    this.leyendaValidacionExitosa = leyendaValidacionExitosa;
  }

  public boolean isLeyendaConteoExitoso() {
    return leyendaConteoExitoso;
  }

  public void setLeyendaConteoExitoso(boolean leyendaConteoExitoso) {
    this.leyendaConteoExitoso = leyendaConteoExitoso;
  }

  public boolean isCargaExitosa() {
    return cargaExitosa;
  }

  public void setCargaExitosa(boolean cargaExitosa) {
    this.cargaExitosa = cargaExitosa;
  }

  public String getCreditosEncontrados() {
    return creditosEncontrados;
  }

  public void setCreditosEncontrados(String creditosEncontrados) {
    this.creditosEncontrados = creditosEncontrados;
  }

  public List<FilaCreditoExcel> getFilas() {
    return filas;
  }

  public void setFilas(List<FilaCreditoExcel> filas) {
    NuevaCargaBean.filas = filas;
  }

  public List<ConteoCredito> getListaCreditosClasificados() {
    return listaCreditosClasificados;
  }

  public void setListaCreditosClasificados(List<ConteoCredito> listaCreditosClasificados) {
    this.listaCreditosClasificados = listaCreditosClasificados;
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

}
