package dto;

import dao.GestorDAO;
import impl.GestorIMPL;
import impl.UsuarioIMPL;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author brionvega
 */
public class Asignacion implements Serializable {
  private int gestor;
  private List<Fila> creditos;
  private float montoTotal;
  private int totalCreditos;
  private GestorDAO gestorDao;

  public Asignacion() {
    creditos = new ArrayList<>();
    gestorDao = new GestorIMPL();
  }

  public int getGestor() {
    return gestor;
  }
  
  public String getNombreDelGestor() {
    Gestor g = gestorDao.buscar(gestor);
    return g.getUsuario().getNombre() + " " + g.getUsuario().getPaterno() + " " + g.getUsuario().getMaterno();
  }

  public void setGestor(int gestor) {
    this.gestor = gestor;
  }

  public List<Fila> getCreditos() {
    return creditos;
  }

  public void setCreditos(List<Fila> creditos) {
    this.creditos = creditos;
  }
  
  public void setCredito(Fila credito) {
    this.creditos.add(credito);
  }

  public float getMontoTotal() {
    montoTotal = (float) 0.0;
    for(Fila f : creditos) {
      montoTotal += Float.parseFloat(f.getSaldoVencido());
    }
    return montoTotal;
  }

  public int getTotalCreditos() {
    totalCreditos = creditos.size();
    return totalCreditos;
  }

}
