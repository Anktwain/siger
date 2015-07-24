/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Telefonos;
import java.util.List;

/**
 *
 * @author brionvega
 */
public interface TelefonosDAO {
    public boolean insertar(Telefonos telefono);
    public boolean editar(Telefonos telefono);
    public boolean eliminar(Telefonos telefono);
    public Telefonos buscar(int idTelefono);
    public List<Telefonos> buscarTodo();
}
