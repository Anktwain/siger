/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Telefono;
import java.util.List;

/**
 *
 * @author brionvega
 */
public interface TelefonoDAO {
    public boolean insertar(Telefono telefono);
    public boolean editar(Telefono telefono);
    public boolean eliminar(Telefono telefono);
    public Telefono buscar(int idTelefono);
    public List<Telefono> buscarTodo();
}
