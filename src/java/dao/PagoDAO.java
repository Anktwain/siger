/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Pago;
import java.util.Date;
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

  public List<Pago> pagosPorDespacho(int idDespacho, String fechaInicio, String fechaFin);

  public List<Pago> pagosPorRevisarPorDespacho(int idDespacho);
  
  public Pago buscarPagoHoy(int idCredito);
  
  public String obtenerGestorDelDia(int idDespacho);
  
  public Number calcularPagosPendientes(int idDespacho);
  
  public Number calcularSaldoAprobadoHoy(int idDespacho);
  
  public double calcularRecuperacionDespacho(int idDespacho);
  
  public List<Pago> pagosPorRevisarBanco(int idDespacho);
  
  public float calcularMontoGestor(int idGestor);
  
  public Pago buscarPagoFechaCredito(Date fecha, int idCredito);
  
  public List<Pago> buscarTodosPagosGestor(int idUsuario);
          
}
