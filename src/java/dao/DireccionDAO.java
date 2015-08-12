package dao;

import dto.Direccion;
import java.util.List;

/**
 * La interfaz {@code DireccionDAO} contiene los prototipos de las 
 * funciones que se realizarán sobre la tabla _ de la base de datos.
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public interface DireccionDAO {

    /**
     *
     * @param direccion
     * @return
     */
    public boolean insertar(Direccion direccion);

    /**
     *
     * @param direccion
     * @return
     */
    public boolean editar(Direccion direccion);

    /**
     *
     * @param direccion
     * @return
     */
    public boolean eliminar(Direccion direccion);

    /**
     *
     * @param idDireccion
     * @return
     */
    public Direccion buscar(int idDireccion);

    /**
     *
     * @return
     */
    public List<Direccion> buscarTodo();    
    
    public List<Direccion> buscarPorSujeto(int idSujeto);
}
