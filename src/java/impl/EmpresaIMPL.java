/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

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
public class EmpresaIMPL implements EmpresaDAO {

    @Override
    public boolean insertar(Empresa empresa) {
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
    public boolean editar(Empresa empresa) {
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
    public boolean eliminar(Empresa empresa) {
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
    public Empresa buscar(int idEmpresa) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Empresa buscarEmpresaPorSujeto(int idEmpresa) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        Empresa empresa;
        
        try { 
            empresa = (Empresa) sesion.createSQLQuery("select * from empresa where sujetos_id_sujeto = " + Integer.toString(idEmpresa) + ";").addEntity(Empresa.class).uniqueResult();
        } catch(HibernateException he) {
            empresa = null;
            he.printStackTrace();
        } finally {
            cerrar(sesion);
        }
        return empresa;
    }

    @Override
    public List<Empresa> buscarTodo() {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        List<Empresa> listaEmpresa;
        
        try { // Buscamos todas las empresas.
            listaEmpresa = sesion.createSQLQuery("from Empresa").list();
        } catch(HibernateException he) {
            listaEmpresa = null;
            he.printStackTrace();
        } finally {
            cerrar(sesion);
        }
        return listaEmpresa;
        /*
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        */
    }
    
    private void cerrar(Session sesion) {
        if(sesion.isOpen())
            sesion.close();
    }
    
}