package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Pablo
 */
@ManagedBean(name = "zonasVistaBean")
@ViewScoped
public class ZonasVistaBean implements Serializable {

  private List<String> estadosDePrueba;
  private List<String> mpiosDePrueba;

  private List<String> estadosAutoCompletados;
  private List<String> mpiosAutoCompletados;

  private List<String> estadosSeleccionados;
  private List<String> mpiosSeleccionados;

  public ZonasVistaBean() {

    estadosDePrueba = new ArrayList<>();
    mpiosDePrueba = new ArrayList<>();
    
    estadosAutoCompletados = new ArrayList<>();
    mpiosAutoCompletados = new ArrayList<>();
    
    estadosSeleccionados = new ArrayList<>();
    mpiosSeleccionados = new ArrayList<>();
    
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
  }

  public List<String> getEstadosDePrueba() {
    return estadosDePrueba;
  }

  public void setEstadosDePrueba(ArrayList<String> estadosDePrueba) {
    this.estadosDePrueba = estadosDePrueba;
  }

  public List<String> getMpiosDePrueba() {
    return mpiosDePrueba;
  }

  public void setMpiosDePrueba(ArrayList<String> mpiosDePrueba) {
    this.mpiosDePrueba = mpiosDePrueba;
  }

  public List<String> autocompletarEstados() {
    return estadosAutoCompletados;
  }

  public List<String> getEstadosAutoCompletados() {
    return estadosAutoCompletados;
  }

  public void setEstadosAutoCompletados(ArrayList<String> estadosAutoCompletados) {
    this.estadosAutoCompletados = estadosAutoCompletados;
  }

  public List<String> getEstadosSeleccionados() {
    return estadosSeleccionados;
  }

  public void setEstadosSeleccionados(ArrayList<String> estadosSeleccionados) {
    this.estadosSeleccionados = estadosSeleccionados;
  }

  public List<String> getMpiosSeleccionados() {
    return mpiosSeleccionados;
  }

  public void setMpiosSeleccionados(ArrayList<String> mpiosSeleccionados) {
    this.mpiosSeleccionados = mpiosSeleccionados;
  }

  public List<String> getMpiosAutoCompletados() {
    return mpiosAutoCompletados;
  }

  public void setMpiosAutoCompletados(List<String> mpiosAutoCompletados) {
    this.mpiosAutoCompletados = mpiosAutoCompletados;
  }

  
  
}
