/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.ActualizacionDAO;
import dao.ConceptoDevolucionDAO;
import dao.ContactoDAO;
import dao.ConvenioPagoDAO;
import dao.CreditoDAO;
import dao.DevolucionDAO;
import dao.DireccionDAO;
import dao.EmailDAO;
import dao.FacDAO;
import dao.GestionDAO;
import dao.GestorDAO;
import dao.HistorialDAO;
import dao.MarcajeDAO;
import dao.MotivoDevolucionDAO;
import dao.TelefonoDAO;
import dto.Actualizacion;
import dto.ConceptoDevolucion;
import dto.Contacto;
import dto.ConvenioPago;
import dto.Credito;
import dto.Devolucion;
import dto.Direccion;
import dto.Email;
import dto.Fac;
import dto.Gestion;
import dto.Gestor;
import dto.Historial;
import dto.MotivoDevolucion;
import dto.Telefono;
import impl.ActualizacionIMPL;
import impl.ConceptoDevolucionIMPL;
import impl.ContactoIMPL;
import impl.ConvenioPagoIMPL;
import impl.CreditoIMPL;
import impl.DevolucionIMPL;
import impl.DireccionIMPL;
import impl.EmailIMPL;
import impl.FacIMPL;
import impl.GestionIMPL;
import impl.GestorIMPL;
import impl.HistorialIMPL;
import impl.MarcajeIMPL;
import impl.MotivoDevolucionIMPL;
import impl.TelefonoIMPL;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import util.GestionAutomatica;
import util.constantes.Devoluciones;
import util.constantes.Marcajes;
import util.constantes.Perfiles;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "vistaCreditoBean")
@ViewScoped
public class VistaCreditoBean implements Serializable {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");
  CreditoActualBean creditoActualBean = (CreditoActualBean) elContext.getELResolver().getValue(elContext, null, "creditoActualBean");

  // VARIABLES DE CLASE
  private boolean habilitaUrgente;
  private boolean habilitaFacs;
  private boolean habilitaCredsRelacionados;
  private boolean habilitaHistorial;
  private String observaciones;
  private String numeroCreditos;
  private String calleNumero;
  private String coloniaMunicipio;
  private String estadoCP;
  private String telefono;
  private String telefonoPrincipal;
  private String correo;
  private String estatusUltimaGestion;
  private String fechaInicio;
  private String fechaFin;
  private String fup;
  private String fvp;
  private String fechaQuebranto;
  private String numeroCuenta;
  private float saldoVencido;
  private int mesesVencidos;
  private Credito creditoActual;
  private Credito creditoRelacionadoSeleccionado;
  private Gestor gestorSeleccionado;
  private ConceptoDevolucion conceptoSeleccionado;
  private MotivoDevolucion motivoSeleccionado;
  private Telefono telefonoSeleccionado;
  private Email correoSeleccionado;
  private final CreditoDAO creditoDao;
  private final DireccionDAO direccionDAO;
  private final TelefonoDAO telefonoDAO;
  private final EmailDAO emailDAO;
  private final ContactoDAO contactoDAO;
  private final HistorialDAO historialDao;
  private final GestionDAO gestionDao;
  private final GestorDAO gestorDao;
  private final ConvenioPagoDAO convenioPagoDao;
  private final ConceptoDevolucionDAO conceptoDevolucionDao;
  private final MotivoDevolucionDAO motivoDevolucionDao;
  private final DevolucionDAO devolucionDao;
  private final FacDAO facDao;
  private final MarcajeDAO marcajeDao;
  private final ActualizacionDAO actualizacionDao;
  private List<Gestion> listaGestiones;
  private List<Gestor> listaGestores;
  private List<Credito> creditosRelacionados;
  private List<Direccion> listaDirecciones;
  private List<Telefono> listaTelefonos;
  private List<Email> listaCorreos;
  private List<Contacto> listaContactos;
  private List<Historial> historial;
  private List<ConceptoDevolucion> listaConceptos;
  private List<MotivoDevolucion> listaMotivos;
  private List<Fac> actualizaciones;

  public VistaCreditoBean() {
    telefonoSeleccionado = new Telefono();
    correoSeleccionado = new Email();
    conceptoSeleccionado = new ConceptoDevolucion();
    motivoSeleccionado = new MotivoDevolucion();
    creditoActual = new Credito();
    creditoRelacionadoSeleccionado = new Credito();
    gestorSeleccionado = new Gestor();
    creditoDao = new CreditoIMPL();
    direccionDAO = new DireccionIMPL();
    telefonoDAO = new TelefonoIMPL();
    emailDAO = new EmailIMPL();
    contactoDAO = new ContactoIMPL();
    historialDao = new HistorialIMPL();
    gestionDao = new GestionIMPL();
    gestorDao = new GestorIMPL();
    devolucionDao = new DevolucionIMPL();
    convenioPagoDao = new ConvenioPagoIMPL();
    conceptoDevolucionDao = new ConceptoDevolucionIMPL();
    motivoDevolucionDao = new MotivoDevolucionIMPL();
    facDao = new FacIMPL();
    actualizacionDao = new ActualizacionIMPL();
    marcajeDao = new MarcajeIMPL();
    listaMotivos = new ArrayList();
    creditosRelacionados = new ArrayList();
    listaDirecciones = new ArrayList();
    historial = new ArrayList();
    listaGestiones = new ArrayList();
    listaGestores = new ArrayList();
    actualizaciones = new ArrayList();
    listaConceptos = conceptoDevolucionDao.obtenerConceptos();
    obtenerDatos();
  }

  // METODO QUE OBTENDRA TODOS LOS DATOS PRIMARIOS SEGUN EL CREDITO SELECCIONADO EN LA VISTA cuentas.xhtml
  public final void obtenerDatos() {
    creditoActual = creditoActualBean.getCreditoActual();
    // HABILITAR BOTON DE MARCAR URGENTE
    habilitaUrgente = creditoActual.getMarcaje().getIdMarcaje() == Marcajes.URGENTE;
    // SE OBTIENE EL NUMERO DE CUENTA DEL CREDITO
    if(creditoActual.getNumeroCuenta() == null){
      numeroCuenta = "N/D";
    }
    else{
      numeroCuenta = creditoActual.getNumeroCuenta();
    }
    // SE OBTIENE LA LISTA DE GESTORES PARA REASIGNAR
    listaGestores = gestorDao.buscarPorDespachoExceptoEste(indexBean.getUsuario().getDespacho().getIdDespacho(), creditoActual.getGestor().getIdGestor());
    // OBTENEMOS EL ID DEL SUJETO PARA TODAS LAS OPERACIONES
    int idSujeto = creditoActual.getDeudor().getSujeto().getIdSujeto();
    // OBTENER LA PRIMER DIRECCION DEL DEUDOR
    Direccion d;
    try {
      d = direccionDAO.buscarPorSujeto(idSujeto).get(0);
      calleNumero = d.getCalle() + ",";
      coloniaMunicipio = d.getColonia().getNombre() + ",  " + d.getMunicipio().getNombre() + ",";
      estadoCP = d.getEstadoRepublica().getNombre() + ",  C.P. " + d.getColonia().getCodigoPostal();
    } catch (Exception e) {
      Logs.log.error("No se pudo obtener la primer direccion del deudor.");
      Logs.log.error(e);
    }
    // OBTENEMOS LAS DIFERENTES FECHAS QUE SE REQUIEREN
    try {
      // OBTENER EL PRIMER TELEFONO DEL DEUDOR
      Telefono tel;
      tel = telefonoDAO.buscarPorSujeto(idSujeto).get(0);
      String fon = tel.getNumero();
      telefono = "(" + fon.substring(0, (fon.length()) - 7) + ") - " + fon.substring((fon.length()) - 7, fon.length() - 4) + " - " + fon.substring(fon.length() - 4, fon.length());
      // OBTENER EL PRIMER CORREO DEL DEUDOR
      Email mail;
      mail = emailDAO.buscarPorSujeto(idSujeto).get(0);
      correo = mail.getDireccion().toLowerCase();
    } catch (Exception e) {
      Logs.log.error("No se pudo obtener el primer email del deudor.");
      Logs.log.error(e);
    }
    // SE OBTIENEN LAS FECHAS DE INTERES DEL CREDITO
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    fechaInicio = df.format(creditoActual.getFechaInicio());
    fechaFin = df.format(creditoActual.getFechaFin());
    Actualizacion a = actualizacionDao.buscarUltimaActualizacion(creditoActual.getIdCredito());
    // FECHA ULTIMO PAGO
    if (a.getFechaUltimoPago() == null) {
      fup = "N/D";
    } else {
      fup = df.format(a.getFechaUltimoPago());
    }
    // FECHA ULTIMO VENCIMIENTO PAGADO
    if (a.getFechaUltimoVencimientoPagado() == null) {
      fvp = "N/D";
    } else {
      fvp = df.format(a.getFechaUltimoVencimientoPagado());
    }
    // FECHA DE QUEBRANTO
    if (creditoActual.getFechaQuebranto() == null) {
      fechaQuebranto = "N/D";
    } else {
      fechaQuebranto = df.format(creditoActual.getFechaQuebranto());
    }
    mesesVencidos = creditoDao.buscarMesesVencidosCredito(creditoActual.getIdCredito());
    saldoVencido = obtenerSaldoVencido(creditoActual.getIdCredito());
    // OBTENEMOS LA LISTA DE GESTIONES PREVIAS
    obtenerGestionesAnteriores();
    // OBTENEMOS LA LISTA DE LAS DIRECCIONES DE ESTE DEUDOR, SI ES QUE EXISTE TAL LISTA
    listaDirecciones = direccionDAO.buscarPorSujeto(idSujeto);
    // OBTENEMOS LA LISTA DE CREDITOS RELACIONADOS
    creditosRelacionados = creditoDao.buscarCreditosRelacionados(creditoActual.getIdCredito(), creditoActual.getDeudor().getNumeroDeudor());
    habilitaCredsRelacionados = !creditosRelacionados.isEmpty();
    // OBTENEMOS EL NUMERO DE CREDITOS PARA ESTE CLIENTE
    numeroCreditos = String.valueOf(creditosRelacionados.size() + 1);
    // OBTENEMOS LA LISTA DE TELEFONOS DEL DEUDOR
    listaTelefonos = telefonoDAO.buscarPorSujeto(idSujeto);
    // OBTENEMOS LA LISTA DE CORREOS ELECTRONICOS DEL DEUDOR
    listaCorreos = emailDAO.buscarPorSujeto(idSujeto);
    // OBTENEMOS LA LISTA DE CONTACTOS DEL DEUDOR
    listaContactos = contactoDAO.buscarContactoPorSujeto(idSujeto);
    // OBTENEMOS EL HISTORIAL DEL CREDITO
    historial = historialDao.buscarHistorialPorIdCredito(creditoActual.getIdCredito());
    habilitaHistorial = !historial.isEmpty();
    // OBTENEMOS LA LISTA DE ACTUALIZACIONES DEL CREDITO
    actualizaciones = facDao.buscarPorCredito(creditoActual.getIdCredito());
    habilitaFacs = !actualizaciones.isEmpty();
  }

  // METODO PARA ABRIR EL DIALOGO
  public void confirmar() {
    RequestContext.getCurrentInstance().execute("PF('confirmacionDialog2').show();");
  }

  // METODO PARA CERRAR EL DIALOGO
  public void cerrar() {
    gestorSeleccionado = creditoActual.getGestor();
    RequestContext.getCurrentInstance().update("barraDetalleCreditoForm:listaGestoresVistaCredito");
    RequestContext.getCurrentInstance().execute("PF('confirmacionDialog2').hide();");
  }

  // METODO QUE REASIGNARA AL GESTOR
  public void reasignarGestor() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    // GESTION AUTOMATICA 1
    // SI EXISTE UN CONVENIO
    ConvenioPago c = convenioPagoDao.buscarConvenioEnCursoCredito(creditoActual.getIdCredito());
    if (c != null) {
      GestionAutomatica.generarGestionAutomatica("13CONRE", creditoActual, indexBean.getUsuario(), "SE REASIGNA CONVENIO");
    }
    // CAMBIAMOS EL GESTOR ASIGNADO ACTUALMENTE
    Credito cred = creditoActual;
    Gestor nuevoGestor = gestorDao.buscar(gestorSeleccionado.getIdGestor());
    cred.setGestor(nuevoGestor);
    boolean ok = creditoDao.editar(cred);
    if (ok) {
      // GUARDAMOS EN EL LOG EL DETALLE DE LA REASIGNACION
      Logs.log.info("El administrador: " + indexBean.getUsuario().getNombreLogin() + " reasigno el credito del gestor " + creditoActual.getGestor().getUsuario().getNombreLogin() + " al gestor " + nuevoGestor.getUsuario().getNombreLogin());
      // ESCRIBIMOS EN EL HISTORIAL LA REASIGNACION
      Historial h = new Historial();
      h.setCredito(creditoActual);
      Date fecha = new Date();
      h.setFecha(fecha);
      String evento = "El administrador: " + indexBean.getUsuario().getNombreLogin() + " reasigno el credito al gestor: " + nuevoGestor.getUsuario().getNombreLogin();
      h.setEvento(evento);
      ok = ok & (historialDao.insertarHistorial(creditoActual.getIdCredito(), evento));
      if (ok) {
        // GESTION AUTOMATICA 2
        GestionAutomatica.generarGestionAutomatica("15CTARE", creditoActual, indexBean.getUsuario(), "REASIGNACION DE CREDITO NO. " + creditoActual.getNumeroCredito());
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se reasigno el credito."));
        creditoActual.setGestor(nuevoGestor);
      } else {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se reasigno el credito. Contacte al equipo de sistemas."));
      }
    } else {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se reasigno el credito. Contacte al equipo de sistemas."));
    }
    cerrar();
  }

  // METODO QUE LE DA UNA ETIQUETA AL MES DE LA ACTUALIZACION FAC
  public String etiquetarMes(int mes) {
    String cadena = "";
    switch (mes) {
      case 1:
        cadena = "Enero";
        break;
      case 2:
        cadena = "Febrero";
        break;
      case 3:
        cadena = "Marzo";
        break;
      case 4:
        cadena = "Abril";
        break;
      case 5:
        cadena = "Mayo";
        break;
      case 6:
        cadena = "Junio";
        break;
      case 7:
        cadena = "Julio";
        break;
      case 8:
        cadena = "Agosto";
        break;
      case 9:
        cadena = "Septiembre";
        break;
      case 10:
        cadena = "Octubre";
        break;
      case 11:
        cadena = "Noviembre";
        break;
      case 12:
        cadena = "Diciembre";
        break;
    }
    return cadena;
  }

  // METODO QUE OBTIENE LA LISTA DE GESTIONES
  public void obtenerGestionesAnteriores() {
    listaGestiones = gestionDao.buscarGestionesCredito(creditoActual.getIdCredito());
    if (!listaGestiones.isEmpty()) {
      estatusUltimaGestion = listaGestiones.get(0).getEstatusInformativo().getEstatus();
    }
  }

  // METODO QUE OBTIENE LOS MOTIVOS DE DEVOLUCION DEPENDIENDO DEL CONCEPTO SELECCIONADO
  public void preparaMotivos() {
    conceptoSeleccionado = conceptoDevolucionDao.buscarPorId(conceptoSeleccionado.getIdConceptoDevolucion());
    listaMotivos = motivoDevolucionDao.obtenerMotivosPorConcepto(conceptoSeleccionado.getIdConceptoDevolucion());
  }

  // METODO QUE MANDA UN CREDITO A DEVOLUCION
  public void devolverCredito() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    boolean ok = false;
    String evento;
    if ((conceptoSeleccionado.getIdConceptoDevolucion() != 0) && (motivoSeleccionado.getIdMotivoDevolucion() != 0)) {
      Devolucion d = new Devolucion();
      Date fecha = new Date();
      d.setFecha(fecha);
      conceptoSeleccionado = conceptoDevolucionDao.buscarPorId(conceptoSeleccionado.getIdConceptoDevolucion());
      d.setConceptoDevolucion(conceptoSeleccionado);
      motivoSeleccionado = motivoDevolucionDao.buscarPorId(motivoSeleccionado.getIdMotivoDevolucion());
      d.setMotivoDevolucion(motivoSeleccionado);
      d.setCredito(creditoActual);
      d.setObservaciones(observaciones);
      d.setSolicitante(indexBean.getUsuario().getNombreLogin());
      if (indexBean.getUsuario().getPerfil() != Perfiles.GESTOR) {
        d.setEstatus(Devoluciones.DEVUELTO);
        d.setRevisor(indexBean.getUsuario().getNombreLogin());
        evento = "El administrador: " + indexBean.getUsuario().getNombreLogin() + ", devolvio el credito";
        Logs.log.info(evento);
        ok = historialDao.insertarHistorial(creditoActual.getIdCredito(), evento);
      } else {
        d.setEstatus(Devoluciones.PENDIENTE);
      }
      ok = ok && (devolucionDao.insertar(d));
      if (ok) {
        RequestContext con = RequestContext.getCurrentInstance();
        con.execute("PF('dlgDevolucionVistaCredito').hide();");
        con.update("cuentas");
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se devolvio el credito seleccionado"));
      } else {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo devolver el credito. Contacte con el administrador de base de datos"));
      }
    }
  }

  // METODO PARA OBTENER EL SALDO VENCIDO
  public float obtenerSaldoVencido(int idCredito) {
    return creditoDao.buscarSaldoVencidoCredito(idCredito);
  }

  // METODO QUE PREPARA EL CREDITO RELACIONADO Y LO MUESTRA EN PANTALLA
  public void abrirCreditoRelacionado() {
    creditoActualBean.setCreditoActual(creditoRelacionadoSeleccionado);
    if (indexBean.getUsuario().getPerfil() == Perfiles.ADMINISTRADOR) {
      try {
        FacesContext.getCurrentInstance().getExternalContext().redirect("vistaCreditoAdmin.xhtml");
      } catch (IOException ioe) {
        Logs.log.error("No se pudo redirigir a la vista del credito del administrador");
        Logs.log.error(ioe);
      }
    } else {
      try {
        FacesContext.getCurrentInstance().getExternalContext().redirect("vistaCreditoGestor.xhtml");
      } catch (IOException ioe) {
        Logs.log.error("No se pudo redirigir a la vista del credito del gestor");
        Logs.log.error(ioe);
      }
    }
  }

  // METODO QUE MARCA UNA CUENTA COMO URGENTE
  public void marcarUrgente() {
    creditoActual.setMarcaje(marcajeDao.buscarMarcajePorId(Marcajes.URGENTE));
    if (creditoDao.editar(creditoActual)) {
      habilitaUrgente = true;
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se marco la cuenta como urgente."));
    } else {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se marco la cuenta. Contacte al equipo de sistemas."));
    }
  }

  // ***********************************************************************************************************************
  // ***********************************************************************************************************************
  // ***********************************************************************************************************************
  // GETTERS & SETTERS
  public String getNumeroCreditos() {
    return numeroCreditos;
  }

  public void setNumeroCreditos(String numeroCreditos) {
    this.numeroCreditos = numeroCreditos;
  }

  public String getCalleNumero() {
    return calleNumero;
  }

  public void setCalleNumero(String calleNumero) {
    this.calleNumero = calleNumero;
  }

  public String getColoniaMunicipio() {
    return coloniaMunicipio;
  }

  public void setColoniaMunicipio(String coloniaMunicipio) {
    this.coloniaMunicipio = coloniaMunicipio;
  }

  public String getEstadoCP() {
    return estadoCP;
  }

  public void setEstadoCP(String estadoCP) {
    this.estadoCP = estadoCP;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }

  public String getFechaInicio() {
    return fechaInicio;
  }

  public void setFechaInicio(String fechaInicio) {
    this.fechaInicio = fechaInicio;
  }

  public String getFechaFin() {
    return fechaFin;
  }

  public void setFechaFin(String fechaFin) {
    this.fechaFin = fechaFin;
  }

  public String getFup() {
    return fup;
  }

  public void setFup(String fup) {
    this.fup = fup;
  }

  public String getFvp() {
    return fvp;
  }

  public void setFvp(String fvp) {
    this.fvp = fvp;
  }

  public float getSaldoVencido() {
    return saldoVencido;
  }

  public void setSaldoVencido(float saldoVencido) {
    this.saldoVencido = saldoVencido;
  }

  public Credito getCreditoActual() {
    return creditoActual;
  }

  public void setCreditoActual(Credito creditoActual) {
    this.creditoActual = creditoActual;
  }

  public Gestor getGestorSeleccionado() {
    return gestorSeleccionado;
  }

  public void setGestorSeleccionado(Gestor gestorSeleccionado) {
    this.gestorSeleccionado = gestorSeleccionado;
  }

  public List<Gestion> getListaGestiones() {
    return listaGestiones;
  }

  public void setListaGestiones(List<Gestion> listaGestiones) {
    this.listaGestiones = listaGestiones;
  }

  public List<Gestor> getListaGestores() {
    return listaGestores;
  }

  public void setListaGestores(List<Gestor> listaGestores) {
    this.listaGestores = listaGestores;
  }

  public List<Credito> getCreditosRelacionados() {
    return creditosRelacionados;
  }

  public void setCreditosRelacionados(List<Credito> creditosRelacionados) {
    this.creditosRelacionados = creditosRelacionados;
  }

  public List<Direccion> getListaDirecciones() {
    return listaDirecciones;
  }

  public void setListaDirecciones(List<Direccion> listaDirecciones) {
    this.listaDirecciones = listaDirecciones;
  }

  public List<Telefono> getListaTelefonos() {
    return listaTelefonos;
  }

  public void setListaTelefonos(List<Telefono> listaTelefonos) {
    this.listaTelefonos = listaTelefonos;
  }

  public List<Email> getListaCorreos() {
    return listaCorreos;
  }

  public void setListaCorreos(List<Email> listaCorreos) {
    this.listaCorreos = listaCorreos;
  }

  public List<Contacto> getListaContactos() {
    return listaContactos;
  }

  public void setListaContactos(List<Contacto> listaContactos) {
    this.listaContactos = listaContactos;
  }

  public List<Historial> getHistorial() {
    return historial;
  }

  public void setHistorial(List<Historial> historial) {
    this.historial = historial;
  }

  public int getMesesVencidos() {
    return mesesVencidos;
  }

  public void setMesesVencidos(int mesesVencidos) {
    this.mesesVencidos = mesesVencidos;
  }

  public List<ConceptoDevolucion> getListaConceptos() {
    return listaConceptos;
  }

  public void setListaConceptos(List<ConceptoDevolucion> listaConceptos) {
    this.listaConceptos = listaConceptos;
  }

  public ConceptoDevolucion getConceptoSeleccionado() {
    return conceptoSeleccionado;
  }

  public void setConceptoSeleccionado(ConceptoDevolucion conceptoSeleccionado) {
    this.conceptoSeleccionado = conceptoSeleccionado;
  }

  public MotivoDevolucion getMotivoSeleccionado() {
    return motivoSeleccionado;
  }

  public void setMotivoSeleccionado(MotivoDevolucion motivoSeleccionado) {
    this.motivoSeleccionado = motivoSeleccionado;
  }

  public List<MotivoDevolucion> getListaMotivos() {
    return listaMotivos;
  }

  public void setListaMotivos(List<MotivoDevolucion> listaMotivos) {
    this.listaMotivos = listaMotivos;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public List<Fac> getActualizaciones() {
    return actualizaciones;
  }

  public void setActualizaciones(List<Fac> actualizaciones) {
    this.actualizaciones = actualizaciones;
  }

  public String getFechaQuebranto() {
    return fechaQuebranto;
  }

  public void setFechaQuebranto(String fechaQuebranto) {
    this.fechaQuebranto = fechaQuebranto;
  }

  public String getEstatusUltimaGestion() {
    return estatusUltimaGestion;
  }

  public void setEstatusUltimaGestion(String estatusUltimaGestion) {
    this.estatusUltimaGestion = estatusUltimaGestion;
  }

  public Credito getCreditoRelacionadoSeleccionado() {
    return creditoRelacionadoSeleccionado;
  }

  public void setCreditoRelacionadoSeleccionado(Credito creditoRelacionadoSeleccionado) {
    this.creditoRelacionadoSeleccionado = creditoRelacionadoSeleccionado;
  }

  public boolean isHabilitaUrgente() {
    return habilitaUrgente;
  }

  public void setHabilitaUrgente(boolean habilitaUrgente) {
    this.habilitaUrgente = habilitaUrgente;
  }

  public boolean isHabilitaFacs() {
    return habilitaFacs;
  }

  public void setHabilitaFacs(boolean habilitaFacs) {
    this.habilitaFacs = habilitaFacs;
  }

  public boolean isHabilitaCredsRelacionados() {
    return habilitaCredsRelacionados;
  }

  public void setHabilitaCredsRelacionados(boolean habilitaCredsRelacionados) {
    this.habilitaCredsRelacionados = habilitaCredsRelacionados;
  }

  public boolean isHabilitaHistorial() {
    return habilitaHistorial;
  }

  public void setHabilitaHistorial(boolean habilitaHistorial) {
    this.habilitaHistorial = habilitaHistorial;
  }

  public Telefono getTelefonoSeleccionado() {
    return telefonoSeleccionado;
  }

  public void setTelefonoSeleccionado(Telefono telefonoSeleccionado) {
    this.telefonoSeleccionado = telefonoSeleccionado;
  }

  public Email getCorreoSeleccionado() {
    return correoSeleccionado;
  }

  public void setCorreoSeleccionado(Email correoSeleccionado) {
    this.correoSeleccionado = correoSeleccionado;
  }

  public String getTelefonoPrincipal() {
    return telefonoPrincipal;
  }

  public void setTelefonoPrincipal(String telefonoPrincipal) {
    this.telefonoPrincipal = telefonoPrincipal;
  }

  public String getNumeroCuenta() {
    return numeroCuenta;
  }

  public void setNumeroCuenta(String numeroCuenta) {
    this.numeroCuenta = numeroCuenta;
  }

}
