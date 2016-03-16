/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.CreditoDAO;
import dao.DireccionDAO;
import dao.ZonaDAO;
import dto.Credito;
import dto.Direccion;
import dto.Zona;
import impl.CreditoIMPL;
import impl.DireccionIMPL;
import impl.ZonaIMPL;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.el.ELContext;
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
  private Zona zonaSeleccionada;
  private CreditoDireccion creditoSeleccionado;
  private List<Zona> listaZonas;
  private List<CreditoDireccion> listaCreditos;
  private List<Credito> listaCreditos2;
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
    zonaSeleccionada = new Zona();
    direccionCredito = new Direccion();
    creditoSeleccionado = new CreditoDireccion();
    creditoDao = new CreditoIMPL();
    zonaDao = new ZonaIMPL();
    direccionDao = new DireccionIMPL();
    obtenerListas();
  }

  // METODO QUE OBTIENE LAS LISTAS INICIALES
  public final void obtenerListas() {
    listaZonas = zonaDao.buscarPorDespacho(indexBean.getUsuario().getDespacho().getIdDespacho());
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
  public void obtenerCreditosPorSaldoVencido(){
    listaCreditos2 = creditoDao.buscarCreditosPorSaldoVencido(saldoInferior, saldoSuperior);
  }
  
  public void obtenerCreditosPorMesesVencidos(){
    listaCreditos2 = creditoDao.buscarCreditosPorMesesVencidos(mesesVencidos);
  }
  
  // METODO QUE OBTIENE EL SALDO VENCIDO DE UN CREDITO
  public float obtenerSaldoVencido(int idCredito){
    return creditoDao.buscarSaldoVencidoCredito(idCredito);
  }
  
  // METODO QUE OBTIENE LOS MESES VENCIDOS DEL CREDITO
  public int obtenerMesesVencidos(int idCredito){
    return creditoDao.buscarMesesVencidosCredito(idCredito);
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
