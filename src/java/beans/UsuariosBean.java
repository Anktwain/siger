package beans;
import dto.Usuarios;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Pablo
 */
@ManagedBean
@SessionScoped
public class UsuariosBean implements Serializable{
    public UsuariosBean(){
    }
    
    private SesionBean sesion;
    private List<Usuarios> gestoresNoConfirmados;
    private List<Usuarios> usuariosEncontrados;

    public SesionBean getSesion() {
        return sesion;
    }

    public void setSesion(SesionBean sesion) {
        this.sesion = sesion;
    }

    public List<Usuarios> getGestoresNoConfirmados() {
        return gestoresNoConfirmados;
    }

    public void setGestoresNoConfirmados(List<Usuarios> gestoresNoConfirmados) {
        this.gestoresNoConfirmados = gestoresNoConfirmados;
    }

    public List<Usuarios> getUsuariosEncontrados() {
        return usuariosEncontrados;
    }

    public void setUsuariosEncontrados(List<Usuarios> usuariosEncontrados) {
        this.usuariosEncontrados = usuariosEncontrados;
    }
}
