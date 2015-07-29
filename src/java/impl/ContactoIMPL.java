package impl;

import dao.ContactoDAO;
import dto.Contacto;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.log.Logs;

/**
 * La clase {@code ContactoIMPL} permite ...
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public class ContactoIMPL implements ContactoDAO {

    /**
     *
     *
     * @return
     */
    @Override
    public boolean insertar(Contacto contacto) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;

        try {
            sesion.save(contacto);
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
    public boolean editar(Contacto contacto) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;

        try {
            sesion.update(contacto);
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
    public boolean eliminar(Contacto contacto) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;

        try {
            // Se colocará algo similar a esto: usuario.setPerfil(Perfiles.ELIMINADO);
            sesion.update(contacto);
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
    public Contacto buscar(int idContacto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     *
     * @return
     */
    @Override
    public List<Contacto> buscarTodo() {
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
