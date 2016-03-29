/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Gestion;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public interface GestionDAO {

  public Number calcularVisitasDomiciliariasPorDespacho(int idDespacho);
  public Number calcularVisitasDomiciliariasPorGestor(int idusuario);
  public boolean insertarGestion(Gestion gestion);
  public List<Gestion> busquedaReporteGestiones (String consulta);
  public List<Gestion> buscarGestionesCreditoGestor(int idUsuario, int idCredito);
  public List<Gestion> buscarGestionesCredito(int idCredito);  
  public Gestion obtenerGestionAutomaticaPorAbreviatura(String abreviatura);
  public boolean buscarGestionHoy(int idCredito);
  public Number calcularGestionesHoyPorDespacho(int idDespacho);
  public Number calcularGestionesHoyPorGestor(int idUsuario);
  public String obtenerGestorDelDia(int idDespacho);
}
