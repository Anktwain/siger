/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Email;
import java.util.List;

/**
 *
 * @author brionvega
 */
public interface EmailDAO {
    public boolean insertar(Email email);
    public boolean editar(Email email);
    public boolean eliminar(Email email);
    public Email buscar(int idEmail);
    public List<Email> buscarTodo();
}
