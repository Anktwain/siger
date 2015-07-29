/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Empresa;
import dto.Sujeto;
import java.util.List;

/**
 *
 * @author brionvega
 */
public interface EmpresaDAO {
    public boolean insertar(Empresa empresa);
    public boolean editar(Empresa empresa);
    public boolean eliminar(Empresa empresa);
    public Empresa buscar(int idEmpresa);
    public Empresa buscarEmpresaPorSujeto(int idEmpresa);
    public List<Empresa> buscarTodo();    
}
