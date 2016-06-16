/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dto.Gestion;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "obtenerOracionCompletaGestionBean")
@ViewScoped
public class ObtenerOracionCompletaGestionBean implements Serializable {

  // CONSTRUCTOR
  public ObtenerOracionCompletaGestionBean() {
  }

  // METODO QUE CREA UNA ORACION A PARTIR DE LOS OBJETOS RELACIONADOS CON LA GESTION RECIBIDOS
  /*
   public String obtenerOracion(Gestion gestion) {
   if (gestion != null) {
   String completa = "";
   switch (gestion.getTipoGestion().getNombre()) {
   case "VISITA DOMICILIARIA":
   completa = gestion.getTipoGestion().getNombre() + ", " + gestion.getGestion() + ". " + gestion.getEstatusInformativo().getEstatus() + ".";
   break;
   case "TELEFONIA":
   completa = gestion.getTipoGestion().getAbreviatura() + ", " + gestion.getGestion() + ". " + gestion.getEstatusInformativo().getEstatus() + ".";
   break;
   case "LLAMADA ENTRANTE":
   completa = gestion.getTipoGestion().getNombre() + ", " + gestion.getGestion() + ". " + gestion.getEstatusInformativo().getEstatus() + ".";
   break;
   case "CORPORATIVO":
   completa = gestion.getGestion() + ". " + gestion.getEstatusInformativo().getEstatus() + ".";
   break;
   case "AUTOMATICA":
   completa = gestion.getGestion() + " " + gestion.getEstatusInformativo().getEstatus() + ".";
   break;
   }
   return completa;
   } else {
   return "";
   }
   }
   */
  // METODO QUE CREA UNA ORACION CON CADA GESTION REALIZADA SIGUIENTO EL FORMATO PERMITIDO POR EL BANCO
  public String obtenerOracion(Gestion gestion) {
    if (gestion != null) {
      String completa;
      if (gestion.getTipoQuienGestion().getIdTipoQuienGestion() == 12) {
        if (gestion.getTipoGestion().getNombre().equals("AUTOMATICA")) {
          completa = gestion.getDondeGestion().getNombre() + ". " + gestion.getDescripcionGestion().getTextoGestion() + ". " + gestion.getEstatusInformativo().getEstatus();
        } else {
          completa = gestion.getTipoGestion().getNombre() + ": " + gestion.getDondeGestion().getNombre() + ". " + gestion.getAsuntoGestion().getTipoAsuntoGestion().getAsunto() + ", " + gestion.getDescripcionGestion().getTextoGestion() + ". " + gestion.getEstatusInformativo().getEstatus();
        }
      } else {
        if (gestion.getTipoGestion().getNombre().equals("AUTOMATICA")) {
          completa = gestion.getDondeGestion().getNombre() + ". " + gestion.getDescripcionGestion().getTextoGestion() + " " + gestion.getQuienGestion().getQuien() + ". " + gestion.getEstatusInformativo().getEstatus();
        } else {
          completa = gestion.getTipoGestion().getNombre() + ": " + gestion.getDondeGestion().getNombre() + ". " + gestion.getAsuntoGestion().getTipoAsuntoGestion().getAsunto() + ", " + gestion.getDescripcionGestion().getTextoGestion() + " " + gestion.getQuienGestion().getQuien() + ". " + gestion.getEstatusInformativo().getEstatus();
        }
      }
      return completa;
    } else {
      return "";
    }
  }

}
