/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Usuarios;
import java.util.List;

/**
 *
 * @author brionvega
 */
public interface UsuariosDAO {
    public int insertar(Usuarios usuario);
    public boolean editar(Usuarios usuario);
    public boolean eliminar(Usuarios usuario);
    public Usuarios buscar(int idUsuario);
    public Usuarios buscar(String nombre, String password);
    public Usuarios buscarPorCorreo(String nombreLogin, String correo);
    public List<Usuarios> buscarTodo();
    public List<Usuarios> buscarUsuariosNoConfirmados();
    public Usuarios buscarNombreLogin(String nombreLogin);
    public Usuarios buscarCorreo(String correo);
}
