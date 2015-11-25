package beans;

import dao.MunicipioDAO;
import impl.MunicipioIMPL;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Pablo
 */
@FacesConverter("mpioConverter")
public class MunicipioConverter implements Converter {

  @Override
  public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
    ZonasVistaBean zonaVista = (ZonasVistaBean) fc.getExternalContext().getApplicationMap().get("zonasVistaBean");

    if (string != null) {
      if (string.trim().length() > 0) {
        System.out.println("\n_______________Por devolver el Municipio con el nombre " + string);
        return zonaVista.getMpioPorNombre(string);
      }
    }
    return null;
  }

  @Override
  public String getAsString(FacesContext fc, UIComponent uic, Object o) {
    if (o != null) {
      if(o.toString()!=null){
        System.out.println("\n_______________Por devolver la cadena " + o.toString());
        return o.toString();
      }
    }
    return null;
  }

}
