/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.UsuariosDAO;
import dto.Usuarios;
import impl.UsuariosIMPL;

/**
 *
 * @author brionvega
 */
public class RecuperarContrasenaBean {
    private String correo;
    private String usuarioLogin;
    
    private Usuarios usuario;
    
    private UsuariosDAO usuarioDao;
    
    public  RecuperarContrasenaBean() {
        usuario = new Usuarios();
        usuarioDao = new UsuariosIMPL();
    }
    
    public void recuperar() {
        // busca que el usuario cuyos datos se especifican en los campos se encuentre en la BD
        usuario = usuarioDao.buscarPorCorreo(usuarioLogin, correo);
        
        // si el usuario está, entonces enviar un correo
        if(usuario != null) {
            
        } else {
            System.out.println("El usuario no existe");
        }
    }
}
