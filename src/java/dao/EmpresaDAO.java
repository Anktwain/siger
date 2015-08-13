package dao;

import dto.Empresa;
import dto.Sujeto;
import java.util.List;

/**
 * La interfaz {@code EmpresaDAO} contiene los prototipos de las 
 * funciones que se realizarán sobre la tabla _ de la base de datos.
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public interface EmpresaDAO {

    /**
     *
     * @param empresa
     * @return
     */
    public boolean insertar(Empresa empresa);

    /**
     *
     * @param empresa
     * @return
     */
    public boolean editar(Empresa empresa);

    /**
     *
     * @param empresa
     * @return
     */
    public boolean eliminar(Empresa empresa);

    /**
     *
     * @param idEmpresa
     * @return
     */
    public Empresa buscar(int idEmpresa);

    /**
     *
   * @param idEmpresa
     * @return
     */
    public Empresa buscarEmpresaPorSujeto(int idEmpresa);
    
  /**
   *
   * @return
   */
  public List<Empresa> buscarTodo();    
}
