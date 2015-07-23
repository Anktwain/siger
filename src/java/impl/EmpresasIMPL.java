/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

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
public class EmpresasIMPL implements EmpresasDAO {

    @Override
    public boolean insertar(Empresas empresa) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;
                
        try {
            sesion.save(empresa);
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
    public boolean editar(Empresas empresa) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;
        
        try {
            sesion.update(empresa);
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
    public boolean eliminar(Empresas empresa) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;
        
        try {
            // Se colocará algo similar a esto: usuario.setPerfil(Perfiles.ELIMINADO);
            sesion.update(empresa);
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
    public Empresas buscar(int idEmpresa) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Empresas> buscarTodo() {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        List<Empresas> listaEmpresas;
        
        try { // Buscamos todas las empresas.
            listaEmpresas = sesion.createQuery("from Empresas").list();
        } catch(HibernateException he) {
            listaEmpresas = null;
            he.printStackTrace();
        } finally {
            cerrar(sesion);
        }
        return listaEmpresas;
        /*
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        */
    }
    
    private void cerrar(Session sesion) {
        if(sesion.isOpen())
            sesion.close();
    }
    
}