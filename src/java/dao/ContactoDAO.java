package dao;

import dto.Contacto;
import dto.tablas.Contactos;
import java.util.List;

/**
 * La interfaz {@code ContactoDAO} contiene los prototipos de las 
 * funciones que se realizarán sobre la tabla _ de la base de datos.
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public interface ContactoDAO {

    /**
     *
     * @param contacto
     * @return
     */
    public Contacto insertar(Contacto contacto);

    /**
     *
     * @param contacto
     * @return
     */
    public boolean editar(Contacto contacto);

    /**
     *
     * @param contacto
     * @return
     */
    public boolean eliminar(Contacto contacto);

    /**
     *
     * @param idContacto
     * @return
     */
    public Contacto buscar(int idContacto);

    /**
     *
     * @return
     */
    public List<Contacto> buscarTodo();
    
    public List<Contacto> buscarPorSujeto(int idSujeto);
    
    public List<Contactos> buscarContactoPorSujeto(int idSujeto);
}
