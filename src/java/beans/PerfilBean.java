package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import util.log.Logs;

/**
 *
 * @author Pablo
 */
@ManagedBean(name = "perfilBean")
@SessionScoped
public class PerfilBean implements Serializable {

  private String nombre;
  private int clave;
  private static ArrayList<PerfilBean> lista;

  public PerfilBean() {
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

  public static List<PerfilBean> enlistar() {
    if (lista == null) {
      lista = new ArrayList<>();
    }
    lista.clear();
    lista.add(new PerfilBean("administrador", util.constantes.Perfiles.ADMINISTRADOR));
    lista.add(new PerfilBean("eliminado", util.constantes.Perfiles.ELIMINADO));
    lista.add(new PerfilBean("gestor", util.constantes.Perfiles.GESTOR));
    lista.add(new PerfilBean("gestor no confirmado", util.constantes.Perfiles.GESTOR_NO_CONFIRMADO));
    Logs.log.info("***********Perfiles enlistados en PerfilBean.enlistar():");
    for (PerfilBean perfilEnlistado : lista) {
      Logs.log.info(perfilEnlistado.nombre + ": " + perfilEnlistado.clave);
    }
    return lista;
  }

  @Override
  public String toString(){
    return this.nombre;
  }
}
