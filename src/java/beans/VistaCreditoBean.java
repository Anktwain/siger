/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.ContactoDAO;
import dao.ConvenioPagoDAO;
import dao.CreditoDAO;
import dao.DireccionDAO;
import dao.EmailDAO;
import dao.GestionDAO;
import dao.GestorDAO;
import dao.HistorialDAO;
import dao.TelefonoDAO;
import dto.ConvenioPago;
import dto.tablas.Creditos;
import dto.Credito;
import dto.Direccion;
import dto.Email;
import dto.Gestion;
import dto.Gestor;
import dto.Historial;
import dto.Telefono;
import dto.tablas.Contactos;
import dto.tablas.Direcciones;
import impl.ContactoIMPL;
import impl.ConvenioPagoIMPL;
import impl.CreditoIMPL;
import impl.DireccionIMPL;
import impl.EmailIMPL;
import impl.GestionIMPL;
import impl.GestorIMPL;
import impl.HistorialIMPL;
import impl.TelefonoIMPL;
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
import util.constantes.Convenios;
import util.constantes.Perfiles;

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
  CuentasVistaBean cuentasVistaBean = (CuentasVistaBean) elContext.getELResolver().getValue(elContext, null, "cuentasVistaBean");

  // VARIABLES DE CLASE
  private boolean reasignarVisible;
  private boolean conveniosVisible;
  private String nombreDeudor;
  private String numeroCredito;
  private String numeroCreditos;
  private String calleNumero;
  private String coloniaMunicipio;
  private String estadoCP;
  private String telefono;
  private String correo;
  private String fechaInicio;
  private String fechaFin;
  private String fup;
  private String fvp;
  private String mensualidad;
  private String saldoVencido;
  private Creditos creditoActualCred;
  private Credito creditoActual;
  private Gestor gestorSeleccionado;
  private Gestor gestorActual;
  private CreditoDAO creditoDao;
  private DireccionDAO direccionDAO;
  private TelefonoDAO telefonoDAO;
  private EmailDAO emailDAO;
  private ContactoDAO contactoDAO;
  private HistorialDAO historialDao;
  private GestionDAO gestionDao;
  private GestorDAO gestorDao;
  private ConvenioPagoDAO convenioPagoDao;
  private List<Gestion> listaGestiones;
  private List<Gestor> listaGestores;
  private List<Credito> creditosRelacionados;
  private List<Creditos> credsRelacionados;
  private List<Direcciones> listaDirecciones;
  private List<Telefono> listaTelefonos;
  private List<Email> listaCorreos;
  private List<Contactos> listaContactos;
  private List<Historial> historial;

  public VistaCreditoBean() {
    creditoActualCred = new Creditos();
    creditoActual = new Credito();
    gestorSeleccionado = new Gestor();
    gestorActual = new Gestor();
    creditoDao = new CreditoIMPL();
    direccionDAO = new DireccionIMPL();
    telefonoDAO = new TelefonoIMPL();
    emailDAO = new EmailIMPL();
    contactoDAO = new ContactoIMPL();
    historialDao = new HistorialIMPL();
    gestionDao = new GestionIMPL();
    gestorDao = new GestorIMPL();
    convenioPagoDao = new ConvenioPagoIMPL();
    creditosRelacionados = new ArrayList();
    credsRelacionados = new ArrayList();
    listaDirecciones = new ArrayList();
    historial = new ArrayList();
    listaGestiones = new ArrayList();
    listaGestores = new ArrayList();
    creditoActualCred = cuentasVistaBean.getCreditoSeleccionado().get(0);
    obtenerDatos();
  }
  
  // METODO QUE OBTENDRA TODOS LOS DATOS PRIMARIOS SEGUN EL CREDITO SELECCIONADO EN LA VISTA cuentas.xhtml
  private void obtenerDatos() {
    // OBTIENE LA CADENA CON EL NUMERO DE CREDITO
    numeroCredito = creditoActualCred.getNumeroCredito();
    // OBTENEMOS EL CREDITO RELACIONADO
    creditoActual = creditoDao.buscarCreditoPorId(creditoDao.obtenerIdDelCredito(numeroCredito));
    // BUSCAMOS EL GESTOR ACTUAL DEL CREDITO
    gestorActual = gestorDao.buscarGestorDelCredito(creditoActual.getIdCredito());
    // OBTENEMOS EL ID DEL SUJETO PARA TODAS LAS OPERACIONES
    int idSujeto = creditoActual.getDeudor().getSujeto().getIdSujeto();
    // OBTIENE LA CADENA CON EL NOMBRE DEL DEUDOR
    nombreDeudor = creditoActualCred.getNombreRazonSocial();
    // OBTENER LA PRIMER DIRECCION DEL DEUDOR
    Direccion d;
    try{
    d = direccionDAO.buscarPorSujeto(idSujeto).get(0);
    calleNumero = d.getCalle() + ",";
    coloniaMunicipio = d.getColonia().getNombre() + ",  " + d.getMunicipio().getNombre() + ",";
    estadoCP = d.getEstadoRepublica().getNombre() + ",  C.P. " + d.getColonia().getCodigoPostal();
    } catch (Exception e) {
    }
    // OBTENEMOS EL NUMERO DE CREDITOS PARA ESTE CLIENTE
    numeroCreditos = Integer.toString(creditoDao.buscarCreditosRelacionados(creditoActual).size() + 1);
    // OBTENEMOS LAS DIFERENTES FECHAS QUE SE REQUIEREN
    try {
      // OBTENER EL PRIMER TELEFONO DEL DEUDOR
      Telefono tel;
      tel = telefonoDAO.buscarPorSujeto(idSujeto).get(0);
      telefono = "(" + tel.getLada() + ") " + tel.getNumero();
      // OBTENER EL PRIMER CORREO DEL DEUDOR
      Email mail;
      mail = emailDAO.buscarPorSujeto(idSujeto).get(0);
      correo = mail.getDireccion();
      DateFormat df = new SimpleDateFormat("dd de MM del yyyy");
      fechaInicio = df.format(creditoActual.getFechaInicio());
      fechaFin = df.format(creditoActual.getFechaFin());
      fup = "";
      fvp = "";
    } catch (Exception e) {
    }
    mensualidad = creditoActual.getMensualidad().toString();
    saldoVencido = "";
    // OBTENEMOS LA LISTA DE GESTIONES PREVIAS
    int idCredito = creditoActual.getIdCredito();
    // LOS ADMINISTRADORES NO PUEDEN REALIZAR CONVENIOS DE PAGO
    // LOS GESTORES NO PUEDEN REASIGNAR CREDITOS
    if(indexBean.getUsuario().getPerfil() == Perfiles.GESTOR){
      int idUsuario = indexBean.getUsuario().getIdUsuario();
      listaGestiones = gestionDao.buscarGestionesCreditoGestor(idUsuario, idCredito);
      // NO SE VISUALIZA LA REASIGNACION DE GESTOR
      reasignarVisible = false;
      conveniosVisible = true;
    }
    else{
      listaGestiones = gestionDao.buscarGestionesCredito(idCredito);
      listaGestores = gestorDao.buscarPorDespachoExceptoEste(indexBean.getUsuario().getDespacho().getIdDespacho(), gestorActual.getIdGestor());
      reasignarVisible = true;
      conveniosVisible = false;
    }
    // OBTENEMOS LA LISTA DE LAS DIRECCIONES DE ESTE DEUDOR, SI ES QUE EXISTE TAL LISTA
    List<Direccion> listaDireccionesSinNormalizar;
    listaDireccionesSinNormalizar = direccionDAO.buscarPorSujeto(idSujeto);
    int tam = listaDireccionesSinNormalizar.size();
    if (tam > 0) {
      for (int i = 0; i < tam; i++) {
        Direcciones oneDirection = new Direcciones();
        Direccion vieja;
        vieja = listaDireccionesSinNormalizar.get(i);
        oneDirection.setCalleNumero(vieja.getCalle());
        oneDirection.setColonia(vieja.getColonia().getNombre());
        oneDirection.setMunicipio(vieja.getMunicipio().getNombre());
        oneDirection.setEstado(vieja.getEstadoRepublica().getNombre());
        oneDirection.setCp(vieja.getColonia().getCodigoPostal());
        listaDirecciones.add(oneDirection);
      }
    }
    // OBTENEMOS LA LISTA DE CREDITOS RELACIONADOS
    creditosRelacionados = creditoDao.buscarCreditosRelacionados(creditoActual);
    tam = creditosRelacionados.size();
    if (tam > 0) {
      for (int i = 0; i < tam; i++) {
        Creditos c = new Creditos();
        Credito viejo;
        viejo = creditosRelacionados.get(i);
        c.setNumeroCredito(viejo.getNumeroCredito());
        // CUANDO SE DEFINAN LOS TIPOS DE CREDITOS, SE QUITARA LA ASIGNACION DIRECTA
        c.setTipoCredito("Linea telefonica");
        c.setNombreProducto(viejo.getProducto().getNombre());
        c.setSaldoVencido(viejo.getMonto());
        credsRelacionados.add(c);
      }
    }
    // OBTENEMOS LA LISTA DE TELEFONOS DEL DEUDOR
    listaTelefonos = telefonoDAO.buscarPorSujeto(idSujeto);
    // OBTENEMOS LA LISTA DE CORREOS ELECTRONICOS DEL DEUDOR
    listaCorreos = emailDAO.buscarPorSujeto(idSujeto);
    // OBTENEMOS LA LISTA DE CONTACTOS DEL DEUDOR
    listaContactos = contactoDAO.buscarContactoPorSujeto(idSujeto);
    // OBTENEMOS EL HISTORIAL DEL CREDITO
    historial = historialDao.buscarHistorialPorIdCredito(creditoActual.getIdCredito());
  }
  
  // METODO PARA ABRIR EL DIALOGO
  public void confirmar(){
    RequestContext.getCurrentInstance().execute("PF('confirmacionDialog2').show();");
  }
  
  // METODO PARA CERRAR EL DIALOGO
  public void cerrar(){
    gestorSeleccionado = gestorActual;
    RequestContext.getCurrentInstance().update("barraDetalleCreditoForm:listaGestoresVistaCredito");
    RequestContext.getCurrentInstance().execute("PF('confirmacionDialog2').hide();");
  }

  // METODO QUE REASIGNARA AL GESTOR
  public void reasignarGestor(){
    FacesContext contexto = FacesContext.getCurrentInstance();
    // FINALIZAMOS LOS CONVENIOS EN CURSO DEL GESTOR ACTUAL PARA ESTE CREDITO
    List<ConvenioPago> convenios = convenioPagoDao.buscarConveniosEnCursoCredito(creditoActual.getIdCredito());
    boolean ok = true;
    for (int i = 0; i < convenios.size(); i++) {
      ConvenioPago c = convenios.get(i);
      c.setEstatus(Convenios.FINALIZADO);
      ok = convenioPagoDao.editar(c);
    }
    // CAMBIAMOS EL GESTOR ASIGNADO ACTUALMENTE
    Credito cred = creditoActual;
    Gestor nuevoGestor = gestorDao.buscar(gestorSeleccionado.getIdGestor());
    cred.setGestor(nuevoGestor);
    ok = ok & (creditoDao.editar(cred));
    // ESCRIBIMOS EN EL HISTORIAL LA REASIGNACION
    Historial h = new Historial();
    h.setCredito(creditoActual);
    Date fecha = new Date();
    h.setFecha(fecha);
    String evento = "El administrador: " + indexBean.getUsuario().getNombreLogin() + " reasigno el credito del gestor " + gestorActual.getUsuario().getNombreLogin() + " al gestor " + nuevoGestor.getUsuario().getNombreLogin();
    h.setEvento(evento);
    ok = ok & (historialDao.insertarHistorial(creditoActual.getIdCredito(), evento));
    if(ok){
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se reasigno el credito."));
      gestorActual = nuevoGestor;
    }
    else{
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se reasigno el credito. Contacte al equipo de sistemas."));
    }
    cerrar();
  }
  
  // ***********************************************************************************************************************
  // ***********************************************************************************************************************
  // ***********************************************************************************************************************
  // GETTERS & SETTERS
  public ELContext getElContext() {
    return elContext;
  }

  public void setElContext(ELContext elContext) {
    this.elContext = elContext;
  }

  public CuentasVistaBean getCuentasVistaBean() {
    return cuentasVistaBean;
  }

  public void setCuentasVistaBean(CuentasVistaBean cuentasVistaBean) {
    this.cuentasVistaBean = cuentasVistaBean;
  }

  public String getNombreDeudor() {
    return nombreDeudor;
  }

  public void setNombreDeudor(String nombreDeudor) {
    this.nombreDeudor = nombreDeudor;
  }

  public String getNumeroCredito() {
    return numeroCredito;
  }

  public void setNumeroCredito(String numeroCredito) {
    this.numeroCredito = numeroCredito;
  }

  public Creditos getCreditoActualCred() {
    return creditoActualCred;
  }

  public void setCreditoActualCred(Creditos creditoActualCred) {
    this.creditoActualCred = creditoActualCred;
  }

  public Credito getCreditoActual() {
    return creditoActual;
  }

  public void setCreditoActual(Credito creditoActual) {
    this.creditoActual = creditoActual;
  }

  public CreditoDAO getCreditoDao() {
    return creditoDao;
  }

  public void setCreditoDao(CreditoDAO creditoDao) {
    this.creditoDao = creditoDao;
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

  public String getMensualidad() {
    return mensualidad;
  }

  public void setMensualidad(String mensualidad) {
    this.mensualidad = mensualidad;
  }

  public String getSaldoVencido() {
    return saldoVencido;
  }

  public void setSaldoVencido(String saldoVencido) {
    this.saldoVencido = saldoVencido;
  }

  public String getNumeroCreditos() {
    return numeroCreditos;
  }

  public void setNumeroCreditos(String numeroCreditos) {
    this.numeroCreditos = numeroCreditos;
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

  public DireccionDAO getDireccionDAO() {
    return direccionDAO;
  }

  public void setDireccionDAO(DireccionDAO direccionDAO) {
    this.direccionDAO = direccionDAO;
  }

  public TelefonoDAO getTelefonoDAO() {
    return telefonoDAO;
  }

  public void setTelefonoDAO(TelefonoDAO telefonoDAO) {
    this.telefonoDAO = telefonoDAO;
  }

  public EmailDAO getEmailDAO() {
    return emailDAO;
  }

  public void setEmailDAO(EmailDAO emailDAO) {
    this.emailDAO = emailDAO;
  }

  public List<Credito> getCreditosRelacionados() {
    return creditosRelacionados;
  }

  public void setCreditosRelacionados(List<Credito> creditosRelacionados) {
    this.creditosRelacionados = creditosRelacionados;
  }

  public List<Direcciones> getListaDirecciones() {
    return listaDirecciones;
  }

  public void setListaDirecciones(List<Direcciones> listaDirecciones) {
    this.listaDirecciones = listaDirecciones;
  }

  public List<Creditos> getCredsRelacionados() {
    return credsRelacionados;
  }

  public void setCredsRelacionados(List<Creditos> credsRelacionados) {
    this.credsRelacionados = credsRelacionados;
  }

  public ContactoDAO getContactoDAO() {
    return contactoDAO;
  }

  public void setContactoDAO(ContactoDAO contactoDAO) {
    this.contactoDAO = contactoDAO;
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

  public List<Contactos> getListaContactos() {
    return listaContactos;
  }

  public void setListaContactos(List<Contactos> listaContactos) {
    this.listaContactos = listaContactos;
  }

  public HistorialDAO getHistorialDao() {
    return historialDao;
  }

  public void setHistorialDao(HistorialDAO historialDao) {
    this.historialDao = historialDao;
  }

  public List<Historial> getHistorial() {
    return historial;
  }

  public void setHistorial(List<Historial> historial) {
    this.historial = historial;
  }

  public IndexBean getIndexBean() {
    return indexBean;
  }

  public void setIndexBean(IndexBean indexBean) {
    this.indexBean = indexBean;
  }

  public GestionDAO getGestionDao() {
    return gestionDao;
  }

  public void setGestionDao(GestionDAO gestionDao) {
    this.gestionDao = gestionDao;
  }

  public List<Gestion> getListaGestiones() {
    return listaGestiones;
  }

  public void setListaGestiones(List<Gestion> listaGestiones) {
    this.listaGestiones = listaGestiones;
  }

  public boolean isReasignarVisible() {
    return reasignarVisible;
  }

  public void setReasignarVisible(boolean reasignarVisible) {
    this.reasignarVisible = reasignarVisible;
  }

  public Gestor getGestorSeleccionado() {
    return gestorSeleccionado;
  }

  public void setGestorSeleccionado(Gestor gestorSeleccionado) {
    this.gestorSeleccionado = gestorSeleccionado;
  }

  public GestorDAO getGestorDao() {
    return gestorDao;
  }

  public void setGestorDao(GestorDAO gestorDao) {
    this.gestorDao = gestorDao;
  }

  public List<Gestor> getListaGestores() {
    return listaGestores;
  }

  public void setListaGestores(List<Gestor> listaGestores) {
    this.listaGestores = listaGestores;
  }

  public Gestor getGestorActual() {
    return gestorActual;
  }

  public void setGestorActual(Gestor gestorActual) {
    this.gestorActual = gestorActual;
  }

  public boolean isConveniosVisible() {
    return conveniosVisible;
  }

  public void setConveniosVisible(boolean conveniosVisible) {
    this.conveniosVisible = conveniosVisible;
  }

}
