/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Fac;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface FacDAO {
  
  public boolean insertar(Fac fac);
  
  public List<Fac> buscarPorCredito(int idCredito);
  
}
