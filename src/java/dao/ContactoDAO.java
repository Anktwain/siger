/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Contacto;
import java.util.List;

/**
 *
 * @author brionvega
 */
public interface ContactoDAO {
    public boolean insertar(Contacto contacto);
    public boolean editar(Contacto contacto);
    public boolean eliminar(Contacto contacto);
    public Contacto buscar(int idContacto);
    public List<Contacto> buscarTodo();
}
