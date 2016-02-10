/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.DondeGestion;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface DondeGestionDAO {
  public List<DondeGestion> buscarTodo();
  public List<DondeGestion> buscarPorTipoGestion(int idTipoGestion);
  public DondeGestion buscarPorId(int idDondeGestion);
}
