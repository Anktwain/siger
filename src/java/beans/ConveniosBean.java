/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.ConvenioPagoDAO;
import dao.EstatusInformativoDAO;
import dao.GestionDAO;
import dto.ConvenioPago;
import dto.Credito;
import dto.EstatusInformativo;
import dto.Gestion;
import impl.ConvenioPagoIMPL;
import impl.EstatusInformativoIMPL;
import impl.GestionIMPL;
import java.io.*;
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
  private float saldoMaximo;
  private Credito creditoActual;
  private ConvenioPagoDAO convenioPagoDao;
  private List<ConvenioPago> listaConvenios;
  private List<ConvenioPago> convenioSeleccionado;
  private List<ConvenioPago> listaHistorialConvenios;
  private EstatusInformativoDAO estatusInformativoDao;
  private GestionDAO gestionDao;

  // CONSTRUCTOR
  public ConveniosBean() {
    creditoActual = vistaCreditoBean.getCreditoActual();
    saldoMaximo = creditoActual.getMonto();
    convenioPagoDao = new ConvenioPagoIMPL();
    listaConvenios = new ArrayList();
    listaHistorialConvenios = new ArrayList();
    estatusInformativoDao = new EstatusInformativoIMPL();
    gestionDao = new GestionIMPL();
    cargarListas();
  }

  // METODO QUE TRAE LA LISTA DE CONVENIOS
  public final void cargarListas() {
    int idCredito = creditoActual.getIdCredito();
    listaConvenios = convenioPagoDao.buscarConveniosEnCursoCredito(idCredito);
    listaHistorialConvenios = convenioPagoDao.buscarConveniosFinalizadosCredito(idCredito);
    if (listaConvenios.isEmpty()) {
      habilitaConvenios = false;
    } else {
      habilitaConvenios = true;
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
        g.setTipoGestion(Gestiones.ASUNTO.get(2));
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
      cargarListas();
    } else {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se finalizo el convenio. Contacte al equipo de sistemas."));
    }
    cargarListas();
  }

  // METODO AUXILIAR PARA TOMAR EL EVENTO DE CARGA DE ARCHIVOS
  public String cargarArchivo(FileUploadEvent evento) {
    FacesContext contexto = FacesContext.getCurrentInstance();
    UploadedFile archivoRecibido = evento.getFile();
    byte[] bytes = null;
    String ok;
    try {
      String ruta = "C:\\Program Files\\Apache Software Foundation\\Apache Tomcat 8.0.3\\bin\\Comprobantes\\";
      Date fecha = new Date();
      String nombre = ruta + indexBean.getUsuario().getNombreLogin() + "-" + fecha + archivoRecibido.getFileName().substring(evento.getFile().getFileName().indexOf("."));
      nombre = nombre.replace(" ", "");
      nombre = nombre.replace(":", "-");
      bytes = archivoRecibido.getContents();
      BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(nombre)));
      stream.write(bytes);
      stream.close();
      Logs.log.info("Se carga archivo al servidor: " + nombre);
      ok = archivoRecibido.getFileName();
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "El comprobante se cargo con exito."));
    } catch (IOException ioe) {
      Logs.log.error(ioe);
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se cargo el comprobante."));
      ok = null;
    }
    return ok;
  }

  // METODO QUE CARGA UN PAGO A UN CONVENIO EN ESPECIFICO
  public void agregarPago() {
    System.out.println("SUCK MY DICK");
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

  public ConvenioPagoDAO getConvenioPagoDao() {
    return convenioPagoDao;
  }

  public void setConvenioPagoDao(ConvenioPagoDAO convenioPagoDao) {
    this.convenioPagoDao = convenioPagoDao;
  }

  public List<ConvenioPago> getListaConvenios() {
    return listaConvenios;
  }

  public void setListaConvenios(List<ConvenioPago> listaConvenios) {
    this.listaConvenios = listaConvenios;
  }

  public ELContext getElContext() {
    return elContext;
  }

  public void setElContext(ELContext elContext) {
    this.elContext = elContext;
  }

  public VistaCreditoBean getVistaCreditoBean() {
    return vistaCreditoBean;
  }

  public void setVistaCreditoBean(VistaCreditoBean vistaCreditoBean) {
    this.vistaCreditoBean = vistaCreditoBean;
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

  public EstatusInformativoDAO getEstatusInformativoDao() {
    return estatusInformativoDao;
  }

  public void setEstatusInformativoDao(EstatusInformativoDAO estatusInformativoDao) {
    this.estatusInformativoDao = estatusInformativoDao;
  }

  public GestionDAO getGestionDao() {
    return gestionDao;
  }

  public void setGestionDao(GestionDAO gestionDao) {
    this.gestionDao = gestionDao;
  }

  public IndexBean getIndexBean() {
    return indexBean;
  }

  public void setIndexBean(IndexBean indexBean) {
    this.indexBean = indexBean;
  }

  public float getSaldoMaximo() {
    return saldoMaximo;
  }

  public void setSaldoMaximo(float saldoMaximo) {
    this.saldoMaximo = saldoMaximo;
  }

}
