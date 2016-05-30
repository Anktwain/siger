/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.CampanaDAO;
import dao.CreditoDAO;
import dao.DireccionDAO;
import dao.EstadoRepublicaDAO;
import dao.InstitucionDAO;
import dao.MunicipioDAO;
import dao.ProductoDAO;
import dao.ZonaDAO;
import dto.Campana;
import dto.Credito;
import dto.Direccion;
import dto.EstadoRepublica;
import dto.Institucion;
import dto.Municipio;
import dto.Producto;
import dto.Zona;
import impl.CampanaIMPL;
import impl.CreditoIMPL;
import impl.DireccionIMPL;
import impl.EstadoRepublicaIMPL;
import impl.InstitucionIMPL;
import impl.MunicipioIMPL;
import impl.ProductoIMPL;
import impl.ZonaIMPL;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "busquedaBean")
@ViewScoped
public class BusquedaBean implements Serializable {

  // VARIABLES DE CLASE
  private final ZonaDAO zonaDao;
  private final CreditoDAO creditoDao;
  private final DireccionDAO direccionDao;
  private final InstitucionDAO institucionDao;
  private final ProductoDAO productoDao;
  private final CampanaDAO campanaDao;
  private final EstadoRepublicaDAO estadoRepublicaDao;
  private final MunicipioDAO municipioDao;
  private Zona zonaSeleccionada;
  private CreditoDireccion creditoSeleccionado;
  private Institucion institucionSeleccionada;
  private Producto productoSeleccionado;
  private Campana campanaSeleccionada;
  private EstadoRepublica estadoSeleccionado;
  private Municipio municipioSeleccionado;
  private List<EstadoRepublica> listaEstados;
  private List<Municipio> listaMunicipios;
  private List<Zona> listaZonas;
  private List<CreditoDireccion> listaCreditos;
  private List<Credito> listaCreditos2;
  private List<Institucion> listaInstituciones;
  private List<Producto> listaProductos;
  private List<Campana> listaCampanas;
  private Direccion direccionCredito;
  private float saldoInferior;
  private float saldoSuperior;
  private int mesesVencidos;

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  // CONSTRUCTOR
  public BusquedaBean() {
    listaCreditos = new ArrayList();
    listaZonas = new ArrayList();
    listaCreditos2 = new ArrayList();
    listaInstituciones = new ArrayList();
    listaProductos = new ArrayList();
    listaCampanas = new ArrayList();
    zonaSeleccionada = new Zona();
    direccionCredito = new Direccion();
    creditoSeleccionado = new CreditoDireccion();
    institucionSeleccionada = new Institucion();
    productoSeleccionado = new Producto();
    campanaSeleccionada = new Campana();
    estadoSeleccionado = new EstadoRepublica();
    municipioSeleccionado = new Municipio();
    creditoDao = new CreditoIMPL();
    zonaDao = new ZonaIMPL();
    direccionDao = new DireccionIMPL();
    institucionDao = new InstitucionIMPL();
    productoDao = new ProductoIMPL();
    campanaDao = new CampanaIMPL();
    estadoRepublicaDao = new EstadoRepublicaIMPL();
    municipioDao = new MunicipioIMPL();
    obtenerListas();
  }

  // METODO QUE OBTIENE LAS LISTAS INICIALES
  public final void obtenerListas() {
    listaEstados = estadoRepublicaDao.buscarTodo();
    listaZonas = zonaDao.buscarPorDespacho(indexBean.getUsuario().getDespacho().getIdDespacho());
    listaInstituciones = institucionDao.buscarInstitucionesPorDespacho(indexBean.getUsuario().getDespacho().getIdDespacho());
    listaCampanas = campanaDao.buscarTodas();
  }

  // METODO QUE BUSCA LOS CREDITOS DE LA ZONA SELECCIONADA
  public void obtenerCreditosPorZona() {
    listaCreditos.clear();
    List<Credito> listaCreds = creditoDao.buscarCreditosPorZona(zonaSeleccionada.getIdZona());
    for (int i = 0; i < (listaCreds.size()); i++) {
      CreditoDireccion cd = new CreditoDireccion();
      cd.setNumeroCredito(listaCreds.get(i).getNumeroCredito());
      cd.setNombreDeudor(listaCreds.get(i).getDeudor().getSujeto().getNombreRazonSocial());
      cd.setInstitucion(listaCreds.get(i).getProducto().getInstitucion().getNombreCorto());
      cd.setGestorActual(listaCreds.get(i).getGestor().getUsuario().getNombreLogin());
      List<Direccion> dirs = direccionDao.buscarPorSujeto(listaCreds.get(i).getDeudor().getSujeto().getIdSujeto());
      if ((dirs != null) && (dirs.size() > 0)) {
        cd.setCalleNumero(dirs.get(0).getCalle());
        cd.setColonia(dirs.get(0).getColonia().getNombre());
        cd.setCp(dirs.get(0).getColonia().getCodigoPostal());
        cd.setEstado(dirs.get(0).getEstadoRepublica().getNombre());
        cd.setMunicipio(dirs.get(0).getMunicipio().getNombre());
      }
      listaCreditos.add(cd);
    }
  }

  // METODO QUE BUSCA LOS CREDITOS DEPENDIENDO DEL ESTADO Y MUNICIPIOS SELECCIONADOS
  public void obtenerCreditosPorAreaGeografica() {
    listaCreditos.clear();
    if (estadoSeleccionado.getIdEstado() == 0) {
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe seleccionar un estado."));
    } else {
      List<Credito> listaCreds = new ArrayList();
      if (municipioSeleccionado.getIdMunicipio() == 0) {
        listaCreds = creditoDao.buscarCreditosPorEstado(estadoSeleccionado.getIdEstado());
      } else {
        listaCreds = creditoDao.buscarCreditosPorMunicipio(municipioSeleccionado.getIdMunicipio());
      }
      for (int i = 0; i < (listaCreds.size()); i++) {
        CreditoDireccion cd = new CreditoDireccion();
        cd.setNumeroCredito(listaCreds.get(i).getNumeroCredito());
        cd.setNombreDeudor(listaCreds.get(i).getDeudor().getSujeto().getNombreRazonSocial());
        cd.setInstitucion(listaCreds.get(i).getProducto().getInstitucion().getNombreCorto());
        cd.setGestorActual(listaCreds.get(i).getGestor().getUsuario().getNombreLogin());
        List<Direccion> dirs = direccionDao.buscarPorSujeto(listaCreds.get(i).getDeudor().getSujeto().getIdSujeto());
        if ((dirs != null) && (dirs.size() > 0)) {
          cd.setCalleNumero(dirs.get(0).getCalle());
          cd.setColonia(dirs.get(0).getColonia().getNombre());
          cd.setCp(dirs.get(0).getColonia().getCodigoPostal());
          cd.setEstado(dirs.get(0).getEstadoRepublica().getNombre());
          cd.setMunicipio(dirs.get(0).getMunicipio().getNombre());
        }
        listaCreditos.add(cd);
      }
    }
  }

  // METODO QUE CARGA LOS MUNICIPIOS CUANDO SE HA SELECCIONADO UN ESTADO
  public void obtenerMunicipios(EstadoRepublica estadoSeleccionado) {
    listaMunicipios = municipioDao.buscarMunicipiosPorEstado(estadoSeleccionado.getIdEstado());
  }

  // METODO QUE OBTIENE LA DIRECCION DEL CREDITO SELECCIONADO Y ABRE EL DIALOGO CORRESPONDIENTE
  public void obtenerDireccionCredito() {
    List<Direccion> dirs = direccionDao.buscarPorSujeto(creditoDao.buscar(creditoSeleccionado.numeroCredito).getDeudor().getSujeto().getIdSujeto());
    if (dirs.size() > 0) {
      direccionCredito = dirs.get(0);
    }
    RequestContext.getCurrentInstance().update("detalleDireccionesForm");
    RequestContext.getCurrentInstance().execute("PF('dlgDetalleDirecciones').show()");
  }

  // METODO QUE OBTIENE LOS CREDITOS POR UN RANGO ESPECIFICO DE SU SALDO VENCIDO
  public void obtenerCreditosPorSaldoVencido() {
    listaCreditos2.clear();
    listaCreditos2 = creditoDao.buscarCreditosPorSaldoVencido(saldoInferior, saldoSuperior);
  }

  public void obtenerCreditosPorMesesVencidos() {
    listaCreditos2.clear();
    listaCreditos2 = creditoDao.buscarCreditosPorMesesVencidos(mesesVencidos);
  }

  // METODO QUE OBTIENE EL SALDO VENCIDO DE UN CREDITO
  public float obtenerSaldoVencido(int idCredito) {
    return creditoDao.buscarSaldoVencidoCredito(idCredito);
  }

  // METODO QUE OBTIENE LOS MESES VENCIDOS DEL CREDITO
  public int obtenerMesesVencidos(int idCredito) {
    return creditoDao.buscarMesesVencidosCredito(idCredito);
  }

  // METODO QUE PREPARA LOS COMBOS CON LOS PRODUCTOS SEGUN LA INSTITUCION SELECCIONADA
  public void preparaProductos() {
    listaProductos = productoDao.buscarProductosPorInstitucion(institucionSeleccionada.getIdInstitucion());
  }

  // METODO QUE OBTIENE LOS CREDITOS DE LOS PRODUCTOS SELECCIONADOS
  public void obtenerCreditosPorProductosCampana() {
    listaCreditos2.clear();
    if (institucionSeleccionada.getIdInstitucion() == 0) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "Debe seleccionar una institucion."));
    } else {
      List<Credito> lista = creditoDao.buscarCreditosPorProducto(productoSeleccionado.getIdProducto());
      if (campanaSeleccionada.getIdCampana() == 0) {
        listaCreditos2 = lista;
      } else {
        for (int i = 0; i < (lista.size()); i++) {
          if (lista.get(i).getCampana().getIdCampana().intValue() == campanaSeleccionada.getIdCampana()) {
            listaCreditos2.add(lista.get(i));
          }
        }
      }
    }
  }

  // ***********************************************************************************************************************
  // ***********************************************************************************************************************
  // ***********************************************************************************************************************
  // GETTERS & SETTERS
  public Zona getZonaSeleccionada() {
    return zonaSeleccionada;
  }

  public void setZonaSeleccionada(Zona zonaSeleccionada) {
    this.zonaSeleccionada = zonaSeleccionada;
  }

  public List<Zona> getListaZonas() {
    return listaZonas;
  }

  public void setListaZonas(List<Zona> listaZonas) {
    this.listaZonas = listaZonas;
  }

  public CreditoDireccion getCreditoSeleccionado() {
    return creditoSeleccionado;
  }

  public void setCreditoSeleccionado(CreditoDireccion creditoSeleccionado) {
    this.creditoSeleccionado = creditoSeleccionado;
  }

  public Direccion getDireccionCredito() {
    return direccionCredito;
  }

  public void setDireccionCredito(Direccion direccionCredito) {
    this.direccionCredito = direccionCredito;
  }

  public List<CreditoDireccion> getListaCreditos() {
    return listaCreditos;
  }

  public void setListaCreditos(List<CreditoDireccion> listaCreditos) {
    this.listaCreditos = listaCreditos;
  }

  public float getSaldoInferior() {
    return saldoInferior;
  }

  public void setSaldoInferior(float saldoInferior) {
    this.saldoInferior = saldoInferior;
  }

  public float getSaldoSuperior() {
    return saldoSuperior;
  }

  public void setSaldoSuperior(float saldoSuperior) {
    this.saldoSuperior = saldoSuperior;
  }

  public List<Credito> getListaCreditos2() {
    return listaCreditos2;
  }

  public void setListaCreditos2(List<Credito> listaCreditos2) {
    this.listaCreditos2 = listaCreditos2;
  }

  public int getMesesVencidos() {
    return mesesVencidos;
  }

  public void setMesesVencidos(int mesesVencidos) {
    this.mesesVencidos = mesesVencidos;
  }

  public Institucion getInstitucionSeleccionada() {
    return institucionSeleccionada;
  }

  public void setInstitucionSeleccionada(Institucion institucionSeleccionada) {
    this.institucionSeleccionada = institucionSeleccionada;
  }

  public Producto getProductoSeleccionado() {
    return productoSeleccionado;
  }

  public void setProductoSeleccionado(Producto productoSeleccionado) {
    this.productoSeleccionado = productoSeleccionado;
  }

  public List<Institucion> getListaInstituciones() {
    return listaInstituciones;
  }

  public void setListaInstituciones(List<Institucion> listaInstituciones) {
    this.listaInstituciones = listaInstituciones;
  }

  public List<Producto> getListaProductos() {
    return listaProductos;
  }

  public void setListaProductos(List<Producto> listaProductos) {
    this.listaProductos = listaProductos;
  }

  public Campana getCampanaSeleccionada() {
    return campanaSeleccionada;
  }

  public void setCampanaSeleccionada(Campana campanaSeleccionada) {
    this.campanaSeleccionada = campanaSeleccionada;
  }

  public List<Campana> getListaCampanas() {
    return listaCampanas;
  }

  public void setListaCampanas(List<Campana> listaCampanas) {
    this.listaCampanas = listaCampanas;
  }

  public EstadoRepublica getEstadoSeleccionado() {
    return estadoSeleccionado;
  }

  public void setEstadoSeleccionado(EstadoRepublica estadoSeleccionado) {
    this.estadoSeleccionado = estadoSeleccionado;
  }

  public Municipio getMunicipioSeleccionado() {
    return municipioSeleccionado;
  }

  public void setMunicipioSeleccionado(Municipio municipioSeleccionado) {
    this.municipioSeleccionado = municipioSeleccionado;
  }

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

  // CLASE MIEMBRO PARA PODER MOSTRAR EL CREDITO Y SU DIRECCION
  public static class CreditoDireccion {

    private String numeroCredito;
    private String nombreDeudor;
    private String institucion;
    private String gestorActual;
    private String calleNumero;
    private String colonia;
    private String cp;
    private String municipio;
    private String estado;

    public CreditoDireccion() {
    }

    public String getNumeroCredito() {
      return numeroCredito;
    }

    public void setNumeroCredito(String numeroCredito) {
      this.numeroCredito = numeroCredito;
    }

    public String getNombreDeudor() {
      return nombreDeudor;
    }

    public void setNombreDeudor(String nombreDeudor) {
      this.nombreDeudor = nombreDeudor;
    }

    public String getInstitucion() {
      return institucion;
    }

    public void setInstitucion(String institucion) {
      this.institucion = institucion;
    }

    public String getGestorActual() {
      return gestorActual;
    }

    public void setGestorActual(String gestorActual) {
      this.gestorActual = gestorActual;
    }

    public String getCalleNumero() {
      return calleNumero;
    }

    public void setCalleNumero(String calleNumero) {
      this.calleNumero = calleNumero;
    }

    public String getColonia() {
      return colonia;
    }

    public void setColonia(String colonia) {
      this.colonia = colonia;
    }

    public String getCp() {
      return cp;
    }

    public void setCp(String cp) {
      this.cp = cp;
    }

    public String getMunicipio() {
      return municipio;
    }

    public void setMunicipio(String municipio) {
      this.municipio = municipio;
    }

    public String getEstado() {
      return estado;
    }

    public void setEstado(String estado) {
      this.estado = estado;
    }

  }

}
