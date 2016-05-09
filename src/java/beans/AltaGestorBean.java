package beans;

import dao.DespachoDAO;
import dao.UsuarioDAO;
import dto.Despacho;
import dto.Usuario;
import impl.DespachoIMPL;
import impl.UsuarioIMPL;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import util.MD5;
import util.constantes.Directorios;
import util.constantes.Patrones;
import util.constantes.Perfiles;
import util.log.Logs;

/**
 * La clase {@code AltaGestorBean} almacena la información de la vista
 * {@code AltaGestor.xhtml} para dar de alta a un nuevo gestor que,
 * posteriormente, deberá ser confirmado por un administrador.
 *
 * @author brionvega
 * @since SigerWeb2.0
 */
@ManagedBean
@ViewScoped
public class AltaGestorBean implements Serializable {

  private int perfil;

  private String nombre;
  private String paterno;
  private String materno;
  private String nombreLogin;
  private String password;
  private String confirmePassword;
  private String correo;
  private String imagenPerfil;
  private int idDespacho;
  private Usuario usuario;
  private final DespachoDAO despachoDao;
  private final UsuarioDAO usuarioDao;

  /**
   *
   */
  public AltaGestorBean() {
    despachoDao = new DespachoIMPL();
    usuarioDao = new UsuarioIMPL();
    perfil = Perfiles.GESTOR_NO_CONFIRMADO;
    imagenPerfil = Directorios.RUTA_IMAGENES_DE_PERFIL + "sin.png";
  }

  /**
   *
   * @throws IOException
   */
  public void insertar() throws IOException {
    // Valida correo
    if (!validarCorreo()) {
      FacesContext context = FacesContext.getCurrentInstance();
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo agregar el usuario",
              "El formato de correo electrónico no es válido."));
    } else if (!passwordsCoinciden()) {
      FacesContext context = FacesContext.getCurrentInstance();
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo agregar el usuario",
              "Las contraseñas no coinciden, vuelva a escribirlas."));
    } else if (!nombreLoginEsUnico()) {
      FacesContext context = FacesContext.getCurrentInstance();
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo agregar el usuario",
              "El nombre de usuario ya existe, elija otro."));
    } else if (!correoEsUnico()) {
      FacesContext context = FacesContext.getCurrentInstance();
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo agregar el usuario",
              "La dirección de correo electrónico ya se encuentra registrada."));
    } else if (!despachoExiste()) {
      FacesContext context = FacesContext.getCurrentInstance();
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo agregar el usuario",
              "La clave de despacho proporcionada no es valida."));
    } else {
      insertarUsuario();
    }
  }

  /**
   *
   *
   * @return
   */
  private boolean nombreLoginEsUnico() {
    return (usuarioDao.buscarNombreLogin(nombreLogin) == null);
  }

  /**
   *
   *
   * @return
   */
  private boolean correoEsUnico() {
    return (usuarioDao.buscarCorreo(correo) == null);
  }

  /**
   *
   *
   * @return
   */
  private boolean despachoExiste() {
    boolean ok = false;
    List<Despacho> despachos = despachoDao.getAll();
    for (int i = 0; i < (despachos.size()); i++) {
      if (despachos.get(i).getIdDespacho() == idDespacho) {
        ok = true;
      }
    }
    return ok;
  }

  /**
   *
   *
   *
   */
  private void insertarUsuario(){
    // Crea objeto de tipo Usuario:
    usuario = new Usuario();
    usuario.setNombre(nombre);
    usuario.setPaterno(paterno);
    usuario.setMaterno(materno);
    usuario.setNombreLogin(nombreLogin);
    usuario.setPassword(MD5.encriptar(password));
    usuario.setPerfil(perfil);
    usuario.setCorreo(correo);
    usuario.setImagenPerfil(imagenPerfil);
    usuario.setDespacho(despachoDao.buscarPorId(idDespacho));
    // Guarda el objeto en la BD
    if (usuarioDao.insertar(usuario) == 0) { // error al guardar
      FacesContext context = FacesContext.getCurrentInstance();
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "No se pudo agregar el usuario",
              "Error al guardar en BD, repórtese con Soporte Técnico."));
    } else {
      FacesContext context = FacesContext.getCurrentInstance();
      ExternalContext externalContext = context.getExternalContext();
      context.addMessage(null, new FacesMessage("Operacion exitosa",
              "Se agrego el usuario correctamente al sistema. Espere instrucciones del administrador."));
      externalContext.getFlash().setKeepMessages(true);
      try {
        FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
      } catch (IOException ioe) {
        Logs.log.error("No se pudo redirigir al index");
        Logs.log.error(ioe);
      }
    }
  }

  /**
   *
   *
   * @return
   */
  private boolean validarCorreo() {
    // Compila la cadena PATRON_EMAIL como un patrón
    Pattern patron = Pattern.compile(Patrones.PATRON_EMAIL);

    // Compara el correo con el patrón dado
    Matcher matcher = patron.matcher(correo);

    return matcher.matches();
  }

  /**
   *
   *
   * @return
   */
  private boolean passwordsCoinciden() {
    return password.equals(confirmePassword);
  }

  /**
   *
   *
   * @return
   */
  public String getNombre() {
    return nombre;
  }

  /**
   *
   *
   * @param nombre
   */
  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  /**
   *
   *
   * @return
   */
  public String getPaterno() {
    return paterno;
  }

  /**
   *
   *
   * @param paterno
   */
  public void setPaterno(String paterno) {
    this.paterno = paterno;
  }

  /**
   *
   *
   * @return
   */
  public String getMaterno() {
    return materno;
  }

  /**
   *
   *
   * @param materno
   */
  public void setMaterno(String materno) {
    this.materno = materno;
  }

  /**
   *
   *
   * @return
   */
  public String getNombreLogin() {
    return nombreLogin;
  }

  /**
   *
   *
   * @param nombreLogin
   */
  public void setNombreLogin(String nombreLogin) {
    this.nombreLogin = nombreLogin;
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
  public String getConfirmePassword() {
    return confirmePassword;
  }

  /**
   *
   *
   * @param confirmePassword
   */
  public void setConfirmePassword(String confirmePassword) {
    this.confirmePassword = confirmePassword;
  }

  /**
   *
   *
   * @return
   */
  public String getCorreo() {
    return correo;
  }

  /**
   *
   *
   * @param correo
   */
  public void setCorreo(String correo) {
    this.correo = correo;
  }

  /**
   *
   *
   * @return
   */
  public String getImagenPerfil() {
    return imagenPerfil;
  }

  /**
   *
   *
   * @param imagenPerfil
   */
  public void setImagenPerfil(String imagenPerfil) {
    this.imagenPerfil = imagenPerfil;
  }

  public int getIdDespacho() {
    return idDespacho;
  }

  public void setIdDespacho(int idDespacho) {
    this.idDespacho = idDespacho;
  }

}
