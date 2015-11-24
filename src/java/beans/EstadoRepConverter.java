package beans;

import dto.EstadoRepublica;
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
    ZonasVistaBean zonaVista = (ZonasVistaBean) fc.getExternalContext().getApplicationMap().get("zonasVistaBean");
    Object estado;
    
    if (string != null && string.trim().length() > 0) {
      System.out.println("_________________________________________edoRepConverter.getAsObject { string = " + string + "}");
      if (zonaVista != null) {
        System.out.println("Enviando el objeto completo.");
        estado = zonaVista.getEdoRepPorNombre(string);
      }else{
        System.out.println("Enviando solo el String :(   .");
        estado = string;
      }
      return estado;
    } else {
      System.out.println("_________________________________________edoRepConverter.getAsObject { string = null }");
      return null;
    }
  }

  @Override
  public String getAsString(FacesContext fc, UIComponent uic, Object o) {

    if (o == null) {
      System.out.println("_________________________________________edoRepConverter.getAsString { o = null }");
      return null; // linea temporal. SOLO PARA CUMPLIR CON EL VALOR DE RETORNO !!!!!!!!!!
    } else {
      System.out.println("_________________________________________edoRepConverter.getAsString { o = " + ((EstadoRepublica) o)
              + ", o.getClass() = " + o.getClass() + "}");
      return o.toString(); // linea temporal. SOLO PARA CUMPLIR CON EL VALOR DE RETORNO !!!!!!!!!!
    }
  }
}
