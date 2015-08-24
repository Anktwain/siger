/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Colonia;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface ColoniaDAO {
  public List<Colonia> buscarColoniasPorMunicipio(int idMunicipio);
  public Colonia buscar(int idColonia);
}