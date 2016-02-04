package dao;
import dto.EstadoRepublica;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface EstadoRepublicaDAO {
    public List<EstadoRepublica> buscarTodo();
    public EstadoRepublica buscar(int idEstado);
    public EstadoRepublica buscar(String cadena);
    public EstadoRepublica buscarPorId(int idEstado);
}