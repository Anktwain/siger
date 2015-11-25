package beans;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Pablo
 */

@FacesConverter("mpioConverter")
public class MunicipioConverter  implements Converter{

  @Override
  public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public String getAsString(FacesContext fc, UIComponent uic, Object o) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
}
