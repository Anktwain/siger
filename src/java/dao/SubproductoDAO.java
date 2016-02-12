package dao;

import dto.Subproducto;
import java.util.List;

/**
 * La interfaz {@code SubproductoDAO} contiene los prototipos de las 
 * funciones que se realizarán sobre la tabla _ de la base de datos.
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public interface SubproductoDAO {

    /**
     *
     * @param subproducto
     * @return
     */
    public boolean insertar(Subproducto subproducto);

    /**
     *
     * @param subproducto
     * @return
     */
    public boolean editar(Subproducto subproducto);

    /**
     *
     * @param subproducto
     * @return
     */
    public boolean eliminar(Subproducto subproducto);

    /**
     *
     * @param idSubproducto
     * @return
     */
    public Subproducto buscar(int idSubproducto);
    public Subproducto buscar(String nombreSubproducto);

    /**
     *
     * @return
     */
    public List<Subproducto> buscarTodo();
    
    public List<Subproducto> buscarSubproductosPorProducto(int idProducto);
}
