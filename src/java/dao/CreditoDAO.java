package dao;

import dto.tablas.Creditos;
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
  public List<Creditos> creditosEnGestionPorDespacho(int idDespacho);
  public List<Credito> buscarCreditosPorCliente(String numeroDeudor);
  public List<Credito> buscarCreditosRelacionados(Credito creditoActual);
  public List<String> barraBusquedaAdmin(String valor, int idDespacho);
  public List<String> barraBusquedaGestor(String valor, int idUsuario);
  public Credito buscar(String numeroCredito);
  public Credito insertar(Credito credito);
}
