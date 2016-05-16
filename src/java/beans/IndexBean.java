package beans;

import java.io.Serializable;
import dto.Usuario;
import impl.UsuarioIMPL;
import dao.UsuarioDAO;
import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import util.constantes.Constantes;
import javax.faces.context.FacesContext;
import util.MD5;
import javax.servlet.http.HttpSession;
import util.constantes.Perfiles;
import util.log.Logs;

/**
 * La clase {@code IndexBean} permite el manejo del inicio de sesion y es el
 * bean correspondiente a la vista {@code index.xhtml}
 *
 * @author
 * @author
 * @author Cofradia
 * @since SigerWeb2.0
 */
@ManagedBean(name = "indexBean")
@SessionScoped
public class IndexBean implements Serializable {

  /**
   * El atributo en el bean correspondiente al campo usuario de la vista
   */
  private String nombreUsuario;
  /**
   * El atributo en el bean correspondiente al campo contraseña de la vista
   */
  private String password;
  private Usuario usuario;
  private final UsuarioDAO usuarioDao;
  private boolean adminVisible;
  private String vista;

  /**
   * Constructor por defecto. <br/>
   * Configura como {@code null} los campos {@code usuario}, {@code usuarioDao}
   * y {@code beanDeSesion}
   */
  public IndexBean() {
    usuario = new Usuario();
    usuarioDao = new UsuarioIMPL();
    adminVisible = false;
  }

  /**
   * Indica si la cadena del atributo {@code nombreUsuario} del bean es válida
   * según los siguientes parámetros:
   * <ul>
   * <li>Si es una cadena no vacía.</li>
   * <li>Si no contiene algun tipo de espacios en blanco.</li>
   * <li>Si contiene como máximo {@code Constantes.longNombreUsuario} caracteres
   * de longitud.</li>
   * </ul>
   *
   * @return {@code true} si la cadena es válida, {@code false} si la cadena es
   * inválida.
   */
  public boolean validarNombreUsuario() {
    return (nombreUsuario != null) && (!nombreUsuario.equals(""))
            && (nombreUsuario.length() <= Constantes.longNombreUsuario) && (!nombreUsuario.matches("[.*\\s*]*"));
  }

  /**
   * Indica si la cadena del atributo {@code password} del bean es válida según
   * los siguientes parámetros:
   * <ul>
   * <li>Si es una cadena no vacía.</li>
   * <li>Si no contiene algun tipo de espacios en blanco.</li>
   * <li>Si contiene como máximo {@code Constantes.longPassword} caracteres de
   * longitud.</li>
   * </ul>
   *
   * @return {@code true} si la cadena es válida, {@code false} si la cadena es
   * inválida.
   */
  public boolean validarPassword() {
    return (password != null) && (!password.equals(""))
            && (password.length() <= Constantes.longPassword) && (!password.matches("[.*\\s*]*"));
  }

  /**
   * Solicita una búsqueda con las cadenas de nombre de usuario y de password y
   * devuelve el objeto {@code Usuario} al que correspondan en su caso, o
   * {@code null} en otro caso.
   *
   */
  public void ingresar() {
    HttpSession sesion = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    nombreUsuario = nombreUsuario.toLowerCase();
    usuario = usuarioDao.buscar(nombreUsuario, MD5.encriptar(password));
    FacesContext instanciaActual = FacesContext.getCurrentInstance();
    if (usuario != null) {
      switch (usuario.getPerfil()) {
        case Perfiles.GESTOR_NO_CONFIRMADO:
          instanciaActual.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Acceso denegado.", "Usuario no encontrado."));
          Logs.log.info("Intento de acceso gestor no confirmado. " + nombreUsuario + ", despacho: " + usuario.getDespacho().getNombreCorto());
          break;
        case Perfiles.ELIMINADO:
          instanciaActual.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Acceso denegado.", usuario.getNombre() + " ha sido eliminado."));
          Logs.log.info("Intento de acceso usuario eliminado. " + nombreUsuario + ", despacho: " + usuario.getDespacho().getNombreCorto());
          break;
        case Perfiles.ADMINISTRADOR:
        case Perfiles.SUPER_ADMINISTRADOR: {
          try {
            instanciaActual.getExternalContext().redirect("panelAdministrativo.xhtml");
          } catch (IOException ioe) {
            Logs.log.error("No se pudo redirigir a la vista panel administrativo.");
            Logs.log.error(ioe);
          }
        }
        Logs.log.info("Usuario administrador " + usuario.getNombreLogin() + ", despacho " + usuario.getDespacho().getNombreCorto() + " inicio la sesion " + sesion.getId());
        vista = "panelAdministrativo.xhtml";
        adminVisible = true;
        //}
        break;
        case Perfiles.GESTOR: {
          try {
            instanciaActual.getExternalContext().redirect("panelGestor.xhtml");
          } catch (IOException ioe) {
            Logs.log.error("No se pudo redirigir a la vista panel gestor.");
            Logs.log.error(ioe);
          }
        }
        Logs.log.info("Usuario gestor " + usuario.getNombreLogin() + ", despacho " + usuario.getDespacho().getNombreCorto() + " inicio la sesion " + sesion.getId());
        vista = "panelGestor.xhtml";
        adminVisible = false;
        //}
        break;
        default:
          instanciaActual.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Acceso denegado.", usuario.getNombre() + ". Usuario no encontrado."));
          Logs.log.warn("Usuario o password incorrectos. " + nombreUsuario + ", despacho: " + usuario.getDespacho().getNombreCorto());
          break;
      }
    } else {
      instanciaActual.addMessage("", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Acceso denegado.", "Verifique los datos y que el administrador haya dado de alta su cuenta."));
      Logs.log.warn("Intento de intrusion al sistema. Login: " + nombreUsuario + ", Pass: " + password);
    }
    nombreUsuario = "";
    password = "";
  }

  // TO DO:
  // CHECAR SI LAS QUINCENAS DEL AÑO HAN SIDO GENERADAS
  // SI NO SE HAN GENERADO. GENERARLAS
  // METODO QUE GESTIONA EL CIERRE DE SESION
  public void cerrarSesion() {
    HttpSession sesion = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    sesion.invalidate();
    nombreUsuario = "";
    password = "";
    try {
      FacesContext.getCurrentInstance().getExternalContext().redirect("faces/index.xhtml");
      Logs.log.info("Usuario " + usuario.getNombreLogin() + ", despacho " + usuario.getDespacho().getNombreCorto() + " cerro la sesion " + sesion.getId());
    } catch (IOException ioe) {
      Logs.log.error("No se pudo redirigir al index.");
      Logs.log.error(ioe);
    }
  }

  // METODO QUE SIRVE PARA VERIFICAR 
  public void verificarAcceso(String modulo) {
    if ((usuario.getPerfil() == Perfiles.SUPER_ADMINISTRADOR) || (usuario.getPerfil() == Perfiles.ADMINISTRADOR) || (usuario.getPerfil() == Perfiles.GESTOR)) {
      verificarAccesoIlegal(modulo);
    } else {
      verificarIntrusion(modulo);
    }
  }

  // METODO QUE REDIRIGE LAS INTRUSIONES AL SISTEMA (INTENTO DE ACCESO SIN ESTAR LOGGEADO)
  // ESTO CON LA FINALIDAD DE EVITAR ACCESOS NO PERMITIDOS QUE PUEDEN VULNERAR LA INTEGRIDAD DE LOS DATOS
  public void verificarIntrusion(String modulo) {
    try {
      FacesContext.getCurrentInstance().getExternalContext().redirect("faces/index.xhtml");
      Logs.log.warn("Se intento entrar al modulo '" + modulo + "' sin haber ingresado al sistema. Ha sido redirigido");
    } catch (IOException ioe) {
      Logs.log.error("Intrusion en el modulo: '" + modulo + "'. El intruso no logro ser redirigido");
      Logs.log.error(ioe);
    }
  }

  // METODO QUE VERIFICA SI SE TRATA DE UN USUARIO ADMINISTRATIVO O DE UN GESTOR
  // ESTO CON LA FINALIDAD DE EVITAR ACCESOS NO PERMITIDOS QUE PUEDEN VULNERAR LA INTEGRIDAD DE LOS DATOS
  public void verificarAccesoIlegal(String modulo) {
    if ((usuario.getPerfil() == Perfiles.SUPER_ADMINISTRADOR) || (usuario.getPerfil() == Perfiles.ADMINISTRADOR)) {
    } else {
      if ((modulo.equals("panel administrativo")) || (modulo.equals("carga")) || (modulo.equals("cuentas")) || (modulo.equals("gestiones")) || (modulo.equals("pagos")) || (modulo.equals("usuarios")) || (modulo.equals("marcaje")) || (modulo.equals("devolucion")) || (modulo.equals("validar direcciones"))) {
        try {
          FacesContext.getCurrentInstance().getExternalContext().redirect("accesoIlegal.xhtml");
          Logs.log.warn("El gestor " + usuario.getNombreLogin() + " del despacho " + usuario.getDespacho().getNombreCorto() + " intento entrar al modulo administrativo: '" + modulo + "'. Ha sido redirigido");
        } catch (IOException ioe) {
          Logs.log.warn("El gestor " + usuario.getNombreLogin() + " del despacho " + usuario.getDespacho().getNombreCorto() + " accedio al modulo administrativo: '" + modulo + "'. Fallo el redireccionamiento");
          Logs.log.warn(ioe);
        }
      }
    }
  }

  // METODO QUE VERIFICA SI EXISTE UNA SESION ANTERIOR
  public boolean verificarSesionAnterior(HttpSession sesion, String tipoUsuario) {
    return true;
  }

  // GETTERS & SETTERS
  /**
   *
   *
   * @return
   */
  public String getNombreUsuario() {
    return nombreUsuario;
  }

  /**
   *
   *
   * @param nomUsuario
   */
  public void setNombreUsuario(String nomUsuario) {
    this.nombreUsuario = nomUsuario;
  }

  /**
   *
   *
   * @return
   */
  public String getPassword() {
    return password;
  }

  /**
   *
   *
   * @param password
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   *
   *
   * @return
   */
  public Usuario getUsuario() {
    return usuario;
  }

  /**
   *
   *
   * @param usuario
   */
  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  public String getVista() {
    return vista;
  }

  public void setVista(String vista) {
    this.vista = vista;
  }

  public boolean isAdminVisible() {
    return adminVisible;
  }

  public void setAdminVisible(boolean adminVisible) {
    this.adminVisible = adminVisible;
  }

}
