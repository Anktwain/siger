package impl;

import dao.EstadoRepublicaDAO;
import dto.EstadoRepublica;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

/**
 *
 * @author Eduardo
 */
public class EstadoRepublicaIMPL implements EstadoRepublicaDAO{
    
    @Override
    public List<EstadoRepublica> buscarTodo() {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        List<EstadoRepublica> estados;
        try { 
            estados = sesion.createSQLQuery("select * from estado_republica order by nombre asc;").addEntity(EstadoRepublica.class).list();
        } catch(HibernateException he) {
            estados = null;
            he.printStackTrace();
        } finally {
            cerrar(sesion);
        }
        return estados;
    }
    
    private void cerrar(Session sesion) {
        if (sesion.isOpen()) {
            sesion.close();
        }
    }
        
}
