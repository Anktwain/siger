package beans;

import dao.ColoniaDAO;
import dao.CreditoDAO;
import dao.DireccionDAO;
import dao.EstadoRepublicaDAO;
import dao.MunicipioDAO;
import dao.SujetoDAO;
import dto.Colonia;
import dto.Direccion;
import dto.EstadoRepublica;
import dto.Municipio;
import impl.ColoniaIMPL;
import impl.CreditoIMPL;
import impl.DireccionIMPL;
import impl.EstadoRepublicaIMPL;
import impl.MunicipioIMPL;
import impl.SujetoIMPL;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import util.GestionAutomatica;
import util.constantes.Directorios;
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
  private final String nombreArchivo;
  private List<String> tiposAsentamiento;
  private List<DirsPorValidar> direccionesPorValidar;
  private DirsPorValidar direccionSeleccionada;
  private Colonia nuevaColonia;
  private Municipio nuevoMunicipio;
  private EstadoRepublica nuevoEstado;
  private Colonia nuevaColoniaColonia;
  private Municipio nuevaColoniaMunicipio;
  private EstadoRepublica nuevaColoniaEstado;
  private String nombreNuevaColonia;
  private String cpNuevaColonia;
  private String tipoNuevaColonia;
  private List<EstadoRepublica> listaEstados;
  private List<Municipio> listaMunicipios;
  private List<Colonia> listaColonias;
  private final EstadoRepublicaDAO estadoRepublicaDao;
  private final MunicipioDAO municipioDao;
  private final ColoniaDAO coloniaDao;
  private final SujetoDAO sujetoDao;
  private final DireccionDAO direccionDao;
  private final CreditoDAO creditoDao;

  // CONSTRUCTOR
  public VistaValidarDirecciones() {
    tiposAsentamiento = Arrays.asList("Aeropuerto", "Ampliación", "Barrio", "Campamento", "Ciudad", "Club de golf", "Colonia", "Condominio", "Congregación", "Conjunto habitacional", "Ejido", "Equipamiento", "Estación", "Exhacienda", "Finca", "Fraccionamiento", "Gran usuario", "Granja", "Hacienda", "Ingenio", "Paraje", "Parque industrial", "Poblado comunal", "Pueblo", "Puerto", "Ranchería", "Rancho", "Residencial", "Unidad habitacional", "Villa", "Zona comercial", "Zona federal", "Zona industrial", "Zona militar");
    nombreArchivo = "direccionar201653105250.txt";
    nuevaColonia = new Colonia();
    nuevoMunicipio = new Municipio();
    nuevoEstado = new EstadoRepublica();
    nuevaColoniaColonia = new Colonia();
    nuevaColoniaMunicipio = new Municipio();
    nuevaColoniaEstado = new EstadoRepublica();
    estadoRepublicaDao = new EstadoRepublicaIMPL();
    municipioDao = new MunicipioIMPL();
    coloniaDao = new ColoniaIMPL();
    sujetoDao = new SujetoIMPL();
    direccionDao = new DireccionIMPL();
    creditoDao = new CreditoIMPL();
    direccionSeleccionada = new DirsPorValidar();
    direccionesPorValidar = obtenerListaDeDirecciones();
  }

  /**
   * Método que obtiene una lista de direcciones por validar.
   *
   * @return Una lista de las direcciones a validar
   */
  private List<DirsPorValidar> obtenerListaDeDirecciones() {
    String lineaActual;
    List<String> lineas = new ArrayList();
    List<DirsPorValidar> lista = new ArrayList();
    // TO FIX:
    // CAMBIAR EL ARCHIVO PARA QUE NO SIEMPRE ABRA EL MISMO
    try (BufferedReader buferLectura = new BufferedReader(new FileReader(Directorios.RUTA_REMESAS + nombreArchivo))) {
      while ((lineaActual = buferLectura.readLine()) != null) {
        lineas.add(lineaActual);
      }
      buferLectura.close();
      for (int i = 0; i < (lineas.size()); i++) {
        String[] arr = lineas.get(i).split(";");
        if (arr.length == 7) {
          DirsPorValidar d = new DirsPorValidar();
          d.setIdSujeto(Integer.parseInt(arr[0]));
          d.setNumeroCredito(arr[1]);
          d.setCalle(arr[2]);
          d.setColonia(arr[3]);
          d.setMunicipio(arr[4]);
          d.setEstado(arr[5]);
          d.setCp(arr[6]);
          lista.add(d);
        }
      }
    } catch (IOException ioe) {
      Logs.log.error("Error de lectura en archivo de direcciones");
      Logs.log.error(ioe.getMessage());
      lista = null;
    }
    return lista;
  }

  // METODO QUE PREPARA LA DIRECCION SELECCIONADA
  public void prepararDireccion() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    try {
      listaColonias = coloniaDao.buscarPorCodigoPostal(direccionSeleccionada.getCp());
      if (!listaColonias.isEmpty()) {
        listaEstados = estadoRepublicaDao.buscarTodo();
        listaMunicipios = municipioDao.buscarMunicipiosPorEstado(listaColonias.get(0).getMunicipio().getEstadoRepublica().getIdEstado());
        nuevaColonia.setCodigoPostal(direccionSeleccionada.getCp());
        nuevoMunicipio = listaColonias.get(0).getMunicipio();
        nuevoEstado = listaColonias.get(0).getMunicipio().getEstadoRepublica();
        for (int i = 0; i < (listaColonias.size()); i++) {
          if (direccionSeleccionada.getColonia().contains(listaColonias.get(i).getNombre())) {
            nuevaColonia = listaColonias.get(i);
          }
        }
      } else {
        listaColonias = coloniaDao.buscarPorCodigoPostal(direccionSeleccionada.getCp().substring(0, 3) + "00");
        listaEstados = estadoRepublicaDao.buscarTodo();
        nuevaColonia.setCodigoPostal(direccionSeleccionada.getCp());
        listaMunicipios = municipioDao.buscarMunicipiosPorEstado(listaColonias.get(0).getMunicipio().getEstadoRepublica().getIdEstado());
        nuevoMunicipio = listaColonias.get(0).getMunicipio();
        nuevoEstado = listaColonias.get(0).getMunicipio().getEstadoRepublica();
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion.", "No existen colonias con este codigo postal. Elija una colonia de la lista o agregue una nueva."));
      }
    } catch (Exception e) {
    }
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

  // METODO QUE VALIDA UNA DIRECCION
  public void validar() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    nuevaColonia = coloniaDao.buscar(nuevaColonia.getIdColonia());
    nuevoMunicipio = municipioDao.buscar(nuevoMunicipio.getIdMunicipio());
    nuevoEstado = estadoRepublicaDao.buscar(nuevoEstado.getIdEstado());
    Direccion d = new Direccion();
    // TO FIX
    // SE DEBERAN CARGAR DESDE LA REMESA
    d.setExterior("S/N");
    d.setLatitud(BigDecimal.ZERO);
    d.setLongitud(BigDecimal.ZERO);
    d.setCalle(direccionSeleccionada.getCalle());
    d.setColonia(nuevaColonia);
    d.setEstadoRepublica(nuevoEstado);
    d.setMunicipio(nuevoMunicipio);
    d.setSujeto(sujetoDao.buscar(direccionSeleccionada.getIdSujeto()));
    // VERIFICAR SI LA DIRECCION YA EXISTE PARA ESE CREDITO, DE SER ASI YA NO INSERTARLA
    if (direccionDao.insertar(d) != null) {
      if (borrarDireccionDeArchivo()) {
        nuevaColonia = new Colonia();
        nuevoMunicipio = new Municipio();
        nuevoEstado = new EstadoRepublica();
        direccionesPorValidar = obtenerListaDeDirecciones();
        Logs.log.info("El administrador " + indexBean.getUsuario().getNombreLogin() + " valido una direccion asociada al sujeto " + d.getSujeto().getNombreRazonSocial());
        GestionAutomatica.generarGestionAutomatica("4DOMI", creditoDao.buscarPorSujeto(direccionSeleccionada.getIdSujeto()), indexBean.getUsuario(), "SE VALIDA DIRECCION ASOCIADA AL CLIENTE: " + d.getSujeto().getNombreRazonSocial());
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se ha validado la direccion"));
      } else {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se actualizo el archivo. Contacte al equipo de sistemas"));
      }
    } else {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se valido la direccion. Contacte al equipo de sistemas"));
    }

  }

  // METODO QUE ELIMINA DEL ARCHIVO LA DIRECCION QUE YA FUE VALIDADA
  public boolean borrarDireccionDeArchivo() {
    // SE CONSTRUYE LA CADENA DE TEXTO A BUSCAR
    String direccion = Integer.toString(direccionSeleccionada.getIdSujeto()) + ";" + direccionSeleccionada.getNumeroCredito() + ";";
    // SE ESCRIBE UN NUEVO ARCHIVO SIN ESCRIBIR LA COINCIDENCIA
    File inputFile = new File(Directorios.RUTA_REMESAS + nombreArchivo);
    File tempFile = new File(Directorios.RUTA_REMESAS + "tmp" + nombreArchivo);
    try {
      BufferedReader reader = new BufferedReader(new FileReader(inputFile));
      BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
      String currentLine;
      while ((currentLine = reader.readLine()) != null) {
        if (currentLine.contains(direccion)) {
          continue;
        }
        writer.write(currentLine + System.getProperty("line.separator"));
      }
      writer.close();
      reader.close();
    } catch (Exception e) {
      Logs.log.error("Error de lectura/escritura en archivo de direcciones");
      Logs.log.error(e.getMessage());
    }
    // SE ELIMINA EL ARCHIVO ORIGINAL
    inputFile.delete();
    // SE RENOMBRA EL ARCHIVO CON EL NOMBRE ORIGINAL
    return tempFile.renameTo(inputFile);
  }

  // METODO QUE PREPARA LAS LISTAS PARA UNA NUEVA COLONIA
  public void prepararCombos() {
    nuevaColoniaEstado = nuevoEstado;
    nuevaColoniaMunicipio = nuevoMunicipio;
  }

  // METODO QUE CREA UNA NUEVA COLONIA
  public void crearColonia() {
    FacesContext contexto = FacesContext.getCurrentInstance();
    if (!cpNuevaColonia.matches("(\\d)+")) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El codigo postal debe ser numerico."));
    } else if (!cpNuevaColonia.substring(0, 3).equals(direccionSeleccionada.getCp().substring(0, 3))) {
      contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "El codigo postal introducido no pertenece a este municipio."));
    } else {
      nuevaColoniaColonia.setCodigoPostal(cpNuevaColonia);
      nuevaColoniaColonia.setMunicipio(nuevaColoniaMunicipio);
      nuevaColoniaColonia.setNombre(nombreNuevaColonia);
      nuevaColoniaColonia.setTipo(tipoNuevaColonia);
      if (coloniaDao.insertar(nuevaColoniaColonia)) {
        listaColonias = coloniaDao.buscarPorCodigoPostal(cpNuevaColonia);
        nombreNuevaColonia = "";
        cpNuevaColonia = "";
        Logs.log.info("El administrador " + indexBean.getUsuario().getNombreLogin() + " agrego la colonia " + nuevaColoniaColonia.getTipo() + " " + nuevaColoniaColonia.getNombre());
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacion exitosa.", "Se creo la nueva colonia. Ahora valide la direccion."));
      } else {
        contexto.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error.", "No se pudo agregar la nueva colonia. Contacte al equipo de sistemas."));
      }
    }
  }

  // GETTERS & SETTERS
  public void setDireccionesPorValidar(List<DirsPorValidar> direccionesPorValidar) {
    this.direccionesPorValidar = direccionesPorValidar;
  }

  public List<DirsPorValidar> getDireccionesPorValidar() {
    return direccionesPorValidar;
  }

  public DirsPorValidar getDireccionSeleccionada() {
    return direccionSeleccionada;
  }

  public void setDireccionSeleccionada(DirsPorValidar direccionSeleccionada) {
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

  // CLASE MIEMBRO QUE PREPARA LAS DIRECCIONES PARA VALIDAR
  public static class DirsPorValidar {

    private int idSujeto;
    private String numeroCredito;
    private String calle;
    private String colonia;
    private String municipio;
    private String estado;
    private String cp;

    public DirsPorValidar() {
    }

    public int getIdSujeto() {
      return idSujeto;
    }

    public void setIdSujeto(int idSujeto) {
      this.idSujeto = idSujeto;
    }

    public String getNumeroCredito() {
      return numeroCredito;
    }

    public void setNumeroCredito(String numeroCredito) {
      this.numeroCredito = numeroCredito;
    }

    public String getCalle() {
      return calle;
    }

    public void setCalle(String calle) {
      this.calle = calle;
    }

    public String getColonia() {
      return colonia;
    }

    public void setColonia(String colonia) {
      this.colonia = colonia;
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

    public String getCp() {
      return cp;
    }

    public void setCp(String cp) {
      this.cp = cp;
    }

  }

}
