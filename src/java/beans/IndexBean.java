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
import java.util.Calendar;
import java.util.Date;
import org.primefaces.context.RequestContext;
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
  /**
   * Información de la sesión
   */
  private Date horaInicio;
  /**
   * Información de la sesión
   */
  private Date horaFin;
  /**
   * Información de la sesión
   */
  private Usuario usuarioActivo;
  /**
   * Información de la sesión
   */
  private boolean sesionActiva;

  private String vista;

  /**
   * Constructor por defecto. <br/>
   * Configura como {@code null} los campos {@code usuario}, {@code usuarioDao}
   * y {@code beanDeSesion}
   */
  public IndexBean() {
    usuario = new Usuario();
    usuarioDao = new UsuarioIMPL();
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
    cerrarSesion();
    Logs.log.info("########################### Estamos en la la funcion ingresar ###########################\n");
    nombreUsuario = nombreUsuario.toLowerCase();
    usuario = usuarioDao.buscar(nombreUsuario, MD5.encriptar(password));
    Calendar cal = Calendar.getInstance();
    FacesContext instanciaActual = FacesContext.getCurrentInstance();
    if (usuario != null) {
      switch (usuario.getPerfil()) {
        case Perfiles.GESTOR_NO_CONFIRMADO:
          instanciaActual.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Acceso denegado.",
                  usuario.getNombre() + " ha ingresado con el perfil "
                  + usuario.getPerfil() + " (GESTOR_NO_CONFIRMADO) correctamente."));
          Logs.log.info("#################### NOT OK. ACCESO DENEGADO(Gestor no confirmado).");
          sesionActiva = false;
          break;
        case Perfiles.ELIMINADO:
          instanciaActual.addMessage("",
                  new FacesMessage(FacesMessage.SEVERITY_INFO, "Acceso denegado.",
                          usuario.getNombre() + "No podrá ingresar con el perfil " + usuario.getPerfil() + " (ELIMINADO) porque ha sido desactivado."));
          Logs.log.info("#################### NOT OK. ACCESO DENEGADO(Gestor eliminado).");
          sesionActiva = false;
          break;
        case Perfiles.ADMINISTRADOR:
        case Perfiles.SUPER_ADMINISTRADOR:
//                    FacesContext mensaje = FacesContext.getCurrentInstance();
//                    mensaje.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Acceso permitido.", usuario.getNombre() + " ha ingresado con el perfil " + usuario.getPerfil() + " (ADMINISTRADOR) correctamente."));
//                    FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Acceso permitido.", usuario.getNombre() + " ha ingresado con el perfil " + usuario.getPerfil() + " (ADMINISTRADOR) correctamente."));
          instanciaActual.getExternalContext().redirect("faces/panelAdministrativo.xhtml");
          Logs.log.info("#################### OK. ACCESO ADMIN CORRECTO.");
          horaInicio = cal.getTime();
          horaFin = cal.getTime();
          sesionActiva = true;
          usuarioActivo = usuario;
          vista = "panelAdministrativo.xhtml";
          break;
        case Perfiles.GESTOR:
          instanciaActual.addMessage("",
                  new FacesMessage(FacesMessage.SEVERITY_INFO, "Acceso permitido.",
                          usuario.getNombre() + " ha ingresado con el perfil " + usuario.getPerfil() + " (GESTOR) correctamente."));
          instanciaActual.getExternalContext().redirect("faces/panelGestor.xhtml");
          Logs.log.info("#################### OK. ACCESO GESTOR CORRECTO.");
          horaInicio = cal.getTime();
          horaFin = cal.getTime();
          sesionActiva = true;
          usuarioActivo = usuario;
          vista = "panelGestor.xhtml";
          break;
        default:
          instanciaActual.addMessage("",
                  new FacesMessage(FacesMessage.SEVERITY_FATAL, "Acceso denegado.",
                          usuario.getNombre() + "Está intentando entrar con un perfil desconocido. (Perfil =" + usuario.getPerfil() + ")."));

          Logs.log.info("#################### NOT OK. INTENTANDO ENTRAR CON UN PERFIL DESCONOCIDO.");
          sesionActiva = false;
          break;
      }

    } else {
      instanciaActual.addMessage("",
              new FacesMessage(FacesMessage.SEVERITY_FATAL, "Acceso denegado.",
                      "Verifica que los datos que has introducido son correctos y que el administrador haya dado de alta tu cuenta."));
      Logs.log.info("#################### NOT OK. USUARIO NO CONFIRMADO O NOMBRE DE USUARIO O CONTRASEÑA INCORRECTOS!");
      sesionActiva = false;
    }
  }

  public void cerrarSesion() {
    sesionActiva = false;
    usuarioActivo = null;
    vista = "";
    RequestContext.getCurrentInstance().update("index.xhtml");
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

}
