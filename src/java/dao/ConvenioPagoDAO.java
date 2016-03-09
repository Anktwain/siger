/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.ConvenioPago;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface ConvenioPagoDAO {
  
  public boolean insertar(ConvenioPago convenio);
  public boolean editar(ConvenioPago convenio);
  public ConvenioPago buscarConvenioEnCursoCredito(int idCredito);
  public List<ConvenioPago> buscarConveniosFinalizadosCredito(int idCredito);
  public Number calcularSaldoPendiente(int idConvenio);
}
