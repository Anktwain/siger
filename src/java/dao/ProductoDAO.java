/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Producto;
import java.util.List;

/**
 *
 * @author brionvega
 */
public interface ProductoDAO {
    public boolean insertar(Producto producto);
    public boolean editar(Producto producto);
    public boolean eliminar(Producto producto);
    public Producto buscar(int idProducto);
    public List<Producto> buscarTodo(); 
}
