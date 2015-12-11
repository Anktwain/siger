package beans;

import dao.EstadoRepublicaDAO;
import dao.GestorDAO;
import dto.EstadoRepublica;
import dto.Gestor;
import dto.Usuario;
import impl.EstadoRepublicaIMPL;
import impl.GestorIMPL;
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
  private List<Gestor> gestoresDespachoActual;
  private final String gestorSinSeleccion;

  public ZonaService() {
    EstadoRepublicaDAO estadoRepDao = new EstadoRepublicaIMPL();
    GestorDAO gestorDao = new GestorIMPL();

    this.lugarSinSeleccion = Constantes.LUGAR_SIN_SELECCION;
    estadosRep = estadoRepDao.buscarTodo();
    System.out.println("............................ Estados Listados en ZonaService ..........................");
    for (EstadoRepublica estadoIterador : estadosRep) {
      System.out.println(estadoIterador);
    }
    System.out.println("...................................... fin ............................................\n");

    this.gestorSinSeleccion = Constantes.GESTOR_SIN_SELECCION;
    this.gestoresDespachoActual = gestorDao.buscarTodo();
    System.out.println("............................ Gestores Listados en ZonaService ..........................");
    for (Gestor gestorIterador : gestoresDespachoActual) {
      System.out.println(gestorIterador);
    }
    System.out.println("...................................... fin ............................................\n");

  }

  public EstadoRepublica getEdoRepPorNombre(String nombre) {
    EstadoRepublica edoEncontrado = null;
    if (nombre.equals(this.lugarSinSeleccion)) {
      edoEncontrado = new EstadoRepublica(nombre);   // Revisar esta línea
    } else {
      for (EstadoRepublica edoRepIterador : this.estadosRep) {
        if (edoRepIterador.getNombre().equals(nombre)) {
          edoEncontrado = edoRepIterador;
        }
      }
    }
    System.out.println(" ..................... Se encontró: <" + edoEncontrado + ">");
    return edoEncontrado;
  }

  public Gestor getGestorPorNombre(String nombre) {
    Gestor gestorEncontrado = null;
    if (nombre.equals(this.gestorSinSeleccion)) {
      gestorEncontrado = new Gestor(new Usuario()); // Revisar esta línea
    } else {
      for (Gestor gestorIterador : this.gestoresDespachoActual) {
        if (gestorIterador.toString().equals(nombre)) {
          gestorEncontrado = gestorIterador;
        }
      }
    }
    System.out.println(" ..................... Se encontró: <" + gestorEncontrado + ">");
    return gestorEncontrado;
  }

}
