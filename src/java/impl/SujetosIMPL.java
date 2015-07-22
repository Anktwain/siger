/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.SujetosDAO;
import dto.Sujetos;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

/**
 *
 * @author brionvega
 */
public class SujetosIMPL implements SujetosDAO {

    @Override
    public boolean insertar(Sujetos sujeto) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;
                
        try {
            sesion.save(sujeto);
            tx.commit();
            ok = true;
            //log.info("Se insertó un nuevo usuaario");
        } catch(HibernateException he){
            ok = false;
            if(tx != null)
                tx.rollback();
            he.printStackTrace();
   //         log.error(he.getMessage());
        } finally {
            cerrar(sesion);
        }
        return ok;
    }

    @Override
    public boolean editar(Sujetos sujeto) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;
        
        try {
            sesion.update(sujeto);
            tx.commit();
            ok = true;
        } catch(HibernateException he) {
            ok = false;
            if(tx != null)
                tx.rollback();
            he.printStackTrace();
        } finally {
            cerrar(sesion);
        }
        
        return ok;
    }

    @Override
    public boolean eliminar(Sujetos sujeto) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;
        
        try {
            // Se colocará algo similar a esto: usuario.setPerfil(Perfiles.ELIMINADO);
            sesion.update(sujeto);
            tx.commit();
            ok = true;
        } catch(HibernateException he) {
            ok = false;
            if(tx != null)
                tx.rollback();
            he.printStackTrace();
        } finally {
            cerrar(sesion);
        }
        
        return ok;
    }

    @Override
    public Sujetos buscar(int idSujeto) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        Sujetos sujeto;
        
        try {
            sujeto = (Sujetos) sesion.get(Sujetos.class, idSujeto);
            // obtuvo el usuario, solo se muestra si no ha sido eliminado:
            if(sujeto != null)
               // Colocar algo similar a esto: if(usuario.getPerfil() == Perfiles.ELIMINADO)
                    sujeto = null;
        } catch(HibernateException he) {
            sujeto = null;
            he.printStackTrace();
        } finally {
            cerrar(sesion);
        }
        
        return sujeto;
    }

    @Override
    public List<Sujetos> buscarTodo() {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        List<Sujetos> listaSujetos;
        
        try { // Buscamos todas las empresas.
            listaSujetos = sesion.createQuery("from Sujetos").list();
        } catch(HibernateException he) {
            listaSujetos = null;
            he.printStackTrace();
        } finally {
            cerrar(sesion);
        }
        return listaSujetos;
        /*
        return null;
        */
    }
    
    private void cerrar(Session sesion) {
        if(sesion.isOpen())
            sesion.close();
    }
}
