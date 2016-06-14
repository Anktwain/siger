/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.ActualizacionDAO;
import dao.AjusteDAO;
import dao.ColoniaDAO;
import dao.ConceptoDevolucionDAO;
import dao.ContactoDAO;
import dao.ConvenioPagoDAO;
import dao.CreditoDAO;
import dao.DevolucionDAO;
import dao.DireccionDAO;
import dao.EmailDAO;
import dao.EstadoRepublicaDAO;
import dao.FacDAO;
import dao.GestionDAO;
import dao.GestorDAO;
import dao.HistorialDAO;
import dao.MarcajeDAO;
import dao.MotivoDevolucionDAO;
import dao.MunicipioDAO;
import dao.TelefonoDAO;
import dto.Actualizacion;
import dto.Ajuste;
import dto.Colonia;
import dto.ConceptoDevolucion;
import dto.Contacto;
import dto.ConvenioPago;
import dto.Credito;
import dto.Devolucion;
import dto.Direccion;
import dto.Email;
import dto.EstadoRepublica;
import dto.Fac;
import dto.Gestion;
import dto.Gestor;
import dto.Historial;
import dto.Impresion;
import dto.MotivoDevolucion;
import dto.Municipio;
import dto.QuienGestion;
import dto.Telefono;
import impl.ActualizacionIMPL;
import impl.AjusteIMPL;
import impl.ColoniaIMPL;
import impl.ConceptoDevolucionIMPL;
import impl.ContactoIMPL;
import impl.ConvenioPagoIMPL;
import impl.CreditoIMPL;
import impl.DevolucionIMPL;
import impl.DireccionIMPL;
import impl.EmailIMPL;
import impl.EstadoRepublicaIMPL;
import impl.FacIMPL;
import impl.GestionIMPL;
import impl.GestorIMPL;
import impl.HistorialIMPL;
import impl.ImpresionIMPL;
import impl.MarcajeIMPL;
import impl.MotivoDevolucionIMPL;
import impl.MunicipioIMPL;
import impl.QuienGestionIMPL;
import impl.TelefonoIMPL;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
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
import util.constantes.Ajustes;
import util.constantes.Correos;
import util.constantes.Devoluciones;
import util.constantes.Marcajes;
import util.constantes.Patrones;
import util.constantes.Perfiles;
import util.constantes.Telefonos;
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
  private boolean habilitaAjustes;
  private boolean principalNuevoTelefono;
  private boolean principalNuevoCorreo;
  private String color;
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
  private String ladaNuevoTelefono;
  private String nuevoTelefono;
  private String tipoNuevoTelefono;
  private String horarioNuevoTelefono;
  private String nuevoCorreo;
  private String tipoNuevoCorreo;
  private String nuevoContacto;
  private String tipoNuevoContacto;
  private String observacionesContacto;
  private String cpNuevaDireccion;
  private String calleNuevaDireccion;
  private String exteriorNuevaDireccion;
  private String interiorNuevaDireccion;
  private String latitudNuevaDireccion;
  private String longitudNuevaDireccion;
  private float saldoVencido;
  private int mesesVencidos;
  private Credito creditoActual;
  private Credito creditoRelacionadoSeleccionado;
  private Gestor gestorSeleccionado;
  private ConceptoDevolucion conceptoSeleccionado;
  private MotivoDevolucion motivoSeleccionado;
  private Telefono telefonoSeleccionado;
  private Email correoSeleccionado;
  private EstadoRepublica estadoNuevaDireccion;
  private Municipio municipioNuevaDireccion;
  private Colonia coloniaNuevaDireccion;
  private Direccion direccionSeleccionada;
  private final CreditoDAO creditoDao;
  private final DireccionDAO direccionDao;
  private final TelefonoDAO telefonoDao;
  private final EmailDAO emailDao;
  private final ContactoDAO contactoDao;
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
  private final EstadoRepublicaDAO estadoRepublicaDao;
  private final MunicipioDAO municipioDao;
  private final ColoniaDAO coloniaDao;
  private final AjusteDAO ajusteDao;
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
  private List<Ajuste> ajustes;
  private List<String> listaSujetos;
  private List<EstadoRepublica> listaEstados;
  private List<Municipio> listaMunicipios;
  private List<Colonia> listaColonias;

  public VistaCreditoBean() {
    telefonoSeleccionado = new Telefono();
    correoSeleccionado = new Email();
    conceptoSeleccionado = new ConceptoDevolucion();
    motivoSeleccionado = new MotivoDevolucion();
    creditoActual = new Credito();
    creditoRelacionadoSeleccionado = new Credito();
    gestorSeleccionado = new Gestor();
    estadoNuevaDireccion = new EstadoRepublica();
    municipioNuevaDireccion = new Municipio();
    coloniaNuevaDireccion = new Colonia();
    direccionSeleccionada = new Direccion();
    creditoDao = new CreditoIMPL();
    direccionDao = new DireccionIMPL();
    telefonoDao = new TelefonoIMPL();
    emailDao = new EmailIMPL();
    contactoDao = new ContactoIMPL();
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
    estadoRepublicaDao = new EstadoRepublicaIMPL();
    municipioDao = new MunicipioIMPL();
    coloniaDao = new ColoniaIMPL();
    ajusteDao = new AjusteIMPL();
    listaMotivos = new ArrayList();
    creditosRelacionados = new ArrayList();
    listaDirecciones = new ArrayList();
    historial = new ArrayList();
    listaGestiones = new ArrayList();
    listaGestores = new ArrayList();
    actualizaciones = new ArrayList();
    listaSujetos = new ArrayList();
    listaEstados = new ArrayList();
    listaMunicipios = new ArrayList();
    listaColonias = new ArrayList();
    ajustes = new ArrayList();
    obtenerDatos();
    inicializarDireccion();
  }

  // METODO QUE OBTENDRA TODOS LOS DATOS PRIMARIOS SEGUN EL CREDITO SELECCIONADO EN LA VISTA cuentas.xhtml
  public final void obtenerDatos() {
    latitudNuevaDireccion = "0.000000";
    longitudNuevaDireccion = "0.000000";
    listaConceptos = conceptoDevolucionDao.obtenerConceptos();
    listaEstados = estadoRepublicaDao.buscarTodo();
    creditoActual = creditoActualBean.getCreditoActual();
    checarColor();
    // HABILITAR BOTON DE MARCAR URGENTE
    habilitaUrgente = creditoActual.getMarcaje().getIdMarcaje() == Marcajes.URGENTE;
    if ((creditoActual.getNumeroCuenta() == null) || (creditoActual.getNumeroCuenta().length() == 0)) {
      numeroCuenta = "N/D";
    } else {
      //907 67
      numeroCuenta = creditoActual.getNumeroCuenta();
    }
    // SE OBTIENE LA LISTA DE GESTORES PARA REASIGNAR
    listaGestores = gestorDao.buscarPorDespachoExceptoEste(indexBean.getUsuario().getDespacho().getIdDespacho(), creditoActual.getGestor().getIdGestor());
    // OBTENEMOS EL ID DEL SUJETO PARA TODAS LAS OPERACIONES
    int idSujeto = creditoActual.getDeudor().getSujeto().getIdSujeto();
    // OBTENER LA PRIMER DIRECCION DEL DEUDOR
    Direccion d;
    try {
      d = direccionDao.buscarPorSujeto(idSujeto).get(0);
      if (d.getInterior() == null) {
        calleNumero = d.getCalle() + " " + d.getExterior();
      } else {
        calleNumero = d.getCalle() + " " + d.getExterior() + " " + d.getInterior();
      }
      coloniaMunicipio = d.getColonia().getTipo() + " " + d.getColonia().getNombre() + ",  " + d.getMunicipio().getNombre();
      estadoCP = d.getEstadoRepublica().getNombre() + ",  C.P. " + d.getColonia().getCodigoPostal();
    } catch (Exception e) {
      Logs.log.error("No se pudo obtener la primer direccion del deudor.");
      Logs.log.error(e);
    }
    // OBTENEMOS LAS DIFERENTES FECHAS QUE SE REQUIEREN
    try {
      // OBTENER EL PRIMER TELEFONO DEL DEUDOR
      telefono = formatoTelefono(telefonoDao.buscarPorSujeto(idSujeto).get(0));
    } catch (Exception e) {
      Logs.log.error("No se pudo obtener el primer telefono del deudor.");
      Logs.log.error(e);
    }
    // SE OBTIENE EL CORREO PRINCIPAL DEL DEUDOR
    Email mail = emailDao.buscarPrincipalPorSujeto(idSujeto);
    if (mail != null) {
      correo = mail.getDireccion().toLowerCase();
    } else {
      correo = "";
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
    listaDirecciones = direccionDao.buscarPorSujeto(idSujeto);
    // OBTENEMOS LA LISTA DE CREDITOS RELACIONADOS
    creditosRelacionados = creditoDao.buscarCreditosRelacionados(creditoActual.getIdCredito(), creditoActual.getDeudor().getNumeroDeudor());
    habilitaCredsRelacionados = !creditosRelacionados.isEmpty();
    // OBTENEMOS EL NUMERO DE CREDITOS PARA ESTE CLIENTE
    numeroCreditos = String.valueOf(creditosRelacionados.size() + 1);
    //OBTENEMOS EL TELEFONO PRINCIPAL DEL DEUDOR
    Telefono p = telefonoDao.buscarTelefonoPrincipal(creditoActual.getDeudor().getSujeto().getIdSujeto());
    if (p != null) {
      telefonoPrincipal = formatoTelefono(p);
    } else {
      telefonoPrincipal = "";
    }
    // OBTENEMOS LA LISTA DE TELEFONOS DEL DEUDOR
    listaTelefonos = telefonoDao.buscarPorCliente(creditoActual.getDeudor().getNumeroDeudor());
    // OBTENEMOS LA LISTA DE CORREOS ELECTRONICOS DEL DEUDOR
    listaCorreos = emailDao.buscarPorCliente(creditoActual.getDeudor().getNumeroDeudor());
    // OBTENEMOS LA LISTA DE CONTACTOS DEL DEUDOR
    listaContactos = contactoDao.buscarContactoPorCliente(creditoActual.getDeudor().getNumeroDeudor());
    // OBTENEMOS EL HISTORIAL DEL CREDITO
    historial = historialDao.buscarPorCredito(creditoActual.getIdCredito());
    habilitaHistorial = !historial.isEmpty();
    // OBTENEMOS LA LISTA DE ACTUALIZACIONES DEL CREDITO
    actualizaciones = facDao.buscarPorCredito(creditoActual.getIdCredito());
    habilitaFacs = !actualizaciones.isEmpty();
    // OBTENEMOS LA LISTA DE AJUSTES ACTUALES Y ANTERIORES
    ajustes = ajusteDao.buscarAjustesPorCredito(creditoActual.getIdCredito());
    habilitaAjustes = !ajustes.isEmpty();
    // SE OBTIENE LA LISTA DE SUJETOS QUE PODRIAN SER NUEVOS CONTACTOS
    List< QuienGestion> sujetos = new QuienGestionIMPL().buscarTodo();
    for (int i = 1; i < (sujetos.size()); i++) {
      if (sujetos.get(i).getTipoQuienGestion().getIdTipoQuienGestion() < 7) {
        listaSujetos.add(sujetos.get(i).getQuien());
      }
    }
  }

  // METODO QUE CHECA LA FRECUENCIA DE GESTION DE LA CUENTA
  public void checarColor() {
    int dias = gestionDao.checarDiasSinGestionar(creditoActual.getIdCredito()).intValue();
    if (dias <= 3) {
      color = "#04B404";
    }
    if ((dias > 3) && (dias <= 7)) {
      color = "#F3CE85";
    }
    if (dias > 7) {
      color = "#EC1010";
    }
  }

  // METODO QUE PREPARA LA DIRECCION QUE SE PRETENDERA EDITAR
  public final void inicializarDireccion() {
    direccionSeleccionada.setCalle("");
    direccionSeleccionada.setColonia(coloniaDao.buscar(1));
    direccionSeleccionada.setEstadoRepublica(estadoRepublicaDao.buscar(1));
    direccionSeleccionada.setExterior("");
    direccionSeleccionada.setIdDireccion(0);
    direccionSeleccionada.setInterior("");
    direccionSeleccionada.setLatitud(BigDecimal.ZERO);
    direccionSeleccionada.setLongitud(BigDecimal.ZERO);
    direccionSeleccionada.setMunicipio(municipioDao.buscar(1));
  }

  // METODO QUE DA FORMATO A LOS TELEFONOS
  public String formatoTelefono(Telefono tel) {
    String telefono;
    if (tel.getLada() == null) {
      telefono = tel.getNumero();
    } else {
      telefono = tel.getLada() + tel.getNumero();
    }
    if (telefono.length() >= 7) {
      return "(" + telefono.substring(0, (telefono.length()) - 7) + ") - " + telefono.substring((telefono.length()) - 7, telefono.length() - 4) + " - " + telefono.substring(telefono.length() - 4, telefono.length());
    } else if (telefono.length() > 7) {
      return telefono.substring(0, 2) + " - " + telefono.substring(3, 7);
    } else {
      return telefono;
    }
  }

  // METODO PARA ABRIR EL DIALOGO DE CONFIRMACION DE DEVOLUCION
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
      h.setFecha(new Date());
      h.setEvento("El administrador: " + indexBean.getUsuario().getNombreLogin() + " reasigno el credito al gestor: " + nuevoGestor.getUsuario().getNombreLogin());
      ok = ok & (historialDao.insertar(h));
      if (ok) {
        // GESTION AUTOMATICA 2
        GestionAutomatica.generarGestionAutomatica("15CTARE", creditoActual, indexBean.getUsuario(), "REASIGNACION DE CREDITO NO. " + creditoActual.getNumeroCredito());
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se reasigno el credito."));
        creditoActual.setGestor(nuevoGestor);
      } else {
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se reasigno el credito. Contacte al equipo de sistemas."));
      }
    } else {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se reasigno el credito. Contacte al equipo de sistemas."));
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

  // METODO QUE LE DA UNA ETIQUETA AL TIPO DE AJUSTE DE TELMEX
  public String etiquetarAjuste(int tipo) {
    String cadena = "";
    switch (tipo) {
      case Ajustes.ANTERIORES:
        cadena = "Anteriores";
        break;
      case Ajustes.ACTUALES:
        cadena = "Actuales";
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
    boolean ok = false;
    if ((conceptoSeleccionado.getIdConceptoDevolucion() != 0) && (motivoSeleccionado.getIdMotivoDevolucion() != 0)) {
      Devolucion d = new Devolucion();
      d.setFecha(new Date());
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
        String evento = "El administrador: " + indexBean.getUsuario().getNombreLogin() + ", devolvio el credito";
        Logs.log.info(evento);
        Historial h = new Historial();
        h.setCredito(creditoActual);
        h.setEvento(evento);
        h.setFecha(new Date());
        ok = historialDao.insertar(h);
      } else {
        d.setEstatus(Devoluciones.PENDIENTE);
      }
      ok = ok && (devolucionDao.insertar(d));
      if (ok) {
        RequestContext con = RequestContext.getCurrentInstance();
        con.execute("PF('dlgDevolucionVistaCredito').hide();");
        con.update("cuentas");
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se devolvio el credito seleccionado"));
      } else {
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo devolver el credito. Contacte con el administrador de base de datos"));
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

  // METODO QUE AGREGA UN TELEFONO A LA BASE DE DATOS
  public void agregarTelefono() {
    Telefono t = new Telefono();
    t.setSujeto(creditoActual.getDeudor().getSujeto());
    t.setNumero(nuevoTelefono);
    t.setLada(ladaNuevoTelefono);
    t.setTipo(tipoNuevoTelefono);
    t.setHorario(horarioNuevoTelefono);
    t = telefonoDao.insertar(t);
    if (t != null) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se agrego el nuevo telefono."));
      obtenerDatos();
      if (principalNuevoTelefono) {
        establecerTelefonoPrincipal(t);
      }
      nuevoTelefono = "";
      ladaNuevoTelefono = "";
      horarioNuevoTelefono = "";
      tipoNuevoTelefono = "";
      principalNuevoTelefono = false;
    } else {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se agrego el nuevo telefono. Contacte al equipo de sistemas."));
    }
  }

  // METODO QUE DETERMINA UN NUMERO TELEFONICO COMO PRINCIPAL
  public void establecerTelefonoPrincipal(Telefono telefono) {
    List<Telefono> telefonos = telefonoDao.buscarPorSujeto(creditoActual.getDeudor().getSujeto().getIdSujeto());
    boolean ok = true;
    for (int i = 0; i < (telefonos.size()); i++) {
      telefonos.get(i).setPrincipal(Telefonos.NORMAL);
      ok = ok & (telefonoDao.editar(telefonos.get(i)));
    }
    if (ok) {
      telefono.setPrincipal(Telefonos.PRINCIPAL);
      if (telefonoDao.editar(telefono)) {
        telefonoPrincipal = formatoTelefono(telefono);
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se establecio el telefono como principal."));
      } else {
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo establecer el telefono como principal. Contacte al equipo de sistemas."));
      }
    } else {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo establecer el telefono como principal. Contacte al equipo de sistemas."));
    }
  }

  // METODO QUE AGREGA UN EMAIL A LA BASE DE DATOS
  public void agregarEmail() {
    if (!nuevoCorreo.matches(Patrones.PATRON_EMAIL)) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "La direccion de correo proporcionada no tiene un formato valido."));
    } else {
      Email e = new Email();
      e.setSujeto(creditoActual.getDeudor().getSujeto());
      e.setDireccion(nuevoCorreo);
      e.setTipo(tipoNuevoCorreo);
      e = emailDao.insertar(e);
      if (e != null) {
        obtenerDatos();
        if (principalNuevoCorreo) {
          establecerCorreoPrincipal(e);
        }
        nuevoCorreo = "";
        tipoNuevoCorreo = "";
        principalNuevoCorreo = false;
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se agrego el nuevo correo."));
      } else {
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se agrego el nuevo correo. Contacte al equipo de sistemas."));
      }
    }
  }

  // METODO QUE DETERMINA UN CORREO ELECTRONICO COMO PRINCIPAL
  public void establecerCorreoPrincipal(Email c) {
    List<Email> correos = emailDao.buscarPorSujeto(creditoActual.getDeudor().getSujeto().getIdSujeto());
    boolean ok = true;
    for (int i = 0; i < (correos.size()); i++) {
      correos.get(i).setPrincipal(Correos.NORMAL);
      ok = ok & (emailDao.editar(correos.get(i)));
    }
    if (ok) {
      c.setPrincipal(Correos.PRINCIPAL);
      if (emailDao.editar(c)) {
        correo = c.getDireccion();
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se establecio el correo electronico como principal."));
      } else {
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo establecer el correo electronico como principal. Contacte al equipo de sistemas."));
      }
    } else {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo establecer el correo electronico como principal. Contacte al equipo de sistemas."));
    }
  }

  // METODO QUE AGREGA UN NUEVO CONTACTO
  public void agregarContacto() {
    if (nuevoContacto.equals("")) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "Debe escribir un nombre para el nuevo contacto."));
    } else if (tipoNuevoContacto.equals("")) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "Debe seleccionar el tipo de contacto."));
    } else {
      Contacto c = new Contacto();
      c.setDeudor(creditoActual.getDeudor());
      c.setSujeto(creditoActual.getDeudor().getSujeto());
      c.setNombre(nuevoContacto);
      c.setTipo(tipoNuevoContacto);
      c.setObservaciones(observacionesContacto);
      c = contactoDao.insertar(c);
      if (c != null) {
        obtenerDatos();
        listaContactos = contactoDao.buscarContactoPorSujeto(creditoActual.getDeudor().getSujeto().getIdSujeto());
        nuevoContacto = "";
        tipoNuevoContacto = "";
        observacionesContacto = "";
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se agrego el nuevo contacto."));
      } else {
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se agrego el nuevo contacto. Contacte al equipo de sistemas."));
      }
    }
  }

  // TEST METHOD
  public void actualizarColonia() {
    direccionSeleccionada.setColonia(coloniaDao.buscar(direccionSeleccionada.getColonia().getIdColonia()));
  }

  // METODO QUE PREPARA LAS LISTAS PARA LLENAR LOS COMBOS DE LA DIRECCION A EDITAR
  public void prepararDireccion() {
    listaMunicipios = municipioDao.buscarMunicipiosPorEstado(direccionSeleccionada.getEstadoRepublica().getIdEstado());
    listaColonias = coloniaDao.buscarColoniasPorMunicipio(direccionSeleccionada.getMunicipio().getIdMunicipio());
  }

  // METODO QUE GUARDA LOS CAMBIOS DE LA DIRECCION SELECCIONADA
  public void editarDireccion() {
    BigDecimal minLat = BigDecimal.valueOf(-90.000000);
    BigDecimal maxLat = BigDecimal.valueOf(90.000000);
    BigDecimal minLon = BigDecimal.valueOf(-180.000000);
    BigDecimal maxLon = BigDecimal.valueOf(180.000000);
    if (direccionSeleccionada.getCalle().equals("")) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "Debe ingresar una calle."));
    } else if (direccionSeleccionada.getExterior().equals("")) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "Debe ingresar un numero exterior."));
    } else if (!direccionSeleccionada.getLatitud().toString().matches("-?[0-9]{1,3}.[0-9]{6}")) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El valor de latitud ingresado no es valido."));
    } else if ((direccionSeleccionada.getLatitud().compareTo(minLat) == -1) || (direccionSeleccionada.getLatitud().compareTo(maxLat) == 1)) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El valor de latitud esta fuera de rango."));
    } else if (!direccionSeleccionada.getLongitud().toString().matches("-?[0-9]{1,3}.[0-9]{6}")) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El valor de longitud ingresado no es valido."));
    } else if ((direccionSeleccionada.getLongitud().compareTo(minLon) == -1) || (direccionSeleccionada.getLongitud().compareTo(maxLon) == 1)) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El valor de longitud esta fuera de rango."));
    } else {
      if (direccionDao.editar(direccionSeleccionada)) {
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se edito la direccion."));
      } else {
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se edito la direccion. Contacte al equipo de sistemas."));
      }
    }
  }

  // METODO QUE PREPARA LA DIRECCION DE ACUERDO AL CODIGO POSTAL INTRODUCIDO
  public void detectarDireccionPorCP() {
    if (!cpNuevaDireccion.matches("(\\d)+")) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El codigo postal se compone exclusivamente por numeros."));
    } else if (cpNuevaDireccion.length() < 5) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El codigo postal no tiene la longitud adecuada."));
    } else {
      listaColonias = coloniaDao.buscarPorCodigoPostal(cpNuevaDireccion);
      if (listaColonias.isEmpty()) {
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No existen colonias para el codigo postal proporcionado. Registre la direccion de forma normal."));
      } else {
        estadoNuevaDireccion = listaColonias.get(0).getMunicipio().getEstadoRepublica();
        listaMunicipios = municipioDao.buscarMunicipiosPorEstado(estadoNuevaDireccion.getIdEstado());
        municipioNuevaDireccion = listaColonias.get(0).getMunicipio();
        if (listaColonias.size() == 1) {
          coloniaNuevaDireccion = listaColonias.get(0);
          FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Colonia detectada.", "Se detecto la colonia."));
        } else {
          FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Colonia no detectada.", "Seleccione la colonia del listado disponible."));
        }
      }
    }
  }

  // METODO QUE PREPARA LA LISTA DE MUNICIPIOS DE ACUERDO AL ESTADO SELECCIONADO
  public void obtenerMunicipios(EstadoRepublica estado) {
    listaMunicipios = municipioDao.buscarMunicipiosPorEstado(estado.getIdEstado());
  }

  // METODO QUE PREPARA LA LISTA DE COLONIAS DE ACUERDO AL MUNICIPIO SELECCIONADO
  public void obtenerColonias(Municipio municipio) {
    listaColonias = coloniaDao.buscarColoniasPorMunicipio(municipio.getIdMunicipio());
  }

  // METODO QUE CAMBIA EL CODIGO POSTAL DEPENDIENDO DE LA SELECCION
  public void completarCp() {
    coloniaNuevaDireccion = coloniaDao.buscar(coloniaNuevaDireccion.getIdColonia());
    cpNuevaDireccion = coloniaNuevaDireccion.getCodigoPostal();
  }

  // METODO QUE INSERTA LA NUEVA DIRECCION
  public void agregarDireccion() {
    BigDecimal minLat = BigDecimal.valueOf(-90.000000);
    BigDecimal maxLat = BigDecimal.valueOf(90.000000);
    BigDecimal minLon = BigDecimal.valueOf(-180.000000);
    BigDecimal maxLon = BigDecimal.valueOf(180.000000);
    if (!cpNuevaDireccion.matches("(\\d)+")) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El codigo postal se compone exclusivamente por numeros."));
    } else if (cpNuevaDireccion.length() < 5) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El codigo postal no tiene la longitud adecuada."));
    } else if (estadoNuevaDireccion.getIdEstado() == 0) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "Debe seleccionar un estado."));
    } else if (municipioNuevaDireccion.getIdMunicipio() == 0) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "Debe seleccionar un municipio."));
    } else if (coloniaNuevaDireccion.getIdColonia() == 0) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "Debe seleccionar una colonia."));
    } else if (calleNuevaDireccion.equals("")) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "Debe ingresar una calle."));
    } else if (exteriorNuevaDireccion.equals("")) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "Debe ingresar un numero exterior."));
    } else if (!latitudNuevaDireccion.matches("-?[0-9]{1,3}.[0-9]{6}")) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El valor de latitud ingresado no es valido."));
    } else if ((BigDecimal.valueOf(Double.valueOf(latitudNuevaDireccion)).compareTo(minLat) == -1) || (BigDecimal.valueOf(Double.valueOf(latitudNuevaDireccion)).compareTo(maxLat) == 1)) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El valor de latitud esta fuera de rango."));
    } else if (!longitudNuevaDireccion.matches("-?[0-9]{1,3}.[0-9]{6}")) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El valor de longitud ingresado no es valido."));
    } else if ((BigDecimal.valueOf(Double.valueOf(longitudNuevaDireccion)).compareTo(minLon) == -1) || (BigDecimal.valueOf(Double.valueOf(longitudNuevaDireccion)).compareTo(maxLon) == 1)) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El valor de longitud esta fuera de rango."));
    } else {
      Direccion d = new Direccion();
      d.setSujeto(creditoActual.getDeudor().getSujeto());
      d.setEstadoRepublica(estadoNuevaDireccion);
      d.setMunicipio(municipioNuevaDireccion);
      d.setColonia(coloniaNuevaDireccion);
      d.setCalle(calleNuevaDireccion);
      d.setExterior(exteriorNuevaDireccion);
      d.setInterior(interiorNuevaDireccion);
      if (latitudNuevaDireccion.equals("")) {
        d.setLatitud(BigDecimal.ZERO);
      } else {
        d.setLatitud(BigDecimal.valueOf(Double.valueOf(latitudNuevaDireccion)));
      }
      if (longitudNuevaDireccion.equals("")) {
        d.setLongitud(BigDecimal.ZERO);
      } else {
        d.setLongitud(BigDecimal.valueOf(Double.valueOf(longitudNuevaDireccion)));
      }
      if (direccionDao.insertar(d) != null) {
        listaDirecciones = direccionDao.buscarPorSujeto(creditoActual.getDeudor().getSujeto().getIdSujeto());
        cpNuevaDireccion = "";
        calleNuevaDireccion = "";
        exteriorNuevaDireccion = "";
        interiorNuevaDireccion = "";
        latitudNuevaDireccion = "0.000000";
        longitudNuevaDireccion = "0.000000";
        coloniaNuevaDireccion.setIdColonia(0);
        municipioNuevaDireccion.setIdMunicipio(0);
        estadoNuevaDireccion.setIdEstado(0);
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se registro la nueva direccion."));
      } else {
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se registro la nueva direccion. Contacte al equipo de sistemas."));
      }
    }
  }

  // METODO QUE ELIMINA LA DIRECCION SELECCIONADA
  public void eliminarDireccion() {
    List<Impresion> impresiones = new ImpresionIMPL().buscarPorDireccion(direccionSeleccionada.getIdDireccion());
    if (!impresiones.isEmpty()) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error.", "No se puede eliminar la direccion ya que existen impresiones asociadas a esta."));
    } else {
      if (direccionDao.eliminar(direccionSeleccionada)) {
        listaDirecciones = direccionDao.buscarPorSujeto(creditoActual.getDeudor().getSujeto().getIdSujeto());
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se elimino la direccion."));
      } else {
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se elimino la direccion. Contacte al equipo de sistemas"));
      }
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

  public boolean isPrincipalNuevoTelefono() {
    return principalNuevoTelefono;
  }

  public void setPrincipalNuevoTelefono(boolean principalNuevoTelefono) {
    this.principalNuevoTelefono = principalNuevoTelefono;
  }

  public String getLadaNuevoTelefono() {
    return ladaNuevoTelefono;
  }

  public void setLadaNuevoTelefono(String ladaNuevoTelefono) {
    this.ladaNuevoTelefono = ladaNuevoTelefono;
  }

  public String getNuevoTelefono() {
    return nuevoTelefono;
  }

  public void setNuevoTelefono(String nuevoTelefono) {
    this.nuevoTelefono = nuevoTelefono;
  }

  public String getTipoNuevoTelefono() {
    return tipoNuevoTelefono;
  }

  public void setTipoNuevoTelefono(String tipoNuevoTelefono) {
    this.tipoNuevoTelefono = tipoNuevoTelefono;
  }

  public String getHorarioNuevoTelefono() {
    return horarioNuevoTelefono;
  }

  public void setHorarioNuevoTelefono(String horarioNuevoTelefono) {
    this.horarioNuevoTelefono = horarioNuevoTelefono;
  }

  public boolean isPrincipalNuevoCorreo() {
    return principalNuevoCorreo;
  }

  public void setPrincipalNuevoCorreo(boolean principalNuevoCorreo) {
    this.principalNuevoCorreo = principalNuevoCorreo;
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

  public String getNuevoContacto() {
    return nuevoContacto;
  }

  public void setNuevoContacto(String nuevoContacto) {
    this.nuevoContacto = nuevoContacto;
  }

  public String getTipoNuevoContacto() {
    return tipoNuevoContacto;
  }

  public void setTipoNuevoContacto(String tipoNuevoContacto) {
    this.tipoNuevoContacto = tipoNuevoContacto;
  }

  public List<String> getListaSujetos() {
    return listaSujetos;
  }

  public void setListaSujetos(List<String> listaSujetos) {
    this.listaSujetos = listaSujetos;
  }

  public String getObservacionesContacto() {
    return observacionesContacto;
  }

  public void setObservacionesContacto(String observacionesContacto) {
    this.observacionesContacto = observacionesContacto;
  }

  public String getCpNuevaDireccion() {
    return cpNuevaDireccion;
  }

  public void setCpNuevaDireccion(String cpNuevaDireccion) {
    this.cpNuevaDireccion = cpNuevaDireccion;
  }

  public String getCalleNuevaDireccion() {
    return calleNuevaDireccion;
  }

  public void setCalleNuevaDireccion(String calleNuevaDireccion) {
    this.calleNuevaDireccion = calleNuevaDireccion;
  }

  public String getExteriorNuevaDireccion() {
    return exteriorNuevaDireccion;
  }

  public void setExteriorNuevaDireccion(String exteriorNuevaDireccion) {
    this.exteriorNuevaDireccion = exteriorNuevaDireccion;
  }

  public String getInteriorNuevaDireccion() {
    return interiorNuevaDireccion;
  }

  public void setInteriorNuevaDireccion(String interiorNuevaDireccion) {
    this.interiorNuevaDireccion = interiorNuevaDireccion;
  }

  public String getLatitudNuevaDireccion() {
    return latitudNuevaDireccion;
  }

  public void setLatitudNuevaDireccion(String latitudNuevaDireccion) {
    this.latitudNuevaDireccion = latitudNuevaDireccion;
  }

  public String getLongitudNuevaDireccion() {
    return longitudNuevaDireccion;
  }

  public void setLongitudNuevaDireccion(String longitudNuevaDireccion) {
    this.longitudNuevaDireccion = longitudNuevaDireccion;
  }

  public List<EstadoRepublica> getListaEstados() {
    return listaEstados;
  }

  public void setListaEstados(List<EstadoRepublica> listaEstados) {
    this.listaEstados = listaEstados;
  }

  public List<Municipio> getListaMunicipios() {
    return listaMunicipios;
  }

  public void setListaMunicipios(List<Municipio> listaMunicipios) {
    this.listaMunicipios = listaMunicipios;
  }

  public List<Colonia> getListaColonias() {
    return listaColonias;
  }

  public void setListaColonias(List<Colonia> listaColonias) {
    this.listaColonias = listaColonias;
  }

  public EstadoRepublica getEstadoNuevaDireccion() {
    return estadoNuevaDireccion;
  }

  public void setEstadoNuevaDireccion(EstadoRepublica estadoNuevaDireccion) {
    this.estadoNuevaDireccion = estadoNuevaDireccion;
  }

  public Municipio getMunicipioNuevaDireccion() {
    return municipioNuevaDireccion;
  }

  public void setMunicipioNuevaDireccion(Municipio municipioNuevaDireccion) {
    this.municipioNuevaDireccion = municipioNuevaDireccion;
  }

  public Colonia getColoniaNuevaDireccion() {
    return coloniaNuevaDireccion;
  }

  public void setColoniaNuevaDireccion(Colonia coloniaNuevaDireccion) {
    this.coloniaNuevaDireccion = coloniaNuevaDireccion;
  }

  public Direccion getDireccionSeleccionada() {
    return direccionSeleccionada;
  }

  public void setDireccionSeleccionada(Direccion direccionSeleccionada) {
    this.direccionSeleccionada = direccionSeleccionada;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public List<Ajuste> getAjustes() {
    return ajustes;
  }

  public void setAjustes(List<Ajuste> ajustes) {
    this.ajustes = ajustes;
  }

  public boolean isHabilitaAjustes() {
    return habilitaAjustes;
  }

  public void setHabilitaAjustes(boolean habilitaAjustes) {
    this.habilitaAjustes = habilitaAjustes;
  }

}
