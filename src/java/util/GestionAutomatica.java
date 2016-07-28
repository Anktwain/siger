/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import dto.Credito;
import dto.EstatusInformativo;
import dto.Gestion;
import dto.Usuario;
import impl.GestionIMPL;
import impl.EstatusInformativoIMPL;

/**
 *
 * @author Eduardo
 */
public class GestionAutomatica {

  // METODO QUE GENERA LA GESTION AUTOMATICA
  public boolean generarGestionAutomatica(String abreviatura, Credito credito, Usuario usuario, String gestion) {
    Gestion g = new GestionIMPL().obtenerGestionAutomaticaPorAbreviatura(abreviatura);
    g.setCredito(credito);
    g.setEstatusInformativo(buscarEstatus(abreviatura));
    g.setUsuario(usuario);
    g.setGestion(gestion);
    return (new GestionIMPL().insertarGestion(g));
  }

  public EstatusInformativo buscarEstatus(String abreviatura) {
    switch (abreviatura) {
      case "4DOMI":
        return new EstatusInformativoIMPL().buscar(12);
      case "9APROB":
      case "14DELC":
        return new EstatusInformativoIMPL().buscar(2);
      case "13CONRE":
      case "15CTARE":
      case "17PAGNO":
      case "25VAPA":
        return new EstatusInformativoIMPL().buscar(9);
      case "16PAGSI":
        return new EstatusInformativoIMPL().buscar(3);
      case "32RUVD":
        return new EstatusInformativoIMPL().buscar(7);
      case "33CEIB":
      default:
        return new EstatusInformativoIMPL().buscar(16);
    }
  }

}
