/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.ComprobantePagoDAO;
import dao.ConvenioPagoDAO;
import dao.CreditoDAO;
import dao.PagoDAO;
import dao.PromesaPagoDAO;
import dao.QuincenaDAO;
import dto.ComprobantePago;
import dto.ConvenioPago;
import dto.Credito;
import dto.Gestor;
import dto.Pago;
import dto.PromesaPago;
import dto.QuienGestion;
import dto.Quincena;
import dto.TipoGestion;
import impl.ComprobantePagoIMPL;
import impl.ConvenioPagoIMPL;
import impl.CreditoIMPL;
import impl.PagoIMPL;
import impl.PromesaPagoIMPL;
import impl.QuienGestionIMPL;
import impl.QuincenaIMPL;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import util.GestionAutomatica;
import util.constantes.Convenios;
import util.constantes.Directorios;
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
  CreditoActualBean creditoActualBean = (CreditoActualBean) elContext.getELResolver().getValue(elContext, null, "creditoActualBean");

  // VARIABLES DE CLASE
  private boolean habilitaConvenios;
  private boolean habilitaPromesas;
  private boolean quitaCapital;
  private boolean sinContacto;
  private boolean habilita1;
  private boolean habilita2;
  private boolean habilita3;
  private boolean habilita4;
  private boolean habilita5;
  private boolean habilita6;
  private boolean habilita7;
  private boolean habilitaCarga;
  private boolean cargoComprobantes;
  private float saldoNuevoConvenio;
  private float[] saldosNuevasPromesas;
  private float saldoNuevoPago;
  private float saldoMaximo;
  private int pagosPrometidos;
  private Number saldoPendiente;
  private Date fechaDeposito;
  private Date[] fechasNuevasPromesas;
  private String cuentaPago;
  private String observacionesPago;
  private Credito creditoActual;
  private TipoGestion tipoGestionSeleccionada;
  private final QuincenaDAO quincenaDao;
  private final PagoDAO pagoDao;
  private final ConvenioPagoDAO convenioPagoDao;
  private final PromesaPagoDAO promesaPagoDao;
  private final CreditoDAO creditoDao;
  private final ComprobantePagoDAO comprobantePagoDao;
  private PromesaPago promesaSeleccionada;
  private ConvenioPago convenioActivo;
  private List<ConvenioPago> listaHistorialConvenios;
  private List<PromesaPago> listaPromesas;
  private List<Pago> listaPagosConvenioActivo;
  private List<Pago> listaHistorialPagos;
  private List<String> listaSujetos;
  private List<String> listaCuentasPago;
  private List<String> listaNombresComprobantes;
  private String sujetoConvenio;
  private UploadedFile archivo;

  // CONSTRUCTOR
  public ConveniosBean() {
    habilitaCarga = true;
    cargoComprobantes = false;
    convenioActivo = new ConvenioPago();
    creditoActual = creditoActualBean.getCreditoActual();
    convenioPagoDao = new ConvenioPagoIMPL();
    listaHistorialConvenios = new ArrayList();
    listaPagosConvenioActivo = new ArrayList();
    listaHistorialPagos = new ArrayList();
    listaSujetos = new ArrayList();
    listaCuentasPago = new ArrayList();
    listaNombresComprobantes = new ArrayList();
    quincenaDao = new QuincenaIMPL();
    pagoDao = new PagoIMPL();
    promesaPagoDao = new PromesaPagoIMPL();
    creditoDao = new CreditoIMPL();
    comprobantePagoDao = new ComprobantePagoIMPL();
    saldoMaximo = creditoDao.buscarSaldoVencidoCredito(creditoActual.getIdCredito());
    tipoGestionSeleccionada = new TipoGestion();
    saldosNuevasPromesas = new float[7];
    fechasNuevasPromesas = new Date[7];
    cargarListas();
  }

  // METODO QUE TRAE LA LISTA DE CONVENIOS
  public final void cargarListas() {
    observacionesPago = "TE MANDO PAGO PARA SU VALIDACION.";
    convenioActivo = convenioPagoDao.buscarConvenioEnCursoCredito(creditoActual.getIdCredito());
    listaHistorialConvenios = convenioPagoDao.buscarConveniosFinalizadosCredito(creditoActual.getIdCredito());
    listaHistorialPagos = pagoDao.buscarPagosPorCredito(creditoActual.getIdCredito());
    listaCuentasPago = Arrays.asList("50010911552", "50010911556", "50015025745", "50015025741", "50015025905", "50015025902", "RECIBO TELMEX", "ACTA DEFUNCION");
    List< QuienGestion> sujetos = new QuienGestionIMPL().buscarTodo();
    for (int i = 0; i < (sujetos.size()); i++) {
      if (sujetos.get(i).getTipoQuienGestion().getIdTipoQuienGestion() < 7) {
        listaSujetos.add(sujetos.get(i).getQuien());
      }
    }
    if (convenioActivo == null) {
      listaPagosConvenioActivo.clear();
      habilitaConvenios = false;
      listaPromesas = new ArrayList();
    } else {
      habilitaConvenios = true;
      int idConvenio = convenioActivo.getIdConvenioPago();
      listaPagosConvenioActivo = pagoDao.buscarPagosPorConvenioActivo(idConvenio);
      listaPromesas = promesaPagoDao.buscarPorConvenio(idConvenio);
    }
  }

  // METODO QUE AGREGA UN CONVENIO DE PAGO
  public void agregarConvenio() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    if (saldoNuevoConvenio > saldoMaximo) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El saldo negociado debe ser menor o igual al saldo vencido."));
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
      convenio = convenioPagoDao.insertar(convenio);
      if (convenio != null) {
        convenioActivo = convenio;
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se creo un nuevo convenio."));
        GestionAutomatica.generarGestionAutomatica("9APROB", creditoActual, indexBean.getUsuario(), "CONVENIO CON " + sujetoConvenio + " POR $" + saldoNuevoConvenio + " EN " + pagosPrometidos + " PARCIALIDAD(ES).");
        habilitaPromesas = true;
        switch (pagosPrometidos) {
          case 1:
            habilita1 = true;
            break;
          case 2:
            habilita1 = true;
            habilita2 = true;
            break;
          case 3:
            habilita1 = true;
            habilita2 = true;
            habilita3 = true;
            break;
          case 4:
            habilita1 = true;
            habilita2 = true;
            habilita3 = true;
            habilita4 = true;
            break;
          case 5:
            habilita1 = true;
            habilita2 = true;
            habilita3 = true;
            habilita4 = true;
            habilita5 = true;
            break;
          case 6:
            habilita1 = true;
            habilita2 = true;
            habilita3 = true;
            habilita4 = true;
            habilita5 = true;
            habilita6 = true;
            break;
          case 7:
            habilita1 = true;
            habilita2 = true;
            habilita3 = true;
            habilita4 = true;
            habilita5 = true;
            habilita6 = true;
            habilita7 = true;
            break;
        }
        RequestContext.getCurrentInstance().update("formConvenios");
        cargarListas();
      } else {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se creo el convenio. Contacte al equipo de sistemas."));
      }
    }
  }

  // METODO QUE TERMINA UN CONVENIO DE PAGO
  public void finalizarConvenio() {
    ConvenioPago c = convenioActivo;
    c.setEstatus(Convenios.FINALIZADO);
    boolean ok = convenioPagoDao.editar(c);
    FacesContext contexto = FacesContext.getCurrentInstance();
    if (ok) {
      GestionAutomatica.generarGestionAutomatica("14DELC", creditoActual, indexBean.getUsuario(), "SE FINALIZA CONVENIO");
      // TO FIX
      // EVITAR QUE LA VISTA YA TENGA LOS DATOS PRECARGADOS Y LAS PROMESAS DISPONIBLES
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se finalizo convenio."));
    } else {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se finalizo el convenio. Contacte al equipo de sistemas."));
    }
    cargarListas();
  }
  
  // METODO QUE BORRA EL CONVENIO EN CASO DE QUE EL USUARIO CANCELE LA OPERACION
  public void borrarConvenio(){
    if(!convenioPagoDao.eliminar(convenioActivo)){
      Logs.log.error("No se pudo terminar el convenio del credito " + convenioActivo.getCredito().getNumeroCredito() + " por cancelacion del usuario.");
    }
    else{
      cargarListas();
      Logs.log.info("Se termino convenio por cancelacion del usuario en el modulo de agregar promesas de pago.");
    }
    cancelar();
  }

  // METODO AUXILIAR PARA TOMAR EL EVENTO DE CARGA DE ARCHIVOS
  public void eventoDeCarga(FileUploadEvent evento) {
    archivo = evento.getFile();
    cargarArchivoAlServidor(archivo);
  }

  // METODO QUE CARGA AL SERVIDOR EL ARCHIVO OBTENIDO DEL EVENTO FILE UPLOAD
  public void cargarArchivoAlServidor(UploadedFile archivo) {
    byte[] bytes;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    String extension = archivo.getFileName().substring(archivo.getFileName().indexOf("."));
    String nombre = indexBean.getUsuario().getNombreLogin() + "_" + df.format(new Date()) + extension;
    nombre = nombre.replace(" ", "");
    nombre = Directorios.RUTA_WINDOWS_CARGA_COMPROBANTES + nombre;
    bytes = archivo.getContents();
    BufferedOutputStream stream;
    try {
      stream = new BufferedOutputStream(new FileOutputStream(new File(nombre)));
      stream.write(bytes);
      Logs.log.info("Se carga comprobante de pago al servidor: " + nombre);
      listaNombresComprobantes.add(nombre);
      cargoComprobantes = true;
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se cargo el comprobante en el sistema."));
    } catch (FileNotFoundException fnfe) {
      Logs.log.error("No se cargo el comprobante al servidor");
      Logs.log.error(nombre);
      Logs.log.error(fnfe.getStackTrace());
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se cargo el comprobante. Contacte al equipo de sistemas"));
    } catch (IOException ioe) {
      Logs.log.error("No se cargo el comprobante al servidor");
      Logs.log.error(nombre);
      Logs.log.error(ioe.getStackTrace());
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se cargo el comprobante. Contacte al equipo de sistemas"));
    }
  }

  // METODO QUE AGREGA UNA PROMESA DE PAGO AL CONVENIO EN CURSO
  public void agregarPromesas() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    boolean ok = true;
    boolean pagosNoValidos = false;
    boolean fechasNoValidas = false;
    float totalPagos = 0;
    for (int i = 0; i < pagosPrometidos; i++) {
      totalPagos = totalPagos + saldosNuevasPromesas[i];
      if (saldosNuevasPromesas[i] <= 0) {
        pagosNoValidos = true;
      }
      if (fechasNuevasPromesas[i].before(new Date())) {
        fechasNoValidas = true;
      }
    }
    if (totalPagos > convenioActivo.getSaldoNegociado()) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El saldo de las promesas es mayor al saldo del convenio."));
    } else if (pagosNoValidos) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pueden prometer pagos iguales o menores a cero."));
    } else if (fechasNoValidas) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pueden prometer pagos en fechas anteriores al dia de hoy."));
    } else {
      for (int i = 0; i < pagosPrometidos; i++) {
        PromesaPago p = new PromesaPago();
        p.setConvenioPago(convenioActivo);
        p.setFechaPrometida(fechasNuevasPromesas[i]);
        p.setCantidadPrometida(saldosNuevasPromesas[i]);
        ok = ok & (promesaPagoDao.insertar(p));
      }
      if (ok) {
        cargarListas();
        RequestContext.getCurrentInstance().update("formConvenios");
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se agregaron las promesas de pago."));
        RequestContext.getCurrentInstance().execute("PF('agregarConvenioDialog').hide();");
      } else {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se agregaron las promesas de pago. Contacte al equipo de sistemas"));
      }
    }
  }

  // METODO QUE CARGA UN PAGO A UNA PROMESA EN ESPECIFICO
  public void agregarPago() {
    float montoMaximo = promesaSeleccionada.getCantidadPrometida();
    if ((saldoNuevoPago <= 0) || (saldoNuevoPago > montoMaximo)) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El monto del pago debe ser mayor a cero y menor al saldo convenido."));
    } else if (fechaDeposito.after(new Date())) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "La fecha no puede ser posterior al dia de hoy."));
    } else {
      Pago p = new Pago();
      p.setMontoPago(saldoNuevoPago);
      p.setMontoAprobado(0);
      p.setPromesaPago(promesaSeleccionada);
      p.setEstatus(Pagos.PENDIENTE);
      p.setFechaDeposito(fechaDeposito);
      p.setFechaRegistro(new Date());
      p.setNumeroCuenta(cuentaPago);
      Quincena q = quincenaDao.obtenerQuincenaActual();
      p.setQuincena(q);
      p.setRevisor("");
      p.setObservacionGestor(observacionesPago);
      p.setGestor(creditoActual.getGestor());
      p.setPagado(Pagos.NO_PAGADO);
      Gestor g = creditoActual.getGestor();
      p.setGestor(g);
      if (!indexBean.getUsuario().getNombreLogin().equals(creditoActual.getGestor().getUsuario().getNombreLogin())) {
        Logs.log.warn("El usuario " + indexBean.getUsuario().getNombreLogin() + " ha cargado un pago para el credito " + creditoActual.getNumeroCredito() + ", asignado al gestor " + creditoActual.getGestor().getUsuario().getNombreLogin());
      }
      Pago pago = pagoDao.insertar(p);
      if (pago != null) {
        if (cargarComprobantesDB(pago)) {
          listaNombresComprobantes = new ArrayList();
          cargarListas();
          FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se agrego un nuevo pago."));
        } else {
          if (pagoDao.eliminar(pago)) {
          } else {
          }
          FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se agrego el pago. Contacte al equipo de sistemas."));
        }
      } else {
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se agrego el pago. Contacte al equipo de sistemas."));
      }
    }
    RequestContext.getCurrentInstance().execute("PF('agregarPagoDialog').hide();");
  }

  // METODO QUE CARGA LOS COMPROBANTES A LA BASE DE DATOS
  public boolean cargarComprobantesDB(Pago pago) {
    boolean ok = true;
    for (int i = 0; i < listaNombresComprobantes.size(); i++) {
      ComprobantePago c = new ComprobantePago();
      c.setNombreComprobante(listaNombresComprobantes.get(i));
      c.setPago(pago);
      ok = ok & (comprobantePagoDao.insertar(c));
    }
    return ok;
  }

  // METODO QUE CANCELA EL PROCESO DE CARGA DE UN PAGO
  public void cancelar() {
    try {
      FacesContext.getCurrentInstance().getExternalContext().redirect("vistaConvenio.xhtml");
    } catch (IOException ioe) {
      Logs.log.error("No se pudo recargar la vista de convenios.");
      Logs.log.error(ioe);
    }
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

  // METODO QUE INICIALIZA EL MONTO DEL NUEVO PAGO
  public void inicializarMontoPago() {
    saldoNuevoPago = promesaSeleccionada.getCantidadPrometida();
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

  public List<String> getListaSujetos() {
    return listaSujetos;
  }

  public void setListaSujetos(List<String> listaSujetos) {
    this.listaSujetos = listaSujetos;
  }

  public String getSujetoConvenio() {
    return sujetoConvenio;
  }

  public void setSujetoConvenio(String sujetoConvenio) {
    this.sujetoConvenio = sujetoConvenio;
  }

  public boolean isHabilita1() {
    return habilita1;
  }

  public void setHabilita1(boolean habilita1) {
    this.habilita1 = habilita1;
  }

  public boolean isHabilita2() {
    return habilita2;
  }

  public void setHabilita2(boolean habilita2) {
    this.habilita2 = habilita2;
  }

  public boolean isHabilita3() {
    return habilita3;
  }

  public void setHabilita3(boolean habilita3) {
    this.habilita3 = habilita3;
  }

  public boolean isHabilita4() {
    return habilita4;
  }

  public void setHabilita4(boolean habilita4) {
    this.habilita4 = habilita4;
  }

  public boolean isHabilita5() {
    return habilita5;
  }

  public void setHabilita5(boolean habilita5) {
    this.habilita5 = habilita5;
  }

  public boolean isHabilita6() {
    return habilita6;
  }

  public void setHabilita6(boolean habilita6) {
    this.habilita6 = habilita6;
  }

  public boolean isHabilita7() {
    return habilita7;
  }

  public void setHabilita7(boolean habilita7) {
    this.habilita7 = habilita7;
  }

  public float[] getSaldosNuevasPromesas() {
    return saldosNuevasPromesas;
  }

  public void setSaldosNuevasPromesas(float[] saldosNuevasPromesas) {
    this.saldosNuevasPromesas = saldosNuevasPromesas;
  }

  public Date[] getFechasNuevasPromesas() {
    return fechasNuevasPromesas;
  }

  public void setFechasNuevasPromesas(Date[] fechasNuevasPromesas) {
    this.fechasNuevasPromesas = fechasNuevasPromesas;
  }

  public List<String> getListaCuentasPago() {
    return listaCuentasPago;
  }

  public void setListaCuentasPago(List<String> listaCuentasPago) {
    this.listaCuentasPago = listaCuentasPago;
  }

  public boolean isHabilitaCarga() {
    return habilitaCarga;
  }

  public void setHabilitaCarga(boolean habilitaCarga) {
    this.habilitaCarga = habilitaCarga;
  }

  public boolean isCargoComprobantes() {
    return cargoComprobantes;
  }

  public void setCargoComprobantes(boolean cargoComprobantes) {
    this.cargoComprobantes = cargoComprobantes;
  }

  public UploadedFile getArchivo() {
    return archivo;
  }

  public void setArchivo(UploadedFile archivo) {
    this.archivo = archivo;
  }

  public List<String> getListaNombresComprobantes() {
    return listaNombresComprobantes;
  }

  public void setListaNombresComprobantes(List<String> listaNombresComprobantes) {
    this.listaNombresComprobantes = listaNombresComprobantes;
  }

}
