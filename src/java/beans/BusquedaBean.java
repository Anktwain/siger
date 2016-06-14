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
import dao.GestionDAO;
import dao.InstitucionDAO;
import dao.MunicipioDAO;
import dao.ProductoDAO;
import dao.SubproductoDAO;
import dao.ZonaDAO;
import dto.Campana;
import dto.Credito;
import dto.Direccion;
import dto.EstadoRepublica;
import dto.Institucion;
import dto.Municipio;
import dto.Producto;
import dto.Subproducto;
import dto.Zona;
import impl.CampanaIMPL;
import impl.CreditoIMPL;
import impl.DireccionIMPL;
import impl.EstadoRepublicaIMPL;
import impl.GestionIMPL;
import impl.InstitucionIMPL;
import impl.MunicipioIMPL;
import impl.ProductoIMPL;
import impl.SubproductoIMPL;
import impl.ZonaIMPL;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import util.constantes.Perfiles;
import util.log.Logs;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "busquedaBean")
@ViewScoped
public class BusquedaBean implements Serializable {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");
  CreditoActualBean creditoActualBean = (CreditoActualBean) elContext.getELResolver().getValue(elContext, null, "creditoActualBean");
  CuentasGestorBean cuentasGestorBean = (CuentasGestorBean) elContext.getELResolver().getValue(elContext, null, "cuentasGestorBean");

  // VARIABLES DE CLASE
  private final ZonaDAO zonaDao;
  private final CreditoDAO creditoDao;
  private final DireccionDAO direccionDao;
  private final InstitucionDAO institucionDao;
  private final ProductoDAO productoDao;
  private final SubproductoDAO subproductoDao;
  private final CampanaDAO campanaDao;
  private final EstadoRepublicaDAO estadoRepublicaDao;
  private final MunicipioDAO municipioDao;
  private final GestionDAO gestionDao;
  private Zona zonaSeleccionada;
  private CreditoDireccion creditoDireccionSeleccionado;
  private Institucion institucionSeleccionada;
  private Producto productoSeleccionado;
  private Subproducto subproductoSeleccionado;
  private Campana campanaSeleccionada;
  private EstadoRepublica estadoSeleccionado;
  private Municipio municipioSeleccionado;
  private Credito creditoSeleccionado;
  private List<EstadoRepublica> listaEstados;
  private List<Municipio> listaMunicipios;
  private List<Zona> listaZonas;
  private List<CreditoDireccion> listaCreditos;
  private List<Credito> listaCreditos2;
  private List<Institucion> listaInstituciones;
  private List<Producto> listaProductos;
  private List<Campana> listaCampanas;
  private List<Subproducto> listaSubproductos;
  private Direccion direccionCredito;
  private float saldoInferior;
  private float saldoSuperior;
  private int mesesVencidos;
  private String colorSeleccionado;

  // CONSTRUCTOR
  public BusquedaBean() {
    listaCreditos = new ArrayList();
    listaZonas = new ArrayList();
    listaCreditos2 = new ArrayList();
    listaInstituciones = new ArrayList();
    listaProductos = new ArrayList();
    listaCampanas = new ArrayList();
    listaSubproductos = new ArrayList();
    zonaSeleccionada = new Zona();
    direccionCredito = new Direccion();
    creditoSeleccionado = new Credito();
    creditoDireccionSeleccionado = new CreditoDireccion();
    institucionSeleccionada = new Institucion();
    productoSeleccionado = new Producto();
    subproductoSeleccionado = new Subproducto();
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
    subproductoDao = new SubproductoIMPL();
    gestionDao = new GestionIMPL();
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
      List<Credito> listaCreds;
      if (municipioSeleccionado.getIdMunicipio() == 0) {
        if (indexBean.getUsuario().getPerfil() == Perfiles.GESTOR) {
          String consulta = "SELECT * FROM credito WHERE id_deudor IN (SELECT id_deudor FROM deudor WHERE id_sujeto IN (SELECT id_sujeto FROM direccion WHERE id_direccion IN (SELECT id_direccion FROM direccion WHERE id_estado = " + estadoSeleccionado.getIdEstado() + "))) AND id_gestor = (SELECT id_gestor FROM gestor WHERE id_usuario = " + indexBean.getUsuario().getIdUsuario() + ");";
          listaCreds = creditoDao.busquedaEspecialCreditos(consulta);
        } else {
          listaCreds = creditoDao.buscarCreditosPorEstado(estadoSeleccionado.getIdEstado());
        }
      } else {
        if (indexBean.getUsuario().getPerfil() == Perfiles.GESTOR) {
          String consulta = "SELECT * FROM credito WHERE id_deudor IN (SELECT id_deudor FROM deudor WHERE id_sujeto IN (SELECT id_sujeto FROM direccion WHERE id_direccion IN (SELECT id_direccion FROM direccion WHERE id_municipio = " + municipioSeleccionado.getIdMunicipio() + "))) AND id_gestor = (SELECT id_gestor FROM gestor WHERE id_usuario = " + indexBean.getUsuario().getIdUsuario() + ");";
          listaCreds = creditoDao.busquedaEspecialCreditos(consulta);
        } else {
          listaCreds = creditoDao.buscarCreditosPorMunicipio(municipioSeleccionado.getIdMunicipio());

        }
      }
      for (int i = 0; i < (listaCreds.size()); i++) {
        CreditoDireccion cd = new CreditoDireccion();
        cd.setNumeroCredito(listaCreds.get(i).getNumeroCredito());
        cd.setNombreDeudor(listaCreds.get(i).getDeudor().getSujeto().getNombreRazonSocial());
        cd.setInstitucion(listaCreds.get(i).getProducto().getInstitucion().getNombreCorto());
        cd.setGestorActual(listaCreds.get(i).getGestor().getUsuario().getNombreLogin());
        List<Direccion> dirs = direccionDao.buscarPorSujeto(listaCreds.get(i).getDeudor().getSujeto().getIdSujeto());
        if ((dirs != null) && (dirs.size() > 0)) {
          cd.setCalleNumero(dirs.get(0).getCalle() + " " + dirs.get(0).getExterior() + " " + dirs.get(0).getInterior());
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
    List<Direccion> dirs = direccionDao.buscarPorSujeto(creditoDao.buscar(creditoDireccionSeleccionado.numeroCredito).getDeudor().getSujeto().getIdSujeto());
    if (dirs.size() > 0) {
      direccionCredito = dirs.get(0);
    }
    RequestContext.getCurrentInstance().update("detalleDireccionesForm");
    RequestContext.getCurrentInstance().execute("PF('dlgDetalleDirecciones').show()");
  }

  // METODO QUE OBTIENE LOS CREDITOS POR UN RANGO ESPECIFICO DE SU SALDO VENCIDO
  public void obtenerCreditosPorSaldoVencido() {
    listaCreditos2.clear();
    if (indexBean.getUsuario().getPerfil() == Perfiles.GESTOR) {
      String consulta = "SELECT DISTINCT * FROM credito WHERE id_credito IN (SELECT id_credito FROM actualizacion WHERE saldo_vencido > " + saldoInferior + " AND saldo_vencido < " + saldoSuperior + " ORDER BY id_actualizacion DESC) AND id_gestor = (SELECT id_gestor FROM gestor WHERE id_usuario = " + indexBean.getUsuario().getIdUsuario() + ");";
      listaCreditos2 = creditoDao.busquedaEspecialCreditos(consulta);
    } else {
      listaCreditos2 = creditoDao.buscarCreditosPorSaldoVencido(saldoInferior, saldoSuperior);
    }
  }

  public void obtenerCreditosPorMesesVencidos() {
    listaCreditos2.clear();
    if (indexBean.getUsuario().getPerfil() == Perfiles.GESTOR) {
      String consulta = "SELECT DISTINCT * FROM credito WHERE id_credito IN (SELECT id_credito FROM actualizacion WHERE meses_vencidos = " + mesesVencidos + " ORDER BY id_actualizacion DESC) AND id_gestor = (SELECT id_gestor FROM gestor WHERE id_usuario = " + indexBean.getUsuario().getIdUsuario() + ");";
      listaCreditos2 = creditoDao.busquedaEspecialCreditos(consulta);
    } else {
      listaCreditos2 = creditoDao.buscarCreditosPorMesesVencidos(mesesVencidos);
    }
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

  // METODO QUE PREPARA LOS COMBOS CON LOS SUBPRODUCTOS SEGUN EL PRODUCTO SELECCIONADO
  public void preparaSubproductos() {
    listaSubproductos = subproductoDao.buscarSubproductosPorProducto(productoSeleccionado.getIdProducto());
  }

  // METODO QUE OBTIENE LOS CREDITOS DE LOS PRODUCTOS SELECCIONADOS
  public void obtenerCreditosPorProductosCampana() {
    listaCreditos2.clear();
    if (institucionSeleccionada.getIdInstitucion() == 0) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "Debe seleccionar una institucion."));
    } else {
      String consulta = "SELECT * FROM credito WHERE id_credito NOT IN (SELECT id_credito FROM devolucion)";
      if (productoSeleccionado.getIdProducto() == 0) {
        consulta = consulta + " AND id_producto IN (SELECT id_producto FROM producto WHERE id_institucion = " + institucionSeleccionada.getIdInstitucion() + ")";
      }
      if (subproductoSeleccionado.getIdSubproducto() != 0) {
        consulta = consulta + " AND id_subproducto = " + subproductoSeleccionado.getIdSubproducto();
      }
      if (indexBean.getUsuario().getPerfil() == Perfiles.GESTOR) {
        consulta = consulta + " AND id_gestor = (SELECT id_gestor FROM gestor WHERE id_usuario = " + indexBean.getUsuario().getIdUsuario() + ");";
      } else {
        consulta = consulta + " AND id_gestor IN (SELECT id_gestor FROM gestor WHERE id_usuario IN (SELECT id_usuario FROM usuario WHERE id_despacho = " + indexBean.getUsuario().getDespacho().getIdDespacho() + "));";
      }
      List<Credito> lista = creditoDao.busquedaEspecialCreditos(consulta);
      List<Credito> listaFinal = new ArrayList();
      if (campanaSeleccionada.getIdCampana() == 0) {
        listaFinal = lista;
      } else {
        for (int i = 0; i < (lista.size()); i++) {
          if (lista.get(i).getCampana().getIdCampana().intValue() == campanaSeleccionada.getIdCampana()) {
            listaFinal.add(lista.get(i));
          }
        }
      }
      if (!colorSeleccionado.equals("0")) {
        List<Credito> listaRojas = new ArrayList();
        List<Credito> listaAmarillas = new ArrayList();
        List<Credito> listaVerdes = new ArrayList();
        for (int i = 0; i < (lista.size()); i++) {
          int dias = gestionDao.checarDiasSinGestionar(lista.get(i).getIdCredito()).intValue();
          if (dias <= 3) {
            listaVerdes.add(lista.get(i));
          }
          if ((dias > 3) && (dias <= 7)) {
            listaAmarillas.add(lista.get(i));
          }
          if (dias > 7) {
            listaRojas.add(lista.get(i));
          }
        }
        switch (colorSeleccionado) {
          case "Verde":
            listaCreditos2 = listaVerdes;
            break;
          case "Amarillo":
            listaCreditos2 = listaAmarillas;
            break;
          case "Rojo":
            listaCreditos2 = listaRojas;
            break;
        }
      } else {
        listaCreditos2 = listaFinal;
      }
    }
  }

  // METODO QUE ABRE LA VISTA DEL DETALLE DEL CREDITO DIRECCION
  public void abrirDetalleCreditoDireccion() {
    if (creditoDireccionSeleccionado != null) {
      Credito c = creditoDao.buscar(creditoDireccionSeleccionado.numeroCredito);
      if (c != null) {
        creditoActualBean.setCreditoActual(c);
        try {
          if (indexBean.getUsuario().getPerfil() == Perfiles.GESTOR) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("vistaCreditoGestor.xhtml");
          } else {
            FacesContext.getCurrentInstance().getExternalContext().redirect("vistaCreditoAdmin.xhtml");
          }
        } catch (IOException ioe) {
          Logs.log.error("No se pudo redirigir a la vista del credito.");
          Logs.log.error(ioe);
        }
      }
    } else {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No ha seleccionado ningun credito"));
    }
  }

  // METODO QUE ABRE LA VISTA DEL DETALLE DEL CREDITO
  public void abrirDetalleCredito() {
    if (creditoSeleccionado != null) {
      Credito c = creditoDao.buscarCreditoPorId(creditoSeleccionado.getIdCredito());
      if (c != null) {
        creditoActualBean.setCreditoActual(c);
        try {
          if (indexBean.getUsuario().getPerfil() == Perfiles.GESTOR) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("vistaCreditoGestor.xhtml");
          } else {
            FacesContext.getCurrentInstance().getExternalContext().redirect("vistaCreditoAdmin.xhtml");
          }
        } catch (IOException ioe) {
          Logs.log.error("No se pudo redirigir a la vista del credito.");
          Logs.log.error(ioe);
        }
      }
    } else {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No ha seleccionado ningun credito"));
    }
  }

  // METODO QUE PREPARA UNA CAMPAÑA SEGUN LOS CREDITOS FILTRADOS
  public void generarCampana(List<Credito> creditos) {
    if (indexBean.getUsuario().getPerfil() == Perfiles.GESTOR) {
      cuentasGestorBean.preparaCampanaBusqueda(creditos);
    }
  }
  
  // METODO QUE PREPARA UNA CAMPAÑA SEGUN LOS CREDITOS DIRECCION FILTRADOS
  public void generarCampanaCreditosDireccion(List<CreditoDireccion> creditos) {
    List<Credito> creds = new ArrayList();
    for (int i = 0; i <(creditos.size()); i++) {
      creds.add(creditoDao.buscar(creditos.get(i).getNumeroCredito()));
    }
    if (indexBean.getUsuario().getPerfil() == Perfiles.GESTOR) {
      cuentasGestorBean.preparaCampanaBusqueda(creds);
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

  public List<Subproducto> getListaSubproductos() {
    return listaSubproductos;
  }

  public void setListaSubproductos(List<Subproducto> listaSubproductos) {
    this.listaSubproductos = listaSubproductos;
  }

  public Subproducto getSubproductoSeleccionado() {
    return subproductoSeleccionado;
  }

  public void setSubproductoSeleccionado(Subproducto subproductoSeleccionado) {
    this.subproductoSeleccionado = subproductoSeleccionado;
  }

  public CreditoDireccion getCreditoDireccionSeleccionado() {
    return creditoDireccionSeleccionado;
  }

  public void setCreditoDireccionSeleccionado(CreditoDireccion creditoDireccionSeleccionado) {
    this.creditoDireccionSeleccionado = creditoDireccionSeleccionado;
  }

  public Credito getCreditoSeleccionado() {
    return creditoSeleccionado;
  }

  public void setCreditoSeleccionado(Credito creditoSeleccionado) {
    this.creditoSeleccionado = creditoSeleccionado;
  }

  public String getColorSeleccionado() {
    return colorSeleccionado;
  }

  public void setColorSeleccionado(String colorSeleccionado) {
    this.colorSeleccionado = colorSeleccionado;
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
