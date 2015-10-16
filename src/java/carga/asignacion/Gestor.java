
package carga.asignacion;

import java.util.ArrayList;

/**
 *
 * @author Cofradía
 */
public class Gestor {

  private int id;
  
  private double nuevoTotalAsignado;
  private double granTotalPorCobrar;

  public ArrayList<Credito> getCredsNuevosTotales() {
    return credsNuevosTotales;
  }

  public void setCredsNuevosTotales(ArrayList<Credito> credsNuevosTotales) {
    this.credsNuevosTotales = credsNuevosTotales;
  }
  private ArrayList<Credito> credsNuevosTotales;

  public Gestor(int id, double nuevoTotalAsignado, double granTotalPorCobrar) {
    this.id = id;
    this.nuevoTotalAsignado = nuevoTotalAsignado;
    this.granTotalPorCobrar = granTotalPorCobrar;
    credsNuevosTotales = new ArrayList<>();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public double getNuevoTotalAsignado() {
    return nuevoTotalAsignado;
  }

  public void setNuevoTotalAsignado(double nuevoTotalAsignado) {
    this.nuevoTotalAsignado = nuevoTotalAsignado;
  }

  public double getGranTotalPorCobrar() {
    return granTotalPorCobrar;
  }

  public void setGranTotalPorCobrar(double granTotalPorCobrar) {
    this.granTotalPorCobrar = granTotalPorCobrar;
  } 
}
