package impl;

import dao.ClienteDAO;
import dto.Cliente;
import dto.Sujeto;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.constantes.Sujetos;
import util.log.Logs;

/**
 * La clase {@code ClienteIMPL} permite ...
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public class ClienteIMPL implements ClienteDAO {

    /**
     *
     *
     * @return
     */
    @Override
    public Cliente insertar(Cliente cliente) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();

        try {
            sesion.save(cliente);
            tx.commit();
            Logs.log.info("Se insertó un nuevo Cliente: id = " + cliente.getIdCliente()
            + " asociado al Sujeto: " + cliente.getSujeto().getIdSujeto());
        } catch (HibernateException he) {
            cliente = null;
            if (tx != null) {
                tx.rollback();
            }
            Logs.log.error("No se pudo insertar Cliente");
            Logs.log.error(he.getMessage());
        } finally {
            cerrar(sesion);
        }
        
        return cliente;
    }

    /**
     *
     *
     * @return
     */
    @Override
    public boolean editar(Cliente cliente) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;

        try {
            sesion.update(cliente);
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
    public boolean eliminar(Cliente cliente) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;

        try {
            // Se colocará algo similar a esto: usuario.setPerfil(Perfiles.ELIMINADO);
            sesion.update(cliente);
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
    public Cliente buscar(int idCliente) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     *
     * @return
     */
    @Override
    public List<Cliente> buscarTodo() {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        List<Cliente> listaClientes;

        try { // Buscamos a todos los usuarios que no hayan sido eliminados, un usuario eliminado tiene perfil = 0.
            listaClientes = sesion.createSQLQuery("select c.* from sujeto s join cliente c"
                    + " on s.id_sujeto = c.sujetos_id_sujeto"
                    + " where s.eliminado != " + Sujetos.ELIMINADO).addEntity(Cliente.class).list();
        } catch (HibernateException he) {
            listaClientes = null;
            Logs.log.error(he.getMessage());
        } finally {
            cerrar(sesion);
        }

        return listaClientes;
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
