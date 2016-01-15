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
@FacesConverter("edoRepConverter")
public class EstadoRepConverter implements Converter {

  @Override
  public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
    ZonaService zonaService = (ZonaService) fc.getExternalContext().getApplicationMap().get("zonaService");
    Object devuelto = null;

    if (string != null) {
      if (string.trim().length() > 0) {
        devuelto = zonaService.getEdoRep(string);
        System.out.println("________________edoRepConverter.getAsObject { string = "
                + string + "}."
                + " Enviando el " + devuelto.getClass() + " " + devuelto.toString()); // Línea de prueba
      }
    } else {
      System.out.println("________________edoRepConverter.getAsObject { <string> = null, o bien, <string.trim().length()> = 0 } -> Enviando null"); // Línea de prueba
    }
    return devuelto;
  }

  @Override
  public String getAsString(FacesContext fc, UIComponent uic, Object o) {
    String devuelta = null;
    if (o != null) {
      if (o.toString() != null) {
        devuelta = o.toString();
        System.out.println("________________edoRepConverter.getAsString { Enviando "
                + devuelta + ", o.getClass() = " + o.getClass() + "}");      // Línea de prueba
      }
    } else {
      System.out.println("________________edoRepConverter.getAsString { <o> = null, o bien, <o.toString()> = null } -> Enviando null.");    // Línea de prueba
    }
    return devuelta;
  }
}
