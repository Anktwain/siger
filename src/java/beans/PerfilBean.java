package beans;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import util.log.Logs;

/**
 *
 * @author Pablo
 */
@ManagedBean(name = "perfilBean")
@ViewScoped
public class PerfilBean implements Serializable {

  private String nombre;
  private int clave;
  private LinkedList<PerfilBean> lista;
  private int tamanioLista;

  public PerfilBean() {
    enlistar();
    tamanioLista = lista.size();
  }

  public PerfilBean(String nombre, int clave) {
    this.nombre = nombre;
    this.clave = clave;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public int getClave() {
    return clave;
  }

  public void setClave(int clave) {
    this.clave = clave;
  }

  /* Estas líneas son de prueba para ver si se inicializa como null la referencia al inicio del programa*/
  public final void enlistar() {
    if (lista == null) {
      lista = new LinkedList<>();
      lista.add(new PerfilBean("administrador", util.constantes.Perfiles.ADMINISTRADOR));
      lista.add(new PerfilBean("eliminado", util.constantes.Perfiles.ELIMINADO));
      lista.add(new PerfilBean("gestor", util.constantes.Perfiles.GESTOR));
      lista.add(new PerfilBean("gestor no confirmado", util.constantes.Perfiles.GESTOR_NO_CONFIRMADO));
    }
  }

  public List<PerfilBean> ordenarLista(int cvePerfActual) throws Exception {
    if (lista.get(0).clave != cvePerfActual) {
      PerfilBean perfilActual = buscar(cvePerfActual);
      lista.addFirst(perfilActual);
      lista.removeLastOccurrence(perfilActual);
    }
    if (lista.size() != tamanioLista) {
      throw new Exception("La lista no contiene la cantidad de perfiles con los que se inicializó");
    }
    return lista;
  }

  @Override
  public String toString() {
    return this.nombre;
  }

  public PerfilBean buscar(int cvePerfil) {
    int i;
    for (i = 0; lista.get(i).clave != cvePerfil; i++) {
    }
    return lista.get(i);
  }
}
