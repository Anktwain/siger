package beans.convertidores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Pablo
 */
@FacesConverter("gestorConverter")
public class GestorConverter implements Converter{

  @Override
  public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
    return string;
  }

  @Override
  public String getAsString(FacesContext fc, UIComponent uic, Object o) {
    try{
    return o.toString();
    }catch(NullPointerException npo){
      System.out.println("Puntero nulo en gestorConverter.getAsString porque el objeto o no tiene un string que devolver.");
    }
    return null;
  }
  
}
