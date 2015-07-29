package impl;

import dao.SubproductoDAO;
import dto.Subproducto;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

/**
 * La clase {@code SubproductoIMPL} permite ...
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public class SubproductoIMPL implements SubproductoDAO {

    /**
     *
     *
     * @return
     */
    @Override
    public boolean insertar(Subproducto subproducto) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;

        try {
            sesion.save(subproducto);
            tx.commit();
            ok = true;
            //log.info("Se insertó un nuevo usuaario");
        } catch (HibernateException he) {
            ok = false;
            if (tx != null) {
                tx.rollback();
            }
            he.printStackTrace();
            //         log.error(he.getMessage());
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
    public boolean editar(Subproducto subproducto) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;

        try {
            sesion.update(subproducto);
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
    public boolean eliminar(Subproducto subproducto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     *
     * @return
     */
    @Override
    public Subproducto buscar(int idSubproducto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     *
     * @return
     */
    @Override
    public List<Subproducto> buscarTodo() {
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
