/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.ProductosDAO;
import dto.Productos;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

/**
 *
 * @author brionvega
 */
public class ProductosIMPL implements ProductosDAO {
    @Override
    public boolean insertar(Productos producto) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;
                
        try {
            sesion.save(producto);
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
    public boolean editar(Productos producto) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;
        
        try {
            sesion.update(producto);
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
    public boolean eliminar(Productos producto) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;
        
        try {
            // Se colocará algo similar a esto: usuario.setPerfil(Perfiles.ELIMINADO);
            sesion.update(producto);
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
    public Productos buscar(int idProducto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Productos> buscarTodo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void cerrar(Session sesion) {
        if(sesion.isOpen())
            sesion.close();
    }
}
