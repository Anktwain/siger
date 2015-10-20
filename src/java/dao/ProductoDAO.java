package dao;

import dto.Producto;
import java.util.List;

/**
 * La interfaz {@code ProductoDAO} contiene los prototipos de las 
 * funciones que se realizarán sobre la tabla _ de la base de datos.
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public interface ProductoDAO {

    /**
     *
     * @param producto
     * @return
     */
    public boolean insertar(Producto producto);

    /**
     *
     * @param producto
     * @return
     */
    public boolean editar(Producto producto);

    /**
     *
     * @param producto
     * @return
     */
    public boolean eliminar(Producto producto);

    /**
     *
     * @param idProducto
     * @return
     */
    public Producto buscar(int idProducto);

    /**
     *
     * @return
     */
    public List<Producto> buscarTodo();
    
    public List<Producto> buscarProductosPorInstitucion(int idInstitucion);
}
