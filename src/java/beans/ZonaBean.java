package beans;

import dto.Colonia;
import dto.EstadoRepublica;
import dto.Gestor;
import dto.Municipio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * La lógica del manejo de datos para la vista Zonas. El manejo de los controles
 * de la vista se hace en el Bean ZonasVistaBean
 *
 * @author Pablo
 * @see ZonasVistaBean
 */
@ManagedBean(name = "zonaBean")
@SessionScoped
public class ZonaBean implements Serializable {

  /**
   * El nombre de la zona con el que se le identificará en su respectivo
   * despacho de creación. El nombre de zona es <strong> único por despacho
   * </strong>, pero dos despachos cualesquiera podrían tener zonas con los
   * mismos nombres.
   */
  private String nombre;

  /**
   * Lista donde se almacenan todos los estadosRep de la república seleccionados
   * en la vista.
   */
  private List<EstadoRepublica> edosRepSeleccionados;

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
  
  private Gestor gestorAsignado;

  public ZonaBean() {
    edosRepSeleccionados = new ArrayList<>();
    mpiosSeleccionados = new ArrayList<>();
    coloniasSeleccionadas = new ArrayList<>();
    gestorAsignado = new Gestor();
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public List<EstadoRepublica> getEdosRepSeleccionados() {
    return edosRepSeleccionados;
  }

  public void setEdosRepSeleccionados(List<EstadoRepublica> edosRepSelec) {
    this.edosRepSeleccionados = edosRepSelec;
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

  public Gestor getGestorAsignado() {
    return gestorAsignado;
  }

  public void setGestorAsignado(Gestor gestorAsignado) {
    this.gestorAsignado = gestorAsignado;
  }

  
}
