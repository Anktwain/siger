/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.SujetoDAO;
import dto.Sujeto;
import dao.EmpresaDAO;
import dto.Empresa;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

/**
 *
 * @author brionvega
 */
public class SujetoIMPL implements SujetoDAO {

    @Override
    public int insertar(Sujeto sujeto) {
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
    public boolean editar(Sujeto sujeto) {
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
    public boolean eliminar(Sujeto sujeto) {
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
    public Sujeto buscar(int idSujeto) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        Sujeto sujeto;
        
        try {
            sujeto = (Sujeto) sesion.get(Sujeto.class, idSujeto);
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
    public List<Sujeto> buscarTodo() {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        List<Sujeto> listaSujeto;
        
        try { 
            listaSujeto = sesion.createQuery("from Sujeto").list();
        } catch(HibernateException he) {
            listaSujeto = null;
            he.printStackTrace();
        } finally {
            cerrar(sesion);
        }
        return listaSujeto;
        /*
        return null;
        */
    }
    
    @Override
    public List<Sujeto> buscarEmpresas() {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        List<Sujeto> listaSujeto;
        
        try { // Buscamos todas las empresas.
            //"select e.name, a.city from Employee e INNER JOIN e.address a"
            listaSujeto = sesion.createSQLQuery("select s.* from sujetos s join empresas e on e.sujetos_id_sujeto=s.id_sujeto;").addEntity(Sujeto.class).list();
        } catch(HibernateException he) {
            listaSujeto = null;
            he.printStackTrace();
        } finally {
            cerrar(sesion);
        }
        return listaSujeto;
        /*
        return null;
        */
    }
    
    private void cerrar(Session sesion) {
        if(sesion.isOpen())
            sesion.close();
    }

}
