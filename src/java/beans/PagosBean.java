/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import dao.ActualizacionDAO;
import dao.CreditoDAO;
import dao.GestorDAO;
import dao.PagoDAO;
import dto.Actualizacion;
import dto.Gestor;
import dto.Pago;
import impl.ActualizacionIMPL;
import impl.CreditoIMPL;
import impl.GestorIMPL;
import impl.PagoIMPL;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.primefaces.context.RequestContext;
import util.GestionAutomatica;
import util.ManejadorArchivosDeTexto;
import util.constantes.Directorios;
import util.constantes.Pagos;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "pagosBean")
@ViewScoped
public class PagosBean implements Serializable {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  // VARIABLES DE CLASE
  private boolean habilitaTabla;
  private boolean permitirExport;
  private String urlImagen;
  private String nombreArchivo;
  private Pago pagoSeleccionado;
  private final PagoDAO pagoDao;
  private final CreditoDAO creditoDao;
  private final ActualizacionDAO actualizacionDao;
  private final GestorDAO gestorDao;
  private List<Pago> pagosPorRevisar;
  private List<Pago> listaPagos;
  private List<String> listaRemitentes;
  private List<String> listaDestinatarios;
  private List<String> listaCorreos;
  private List<PagoGestor> listaPagosGestor;
  private String remitente;
  private List<String> destinatarios;
  private List<String> listaObservacionesPago;
  private String copia;
  private Date fechaInicio;
  private Date fechaFin;
  private String mensajeCorreo;
  private String observacionParaBanco;

  // CONSTRUCTOR
  public PagosBean() {
    remitente = indexBean.getUsuario().getCorreo();
    permitirExport = false;
    habilitaTabla = false;
    pagoDao = new PagoIMPL();
    creditoDao = new CreditoIMPL();
    actualizacionDao = new ActualizacionIMPL();
    gestorDao = new GestorIMPL();
    pagosPorRevisar = new ArrayList();
    listaPagos = new ArrayList();
    listaRemitentes = new ArrayList();
    listaDestinatarios = new ArrayList();
    listaCorreos = new ArrayList();
    destinatarios = new ArrayList();
    listaObservacionesPago = new ArrayList();
    listaPagosGestor = new ArrayList();
    cargarListas();
  }

  // METODO QUE GENERA LAS LISTAS CON INFORMACION DE LA BASE DE DATOS
  public final void cargarListas() {
    pagosPorRevisar = pagoDao.pagosPorRevisarPorDespacho(indexBean.getUsuario().getDespacho().getIdDespacho());
    listaObservacionesPago = Arrays.asList("1 MES", "2 O MAS MESES", "QUITA DE CAPITAL", "CASTIGO", "QUEBRANTO", "NO HACER PAGO(S)", "NO ENVIAR A BANCO", "ACTA DEFUNCION", "LIQUIDADA", "REGULARIZADA");
    listaPagosGestor = generarPagosGestor();
  }

  // METODO QUE TRAE LOS DATOS DEL PAGO SELECCIONADO
  public void visualizar() {
    urlImagen = Directorios.RUTA_SERVIDOR_WEB_COMPROBANTES + pagoSeleccionado.getNombreComprobante();
    RequestContext.getCurrentInstance().update("formVisorPago");
    RequestContext.getCurrentInstance().execute("PF('detallePagoDialog').show();");
  }

// METODO QUE APRUEBA UN PAGO
  public void aprobarPago() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    Actualizacion act = actualizacionDao.buscarUltimaActualizacion(pagoSeleccionado.getPromesaPago().getConvenioPago().getCredito().getIdCredito());
    if (act != null) {
      float saldoVencido = act.getSaldoVencido();
      saldoVencido = saldoVencido - pagoSeleccionado.getMonto();
      act.setSaldoVencido(saldoVencido);
      if (!actualizacionDao.editar(act)) {
        Logs.log.error("No se pudo aplicar la actualizacion al saldo vencido.");
      }
    } else {
      Logs.log.error("No se pudo aplicar la actualizacion al saldo vencido.");
    }
    pagoSeleccionado.setEstatus(Pagos.APROBADO);
    pagoSeleccionado.setRevisor(indexBean.getUsuario().getNombreLogin());
    boolean ok = pagoDao.editar(pagoSeleccionado);
    if (ok) {
      cargarListas();
      Logs.log.info("El administrador " + indexBean.getUsuario().getNombreLogin() + " aprobo un pago por $" + pagoSeleccionado.getMonto() + " del credito # " + pagoSeleccionado.getPromesaPago().getConvenioPago().getCredito().getNumeroCredito());
      GestionAutomatica.generarGestionAutomatica("16PAGSI", pagoSeleccionado.getPromesaPago().getConvenioPago().getCredito(), indexBean.getUsuario(), "SE APRUEBA PAGO POR $" + pagoSeleccionado.getMonto());
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se aprobo el pago."));
    } else {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo aprobar el pago. Contacte al equipo de sistemas."));
    }
  }

  // METODO QUE RECHAZA UN PAGO
  public void rechazarPago() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    pagoSeleccionado.setEstatus(Pagos.RECHAZADO);
    pagoSeleccionado.setRevisor(indexBean.getUsuario().getNombreLogin());
    boolean ok = pagoDao.editar(pagoSeleccionado);
    if (ok) {
      cargarListas();
      Logs.log.info("El administrador " + indexBean.getUsuario().getNombreLogin() + " rechazo un pago por $" + pagoSeleccionado.getMonto() + " del credito # " + pagoSeleccionado.getPromesaPago().getConvenioPago().getCredito().getNumeroCredito());
      GestionAutomatica.generarGestionAutomatica("17PAGNO", pagoSeleccionado.getPromesaPago().getConvenioPago().getCredito(), indexBean.getUsuario(), "SE RECHAZA PAGO POR $" + pagoSeleccionado.getMonto());
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se rechazo el pago."));
    } else {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo rechazar el pago. Contacte al equipo de sistemas."));
    }
  }

  public void buscar() {
    if (validarFechas()) {
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
      nombreArchivo = "PAGOS_" + df.format(fechaInicio) + "_" + df.format(fechaFin).replace(":", "-");
      listaPagos = pagoDao.pagosPorDespacho(indexBean.getUsuario().getDespacho().getIdDespacho(), df.format(fechaInicio), df.format(fechaFin));
      habilitaTabla = true;
      permitirExport = !listaPagos.isEmpty();
      RequestContext.getCurrentInstance().update("formTodosPagos");
    }
  }

  // METODO QUE VALIDA LAS FECHAS
  public boolean validarFechas() {
    boolean ok = true;
    FacesContext contexto = FacesContext.getCurrentInstance();
    Date fechaActual = new Date();
    if (fechaInicio.after(fechaActual)) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "La fecha inicial no puede ser mayor a la actual."));
      ok = false;
    }
    if (fechaFin.after(fechaActual)) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "La fecha final no puede ser mayor a la actual."));
      ok = false;
    }
    if (fechaFin.before(fechaInicio)) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "La fecha final no puede ser menor a la fecha inicial."));
      ok = false;
    }
    return ok;
  }

  // METODO QUE LE DA UNA ETIQUETA A LOS VALORES NUMERICOS DEL ESTATUS DE PAGOS
  public String etiquetarEstatus(int estatus) {
    String estado = null;
    if (estatus == Pagos.APROBADO) {
      estado = "Aprobado";
    }
    if (estatus == Pagos.PENDIENTE) {
      estado = "Pendiente";
    }
    if (estatus == Pagos.RECHAZADO) {
      estado = "Rechazado";
    }
    if (estatus == Pagos.REVISION_BANCO) {
      estado = "Revision banco";
    }
    return estado;
  }

  // METODO QUE GESTIONA EL ENVIO DE PAGOS A REVISION
  public void verificarCorreoRevision() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    if (pagoSeleccionado.getEstatus() == Pagos.REVISION_BANCO) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "Este pago ya fue enviado a revision."));
    } else {
      try {
        String[] arr;
        arr = ManejadorArchivosDeTexto.leerArchivo(Directorios.RUTA_CORREOS, "remitentes.txt").split(";");
        for (int i = 0; i < (arr.length); i++) {
          listaRemitentes.add(arr[i]);
        }
        arr = ManejadorArchivosDeTexto.leerArchivo(Directorios.RUTA_CORREOS, "destinatarios.txt").split(";");
        for (int i = 0; i < (arr.length); i++) {
          listaDestinatarios.add(arr[i]);
        }
        listaCorreos.addAll(listaRemitentes);
        listaCorreos.addAll(listaDestinatarios);
        copia = "1ctexpress_ibr@corporativodelrio.com";
        mensajeCorreo = "TE MANDO PAGO PARA SU VALIDACION";
      } catch (IOException ioe) {
        Logs.log.error(ioe);
      }
      RequestContext.getCurrentInstance().execute("PF('mailPagoDialog').show()");
    }
  }

  // METODO QUE ENVIA EL CORREO ELECTRONICO AL BANCO
  public void enviarCorreoBanco() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    try {
      Properties props = new Properties();
      props.put("mail.smtp.host", "mail.corporativodelrio.com");
      props.setProperty("mail.smtp.starttls.enable", "true");
      props.setProperty("mail.smtp.port", "587");
      props.setProperty("mail.smtp.user", remitente);
      props.setProperty("mail.smtp.auth", "true");
      Session sesion = Session.getDefaultInstance(props, null);
      MimeMessage mensaje = new MimeMessage(sesion);
      mensaje.setFrom(new InternetAddress(remitente));
      mensaje.addRecipient(Message.RecipientType.BCC, new InternetAddress(copia));
      if (!destinatarios.isEmpty()) {
        mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatarios.get(0)));
        for (int i = 1; i < (destinatarios.size()); i++) {
          mensaje.addRecipient(Message.RecipientType.CC, new InternetAddress(destinatarios.get(i)));
        }
      }
      mensaje.setSubject(pagoSeleccionado.getPromesaPago().getConvenioPago().getCredito().getProducto().getNombre() + " " + pagoSeleccionado.getPromesaPago().getConvenioPago().getCredito().getDeudor().getSujeto().getNombreRazonSocial() + " $" + pagoSeleccionado.getMonto());
      mensajeCorreo = mensajeCorreo + ". " + observacionParaBanco + "\n\nSALUDOS CORDIALES.\n\n";
      MimeMultipart multiParte = new MimeMultipart();
      BodyPart texto = new MimeBodyPart();
      texto.setText(mensajeCorreo);
      multiParte.addBodyPart(texto);
      BodyPart adjunto = new MimeBodyPart();
      adjunto.setDataHandler(new DataHandler(new FileDataSource(Directorios.RUTA_WINDOWS_CARGA_COMPROBANTES + pagoSeleccionado.getNombreComprobante())));
      adjunto.setFileName(pagoSeleccionado.getNombreComprobante());
      multiParte.addBodyPart(adjunto);
      mensaje.setContent(multiParte);
      Transport t = sesion.getTransport("smtp");
      t.connect(remitente, buscaP(remitente));
      t.sendMessage(mensaje, mensaje.getAllRecipients());
      pagoSeleccionado.setEstatus(Pagos.REVISION_BANCO);
      if (pagoDao.editar(pagoSeleccionado)) {
        GestionAutomatica.generarGestionAutomatica("25VAPA", pagoSeleccionado.getPromesaPago().getConvenioPago().getCredito(), indexBean.getUsuario(), "SE ENVIA PAGO PARA REVISION POR PARTE DEL BANCO");
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se envio el pago a revision."));
      }
    } catch (Exception e) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se envio el pago a revision. Contacte al equipo de sistemas."));
      Logs.log.error("No se pudo enviar pago a revision con el banco");
      Logs.log.error("Remitente: " + remitente + ", destinatario: " + destinatarios + ", cco: " + copia);
      Logs.log.error(e);
    } finally {
      RequestContext.getCurrentInstance().execute("PF('mailPagoDialog').hide()");
    }
  }

  // METODO QUE OBTIENE LOS MESES VENCIDOS DEL CREDITO
  public int obtenerMesesVencidos(int idCredito) {
    return creditoDao.buscarMesesVencidosCredito(idCredito);
  }

  //METODO QUE GUARDA EL EVENTO DE EXPORTACION
  public void reportarExportacion(String formato) {
    Logs.log.info("El administrador " + indexBean.getUsuario().getNombreLogin() + " exporto un reporte de pagos en formato " + formato);
  }

  // METODO QUE PREPARA EL PDF PARA QUE TENGA MEJOR ESTETICA
  public void preparaPdf(Object document) {
    Document pdf = (Document) document;
    pdf.setPageSize(PageSize.LETTER.rotate());
    pdf.setMargins(10, 10, 10, 10);
    pdf.open();
  }

  // METODO QUE OBTIENE LA CONTRASEÑA DEL REMITENTE
  public String buscaP(String remitente) {
    String p = "";
    switch (remitente) {
      case "lilia.delrio@corporativodelrio.com":
        p = "002-21-4";
        break;
      case "eduardo.chavez@corporativodelrio.com":
        p = "009-94-92";
        break;
      case "1ctexpress_ibr@corporativodelrio.com":
        p = "Dt%W9*DyLdg5";
        break;
      case "solinfo_ibr@corporativodelrio.com":
        p = "&@7V#tD,LDt,";
        break;
      case "cobranza_ibr@corporativodelrio.com":
        p = "";
        break;
    }
    return p;
  }
  
  // METODO QUE GNERA LA TABLA DE PAGOS POR GESTOR
  public List<PagoGestor> generarPagosGestor(){
    List<PagoGestor> lista = new ArrayList();
    List<Gestor> gestores = gestorDao.buscarPorDespacho(indexBean.getUsuario().getDespacho().getIdDespacho());
    for (int i = 0; i <(gestores.size()); i++) {
      PagoGestor pg = new PagoGestor();
      pg.setIdGestor(gestores.get(i).getIdGestor());
      pg.setGestor(gestores.get(i).getUsuario().getNombreLogin());
      pg.setMontoPagos(pagoDao.calcularMontoGestor(gestores.get(i).getIdGestor()));
      pg.setMontoPrometido(actualizacionDao.obtenerMontoPrometidoGestor(gestores.get(i).getIdGestor()));
      pg.setEficiencia((pg.getMontoPagos()*100)/pg.getMontoPrometido());
      lista.add(pg);
    }
    return lista;
  }

  // GETTERS & SETTERS
  public List<Pago> getPagosPorRevisar() {
    return pagosPorRevisar;
  }

  public void setPagosPorRevisar(List<Pago> pagosPorRevisar) {
    this.pagosPorRevisar = pagosPorRevisar;
  }

  public Pago getPagoSeleccionado() {
    return pagoSeleccionado;
  }

  public void setPagoSeleccionado(Pago pagoSeleccionado) {
    this.pagoSeleccionado = pagoSeleccionado;
  }

  public String getUrlImagen() {
    return urlImagen;
  }

  public void setUrlImagen(String urlImagen) {
    this.urlImagen = urlImagen;
  }

  public List<Pago> getListaPagos() {
    return listaPagos;
  }

  public void setListaPagos(List<Pago> listaPagos) {
    this.listaPagos = listaPagos;
  }

  public boolean isHabilitaTabla() {
    return habilitaTabla;
  }

  public void setHabilitaTabla(boolean habilitaTabla) {
    this.habilitaTabla = habilitaTabla;
  }

  public boolean isPermitirExport() {
    return permitirExport;
  }

  public void setPermitirExport(boolean permitirExport) {
    this.permitirExport = permitirExport;
  }

  public String getNombreArchivo() {
    return nombreArchivo;
  }

  public void setNombreArchivo(String nombreArchivo) {
    this.nombreArchivo = nombreArchivo;
  }

  public Date getFechaInicio() {
    return fechaInicio;
  }

  public void setFechaInicio(Date fechaInicio) {
    this.fechaInicio = fechaInicio;
  }

  public Date getFechaFin() {
    return fechaFin;
  }

  public void setFechaFin(Date fechaFin) {
    this.fechaFin = fechaFin;
  }

  public List<String> getListaRemitentes() {
    return listaRemitentes;
  }

  public void setListaRemitentes(List<String> listaRemitentes) {
    this.listaRemitentes = listaRemitentes;
  }

  public List<String> getListaDestinatarios() {
    return listaDestinatarios;
  }

  public void setListaDestinatarios(List<String> listaDestinatarios) {
    this.listaDestinatarios = listaDestinatarios;
  }

  public List<String> getListaCorreos() {
    return listaCorreos;
  }

  public void setListaCorreos(List<String> listaCorreos) {
    this.listaCorreos = listaCorreos;
  }

  public String getRemitente() {
    return remitente;
  }

  public void setRemitente(String remitente) {
    this.remitente = remitente;
  }

  public List<String> getDestinatarios() {
    return destinatarios;
  }

  public void setDestinatarios(List<String> destinatarios) {
    this.destinatarios = destinatarios;
  }

  public String getCopia() {
    return copia;
  }

  public void setCopia(String copia) {
    this.copia = copia;
  }

  public String getMensajeCorreo() {
    return mensajeCorreo;
  }

  public void setMensajeCorreo(String mensajeCorreo) {
    this.mensajeCorreo = mensajeCorreo;
  }

  public List<String> getListaObservacionesPago() {
    return listaObservacionesPago;
  }

  public void setListaObservacionesPago(List<String> listaObservacionesPago) {
    this.listaObservacionesPago = listaObservacionesPago;
  }

  public String getObservacionParaBanco() {
    return observacionParaBanco;
  }

  public void setObservacionParaBanco(String observacionParaBanco) {
    this.observacionParaBanco = observacionParaBanco;
  }

  public List<PagoGestor> getListaPagosGestor() {
    return listaPagosGestor;
  }

  public void setListaPagosGestor(List<PagoGestor> listaPagosGestor) {
    this.listaPagosGestor = listaPagosGestor;
  }

  // CLASE MIEMBRO QUE RELACIONA AL GESTOR CON SUS PAGOS
  // MOSTRARA TODOS LOS DATOS RELACIONADOS CON LA COBRANZA Y EL DESEMPEÑO DE CADA GESTOR
  public static class PagoGestor {

    private int idGestor;
    private String gestor;
    private float montoPagos;
    private float montoPrometido;
    private float eficiencia;

    public PagoGestor() {
    }

    public int getIdGestor() {
      return idGestor;
    }

    public void setIdGestor(int idGestor) {
      this.idGestor = idGestor;
    }

    public String getGestor() {
      return gestor;
    }

    public void setGestor(String gestor) {
      this.gestor = gestor;
    }

    public float getMontoPagos() {
      return montoPagos;
    }

    public void setMontoPagos(float montoPagos) {
      this.montoPagos = montoPagos;
    }

    public float getMontoPrometido() {
      return montoPrometido;
    }

    public void setMontoPrometido(float montoPrometido) {
      this.montoPrometido = montoPrometido;
    }

    public float getEficiencia() {
      return eficiencia;
    }

    public void setEficiencia(float eficiencia) {
      this.eficiencia = eficiencia;
    }
    
  }

}
