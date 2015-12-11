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

    if (string != null){
      if(string.trim().length() > 0) {
      System.out.println("________________________________edoRepConverter.getAsObject { string = " 
              + string + "}."
              + " Intentando enviar el EstadoRepublica que coincida con este nombre."); // Línea de prueba

      return zonaService.getEdoRepPorNombre(string);
      }else{
        return null;
      }
    } else {
      System.out.println("________________________________edoRepConverter.getAsObject { string = null }"); // Línea de prueba
      return null;
    }
  }

  @Override
  public String getAsString(FacesContext fc, UIComponent uic, Object o) {
    if (o != null) {
      if (o.toString() != null) {
        System.out.println("_______________________________edoRepConverter.getAsString { o = " 
                + o + ", o.getClass() = " + o.getClass() + "}");      // Línea de prueba
      }
      return o.toString();
    } else {
      System.out.println("_________________________________edoRepConverter.getAsString { o = null }");    // Línea de prueba
      return null;
    }
  }
}
