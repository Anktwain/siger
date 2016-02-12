/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Gestion;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface GestionDAO {

  public Number calcularVisitasDomiciliariasPorDespacho(int idDespacho);
  public Number calcularVisitasDomiciliariasPorGestor(int idusuario);
  public boolean insertarGestion(Gestion gestion);
  public List<Gestion> buscarGestionesCredito(int idCredito);
  public List<Gestion> buscarGestionesCreditoGestor(int idUsuario, int idCredito);
  public List<Gestion> buscarGestionesPorDespacho(int idDespacho, Date fechaInicio, Date fechaFin);
  public List<Gestion> buscarGestionesPorGestor(int idDespacho, int idGestor, Date fechaInicio, Date fechaFin);
  public List<Gestion> buscarGestionesPorTipo(int idDespacho, String tipoGestion, Date fechaInicio, Date fechaFin);
  public List<Gestion> buscarGestionesPorInstitucion(int idDespacho, int idInstitucion, Date fechaInicio, Date fechaFin);
  public List<Gestion> buscarGestionesPorInstitucionProducto(int idDespacho, int idInstitucion, int idProducto, Date fechaInicio, Date fechaFin);
  public List<Gestion> buscarGestionesPorGestorTipo(int idDespacho, int idGestor, String tipoGestion, Date fechaInicio, Date fechaFin);
  public List<Gestion> buscarGestionesPorGestorInstitucion(int idDespacho, int idGestor, int idInstitucion, Date fechaInicio, Date fechaFin);
  public List<Gestion> buscarGestionesPorGestorInstitucionProducto(int idDespacho, int idGestor, int idInstitucion, int idProducto, Date fechaInicio, Date fechaFin);
  public List<Gestion> buscarGestionesPorTipoInstitucion(int idDespacho, String tipoGestion, int idInstitucion, Date fechaInicio, Date fechaFin);
  public List<Gestion> buscarGestionesPorTipoInstitucionProducto(int idDespacho, String tipoGestion, int idInstitucion, int idProducto, Date fechaInicio, Date fechaFin);
  public List<Gestion> buscarGestionesPorGestorTipoInstitucionProducto(int idDespacho, int idGestor, String tipoGestion, int idInstitucion, int idProducto, Date fechaInicio, Date fechaFin);
}
