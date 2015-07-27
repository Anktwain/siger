package beans;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import dto.Usuario;

/**
 * La clase {@code SesionBean} permite ... y es el bean correspondiente a la
 * vista {@code sesion.xhtml}
 *
 * @author
 * @author
 * @author Pablo
 * @since SigerWeb2.0
 */
@ManagedBean
@SessionScoped
public class SesionBean implements Serializable {

    private Date horaInicio;
    private Date horaFin;
    private Usuario usuarioActivo;
    private boolean sesionActiva;

    /**
     *
     *
     * @return
     */
    public Date getHoraFin() {
        return horaFin;
    }

    /**
     *
     *
     * @param
     */
    public void setHoraFin(Date horaFin) {
        this.horaFin = horaFin;
    }

    /**
     *
     *
     * @return
     */
    public boolean isSesionActiva() {
        return sesionActiva;
    }

    /**
     *
     *
     * @param
     */
    public void setSesionActiva(boolean sesionActiva) {
        this.sesionActiva = sesionActiva;
    }

    /**
     *
     *
     * @return
     */
    public Date getHoraInicio() {
        return horaInicio;
    }

    /**
     *
     *
     * @param
     */
    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    /**
     *
     *
     * @return
     */
    public Usuario getUsuarioActivo() {
        return usuarioActivo;
    }

    /**
     *
     *
     * @param
     */
    public void setUsuarioActivo(Usuario usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
    }
}
