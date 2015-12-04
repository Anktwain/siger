package beans;

import dao.EstadoRepublicaDAO;
import dto.EstadoRepublica;
import impl.EstadoRepublicaIMPL;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import util.constantes.Constantes;

/**
 *
 * @author Pablo
 */
@ManagedBean(name = "zonaService", eager = true)
@ApplicationScoped
public class ZonaService {

  private final String lugarSinSeleccion;
  private final List<EstadoRepublica> estadosRep;

  public ZonaService() {
    EstadoRepublicaDAO estadoRepDao = new EstadoRepublicaIMPL();
    this.lugarSinSeleccion = Constantes.LUGAR_SIN_SELECCION;
    estadosRep = estadoRepDao.buscarTodo();
    System.out.println("............................ Estados Listados en ZonaService ..........................");
    for (EstadoRepublica estadoIterador : estadosRep) {
      System.out.println(estadoIterador);
    }
    System.out.println("...................................... fin ............................................\n");

  }

  public EstadoRepublica getEdoRepPorNombre(String nombre) {
    if (nombre.equals(this.lugarSinSeleccion)) {
      return new EstadoRepublica(nombre);
    } else {
      for (EstadoRepublica edoRepIterador : this.estadosRep) {
        if (edoRepIterador.getNombre().equals(nombre)) {
          return edoRepIterador;
        }
      }
    }
    return null;
  }

}
