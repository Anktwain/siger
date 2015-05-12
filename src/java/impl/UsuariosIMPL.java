/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.UsuariosDAO;
import dto.Usuarios;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.constantes.PerfilesEnum;

/**
 *
 * @author brionvega
 */
public class UsuariosIMPL implements UsuariosDAO {

    @Override
    public boolean insertar(Usuarios usuario) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;
                
        try {
            sesion.save(usuario);
            tx.commit();
            ok = true;
        } catch(HibernateException he){
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
    public boolean editar(Usuarios usuario) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;
        
        try {
            sesion.update(usuario);
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
    public boolean eliminar(Usuarios usuario) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean exito;
        
        try {
            usuario.setPerfil(PerfilesEnum.ELIMINADO);
            sesion.update(usuario);
            sesion.getTransaction().commit();
            exito = true;
        } catch(HibernateException he) {
            exito = false;
            if(tx != null)
                tx.rollback();
            he.printStackTrace();
        } finally {
            cerrar(sesion);
        }
        
        return exito;
    }

    @Override
    public Usuarios buscar(int idUsuario) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Usuarios> buscarTodo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void cerrar(Session sesion) {
        if(sesion.isOpen())
            sesion.close();
    }
    
}
