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
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
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
   * <li>Si es una cadena no vacía.
   * <li>Si no contiene algun tipo de espacios en blanco.
   * <li>Si contiene como máximo {@code Constantes.longNombreUsuario} caracteres
   * de longitud.
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
   * <li>Si es una cadena no vacía.
   * <li>Si no contiene algun tipo de espacios en blanco.
   * <li>Si contiene como máximo {@code Constantes.longPassword} caracteres de
   * longitud.
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
   * @throws java.io.IOException
   */
  public void ingresar() throws IOException {
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
        case Perfiles.SUPER_ADMINISTRADOR:
          instanciaActual.getExternalContext().redirect("panelAdministrativo.xhtml");
          Logs.log.info("Acceso administrador " + nombreUsuario + ", despacho " + usuario.getDespacho().getNombreCorto() + ", sesion " + sesion.getId());
          vista = "panelAdministrativo.xhtml";
          adminVisible = true;
          break;
        case Perfiles.GESTOR:
          instanciaActual.getExternalContext().redirect("panelGestor.xhtml");
          Logs.log.info("Acceso gestor " + nombreUsuario + ", despacho: " + usuario.getDespacho().getNombreCorto() + ", sesion " + sesion.getId());
          vista = "panelGestor.xhtml";
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
      FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
      Logs.log.info("Se intento entrar al modulo " + modulo + " sin haber ingresado al sistema. Ha sido redirigido");
    } catch (IOException ex) {
      Logs.log.info("Intrusion en el modulo " + modulo + ". El intruso no logro ser redirigido");
      ex.printStackTrace();
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
          Logs.log.info("El gestor " + usuario.getNombreLogin() + " del despacho " + usuario.getDespacho().getNombreCorto() + " intento entrar al modulo administrativo " + modulo + ". Ha sido redirigido");
        } catch (IOException ex) {
          Logs.log.warn("El gestor " + usuario.getNombreLogin() + " del despacho " + usuario.getDespacho().getNombreCorto() + " accedio al modulo administrativo " + modulo + ". Fallo el redireccionamiento");
          ex.printStackTrace();
        }
      }
    }
  }

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
