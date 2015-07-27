/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Sujetos;
import java.util.List;

/**
 *
 * @author brionvega
 */
public interface SujetosDAO {
    public int insertar(Sujetos sujeto);
    public boolean editar(Sujetos sujeto);
    public boolean eliminar(Sujetos sujeto);
    public Sujetos buscar(int idSujeto);
    public List<Sujetos> buscarTodo();
    public List<Sujetos> buscarEmpresas();
}
