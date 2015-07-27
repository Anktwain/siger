/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Direccion;
import java.util.List;

/**
 *
 * @author brionvega
 */
public interface DireccionDAO {
    public boolean insertar(Direccion direccion);
    public boolean editar(Direccion direccion);
    public boolean eliminar(Direccion direccion);
    public Direccion buscar(int idDireccion);
    public List<Direccion> buscarTodo();    
}
