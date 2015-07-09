/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Subproductos;
import java.util.List;

/**
 *
 * @author brionvega
 */
public interface SubproductosDAO {
    public boolean insertar(Subproductos subproducto);
    public boolean editar(Subproductos subproducto);
    public boolean eliminar(Subproductos subproducto);
    public Subproductos buscar(int idSubproducto);
    public List<Subproductos> buscarTodo();    
}
