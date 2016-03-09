package dao;

import dto.Institucion;
import dto.Sujeto;
import java.util.List;

/**
 * La interfaz {@code InstitucionDAO} contiene los prototipos de las 
 * funciones que se realizarán sobre la tabla _ de la base de datos.
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public interface InstitucionDAO {

    /**
     *
     * @param institucion
     * @return
     */
    public boolean insertar(Institucion institucion);

    /**
     *
     * @param institucion
     * @return
     */
    public boolean editar(Institucion institucion);

    /**
     *
     * @param institucion
     * @return
     */
    public boolean eliminar(Institucion institucion);

    /**
     *
     * @param idInstitucion
     * @return
     */
    public Institucion buscar(int idInstitucion);

    /**
     *
   * @param idInstitucion
     * @return
     */
    public Institucion buscarInstitucionPorSujeto(int idInstitucion);
    
  /**
   *
   * @return
   */
  public List<Institucion> buscarTodo();
  
    /**
     *
     * @return
     */
    public List<Sujeto> buscarInstituciones();
    
    public List<Institucion> buscarInstitucionesPorDespacho(int idDespacho);
}
