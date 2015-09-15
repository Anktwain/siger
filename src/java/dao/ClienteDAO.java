package dao;

import dto.Cliente;
import dto.Sujeto;
import java.util.List;

/**
 * La interfaz {@code ClienteDAO} contiene los prototipos de las 
 * funciones que se realizarán sobre la tabla _ de la base de datos.
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public interface ClienteDAO {

    /**
     *
     * @param cliente
     * @return
     */
    public Cliente insertar(Cliente cliente);

    /**
     *
     * @param cliente
     * @return
     */
    public boolean editar(Cliente cliente);

    /**
     *
     * @param cliente
     * @return
     */
    public boolean eliminar(Cliente cliente);

    /**
     *
     * @param idCliente
     * @return
     */
    public Cliente buscar(int idCliente);

    /**
     *
     * @return
     */
    public List<Cliente> buscarTodo();    
    
    public Cliente ultimoInsertado();
}
