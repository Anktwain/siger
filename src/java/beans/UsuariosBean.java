package beans;

import dao.AdministradorDAO;
import dao.GestorDAO;
import dao.UsuarioDAO;
import dto.Administrativo;
import dto.Gestor;
import dto.Usuario;
import impl.AdministradorIMPL;
import impl.GestorIMPL;
import impl.UsuarioIMPL;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import util.MD5;
import util.constantes.Directorios;
import util.constantes.Patrones;
import util.constantes.Perfiles;
import util.log.Logs;

/**
 * La clase {@code UsuariosBean} permite ... y es el bean correspondiente a la
 * vista {@code usuarios.xhtml}
 *
 * @author
 * @author
 * @author Cofradia
 * @since SigerWeb2.0
 */
@ManagedBean(name = "usuariosBean")
@ViewScoped
public class UsuariosBean implements Serializable {

  /**
   * Objeto DTO donde se almacena TEMPORALMENTE la información con la que se
   * dará de alta a un nuevo usuario en el sistema.
   */
  private Usuario usuario;
  /**
   * Objeto DTO donde se almacena la información completa del usuario que se
   * seleccionará en la tabla.
   */
  private Usuario usuarioSeleccionado;
  /**
   * Objeto DTO que contiene la información que se desplegará después de
   * seleccionar un usuario de la tabla.
   */
  private Usuario usuarioEnDespliegue;

  /**
   * Objeto DAO que se usa para acceder a las operaciones sobre la informacion
   * correspondiente al usuario en la base de datos.
   */
  private final UsuarioDAO usuarioDao;

  private int perfil;
  private String nombre;
  private String paterno;
  private String materno;
  private String nombreLogin;
  private String password;
  private String confirmePassword;
  private String correo;
  private String imagenPerfil;
  private String extension;

//  private SesionBean sesion;
//  private final String rutaImPerfil = Directorios.RUTA_IMAGENES_DE_PERFIL;
//  private List<PerfilBean> listaPerfiles;
  /**
   * Lista que almacena a los gestores que no han sido confirmados en el sistema
   * y que se desplegarán en la vista.
   */
  private List<Usuario> gestoresNoConfirmados;
  /**
   * Lista que almacena a los usuarios que coinciden con algún criterio de
   * búsqueda solicitada desde la vista.
   */
  private List<Usuario> usuariosEncontrados;
  /**
   * Lista de los gestores que se van a confirmar según la solicitud hecha en la
   * vista.
   */
  private List<Usuario> gestoresPorConfirmar;
  /**
   * Lista de todos los usuarios dados de alta en el sistema. Incluye
   * administradores del sistema, gestores confirmados y no confirmados
   */
  private List<Usuario> todosUsuarios;
  /**
   * Atributo que contiene al <strong>objeto PerfilBean</strong> del usuario
   * seleccionado, descrito por su nombre de perfil y su clave numérica
   * correspondiente.
   */
  private PerfilBean perfilBean;

  /**
   * Constructor por defecto. <br/>
   * Configura ...
   */
  public UsuariosBean() {
    usuarioDao = new UsuarioIMPL();
    usuario = new Usuario();
    perfil = Perfiles.GESTOR_NO_CONFIRMADO;
    perfilBean = new PerfilBean();
    imagenPerfil = Directorios.RUTA_IMAGENES_DE_PERFIL + "sin.png";
    gestoresNoConfirmados = usuarioDao.buscarUsuariosNoConfirmados();
    todosUsuarios = usuarioDao.buscarTodo();
//    usuarioSeleccionado = new Usuario();
    imprimirUsuarios();
  }

  /**
   * Funcion de depuración.
   */
  public void imprimirUsuarios() {
    Logs.log.info("*********************** todosUsuario: *********************");
    int i = 0;
    for (Usuario usuarioActual : todosUsuarios) {
      Logs.log.info("[" + usuarioActual.toString() + "]: " + "U." + (++i) + " - "
              + usuarioActual.getNombre() + " " + usuarioActual.getPaterno() + " " + usuarioActual.getMaterno()
              + " " + usuarioActual.getImagenPerfil());
    }
    Logs.log.info("***********************              *********************");

  }

  /**
   *
   * @throws java.io.IOException
   */
  public void insertar() throws IOException {
    // Valida correo
    if (!validarCorreo()) {
      FacesContext.getCurrentInstance().addMessage(null,
              new FacesMessage(FacesMessage.SEVERITY_ERROR,
                      "No se pudo agregar el usuario", "El formato de correo electrónico no es válido."));
    } else if (!passwordsCoinciden()) {
      FacesContext.getCurrentInstance().addMessage(null,
              new FacesMessage(FacesMessage.SEVERITY_ERROR,
                      "No se pudo agregar el usuario", "Las contraseñas no coinciden, vuelva a escribirlas."));
    } else if (!nombreLoginEsUnico()) {
      FacesContext.getCurrentInstance().addMessage(null,
              new FacesMessage(FacesMessage.SEVERITY_ERROR,
                      "No se pudo agregar el usuario", "El nombre de usuario ya existe, elija otro."));
    } else if (!correoEsUnico()) {
      FacesContext.getCurrentInstance().addMessage(null,
              new FacesMessage(FacesMessage.SEVERITY_ERROR,
                      "No se pudo agregar el usuario", "La dirección de correo electrónico ya se encuentra registrada."));
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
    if (usuarioDao.buscarNombreLogin(nombreLogin) != null) {
      // Error: El usuario ya existe
      return false;
    }
    return true;
  }

  /**
   *
   *
   * @return
   */
  private boolean correoEsUnico() {
    if (usuarioDao.buscarCorreo(correo) != null) {
      return false;
    }
    return true;
  }

  /**
   *
   *
   * @throws IOException
   */
  private void insertarUsuario() throws IOException {
    // Crea objeto de tipo Usuario:
    usuario.setNombre(nombre);
    usuario.setPaterno(paterno);
    usuario.setMaterno(materno);
    usuario.setNombreLogin(nombreLogin);
    usuario.setPassword(MD5.encriptar(password));
    usuario.setPerfil(perfil);
    usuario.setCorreo(correo);
    usuario.setImagenPerfil(imagenPerfil);
    // Guarda el objeto en la BD
    if (usuarioDao.insertar(usuario) == 0) { // error al guardar
      FacesContext.getCurrentInstance().addMessage(null,
              new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo agregar el usuario",
                      "Error al guardar en BD, notifíquelo a un administrador."));
    } else {
      insertarUsuarioPorPerfil();
    }
  }

  /**
   *
   *
   *
   */
  private void insertarUsuarioPorPerfil() {
    Gestor gestor = new Gestor();
    Administrativo administrador = new Administrativo();
    GestorDAO gestorDao = new GestorIMPL();
    AdministradorDAO administradorDao = new AdministradorIMPL();
    // Busca al usuario recién ingresado y guarda un objeto de tipo Gestor o Administrador, según corresponda
    usuario = usuarioDao.buscarPorCorreo(usuario.getNombreLogin(), usuario.getCorreo());
    if (usuario.getPerfil() == Perfiles.ADMINISTRADOR) {
      administrador.setUsuario(usuario);
      administradorDao.insertar(administrador);
      Logs.log.info("#################### Administrador ha agregado al administrador: " + usuario.getIdUsuario());
      FacesContext.getCurrentInstance().addMessage(null,
              new FacesMessage(FacesMessage.SEVERITY_INFO, "Se agregó un nuevo usuario",
                      "Se ha agregó correctamente: " + usuario.getNombre()
                      + " " + usuario.getPaterno() + " " + usuario.getMaterno()));
    }
    if (usuario.getPerfil() == Perfiles.GESTOR) {
      gestor.setUsuario(usuario);
      gestor.setExtension(extension);
      gestorDao.insertar(gestor);
      Logs.log.info("#################### Administrador ha agregado al gestor: " + usuario.getIdUsuario());
      FacesContext.getCurrentInstance().addMessage(null,
              new FacesMessage(FacesMessage.SEVERITY_INFO, "Se agregó un nuevo usuario",
                      "Se ha agregó correctamente: " + usuario.getNombre()
                      + " " + usuario.getPaterno() + " " + usuario.getMaterno()));
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
   * @param usuariosSeleccionados
   */
  public void confirmarGestores(List<Usuario> usuariosSeleccionados) {
    for (int i = 0; i < (usuariosSeleccionados.size()); i++) {
      // CAMBIAR SU PERFIL EN LA BASE DE DATOS. DE -2 A 2            
      usuario = usuariosSeleccionados.get(i);
      usuario.setPerfil(Perfiles.GESTOR);
      usuarioDao.editar(usuario);
      if (usuarioDao.editar(usuario) == false) { // error al guardar
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo confirmar el usuario",
                        "Error al guardar en BD, notifíquelo a un administrador."));
      } else {
        // SE INSERTA EL GESTOR EN LA TABLA GESTORES
        insertarGestor(usuario);
        // SE BORRA EL CONFIRMADO DE LA LISTA PARA CONFIRMAR
        gestoresNoConfirmados.remove(usuario);
        RequestContext.getCurrentInstance().update("noConfirmados");
        RequestContext.getCurrentInstance().update("circulito");
        //LOG
        Logs.log.info("#################### Se confirmo al gestor con id " + usuario.getIdUsuario());
        // MENSAJE EN VISTA
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Se confirmo un nuevo usuario",
                        "Se ha confirmado al usuario: " + usuario.getNombre()
                        + " " + usuario.getPaterno() + " " + usuario.getMaterno()));
        // AVISO
        Logs.log.debug("************ CONSOLA SIGERWEB ****************");
        Logs.log.debug("Se ha confirmado al usuario: " + usuariosSeleccionados.get(i).getNombreLogin());
        RequestContext.getCurrentInstance().update("noConfirmados");
        RequestContext.getCurrentInstance().update("circulito");
      }
    }
  }

  /**
   *
   *
   * @param usuariosSeleccionados
   */
  public void eliminarGestores(List<Usuario> usuariosSeleccionados) {
    for (int i = 0; i < (usuariosSeleccionados.size()); i++) {
      usuario = usuariosSeleccionados.get(i);
      usuarioDao.eliminar(usuario);
      if (usuarioDao.eliminar(usuario) == false) { // error al borrar
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo borrar el usuario",
                        "Error al guardar en BD, notifíquelo a un administrador."));
      } else {
        RequestContext.getCurrentInstance().update("noConfirmados");
        RequestContext.getCurrentInstance().update("circulito");
        //LOG
        Logs.log.info("#################### Se elimino al gestor con id " + usuario.getIdUsuario());
        // MENSAJE EN VISTA
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Se elimino el usuario",
                        "Se ha eliminado al usuario: " + usuario.getNombre()
                        + " " + usuario.getPaterno() + " " + usuario.getMaterno()));
        // AVISO
        Logs.log.debug("************ CONSOLA SIGERWEB ****************");
        Logs.log.debug("Se ha eliminado al usuario: " + usuariosSeleccionados.get(i).getNombreLogin());
        RequestContext.getCurrentInstance().update("noConfirmados");
        RequestContext.getCurrentInstance().update("circulito");
      }
    }
  }

  /**
   *
   *
   * @param
   */
  private void insertarGestor(Usuario usuario) {
    Gestor gestor = new Gestor();
    GestorDAO gestorDao = new GestorIMPL();
    gestor.setUsuario(usuario);
    gestor.setExtension(extension);
    gestorDao.insertar(gestor);
    Logs.log.info("#################### Administrador ha agregado al gestor: " + usuario.getIdUsuario());
    FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Se agregó un nuevo gestor",
                    "Se ha agregado correctamente."));
  }

  /**
   *
   *
   * @param event
   */
  public void onRowSelect(SelectEvent event) {
    Logs.log.debug("*********************** Se selecciono un registro");
  }

  /**
   *
   *
   * @param event
   */
  public void onRowUnselect(UnselectEvent event) {
    Logs.log.debug("*********************** Se deselecciono un registro");
  }

  public void desplegarUsuario() {

  }

  public void limpiarBean() {

  }

  /**
   * ************************** Getters y setters **************************
   */
  public PerfilBean getPerfilBean() {
    return perfilBean;
  }

  public void setPerfilBean(PerfilBean perfilBean) {
    this.perfilBean = perfilBean;
  }

  /**
   *
   *
   * @return
   */
  public List<Usuario> getTodosUsuarios() {
    return todosUsuarios;
  }

  /**
   *
   *
   * @return
   */
  public Usuario getUsuarioSeleccionado() {
    return usuarioSeleccionado;
  }

  /**
   *
   *
   * @param uSelec
   */
  public void setUsuarioSeleccionado(Usuario uSelec) {

    Logs.log.info("setUsuarioSeleccionado{");
    Logs.log.info("[" + uSelec.toString() + "] <- uSelec: " + uSelec.getNombre() + " "
            + uSelec.getPaterno() + " " + uSelec.getMaterno() + " <" + uSelec.getNombreLogin()
            + "> P:" + uSelec.getPerfil() + " " + uSelec.getCorreo() + " " + uSelec.getImagenPerfil());
    if (this.usuarioSeleccionado == null) {
      this.usuarioSeleccionado = new Usuario();
    } else {
      this.usuarioSeleccionado.setAdministrativos(uSelec.getAdministrativos());
      this.usuarioSeleccionado.setCorreo(uSelec.getCorreo());
      this.usuarioSeleccionado.setGestors(uSelec.getGestors());
      this.usuarioSeleccionado.setImagenPerfil(uSelec.getImagenPerfil());
      this.usuarioSeleccionado.setMaterno(uSelec.getMaterno());
      this.usuarioSeleccionado.setNombre(uSelec.getNombre());
      this.usuarioSeleccionado.setNombreLogin(uSelec.getNombreLogin());
      this.usuarioSeleccionado.setPassword(uSelec.getPassword());
      this.usuarioSeleccionado.setPaterno(uSelec.getPaterno());
      this.usuarioSeleccionado.setPerfil(uSelec.getPerfil());
    }
//    this.usuarioSeleccionado = usuarioSeleccionado;
    imprimirUsuarios();
    Logs.log.info("[" + this.usuarioSeleccionado.toString() + "] <- usuarioSeleccionado: "
            + this.usuarioSeleccionado.getNombre() + " " + this.usuarioSeleccionado.getPaterno()
            + " " + this.usuarioSeleccionado.getMaterno() + " <"
            + this.usuarioSeleccionado.getNombreLogin() + "> P:" + this.usuarioSeleccionado.getPerfil()
            + " " + this.usuarioSeleccionado.getCorreo() + " " + this.usuarioSeleccionado.getImagenPerfil());
    Logs.log.info("}\n");
  }

  /**
   *
   *
   * @param todosUsuarios
   */
  public void setTodosUsuarios(List<Usuario> todosUsuarios) {
    this.todosUsuarios = todosUsuarios;
  }

  /**
   *
   *
   * @return
   */
  public List<Usuario> getGestoresNoConfirmados() {
    return gestoresNoConfirmados;
  }

  /**
   *
   *
   * @param gestoresNoConfirmados
   *
   */
  public void setGestoresNoConfirmados(List<Usuario> gestoresNoConfirmados) {
    this.gestoresNoConfirmados = gestoresNoConfirmados;
  }

  /**
   *
   *
   * @return
   */
  public List<Usuario> getUsuariosEncontrados() {
    return usuariosEncontrados;
  }

  /**
   *
   *
   * @param usuariosEncontrados
   */
  public void setUsuariosEncontrados(List<Usuario> usuariosEncontrados) {
    this.usuariosEncontrados = usuariosEncontrados;
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
   * @return
   */
  public List<Usuario> getGestoresPorConfirmar() {
    return gestoresPorConfirmar;
  }

  /**
   *
   *
   * @param usuariosSeleccionados
   */
  public void setGestoresPorConfirmar(List<Usuario> usuariosSeleccionados) {
    this.gestoresPorConfirmar = usuariosSeleccionados;
  }

  /**
   *
   *
   * @param usuario
   */
  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  /**
   *
   *
   * @return
   */
  public int getPerfil() {
    return perfil;
  }

  /**
   *
   *
   * @param perfil
   */
  public void setPerfil(int perfil) {
    this.perfil = perfil;
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

  /**
   *
   *
   * @return
   */
  public String getExtension() {
    return extension;
  }

  /**
   *
   *
   * @param extension
   */
  public void setExtension(String extension) {
    this.extension = extension;
  }

//  public SesionBean getSesion() {
//    return sesion;
//  }
//
//  public void setSesion(SesionBean sesion) {
//    this.sesion = sesion;
//  }
//
//  public List<PerfilBean> getListaPerfiles() {
//    return listaPerfiles;
//  }
//
//  public void setListaPerfiles(List<PerfilBean> listaPerfiles) {
//    this.listaPerfiles = listaPerfiles;
//  }
//
//  public String getRutaImPerfil() {
//    return rutaImPerfil;
//  }
}
