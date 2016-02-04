/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.ConvenioPagoDAO;
import dao.EstatusInformativoDAO;
import dao.GestionDAO;
import dao.GestorDAO;
import dao.PagoDAO;
import dao.QuincenaDAO;
import dto.ConvenioPago;
import dto.Credito;
import dto.EstatusInformativo;
import dto.Gestion;
import dto.Gestor;
import dto.Pago;
import dto.Quincena;
import impl.ConvenioPagoIMPL;
import impl.EstatusInformativoIMPL;
import impl.GestionIMPL;
import impl.GestorIMPL;
import impl.PagoIMPL;
import impl.QuincenaIMPL;
import java.io.*;
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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import util.constantes.Convenios;
import util.constantes.Gestiones;
import util.constantes.Pagos;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "conveniosBean")
@ViewScoped
public class ConveniosBean implements Serializable {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");
  VistaCreditoBean vistaCreditoBean = (VistaCreditoBean) elContext.getELResolver().getValue(elContext, null, "vistaCreditoBean");

  // VARIABLES DE CLASE
  private boolean habilitaConvenios;
  private float saldoNuevoConvenio;
  private float saldoNuevoPago;
  private float saldoMaximo;
  private Number saldoPendiente;
  private Date fechaDeposito;
  private String cuentaPago;
  private String observacionesPago;
  private String ruta;
  private String nombrePago;
  private Credito creditoActual;
  private final EstatusInformativoDAO estatusInformativoDao;
  private final GestionDAO gestionDao;
  private final QuincenaDAO quincenaDao;
  private final PagoDAO pagoDao;
  private final GestorDAO gestorDao;
  private final ConvenioPagoDAO convenioPagoDao;
  private List<ConvenioPago> listaConvenios;
  private List<ConvenioPago> convenioSeleccionado;
  private List<ConvenioPago> listaHistorialConvenios;
  private List<Pago> listaPagosConvenioActivo;
  private List<Pago> listaHistorialPagos;
  private UploadedFile archivo;

  // CONSTRUCTOR
  public ConveniosBean() {
    ruta = "C:\\Users\\Eduardo.CORPDELRIO\\Documents\\NetBeansProjects\\SigerWeb\\SigerWeb\\web\\resources\\img\\comprobantes\\";
    creditoActual = vistaCreditoBean.getCreditoActual();
    saldoMaximo = creditoActual.getMonto();
    convenioPagoDao = new ConvenioPagoIMPL();
    listaConvenios = new ArrayList();
    listaHistorialConvenios = new ArrayList();
    listaPagosConvenioActivo = new ArrayList();
    listaHistorialPagos = new ArrayList();
    estatusInformativoDao = new EstatusInformativoIMPL();
    gestionDao = new GestionIMPL();
    quincenaDao = new QuincenaIMPL();
    pagoDao = new PagoIMPL();
    gestorDao = new GestorIMPL();
    cargarListas();
  }

  // METODO QUE TRAE LA LISTA DE CONVENIOS
  public final void cargarListas() {
    int idCredito = creditoActual.getIdCredito();
    listaConvenios = convenioPagoDao.buscarConveniosEnCursoCredito(idCredito);
    listaHistorialConvenios = convenioPagoDao.buscarConveniosFinalizadosCredito(idCredito);
    listaHistorialPagos = pagoDao.buscarPagosPorCredito(idCredito);
    if (listaConvenios.isEmpty()) {
      listaPagosConvenioActivo.clear();
      habilitaConvenios = false;
      saldoPendiente = 0;
    } else {
      habilitaConvenios = true;
      int idConvenio = listaConvenios.get(0).getIdConvenioPago();
      saldoPendiente = convenioPagoDao.calcularSaldoPendiente(idConvenio);
      listaPagosConvenioActivo = pagoDao.buscarPagosPorConvenioActivo(idConvenio);
    }
  }

  // METODO QUE AGREGA UN CONVENIO DE PAGO
  public void agregarConvenio() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    if (saldoNuevoConvenio > saldoMaximo) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El saldo negociado debe ser menor al saldo vencido."));
    } else {
      ConvenioPago convenio = new ConvenioPago();
      convenio.setCredito(creditoActual);
      convenio.setEstatus(Convenios.EN_CURSO);
      Date fecha = new Date();
      convenio.setFecha(fecha);
      convenio.setPagosRealizados(0);
      convenio.setSaldoNegociado(saldoNuevoConvenio);
      boolean ok = convenioPagoDao.insertar(convenio);
      if (ok) {
        Gestion g = new Gestion();
        g.setCredito(creditoActual);
        g.setFecha(fecha);
        g.setUsuario(indexBean.getUsuario());
        g.setTipoGestion(Gestiones.TIPO_GESTION.get(2));
        g.setLugarGestion(Gestiones.DONDE_CORPORATIVO.get(0));
        g.setAsuntoGestion(Gestiones.ASUNTO.get(6));
        g.setDescripcionGestion("SE REALIZA CONVENIO DE PAGO CON ");
        g.setTipoSujetoGestion(Gestiones.TIPO_SUJETOS.get(0));
        EstatusInformativo e = estatusInformativoDao.buscar(2);
        g.setEstatusInformativo(e);
        g.setGestion("SALDO NEGOCIADO: " + saldoNuevoConvenio);
        ok = ok & (gestionDao.insertarGestion(g));
        if (ok) {
          contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se creo un nuevo convenio."));
          cargarListas();
        } else {
          contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se creo el convenio. Contacte al equipo de sistemas."));
        }
      } else {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se creo el convenio. Contacte al equipo de sistemas."));
      }
      RequestContext.getCurrentInstance().execute("PF('agregarConvenioDialog').hide();");
    }
  }

  // METODO QUE TERMINA UN CONVENIO DE PAGO
  public void finalizarConvenio() {
    ConvenioPago c = convenioSeleccionado.get(0);
    c.setEstatus(Convenios.FINALIZADO);
    boolean ok = convenioPagoDao.editar(c);
    FacesContext contexto = FacesContext.getCurrentInstance();
    if (ok) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se finalizo convenio."));
    } else {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se finalizo el convenio. Contacte al equipo de sistemas."));
    }
    cargarListas();
  }

  // METODO AUXILIAR PARA TOMAR EL EVENTO DE CARGA DE ARCHIVOS
  public String eventoDeCarga(FileUploadEvent evento) {
    FacesContext contexto = FacesContext.getCurrentInstance();
    archivo = evento.getFile();
    byte[] bytes;
    String ok;
    try {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
      Date f = new Date();
      String fecha = dateFormat.format(f);
      String nombre = indexBean.getUsuario().getNombreLogin() + "-" + fecha + archivo.getFileName().substring(archivo.getFileName().indexOf("."));
      nombre = nombre.replace(" ", "");
      nombrePago = nombre;
      nombre = ruta + nombre;
      bytes = archivo.getContents();
      BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(nombre)));
      stream.write(bytes);
      Logs.log.info("Se carga archivo al servidor: " + nombre);
      ok = archivo.getFileName();
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Carga exitosa.", "El comprobante se cargo con exito."));
    } catch (IOException ioe) {
      Logs.log.error(ioe);
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se cargo el comprobante."));
      ok = null;
    }
    return ok;
  }

  // METODO QUE CARGA UN PAGO A UN CONVENIO EN ESPECIFICO
  public void agregarPago() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    float montoMaximo = convenioSeleccionado.get(0).getSaldoNegociado();
    if (saldoNuevoPago > 0 && saldoNuevoPago <= montoMaximo) {
      Pago p = new Pago();
      p.setMonto(saldoNuevoPago);
      p.setConvenioPago(convenioSeleccionado.get(0));
      p.setEstatus(Pagos.PENDIENTE);
      p.setFechaDeposito(fechaDeposito);
      Date fechaRegistro = new Date();
      p.setFechaRegistro(fechaRegistro);
      p.setNombreComprobante(nombrePago);
      p.setNumeroCuenta(cuentaPago);
      Quincena q = quincenaDao.obtenerQuincenaActual();
      p.setQuincena(q);
      p.setObservaciones(observacionesPago);
      Gestor g = gestorDao.buscarGestorDelCredito(creditoActual.getIdCredito());
      p.setGestor(g);
      boolean ok = pagoDao.insertar(p);
      if (ok) {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se agrego un nuevo pago."));
        cargarListas();
      } else {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se agrego el pago. Contacte al equipo de sistemas."));
      }
    } else {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El monto del pago debe ser mayor a cero y menor al saldo convenido."));
    }
    RequestContext.getCurrentInstance().execute("PF('agregarPagoDialog').hide();");
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
    return estado;
  }

  // ***********************************************************************************************************************
  // ***********************************************************************************************************************
  // ***********************************************************************************************************************
  // GETTERS & SETTERS
  public List<ConvenioPago> getConvenioSeleccionado() {
    return convenioSeleccionado;
  }

  public void setConvenioSeleccionado(List<ConvenioPago> convenioSeleccionado) {
    this.convenioSeleccionado = convenioSeleccionado;
  }

  public List<ConvenioPago> getListaConvenios() {
    return listaConvenios;
  }

  public void setListaConvenios(List<ConvenioPago> listaConvenios) {
    this.listaConvenios = listaConvenios;
  }

  public List<ConvenioPago> getListaHistorialConvenios() {
    return listaHistorialConvenios;
  }

  public void setListaHistorialConvenios(List<ConvenioPago> listaHistorialConvenios) {
    this.listaHistorialConvenios = listaHistorialConvenios;
  }

  public float getSaldoNuevoConvenio() {
    return saldoNuevoConvenio;
  }

  public void setSaldoNuevoConvenio(float saldoNuevoConvenio) {
    this.saldoNuevoConvenio = saldoNuevoConvenio;
  }

  public Credito getCreditoActual() {
    return creditoActual;
  }

  public void setCreditoActual(Credito creditoActual) {
    this.creditoActual = creditoActual;
  }

  public boolean isHabilitaConvenios() {
    return habilitaConvenios;
  }

  public void setHabilitaConvenios(boolean habilitaConvenios) {
    this.habilitaConvenios = habilitaConvenios;
  }

  public float getSaldoMaximo() {
    return saldoMaximo;
  }

  public void setSaldoMaximo(float saldoMaximo) {
    this.saldoMaximo = saldoMaximo;
  }

  public float getSaldoNuevoPago() {
    return saldoNuevoPago;
  }

  public void setSaldoNuevoPago(float saldoNuevoPago) {
    this.saldoNuevoPago = saldoNuevoPago;
  }

  public Date getFechaDeposito() {
    return fechaDeposito;
  }

  public void setFechaDeposito(Date fechaDeposito) {
    this.fechaDeposito = fechaDeposito;
  }

  public String getCuentaPago() {
    return cuentaPago;
  }

  public void setCuentaPago(String cuentaPago) {
    this.cuentaPago = cuentaPago;
  }

  public String getObservacionesPago() {
    return observacionesPago;
  }

  public void setObservacionesPago(String observacionesPago) {
    this.observacionesPago = observacionesPago;
  }

  public UploadedFile getArchivo() {
    return archivo;
  }

  public void setArchivo(UploadedFile archivo) {
    this.archivo = archivo;
  }

  public Number getSaldoPendiente() {
    return saldoPendiente;
  }

  public void setSaldoPendiente(Number saldoPendiente) {
    this.saldoPendiente = saldoPendiente;
  }

  public List<Pago> getListaPagosConvenioActivo() {
    return listaPagosConvenioActivo;
  }

  public void setListaPagosConvenioActivo(List<Pago> listaPagosConvenioActivo) {
    this.listaPagosConvenioActivo = listaPagosConvenioActivo;
  }

  public List<Pago> getListaHistorialPagos() {
    return listaHistorialPagos;
  }

  public void setListaHistorialPagos(List<Pago> listaHistorialPagos) {
    this.listaHistorialPagos = listaHistorialPagos;
  }

}
