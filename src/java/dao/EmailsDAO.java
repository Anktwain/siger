/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Emails;
import java.util.List;

/**
 *
 * @author brionvega
 */
public interface EmailsDAO {
    public boolean insertar(Emails email);
    public boolean editar(Emails email);
    public boolean eliminar(Emails email);
    public Emails buscar(int idEmail);
    public List<Emails> buscarTodo();
}
