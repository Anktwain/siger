/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.SujetosDAO;
import dto.Sujetos;
import dao.EmpresasDAO;
import dto.Empresas;
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
    public int insertar(Sujetos sujeto) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        int id;
                
        try {
            sesion.save(sujeto);
            tx.commit();
            id = sujeto.getIdSujeto();
            //log.info("Se insertó un nuevo usuaario");
        } catch(HibernateException he){
            id = 0;
            if(tx != null)
                tx.rollback();
            he.printStackTrace();
   //         log.error(he.getMessage());
        } finally {
            cerrar(sesion);
        }
        return id;
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
        
        try { 
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
    
    @Override
    public List<Sujetos> buscarEmpresas() {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        List<Sujetos> listaSujetos;
        
        try { // Buscamos todas las empresas.
            //"select e.name, a.city from Employee e INNER JOIN e.address a"
            listaSujetos = sesion.createQuery("select s.nombreRazonSocial, s.rfc from Sujetos s, Empresas e where s.idSujeto = e.sujetos").list();
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
