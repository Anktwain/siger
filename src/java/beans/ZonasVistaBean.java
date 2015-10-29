package beans;

import dao.EstadoRepublicaDAO;
import dao.MunicipioDAO;
import dto.Colonia;
import dto.EstadoRepublica;
import dto.Municipio;
import impl.EstadoRepublicaIMPL;
import impl.MunicipioIMPL;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import util.log.Logs;

/**
 *
 * @author Pablo
 */
@ManagedBean(name = "zonasVistaBean")
@ViewScoped
public class ZonasVistaBean implements Serializable {

  /**
   * El nombre de la zona con el que se le identificará en su respectivo
   * despacho de creación. El nombre de zona es <strong> único por despacho
   * </strong>, pero dos despachos cualesquiera podrían tener zonas con los
   * mismos nombres.
   */
  private String nombre;

  /* **** Código de prueba *****/
//  private String estadoSeleccionado;
//
//  private List<String> estadosDePrueba;
//  private List<String> mpiosDePrueba;
//  private List<String> coloniasDePrueba;
//
//  private List<String> estadosAutoCompletados;
//  private List<String> mpiosAutoCompletados;
//  private List<String> coloniasAutoCompletadas;
//
//  private List<String> estadosSeleccionados;
//  private List<String> mpiosSeleccionados;
//  private List<String> coloniasSeleccionadas;
  /* **** Código de prueba *****/
  
  /**
   * Todos los estadosRep que se listarán en la vista.
   */
  private List<EstadoRepublica> estadosRep;
  /**
   * Todos los municipios que se desplegarán en la vista y que corresponden a
   * los del estado desplegado.
   */
  private List<Municipio> mpiosVisibles;
  /**
   * Todas las colonias que se desplegarán en la vista y que corresponden a los
   * del municipio desplegado.
   */
  private List<Colonia> coloniasVisibles;

  /**
   * Lista con las coincidencias encontradas que se desplegará cuando el usuario
   * introduzca un criterio de búsqueda de municipios en la vista.
   */
  private List<Municipio> mpiosAutoCompletados;
  /**
   * Lista con las coincidencias encontradas que se desplegará cuando el usuario
   * introduzca un criterio de búsqueda de colonias en la vista.
   */
  private List<Colonia> coloniasAutoCompletadas;

  /**
   * Lista en la que se almacenan todos los municipios seleccionados de todos
   * los estadosRep.
   */
  private List<Municipio> mpiosSeleccionados;
  /**
   * Lista en la que se almacenan todas las colonias seleccionadas de todos los
   * municipios.
   */
  private List<Colonia> coloniasSeleccionadas;

  /**
   * Objeto DAO para realizar las operaciones en la base de datos que
   * corresponden con la tabla Municipio.
   */
  private MunicipioDAO mpioDao;
  /**
   * Lista donde se almacenan todos los estadosRep de la república seleccionados
   * en la vista.
   */
  private List<EstadoRepublica> edosRepSeleccionados;
  /**
   * Estado que actualmente se despliega en la vista.
   */
  private EstadoRepublica edoRepVisible;
  /**
   * Número que corresponde al orden en la lista en el que se encuentra el
   * estado que actualmente se despliega en la vista.
   */
  private int idEdoVisible;

  private List<Municipio> mpiosDelEstadoRepSelec;
  private List<Colonia> coloniasDelEstadoRepSelec;

  public ZonasVistaBean() {

    /* **** Código de prueba *****/
//    estadosDePrueba = new ArrayList<>();
//    mpiosDePrueba = new ArrayList<>();
//    coloniasDePrueba = new ArrayList<>();
//
//    estadosAutoCompletados = new ArrayList<>();
//    mpiosAutoCompletados = new ArrayList<>();
//    coloniasAutoCompletadas = new ArrayList<>();
//
//    estadosSeleccionados = new ArrayList<>();
//    mpiosSeleccionados = new ArrayList<>();
//    coloniasSeleccionadas = new ArrayList<>();
//
//    estadosDePrueba.add("Aguascalientes");
//    estadosDePrueba.add("Chiapas");
//    estadosDePrueba.add("Distrito Federal");
//    estadosDePrueba.add("Querétaro");
//    estadosDePrueba.add("Zacatecas");
//
//    mpiosDePrueba.add("Álvaro Obregón");
//    mpiosDePrueba.add("Benito Juárez");
//    mpiosDePrueba.add("Cuauhtémoc");
//    mpiosDePrueba.add("Milpa Alta");
//    mpiosDePrueba.add("Venustiano Carranza");
//
//    coloniasDePrueba.add("Obrera");
//    coloniasDePrueba.add("Doctores");
//    coloniasDePrueba.add("Tránsito");
//    coloniasDePrueba.add("Pro hogar");
//    coloniasDePrueba.add("Anáhuac");
    /* **** Código de prueba *****/
    
    EstadoRepublicaDAO estadoRepDao = new EstadoRepublicaIMPL();
    mpioDao = new MunicipioIMPL();

    estadosRep = estadoRepDao.buscarTodo();
    edosRepSeleccionados = new ArrayList<>();

    coloniasVisibles = new ArrayList<>();

    mpiosDelEstadoRepSelec = new ArrayList<>();
    mpiosVisibles = new ArrayList<>();
    
    mpiosSeleccionados = new ArrayList<>();
    coloniasSeleccionadas = new ArrayList<>();

  }

//  public String getEstadosDePrueba() {
//    StringBuilder edp = new StringBuilder(estadosDePrueba.get(0));
//
//    for (int i = 1; i < estadosDePrueba.size(); i++) {
//      edp.append("\n");
//      edp.append(estadosDePrueba.get(i));
//    }
//    return edp.toString();
//  }
//
//  public void setEstadosDePrueba(List<String> estadosDePrueba) {
//    this.estadosDePrueba = estadosDePrueba;
//  }
//
//  public List<String> getMpiosDePrueba() {
//    return mpiosDePrueba;
//  }
//
//  public void setMpiosDePrueba(List<String> mpiosDePrueba) {
//    this.mpiosDePrueba = mpiosDePrueba;
//  }
//
//  public List<String> autocompletarEstados() {
//    estadosAutoCompletados.add(estadosDePrueba.get(0));
//    estadosAutoCompletados.add(estadosDePrueba.get(estadosDePrueba.size() - 1));
//    return estadosAutoCompletados;
//  }
//
//  public List<String> getEstadosAutoCompletados() {
//    return estadosAutoCompletados;
//  }
//
//  public void setEstadosAutoCompletados(List<String> estadosAutoCompletados) {
//    this.estadosAutoCompletados = estadosAutoCompletados;
//  }
//
//  public List<String> getEstadosSeleccionados() {
//    estadosSeleccionados = estadosDePrueba;
//    return estadosSeleccionados;
//  }
//
//  public void setEstadosSeleccionados(List<String> estadosSeleccionados) {
//    this.estadosSeleccionados = estadosSeleccionados;
//  }
//
//  public List<String> getMpiosSeleccionados() {
//    return mpiosSeleccionados;
//  }
//
//  public void setMpiosSeleccionados(List<String> mpiosSeleccionados) {
//    this.mpiosSeleccionados = mpiosSeleccionados;
//  }
//
//  public List<String> getMpiosAutoCompletados() {
//    return mpiosAutoCompletados;
//  }
//
//  public void setMpiosAutoCompletados(List<String> mpiosAutoCompletados) {
//    this.mpiosAutoCompletados = mpiosAutoCompletados;
//  }
//
//  public String getEstadoSeleccionado() {
//    return estadoSeleccionado;
//  }
//
//  public void setEstadoSeleccionado(String estadoSeleccionado) {
//    this.estadoSeleccionado = estadoSeleccionado;
//  }
//
//  public List<String> getColoniasDePrueba() {
//    return coloniasDePrueba;
//  }
//
//  public void setColoniasDePrueba(List<String> coloniasDePrueba) {
//    this.coloniasDePrueba = coloniasDePrueba;
//  }
//
//  public List<String> getColoniasAutoCompletadas() {
//    return coloniasAutoCompletadas;
//  }
//
//  public void setColoniasAutoCompletadas(List<String> coloniasAutoCompletadas) {
//    this.coloniasAutoCompletadas = coloniasAutoCompletadas;
//  }
//
//  public List<String> getColoniasSeleccionadas() {
//    return coloniasSeleccionadas;
//  }
//
//  public void setColoniasSeleccionadas(List<String> coloniasSeleccionadas) {
//    this.coloniasSeleccionadas = coloniasSeleccionadas;
//  }
  
  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public void onEstadosChange() {
  }

  public List<EstadoRepublica> getEstadosRep() {
    return estadosRep;
  }

  public void setEstadosRep(List<EstadoRepublica> estadosRep) {
    this.estadosRep = estadosRep;
  }

  public List<Municipio> getMpiosVisibles() {
    mpiosVisibles = mpioDao.buscarMunicipiosPorEstado(edosRepSeleccionados.get(idEdoVisible).getIdEstado());
    return mpiosVisibles;
  }

  public void setMpiosVisibles(List<Municipio> mpiosVisibles) {
    this.mpiosVisibles = mpiosVisibles;
  }

  public List<Colonia> getColoniasVisibles() {
    return coloniasVisibles;
  }

  public void setColoniasVisibles(List<Colonia> coloniasVisibles) {
    this.coloniasVisibles = coloniasVisibles;
  }

  public List<EstadoRepublica> getEdosRepSeleccionados() {
    return edosRepSeleccionados;
  }

  public void setEdosRepSeleccionados(List<EstadoRepublica> edosRepSelec) {
    this.edosRepSeleccionados = edosRepSelec;
  }

  public int getIdEdoVisible() {
    return idEdoVisible;
  }

  public void setIdEdoVisible(int idEdoVisible) {
    this.idEdoVisible = idEdoVisible;
  }

  public EstadoRepublica getEdoRepVisible() {
    return edoRepVisible;
  }

  public void setEdoRepVisible(EstadoRepublica edoRepVisible) {
    this.edoRepVisible = edoRepVisible;
  }

  public List<Municipio> getMpiosDelEstadoRepSelec() {
    return mpiosDelEstadoRepSelec;
  }

  public void setMpiosDelEstadoRepSelec(List<Municipio> mpiosDelEstadoRepSelec) {
    this.mpiosDelEstadoRepSelec = mpiosDelEstadoRepSelec;
  }

  public void onMpiosChange() {
  }

  public List<Colonia> getColoniasDelEstadoRepSelec() {
    return coloniasDelEstadoRepSelec;
  }

  public void setColoniasDelEstadoRepSelec(List<Colonia> coloniasDelEstadoRepSelec) {
    this.coloniasDelEstadoRepSelec = coloniasDelEstadoRepSelec;
  }

  public List<Municipio> getMpiosSeleccionados() {
    return mpiosSeleccionados;
  }

  public void setMpiosSeleccionados(List<Municipio> mpiosSeleccionados) {
    this.mpiosSeleccionados = mpiosSeleccionados;
  }

  public List<Colonia> getColoniasSeleccionadas() {
    return coloniasSeleccionadas;
  }

  public void setColoniasSeleccionadas(List<Colonia> coloniasSeleccionadas) {
    this.coloniasSeleccionadas = coloniasSeleccionadas;
  }

  
  
}
