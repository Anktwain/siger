/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Usuario;
import java.util.List;

/**
 *
 * @author brionvega
 */
public interface UsuarioDAO {
    public int insertar(Usuario usuario);
    public boolean editar(Usuario usuario);
    public boolean eliminar(Usuario usuario);
    public Usuario buscar(int idUsuario);
    public Usuario buscar(String nombre, String password);
    public Usuario buscarPorCorreo(String nombreLogin, String correo);
    public List<Usuario> buscarTodo();
    public List<Usuario> buscarUsuariosNoConfirmados();
    public Usuario buscarNombreLogin(String nombreLogin);
    public Usuario buscarCorreo(String correo);
}
