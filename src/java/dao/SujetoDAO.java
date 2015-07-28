/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Sujeto;
import java.util.List;

/**
 *
 * @author brionvega
 */
public interface SujetoDAO {
    public int insertar(Sujeto sujeto);
    public boolean editar(Sujeto sujeto);
    public boolean eliminar(Sujeto sujeto);
    public boolean eliminarEnSerio(Sujeto sujeto);
    public Sujeto buscar(int idSujeto);
    public List<Sujeto> buscarTodo();
    public List<Sujeto> buscarEmpresas();
}
