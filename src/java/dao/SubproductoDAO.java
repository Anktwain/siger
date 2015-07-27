/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Subproducto;
import java.util.List;

/**
 *
 * @author brionvega
 */
public interface SubproductoDAO {
    public boolean insertar(Subproducto subproducto);
    public boolean editar(Subproducto subproducto);
    public boolean eliminar(Subproducto subproducto);
    public Subproducto buscar(int idSubproducto);
    public List<Subproducto> buscarTodo();    
}
