package beans.convertidores;

import beans.ZonaService;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Pablo
 */
@FacesConverter("gestorConverter")
public class GestorConverter implements Converter {

//  @Override
//  public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
//    ZonaService zonaService = (ZonaService) fc.getExternalContext().getApplicationMap().get("zonaService");
//
//    if ((string != null) && (!string.trim().equals(""))) {
//
//      System.out.println("________________________________gestorConverter.getAsObject() { string = "
//              + string + "}."
//              + " Intentando enviar el Gestor que coincida con este nombre."); // Línea de prueba
//
//      return zonaService.getGestorPorNombre(string);
//    }
//
//    return string;
//  }
  @Override
  public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
    ZonaService zonaService = (ZonaService) fc.getExternalContext().getApplicationMap().get("zonaService");

    Object devuelto = null;

    if ((string != null) && (!string.trim().equals(""))) {
      devuelto = zonaService.getGestor(Integer.valueOf(string));

      System.out.println("______________ gestorConverter.getAsObject() { string = "
              + string + "}."
              + " Enviando el Gestor que coincidio con este criterio: " + devuelto); // Línea de prueba
    }
    return devuelto;
  }

  @Override
  public String getAsString(FacesContext fc, UIComponent uic, Object o) {
    try {
      System.out.println("______________ gestorConverter.getAsString() { <o> es un <" + o.getClass() + "> y vale <" + o.toString() + ">.");
      return o.toString();
    } catch (NullPointerException npo) {
      System.out.println("Puntero nulo en gestorConverter.getAsString() porque el objeto <o> es nulo o su metodo toString() devuelve nulo.");
      return null;
    }
  }

}
