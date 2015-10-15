
package carga.asignacion;

/**
 *
 * @author Cofradía
 */
public class Gestor {

  private int id;
  
  private double nuevoTotalAsignado;
  private double granTotalPorCobrar;

  public Gestor(int id, double nuevoTotalAsignado, double granTotalPorCobrar) {
    this.id = id;
    this.nuevoTotalAsignado = nuevoTotalAsignado;
    this.granTotalPorCobrar = granTotalPorCobrar;
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
