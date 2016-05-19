/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.ComprobantePago;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface ComprobantePagoDAO {
  
  public boolean insertar(ComprobantePago comprobante);
  
  public List<ComprobantePago> buscarPorPago(int idPago);
  
}
