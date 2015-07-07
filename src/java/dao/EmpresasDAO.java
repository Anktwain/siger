/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Empresas;
import java.util.List;

/**
 *
 * @author brionvega
 */
public interface EmpresasDAO {
    public boolean insertar(Empresas empresa);
    public boolean editar(Empresas empresa);
    public boolean eliminar(Empresas empresa);
    public Empresas buscar(int idEmpresa);
    public List<Empresas> buscarTodo();    
}
