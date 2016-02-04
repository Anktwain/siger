package beans;

import java.io.Serializable;
import dto.Usuario;
import impl.UsuarioIMPL;
import dao.UsuarioDAO;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * La clase {@code BarraAdministrativoBean} permite ... y es el bean
 * correspondiente a la vista {@code barraAdministrativo.xhtml}
 *
 * @author
 * @author
 * @author Eduardo
 * @since SigerWeb2.0
 */
@ManagedBean(name = "barraAdministrativoBean")
@SessionScoped

public class BarraAdministrativoBean implements Serializable {

  private final UsuarioDAO usuarioDao;
  private List<Usuario> sinConfirmar;

  /**
   *
   */
  public BarraAdministrativoBean() {
    usuarioDao = new UsuarioIMPL();
  }

  /**
   *
   *
   * @return
   */
  // CREO QUE ESTOY LISTO PARA CERTIFICARME COMO ARQUITECTO JAVA
  public String contar() {
    sinConfirmar = usuarioDao.buscarUsuariosNoConfirmados();
    int cuenta = sinConfirmar.size();
    String total = Integer.toString(cuenta);
    return total;
  }

  /**
   *
   *
   * @param sinConfirmar
   */
  public void setSinConfirmar(List sinConfirmar) {
    this.sinConfirmar = sinConfirmar;
  }

  /**
   *
   *
   * @return
   */
  public List getSinConfirmar() {
    return sinConfirmar;
  }
}
