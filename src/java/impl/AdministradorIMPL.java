package impl;

import dao.AdministradorDAO;
import dto.Administrativo;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

/**
 * La clase {@code AdministradorIMPL} permite ...
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public class AdministradorIMPL implements AdministradorDAO {

    /**
     *
     *
     * @return
     */
    @Override
    public boolean insertar(Administrativo administrador) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;

        try {
            sesion.save(administrador);
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
     * @param
     */
    private void cerrar(Session sesion) {
        if (sesion.isOpen()) {
            sesion.close();
        }
    }
}
