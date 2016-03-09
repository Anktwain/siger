/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.AsuntoGestionDAO;
import dao.CreditoDAO;
import dao.DescripcionGestionDAO;
import dao.DondeGestionDAO;
import dao.EstatusInformativoDAO;
import dao.GestionDAO;
import dao.QuienGestionDAO;
import dao.TipoGestionDAO;
import dao.TipoQuienGestionDAO;
import dto.AsuntoGestion;
import dto.Credito;
import dto.DescripcionGestion;
import dto.DondeGestion;
import dto.EstatusInformativo;
import dto.Gestion;
import dto.QuienGestion;
import dto.TipoGestion;
import dto.TipoQuienGestion;
import impl.AsuntoGestionIMPL;
import impl.CreditoIMPL;
import impl.DescripcionGestionIMPL;
import impl.DondeGestionIMPL;
import impl.EstatusInformativoIMPL;
import impl.GestionIMPL;
import impl.QuienGestionIMPL;
import impl.TipoGestionIMPL;
import impl.TipoQuienGestionIMPL;
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
import util.constantes.Marcajes;
import util.constantes.Perfiles;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "gestionBean")
@ViewScoped

public class GestionBean implements Serializable {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");
  ObtenerOracionCompletaGestionBean obtenerOracionCompletaGestionBean = (ObtenerOracionCompletaGestionBean) elContext.getELResolver().getValue(elContext, null, "obtenerOracionCompletaBean");

  // VARIABLES DE CLASE
  private VistaCreditoBean vistaCreditoBean;
  private CuentasGestorBean cuentasGestorBean;
  private List<TipoGestion> listaTipos;
  private List<DondeGestion> listaDonde;
  private List<AsuntoGestion> listaAsuntos;
  private List<DescripcionGestion> listaDescripciones;
  private List<TipoQuienGestion> listaTipoSujetos;
  private List<QuienGestion> listaSujetos;
  private List<EstatusInformativo> listaEstatus;
  private final Gestion nueva;
  private TipoGestion tipoSeleccionado;
  private DondeGestion dondeSeleccionado;
  private AsuntoGestion asuntoSeleccionado;
  private DescripcionGestion descripcionSeleccionada;
  private TipoQuienGestion tipoSujetoSeleccionado;
  private QuienGestion sujetoSeleccionado;
  private EstatusInformativo estatusSeleccionado;
  private String gestion;
  private final GestionDAO gestionDao;
  private final EstatusInformativoDAO estatusInformativoDao;
  private final TipoGestionDAO tipoGestionDao;
  private final DondeGestionDAO dondeGestionDao;
  private final AsuntoGestionDAO asuntoGestionDao;
  private final TipoQuienGestionDAO tipoQuienGestionDao;
  private final QuienGestionDAO quienGestionDao;
  private final DescripcionGestionDAO descripcionGestionDao;
  private final CreditoDAO creditoDao;
  private Credito creditoActual;

  // CONSTRUCTOR
  public GestionBean() {
    creditoActual = new Credito();
    listaTipos = new ArrayList();
    listaDonde = new ArrayList();
    listaAsuntos = new ArrayList();
    listaTipoSujetos = new ArrayList();
    listaSujetos = new ArrayList();
    listaEstatus = new ArrayList();
    listaDescripciones = new ArrayList();
    gestionDao = new GestionIMPL();
    estatusInformativoDao = new EstatusInformativoIMPL();
    tipoGestionDao = new TipoGestionIMPL();
    dondeGestionDao = new DondeGestionIMPL();
    asuntoGestionDao = new AsuntoGestionIMPL();
    descripcionGestionDao = new DescripcionGestionIMPL();
    tipoQuienGestionDao = new TipoQuienGestionIMPL();
    quienGestionDao = new QuienGestionIMPL();
    creditoDao = new CreditoIMPL();
    tipoSeleccionado = new TipoGestion();
    dondeSeleccionado = new DondeGestion();
    asuntoSeleccionado = new AsuntoGestion();
    tipoSujetoSeleccionado = new TipoQuienGestion();
    sujetoSeleccionado = new QuienGestion();
    estatusSeleccionado = new EstatusInformativo();
    descripcionSeleccionada = new DescripcionGestion();
    nueva = new Gestion();
    listaTipos = tipoGestionDao.buscarTodo();
    obtenerBeanActual();
  }

  // METODO QUE OBTIENE EL CREDITO ACTUAL DEPENDIENDO DE LA VISTA
  public final void obtenerBeanActual() {
    if (indexBean.getUsuario().getPerfil() == Perfiles.GESTOR) {
      cuentasGestorBean = (CuentasGestorBean) elContext.getELResolver().getValue(elContext, null, "cuentasGestorBean");
      creditoActual = cuentasGestorBean.getCreditoActual();
    } else {
      vistaCreditoBean = (VistaCreditoBean) elContext.getELResolver().getValue(elContext, null, "vistaCreditoBean");
      creditoActual = vistaCreditoBean.getCreditoActual();
    }
  }

  public void preparaDonde() {
    tipoSeleccionado = tipoGestionDao.buscarPorId(tipoSeleccionado.getIdTipoGestion());
    listaDonde = dondeGestionDao.buscarPorTipoGestion(tipoSeleccionado.getIdTipoGestion());
  }

  public void preparaAsunto() {
    dondeSeleccionado = dondeGestionDao.buscarPorId(dondeSeleccionado.getIdDondeGestion());
    listaAsuntos = asuntoGestionDao.buscarPorTipoGestion(tipoSeleccionado.getIdTipoGestion());
  }

  public void preparaDescripcion() {
    asuntoSeleccionado = asuntoGestionDao.buscarPorId(asuntoSeleccionado.getIdAsuntoGestion());
    listaDescripciones = descripcionGestionDao.buscarPorAsuntoTipo(tipoSeleccionado.getIdTipoGestion(), asuntoSeleccionado.getIdAsuntoGestion());
  }

  public void preparaTipoSujeto() {
    descripcionSeleccionada = descripcionGestionDao.buscarPorId(descripcionSeleccionada.getIdDescripcionGestion());
    listaTipoSujetos = tipoQuienGestionDao.buscarPorAsuntoDescripcion(asuntoSeleccionado.getTipoAsuntoGestion().getClaveAsunto(), descripcionSeleccionada.getAbreviatura());
  }

  public void preparaSujetos() {
    tipoSujetoSeleccionado = tipoQuienGestionDao.buscarPorId(tipoSujetoSeleccionado.getIdTipoQuienGestion());
    listaSujetos = quienGestionDao.buscarPorTipoQuien(tipoSujetoSeleccionado.getIdTipoQuienGestion());
  }

  public void preparaEstatus() {
    sujetoSeleccionado = quienGestionDao.buscarPorId(sujetoSeleccionado.getIdQuienGestion());
    listaEstatus = estatusInformativoDao.buscarTodos();
  }

  public void crearNuevaGestion() {
    preparaGestion();
    boolean ok = verificaMarcaje();
    ok = ok & gestionDao.insertarGestion(nueva);
    FacesContext contexto = FacesContext.getCurrentInstance();
    if (ok) {
      limpiarCampos();
      RequestContext.getCurrentInstance().update("@all");
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se guardo la gestion: "));
    } else {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se guardo la gestion. Contacte al equipo de sistemas."));
    }
  }

  // METODO QUE MUESTRA LA ORACION COMPLETA
  public void preparaGestion() {
    estatusSeleccionado = estatusInformativoDao.buscar(estatusSeleccionado.getIdEstatusInformativo());
    nueva.setTipoGestion(tipoSeleccionado);
    nueva.setDondeGestion(dondeSeleccionado);
    nueva.setAsuntoGestion(asuntoSeleccionado);
    nueva.setDescripcionGestion(descripcionSeleccionada);
    nueva.setTipoQuienGestion(tipoSujetoSeleccionado);
    nueva.setQuienGestion(sujetoSeleccionado);
    nueva.setGestion(gestion.toUpperCase());
    nueva.setEstatusInformativo(estatusSeleccionado);
    Date fecha = new Date();
    nueva.setFecha(fecha);
    nueva.setCredito(creditoActual);
    nueva.setUsuario(indexBean.getUsuario());
  }

  // METODO QUE VERIFICA SI LA GESTION QUITA EL MARCAJE A LAS CUENTAS
  public boolean verificaMarcaje() {
    //TOFIX AQUI
    int marcaje = creditoActual.getMarcaje();
    boolean ok = true;
    // APAGAR MARCAJE
    switch (marcaje) {
      case 1:
        // CORREO ORDINARIO
        if (descripcionSeleccionada.getAbreviatura().equals("4CADE")) {
          Credito c = creditoActual;
          c.setMarcaje(Marcajes.SIN_MARCAJE);
          ok = creditoDao.editar(c);
        }
        break;
      case 2:
        // TELEGRAMA
        if ((descripcionSeleccionada.getAbreviatura().equals("3CETE")) || (descripcionSeleccionada.getAbreviatura().equals("21TELE3"))) {
          Credito c = creditoActual;
          c.setMarcaje(Marcajes.SIN_MARCAJE);
          ok = creditoDao.editar(c);
        }
        break;
      case 3:
        // VISITA
        if (nueva.getTipoGestion().getIdTipoGestion() == 1) {
          Credito c = creditoActual;
          c.setMarcaje(Marcajes.SIN_MARCAJE);
          ok = creditoDao.editar(c);
        }
        break;
      case 4:
        // CORREO ELECTRONICO
        if (descripcionSeleccionada.getAbreviatura().equals("2COEL")) {
          Credito c = creditoActual;
          c.setMarcaje(Marcajes.SIN_MARCAJE);
          ok = creditoDao.editar(c);
        }
        break;
      case 5:
        // LOCALIZACION
        if ((descripcionSeleccionada.getAbreviatura().contains("POSI")) && (tipoSeleccionado.getIdTipoGestion() == (1 | 2))) {
          Credito c = creditoActual;
          c.setMarcaje(Marcajes.SIN_MARCAJE);
          ok = creditoDao.editar(c);
        }
        if (descripcionSeleccionada.getAbreviatura().equals("2SISB")) {
          Credito c = creditoActual;
          c.setMarcaje(Marcajes.SIN_MARCAJE);
          ok = creditoDao.editar(c);
        }
        break;
      case 6:
        // INFORMACION DEL BANCO
        if (descripcionSeleccionada.getAbreviatura().equals("4CIAB")) {
          Credito c = creditoActual;
          c.setMarcaje(Marcajes.SIN_MARCAJE);
          ok = creditoDao.editar(c);
        }
        break;
      // PROXIMAMENTE CASO WHATSAPP
    }
    // ENCENDER MARCAJE
    // CASO CORREO ORDINARIO
    if (descripcionSeleccionada.getAbreviatura().equals("26CACO")) {
      Credito c = creditoActual;
      c.setMarcaje(Marcajes.CORREO_SEPOMEX);
      ok = creditoDao.editar(c);
    } // CASO TELEGRAMA
    else if ((descripcionSeleccionada.getAbreviatura().equals("19TELE1")) || (descripcionSeleccionada.getAbreviatura().equals("20TELE2"))) {
      Credito c = creditoActual;
      c.setMarcaje(Marcajes.TELEGRAMA);
      ok = creditoDao.editar(c);
    } // CASO VISITA
    else if (descripcionSeleccionada.getAbreviatura().equals("32RUVD")) {
      Credito c = creditoActual;
      c.setMarcaje(Marcajes.TELEGRAMA);
      ok = creditoDao.editar(c);
    } // CASO CORREO ELECTRONICO
    else if ((descripcionSeleccionada.getAbreviatura().equals("27CEBC")) || (descripcionSeleccionada.getAbreviatura().equals("28CEBCC")) || (descripcionSeleccionada.getAbreviatura().equals("29CEREQ")) || (descripcionSeleccionada.getAbreviatura().equals("30CENEG")) || (descripcionSeleccionada.getAbreviatura().equals("31CELIB"))) {
      Credito c = creditoActual;
      c.setMarcaje(Marcajes.CORREO_ELECTRONICO);
      ok = creditoDao.editar(c);
    } // CASO LOCALIZACION
    else if ((descripcionSeleccionada.getAbreviatura().equals("1SECC")) || (descripcionSeleccionada.getAbreviatura().equals("3NOSB")) || (descripcionSeleccionada.getAbreviatura().equals("4CADE"))) {
      Credito c = creditoActual;
      c.setMarcaje(Marcajes.LOCALIZACION);
      ok = creditoDao.editar(c);
    } // CASO INFORMACION DEL BANCO
    else if (descripcionSeleccionada.getAbreviatura().equals("33CEIB")) {
      Credito c = creditoActual;
      c.setMarcaje(Marcajes.ESPERA_INFORMACION_BANCO);
      ok = creditoDao.editar(c);
    } // CASO COBRO EN CELULAR
    else if (descripcionSeleccionada.getAbreviatura().equals("1SMSC")) {
      Credito c = creditoActual;
      c.setMarcaje(Marcajes.COBRO_EN_CELULAR);
      ok = creditoDao.editar(c);
    } // PROXIMAMENTE CASO WHATSAPP
    else {
    }
    return ok;
  }

  public void limpiarCampos() {
    listaTipos = tipoGestionDao.buscarTodo();
    listaDonde = new ArrayList();
    listaAsuntos = new ArrayList();
    listaTipoSujetos = new ArrayList();
    listaDescripciones = new ArrayList();
    listaSujetos = new ArrayList();
    listaEstatus = new ArrayList();
    gestion = "";
    RequestContext.getCurrentInstance().update("formNuevaGestion");
  }

  // ***********************************************************************************************************************
  // ***********************************************************************************************************************
  // ***********************************************************************************************************************
  // GETTERS & SETTERS
  public List<TipoGestion> getListaTipos() {
    return listaTipos;
  }

  public void setListaTipos(List<TipoGestion> listaTipos) {
    this.listaTipos = listaTipos;
  }

  public List<DondeGestion> getListaDonde() {
    return listaDonde;
  }

  public void setListaDonde(List<DondeGestion> listaDonde) {
    this.listaDonde = listaDonde;
  }

  public List<AsuntoGestion> getListaAsuntos() {
    return listaAsuntos;
  }

  public void setListaAsuntos(List<AsuntoGestion> listaAsuntos) {
    this.listaAsuntos = listaAsuntos;
  }

  public List<TipoQuienGestion> getListaTipoSujetos() {
    return listaTipoSujetos;
  }

  public void setListaTipoSujetos(List<TipoQuienGestion> listaTipoSujetos) {
    this.listaTipoSujetos = listaTipoSujetos;
  }

  public List<QuienGestion> getListaSujetos() {
    return listaSujetos;
  }

  public void setListaSujetos(List<QuienGestion> listaSujetos) {
    this.listaSujetos = listaSujetos;
  }

  public List<EstatusInformativo> getListaEstatus() {
    return listaEstatus;
  }

  public void setListaEstatus(List<EstatusInformativo> listaEstatus) {
    this.listaEstatus = listaEstatus;
  }

  public TipoGestion getTipoSeleccionado() {
    return tipoSeleccionado;
  }

  public void setTipoSeleccionado(TipoGestion tipoSeleccionado) {
    this.tipoSeleccionado = tipoSeleccionado;
  }

  public DondeGestion getDondeSeleccionado() {
    return dondeSeleccionado;
  }

  public void setDondeSeleccionado(DondeGestion dondeSeleccionado) {
    this.dondeSeleccionado = dondeSeleccionado;
  }

  public AsuntoGestion getAsuntoSeleccionado() {
    return asuntoSeleccionado;
  }

  public void setAsuntoSeleccionado(AsuntoGestion asuntoSeleccionado) {
    this.asuntoSeleccionado = asuntoSeleccionado;
  }

  public TipoQuienGestion getTipoSujetoSeleccionado() {
    return tipoSujetoSeleccionado;
  }

  public void setTipoSujetoSeleccionado(TipoQuienGestion tipoSujetoSeleccionado) {
    this.tipoSujetoSeleccionado = tipoSujetoSeleccionado;
  }

  public QuienGestion getSujetoSeleccionado() {
    return sujetoSeleccionado;
  }

  public void setSujetoSeleccionado(QuienGestion sujetoSeleccionado) {
    this.sujetoSeleccionado = sujetoSeleccionado;
  }

  public EstatusInformativo getEstatusSeleccionado() {
    return estatusSeleccionado;
  }

  public void setEstatusSeleccionado(EstatusInformativo estatusSeleccionado) {
    this.estatusSeleccionado = estatusSeleccionado;
  }

  public String getGestion() {
    return gestion;
  }

  public void setGestion(String gestion) {
    this.gestion = gestion;
  }

  public List<DescripcionGestion> getListaDescripciones() {
    return listaDescripciones;
  }

  public void setListaDescripciones(List<DescripcionGestion> listaDescripciones) {
    this.listaDescripciones = listaDescripciones;
  }

  public DescripcionGestion getDescripcionSeleccionada() {
    return descripcionSeleccionada;
  }

  public void setDescripcionSeleccionada(DescripcionGestion descripcionSeleccionada) {
    this.descripcionSeleccionada = descripcionSeleccionada;
  }

}
