package dao;

import dto.Administrativo;

/**
 * La interfaz {@code AdministradorDAO} contiene los prototipos de las 
 * funciones que se realizarán sobre la tabla _ de la base de datos.
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public interface AdministradorDAO {

    /**
     *
     * @param administrador
     * @return
     */
    public boolean insertar(Administrativo administrador);
}
