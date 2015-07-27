/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Cliente;
import java.util.List;

/**
 *
 * @author brionvega
 */
public interface ClienteDAO {
    public boolean insertar(Cliente cliente);
    public boolean editar(Cliente cliente);
    public boolean eliminar(Cliente cliente);
    public Cliente buscar(int idCliente);
    public List<Cliente> buscarTodo();     
}
