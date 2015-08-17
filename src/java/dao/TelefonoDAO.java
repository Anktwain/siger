package dao;

import dto.Telefono;
import java.util.List;

/**
 * La interfaz {@code TelefonoDAO} contiene los prototipos de las 
 * funciones que se realizarán sobre la tabla _ de la base de datos.
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public interface TelefonoDAO {

    /**
     *
     * @param telefono
     * @return
     */
    public boolean insertar(Telefono telefono);

    /**
     *
     * @param telefono
     * @return
     */
    public boolean editar(Telefono telefono);

    /**
     *
     * @param telefono
     * @return
     */
    public boolean eliminar(Telefono telefono);

    /**
     *
     * @param idTelefono
     * @return
     */
    public Telefono buscar(int idTelefono);

    /**
     *
     * @return
     */
    public List<Telefono> buscarTodo();
    
  /**
   *
   * @param idSujeto
   * @return
   */
  public List<Telefono> buscarPorSujeto(int idSujeto);
}
