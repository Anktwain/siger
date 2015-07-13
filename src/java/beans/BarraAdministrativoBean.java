package beans;

import java.io.Serializable;
import dto.Usuarios;
import impl.UsuariosIMPL;
import dao.UsuariosDAO;
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
    private UsuariosDAO usuarioDao;
    private List<Usuarios> sinConfirmar;
    public BarraAdministrativoBean() {
        usuarioDao = new UsuariosIMPL();
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
