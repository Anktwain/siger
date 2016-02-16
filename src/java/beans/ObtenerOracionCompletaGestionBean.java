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
  public String obtenerOracion(Gestion gestion) {
    if (gestion != null) {
      if (gestion.getTipoQuienGestion().getDescripcion().equals("NO APLICA")) {
        return gestion.getTipoGestion().getNombre() + ". " + gestion.getDondeGestion().getNombre() + ", " + gestion.getAsuntoGestion().getTipoAsuntoGestion().getAsunto() + ": " + gestion.getDescripcionGestion().getTextoGestion() + ". " + gestion.getGestion() + ". " + gestion.getEstatusInformativo().getEstatus() + ".";
      } else {
        return gestion.getTipoGestion().getNombre() + ". " + gestion.getDondeGestion().getNombre() + ", " + gestion.getAsuntoGestion().getTipoAsuntoGestion().getAsunto() + ": " + gestion.getDescripcionGestion().getTextoGestion() + " " + gestion.getTipoQuienGestion().getDescripcion() + ": " + gestion.getQuienGestion().getQuien() + ". " + gestion.getGestion() + ". " + gestion.getEstatusInformativo().getEstatus() + ".";
      }
    }
    return "";
  }
}