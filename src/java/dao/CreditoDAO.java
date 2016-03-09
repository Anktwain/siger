package dao;

import dto.Credito;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface CreditoDAO {
  public boolean editar(Credito credito);
  public Number contarCreditosActivos(int idDespacho);
  public Number contarCreditosActivosPorGestor(int idUsuario);
  public Credito buscarCreditoPorId(int idCredito);
  public int obtenerIdDelCredito(String numeroCredito);
  public List<Credito> creditosEnGestionPorDespacho(int idDespacho);
  public List<Credito> tablaCreditosEnGestionPorDespacho(int idDespacho);
  public List<Credito> buscarCreditosPorCliente(String numeroDeudor);
  public List<Credito> buscarCreditosRelacionados(Credito creditoActual);
  public List<String> barraBusquedaAdmin(String valor, int idDespacho);
  public Credito buscar(String numeroCredito);
  public Credito insertar(Credito credito);
  public List<Credito> buscarPorMarcaje(int idMarcaje);
  public List<Credito> buscarCreditosPorCampana(int idCampana);
  public List<Credito> buscarCreditosPorCampanaGestor(int idCampana, int idUsuario);
  public List<Credito> buscarCreditosConPromesaDePagoHoy();
  public List<Credito> buscarCreditosPorGestor(int idUsuario);
}
