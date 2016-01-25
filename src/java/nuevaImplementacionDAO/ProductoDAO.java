package nuevaImplementacionDAO;

import dto.Producto;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public interface ProductoDAO {
  public boolean insert(Session session, Producto producto) throws Exception;
  public Producto getById(Session session, int idProducto) throws Exception;
  public List<Producto> getAll(Session session) throws Exception;
  public boolean update(Session session, Producto producto) throws Exception;
}
