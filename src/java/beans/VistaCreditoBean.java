/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.ContactoDAO;
import dao.CreditoDAO;
import dao.DireccionDAO;
import dao.EmailDAO;
import dao.TelefonoDAO;
import dto.tablas.Cred;
import dto.Credito;
import dto.Direccion;
import dto.Email;
import dto.Telefono;
import dto.tablas.Cont;
import dto.tablas.Dir;
import impl.ContactoIMPL;
import impl.CreditoIMPL;
import impl.DireccionIMPL;
import impl.EmailIMPL;
import impl.TelefonoIMPL;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "vistaCreditoBean")
@ViewScoped
public class VistaCreditoBean implements Serializable {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  CuentasVistaBean cuentasVistaBean = (CuentasVistaBean) elContext.getELResolver().getValue(elContext, null, "cuentasVistaBean");

  // VARIABLES DE CLASE
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
  private Cred creditoActualCred;
  private Credito creditoActual;
  private CreditoDAO creditoDao;
  private DireccionDAO direccionDAO;
  private TelefonoDAO telefonoDAO;
  private EmailDAO emailDAO;
  private ContactoDAO contactoDAO;
  private List<Credito> creditosRelacionados;
  private List<Cred> credsRelacionados;
  private List<Dir> listaDirecciones;
  private List<Telefono> listaTelefonos;
  private List<Email> listaCorreos;
  private List<Cont> listaContactos;

  public VistaCreditoBean() {
    creditoActualCred = new Cred();
    creditoActual = new Credito();
    creditoDao = new CreditoIMPL();
    direccionDAO = new DireccionIMPL();
    telefonoDAO = new TelefonoIMPL();
    emailDAO = new EmailIMPL();
    contactoDAO = new ContactoIMPL();
    creditosRelacionados = new ArrayList();
    credsRelacionados = new ArrayList();
    listaDirecciones = new ArrayList();
    creditoActualCred = cuentasVistaBean.getCreditoSeleccionado().get(0);
    obtenerDatos();
  }

  // METODO QUE OBTENDRA TODOS LOS DATOS PRIMARIOS SEGUN EL CREDITO SELECCIONADO EN LA VISTA cuentas.xhtml
  private void obtenerDatos() {
    // OBTIENE LA CADENA CON EL NUMERO DE CREDITO
    numeroCredito = creditoActualCred.getNumeroCredito();
    // OBTENEMOS EL CREDITO RELACIONADO
    creditoActual = creditoDao.buscarCreditoPorId(creditoDao.obtenerIdDelCredito(numeroCredito));
    // OBTENEMOS EL ID DEL SUJETO PARA TODAS LAS OPERACIONES
    int idSujeto = creditoActual.getDeudor().getSujeto().getIdSujeto();
    // OBTIENE LA CADENA CON EL NOMBRE DEL DEUDOR
    nombreDeudor = creditoActualCred.getNombreRazonSocial();
    // OBTENER LA PRIMER DIRECCION DEL DEUDOR
    Direccion d = new Direccion();
    d = direccionDAO.buscarPorSujeto(idSujeto).get(0);
    calleNumero = d.getCalle();
    coloniaMunicipio = d.getColonia().getNombre() + ", " + d.getMunicipio().getNombre();
    estadoCP = d.getEstadoRepublica().getNombre() + ", " + d.getColonia().getCodigoPostal();
    // OBTENEMOS EL NUMERO DE CREDITOS PARA ESTE CLIENTE
    numeroCreditos = Integer.toString(creditoDao.buscarCreditosRelacionados(creditoActual).size() + 1);
    // OBTENEMOS LAS DIFERENTES FECHAS QUE SE REQUIEREN
    try {
      // OBTENER EL PRIMER TELEFONO DEL DEUDOR
      Telefono tel = new Telefono();
      tel = telefonoDAO.buscarPorSujeto(idSujeto).get(0);
      telefono = "(" + tel.getLada() + ") " + tel.getNumero();
      // OBTENER EL PRIMER CORREO DEL DEUDOR
      Email mail = new Email();
      mail = emailDAO.buscarPorSujeto(idSujeto).get(0);
      correo = mail.getDireccion();
      DateFormat df = new SimpleDateFormat("dd de MM del yyyy");
      fechaInicio = df.format(creditoActual.getFechaInicio());
      fechaFin = df.format(creditoActual.getFechaFin());
      fup = "";
      fvp = "";
    } catch (Exception e) {
      System.out.println("No hay fechas relacionadas con este credito");
    }
    mensualidad = creditoActual.getMensualidad().toString();
    saldoVencido = "";
    // OBTENEMOS LA LISTA DE LAS DIRECCIONES DE ESTE DEUDOR, SI ES QUE EXISTE TAL LISTA
    List<Direccion> listaDireccionesSinNormalizar = new ArrayList();
    listaDireccionesSinNormalizar = direccionDAO.buscarPorSujeto(idSujeto);
    int tam = listaDireccionesSinNormalizar.size();
    System.out.println("TAMAÑO DE LA LISTA DE DIRECCIONES: " + tam);
    if (tam > 0) {
      for (int i = 0; i < tam; i++) {
        Dir oneDirection = new Dir();
        Direccion vieja = new Direccion();
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
    System.out.println("TAMAÑO DE LA LISTA DE CREDITOS RELACIONADOS: " + tam);
    if (tam > 0) {
      for (int i = 0; i < tam; i++) {
        Cred c = new Cred();
        Credito viejo = new Credito();
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

  public Cred getCreditoActualCred() {
    return creditoActualCred;
  }

  public void setCreditoActualCred(Cred creditoActualCred) {
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

  public List<Dir> getListaDirecciones() {
    return listaDirecciones;
  }

  public void setListaDirecciones(List<Dir> listaDirecciones) {
    this.listaDirecciones = listaDirecciones;
  }

  public List<Cred> getCredsRelacionados() {
    return credsRelacionados;
  }

  public void setCredsRelacionados(List<Cred> credsRelacionados) {
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

  public List<Cont> getListaContactos() {
    return listaContactos;
  }

  public void setListaContactos(List<Cont> listaContactos) {
    this.listaContactos = listaContactos;
  }

}
