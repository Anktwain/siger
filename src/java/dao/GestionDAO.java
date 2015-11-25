/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Gestion;

/**
 *
 * @author Eduardo
 */
public interface GestionDAO {
  
  public Number calcularVisitasDomiciliariasPorDespacho(int idDespacho);
  public Number calcularVisitasDomiciliariasPorGestor(int idusuario);
  public boolean insertarGestion(Gestion gestion);
}
