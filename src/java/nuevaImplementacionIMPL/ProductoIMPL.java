package nuevaImplementacionIMPL;

import dto.Producto;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author brionvega
 */
public class ProductoIMPL implements nuevaImplementacionDAO.ProductoDAO {

  @Override
  public boolean insert(Session session, Producto producto) throws Exception {
    session.save(producto);
    
    return true;
  }

  @Override
  public Producto getById(Session session, int idProducto) throws Exception {
    return (Producto) session.get(Producto.class, idProducto);
  }

  @Override
  public List<Producto> getAll(Session session) throws Exception {
    String hql = "from Producto";
    Query query = session.createQuery(hql);
    
    List<Producto> listaProducto = (List<Producto>) query.list();
    return listaProducto;
  }

  @Override
  public boolean update(Session session, Producto producto) throws Exception {
    session.update(producto);
    
    return true;
  }
  
}
