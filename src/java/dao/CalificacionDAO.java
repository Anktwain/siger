/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Calificacion;

/**
 *
 * @author Eduardo
 */
public interface CalificacionDAO {
  public Calificacion buscarPorId(int idCalificacion);
}
