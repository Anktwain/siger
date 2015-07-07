/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Productos;
import java.util.List;

/**
 *
 * @author brionvega
 */
public interface ProductosDAO {
    public boolean insertar(Productos producto);
    public boolean editar(Productos producto);
    public boolean eliminar(Productos producto);
    public Productos buscar(int idProducto);
    public List<Productos> buscarTodo(); 
}
