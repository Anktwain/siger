/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Pago;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface PagoDAO {

  public boolean insertar(Pago pago);

  public boolean editar(Pago pago);

  public List<Pago> buscarPagosPorConvenioActivo(int idConvenio);

  public List<Pago> buscarPagosPorCredito(int idCredito);

  public List<Pago> pagosPorDespacho(int idDespacho);

  public List<Pago> pagosPorRevisarPorDespacho(int idDespacho);
  
  public Pago buscarPagoHoy(int idCredito);
  
  public String obtenerGestorDelDia(int idDespacho);
}
