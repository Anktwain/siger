package dao;

import dto.Gestor;
import java.util.List;

/**
 * La interfaz {@code GestorDAO} contiene los prototipos de las 
 * funciones que se realizarán sobre la tabla _ de la base de datos.
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public interface GestorDAO {

    /**
     *
     * @param gestor
     * @return
     */
    public boolean insertar(Gestor gestor);
    
    public Gestor buscar(int idGestor);
    
    public List<Gestor> buscarTodo();
    
    public List<Gestor> buscarPorDespacho(int idDespacho);
    
    public Gestor buscarGestorDelCredito(int idCredito);
    
    public List<Gestor> buscarPorDespachoExceptoEste(int idDespacho, int idGestor);
}
