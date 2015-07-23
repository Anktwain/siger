/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Direcciones;
import java.util.List;

/**
 *
 * @author brionvega
 */
public interface DireccionesDAO {
    public boolean insertar(Direcciones direccion);
    public boolean editar(Direcciones direccion);
    public boolean eliminar(Direcciones direccion);
    public Direcciones buscar(int idDireccion);
    public List<Direcciones> buscarTodo();    
}
