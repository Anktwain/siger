package impl;

import dao.DireccionDAO;
import dto.Direccion;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.log.Logs;

/**
 * La clase {@code DireccionIMPL} permite ...
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public class DireccionIMPL implements DireccionDAO {

    /**
     *
     *
     * @return
     */
    @Override
    public boolean insertar(Direccion direccion) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;

        try {
            sesion.save(direccion);
            tx.commit();
            ok = true;
            //log.info("Se insertó un nuevo usuaario");
        } catch (HibernateException he) {
            ok = false;
            if (tx != null) {
                tx.rollback();
            }
//            he.printStackTrace();
            Logs.log.trace(he.getMessage());
        } finally {
            cerrar(sesion);
        }
        return ok;
    }

    /**
     *
     *
     * @return
     */
    @Override
    public boolean editar(Direccion direccion) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;

        try {
            sesion.update(direccion);
            tx.commit();
            ok = true;
        } catch (HibernateException he) {
            ok = false;
            if (tx != null) {
                tx.rollback();
            }
            he.printStackTrace();
        } finally {
            cerrar(sesion);
        }

        return ok;
    }

    /**
     *
     *
     * @return
     */
    @Override
    public boolean eliminar(Direccion direccion) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;

        try {
            // Se colocará algo similar a esto: usuario.setPerfil(Perfiles.ELIMINADO);
            sesion.update(direccion);
            tx.commit();
            ok = true;
        } catch (HibernateException he) {
            ok = false;
            if (tx != null) {
                tx.rollback();
            }
            he.printStackTrace();
        } finally {
            cerrar(sesion);
        }

        return ok;
    }

    /**
     *
     *
     * @return
     */
    @Override
    public Direccion buscar(int idDireccion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     *
     * @return
     */
    @Override
    public List<Direccion> buscarTodo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     *
     * @param
     */
    private void cerrar(Session sesion) {
        if (sesion.isOpen()) {
            sesion.close();
        }
    }
}
