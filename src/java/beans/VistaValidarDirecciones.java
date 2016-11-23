package beans;

import dao.ColoniaDAO;
import dao.CreditoDAO;
import dao.DireccionDAO;
import dao.DireccionTextoDAO;
import dao.EstadoRepublicaDAO;
import dao.MunicipioDAO;
import dao.RemesaDAO;
import dto.Colonia;
import dto.Credito;
import dto.Direccion;
import dto.DireccionTexto;
import dto.EstadoRepublica;
import dto.Municipio;
import impl.ColoniaIMPL;
import impl.CreditoIMPL;
import impl.DireccionIMPL;
import impl.DireccionTextoIMPL;
import impl.EstadoRepublicaIMPL;
import impl.MunicipioIMPL;
import impl.RemesaIMPL;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import util.GestionAutomatica;
import util.constantes.Direcciones;
import util.log.Logs;

/**
 *
 * @author brionvega
 */
@ManagedBean(name = "vistaValidarDirecciones")
@ViewScoped
public class VistaValidarDirecciones implements Serializable {

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  // VARIABLES DE CLASE
  private boolean habilitaColonias;
  private String mesCarga;
  private String despacho;
  private String coloniaBusqueda;
  private String cpBusqueda;
  private List<String> tiposAsentamiento;
  private List<DireccionTexto> direccionesPorValidar;
  private DireccionTexto direccionSeleccionada;
  private EstadoRepublica nuevoEstado;
  private EstadoRepublica nuevaColoniaEstado;
  private EstadoRepublica estadoBuscadorColonias;
  private Municipio nuevaColoniaMunicipio;
  private Municipio nuevoMunicipio;
  private Municipio municipioBuscadorColonias;
  private Colonia nuevaColonia;
  private Colonia nuevaColoniaColonia;
  private Colonia coloniaSeleccionadaBusqueda;
  private String nombreNuevaColonia;
  private String cpNuevaColonia;
  private String tipoNuevaColonia;
  private List<EstadoRepublica> listaEstados;
  private List<Municipio> listaMunicipios;
  private List<Colonia> listaColonias;
  private List<Colonia> listaColoniasBusqueda;
  private final EstadoRepublicaDAO estadoRepublicaDao;
  private final MunicipioDAO municipioDao;
  private final ColoniaDAO coloniaDao;
  private final DireccionDAO direccionDao;
  private final DireccionTextoDAO direccionTextoDao;
  private final CreditoDAO creditoDao;
  private final RemesaDAO remesaDao;

  // CONSTRUCTOR
  public VistaValidarDirecciones() {
    tiposAsentamiento = Arrays.asList("Aeropuerto", "Ampliación", "Barrio", "Campamento", "Ciudad", "Club de golf", "Colonia", "Condominio", "Congregación", "Conjunto habitacional", "Ejido", "Equipamiento", "Estación", "Exhacienda", "Finca", "Fraccionamiento", "Gran usuario", "Granja", "Hacienda", "Ingenio", "Paraje", "Parque industrial", "Poblado comunal", "Pueblo", "Puerto", "Ranchería", "Rancho", "Residencial", "Unidad habitacional", "Villa", "Zona comercial", "Zona federal", "Zona industrial", "Zona militar");
    habilitaColonias = false;
    nuevaColonia = new Colonia();
    nuevoMunicipio = new Municipio();
    municipioBuscadorColonias = new Municipio();
    nuevoEstado = new EstadoRepublica();
    estadoBuscadorColonias = new EstadoRepublica();
    nuevaColoniaColonia = new Colonia();
    nuevaColoniaMunicipio = new Municipio();
    nuevaColoniaEstado = new EstadoRepublica();
    estadoRepublicaDao = new EstadoRepublicaIMPL();
    municipioDao = new MunicipioIMPL();
    coloniaDao = new ColoniaIMPL();
    direccionDao = new DireccionIMPL();
    direccionTextoDao = new DireccionTextoIMPL();
    creditoDao = new CreditoIMPL();
    remesaDao = new RemesaIMPL();
    direccionSeleccionada = new DireccionTexto();
    direccionesPorValidar = new ArrayList();
    listaColoniasBusqueda = new ArrayList();
    obtenerDatos();
  }

  // METODO QUE OBTIENE LOS DATOS INICIALES
  public final void obtenerDatos() {
    listaEstados = estadoRepublicaDao.buscarTodo();
    direccionesPorValidar = direccionTextoDao.buscarSinValidar();
    Calendar c = Calendar.getInstance();
    c.setTime(remesaDao.obtenerUltimaRemesa().getFechaCarga());
    mesCarga = c.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("es", "MX")).toUpperCase();
    despacho = indexBean.getUsuario().getDespacho().getNombreCorto();
  }

  // METODO QUE PREPARA LA DIRECCION SELECCIONADA
  public void prepararDireccion() {
    listaColonias = coloniaDao.buscarPorCodigoPostal(direccionSeleccionada.getCodigoPostal());
    listaEstados = estadoRepublicaDao.buscarTodo();
    if (!listaColonias.isEmpty()) {
      listaMunicipios = municipioDao.buscarMunicipiosPorEstado(listaColonias.get(0).getMunicipio().getEstadoRepublica().getIdEstado());
      nuevaColonia.setCodigoPostal(direccionSeleccionada.getCodigoPostal());
      nuevoMunicipio = listaColonias.get(0).getMunicipio();
      nuevoEstado = listaColonias.get(0).getMunicipio().getEstadoRepublica();
      for (int i = 0; i < (listaColonias.size()); i++) {
        if (direccionSeleccionada.getColonia().contains(listaColonias.get(i).getNombre())) {
          nuevaColonia = listaColonias.get(i);
        }
      }
    } else {
      if (direccionSeleccionada.getCodigoPostal().matches("[0-9]{5}")) {
        nuevoEstado = listaColonias.get(0).getMunicipio().getEstadoRepublica();
        listaMunicipios = municipioDao.buscarMunicipiosPorEstado(listaColonias.get(0).getMunicipio().getEstadoRepublica().getIdEstado());
        nuevoMunicipio = listaColonias.get(0).getMunicipio();
        listaColonias = coloniaDao.buscarPorCodigoPostal(direccionSeleccionada.getCodigoPostal().substring(0, 3) + "00");
        nuevaColonia.setCodigoPostal(direccionSeleccionada.getCodigoPostal());
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion.", "No existen colonias con este codigo postal. Elija una colonia de la lista o agregue una nueva."));
      } else {
        // SE BUSCARA LA PRIMER COLONIA SOLO PARA RELLENAR
        nuevaColonia = coloniaDao.buscar(1);
        listaColonias = coloniaDao.buscarPorCodigoPostal(nuevaColonia.getCodigoPostal());
        nuevoEstado = listaColonias.get(0).getMunicipio().getEstadoRepublica();
        listaMunicipios = municipioDao.buscarMunicipiosPorEstado(listaColonias.get(0).getMunicipio().getEstadoRepublica().getIdEstado());
        nuevoMunicipio = listaColonias.get(0).getMunicipio();
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion.", "El codigo postal no es valido, seleccione los datos."));
      }
    }
    System.out.println("ESTADO PRECARGADO CON ID: " + nuevoEstado.getIdEstado());
  }

  // METODO QUE CARGA TODA LA COLONIA
  public void obtenerColoniaCompleta() {
    nuevaColonia = coloniaDao.buscar(nuevaColonia.getIdColonia());
  }

  // METODO QUE MUESTRA TODAS LAS COLONIAS DEL MUNICIPIO
  public void mostrarColonias() {
    listaColonias = coloniaDao.buscarColoniasPorMunicipio(nuevoMunicipio.getIdMunicipio());
    RequestContext.getCurrentInstance().update("validarDireccionForm");
  }

  // BUG:
  // agregar los metodos que borran las direcciones una vez validadas
  // METODO QUE VALIDA UNA DIRECCION
  public void validar() {
    nuevaColonia = coloniaDao.buscar(nuevaColonia.getIdColonia());
    nuevoMunicipio = municipioDao.buscar(nuevoMunicipio.getIdMunicipio());
    nuevoEstado = estadoRepublicaDao.buscar(nuevoEstado.getIdEstado());
    Direccion d = new Direccion();
    d.setCalle(direccionSeleccionada.getCalle());
    d.setExterior(direccionSeleccionada.getExterior());
    d.setInterior(direccionSeleccionada.getInterior());
    d.setColonia(nuevaColonia);
    d.setMunicipio(nuevoMunicipio);
    d.setEstadoRepublica(nuevoEstado);
    d.setLatitud(BigDecimal.ZERO);
    d.setLongitud(BigDecimal.ZERO);
    Credito c = creditoDao.buscar(direccionSeleccionada.getNumeroCredito());
    if (c != null) {
      d.setSujeto(c.getDeudor().getSujeto());
    // TO FIX:
      // AGREGAR UN MODULO PARA VERIFICAR SI LA DIRECCION YA EXISTE PARA ESE CREDITO, DE SER ASI YA NO INSERTARLA
      if (direccionDao.insertar(d) != null) {
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se ha validado la direccion"));
        direccionSeleccionada.setValidada(Direcciones.VALIDADA);
        if (!direccionTextoDao.editar(direccionSeleccionada)) {
          Logs.log.error("No se marco como validada la direccion texto con id:" + direccionSeleccionada.getIdDireccionTexto());
        }
        nuevaColonia = new Colonia();
        nuevoMunicipio = new Municipio();
        nuevoEstado = new EstadoRepublica();
        direccionesPorValidar = direccionTextoDao.buscarSinValidar();
        Logs.log.info("El administrador " + indexBean.getUsuario().getNombreLogin() + " valido una direccion asociada al sujeto " + d.getSujeto().getNombreRazonSocial());
        GestionAutomatica ga = new GestionAutomatica();
        if (!ga.generarGestionAutomatica("4DOMI", c, indexBean.getUsuario(), "SE VALIDA DIRECCION ASOCIADA AL DEUDOR " + d.getSujeto().getNombreRazonSocial())) {
          Logs.log.error("No se inserto la gestion automatica");
        }
      } else {
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se valido la direccion. Contacte al equipo de sistemas"));
      }
      limpiarBuscador();
    }else{
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El credito en esta direccion no existe."));
    }
  }

  // METODO QUE VALIDA UNA DIRECCION DE LA LISTA DE DIRECCIONES TEXTO
  public void validarDireccion(int idColonia) {
    Direccion d = new Direccion();
    Credito cre = creditoDao.buscar(direccionSeleccionada.getNumeroCredito());
    d.setSujeto(cre.getDeudor().getSujeto());
    d.setCalle(direccionSeleccionada.getCalle());
    d.setExterior(direccionSeleccionada.getExterior());
    d.setInterior(direccionSeleccionada.getInterior());
    Colonia c = coloniaDao.buscar(idColonia);
    d.setColonia(c);
    d.setMunicipio(c.getMunicipio());
    d.setEstadoRepublica(c.getMunicipio().getEstadoRepublica());
    d.setLatitud(BigDecimal.ZERO);
    d.setLongitud(BigDecimal.ZERO);
    // BUG:
    // se comenta porque el campo validada ahora es el campo principal
    // d.setValidada(Direcciones.VALIDADA);
    if (direccionDao.insertar(d) != null) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se valido la direccion."));
      direccionSeleccionada.setValidada(Direcciones.VALIDADA);
      if (!direccionTextoDao.editar(direccionSeleccionada)) {
        Logs.log.error("No se marco como validada la direccion texto id: " + direccionSeleccionada.getIdDireccionTexto());
      } else {
        direccionesPorValidar = direccionTextoDao.buscarSinValidar();
      }
      GestionAutomatica ga = new GestionAutomatica();
      if (!ga.generarGestionAutomatica("4DOMI", cre, indexBean.getUsuario(), "SE VALIDA DIRECCION ASOCIADA AL DEUDOR " + cre.getDeudor().getSujeto().getNombreRazonSocial())) {
        Logs.log.error("No se inserto la gestion automatica");
      }
    } else {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se valido la direccion. Contacte al equipo de sistemas"));
    }
    limpiarBuscador();
  }

  // METODO QUE LIMPIA EL BUSCADOR DE COLONIAS
  public void limpiarBuscador() {
    estadoBuscadorColonias = new EstadoRepublica();
    municipioBuscadorColonias = new Municipio();
    coloniaBusqueda = "";
    cpBusqueda = "";
    habilitaColonias = false;
    listaColoniasBusqueda = new ArrayList();
  }

  // METODO QUE PREPARA LAS LISTAS PARA UNA NUEVA COLONIA
  public void prepararCombos() {
    nuevaColoniaEstado = nuevoEstado;
    nuevaColoniaMunicipio = nuevoMunicipio;
  }

  // METODO QUE CREA UNA NUEVA COLONIA
  public void crearColonia() {
    if (!cpNuevaColonia.matches("(\\d)+")) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El codigo postal debe ser numerico."));
    } else if (!cpNuevaColonia.substring(0, 3).equals(direccionSeleccionada.getCodigoPostal().substring(0, 3))) {
      FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El codigo postal introducido no pertenece a este municipio."));
    } else {
      nuevaColoniaColonia.setCodigoPostal(cpNuevaColonia);
      nuevaColoniaColonia.setMunicipio(nuevaColoniaMunicipio);
      nuevaColoniaColonia.setNombre(nombreNuevaColonia);
      nuevaColoniaColonia.setTipo(tipoNuevaColonia);
      if (coloniaDao.insertar(nuevaColoniaColonia)) {
        Logs.log.info("El administrador " + indexBean.getUsuario().getNombreLogin() + " agrego la colonia " + nuevaColoniaColonia.getTipo() + " " + nuevaColoniaColonia.getNombre());
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se creo la nueva colonia. Ahora valide la direccion."));
        prepararDireccion();
        nuevaColonia = nuevaColoniaColonia;
      } else {
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo agregar la nueva colonia. Contacte al equipo de sistemas."));
      }
    }
  }

  // METODO QUE REGRESA UNA LISTA DE COLONIAS SEGUN LOS CRITERIOS SELECCIONADOS
  public void buscarColonias() {
    String consulta = "SELECT * FROM colonia WHERE";
    // SI SE ELIGIO UN ESTADO
    if (estadoBuscadorColonias.getIdEstado() != 0) {
      consulta = consulta + " id_municipio IN (SELECT id_municipio FROM municipio WHERE id_estado = " + estadoBuscadorColonias.getIdEstado() + ")";
      // SI SE ELIGIO ALGUN MUNICIPIO
      if (municipioBuscadorColonias.getIdMunicipio() != 0) {
        consulta = consulta + " AND id_municipio = " + municipioBuscadorColonias.getIdMunicipio();
        // SI SE ELIGIO ALGUN NOMBRE DE COLONIA
        if (!coloniaBusqueda.isEmpty()) {
          consulta = consulta + " AND nombre LIKE '%" + coloniaBusqueda + "%'";
          // SI SE ELIGIO ALGUN CODIGO POSTAL
          if (!cpBusqueda.isEmpty()) {
            consulta = consulta + " AND codigo_postal = '" + cpBusqueda + "'";
          }
        } // SI NO SE ELIGIO COLONIA
        else {
          // SI SE ELIGIO ALGUN CODIGO POSTAL
          if (!cpBusqueda.isEmpty()) {
            consulta = consulta + " AND codigo_postal = '" + cpBusqueda + "'";
          }
        }
      } // SI NO SE ELIGIO NINGUN MUNICIPIO
      else {
        // SI SE ELIGIO ALGUN NOMBRE DE COLONIA
        if (!coloniaBusqueda.isEmpty()) {
          consulta = consulta + " AND nombre LIKE '%" + coloniaBusqueda + "%'";
          // SI SE ELIGIO ALGUN CODIGO POSTAL
          if (!cpBusqueda.isEmpty()) {
            consulta = consulta + " AND codigo_postal = '" + cpBusqueda + "'";
          }
        } // SI NO SE ELIGIO COLONIA
        else {
          // SI SE ELIGIO ALGUN CODIGO POSTAL
          if (!cpBusqueda.isEmpty()) {
            consulta = consulta + " AND codigo_postal = '" + cpBusqueda + "'";
          }
        }
      }
    } // SI NO SE ELIGIO NINGUN ESTADO
    else {
      // SI SE ELIGIO ALGUN MUNICIPIO
      if (municipioBuscadorColonias.getIdMunicipio() != 0) {
        consulta = consulta + " AND id_municipio = " + municipioBuscadorColonias.getIdMunicipio();
        // SI SE ELIGIO ALGUN NOMBRE DE COLONIA
        if (!coloniaBusqueda.isEmpty()) {
          consulta = consulta + " AND nombre LIKE '%" + coloniaBusqueda + "%'";
          // SI SE ELIGIO ALGUN CODIGO POSTAL
          if (!cpBusqueda.isEmpty()) {
            consulta = consulta + " AND codigo_postal = '" + cpBusqueda + "'";
          }
        } // SI NO SE ELIGIO COLONIA
        else {
          // SI SE ELIGIO ALGUN CODIGO POSTAL
          if (!cpBusqueda.isEmpty()) {
            consulta = consulta + " AND codigo_postal = '" + cpBusqueda + "'";
          }
        }
      } // SI NO SE ELIGIO NINGUN MUNICIPIO
      else {
        // SI SE ELIGIO ALGUN NOMBRE DE COLONIA
        if (!coloniaBusqueda.isEmpty()) {
          consulta = consulta + " nombre LIKE '%" + coloniaBusqueda + "%'";
          // SI SE ELIGIO ALGUN CODIGO POSTAL
          if (!cpBusqueda.isEmpty()) {
            consulta = consulta + " AND codigo_postal = '" + cpBusqueda + "'";
          }
        } // SI NO SE ELIGIO COLONIA
        else {
          // SI SE ELIGIO ALGUN CODIGO POSTAL
          if (!cpBusqueda.isEmpty()) {
            consulta = consulta + " codigo_postal = '" + cpBusqueda + "'";
          }
        }
      }
    }
    if (!consulta.isEmpty()) {
      consulta = consulta + " ORDER BY nombre ASC;";
      listaColoniasBusqueda = coloniaDao.busquedaEspecialColonias(consulta);
      habilitaColonias = true;
    }
  }

  // METODO QUE PREPARA LA LISTA DE MUNICIPIOS DE ACUERDO AL ESTADO SELECCIONADO
  public void obtenerMunicipios(EstadoRepublica estado) {
    listaMunicipios = municipioDao.buscarMunicipiosPorEstado(estado.getIdEstado());
  }

  // METODO QUE PREPARA UNA LISTA DE COLONIAS DE ACUERDO AL MUNICIPIO SELECCIONADO
  public void obtenerColonias(Municipio m) {
    listaColonias = coloniaDao.buscarColoniasPorMunicipio(m.getIdMunicipio());
  }

  // METODO QUE PRECARGA DATOS EN BASE A LA DIRECCION SELECCIONADA EN EL BUSCADOR DE COLONIAS
  public void precargarBuscador() {
    estadoBuscadorColonias = nuevoEstado;
  }

  // METODO QUE GUARDA LA COLONIA SELECCIONADA EN EL BUSCADOR
  public void guardarColonia() {
    nuevaColonia = coloniaSeleccionadaBusqueda;
    nuevoMunicipio = coloniaSeleccionadaBusqueda.getMunicipio();
    RequestContext.getCurrentInstance().update("editarDireccionPorValidarForm:comboboxMunicipiosDireccionPorValidar");
    RequestContext.getCurrentInstance().update("editarDireccionPorValidarForm:comboboxColoniasDireccionPorValidar");
  }

  // GETTERS & SETTERS
  public List<DireccionTexto> getDireccionesPorValidar() {
    return direccionesPorValidar;
  }

  public void setDireccionesPorValidar(List<DireccionTexto> direccionesPorValidar) {
    this.direccionesPorValidar = direccionesPorValidar;
  }

  public DireccionTexto getDireccionSeleccionada() {
    return direccionSeleccionada;
  }

  public void setDireccionSeleccionada(DireccionTexto direccionSeleccionada) {
    this.direccionSeleccionada = direccionSeleccionada;
  }

  public Colonia getNuevaColonia() {
    return nuevaColonia;
  }

  public void setNuevaColonia(Colonia nuevaColonia) {
    this.nuevaColonia = nuevaColonia;
  }

  public Municipio getNuevoMunicipio() {
    return nuevoMunicipio;
  }

  public void setNuevoMunicipio(Municipio nuevoMunicipio) {
    this.nuevoMunicipio = nuevoMunicipio;
  }

  public EstadoRepublica getNuevoEstado() {
    return nuevoEstado;
  }

  public void setNuevoEstado(EstadoRepublica nuevoEstado) {
    this.nuevoEstado = nuevoEstado;
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

  public List<Colonia> getListaColonias() {
    return listaColonias;
  }

  public void setListaColonias(List<Colonia> listaColonias) {
    this.listaColonias = listaColonias;
  }

  public List<String> getTiposAsentamiento() {
    return tiposAsentamiento;
  }

  public void setTiposAsentamiento(List<String> tiposAsentamiento) {
    this.tiposAsentamiento = tiposAsentamiento;
  }

  public Colonia getNuevaColoniaColonia() {
    return nuevaColoniaColonia;
  }

  public void setNuevaColoniaColonia(Colonia nuevaColoniaColonia) {
    this.nuevaColoniaColonia = nuevaColoniaColonia;
  }

  public Municipio getNuevaColoniaMunicipio() {
    return nuevaColoniaMunicipio;
  }

  public void setNuevaColoniaMunicipio(Municipio nuevaColoniaMunicipio) {
    this.nuevaColoniaMunicipio = nuevaColoniaMunicipio;
  }

  public EstadoRepublica getNuevaColoniaEstado() {
    return nuevaColoniaEstado;
  }

  public void setNuevaColoniaEstado(EstadoRepublica nuevaColoniaEstado) {
    this.nuevaColoniaEstado = nuevaColoniaEstado;
  }

  public String getNombreNuevaColonia() {
    return nombreNuevaColonia;
  }

  public void setNombreNuevaColonia(String nombreNuevaColonia) {
    this.nombreNuevaColonia = nombreNuevaColonia;
  }

  public String getCpNuevaColonia() {
    return cpNuevaColonia;
  }

  public void setCpNuevaColonia(String cpNuevaColonia) {
    this.cpNuevaColonia = cpNuevaColonia;
  }

  public String getTipoNuevaColonia() {
    return tipoNuevaColonia;
  }

  public void setTipoNuevaColonia(String tipoNuevaColonia) {
    this.tipoNuevaColonia = tipoNuevaColonia;
  }

  public String getMesCarga() {
    return mesCarga;
  }

  public void setMesCarga(String mesCarga) {
    this.mesCarga = mesCarga;
  }

  public String getDespacho() {
    return despacho;
  }

  public void setDespacho(String despacho) {
    this.despacho = despacho;
  }

  public EstadoRepublica getEstadoBuscadorColonias() {
    return estadoBuscadorColonias;
  }

  public void setEstadoBuscadorColonias(EstadoRepublica estadoBuscadorColonias) {
    this.estadoBuscadorColonias = estadoBuscadorColonias;
  }

  public Municipio getMunicipioBuscadorColonias() {
    return municipioBuscadorColonias;
  }

  public void setMunicipioBuscadorColonias(Municipio municipioBuscadorColonias) {
    this.municipioBuscadorColonias = municipioBuscadorColonias;
  }

  public List<Colonia> getListaColoniasBusqueda() {
    return listaColoniasBusqueda;
  }

  public void setListaColoniasBusqueda(List<Colonia> listaColoniasBusqueda) {
    this.listaColoniasBusqueda = listaColoniasBusqueda;
  }

  public String getColoniaBusqueda() {
    return coloniaBusqueda;
  }

  public void setColoniaBusqueda(String coloniaBusqueda) {
    this.coloniaBusqueda = coloniaBusqueda;
  }

  public String getCpBusqueda() {
    return cpBusqueda;
  }

  public void setCpBusqueda(String cpBusqueda) {
    this.cpBusqueda = cpBusqueda;
  }

  public Colonia getColoniaSeleccionadaBusqueda() {
    return coloniaSeleccionadaBusqueda;
  }

  public void setColoniaSeleccionadaBusqueda(Colonia coloniaSeleccionadaBusqueda) {
    this.coloniaSeleccionadaBusqueda = coloniaSeleccionadaBusqueda;
  }

  public boolean isHabilitaColonias() {
    return habilitaColonias;
  }

  public void setHabilitaColonias(boolean habilitaColonias) {
    this.habilitaColonias = habilitaColonias;
  }

}
