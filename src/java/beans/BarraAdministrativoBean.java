package beans;

import java.io.Serializable;
import dto.Usuario;
import impl.UsuarioIMPL;
import dao.UsuarioDAO;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Eduardo
 */

@ManagedBean(name = "barraAdministrativoBean")
@SessionScoped

public class BarraAdministrativoBean implements Serializable{
    private UsuarioDAO usuarioDao;
    private List<Usuario> sinConfirmar;
    public BarraAdministrativoBean() {
        usuarioDao = new UsuarioIMPL();
    }
    // CREO QUE ESTOY LISTO PARA CERTIFICARME COMO ARQUITECTO JAVA
    public String contar()
    {
        sinConfirmar = usuarioDao.buscarUsuariosNoConfirmados();
        int cuenta = sinConfirmar.size();
        String total = Integer.toString(cuenta);
        System.out.println("************ CONSOLA SIGERWEB ****************");
        System.out.println("Existen " + total + " gestores no confirmados en el sistema");
        return total;
    }
    public void setSinConfirmar(List sinConfirmar){
        this.sinConfirmar = sinConfirmar;
    }
    public List getSinConfirmar(){
        return sinConfirmar;
    }
}
