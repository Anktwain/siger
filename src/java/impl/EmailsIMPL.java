/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.EmailsDAO;
import dto.Emails;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.log.Logs;

/**
 *
 * @author brionvega
 */
public class EmailsIMPL implements EmailsDAO {

    @Override
    public boolean insertar(Emails email) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;
                
        try {
            sesion.save(email);
            tx.commit();
            ok = true;
            //log.info("Se insertó un nuevo usuaario");
        } catch(HibernateException he){
            ok = false;
            if(tx != null)
                tx.rollback();
//            he.printStackTrace();
            Logs.log.trace(he.getMessage());
        } finally {
            cerrar(sesion);
        }
        return ok;
    }

    @Override
    public boolean editar(Emails email) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;
        
        try {
            sesion.update(email);
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
    public boolean eliminar(Emails email) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;
        
        try {
            // Se colocará algo similar a esto: usuario.setPerfil(Perfiles.ELIMINADO);
            sesion.update(email);
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
    public Emails buscar(int idEmail) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Emails> buscarTodo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void cerrar(Session sesion) {
        if(sesion.isOpen())
            sesion.close();
    }
}
