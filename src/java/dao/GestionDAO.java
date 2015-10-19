/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

/**
 *
 * @author Eduardo
 */
public interface GestionDAO {
  
  public Number calcularVisitasDomiciliarias();
  public Number calcularVisitasDomiciliariasPorGestor(int idGestor);
}
