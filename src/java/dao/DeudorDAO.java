package dao;

import dto.Deudor;
import dto.Sujeto;
import java.util.List;

/**
 * La interfaz {@code DeudorDAO} contiene los prototipos de las 
 * funciones que se realizarán sobre la tabla _ de la base de datos.
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public interface DeudorDAO {

    /**
     *
     * @param deudor
     * @return
     */
    public Deudor insertar(Deudor deudor);

    /**
     *
     * @param deudor
     * @return
     */
    public boolean editar(Deudor deudor);

    /**
     *
     * @param deudor
     * @return
     */
    public boolean eliminar(Deudor deudor);

    /**
     *
     * @param idDeudor
     * @return
     */
    public Deudor buscar(int idDeudor);

    /**
     *
     * @return
     */
    public List<Deudor> buscarTodo();    
    
    public Deudor ultimoInsertado();
}
