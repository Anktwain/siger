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

  public Pago insertar(Pago pago);

  public boolean editar(Pago pago);
  
  public boolean eliminar(Pago pago);

  public List<Pago> buscarPagosPorConvenioActivo(int idConvenio);

  public List<Pago> buscarPagosPorCredito(int idCredito);

  public List<Pago> pagosPorDespacho(int idDespacho, String fechaInicio, String fechaFin);

  public List<Pago> pagosPorRevisarPorDespacho(int idDespacho);
  
  public Pago buscarPagoHoy(int idCredito);
  
  public String obtenerGestorDelDia(int idDespacho);
  
  public Number calcularPagosPendientes(int idDespacho);
  
  public Number calcularMontoPagosHoy(int idDespacho);
  
  public double calcularRecuperacionDespacho(int idDespacho);
  
  public List<Pago> pagosPorRevisarBanco(int idDespacho);
  
  public float calcularMontoGestor(int idGestor);
  
  public Pago buscarPagoFechaCredito(Date fecha, int idCredito);
  
  public List<Pago> buscarTodosPagosGestor(int idUsuario);
  
  public List<Pago> buscarPagosQuincenActual();
  
  public List<Pago> buscarPagosQuincenaActualGestor(int idGestor);
  
  public float calcularMontoAprobadoGestorHoy(int idUsuario);
  
  public float calcularSaldoPendienteGestor(int idUsuario);

  public double calcularRecuperacionGestor(int idUsuario);
  
  public List<Pago> pagosPorGestor(int idGestor, String fechaInicio, String fechaFin);
  
  public float calcularSaldoPendienteDespacho(int idDespacho);
  
  public float calcularRecuperacionQuincenaDespacho(int idDespacho);
  
  public List<Pago> buscarPagosPorConvenio(int idConvenio);
  
}
