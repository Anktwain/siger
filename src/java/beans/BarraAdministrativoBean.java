package beans;

import dao.DevolucionDAO;
import java.io.Serializable;
import dto.Usuario;
import impl.UsuarioIMPL;
import dao.UsuarioDAO;
import dto.Devolucion;
import impl.DevolucionIMPL;
import java.util.List;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

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

  // LLAMADA A OTROS BEANS
  ELContext elContext = FacesContext.getCurrentInstance().getELContext();
  IndexBean indexBean = (IndexBean) elContext.getELResolver().getValue(elContext, null, "indexBean");

  // VARIABLES DE CLASE
  private final UsuarioDAO usuarioDao;
  private final DevolucionDAO devolucionDao;

  /**
   *
   */
  public BarraAdministrativoBean() {
    usuarioDao = new UsuarioIMPL();
    devolucionDao = new DevolucionIMPL();
  }

  /**
   *
   *
   * @return
   */
  // CREO QUE ESTOY LISTO PARA CERTIFICARME COMO ARQUITECTO JAVA 05-03-2015
  // METODO QUE CUENTA LOS GESTORES QUE NO SE HAN CONFIRMADO
  public String contarGestoresNoConfirmados() {
    List<Usuario> sinConfirmar = usuarioDao.buscarUsuariosNoConfirmados(indexBean.getUsuario().getDespacho().getIdDespacho());
    if (!sinConfirmar.isEmpty()) {
      return String.valueOf(sinConfirmar.size());
    } else {
      return "0";
    }
  }

  //METODO QUE CUENTA LOS CREDITOS DEVUELTOS QUE NO HAN SIDO REVISADOS
  public String contarDevolucionesPendientes() {
    List<Devolucion> pendientes = devolucionDao.bandejaDevolucionPorDespacho(indexBean.getUsuario().getDespacho().getIdDespacho());
    if (!pendientes.isEmpty()) {
      return String.valueOf(pendientes.size());
    } else {
      return "0";
    }
  }

  // METODO QUE CUENTA LAS DIRECCIONES POR VALIDAR
  public String contarDireccionesPendientes() {
    return "+1";
  }

}
