/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Credito;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface CreditoDAO {
  public Number contarCreditosActivos();
  public Number contarCreditosActivosPorGestor(int idUsuario);
  public Credito buscarCreditoPorId(int idCredito);
}
