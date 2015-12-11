/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.EstatusInformativo;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface EstatusInformativoDAO {
  public List<EstatusInformativo> buscarTodos();
  public boolean insertar(EstatusInformativo estatus);
  public EstatusInformativo buscar(int idEstatus);
}
