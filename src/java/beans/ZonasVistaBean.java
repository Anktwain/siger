package beans;

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

  private String nombre;
  
  private String estadoSeleccionado;
  
  private List<String> estadosDePrueba;
  private List<String> mpiosDePrueba;
  private List<String> coloniasDePrueba;

  private List<String> estadosAutoCompletados;
  private List<String> mpiosAutoCompletados;
  private List<String> coloniasAutoCompletadas;

  private List<String> estadosSeleccionados;
  private List<String> mpiosSeleccionados;
  private List<String> coloniasSeleccionados;

  public ZonasVistaBean() {

    estadosDePrueba = new ArrayList<>();
    mpiosDePrueba = new ArrayList<>();
    coloniasDePrueba = new ArrayList<>();

    estadosAutoCompletados = new ArrayList<>();
    mpiosAutoCompletados = new ArrayList<>();
    coloniasAutoCompletadas = new ArrayList<>();

    estadosSeleccionados = new ArrayList<>();
    mpiosSeleccionados = new ArrayList<>();
    coloniasSeleccionados = new ArrayList<>();

    estadosDePrueba.add("Aguascalientes");
    estadosDePrueba.add("Chiapas");
    estadosDePrueba.add("Distrito Federal");
    estadosDePrueba.add("Querétaro");
    estadosDePrueba.add("Zacatecas");

    mpiosDePrueba.add("Álvaro Obregón");
    mpiosDePrueba.add("Benito Juárez");
    mpiosDePrueba.add("Cuauhtémoc");
    mpiosDePrueba.add("Milpa Alta");
    mpiosDePrueba.add("Venustiano Carranza");
    
    coloniasDePrueba.add("Obrera");
    coloniasDePrueba.add("Doctores");
    coloniasDePrueba.add("Tránsito");
    coloniasDePrueba.add("Pro hogar");
    coloniasDePrueba.add("Anáhuac");
   
  }

  public String getEstadosDePrueba() {
    StringBuilder edp = new StringBuilder(estadosDePrueba.get(0));

    for (int i = 1; i < estadosDePrueba.size(); i++) {
      edp.append("\n");
      edp.append(estadosDePrueba.get(i));
    }
    return edp.toString();
  }

  public void setEstadosDePrueba(List<String> estadosDePrueba) {
    this.estadosDePrueba = estadosDePrueba;
  }
  
  public List<String> getMpiosDePrueba() {
    return mpiosDePrueba;
  }

  public void setMpiosDePrueba(List<String> mpiosDePrueba) {
    this.mpiosDePrueba = mpiosDePrueba;
  }

  public List<String> autocompletarEstados() {
    estadosAutoCompletados.add(estadosDePrueba.get(0));
    estadosAutoCompletados.add(estadosDePrueba.get(estadosDePrueba.size()-1));
    return estadosAutoCompletados;
  }

  public List<String> getEstadosAutoCompletados() {
    return estadosAutoCompletados;
  }

  public void setEstadosAutoCompletados(List<String> estadosAutoCompletados) {
    this.estadosAutoCompletados = estadosAutoCompletados;
  }

  public List<String> getEstadosSeleccionados() {
    estadosSeleccionados = estadosDePrueba;
    return estadosSeleccionados;
  }

  public void setEstadosSeleccionados(List<String> estadosSeleccionados) {
    this.estadosSeleccionados = estadosSeleccionados;
  }

  public List<String> getMpiosSeleccionados() {
    return mpiosSeleccionados;
  }

  public void setMpiosSeleccionados(List<String> mpiosSeleccionados) {
    this.mpiosSeleccionados = mpiosSeleccionados;
  }

  public List<String> getMpiosAutoCompletados() {
    return mpiosAutoCompletados;
  }

  public void setMpiosAutoCompletados(List<String> mpiosAutoCompletados) {
    this.mpiosAutoCompletados = mpiosAutoCompletados;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public void onEstadosChange() {
  }

  public String getEstadoSeleccionado() {
    return estadoSeleccionado;
  }

  public void setEstadoSeleccionado(String estadoSeleccionado) {
    this.estadoSeleccionado = estadoSeleccionado;
  }

  public List<String> getColoniasDePrueba() {
    return coloniasDePrueba;
  }

  public void setColoniasDePrueba(List<String> coloniasDePrueba) {
    this.coloniasDePrueba = coloniasDePrueba;
  }

  public List<String> getColoniasAutoCompletadas() {
    return coloniasAutoCompletadas;
  }

  public void setColoniasAutoCompletadas(List<String> coloniasAutoCompletadas) {
    this.coloniasAutoCompletadas = coloniasAutoCompletadas;
  }

  public List<String> getColoniasSeleccionados() {
    return coloniasSeleccionados;
  }

  public void setColoniasSeleccionados(List<String> coloniasSeleccionados) {
    this.coloniasSeleccionados = coloniasSeleccionados;
  }
  
  
  

}
