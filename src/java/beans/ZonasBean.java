/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;


import dao.EstadoRepublicaDAO;
import dao.GestorDAO;
import dao.MunicipioDAO;
import dao.RegionDAO;
import dao.ZonaDAO;
import dto.EstadoRepublica;
import dto.Gestor;
import dto.Municipio;
import dto.Region;
import dto.Zona;
import impl.EstadoRepublicaIMPL;
import impl.GestorIMPL;
import impl.MunicipioIMPL;
import impl.RegionIMPL;
import impl.ZonaIMPL;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 * La reestructuracion de este modulo es un honor que me ha concedido el Ing.
 * Pablo landeros. Hombre respetable y siempre fiel a sus convicciones. Que el
 * exito lo ilumine a donde quiera que vaya
 *
 * In memoriam @author Pablo
 *
 * @author Eduardo
 */
@ManagedBean(name = "zonasBean")
@ViewScoped
public class ZonasBean implements Serializable {

  // LLAMADA A OTROS BEANS
  public final ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  public final IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  // VARIBALES DE CLASE
  private final int idDespacho;
  private String nombreZona;
  private List<Municipio> detalleZona;
  private List<Municipio> municipiosSeleccionados;
  private List<Municipio> listaMunicipios;
  private List<Zona> listaZonas;
  private List<Gestor> listaGestores;
  private final EstadoRepublicaDAO estadoRepublicaDao;
  private final MunicipioDAO municipioDao;
  private final RegionDAO regionDao;
  private final ZonaDAO zonaDao;
  private final GestorDAO gestorDao;
  private Gestor gestorSeleccionado;
  private Zona zonaSeleccionada;

  // CONSTRUCTOR
  public ZonasBean() {
    listaZonas = new ArrayList();
    listaGestores = new ArrayList();
    listaMunicipios = new ArrayList();
    detalleZona = new ArrayList();
    municipiosSeleccionados = new ArrayList();
    estadoRepublicaDao = new EstadoRepublicaIMPL();
    municipioDao = new MunicipioIMPL();
    regionDao = new RegionIMPL();
    zonaDao = new ZonaIMPL();
    gestorDao = new GestorIMPL();
    gestorSeleccionado = new Gestor();
    zonaSeleccionada = new Zona();
    idDespacho = indexBean.getUsuario().getDespacho().getIdDespacho();
    obtenerListas();
  }

  //  METODO QUE CARGA LA LISTA DE MUNICIPIOS
  public final void obtenerListas() {
    listaZonas = zonaDao.buscarPorDespacho(idDespacho);
    listaGestores = gestorDao.buscarPorDespacho(idDespacho);
    List<EstadoRepublica> estados = estadoRepublicaDao.buscarTodo();
    for (int i = 0; i < (estados.size()); i++) {
      List<Municipio> municipios = municipioDao.buscarMunicipiosPorEstado(estados.get(i).getIdEstado());
      for (int j = 0; j < (municipios.size()); j++) {
        listaMunicipios.add(municipios.get(j));
      }
    }
  }

  // METODO QUE ACTUALIZARA LA VISTA
  public void actualizaVista() {
    RequestContext.getCurrentInstance().update("nuevaZonaForm");
    RequestContext.getCurrentInstance().update("listaZonasForm");
    RequestContext.getCurrentInstance().update("detalleZonaForm");
  }

  // METODO QUE CREA LA ZONA
  public void crearZona() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    boolean ok = false;
    if (nombreZona.equals("")) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "Debe indicar un nombre para la zona."));
    } else if (municipiosSeleccionados.isEmpty()) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "Debe seleccionar al menos un municipio."));
    } else if (validarNombreZona()) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "Ya existe una zona con el nombre propuesto."));
    } else if (!validarGestorZona()) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El gestor seleccionado ya tiene asignada una zona."));
    } else if (!validarMunicipiosEnZona()) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "Uno o varios de los municipios ya pertenecen a otra zona."));
    } else {
      Zona z = new Zona();
      z.setDespacho(indexBean.getUsuario().getDespacho());
      z.setGestor(gestorDao.buscar(gestorSeleccionado.getIdGestor()));
      z.setNombre(nombreZona);
      z = zonaDao.insertar(z);
      if (z != null) {
        for (int i = 0; i < (municipiosSeleccionados.size()); i++) {
          Municipio m = municipioDao.buscarPorId(municipiosSeleccionados.get(i).getIdMunicipio());
          Region r = new Region();
          r.setEstadoRepublica(m.getEstadoRepublica());
          r.setMunicipio(m);
          r.setZona(z);
          if(regionDao.insertar(r) != null){
            ok = true;
          }
        }
        if (ok) {
          contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se agrego la zona al sistema."));
          cancelar();
        } else {
          contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se agrego la zona. Contacte al equipo de sistemas"));
        }
      } else {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se agrego la zona. Contacte al equipo de sistemas"));
      }
    }
  }

  // METODO QUE ELIMINA LA ZONA SELECCIONADA
  public void eliminarZona(Zona zona) {
    FacesContext contexto = FacesContext.getCurrentInstance();
    List<Region> regiones = regionDao.buscarPorZona(zona.getIdZona());
    boolean ok = true;
    for (int i = 0; i <(regiones.size()); i++) {
      ok = regionDao.eliminar(regiones.get(i));
      if(!ok){
        break;
      }
    }
    if(ok){
      ok = zonaDao.eliminar(zona);
      if(ok){
        obtenerListas();
        actualizaVista();
        RequestContext.getCurrentInstance().execute("PF('dlgZonas').hide();");
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se elimino la zona."));
      }
      else{
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo eliminar la zona. Contacte al equipo de sistemas"));
      }
    }
    else{
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo eliminar la zona. Contacte al equipo de sistemas"));
    }
  }

  // METODO QUE ABRE LA TABLA DEL DETALLE DE LA ZONA SELECCIONADA
  public void abrirDetalleZona(Zona zona) {
    detalleZona = regionDao.buscarMunicipiosZona(zona.getIdZona());
    actualizaVista();
    RequestContext.getCurrentInstance().execute("PF('detalleZonaDialog').show();");
  }

  // METODO QUE LIMPIA TODOS LOS CONTROLES
  public void cancelar() {
    nombreZona = "";
    municipiosSeleccionados.clear();
    obtenerListas();
    actualizaVista();
    RequestContext.getCurrentInstance().execute("PF('dlgNuevaZona').hide();");
  }

// METODO QUE VALIDA QUE EL NOMBRE DE LA ZONA NO EXISTA
  public boolean validarNombreZona() {
    boolean ok = true;
    List<String> nombres = zonaDao.buscarNombresZonas(idDespacho);
    if (nombres.size() > 0) {
      for (int i = 0; i < (nombres.size()); i++) {
        ok = nombreZona.equals(nombres.get(i));
      }
    } else {
      ok = true;
    }
    return ok;
  }

  // METODO QUE VALIDA QUE LA ZONA SE LE ASIGNE A UN GESTOR QUE NO TENGA ZONA
  public boolean validarGestorZona() {
    boolean ok = true;
    List<Integer> ids = zonaDao.buscarIdsGestoresZonas();
    for (int i = 0; i < (ids.size()); i++) {
      if ((gestorSeleccionado.getIdGestor()) == ids.get(i).intValue()) {
        ok = false;
        break;
      }
    }
    return ok;
  }

  //METODO QUE VALIDA QUE NO SE REPITAN MUNICIPIOS EN LAS ZONAS
  public boolean validarMunicipiosEnZona() {
    boolean ok = true;
    List<Integer> ids = regionDao.buscarMunicipiosRegion(idDespacho);
    for (int i = 0; i < (ids.size()); i++) {
      for (int j = 0; j < (municipiosSeleccionados.size()); j++) {
        if ((municipiosSeleccionados.get(j).getIdMunicipio()) == (ids.get(i).intValue())) {
          ok = false;
          break;
        }
      }
    }
    return ok;
  }

  // GETTERS & SETTERS
  public String getNombreZona() {
    return nombreZona;
  }

  public void setNombreZona(String nombreZona) {
    this.nombreZona = nombreZona;
  }

  public List<Gestor> getListaGestores() {
    return listaGestores;
  }

  public void setListaGestores(List<Gestor> listaGestores) {
    this.listaGestores = listaGestores;
  }

  public Gestor getGestorSeleccionado() {
    return gestorSeleccionado;
  }

  public void setGestorSeleccionado(Gestor gestorSeleccionado) {
    this.gestorSeleccionado = gestorSeleccionado;
  }

  public List<Municipio> getMunicipiosSeleccionados() {
    return municipiosSeleccionados;
  }

  public void setMunicipiosSeleccionados(List<Municipio> municipiosSeleccionados) {
    this.municipiosSeleccionados = municipiosSeleccionados;
  }

  public List<Municipio> getListaMunicipios() {
    return listaMunicipios;
  }

  public void setListaMunicipios(List<Municipio> listaMunicipios) {
    this.listaMunicipios = listaMunicipios;
  }

  public List<Zona> getListaZonas() {
    return listaZonas;
  }

  public void setListaZonas(List<Zona> listaZonas) {
    this.listaZonas = listaZonas;
  }

  public Zona getZonaSeleccionada() {
    return zonaSeleccionada;
  }

  public void setZonaSeleccionada(Zona zonaSeleccionada) {
    this.zonaSeleccionada = zonaSeleccionada;
  }

  public List<Municipio> getDetalleZona() {
    return detalleZona;
  }

  public void setDetalleZona(List<Municipio> detalleZona) {
    this.detalleZona = detalleZona;
  }

}
