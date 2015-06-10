/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import dao.UsuariosDAO;
import dto.Usuarios;
import java.util.List;
//import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.constantes.Perfiles;
import util.log.Logs;

/**
 *
 * @author brionvega
 */
public class UsuariosIMPL implements UsuariosDAO {
 //   private static Logger log = Logger.getLogger(Logs.class);
    
    @Override
    public boolean insertar(Usuarios usuario) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        boolean ok;
                
        try {
            sesion.save(usuario);
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
            if(usuario != null)
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
    public Usuarios buscar(String nombreLogin, String password){
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        Usuarios usuario;
        
        try { // Buscamos a todos los usuarios que no hayan sido eliminados, un usuario eliminado tiene perfil = 0.
            usuario = (Usuarios) sesion.createQuery("from Usuarios u where "
                    + "u.perfil != 0 and u.perfil != -2 and u.nombreLogin = '"
                    + nombreLogin + "' and u.password = '"
                    + password +"'").uniqueResult();
        } catch(HibernateException he) {
            usuario = null;
            he.printStackTrace();
        } finally {
            cerrar(sesion);
        }
        return usuario;
    }
    
    @Override
    public Usuarios buscarPorCorreo(String nombreLogin, String correo){
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        Usuarios usuario;
        
        try { // Buscamos a todos los usuarios que no hayan sido eliminados, un usuario eliminado tiene perfil = 0.
            usuario = (Usuarios) sesion.createQuery("from Usuarios u where "
                    + "u.perfil != 0 and u.nombreLogin = '"
                    + nombreLogin + "' and u.correo = '"
                    + correo +"'").uniqueResult();

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
            listaUsuarios = sesion.createQuery("from Usuarios u"
                    + " where u.perfil != " + Perfiles.ELIMINADO
                    + " and u.perfil != " + Perfiles.GESTOR_NO_CONFIRMADO).list();
        } catch(HibernateException he) {
            listaUsuarios = null;
            he.printStackTrace();
        } finally {
            cerrar(sesion);
        }
        
        return listaUsuarios;
    }

    @Override
    public Usuarios buscarNombreLogin(String nombreLogin) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        Usuarios usuario;
        
        try {
            usuario = (Usuarios) sesion.createQuery("from Usuarios u where "
                    + "u.perfil != " + Perfiles.ELIMINADO
                    + " and u.nombreLogin = '" + nombreLogin + "'").uniqueResult();
        } catch(HibernateException he) {
            usuario = null;
            he.printStackTrace();
        } finally {
            cerrar(sesion);
        }
        
        return usuario;
    }

    @Override
    public Usuarios buscarCorreo(String correo) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = sesion.beginTransaction();
        Usuarios usuario;
        
        try {
            usuario = (Usuarios) sesion.createQuery("from Usuarios u where "
                    + "u.perfil != " + Perfiles.ELIMINADO
                    + " and u.correo = '" + correo + "'").uniqueResult();
        } catch(HibernateException he) {
            usuario = null;
            he.printStackTrace();
        } finally {
            cerrar(sesion);
        }
        
        return usuario;
    }
    
    

    private void cerrar(Session sesion) {
        if(sesion.isOpen())
            sesion.close();
    }
    
}