/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.DescripcionGestion;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface DescripcionGestionDAO {
  public List<DescripcionGestion> buscarPorAsuntoTipo(int idTipoGestion, int idAsunto);
  public DescripcionGestion buscarPorId(int idDescripcionGestion);
}
