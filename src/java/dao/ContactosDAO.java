/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Contactos;
import java.util.List;

/**
 *
 * @author brionvega
 */
public interface ContactosDAO {
    public boolean insertar(Contactos contacto);
    public boolean editar(Contactos contacto);
    public boolean eliminar(Contactos contacto);
    public Contactos buscar(int idContacto);
    public List<Contactos> buscarTodo();
}
