/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Clientes;
import java.util.List;

/**
 *
 * @author brionvega
 */
public interface ClientesDAO {
    public boolean insertar(Clientes cliente);
    public boolean editar(Clientes cliente);
    public boolean eliminar(Clientes cliente);
    public Clientes buscar(int idCliente);
    public List<Clientes> buscarTodo();     
}
