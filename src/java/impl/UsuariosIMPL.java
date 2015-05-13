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
import util.constantes.Perfiles;

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
        boolean ok;
        
        try {
            usuario.setPerfil(Perfiles.ELIMINADO);
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
    public Usuarios buscar(int idUsuario) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        Usuarios usuario;
        
        try {
            usuario = (Usuarios) sesion.get(Usuarios.class, idUsuario);
            // obtuvo el usuario, solo se muestra si no ha sido eliminado:
            if(usuario.getPerfil() == Perfiles.ELIMINADO)
                usuario = null;
        } catch(HibernateException he) {
            usuario = null;
            he.printStackTrace();
        } finally {
            cerrar(sesion);
        }
        
        return usuario;
    }

    @Override
    public List<Usuarios> buscarTodo() {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        List<Usuarios> listaUsuarios;
        
        try { // Buscamos a todos los usuarios que no hayan sido eliminados, un usuario eliminado tiene perfil = 0.
            listaUsuarios = sesion.createQuery("from Usuarios u where u.perfil != 0").list();
        } catch(HibernateException he) {
            listaUsuarios = null;
            he.printStackTrace();
        } finally {
            cerrar(sesion);
        }
        
        return listaUsuarios;
    }

    private void cerrar(Session sesion) {
        if(sesion.isOpen())
            sesion.close();
    }
    
}