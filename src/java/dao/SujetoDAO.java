package dao;

import dto.Sujeto;
import java.util.List;

/**
 * La interfaz {@code SujetoDAO} contiene los prototipos de las 
 * funciones que se realizarán sobre la tabla _ de la base de datos.
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public interface SujetoDAO {

    /**
     *
     * @param sujeto
     * @return
     */
    public int insertar(Sujeto sujeto);

    /**
     *
     * @param sujeto
     * @return
     */
    public boolean editar(Sujeto sujeto);

    /**
     *
     * @param sujeto
     * @return
     */
    public boolean eliminar(Sujeto sujeto);

    /**
     *
     * @param idSujeto
     * @return
     */
    public boolean eliminarEnSerio(Sujeto sujeto);
    public Sujeto buscar(int idSujeto);

    /**
     *
     * @return
     */
    public List<Sujeto> buscarTodo();

    /**
     *
     * @return
     */
    public List<Sujeto> buscarEmpresas();
}
