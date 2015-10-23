package impl;

import dao.TelefonoDAO;
import dto.Telefono;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.log.Logs;

/**
 * La clase {@code TelefonoIMPL} permite ...
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public class TelefonoIMPL implements TelefonoDAO {

    /**
     *
     *
     * @return
     */
    @Override
    public Telefono insertar(Telefono telefono) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();

        try {
            sesion.save(telefono);
            tx.commit();
            Logs.log.info("Se insertó un nuevo Telefono: id = " + telefono.getIdTelefono() +
                    " asociado a Sujeto: id = " + telefono.getSujeto().getIdSujeto());
        } catch (HibernateException he) {
            telefono = null;
            if (tx != null) {
                tx.rollback();
            }
            Logs.log.error("No se pudo insertar Telefono:");
            Logs.log.error(he.getMessage());
        } finally {
            cerrar(sesion);
        }
        return telefono;
    }

    /**
     *
     *
     * @return
     */
    @Override
    public boolean editar(Telefono telefono) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;

        try {
            sesion.update(telefono);
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
    public boolean eliminar(Telefono telefono) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;

        try {
            sesion.delete(telefono);
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
    public Telefono buscar(int idTelefono) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     *
     * @return
     */
    @Override
    public List<Telefono> buscarTodo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
  /**
   *
   * @param idSujeto
   * @return
   */
  @Override
    public List<Telefono> buscarPorSujeto(int idSujeto) {
      Session sesion = HibernateUtil.getSessionFactory().openSession();
      Transaction tx = sesion.beginTransaction();
      List<Telefono> listaTelefonos;
      String consulta = "select t.* from telefono t join sujeto s on s.id_sujeto=t.id_sujeto where s.id_sujeto=" + idSujeto + ";";
      
      try {
        listaTelefonos = sesion.createSQLQuery(consulta).addEntity(Telefono.class).list();
      } catch (HibernateException he) {
        listaTelefonos = null;
        Logs.log.error(he.getMessage());
      } finally {
        cerrar(sesion);
      }
      
      return listaTelefonos;
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
