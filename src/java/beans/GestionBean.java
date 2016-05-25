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
import dao.MarcajeDAO;
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
import impl.MarcajeIMPL;
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
  CreditoActualBean creditoActualBean = (CreditoActualBean) elContext.getELResolver().getValue(elContext, null, "creditoActualBean");
  ObtenerOracionCompletaGestionBean obtenerOracionCompletaGestionBean = (ObtenerOracionCompletaGestionBean) elContext.getELResolver().getValue(elContext, null, "obtenerOracionCompletaBean");

  // VARIABLES DE CLASE
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
  private final MarcajeDAO marcajeDao;
  private final Credito creditoActual;

  // CONSTRUCTOR
  public GestionBean() {
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
    marcajeDao = new MarcajeIMPL();
    tipoSeleccionado = new TipoGestion();
    dondeSeleccionado = new DondeGestion();
    asuntoSeleccionado = new AsuntoGestion();
    tipoSujetoSeleccionado = new TipoQuienGestion();
    sujetoSeleccionado = new QuienGestion();
    estatusSeleccionado = new EstatusInformativo();
    descripcionSeleccionada = new DescripcionGestion();
    nueva = new Gestion();
    listaTipos = tipoGestionDao.buscarTodo();
    creditoActual = creditoActualBean.getCreditoActual();
  }

  // METODO QUE PREPARA EL COMBOBOX DONDE GESTION
  public void preparaDonde() {
    tipoSeleccionado = tipoGestionDao.buscarPorId(tipoSeleccionado.getIdTipoGestion());
    listaDonde = dondeGestionDao.buscarPorTipoGestion(tipoSeleccionado.getIdTipoGestion());
    if (listaDonde.size() == 1) {
      dondeSeleccionado = listaDonde.get(0);
      preparaAsunto();
    }
  }

  // METODO QUE PREPARA EL COMBOBOX ASUNTO GESTION
  public void preparaAsunto() {
    dondeSeleccionado = dondeGestionDao.buscarPorId(dondeSeleccionado.getIdDondeGestion());
    listaAsuntos = asuntoGestionDao.buscarPorTipoGestion(tipoSeleccionado.getIdTipoGestion());
    if (listaAsuntos.size() == 1) {
      asuntoSeleccionado = listaAsuntos.get(0);
      preparaDescripcion();
    }
  }

  // METODO QUE PREPARA EL COMBOBOX DESCRIPCION GESTION
  public void preparaDescripcion() {
    asuntoSeleccionado = asuntoGestionDao.buscarPorId(asuntoSeleccionado.getIdAsuntoGestion());
    listaDescripciones = descripcionGestionDao.buscarPorAsuntoTipo(tipoSeleccionado.getIdTipoGestion(), asuntoSeleccionado.getIdAsuntoGestion());
    if (listaDescripciones.size() == 1) {
      descripcionSeleccionada = listaDescripciones.get(0);
      preparaTipoSujeto();
    }
  }

  // METODO QUE PREPARA EL COMBOBOX TIPO SUJETO GESTION
  public void preparaTipoSujeto() {
    descripcionSeleccionada = descripcionGestionDao.buscarPorId(descripcionSeleccionada.getIdDescripcionGestion());
    tipoSujetoSeleccionado = new TipoQuienGestion();
    sujetoSeleccionado = new QuienGestion();
    listaTipoSujetos = tipoQuienGestionDao.buscarPorAsuntoDescripcion(asuntoSeleccionado.getTipoAsuntoGestion().getClaveAsunto(), descripcionSeleccionada.getAbreviatura());
    if (listaTipoSujetos.get(0).getDescripcion().equals("NO APLICA")) {
      tipoSujetoSeleccionado = tipoQuienGestionDao.buscarPorId(12);
      preparaSujetos();
      sujetoSeleccionado = quienGestionDao.buscarPorId(89);
      preparaEstatus();
    }
    switch (descripcionSeleccionada.getTextoGestion()) {
      case "NO CONTESTA":
        gestion = descripcionSeleccionada.getTextoGestion();
        break;
      case "GRABACION INDICA QUE NO EXISTE":
        gestion = "NUMERO NO EXISTE";
        break;
    }
  }

  // METODO QUE PREPARA EL COMBOBOX SUJETO GESTION
  public void preparaSujetos() {
    tipoSujetoSeleccionado = tipoQuienGestionDao.buscarPorId(tipoSujetoSeleccionado.getIdTipoQuienGestion());
    listaSujetos = quienGestionDao.buscarPorTipoQuien(tipoSujetoSeleccionado.getIdTipoQuienGestion());
  }

  // METODO QUE PREPARA EL COMBOBOX DE ESTATUS DEL BANCO
  public void preparaEstatus() {
    sujetoSeleccionado = quienGestionDao.buscarPorId(sujetoSeleccionado.getIdQuienGestion());
    listaEstatus = seleccionadorEstatus();
    if (listaEstatus.size() == 1) {
      estatusSeleccionado = listaEstatus.get(0);
    }
  }

  // METODO QUE SELECCIONA EL ESTATUS DEL BANCO DEPENDIENDO DE LA GESTION ACTUAL
  public List<EstatusInformativo> seleccionadorEstatus() {
    List<EstatusInformativo> estatusPosibles = new ArrayList();
    listaEstatus = estatusPosibles;
    switch (tipoSeleccionado.getIdTipoGestion()) {
      case 1:
        switch (asuntoSeleccionado.getTipoAsuntoGestion().getClaveAsunto()) {
          case 2:
            if (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 1) {
              estatusPosibles.add(estatusInformativoDao.buscar(5));
            } else if ((tipoSujetoSeleccionado.getIdTipoQuienGestion() == 2) || (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 3) || (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 4)) {
              estatusPosibles.add(estatusInformativoDao.buscar(10));
            } else if ((tipoSujetoSeleccionado.getIdTipoQuienGestion() == 5) || (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 6) || (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 7)) {
              estatusPosibles.add(estatusInformativoDao.buscar(11));
            } else {
              estatusPosibles.add(estatusInformativoDao.buscar(6));
            }
            break;
          case 3:
            estatusPosibles.add(estatusInformativoDao.buscar(21));
            break;
          case 4:
            estatusPosibles.add(estatusInformativoDao.buscar(19));
            break;
          case 6:
            estatusPosibles.add(estatusInformativoDao.buscar(16));
            break;
          case 7:
          case 11:
            if (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 1) {
              estatusPosibles.add(estatusInformativoDao.buscar(5));
            } else {
              estatusPosibles.add(estatusInformativoDao.buscar(6));
            }
            break;
          case 8:
            switch (descripcionSeleccionada.getAbreviatura()) {
              case "1CONV":
                estatusPosibles.add(estatusInformativoDao.buscar(1));
                break;
              case "2CONV":
              case "3CONV":
                estatusPosibles.add(estatusInformativoDao.buscar(2));
                break;
            }
            break;
        }
        break;
      case 2:
        switch (asuntoSeleccionado.getTipoAsuntoGestion().getClaveAsunto()) {
          case 1:
            switch (descripcionSeleccionada.getAbreviatura()) {
              case "1PRTE":
              case "2PRTE":
              case "3PRTE":
                estatusPosibles.add(estatusInformativoDao.buscar(13));
                break;
              case "5PRTE":
                estatusPosibles.add(estatusInformativoDao.buscar(14));
                break;
              case "4PRTE":
              case "6PRTE":
              case "9PRTE":
              case "10PRTE":
              case "11PRTE":
                estatusPosibles.add(estatusInformativoDao.buscar(15));
                break;
              case "15PRTE":
                estatusPosibles.add(estatusInformativoDao.buscar(20));
                break;
              default:
                estatusPosibles.add(estatusInformativoDao.buscar(9));
                estatusPosibles.add(estatusInformativoDao.buscar(19));
                break;
            }
            break;
          case 2:
            if (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 1) {
              estatusPosibles.add(estatusInformativoDao.buscar(5));
            } else if ((tipoSujetoSeleccionado.getIdTipoQuienGestion() == 2) || (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 3) || (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 4)) {
              estatusPosibles.add(estatusInformativoDao.buscar(10));
            } else if ((tipoSujetoSeleccionado.getIdTipoQuienGestion() == 5) || (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 6) || (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 7)) {
              estatusPosibles.add(estatusInformativoDao.buscar(11));
            } else {
              estatusPosibles.add(estatusInformativoDao.buscar(6));
            }
            break;
          case 3:
            estatusPosibles.add(estatusInformativoDao.buscar(21));
            break;
          case 4:
            estatusPosibles.add(estatusInformativoDao.buscar(19));
            break;
          case 6:
            estatusPosibles.add(estatusInformativoDao.buscar(16));
            break;
          case 7:
          case 11:
            if (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 1) {
              estatusPosibles.add(estatusInformativoDao.buscar(5));
            } else {
              estatusPosibles.add(estatusInformativoDao.buscar(6));
            }
            break;
          case 8:
            switch (descripcionSeleccionada.getAbreviatura()) {
              case "1CONV":
                estatusPosibles.add(estatusInformativoDao.buscar(1));
                break;
              case "2CONV":
              case "3CONV":
                estatusPosibles.add(estatusInformativoDao.buscar(2));
                break;
            }
            break;
        }
        break;
      case 3:
        switch (asuntoSeleccionado.getTipoAsuntoGestion().getClaveAsunto()) {
          case 2:
            if (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 1) {
              estatusPosibles.add(estatusInformativoDao.buscar(5));
            } else if ((tipoSujetoSeleccionado.getIdTipoQuienGestion() == 2) || (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 3) || (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 4)) {
              estatusPosibles.add(estatusInformativoDao.buscar(10));
            } else if ((tipoSujetoSeleccionado.getIdTipoQuienGestion() == 5) || (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 6) || (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 7)) {
              estatusPosibles.add(estatusInformativoDao.buscar(11));
            } else {
              estatusPosibles.add(estatusInformativoDao.buscar(6));
            }
            break;
          case 3:
            estatusPosibles.add(estatusInformativoDao.buscar(21));
            break;
          case 4:
            estatusPosibles.add(estatusInformativoDao.buscar(19));
            break;
          case 6:
            estatusPosibles.add(estatusInformativoDao.buscar(16));
            break;
          case 7:
          case 11:
          case 13:
            if (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 1) {
              estatusPosibles.add(estatusInformativoDao.buscar(5));
            } else {
              estatusPosibles.add(estatusInformativoDao.buscar(6));
            }
            break;
          case 8:
            switch (descripcionSeleccionada.getAbreviatura()) {
              case "1CONV":
                estatusPosibles.add(estatusInformativoDao.buscar(1));
                break;
              case "2CONV":
              case "3CONV":
                estatusPosibles.add(estatusInformativoDao.buscar(2));
                break;
            }
            break;
        }
        break;
      case 4:
        switch (asuntoSeleccionado.getTipoAsuntoGestion().getClaveAsunto()) {
          case 12:
            if (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 1) {
              estatusPosibles.add(estatusInformativoDao.buscar(5));
            } else {
              estatusPosibles.add(estatusInformativoDao.buscar(6));
            }
            break;
          case 14:
          case 15:
            estatusPosibles.add(estatusInformativoDao.buscar(16));
            break;
          case 16:
            if (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 1) {
              estatusPosibles.add(estatusInformativoDao.buscar(5));
            } else if ((tipoSujetoSeleccionado.getIdTipoQuienGestion() == 2) || (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 3) || (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 4)) {
              estatusPosibles.add(estatusInformativoDao.buscar(10));
            } else if ((tipoSujetoSeleccionado.getIdTipoQuienGestion() == 5) || (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 6) || (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 7)) {
              estatusPosibles.add(estatusInformativoDao.buscar(11));
            } else {
              estatusPosibles.add(estatusInformativoDao.buscar(6));
              estatusPosibles.add(estatusInformativoDao.buscar(7));
            }
            break;
          case 17:
          case 18:
            estatusPosibles.add(estatusInformativoDao.buscar(12));
            break;
          case 19:
            switch (descripcionSeleccionada.getAbreviatura()) {
              case "1WHCOMP":
                if (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 1) {
                  estatusPosibles.add(estatusInformativoDao.buscar(5));
                } else {
                  estatusPosibles.add(estatusInformativoDao.buscar(18));
                }
                break;
              case "2WHREC":
                if (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 1) {
                  estatusPosibles.add(estatusInformativoDao.buscar(5));
                } else if ((tipoSujetoSeleccionado.getIdTipoQuienGestion() == 2) || (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 3) || (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 4)) {
                  estatusPosibles.add(estatusInformativoDao.buscar(10));
                } else if ((tipoSujetoSeleccionado.getIdTipoQuienGestion() == 5) || (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 6) || (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 7)) {
                  estatusPosibles.add(estatusInformativoDao.buscar(11));
                } else {
                  estatusPosibles.add(estatusInformativoDao.buscar(6));
                }
                break;
              case "3WHREPA":
                if (tipoSujetoSeleccionado.getIdTipoQuienGestion() == 1) {
                  estatusPosibles.add(estatusInformativoDao.buscar(5));
                } else {
                  estatusPosibles.add(estatusInformativoDao.buscar(6));
                }
                break;
            }
        }
        break;
    }
    if (listaEstatus.isEmpty()) {
      listaEstatus = estatusInformativoDao.buscarTodos();
    }
    return estatusPosibles;
  }

  public void crearNuevaGestion() {
    preparaGestion();
    boolean ok = verificaMarcaje();
    ok = ok & gestionDao.insertarGestion(nueva);
    if (ok) {
      limpiarCampos();
      RequestContext.getCurrentInstance().update("@all");
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se guardo la gestion: "));
    } else {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se guardo la gestion. Contacte al equipo de sistemas."));
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
    boolean ok = false;
    // AREA DE APAGADO DE MARCAJE
    Credito c = creditoActual;
    switch (creditoActual.getMarcaje().getIdMarcaje()) {
      // CORREO ORDINARIO
      case 1:
        if (descripcionSeleccionada.getAbreviatura().equals("4CADE")) {
          c.setMarcaje(marcajeDao.buscarMarcajePorId(Marcajes.SIN_MARCAJE));
          ok = creditoDao.editar(c);
        }
        break;
      // TELEGRAMA
      case 2:
        if ((descripcionSeleccionada.getAbreviatura().equals("3CETE")) || (descripcionSeleccionada.getAbreviatura().equals("21TELE3"))) {
          c.setMarcaje(marcajeDao.buscarMarcajePorId(Marcajes.SIN_MARCAJE));
          ok = creditoDao.editar(c);
        }
        break;
      // VISITA
      case 3:
        if (nueva.getTipoGestion().getIdTipoGestion() == 1) {
          c.setMarcaje(marcajeDao.buscarMarcajePorId(Marcajes.SIN_MARCAJE));
          ok = creditoDao.editar(c);
        }
        break;
      // CORREO ELECTRONICO
      case 4:
        if (descripcionSeleccionada.getAbreviatura().equals("2COEL")) {
          c.setMarcaje(marcajeDao.buscarMarcajePorId(Marcajes.SIN_MARCAJE));
          ok = creditoDao.editar(c);
        }
        break;
      // LOCALIZACION
      case 5:
        if (((descripcionSeleccionada.getAbreviatura().contains("POSI")) && (tipoSeleccionado.getIdTipoGestion() == (1 | 2))) || (descripcionSeleccionada.getAbreviatura().equals("2SISB"))) {
          c.setMarcaje(marcajeDao.buscarMarcajePorId(Marcajes.SIN_MARCAJE));
          ok = creditoDao.editar(c);
        }
        break;
      // INFORMACION DEL BANCO
      case 6:
        if (descripcionSeleccionada.getAbreviatura().equals("4CIAB")) {
          c.setMarcaje(marcajeDao.buscarMarcajePorId(Marcajes.SIN_MARCAJE));
          ok = creditoDao.editar(c);
        }
        break;
      // PROXIMAMENTE CASO WHATSAPP
      default:
        ok = true;
    }
    // AREA DE ENCENDIDO DE MARCAJE
    switch (descripcionSeleccionada.getAbreviatura()) {
      // CASO CORREO ORDINARIO
      case "26CACO":
        c.setMarcaje(marcajeDao.buscarMarcajePorId(Marcajes.CORREO_SEPOMEX));
        break;
      // CASO TELEGRAMA
      case "19TELE1":
      case "20TELE2":
        c.setMarcaje(marcajeDao.buscarMarcajePorId(Marcajes.TELEGRAMA));
        break;
      // CASO VISITA
      case "32RUVD":
        c.setMarcaje(marcajeDao.buscarMarcajePorId(Marcajes.VISITA_DOMICILIARIA));
        break;
      // CASO CORREO ELECTRONICO
      case "27CEBC":
      case "28CEBCC":
      case "29CEREQ":
      case "30CENEG":
      case "31CELIB":
        c.setMarcaje(marcajeDao.buscarMarcajePorId(Marcajes.CORREO_ELECTRONICO));
        break;
      // CASO LOCALIZACION
      case "1SECC":
      case "3NOSB":
      case "4CADE":
        c.setMarcaje(marcajeDao.buscarMarcajePorId(Marcajes.LOCALIZACION));
        break;
      // CASO INFORMACION DEL BANCO
      case "33CEIB":
        c.setMarcaje(marcajeDao.buscarMarcajePorId(Marcajes.ESPERA_INFORMACION_BANCO));
        break;
      // CASO COBRO EN CELULAR
      case "1SMSC":
        c.setMarcaje(marcajeDao.buscarMarcajePorId(Marcajes.COBRO_EN_CELULAR));
        break;
      // PROXIMAMENTE CASO WHATSAPP
      default:
        ok = true;
    }
    return (ok & (creditoDao.editar(c)));
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
