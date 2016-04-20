package beans;

import java.io.Serializable;
import dto.Usuario;
import impl.UsuarioIMPL;
import dao.UsuarioDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
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
@ApplicationScoped
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
  private final List<Sesion> usuariosEnLinea;

  /**
   * Constructor por defecto. <br/>
   * Configura como {@code null} los campos {@code usuario}, {@code usuarioDao}
   * y {@code beanDeSesion}
   */
  public IndexBean() {
    usuario = new Usuario();
    usuarioDao = new UsuarioIMPL();
    adminVisible = false;
    usuariosEnLinea = new ArrayList();
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
          if (verificarSesionAnterior(sesion, "administrador")) {
            instanciaActual.addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Acceso denegado.", "Usted ya tiene una sesion activa en el sistema. Cierre las sesiones anteriores."));
          } else {
            instanciaActual.getExternalContext().redirect("panelAdministrativo.xhtml");
            vista = "panelAdministrativo.xhtml";
            adminVisible = true;
          }
          break;
        case Perfiles.GESTOR:
          if (verificarSesionAnterior(sesion, "gestor")) {
            instanciaActual.addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Acceso denegado.", "Usted ya tiene una sesion activa en el sistema. Cierre las sesiones anteriores."));
          } else {
            instanciaActual.getExternalContext().redirect("panelGestor.xhtml");
            vista = "panelGestor.xhtml";
            adminVisible = false;
          }
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
    try {
      HttpSession sesion = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
      sesion.invalidate();
      nombreUsuario = "";
      password = "";
      Sesion s = new Sesion();
      s.setIdSesion(sesion.getId());
      s.setUsuarioSesion(usuario);
      if (!eliminarSesion(s)) {
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error.", "Su sesion no finalizo correctamente. Contacte al equipo de sistemas"));
      }
      FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
      Logs.log.info("Usuario " + usuario.getNombreLogin() + ", despacho " + usuario.getDespacho().getNombreCorto() + " cerro la sesion " + sesion.getId());
    } catch (IOException ex) {
      ex.printStackTrace();
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
      FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
      Logs.log.info("Se intento entrar al modulo '" + modulo + "' sin haber ingresado al sistema. Ha sido redirigido");
    } catch (IOException ex) {
      Logs.log.info("Intrusion en el modulo: '" + modulo + "'. El intruso no logro ser redirigido");
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
          Logs.log.info("El gestor " + usuario.getNombreLogin() + " del despacho " + usuario.getDespacho().getNombreCorto() + " intento entrar al modulo administrativo: '" + modulo + "'. Ha sido redirigido");
        } catch (IOException ex) {
          Logs.log.warn("El gestor " + usuario.getNombreLogin() + " del despacho " + usuario.getDespacho().getNombreCorto() + " accedio al modulo administrativo: '" + modulo + "'. Fallo el redireccionamiento");
          ex.printStackTrace();
        }
      }
    }
  }

  // METODO QUE VERIFICA SI EXISTE UNA SESION ANTERIOR
  public boolean verificarSesionAnterior(HttpSession sesion, String tipoUsuario) {
    boolean tieneSesion = false;
    if (!usuariosEnLinea.isEmpty()) {
      for (int i = 0; i < (usuariosEnLinea.size()); i++) {
        // SI EL USUARIO ESTA Y LA SESION ES LA MISMA
        if ((usuariosEnLinea.get(i).getUsuarioSesion().getNombreLogin().equals(usuario.getNombreLogin())) && (usuariosEnLinea.get(i).getIdSesion()).equals(sesion.getId())) {
          Logs.log.warn("El usuario " + usuario.getNombreLogin() + " del despacho " + usuario.getDespacho().getNombreCorto() + " inicio otra sesion en su misma computadora.");
        } // SI EL USUARIO ESTA PERO LA SESION ES DIFERENTE
        else if ((usuariosEnLinea.get(i).getUsuarioSesion().getNombreLogin().equals(usuario.getNombreLogin())) && !(usuariosEnLinea.get(i).getIdSesion()).equals(sesion.getId())) {
          Logs.log.warn("El usuario " + usuario.getNombreLogin() + " del despacho " + usuario.getDespacho().getNombreCorto() + "intento iniciar una sesion adicional.");
          tieneSesion = true;
          break;
        } // SI EL USUARIO NO ESTA (NI LA SESION NI EL USUARIO)
        else {
          Sesion s = new Sesion();
          s.setIdSesion(sesion.getId());
          s.setUsuarioSesion(usuario);
          usuariosEnLinea.add(s);
          Logs.log.info("Acceso " + tipoUsuario + ": " + nombreUsuario + ", despacho " + usuario.getDespacho().getNombreCorto() + ", sesion " + sesion.getId());
        }
      }
    } else {
      // SI EL USUARIO NO ESTA (NI LA SESION NI EL USUARIO)
      Sesion s = new Sesion();
      s.setIdSesion(sesion.getId());
      s.setUsuarioSesion(usuario);
      usuariosEnLinea.add(s);
      Logs.log.info("Acceso " + tipoUsuario + ": " + nombreUsuario + ", despacho " + usuario.getDespacho().getNombreCorto() + ", sesion " + sesion.getId());
    }
    return tieneSesion;
  }

  // METODO QUE ELIMINA AL USUARIO QUE CIERRA SESION DE LA LISTA DE USUARIOS ACTIVOS
  public boolean eliminarSesion(Sesion s) {
    boolean ok = false;
    for (int i = 0; i < (usuariosEnLinea.size()); i++) {
      if (usuariosEnLinea.get(i).getUsuarioSesion().getNombreLogin().equals(usuario.getNombreLogin())) {
        if (usuariosEnLinea.remove(i) != null) {
          ok = true;
        }
      }
    }
    return ok;
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

  // CLASE MIEMBRO QUE SE ENCARGARA DE GUARDAR A LOS USUARIOS QUE ESTEN EL EL SISTEMA
  public static class Sesion {

    //  VARIABLES DE CLASE
    private Usuario usuarioSesion;
    private String idSesion;

    // CONSTRUCTOR
    public Sesion() {
      usuarioSesion = new Usuario();
    }

    // GETTERS & SETTERS
    public Usuario getUsuarioSesion() {
      return usuarioSesion;
    }

    public void setUsuarioSesion(Usuario usuarioSesion) {
      this.usuarioSesion = usuarioSesion;
    }

    public String getIdSesion() {
      return idSesion;
    }

    public void setIdSesion(String idSesion) {
      this.idSesion = idSesion;
    }

  }

}
