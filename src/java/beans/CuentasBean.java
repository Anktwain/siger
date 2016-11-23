/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.CampanaDAO;
import dao.ConceptoDevolucionDAO;
import dao.ConvenioPagoDAO;
import dao.CreditoDAO;
import dao.DevolucionDAO;
import dao.GestorDAO;
import dao.HistorialDAO;
import dao.MarcajeDAO;
import dao.MotivoDevolucionDAO;
import dto.Campana;
import dto.ConceptoDevolucion;
import dto.ConvenioPago;
import dto.Credito;
import dto.Devolucion;
import dto.Gestor;
import dto.Historial;
import dto.Marcaje;
import dto.MotivoDevolucion;
import impl.CampanaIMPL;
import impl.ConceptoDevolucionIMPL;
import impl.ConvenioPagoIMPL;
import impl.CreditoIMPL;
import impl.DevolucionIMPL;
import impl.GestorIMPL;
import impl.HistorialIMPL;
import impl.MarcajeIMPL;
import impl.MotivoDevolucionIMPL;
import java.io.IOException;
import java.io.Serializable;
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
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "cuentasBean")
@ViewScoped
public class CuentasBean implements Serializable {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");
  CreditoActualBean creditoActualBean = (CreditoActualBean) elContext.getELResolver().getValue(elContext, null, "creditoActualBean");

  // VARIABLES DE CLASE
  private String observaciones;
  private String colorSeleccionado;
  private ConceptoDevolucion conceptoSeleccionado;
  private MotivoDevolucion motivoSeleccionado;
  private List<Credito> creditosSeleccionados;
  private Gestor gestorSeleccionado;
  private Campana campanaSeleccionada;
  private Marcaje marcajeSeleccionado;
  private final CreditoDAO creditoDao;
  private final DevolucionDAO devolucionDao;
  private final HistorialDAO historialDao;
  private final ConceptoDevolucionDAO conceptoDevolucionDao;
  private final MotivoDevolucionDAO motivoDevolucionDao;
  private final GestorDAO gestorDao;
  private final CampanaDAO campanaDao;
  private final MarcajeDAO marcajeDao;
  private final ConvenioPagoDAO convenioPagoDao;
  private List<Credito> listaCreditos;
  private List<ConceptoDevolucion> listaConceptos;
  private List<MotivoDevolucion> listaMotivos;
  private List<Gestor> listaGestores;
  private List<Campana> listaCampanas;
  private List<Marcaje> listaMarcajes;
  private List<CuentasGestor> listaCuentasGestor;

  //CONSTRUCTOR
  public CuentasBean() {
    creditosSeleccionados = new ArrayList();
    conceptoSeleccionado = new ConceptoDevolucion();
    motivoSeleccionado = new MotivoDevolucion();
    gestorSeleccionado = new Gestor();
    campanaSeleccionada = new Campana();
    marcajeSeleccionado = new Marcaje();
    creditoDao = new CreditoIMPL();
    devolucionDao = new DevolucionIMPL();
    historialDao = new HistorialIMPL();
    conceptoDevolucionDao = new ConceptoDevolucionIMPL();
    motivoDevolucionDao = new MotivoDevolucionIMPL();
    gestorDao = new GestorIMPL();
    campanaDao = new CampanaIMPL();
    marcajeDao = new MarcajeIMPL();
    convenioPagoDao = new ConvenioPagoIMPL();
    listaCreditos = new ArrayList();
    listaConceptos = new ArrayList();
    listaMotivos = new ArrayList();
    listaCuentasGestor = new ArrayList();
    obtenerListas();
  }

  // METODO QUE OBTIENE LA LISTA DE CREDITOS Y DE CONCEPTOS DE DEVOLUCION
  public final void obtenerListas() {
    listaGestores = gestorDao.buscarPorDespacho(indexBean.getUsuario().getDespacho().getIdDespacho());
    listaCampanas = campanaDao.buscarTodas();
    listaMarcajes = marcajeDao.buscarTodos();
    listaConceptos = conceptoDevolucionDao.obtenerConceptos();
    conceptoSeleccionado = new ConceptoDevolucion();
    motivoSeleccionado = new MotivoDevolucion();
    observaciones = "";
    obtenerCuentasGestor();
  }
  
  // METODO QUE OBTIENE LA LISTA DE CUENTAS GESTOR
  public void obtenerCuentasGestor(){
    for (int i = 0; i <(listaGestores.size()); i++) {
      CuentasGestor cg = new CuentasGestor();
      cg.setGestor(listaGestores.get(i));
      cg.setCuentas(creditoDao.contarCreditosActivosSinQuebrantoPermanenciaPorGestor(indexBean.getUsuario().getDespacho().getIdDespacho(), listaGestores.get(i).getIdGestor()));
      cg.setCuentasQuebranto(creditoDao.contarCreditosActivosQuebrantoPermanenciaPorGestor(indexBean.getUsuario().getDespacho().getIdDespacho(), listaGestores.get(i).getIdGestor()));
      cg.setMontoCuentas(creditoDao.calcularMontoPorRecuperarSinQuebrantoPermanenciaGestor(indexBean.getUsuario().getDespacho().getIdDespacho(), listaGestores.get(i).getIdGestor()));
      cg.setMontoCuentasQuebranto(creditoDao.calcularMontoPorRecuperarQuebrantoPermanenciaGestor(indexBean.getUsuario().getDespacho().getIdDespacho(), listaGestores.get(i).getIdGestor()));
      cg.setMontoTotal(cg.getMontoCuentas() + cg.getMontoCuentasQuebranto());
      listaCuentasGestor.add(cg);
    }
  }

  // METODO QUE ABRE LA VISTA DEL DETALLE DEL CREDITO
  public void selectorDeVista() {
    if (!creditosSeleccionados.isEmpty()) {
      creditoActualBean.setCreditoActual(creditosSeleccionados.get(0));
      try {
        FacesContext.getCurrentInstance().getExternalContext().redirect("vistaCreditoAdmin.xhtml");
      } catch (IOException ioe) {
        Logs.log.error("No se pudo redirigir a la vista de credito del administrador.");
        Logs.log.error(ioe);
      }
    } else {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No ha seleccionado ningun credito"));
    }
  }

  // METODO QUE PREPARA LA LISTA DE MOTIVOS DE DEVOLUCION DEPENDIENDO DEL CONCEPTO SELECCIONADO
  public void preparaMotivos() {
    listaMotivos = motivoDevolucionDao.obtenerMotivosPorConcepto(conceptoSeleccionado.getIdConceptoDevolucion());
  }

  // METODO PARA DEVOLVER UN CREDITO DIRECTAMENTE (ADMINISTRADOR ENVIA A DEVOLUCION)
  public void devolverDirecto() {
    boolean ok;
    if ((conceptoSeleccionado.getIdConceptoDevolucion() != 0) && (motivoSeleccionado.getIdMotivoDevolucion() != 0)) {
      Devolucion d = new Devolucion();
      d.setEstatus(Devoluciones.DEVUELTO);
      d.setFecha(new Date());
      conceptoSeleccionado = conceptoDevolucionDao.buscarPorId(conceptoSeleccionado.getIdConceptoDevolucion());
      d.setConceptoDevolucion(conceptoSeleccionado);
      motivoSeleccionado = motivoDevolucionDao.buscarPorId(motivoSeleccionado.getIdMotivoDevolucion());
      d.setMotivoDevolucion(motivoSeleccionado);
      d.setCredito(creditosSeleccionados.get(0));
      d.setObservaciones(observaciones);
      d.setSolicitante(indexBean.getUsuario().getNombreLogin());
      d.setRevisor(indexBean.getUsuario().getNombreLogin());
      Historial h = new Historial();
      h.setEvento("El administrador: " + indexBean.getUsuario().getNombreLogin() + ", devolvio el credito");
      h.setCredito(creditosSeleccionados.get(0));
      h.setFecha(new Date());
      ok = devolucionDao.insertar(d) && historialDao.insertar(h);
      if (ok) {
        RequestContext con = RequestContext.getCurrentInstance();
        obtenerListas();
        con.execute("PF('confirmacionDialog').hide();");
        con.update("cuentas");
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se devolvio el credito seleccionado"));
      } else {
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo devolver el credito. Contacte con el administrador de base de datos"));
      }
    }
  }

  // METODO QUE REASIGNA LOS CREDITOS SELECCIONADOS
  public void reasignarGestor(List<Credito> creditos) {
    if (creditos.isEmpty()) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "Debe seleccionar al menos un credito."));
    } else {
      boolean okGeneral = true;
      for (int i = 0; i < (creditos.size()); i++) {
        // GESTION AUTOMATICA 1
        // SI EXISTE UN CONVENIO
        ConvenioPago c = convenioPagoDao.buscarConvenioEnCursoCredito(creditos.get(i).getIdCredito());
        GestionAutomatica ga = new GestionAutomatica();
        if (c != null) {
          ga.generarGestionAutomatica("13CONRE", creditos.get(i), indexBean.getUsuario(), "SE REASIGNA CONVENIO");
        }
        // CAMBIAMOS EL GESTOR ASIGNADO ACTUALMENTE
        String gestorAnterior = creditos.get(i).getGestor().getUsuario().getNombreLogin();
        creditos.get(i).setGestor(gestorDao.buscar(gestorSeleccionado.getIdGestor()));
        boolean ok = creditoDao.editar(creditos.get(i));
        if (ok) {
          okGeneral = okGeneral & ok;
          // GESTION AUTOMATICA 2
          ga.generarGestionAutomatica("15CTARE", creditos.get(i), indexBean.getUsuario(), "REASIGNACION DE CREDITO NO. " + creditos.get(i).getNumeroCredito());
          // GUARDAMOS EN EL LOG EL DETALLE DE LA REASIGNACION
          Logs.log.info("El administrador: " + indexBean.getUsuario().getNombreLogin() + " reasigno el credito del gestor " + gestorAnterior + " al gestor " + creditos.get(i).getGestor().getUsuario().getNombreLogin());
          // ESCRIBIMOS EN EL HISTORIAL LA REASIGNACION
          Historial h = new Historial();
          h.setCredito(creditos.get(i));
          h.setFecha(new Date());
          h.setEvento("El administrador: " + indexBean.getUsuario().getNombreLogin() + " reasigno el credito al gestor: " + creditos.get(i).getGestor().getUsuario().getNombreLogin());
          ok = ok & (historialDao.insertar(h));
          if (!ok) {
            Logs.log.error("No se actualizo el historial en la reasignacion del credito");
          }
        } else {
          okGeneral = false;
          break;
        }
      }
      if (okGeneral) {
        if (creditos.size() == 1) {
          FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se reasigno el credito."));
        } else {
          FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se reasignaron los creditos."));
        }
        listaCreditos = new ArrayList();
      } else {
        if (creditos.size() == 1) {
          FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se reasigno el credito. Contacte al equipo de sistemas."));
        } else {
          FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se reasignaron los creditos. Contacte al equipo de sistemas."));
        }
      }
    }
  }

  // METODO QUE OBTIENE EL SALDO VENCIDO DEL CREDITO
  public float calcularSaldoVencido(Credito credito) {
    float f = creditoDao.buscarSaldoVencidoCredito(credito.getIdCredito());
    if (f == 0) {
      return 0;
    } else {
      return f;
    }
  }

  // METODO QUE OBTIENE LA LISTA DE CREDITOS SEGUN LOS PARAMETROS DESEADOS
  public void prepararCreditos() {
    String consulta = "SELECT * FROM credito WHERE id_despacho = " + indexBean.getUsuario().getDespacho().getIdDespacho();
    if (gestorSeleccionado.getIdGestor() != 0) {
      consulta = consulta + " AND id_gestor = " + gestorSeleccionado.getIdGestor();
    }
    if (campanaSeleccionada.getIdCampana() != 0) {
      consulta = consulta + " AND id_campana = " + campanaSeleccionada.getIdCampana();
    }
    if (marcajeSeleccionado.getIdMarcaje() != 0) {
      consulta = consulta + " AND id_marcaje = " + marcajeSeleccionado.getIdMarcaje();
    }
    if (!colorSeleccionado.equals("0")) {
      switch (colorSeleccionado) {
        case "Verde":
          consulta = consulta + " AND id_credito IN (SELECT DISTINCT id_credito FROM gestion WHERE DATE(fecha) >= DATE_SUB(CURDATE(), INTERVAL 3 DAY))";
          break;
        case "Amarillo":
          consulta = consulta + " AND id_credito IN (SELECT DISTINCT id_credito FROM gestion WHERE DATE(fecha) >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) AND DATE(fecha) < DATE_SUB(CURDATE(), INTERVAL 3 DAY))";
          break;
        case "Rojo":
          consulta = consulta + " AND id_credito NOT IN (SELECT DISTINCT id_credito FROM gestion WHERE DATE(fecha) > DATE_SUB(CURDATE(), INTERVAL 7 DAY))";
          break;
      }
    }
    consulta = consulta + " AND id_credito NOT IN (SELECT id_credito FROM devolucion WHERE estatus IN (" + Devoluciones.DEVUELTO + ", " + Devoluciones.PENDIENTE + "));";
    listaCreditos = creditoDao.busquedaEspecialCreditos(consulta);
  }

  // SETTERS & GETTERS
  public List<Credito> getListaCreditos() {
    return listaCreditos;
  }

  public void setListaCreditos(List<Credito> listaCreditos) {
    this.listaCreditos = listaCreditos;
  }

  public List<Credito> getCreditosSeleccionados() {
    return creditosSeleccionados;
  }

  public void setCreditosSeleccionados(List<Credito> creditosSeleccionados) {
    this.creditosSeleccionados = creditosSeleccionados;
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

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public List<MotivoDevolucion> getListaMotivos() {
    return listaMotivos;
  }

  public void setListaMotivos(List<MotivoDevolucion> listaMotivos) {
    this.listaMotivos = listaMotivos;
  }

  public MotivoDevolucion getMotivoSeleccionado() {
    return motivoSeleccionado;
  }

  public void setMotivoSeleccionado(MotivoDevolucion motivoSeleccionado) {
    this.motivoSeleccionado = motivoSeleccionado;
  }

  public Gestor getGestorSeleccionado() {
    return gestorSeleccionado;
  }

  public void setGestorSeleccionado(Gestor gestorSeleccionado) {
    this.gestorSeleccionado = gestorSeleccionado;
  }

  public Campana getCampanaSeleccionada() {
    return campanaSeleccionada;
  }

  public void setCampanaSeleccionada(Campana campanaSeleccionada) {
    this.campanaSeleccionada = campanaSeleccionada;
  }

  public Marcaje getMarcajeSeleccionado() {
    return marcajeSeleccionado;
  }

  public void setMarcajeSeleccionado(Marcaje marcajeSeleccionado) {
    this.marcajeSeleccionado = marcajeSeleccionado;
  }

  public List<Gestor> getListaGestores() {
    return listaGestores;
  }

  public void setListaGestores(List<Gestor> listaGestores) {
    this.listaGestores = listaGestores;
  }

  public List<Campana> getListaCampanas() {
    return listaCampanas;
  }

  public void setListaCampanas(List<Campana> listaCampanas) {
    this.listaCampanas = listaCampanas;
  }

  public List<Marcaje> getListaMarcajes() {
    return listaMarcajes;
  }

  public void setListaMarcajes(List<Marcaje> listaMarcajes) {
    this.listaMarcajes = listaMarcajes;
  }

  public String getColorSeleccionado() {
    return colorSeleccionado;
  }

  public void setColorSeleccionado(String colorSeleccionado) {
    this.colorSeleccionado = colorSeleccionado;
  }

  public List<CuentasGestor> getListaCuentasGestor() {
    return listaCuentasGestor;
  }

  public void setListaCuentasGestor(List<CuentasGestor> listaCuentasGestor) {
    this.listaCuentasGestor = listaCuentasGestor;
  }

  // CLASE MIEMBRO QUE GUARDA LOS DATOS DE CUENTAS Y MONTOS POR GESTOR
  public class CuentasGestor{
    
    // VARIABLES DE CLASE
    private int cuentas;
    private int cuentasQuebranto;
    private float montoCuentas;
    private float montoCuentasQuebranto;
    private float montoTotal;
    private Gestor gestor;

    // CONSTRUCTOR
    public CuentasGestor() {
    }
    
    // GETTERS & SETTERS
    public int getCuentas() {
      return cuentas;
    }

    public void setCuentas(int cuentas) {
      this.cuentas = cuentas;
    }

    public int getCuentasQuebranto() {
      return cuentasQuebranto;
    }

    public void setCuentasQuebranto(int cuentasQuebranto) {
      this.cuentasQuebranto = cuentasQuebranto;
    }

    public float getMontoCuentas() {
      return montoCuentas;
    }

    public void setMontoCuentas(float montoCuentas) {
      this.montoCuentas = montoCuentas;
    }

    public float getMontoCuentasQuebranto() {
      return montoCuentasQuebranto;
    }

    public void setMontoCuentasQuebranto(float montoCuentasQuebranto) {
      this.montoCuentasQuebranto = montoCuentasQuebranto;
    }

    public float getMontoTotal() {
      return montoTotal;
    }

    public void setMontoTotal(float montoTotal) {
      this.montoTotal = montoTotal;
    }

    public Gestor getGestor() {
      return gestor;
    }

    public void setGestor(Gestor gestor) {
      this.gestor = gestor;
    }
    
  }
  
}
