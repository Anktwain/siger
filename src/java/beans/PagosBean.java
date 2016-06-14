/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import dao.ActualizacionDAO;
import dao.ComprobantePagoDAO;
import dao.CreditoDAO;
import dao.EmailDAO;
import dao.GestorDAO;
import dao.PagoDAO;
import dto.Actualizacion;
import dto.ComprobantePago;
import dto.Email;
import dto.Gestor;
import dto.Pago;
import impl.ActualizacionIMPL;
import impl.ComprobantePagoIMPL;
import impl.CreditoIMPL;
import impl.EmailIMPL;
import impl.GestorIMPL;
import impl.PagoIMPL;
import impl.QuincenaIMPL;
import impl.SujetoIMPL;
import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
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
import util.constantes.Directorios;
import util.constantes.Pagos;
import util.constantes.Patrones;
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
  private boolean permitirExport2;
  private boolean habilitaVisualizador;
  private String urlImagen;
  private String nombreArchivo;
  private String nombreArchivo2;
  private String sumaAprobados;
  private String sumaCreditos;
  private String sumaEficiencia;
  private Pago pagoSeleccionado;
  private ComprobantePago comprobanteSeleccionado;
  private Gestor gestorSeleccionado;
  private final PagoDAO pagoDao;
  private final CreditoDAO creditoDao;
  private final ActualizacionDAO actualizacionDao;
  private final GestorDAO gestorDao;
  private final EmailDAO emailDao;
  private final ComprobantePagoDAO comprobantePagoDao;
  private List<Pago> pagosPorRevisar;
  private List<Pago> listaPagos;
  private List<Pago> listaPagosQuincena;
  private List<Pago> pagosSeleccionados;
  private List<String> listaRemitentes;
  private List<String> listaDestinatarios;
  private List<String> listaCorreos;
  private List<PagoGestor> listaPagosGestor;
  private List<ComprobantePago> listaComprobantes;
  private List<Gestor> listaGestores;
  private String remitente;
  private List<String> destinatarios;
  private List<String> listaObservacionesPago;
  private String copia;
  private String asunto;
  private Date fechaInicio;
  private Date fechaFin;
  private String mensajeCorreo;
  private String observacionParaBanco;
  private String observacionRechazo;
  private String nuevoCorreo;
  private String tipoNuevoCorreo;
  private float montoAprobado;

  // CONSTRUCTOR
  public PagosBean() {
    remitente = indexBean.getUsuario().getCorreo();
    permitirExport = false;
    permitirExport2 = false;
    habilitaTabla = false;
    habilitaVisualizador = false;
    pagoDao = new PagoIMPL();
    creditoDao = new CreditoIMPL();
    actualizacionDao = new ActualizacionIMPL();
    gestorDao = new GestorIMPL();
    emailDao = new EmailIMPL();
    comprobantePagoDao = new ComprobantePagoIMPL();
    pagosPorRevisar = new ArrayList();
    listaPagos = new ArrayList();
    listaRemitentes = new ArrayList();
    listaDestinatarios = new ArrayList();
    listaCorreos = new ArrayList();
    destinatarios = new ArrayList();
    listaObservacionesPago = new ArrayList();
    listaPagosGestor = new ArrayList();
    listaComprobantes = new ArrayList();
    listaPagosQuincena = new ArrayList();
    pagosSeleccionados = new ArrayList();
    listaGestores = new ArrayList();
    pagoSeleccionado = new Pago();
    comprobanteSeleccionado = new ComprobantePago();
    gestorSeleccionado = new Gestor();
    cargarListas();
  }

  // METODO QUE GENERA LAS LISTAS CON INFORMACION DE LA BASE DE DATOS
  public final void cargarListas() {
    cargarCorreos();
    pagosPorRevisar = pagoDao.pagosPorRevisarPorDespacho(indexBean.getUsuario().getDespacho().getIdDespacho());
    listaObservacionesPago = Arrays.asList("1 MES", "2 O MAS MESES", "QUITA DE CAPITAL", "CASTIGO", "QUEBRANTO", "NO HACER PAGO(S)", "NO ENVIAR A BANCO", "ACTA DEFUNCION", "LIQUIDADA", "REGULARIZADA");
    listaPagosGestor = generarPagosGestor();
    listaGestores = new GestorIMPL().buscarPorDespacho(indexBean.getUsuario().getDespacho().getIdDespacho());
  }

  // METODO QUE CARGA LAS LISTAS DE CORREO
  public void cargarCorreos() {
    listaRemitentes = new ArrayList();
    listaDestinatarios = new ArrayList();
    listaCorreos = new ArrayList();
    List<Email> lista = emailDao.buscarPorSujeto(2);
    for (int i = 0; i < (lista.size()); i++) {
      listaRemitentes.add(lista.get(i).getDireccion());
    }
    lista = emailDao.buscarPorSujeto(3);
    for (int i = 0; i < (lista.size()); i++) {
      listaDestinatarios.add(lista.get(i).getDireccion());
    }
    listaCorreos.addAll(listaRemitentes);
    listaCorreos.addAll(listaDestinatarios);
  }

  // METODO QUE TRAE LOS DATOS DEL PAGO SELECCIONADO
  public void visualizarListaComprobantes(Pago pago) {
    listaComprobantes = comprobantePagoDao.buscarPorPago(pago.getIdPago());
    habilitaVisualizador = true;
  }

  // METODO QUE PREPARA EL MONTO POR APROBAR DEL PAGO A VALIDAR
  public void prepararMontoAprobado() {
    montoAprobado = pagoSeleccionado.getMontoPago();
  }

  // METODO QUE APRUEBA UN PAGO
  public void aprobarPago() {
    if (montoAprobado > pagoSeleccionado.getMontoPago()) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El monto a aprobar es mayor al monto del pago."));
    } else {
      pagoSeleccionado.setEstatus(Pagos.APROBADO);
      pagoSeleccionado.setRevisor(indexBean.getUsuario().getNombreLogin());
      pagoSeleccionado.setMontoAprobado(montoAprobado);
      pagoSeleccionado.setInformacionRevision(observacionParaBanco);
      pagoSeleccionado.setQuincena(new QuincenaIMPL().obtenerQuincenaActual());
      boolean ok = pagoDao.editar(pagoSeleccionado);
      if (ok) {
        Actualizacion act = actualizacionDao.buscarUltimaActualizacion(pagoSeleccionado.getPromesaPago().getConvenioPago().getCredito().getIdCredito());
        if (act != null) {
          float saldoVencido = act.getSaldoVencido();
          saldoVencido = saldoVencido - pagoSeleccionado.getMontoAprobado();
          act.setSaldoVencido(saldoVencido);
          if (!actualizacionDao.editar(act)) {
            Logs.log.error("No se pudo aplicar la actualizacion al saldo vencido.");
          }
        } else {
          Logs.log.error("No existe actualizacion para este credito.");
        }
        cargarListas();
        Logs.log.info("El administrador " + indexBean.getUsuario().getNombreLogin() + " aprobo un pago por $" + pagoSeleccionado.getMontoAprobado() + " del credito # " + pagoSeleccionado.getPromesaPago().getConvenioPago().getCredito().getNumeroCredito());
        GestionAutomatica.generarGestionAutomatica("16PAGSI", pagoSeleccionado.getPromesaPago().getConvenioPago().getCredito(), indexBean.getUsuario(), "SE APRUEBA PAGO POR $" + pagoSeleccionado.getMontoAprobado());
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se aprobo el pago."));
      } else {
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo aprobar el pago. Contacte al equipo de sistemas."));
      }
    }
  }

  // METODO QUE RECHAZA UN PAGO
  public void rechazarPago() {
    pagoSeleccionado.setEstatus(Pagos.RECHAZADO);
    pagoSeleccionado.setRevisor(indexBean.getUsuario().getNombreLogin());
    pagoSeleccionado.setObservacionRevisor(observacionRechazo);
    boolean ok = pagoDao.editar(pagoSeleccionado);
    if (ok) {
      cargarListas();
      Logs.log.info("El administrador " + indexBean.getUsuario().getNombreLogin() + " rechazo un pago por $" + pagoSeleccionado.getMontoPago() + " del credito # " + pagoSeleccionado.getPromesaPago().getConvenioPago().getCredito().getNumeroCredito());
      GestionAutomatica.generarGestionAutomatica("17PAGNO", pagoSeleccionado.getPromesaPago().getConvenioPago().getCredito(), indexBean.getUsuario(), "SE RECHAZA PAGO POR $" + pagoSeleccionado.getMontoPago());
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se rechazo el pago."));
    } else {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo rechazar el pago. Contacte al equipo de sistemas."));
    }
  }

  public void buscar() {
    if (validarFechas()) {
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
      if (gestorSeleccionado.getIdGestor() == 0) {
        nombreArchivo = "PAGOS_" + df.format(fechaInicio) + "_" + df.format(fechaFin).replace(":", "-");
        listaPagos = pagoDao.pagosPorDespacho(indexBean.getUsuario().getDespacho().getIdDespacho(), df.format(fechaInicio), df.format(fechaFin));
      } else {
        listaPagos = pagoDao.pagosPorGestor(gestorSeleccionado.getIdGestor(), df.format(fechaInicio), df.format(fechaFin));
      }
      habilitaTabla = true;
      permitirExport = !listaPagos.isEmpty();
      RequestContext.getCurrentInstance().update("formTodosPagos");
    }
  }

  // METODO QUE VALIDA LAS FECHAS
  public boolean validarFechas() {
    boolean ok = true;
    Date fechaActual = new Date();
    if (fechaInicio.after(fechaActual)) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "La fecha inicial no puede ser mayor a la actual."));
      ok = false;
    }
    if (fechaFin.after(fechaActual)) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "La fecha final no puede ser mayor a la actual."));
      ok = false;
    }
    if (fechaFin.before(fechaInicio)) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "La fecha final no puede ser menor a la fecha inicial."));
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

  // METODO QUE LE DA UNA ETIQUETA AL ESTATUS DEL PAGO DE COMISIONES
  public String etiquetarEstatusPago(int pagado) {
    String estado = null;
    if (pagado == Pagos.PAGADO) {
      estado = "Si";
    }
    if (pagado == Pagos.NO_PAGADO) {
      estado = "No";
    }
    return estado;
  }

  // METODO QUE GESTIONA EL ENVIO DE PAGOS A REVISION
  public void verificarCorreoRevision() {
    if (pagoSeleccionado.getEstatus() == Pagos.REVISION_BANCO) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "Este pago ya fue enviado a revision."));
    } else {
      copia = "cobranza_ibr@corporativodelrio.com";
      String producto;
      if (pagoSeleccionado.getPromesaPago().getConvenioPago().getCredito().getSubproducto().getNombre().contains("CT EXPRESS")) {
        producto = "CT EXPRESS";
      } else if (pagoSeleccionado.getPromesaPago().getConvenioPago().getCredito().getSubproducto().getNombre().contains("PERSONAL")) {
        producto = "SOFOM PERSONAL";
      } else if (pagoSeleccionado.getPromesaPago().getConvenioPago().getCredito().getSubproducto().getNombre().contains("PERSONALES")) {
        producto = "SOFOM PERSONAL";
      } else {
        producto = "SOFOM COMERCIAL";
      }
      String credito = pagoSeleccionado.getPromesaPago().getConvenioPago().getCredito().getNumeroCredito();
      String deudor = pagoSeleccionado.getPromesaPago().getConvenioPago().getCredito().getDeudor().getSujeto().getNombreRazonSocial();
      asunto = producto + " " + credito + " " + deudor + " $" + pagoSeleccionado.getMontoPago() + " " + pagoSeleccionado.getFechaDeposito() + " " + pagoSeleccionado.getNumeroCuenta();
      mensajeCorreo = pagoSeleccionado.getObservacionGestor() + "\n\nSALUDOS CORDIALES.\n\n";
      RequestContext.getCurrentInstance().execute("PF('mailPagoDialog').show()");
    }
  }

  // METODO QUE ENVIA EL CORREO ELECTRONICO AL BANCO
  public void enviarCorreoBanco() {
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
      mensaje.addRecipient(Message.RecipientType.BCC, new InternetAddress("cobranza_ibr@corporativodelrio.com"));
      if (!destinatarios.isEmpty()) {
        mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatarios.get(0)));
        mensaje.addRecipient(Message.RecipientType.CC, new InternetAddress(copia));
        for (int i = 1; i < (destinatarios.size()); i++) {
          mensaje.addRecipient(Message.RecipientType.CC, new InternetAddress(destinatarios.get(i)));
        }
      }
      mensaje.setSubject(asunto);
      MimeMultipart multiParte = new MimeMultipart();
      BodyPart texto = new MimeBodyPart();
      texto.setText(mensajeCorreo);
      multiParte.addBodyPart(texto);
      List<ComprobantePago> comprobantes = comprobantePagoDao.buscarPorPago(pagoSeleccionado.getIdPago());
      for (int i = 0; i < (comprobantes.size()); i++) {
        BodyPart adjunto = new MimeBodyPart();
        adjunto.setDataHandler(new DataHandler(new FileDataSource(comprobantes.get(i).getNombreComprobante())));
        String extension = comprobantes.get(i).getNombreComprobante().substring(comprobantes.get(i).getNombreComprobante().length() - 4, comprobantes.get(i).getNombreComprobante().length());
        adjunto.setFileName("Comprobante" + (i + 1) + extension);
        multiParte.addBodyPart(adjunto);
      }
      mensaje.setContent(multiParte);
      Transport t = sesion.getTransport("smtp");
      t.connect(remitente, buscaP(remitente));
      t.sendMessage(mensaje, mensaje.getAllRecipients());
      pagoSeleccionado.setEstatus(Pagos.REVISION_BANCO);
      if (pagoDao.editar(pagoSeleccionado)) {
        GestionAutomatica.generarGestionAutomatica("25VAPA", pagoSeleccionado.getPromesaPago().getConvenioPago().getCredito(), indexBean.getUsuario(), "SE ENVIA PAGO PARA REVISION POR PARTE DEL BANCO");
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se envio el pago a revision."));
      }
    } catch (Exception e) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se envio el pago a revision. Contacte al equipo de sistemas."));
      Logs.log.error("No se pudo enviar pago a revision con el banco");
      Logs.log.error("Remitente: " + remitente + ", destinatario: " + destinatarios + ", cco: " + copia);
      Logs.log.error(e.getStackTrace());
    } finally {
      RequestContext.getCurrentInstance().execute("PF('mailPagoDialog').hide()");
    }
  }

  // METODO QUE OBTIENE EL NOMBRE DE LA QUINCENA ACTUAL
  public String obtenerQuincenaActual() {
    return new QuincenaIMPL().obtenerQuincenaActual().getNombre();
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

  // METODO QUE GENERA LA TABLA DE PAGOS POR GESTOR
  public List<PagoGestor> generarPagosGestor() {
    float aprobados = 0;
    float creditos = 0;
    List<PagoGestor> lista = new ArrayList();
    List<Gestor> gestores = gestorDao.buscarPorDespacho(indexBean.getUsuario().getDespacho().getIdDespacho());
    for (int i = 0; i < (gestores.size()); i++) {
      PagoGestor pg = new PagoGestor();
      pg.setIdGestor(gestores.get(i).getIdGestor());
      pg.setGestor(gestores.get(i).getUsuario().getNombreLogin());
      pg.setMontoPagos(pagoDao.calcularMontoGestor(gestores.get(i).getIdGestor()));
      pg.setMontoPrometido(actualizacionDao.obtenerMontoPrometidoGestor(gestores.get(i).getIdGestor()));
      if (pg.getMontoPrometido() == 0) {
        pg.setEficiencia(0);
      } else {
        pg.setEficiencia(((pg.getMontoPagos() * 100) / pg.getMontoPrometido()));
      }
      aprobados = aprobados + pg.getMontoPagos();
      creditos = creditos + pg.getMontoPrometido();
      lista.add(pg);
    }
    DecimalFormat df = new DecimalFormat();
    df.setMaximumFractionDigits(2);
    df.setMinimumFractionDigits(2);
    sumaAprobados = "$" + df.format(aprobados);
    sumaCreditos = "$" + df.format(creditos);
    df.setMaximumFractionDigits(8);
    sumaEficiencia = String.format("%,2.6f", pagoDao.calcularRecuperacionDespacho(indexBean.getUsuario().getDespacho().getIdDespacho())) + " %";
    return lista;
  }

  // METODO QUE AGREGA UNA NUEVA DIRECCION DE CORREO A LA BASE DE DATOS
  public void agregarCorreo() {
    if (!nuevoCorreo.matches(Patrones.PATRON_EMAIL)) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "La direccion de correo proporcionada no tiene un formato valido."));
    } else {
      Email e = new Email();
      if (tipoNuevoCorreo.equals("Remitente")) {
        e.setSujeto(new SujetoIMPL().buscar(2));
        e.setTipo("Bufete Del Rio");
      } else {
        e.setSujeto(new SujetoIMPL().buscar(3));
        e.setTipo("Inbursa");
      }
      e.setDireccion(nuevoCorreo);
      e = emailDao.insertar(e);
      if (e != null) {
        nuevoCorreo = "";
        cargarCorreos();
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se agrego el nuevo correo."));
      } else {
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se agrego el nuevo correo. Contacte al equipo de sistemas."));
      }
    }
  }

  // METODO QUE ABRE EL VISUALIZADOR PARA EL COMPROBANTE SELECCIONADO
  public void visualizar() {
    RequestContext.getCurrentInstance().execute("PF('detallePagoDialogPdf').hide();");
    RequestContext.getCurrentInstance().execute("PF('detallePagoDialogImg').hide();");
    String nombre = comprobanteSeleccionado.getNombreComprobante().replace(Directorios.RUTA_WINDOWS_CARGA_COMPROBANTES, "");
    File f = new File(comprobanteSeleccionado.getNombreComprobante());
    if (f.exists() && !f.isDirectory()) {
      urlImagen = Directorios.RUTA_SERVIDOR_WEB_COMPROBANTES + nombre;
      if (nombre.substring(nombre.indexOf(".")).equals(".pdf")) {
        RequestContext.getCurrentInstance().update("formVisorPagoPdf");
        RequestContext.getCurrentInstance().execute("PF('detallePagoDialogPdf').show();");
      } else {
        RequestContext.getCurrentInstance().update("formVisorPagoImagen");
        RequestContext.getCurrentInstance().execute("PF('detallePagoDialogImg').show();");
      }
    } else {
      Logs.log.error("El comprobante de pago " + nombre + " no esta disponible en el servidor.");
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se encontro el archivo. Contacte al equipo de sistemas."));
    }
  }

  // METODO QUE SIRVE PARA INDICAR SI HA SIDO PAGADA LA COMISION DE ESTE PAGO
  public void pagarComision(List<Pago> pagos) {
    if (pagos.isEmpty()) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "Debe seleccionar al menos un pago."));
    } else {
      boolean ok = true;
      for (int i = 0; i < (pagos.size()); i++) {
        Pago p = pagos.get(i);
        p.setPagado(Pagos.PAGADO);
        ok = pagoDao.editar(p);
        if (!ok) {
          break;
        }
      }
      if (ok) {
        if (gestorSeleccionado.getIdGestor() == 0) {
          listaPagosQuincena = pagoDao.buscarPagosQuincenActual();
        } else {
          listaPagosQuincena = pagoDao.buscarPagosQuincenaActualGestor(gestorSeleccionado.getIdGestor());
        }
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se registraron los pagos de comisiones."));
      } else {
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se logro registrar el pago de la comisiones. Contacte al equipo de sistemas."));
      }
    }
  }

  // METODO QUE CARGA LOS PAGOS DE LA QUINCENA ACTUAL SEGUN EL GESTOR SELECCIONADO
  public void cargarPagosQuincenaGestor() {
    if (gestorSeleccionado.getIdGestor() == 0) {
      listaPagosQuincena = pagoDao.buscarPagosQuincenActual();
      nombreArchivo2 = "PAGOS-TODOS-QUINCENA-" + obtenerQuincenaActual();
    } else {
      listaPagosQuincena = pagoDao.buscarPagosQuincenaActualGestor(gestorSeleccionado.getIdGestor());
      nombreArchivo2 = "PAGOS-" + gestorDao.buscar(gestorSeleccionado.getIdGestor()).getUsuario().getNombreLogin() + "-QUINCENA-" + obtenerQuincenaActual();
    }
    permitirExport2 = !listaPagosQuincena.isEmpty();
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

  public float getMontoAprobado() {
    return montoAprobado;
  }

  public void setMontoAprobado(float montoAprobado) {
    this.montoAprobado = montoAprobado;
  }

  public String getObservacionRechazo() {
    return observacionRechazo;
  }

  public void setObservacionRechazo(String observacionRechazo) {
    this.observacionRechazo = observacionRechazo;
  }

  public boolean isHabilitaVisualizador() {
    return habilitaVisualizador;
  }

  public void setHabilitaVisualizador(boolean habilitaVisualizador) {
    this.habilitaVisualizador = habilitaVisualizador;
  }

  public String getAsunto() {
    return asunto;
  }

  public void setAsunto(String asunto) {
    this.asunto = asunto;
  }

  public String getNuevoCorreo() {
    return nuevoCorreo;
  }

  public void setNuevoCorreo(String nuevoCorreo) {
    this.nuevoCorreo = nuevoCorreo;
  }

  public String getTipoNuevoCorreo() {
    return tipoNuevoCorreo;
  }

  public void setTipoNuevoCorreo(String tipoNuevoCorreo) {
    this.tipoNuevoCorreo = tipoNuevoCorreo;
  }

  public List<ComprobantePago> getListaComprobantes() {
    return listaComprobantes;
  }

  public void setListaComprobantes(List<ComprobantePago> listaComprobantes) {
    this.listaComprobantes = listaComprobantes;
  }

  public ComprobantePago getComprobanteSeleccionado() {
    return comprobanteSeleccionado;
  }

  public void setComprobanteSeleccionado(ComprobantePago comprobanteSeleccionado) {
    this.comprobanteSeleccionado = comprobanteSeleccionado;
  }

  public List<Pago> getListaPagosQuincena() {
    return listaPagosQuincena;
  }

  public void setListaPagosQuincena(List<Pago> listaPagosQuincena) {
    this.listaPagosQuincena = listaPagosQuincena;
  }

  public List<Pago> getPagosSeleccionados() {
    return pagosSeleccionados;
  }

  public void setPagosSeleccionados(List<Pago> pagosSeleccionados) {
    this.pagosSeleccionados = pagosSeleccionados;
  }

  public Gestor getGestorSeleccionado() {
    return gestorSeleccionado;
  }

  public void setGestorSeleccionado(Gestor gestorSeleccionado) {
    this.gestorSeleccionado = gestorSeleccionado;
  }

  public List<Gestor> getListaGestores() {
    return listaGestores;
  }

  public void setListaGestores(List<Gestor> listaGestores) {
    this.listaGestores = listaGestores;
  }

  public boolean isPermitirExport2() {
    return permitirExport2;
  }

  public void setPermitirExport2(boolean permitirExport2) {
    this.permitirExport2 = permitirExport2;
  }

  public String getNombreArchivo2() {
    return nombreArchivo2;
  }

  public void setNombreArchivo2(String nombreArchivo2) {
    this.nombreArchivo2 = nombreArchivo2;
  }

  public String getSumaAprobados() {
    return sumaAprobados;
  }

  public void setSumaAprobados(String sumaAprobados) {
    this.sumaAprobados = sumaAprobados;
  }

  public String getSumaCreditos() {
    return sumaCreditos;
  }

  public void setSumaCreditos(String sumaCreditos) {
    this.sumaCreditos = sumaCreditos;
  }

  public String getSumaEficiencia() {
    return sumaEficiencia;
  }

  public void setSumaEficiencia(String sumaEficiencia) {
    this.sumaEficiencia = sumaEficiencia;
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
