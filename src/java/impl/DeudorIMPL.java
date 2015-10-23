package impl;

import dao.DeudorDAO;
import dto.Deudor;
import dto.Sujeto;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.constantes.Sujetos;
import util.log.Logs;

/**
 * La clase {@code DeudorIMPL} permite ...
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public class DeudorIMPL implements DeudorDAO {

    /**
     *
     *
     * @return
     */
    @Override
    public Deudor insertar(Deudor deudor) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();

        try {
            sesion.save(deudor);
            tx.commit();
            Logs.log.info("Se insertó un nuevo Deudor: id = " + deudor.getIdDeudor()
            + " asociado al Sujeto: " + deudor.getSujeto().getIdSujeto());
        } catch (HibernateException he) {
            deudor = null;
            if (tx != null) {
                tx.rollback();
            }
            Logs.log.error("No se pudo insertar Deudor");
            Logs.log.error(he.getMessage());
        } finally {
            cerrar(sesion);
        }
        
        return deudor;
    }

    /**
     *
     *
     * @return
     */
    @Override
    public boolean editar(Deudor deudor) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;

        try {
            sesion.update(deudor);
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
    public boolean eliminar(Deudor deudor) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;

        try {
            // Se colocará algo similar a esto: usuario.setPerfil(Perfiles.ELIMINADO);
            sesion.update(deudor);
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
    public Deudor buscar(int idDeudor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     *
     * @return
     */
    @Override
    public List<Deudor> buscarTodo() {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        List<Deudor> listaDeudors;

        try { // Buscamos a todos los usuarios que no hayan sido eliminados, un usuario eliminado tiene perfil = 0.
            listaDeudors = sesion.createSQLQuery("select c.* from sujeto s join deudor c"
                    + " on s.id_sujeto = c.id_sujeto"
                    + " where s.eliminado != " + Sujetos.ELIMINADO).addEntity(Deudor.class).list();
        } catch (HibernateException he) {
            listaDeudors = null;
            Logs.log.error(he.getMessage());
        } finally {
            cerrar(sesion);
        }

        return listaDeudors;
    }

  @Override
  public Deudor ultimoInsertado() {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = sesion.beginTransaction();
    Deudor deudor;
    
    try {
      deudor = (Deudor) sesion.createSQLQuery("SELECT * from deudor where id_deudor = (SELECT MAX(id_deudor) from deudor);").addEntity(Sujeto.class).uniqueResult();
    } catch (HibernateException he) {
      deudor = null;
      Logs.log.error(he.getMessage());
    } finally {
      cerrar(sesion);
    }
    return deudor;
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
