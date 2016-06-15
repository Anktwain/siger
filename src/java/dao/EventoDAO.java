/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Evento;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface EventoDAO {
  
  public boolean insertar(Evento evento);
  
  public List<Evento> buscarEventosVigentes(int idUsuario);
  
}
