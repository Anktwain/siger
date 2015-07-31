package dao;

import dto.Email;
import java.util.List;

/**
 * La interfaz {@code EmailDAO} contiene los prototipos de las 
 * funciones que se realizarán sobre la tabla _ de la base de datos.
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public interface EmailDAO {

    /**
     *
     * @param email
     * @return
     */
    public boolean insertar(Email email);

    /**
     *
     * @param email
     * @return
     */
    public boolean editar(Email email);

    /**
     *
     * @param email
     * @return
     */
    public boolean eliminar(Email email);

    /**
     *
     * @param idEmail
     * @return
     */
    public Email buscar(int idEmail);

    /**
     *
     * @return
     */
    public List<Email> buscarTodo();
}
