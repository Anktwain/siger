/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.TipoAsuntoGestion;

/**
 *
 * @author Eduardo
 */
public interface TipoAsuntoGestionDAO {
  public TipoAsuntoGestion buscarPorAsunto(int idTipoAsunto);
}
