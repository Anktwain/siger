package dao;

import dto.Usuario;
import java.util.List;

/**
 * La interfaz {@code UsuarioDAO} contiene los prototipos de las 
 * funciones que se realizarán sobre la tabla _ de la base de datos.
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public interface UsuarioDAO {

    /**
     *
     * @param usuario
     * @return
     */
    public int insertar(Usuario usuario);

    /**
     *
     * @param usuario
     * @return
     */
    public boolean editar(Usuario usuario);

    /**
     *
     * @param usuario
     * @return
     */
    public boolean eliminar(Usuario usuario);

    /**
     *
     * @param idUsuario
     * @return
     */
    public Usuario buscar(int idUsuario);

    /**
     *
     * @param nombre
     * @param password
     * @return
     */
    public Usuario buscar(String nombre, String password);

    /**
     *
     * @param nombreLogin
     * @param correo
     * @return
     */
    public Usuario buscarPorCorreo(String nombreLogin, String correo);

    /**
     *
     * @return
     */
    public List<Usuario> buscarTodo();

    /**
     *
     * @return
     */
    public List<Usuario> buscarUsuariosNoConfirmados();

    /**
     *
     * @param nombreLogin
     * @return
     */
    public Usuario buscarNombreLogin(String nombreLogin);

    /**
     *
     * @param correo
     * @return
     */
    public Usuario buscarCorreo(String correo);
    
    public Usuario buscarUsuarioPorIdGestor(int idGestor);
    public List<Usuario> buscarGestores(int idDespacho);
}
