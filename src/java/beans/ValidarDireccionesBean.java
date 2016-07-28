/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.ColoniaDAO;
import dao.CreditoDAO;
import dao.DireccionDAO;
import dao.EstadoRepublicaDAO;
import dao.MunicipioDAO;
import dao.SujetoDAO;
import dto.Colonia;
import dto.ComprobantePago;
import dto.Credito;
import dto.Direccion;
import dto.EstadoRepublica;
import dto.Municipio;
import dto.carga.DireccionPorValidar;
import dto.carga.FilaDireccionExcel;
import impl.ColoniaIMPL;
import impl.CreditoIMPL;
import impl.DireccionIMPL;
import impl.EstadoRepublicaIMPL;
import impl.MunicipioIMPL;
import impl.SujetoIMPL;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import util.Carga.LectorArchivoDireccionesExcel;
import util.Carga.ValidadorDirecciones;
import util.GestionAutomatica;
import util.constantes.Pagos;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "validarDireccionesBean")
@ViewScoped
public class ValidarDireccionesBean implements Serializable {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  // VARIABLES DE CLASE
  private boolean habilitaCarga;
  private boolean habilitaValidacionArchivo;
  private boolean habilitaValidacionAutomatica;
  private String color;
  private String ruta;
  private String direccionesEncontradas;
  private String direccionesValidadas;
  private String tipoNuevaColonia;
  private String nombreNuevaColonia;
  private String cpNuevaColonia;
  private List<String> tiposAsentamiento;
  private String remitente;
  private String destinatario;
  private String asunto;
  private String mensajeCorreo;
  private List<String> listaDestinatarios;
  private DireccionPorValidar direccionSeleccionada;
  private List<FilaDireccionExcel> direccionesArchivo;
  private List<DireccionPorValidar> direccionesPorValidar;
  private Colonia nuevaColonia;
  private List<Colonia> listaColonias;
  private Municipio nuevoMunicipio;
  private Municipio municipioNuevaColonia;
  private List<Municipio> listaMunicipios;
  private EstadoRepublica nuevoEstado;
  private EstadoRepublica estadoNuevaColonia;
  private List<EstadoRepublica> listaEstados;
  private UploadedFile archivo;
  private final ColoniaDAO coloniaDao;
  private final MunicipioDAO municipioDao;
  private final EstadoRepublicaDAO estadoDao;
  private final CreditoDAO creditoDao;
  private final DireccionDAO direccionDao;

  // CONSTRUCTOR
  public ValidarDireccionesBean() {
    habilitaCarga = false;
    habilitaValidacionArchivo = false;
    habilitaValidacionAutomatica = false;
    direccionSeleccionada = new DireccionPorValidar();
    nuevaColonia = new Colonia();
    nuevoMunicipio = new Municipio();
    nuevoEstado = new EstadoRepublica();
    listaColonias = new ArrayList();
    listaMunicipios = new ArrayList();
    listaEstados = new ArrayList();
    direccionesArchivo = new ArrayList();
    direccionesPorValidar = new ArrayList();
    coloniaDao = new ColoniaIMPL();
    municipioDao = new MunicipioIMPL();
    estadoDao = new EstadoRepublicaIMPL();
    creditoDao = new CreditoIMPL();
    direccionDao = new DireccionIMPL();
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
    ruta = "Direcciones" + "_" + df.format(new Date()) + extension;
    ruta = ruta.replace(" ", "-");
    ruta = "C:\\cargasDePruebaNuevoSiger\\" + ruta;
    bytes = archivo.getContents();
    BufferedOutputStream stream;
    try {
      stream = new BufferedOutputStream(new FileOutputStream(new File(ruta)));
      stream.write(bytes);
      Logs.log.info("Se carga archivo de direcciones al servidor: " + ruta);
      habilitaCarga = true;
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se cargo archivo en el sistema."));
    } catch (FileNotFoundException fnfe) {
      Logs.log.error("No se cargo el archivo al servidor.");
      Logs.log.error(ruta);
      Logs.log.error(fnfe.getStackTrace());
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se cargo el archivo. Contacte al equipo de sistemas."));
    } catch (IOException ioe) {
      Logs.log.error("No se cargo el archivo al servidor.");
      Logs.log.error(ruta);
      Logs.log.error(ioe.getStackTrace());
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se cargo el archivo. Contacte al equipo de sistemas."));
    }
  }

  // METODO QUE VALIDA EL ARCHIVO CON LAS DIRECCIONES DE LOS CREDITOS
  public void validarArchivo() {
    LectorArchivoDireccionesExcel lector = new LectorArchivoDireccionesExcel();
    String[] arreglo = lector.leerArchivoExcel(ruta);
    direccionesEncontradas = arreglo[0];
    color = arreglo[1];
    habilitaValidacionArchivo = true;
  }

  // METODO QUE VALIDA AUTOMATICAMENTE LAS DIRECCIONES QUE SEAN CORRECTAS
  public void validarAutomaticas() {
    ValidadorDirecciones validador = new ValidadorDirecciones();
    direccionesPorValidar = validador.validacionAutomatica(direccionesArchivo);
    habilitaValidacionAutomatica = true;
  }

  // METODO QUE PREPARA LOS COMBOBOX SEGUN LA COLONIA SELECCIONADA
  public void prepararDireccion() {
    listaColonias = coloniaDao.buscarPorCodigoPostal(direccionSeleccionada.getCp());
    if (!listaColonias.isEmpty()) {
      listaEstados = estadoDao.buscarTodo();
      listaMunicipios = municipioDao.buscarMunicipiosPorEstado(listaColonias.get(0).getMunicipio().getEstadoRepublica().getIdEstado());
      nuevaColonia.setCodigoPostal(direccionSeleccionada.getCp());
      nuevoMunicipio = listaColonias.get(0).getMunicipio();
      nuevoEstado = listaColonias.get(0).getMunicipio().getEstadoRepublica();
      for (int i = 0; i < (listaColonias.size()); i++) {
        if (direccionSeleccionada.getColonia().contains(listaColonias.get(i).getNombre())) {
          nuevaColonia = listaColonias.get(i);
        }
      }
    } else {
      listaColonias = coloniaDao.buscarPorCodigoPostal(direccionSeleccionada.getCp().substring(0, 3) + "00");
      listaEstados = estadoDao.buscarTodo();
      nuevaColonia.setCodigoPostal(direccionSeleccionada.getCp());
      listaMunicipios = municipioDao.buscarMunicipiosPorEstado(listaColonias.get(0).getMunicipio().getEstadoRepublica().getIdEstado());
      nuevoMunicipio = listaColonias.get(0).getMunicipio();
      nuevoEstado = listaColonias.get(0).getMunicipio().getEstadoRepublica();
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion.", "No existen colonias con este codigo postal. Elija una colonia de la lista o agregue una nueva."));
    }
  }

  // METODO QUE INSERTA UNA NUEVA COLONIA EN EL SISTEMA
  public void crearColonia() {
    if (!cpNuevaColonia.matches("(\\d)+")) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El codigo postal debe ser numerico."));
    } else if (!cpNuevaColonia.substring(0, 3).equals(direccionSeleccionada.getCp().substring(0, 3))) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El codigo postal introducido no pertenece a este municipio."));
    } else {
      Colonia c = new Colonia();
      c.setCodigoPostal(cpNuevaColonia);
      c.setMunicipio(municipioNuevaColonia);
      c.setNombre(nombreNuevaColonia);
      c.setTipo(tipoNuevaColonia);
      if (coloniaDao.insertar(c)) {
        listaColonias = coloniaDao.buscarPorCodigoPostal(cpNuevaColonia);
        nombreNuevaColonia = "";
        cpNuevaColonia = "";
        Logs.log.info("El administrador " + indexBean.getUsuario().getNombreLogin() + " agrego la colonia " + c.getTipo() + " " + c.getNombre());
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se creo la nueva colonia. Ahora valide la direccion."));
      } else {
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo agregar la nueva colonia. Contacte al equipo de sistemas."));
      }
    }
  }

  // METODO QUE MUESTRA LAS COLONIAS DEL MUNICIPIO AL QUE PERTENECE LA DIRECCION
  public void mostrarColonias() {
    listaColonias = coloniaDao.buscarColoniasPorMunicipio(nuevoMunicipio.getIdMunicipio());
  }

  //TEST METHOD
  public void prepararCombos() {
    tiposAsentamiento = coloniaDao.obtenerTiposColonia();
    estadoNuevaColonia = nuevoEstado;
    municipioNuevaColonia = nuevoMunicipio;
  }

  //TEST METHOD
  public void obtenerColoniaCompleta() {

  }

  // METODO QUE VALIDA LAS DIRECCIONES POR EL USUARIO
  public void validar() {
    Credito c = creditoDao.buscar(direccionSeleccionada.getNumeroCredito());
    if (c != null) {
      nuevaColonia = coloniaDao.buscar(nuevaColonia.getIdColonia());
      nuevoMunicipio = municipioDao.buscar(nuevoMunicipio.getIdMunicipio());
      nuevoEstado = estadoDao.buscar(nuevoEstado.getIdEstado());
      Direccion d = new Direccion();
      d.setExterior("S/N");
      // TO FIX:
      // HACER UNA FUNCION QUE REALICE LA GEOLOCALIZACION
      d.setLatitud(BigDecimal.ZERO);
      d.setLongitud(BigDecimal.ZERO);
      d.setCalle(direccionSeleccionada.getCalle());
      d.setColonia(nuevaColonia);
      d.setEstadoRepublica(nuevoEstado);
      d.setMunicipio(nuevoMunicipio);
      d.setSujeto(c.getDeudor().getSujeto());
      // TO FIX:
      // VERIFICAR SI LA DIRECCION YA EXISTE PARA ESE CREDITO, DE SER ASI YA NO INSERTARLA
      if (direccionDao.insertar(d) != null) {
        direccionesPorValidar.remove(direccionSeleccionada);
        nuevaColonia = new Colonia();
        nuevoMunicipio = new Municipio();
        nuevoEstado = new EstadoRepublica();
        Logs.log.info("El administrador " + indexBean.getUsuario().getNombreLogin() + " valido una direccion asociada al sujeto " + d.getSujeto().getNombreRazonSocial());
        GestionAutomatica ga = new GestionAutomatica();
        if (ga.generarGestionAutomatica("4DOMI", c, indexBean.getUsuario(), "SE VALIDA DIRECCION ASOCIADA AL DEUDOR " + d.getSujeto().getNombreRazonSocial())) {
          FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se ha validado la direccion"));
        } else {
          Logs.log.error("No se inserto la gestion automatica.");
        }
      } else {
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se valido la direccion. Contacte al equipo de sistemas"));
      }
    } else {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se valido la direccion. Contacte al equipo de sistemas"));
    }
  }

  // METODO QUE PREPARA LOS DATOS PARA ENVIAR EL CORREO DE LOCALIZACION
  public void preparaCorreo() {
    remitente = "eduardo.chavez@corporativodelrio.com";
    listaDestinatarios = Arrays.asList("lilia.delrio@corporativodelrio.com", "eduardo.chavez@corporativodelrio.com", "solinfo_ibr@corporativodelrio.com");
    destinatario = "solinfo_ibr@corporativodelrio.com";
    asunto = "SOLICITUD DE DATOS CREDITO " + direccionSeleccionada.getNumeroCredito();
    mensajeCorreo = "HOLA, SOLICITO SU APOYO PARA VALIDAR LA DIRECCION DE ESTE CREDITO DEBIDO A QUE: \n\nSALUDOS CORDIALES.";
  }

  // METODO QUE ENVIA UN CORREO PIDIENDO DATOS DE LOCALIZACION DE UNA CUENTA
  public void enviarCorreoLocalizacion() {
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
      mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
      mensaje.setSubject(asunto);
      MimeMultipart multiParte = new MimeMultipart();
      BodyPart texto = new MimeBodyPart();
      texto.setText(mensajeCorreo);
      multiParte.addBodyPart(texto);
      mensaje.setContent(multiParte);
      Transport t = sesion.getTransport("smtp");
      t.connect(remitente, "009-94-92");
      t.sendMessage(mensaje, mensaje.getAllRecipients());
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se envio el correo de localizacion."));
    } catch (Exception e) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se envio el correo de localizacion. Contacte al equipo de sistemas."));
      Logs.log.error("No se pudo enviar el correo de localizacion");
      Logs.log.error("Remitente: " + remitente + ", destinatario: " + destinatario);
      Logs.log.error(e.getStackTrace());
    }
  }
  
  // GETTERS & SETTERS
  public boolean isHabilitaCarga() {
    return habilitaCarga;
  }

  public void setHabilitaCarga(boolean habilitaCarga) {
    this.habilitaCarga = habilitaCarga;
  }

  public boolean isHabilitaValidacionArchivo() {
    return habilitaValidacionArchivo;
  }

  public void setHabilitaValidacionArchivo(boolean habilitaValidacionArchivo) {
    this.habilitaValidacionArchivo = habilitaValidacionArchivo;
  }

  public boolean isHabilitaValidacionAutomatica() {
    return habilitaValidacionAutomatica;
  }

  public void setHabilitaValidacionAutomatica(boolean habilitaValidacionAutomatica) {
    this.habilitaValidacionAutomatica = habilitaValidacionAutomatica;
  }

  public String getDireccionesEncontradas() {
    return direccionesEncontradas;
  }

  public void setDireccionesEncontradas(String direccionesEncontradas) {
    this.direccionesEncontradas = direccionesEncontradas;
  }

  public DireccionPorValidar getDireccionSeleccionada() {
    return direccionSeleccionada;
  }

  public void setDireccionSeleccionada(DireccionPorValidar direccionSeleccionada) {
    this.direccionSeleccionada = direccionSeleccionada;
  }

  public List<DireccionPorValidar> getDireccionesPorValidar() {
    return direccionesPorValidar;
  }

  public void setDireccionesPorValidar(List<DireccionPorValidar> direccionesPorValidar) {
    this.direccionesPorValidar = direccionesPorValidar;
  }

  public List<FilaDireccionExcel> getDireccionesArchivo() {
    return direccionesArchivo;
  }

  public void setDireccionesArchivo(List<FilaDireccionExcel> direccionesArchivo) {
    this.direccionesArchivo = direccionesArchivo;
  }

  public UploadedFile getArchivo() {
    return archivo;
  }

  public void setArchivo(UploadedFile archivo) {
    this.archivo = archivo;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public Colonia getNuevaColonia() {
    return nuevaColonia;
  }

  public void setNuevaColonia(Colonia nuevaColonia) {
    this.nuevaColonia = nuevaColonia;
  }

  public List<Colonia> getListaColonias() {
    return listaColonias;
  }

  public void setListaColonias(List<Colonia> listaColonias) {
    this.listaColonias = listaColonias;
  }

  public Municipio getNuevoMunicipio() {
    return nuevoMunicipio;
  }

  public void setNuevoMunicipio(Municipio nuevoMunicipio) {
    this.nuevoMunicipio = nuevoMunicipio;
  }

  public List<Municipio> getListaMunicipios() {
    return listaMunicipios;
  }

  public void setListaMunicipios(List<Municipio> listaMunicipios) {
    this.listaMunicipios = listaMunicipios;
  }

  public EstadoRepublica getNuevoEstado() {
    return nuevoEstado;
  }

  public void setNuevoEstado(EstadoRepublica nuevoEstado) {
    this.nuevoEstado = nuevoEstado;
  }

  public List<EstadoRepublica> getListaEstados() {
    return listaEstados;
  }

  public void setListaEstados(List<EstadoRepublica> listaEstados) {
    this.listaEstados = listaEstados;
  }

  public String getDireccionesValidadas() {
    return direccionesValidadas;
  }

  public void setDireccionesValidadas(String direccionesValidadas) {
    this.direccionesValidadas = direccionesValidadas;
  }

  public String getTipoNuevaColonia() {
    return tipoNuevaColonia;
  }

  public void setTipoNuevaColonia(String tipoNuevaColonia) {
    this.tipoNuevaColonia = tipoNuevaColonia;
  }

  public String getNombreNuevaColonia() {
    return nombreNuevaColonia;
  }

  public void setNombreNuevaColonia(String nombreNuevaColonia) {
    this.nombreNuevaColonia = nombreNuevaColonia;
  }

  public String getCpNuevaColonia() {
    return cpNuevaColonia;
  }

  public void setCpNuevaColonia(String cpNuevaColonia) {
    this.cpNuevaColonia = cpNuevaColonia;
  }

  public List<String> getTiposAsentamiento() {
    return tiposAsentamiento;
  }

  public void setTiposAsentamiento(List<String> tiposAsentamiento) {
    this.tiposAsentamiento = tiposAsentamiento;
  }

  public Municipio getMunicipioNuevaColonia() {
    return municipioNuevaColonia;
  }

  public void setMunicipioNuevaColonia(Municipio municipioNuevaColonia) {
    this.municipioNuevaColonia = municipioNuevaColonia;
  }

  public EstadoRepublica getEstadoNuevaColonia() {
    return estadoNuevaColonia;
  }

  public void setEstadoNuevaColonia(EstadoRepublica estadoNuevaColonia) {
    this.estadoNuevaColonia = estadoNuevaColonia;
  }

  public String getRemitente() {
    return remitente;
  }

  public void setRemitente(String remitente) {
    this.remitente = remitente;
  }

  public String getDestinatario() {
    return destinatario;
  }

  public void setDestinatario(String destinatario) {
    this.destinatario = destinatario;
  }

  public String getAsunto() {
    return asunto;
  }

  public void setAsunto(String asunto) {
    this.asunto = asunto;
  }

  public String getMensajeCorreo() {
    return mensajeCorreo;
  }

  public void setMensajeCorreo(String mensajeCorreo) {
    this.mensajeCorreo = mensajeCorreo;
  }

  public List<String> getListaDestinatarios() {
    return listaDestinatarios;
  }

  public void setListaDestinatarios(List<String> listaDestinatarios) {
    this.listaDestinatarios = listaDestinatarios;
  }

}
