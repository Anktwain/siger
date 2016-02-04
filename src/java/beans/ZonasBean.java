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
  private boolean habilitaEstados;
  private boolean habilitaMunicipios;
  private final int idDespacho;
  private String nombreZona;
  private List<Zonas> listaZonas;
  private List<EstadoRepublica> listaEstados;
  private List<Municipio> listaMunicipios;
  private List<Gestor> listaGestores;
  private List<String> estadosSeleccionados;
  private List<String> municipiosSeleccionados;
  private final EstadoRepublicaDAO estadoRepublicaDao;
  private final MunicipioDAO municipioDao;
  private final RegionDAO regionDao;
  private final ZonaDAO zonaDao;
  private final GestorDAO gestorDao;
  private Gestor gestorSeleccionado;
  private Zonas zonaSeleccionada;

  // CONSTRUCTOR
  public ZonasBean() {
    habilitaEstados = false;
    habilitaMunicipios = true;
    listaZonas = new ArrayList();
    listaEstados = new ArrayList();
    listaMunicipios = new ArrayList();
    listaGestores = new ArrayList();
    estadosSeleccionados = new ArrayList();
    municipiosSeleccionados = new ArrayList();
    estadoRepublicaDao = new EstadoRepublicaIMPL();
    municipioDao = new MunicipioIMPL();
    regionDao = new RegionIMPL();
    zonaDao = new ZonaIMPL();
    gestorDao = new GestorIMPL();
    gestorSeleccionado = new Gestor();
    zonaSeleccionada = new Zonas();
    listaEstados = estadoRepublicaDao.buscarTodo();
    idDespacho = indexBean.getUsuario().getDespacho().getIdDespacho();
    listaGestores = gestorDao.buscarPorDespacho(idDespacho);
  }

  // METODO QUE ACTUALIZARA LA VISTA
  public void actualizaVista() {
    RequestContext.getCurrentInstance().update("nuevaZonaForm");
    RequestContext.getCurrentInstance().update("listaZonasForm");
  }

  // METODO QUE VERIFICA QUE LA LISTA DE ESTADOS NO ESTE VACIA
  public void verificaEstados() {
    if (estadosSeleccionados.isEmpty()) {
      FacesContext contexto = FacesContext.getCurrentInstance();
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "Debe seleccionar al menos un estado."));
    } else {
      if (!listaMunicipios.isEmpty()) {
        listaMunicipios.clear();
      }
      cargarMunicipios();
      actualizaVista();
    }
  }

  // METODO QUE CARGA LOS MUNICIPIOS PARA LOS ESTADOS SELECCIONADOS
  public void cargarMunicipios() {
    for (int i = 0; i < (estadosSeleccionados.size()); i++) {
      int id = Integer.parseInt(estadosSeleccionados.get(i));
      List<Municipio> municipios = municipioDao.buscarMunicipiosPorEstado(id);
      for (int j = 0; j < (municipios.size()); j++) {
        listaMunicipios.add(municipios.get(j));
      }
    }
    habilitaEstados = true;
    habilitaMunicipios = false;
    actualizaVista();
  }

  // METODO QUE VERIFICA QUE LA LISTA DE MUNICIPIOS NO ESTE VACIA
  public void verificaMunicipios() {
    System.out.println("ENTRO AL METODO");
    FacesContext contexto = FacesContext.getCurrentInstance();
    if (municipiosSeleccionados.isEmpty()) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "Debe seleccionar al menos un municipio."));
    } else {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se agregaron correctamente los municipios."));
    }
  }

  // METODO QUE SELECCIONA TODOS LOS MUNICIPIOS
  public void agregarTodosMunicipios() {
    // TODO: AGREGAR FUNCIONALIDAD
  }

  // METODO QUE CREA LA ZONA
  public void crearZona() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    boolean ok = true;
    ok = ok & (validarNombreZona());
    if (!ok) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El nombre de la zona ya existe para este despacho."));
    } else {
      ok = ok & (validarGestorZona());
      if (!ok) {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El gestor seleccionado ya tiene asignada una zona."));
      } else {
        ok = ok & (validarMunicipiosEnZona());
        if (!ok) {
          contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "Uno o varios de los municipios ya tienen asignada una zona."));
        } else {
          Zona z = new Zona();
          z.setDespacho(indexBean.getUsuario().getDespacho());
          z.setGestor(gestorDao.buscar(gestorSeleccionado.getIdGestor()));
          z.setNombre(nombreZona);
          z = zonaDao.insertar(z);
          if (z != null) {
            for (int i = 0; i < (municipiosSeleccionados.size()); i++) {
              Municipio m = municipioDao.buscarPorId(Integer.parseInt(municipiosSeleccionados.get(i)));
              Region r = new Region();
              r.setEstadoRepublica(m.getEstadoRepublica());
              r.setMunicipio(m);
              r.setZona(z);
              r = regionDao.insertar(r);
              if (r != null) {
                ok = true;
                break;
              } else {
                ok = false;
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
    }
  }

  // METODO QUE LIMPIA TODOS LOS CONTROLES
  public void cancelar() {
    listaMunicipios.clear();
    estadosSeleccionados.clear();
    municipiosSeleccionados.clear();
    nombreZona = "";
    actualizaVista();
    RequestContext.getCurrentInstance().execute("PF('dlgNuevaZona').hide();");
  }

// METODO QUE VALIDA QUE EL NOMBRE DE LA ZONA NO EXISTA
  public boolean validarNombreZona() {
    boolean ok = true;
    List<String> nombres = zonaDao.buscarNombresZonas(idDespacho);
    for (int i = 0; i < (nombres.size()); i++) {
      if (nombreZona.equals(nombres.get(i))) {
        ok = false;
      }
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
        if ((Integer.parseInt(municipiosSeleccionados.get(j))) == (ids.get(i))) {
          ok = false;
          break;
        }
      }
    }
    return ok;
  }

  // METODO QUE CARGA UNA LISTA DE ZONAS DE ESTE DESPACHO
  public void cargarZonas() {
    List<Zona> zonas = zonaDao.buscarPorDespacho(idDespacho);
    Zonas z = new Zonas();
    for (int i = 0; i < (zonas.size()); i++) {
      z.setIdZona(zonas.get(i).getIdZona());
      z.setNombre(zonas.get(i).getNombre());
      List<EstadoRepublica> edos = regionDao.buscarEstadosZona(zonas.get(i).getIdZona());
      String a = "";
      for (int j = 0; j < (edos.size()); j++) {
        a = a + edos.get(j).getNombre() + ", ";
      }
      z.setEstadosZona(a.substring(0, a.length() - 2));
      List<Municipio> muns = regionDao.buscarMunicipiosZona(zonas.get(i).getIdZona());
      String b = "";
      for (int j = 0; j < (muns.size()); j++) {
        b = b + muns.get(j).getNombre() + ", ";
      }
      z.setMunicipiosZona(b.substring(0, b.length() - 2));
      listaZonas.add(z);
    }
    actualizaVista();
    RequestContext.getCurrentInstance().execute("PF('dlgZonas').show();");
  }

  // GETTERS & SETTERS
  public List<EstadoRepublica> getListaEstados() {
    return listaEstados;
  }

  public void setListaEstados(List<EstadoRepublica> listaEstados) {
    this.listaEstados = listaEstados;
  }

  public List<Municipio> getListaMunicipios() {
    return listaMunicipios;
  }

  public void setListaMunicipios(List<Municipio> listaMunicipios) {
    this.listaMunicipios = listaMunicipios;
  }

  public List<String> getEstadosSeleccionados() {
    return estadosSeleccionados;
  }

  public void setEstadosSeleccionados(List<String> estadosSeleccionados) {
    this.estadosSeleccionados = estadosSeleccionados;
  }

  public List<String> getMunicipiosSeleccionados() {
    return municipiosSeleccionados;
  }

  public void setMunicipiosSeleccionados(List<String> municipiosSeleccionados) {
    this.municipiosSeleccionados = municipiosSeleccionados;
  }

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

  public List<Zonas> getListaZonas() {
    return listaZonas;
  }

  public void setListaZonas(List<Zonas> listaZonas) {
    this.listaZonas = listaZonas;
  }

  public Zonas getZonaSeleccionada() {
    return zonaSeleccionada;
  }

  public void setZonaSeleccionada(Zonas zonaSeleccionada) {
    this.zonaSeleccionada = zonaSeleccionada;
  }

  public boolean isHabilitaEstados() {
    return habilitaEstados;
  }

  public void setHabilitaEstados(boolean habilitaEstados) {
    this.habilitaEstados = habilitaEstados;
  }

  public boolean isHabilitaMunicipios() {
    return habilitaMunicipios;
  }

  public void setHabilitaMunicipios(boolean habilitaMunicipios) {
    this.habilitaMunicipios = habilitaMunicipios;
  }

  // CLASE INTERNA MIEMBRO PARA GUARDAR LA LISTA DE ESTADOS Y MUNICIPIOS DE UNA ZONA
  public class Zonas {

    int idZona;
    String nombre;
    String estadosZona;
    String municipiosZona;

    public Zonas() {
    }

    public int getIdZona() {
      return idZona;
    }

    public void setIdZona(int idZona) {
      this.idZona = idZona;
    }

    public String getNombre() {
      return nombre;
    }

    public void setNombre(String nombre) {
      this.nombre = nombre;
    }

    public String getEstadosZona() {
      return estadosZona;
    }

    public void setEstadosZona(String estadosZona) {
      this.estadosZona = estadosZona;
    }

    public String getMunicipiosZona() {
      return municipiosZona;
    }

    public void setMunicipiosZona(String municipiosZona) {
      this.municipiosZona = municipiosZona;
    }

  }

}
