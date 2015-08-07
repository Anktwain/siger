package beans;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import util.log.Logs;

/**
 * La clase {@code PruebaBean} permite ... y es el bean correspondiente a la
 * vista {@code prueba.xhtml}
 *
 * @author
 * @author
 * @author brionvega
 * @since SigerWeb2.0
 */
@ManagedBean
@ViewScoped
public class eliminar_PruebaBean implements Serializable {

    private String nombre;

    /**
     *
     *
     * @param
     */
    public void mostrar() {
        Logs.log.debug("************ CONSOLA SIGERWEB ****************");
        Logs.log.debug("SE DIO DE ALTA A: " + nombre + " EN EL SISTEMA");
    }

    /**
     *
     *
     *
     */
    public String getNombre() {
        return nombre;
    }

    /**
     *
     *
     * @param
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
