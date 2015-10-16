package carga.asignacion;

/**
 *
 * @author Cofradia
 */
public class Credito {

  private int id;
  private double saldoVencido;
  private int idDeudor;
  private int idGestor;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public double getSaldoVencido() {
    return saldoVencido;
  }

  public void setSaldoVencido(double saldoVencido) {
    this.saldoVencido = saldoVencido;
  }

  public int getIdDeudor() {
    return idDeudor;
  }

  public void setIdDeudor(int idDeudor) {
    this.idDeudor = idDeudor;
  }

  public int getIdGestor() {
    return idGestor;
  }

  public void setIdGestor(int idGestor) {
    this.idGestor = idGestor;
  }
  
  

}
