package dto;

import dao.GestorDAO;
import dto.Gestor;
import dto.Gestor;
import impl.GestorIMPL;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author brionvega
 */
public class Asignacion implements Serializable {
  
  /* El id del gestor asociado a esta asignación. */
  private int gestor;
  
  /* Todos los créditos asignados al gestor cuyo id se encuentra en este objeto
   Asignacion. */
  private List<Fila> creditos;
  
  /* El monto total, que se calcula sumando todos los saldos vencidos de los
  créditos contenidos en la lista "creditos". */
  private float montoTotal;
  
  /* El total de créditos asignados al gestor cuyo id se encuentra en este objeto
  Asignacion. Se calcula tomando el tamaño de la lista "creditos". */
  private int totalCreditos;
  
  /* Objeto que permite el acceso a la Base de Datos. */
  private GestorDAO gestorDao;

  public Asignacion() {
    creditos = new ArrayList<>();
    gestorDao = new GestorIMPL();
  }

  /* Setters y Getters*/
  public int getGestor() {
    return gestor;
  }

  public String getNombreDelGestor() {
    Gestor g = gestorDao.buscar(gestor);
    if(g!= null){
    return g.getUsuario().getNombre() + " " + g.getUsuario().getPaterno() + " " + g.getUsuario().getMaterno();
    }
    else{
      return "Gestor " + gestor;
    }
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
    for (Fila f : creditos) {
      montoTotal += Float.parseFloat(f.getSaldoVencido());
    }
    return montoTotal;
  }

  public int getTotalCreditos() {
    totalCreditos = creditos.size();
    return totalCreditos;
  }

}