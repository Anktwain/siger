/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.ConvenioPagoDAO;
import dao.GestorDAO;
import dao.PagoDAO;
import dao.PromesaPagoDAO;
import dao.QuincenaDAO;
import dto.ConvenioPago;
import dto.Credito;
import dto.Gestor;
import dto.Pago;
import dto.PromesaPago;
import dto.Quincena;
import dto.TipoGestion;
import impl.ConvenioPagoIMPL;
import impl.GestorIMPL;
import impl.PagoIMPL;
import impl.PromesaPagoIMPL;
import impl.QuincenaIMPL;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
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
import util.constantes.Directorios;
import util.constantes.Pagos;
import util.constantes.Perfiles;
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
  private boolean adminVisible;
  private boolean habilitaConvenios;
  private boolean habilitaPromesas;
  private boolean quitaCapital;
  private boolean sinContacto;
  private float saldoNuevoConvenio;
  private float saldoNuevaPromesa;
  private float saldoNuevoPago;
  private float saldoMaximo;
  private int pagosPrometidos;
  private Number saldoPendiente;
  private Date fechaDeposito;
  private Date fechaNuevaPromesa;
  private String cuentaPago;
  private String observacionesPago;
  private String nombrePago;
  private Credito creditoActual;
  private TipoGestion tipoGestionSeleccionada;
  private final QuincenaDAO quincenaDao;
  private final PagoDAO pagoDao;
  private final GestorDAO gestorDao;
  private final ConvenioPagoDAO convenioPagoDao;
  private final PromesaPagoDAO promesaPagoDao;
  private PromesaPago promesaSeleccionada;
  private ConvenioPago convenioActivo;
  private List<ConvenioPago> listaHistorialConvenios;
  private List<PromesaPago> listaPromesas;
  private List<Pago> listaPagosConvenioActivo;
  private List<Pago> listaHistorialPagos;
  private UploadedFile archivo;

  // CONSTRUCTOR
  public ConveniosBean() {
    convenioActivo = new ConvenioPago();
    creditoActual = vistaCreditoBean.getCreditoActual();
    saldoMaximo = creditoActual.getMonto();
    convenioPagoDao = new ConvenioPagoIMPL();
    listaHistorialConvenios = new ArrayList();
    listaPagosConvenioActivo = new ArrayList();
    listaHistorialPagos = new ArrayList();
    quincenaDao = new QuincenaIMPL();
    pagoDao = new PagoIMPL();
    gestorDao = new GestorIMPL();
    promesaPagoDao = new PromesaPagoIMPL();
    tipoGestionSeleccionada = new TipoGestion();
    cargarListas();
  }

  // METODO QUE TRAE LA LISTA DE CONVENIOS
  public final void cargarListas() {
    if (indexBean.getUsuario().getPerfil() == Perfiles.GESTOR) {
      adminVisible = false;
    } else {
      adminVisible = true;
    }
    int idCredito = creditoActual.getIdCredito();
    convenioActivo = convenioPagoDao.buscarConvenioEnCursoCredito(idCredito);
    listaHistorialConvenios = convenioPagoDao.buscarConveniosFinalizadosCredito(idCredito);
    listaHistorialPagos = pagoDao.buscarPagosPorCredito(idCredito);
    if (convenioActivo == null) {
      listaPagosConvenioActivo.clear();
      habilitaConvenios = false;
      habilitaPromesas = true;
    } else {
      habilitaConvenios = true;
      habilitaPromesas = false;
      int idConvenio = convenioActivo.getIdConvenioPago();
      listaPagosConvenioActivo = pagoDao.buscarPagosPorConvenioActivo(idConvenio);
      listaPromesas = promesaPagoDao.buscarPorConvenio(idConvenio);
    }
  }

  // METODO QUE AGREGA UN CONVENIO DE PAGO
  public void agregarConvenio() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    if (saldoNuevoConvenio > saldoMaximo) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El saldo negociado debe ser menor al saldo vencido."));
    } else if (saldoNuevoConvenio <= 0) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El saldo negociado debe ser mayor a cero."));
    } else if (pagosPrometidos <= 0) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "Debe prometer al menos un pago."));
    } else {
      ConvenioPago convenio = new ConvenioPago();
      convenio.setCredito(creditoActual);
      if (quitaCapital) {
        convenio.setEstatus(Convenios.CON_QUITA_DE_CAPITAL_EN_CURSO);
      }
      if (sinContacto) {
        convenio.setEstatus(Convenios.SIN_CONTACTO_EN_CURSO);
      }
      if ((quitaCapital) && (sinContacto)) {
        convenio.setEstatus(Convenios.CON_QUITA_SIN_CONTACTO_EN_CURSO);
      } else {
        convenio.setEstatus(Convenios.NORMAL_EN_CURSO);
      }
      Date fecha = new Date();
      convenio.setFecha(fecha);
      convenio.setPagosNegociados(pagosPrometidos);
      convenio.setSaldoNegociado(saldoNuevoConvenio);
      boolean ok = convenioPagoDao.insertar(convenio);
      // NO SE CREA LA GESTION AUTOMATICA PUESTO QUE:
      // - EXISTEN 3 TIPOS DE GESTION PARA CONVENIOS DE PAGO
      // - EL GESTOR DEBE SELECCIONAR EL LUGAR DONDE SE CELEBRO EL CONVENIO
      RequestContext.getCurrentInstance().execute("PF('agregarConvenioDialog').hide();");
      if (ok) {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se creo un nuevo convenio."));
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion.", "No olvide agregar la gestion correspondiente."));
        habilitaPromesas = false;
        RequestContext.getCurrentInstance().update("formConvenios");
        cargarListas();
      } else {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se creo el convenio. Contacte al equipo de sistemas."));
      }
    }
  }

  // METODO QUE TERMINA UN CONVENIO DE PAGO
  public void finalizarConvenio() {
    System.out.println("ENTRO AL METODO DE FINALIZAR CONVENIO");
    ConvenioPago c = convenioActivo;
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
      nombre = Directorios.RUTA_WINDOWS_CARGA_COMPROBANTES + nombre;
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

  // METODO QUE AGREGA UNA PROMESA DE PAGO AL CONVENIO EN CURSO
  public void agregarPromesa() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    ConvenioPago convenio = convenioPagoDao.buscarConvenioEnCursoCredito(creditoActual.getIdCredito());
    List<PromesaPago> promesas = promesaPagoDao.buscarPorConvenio(convenio.getIdConvenioPago());
    int promesasRestantes = convenio.getPagosNegociados() - promesas.size();
    saldoPendiente = convenioPagoDao.calcularSaldoPendiente(convenio.getIdConvenioPago());
    float montoMaximo = saldoPendiente.floatValue() - saldoNuevaPromesa;
    if (promesasRestantes == 0) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "Ya existen las promesas de pago necesarias."));
    } else if (montoMaximo < 0) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "Ya se ha prometido el saldo total del convenio."));
    } else if (saldoNuevaPromesa <= 0) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El saldo prometido de pago debe ser mayor a cero."));
    } else {
      PromesaPago p = new PromesaPago();
      p.setConvenioPago(convenio);
      p.setFechaPrometida(fechaNuevaPromesa);
      p.setCantidadPrometida(saldoNuevaPromesa);
      boolean ok = promesaPagoDao.insertar(p);
      if (ok) {
        cargarListas();
        RequestContext.getCurrentInstance().update("formConvenios");
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se agrego la promesa de pago."));
        RequestContext.getCurrentInstance().execute("PF('agregarPromesaDialog').hide();");
      } else {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se agrego la promesa de pago. Contacte al equipo de sistemas"));
      }
    }
  }

  // METODO QUE CARGA UN PAGO A UN CONVENIO EN ESPECIFICO
  public void agregarPago() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    float montoMaximo = promesaSeleccionada.getCantidadPrometida();
    if ((saldoNuevoPago > 0) && (saldoNuevoPago <= montoMaximo)) {
      Pago p = new Pago();
      p.setMonto(saldoNuevoPago);
      p.setPromesaPago(promesaSeleccionada);
      p.setEstatus(Pagos.PENDIENTE);
      p.setFechaDeposito(fechaDeposito);
      Date fechaRegistro = new Date();
      p.setFechaRegistro(fechaRegistro);
      p.setNombreComprobante(nombrePago);
      p.setNumeroCuenta(cuentaPago);
      Quincena q = quincenaDao.obtenerQuincenaActual();
      p.setQuincena(q);
      p.setRevisor("");
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
  public ConvenioPago getConvenioActivo() {
    return convenioActivo;
  }

  public void setConvenioActivo(ConvenioPago convenioActivo) {
    this.convenioActivo = convenioActivo;
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

  public boolean isHabilitaPromesas() {
    return habilitaPromesas;
  }

  public void setHabilitaPromesas(boolean habilitaPromesas) {
    this.habilitaPromesas = habilitaPromesas;
  }

  public float getSaldoNuevaPromesa() {
    return saldoNuevaPromesa;
  }

  public void setSaldoNuevaPromesa(float saldoNuevaPromesa) {
    this.saldoNuevaPromesa = saldoNuevaPromesa;
  }

  public Date getFechaNuevaPromesa() {
    return fechaNuevaPromesa;
  }

  public void setFechaNuevaPromesa(Date fechaNuevaPromesa) {
    this.fechaNuevaPromesa = fechaNuevaPromesa;
  }

  public List<PromesaPago> getListaPromesas() {
    return listaPromesas;
  }

  public void setListaPromesas(List<PromesaPago> listaPromesas) {
    this.listaPromesas = listaPromesas;
  }

  public PromesaPago getPromesaSeleccionada() {
    return promesaSeleccionada;
  }

  public void setPromesaSeleccionada(PromesaPago promesaSeleccionada) {
    this.promesaSeleccionada = promesaSeleccionada;
  }

  public int getPagosPrometidos() {
    return pagosPrometidos;
  }

  public void setPagosPrometidos(int pagosPrometidos) {
    this.pagosPrometidos = pagosPrometidos;
  }

  public boolean isQuitaCapital() {
    return quitaCapital;
  }

  public void setQuitaCapital(boolean quitaCapital) {
    this.quitaCapital = quitaCapital;
  }

  public boolean isSinContacto() {
    return sinContacto;
  }

  public void setSinContacto(boolean sinContacto) {
    this.sinContacto = sinContacto;
  }

  public TipoGestion getTipoGestionSeleccionada() {
    return tipoGestionSeleccionada;
  }

  public void setTipoGestionSeleccionada(TipoGestion tipoGestionSeleccionada) {
    this.tipoGestionSeleccionada = tipoGestionSeleccionada;
  }

  public boolean isAdminVisible() {
    return adminVisible;
  }

  public void setAdminVisible(boolean adminVisible) {
    this.adminVisible = adminVisible;
  }

}
