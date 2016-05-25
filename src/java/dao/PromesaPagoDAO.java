/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.PromesaPago;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface PromesaPagoDAO {
  
  public boolean insertar(PromesaPago promesa);
  
  public List<PromesaPago> buscarPorConvenio(int idConvenio);
  
  public boolean buscarPromesasHoy(int idCredito);
  
  public boolean buscarPromesasAnticipaFecha(int idCredito);
  
  public boolean buscarPromesasPorCumplirse(int idCredito);
  
  public List<PromesaPago> promesasPagoHoy(int idCredito);
  
  public List<PromesaPago> buscarPromesasAnterioresCredito(int idCredito);
  
  public List<PromesaPago> buscarPromesasFuturas(int idCredito);
}
