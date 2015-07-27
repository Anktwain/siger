package beans;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

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
public class PruebaBean implements Serializable {

    private String nombre;

    /**
     *
     *
     * @param
     */
    public void mostrar() {
        System.out.println("************ CONSOLA SIGERWEB ****************");
        System.out.println("SE DIO DE ALTA A: " + nombre + " EN EL SISTEMA");
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
