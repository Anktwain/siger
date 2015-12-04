package impl;

import dao.InstitucionDAO;
import dto.Institucion;
import dto.Sujeto;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.constantes.Sujetos;

/**
 * La clase {@code InstitucionIMPL} permite ...
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
public class InstitucionIMPL implements InstitucionDAO {

    /**
     *
     *
     * @return
     */
    @Override
    public boolean insertar(Institucion institucion) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;

        try {
            sesion.save(institucion);
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
    public boolean editar(Institucion institucion) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;

        try {
            sesion.update(institucion);
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
    public boolean eliminar(Institucion institucion) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;

        try {
            // Se colocará algo similar a esto: usuario.setPerfil(Perfiles.ELIMINADO);
            sesion.update(institucion);
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
    public Institucion buscar(int idInstitucion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Institucion buscarInstitucionPorSujeto(int idInstitucion) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        Institucion institucion;
        
        try { 
            institucion = (Institucion) sesion.createSQLQuery("select * from institucion where id_sujeto = " + Integer.toString(idInstitucion) + ";").addEntity(Institucion.class).uniqueResult();
        } catch(HibernateException he) {
            institucion = null;
            he.printStackTrace();
        } finally {
            cerrar(sesion);
        }
        return institucion;
    }

    /**
     *
     *
     * @return
     */
    @Override
    public List<Institucion> buscarTodo() {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        List<Institucion> listaInstitucion;

        try { // Buscamos todas las Institucions.
            listaInstitucion = sesion.createSQLQuery("from Institucion").list();
        } catch (HibernateException he) {
            listaInstitucion = null;
            he.printStackTrace();
        } finally {
            cerrar(sesion);
        }
        return listaInstitucion;
        /*
         throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
         */
    }
    /**
     *
     *
     * @return
     */
    
    @Override
    public List<Sujeto> buscarInstituciones() {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        List<Sujeto> listaSujeto;

        try { // Buscamos todas las Instituciones.
            //"select e.name, a.city from Employee e INNER JOIN e.address a"
            listaSujeto = sesion.createSQLQuery("select * from sujeto s join institucion e where s.eliminado = " + Sujetos.ACTIVO + " and s.id_sujeto = e.id_sujeto;").addEntity(Sujeto.class).list();
        } catch(HibernateException he) {
            listaSujeto = null;
            he.printStackTrace();
        } finally {
            cerrar(sesion);
        }
        return listaSujeto;
    }
    
    @Override
    public Number calcularRecuperacionDeInstitucion() {
    return 0;
    }
    
    @Override
    public Number calcularRecuperacionPorGestor(int idGestor) {
    return 0;
    }

  @Override
  public List<Institucion> buscarInstitucionesPorDespacho(int idDespacho) {
    Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        List<Institucion> listaInstituciones;
        
        try { 
            listaInstituciones = sesion.createSQLQuery("SELECT * FROM institucion i WHERE i.id_institucion IN (SELECT c.id_institucion FROM credito c WHERE c.id_despacho = 2);").addEntity(Institucion.class).list();
        } catch(HibernateException he) {
            listaInstituciones = null;
            he.printStackTrace();
        } finally {
            cerrar(sesion);
        }
        return listaInstituciones;
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
